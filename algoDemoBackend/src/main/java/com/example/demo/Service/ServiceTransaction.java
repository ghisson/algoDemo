package com.example.demo.Service;

import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.example.demo.Util.ClientAlgo;
import com.example.demo.Util.Wallet;

import java.security.GeneralSecurityException;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.crypto.Address;
import com.algorand.algosdk.kmd.client.ApiException;
import com.algorand.algosdk.kmd.client.KmdClient;
import com.algorand.algosdk.kmd.client.api.KmdApi;
import com.algorand.algosdk.kmd.client.model.*;
import com.algorand.algosdk.transaction.SignedTransaction;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.util.CryptoProvider;
import com.algorand.algosdk.util.Encoder;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.common.Client;
import com.algorand.algosdk.v2.client.common.IndexerClient;
import com.algorand.algosdk.v2.client.common.Response;
import com.algorand.algosdk.v2.client.model.NodeStatusResponse;
import com.algorand.algosdk.v2.client.model.PendingTransactionResponse;
import com.algorand.algosdk.v2.client.model.PostTransactionsResponse;
import com.algorand.algosdk.v2.client.model.TransactionParametersResponse;
import com.algorand.algosdk.v2.client.model.TransactionsResponse;

import com.algorand.algosdk.v2.client.algod.HealthCheck;
import com.algorand.algosdk.v2.client.algod.Metrics;
import com.algorand.algosdk.v2.client.algod.GetGenesis;
import com.algorand.algosdk.v2.client.algod.SwaggerJSON;
import com.algorand.algosdk.v2.client.algod.GetVersion;
import com.algorand.algosdk.v2.client.algod.AccountInformation;
import com.algorand.algosdk.v2.client.algod.AccountAssetInformation;
import com.algorand.algosdk.v2.client.algod.AccountApplicationInformation;
import com.algorand.algosdk.v2.client.algod.GetPendingTransactionsByAddress;
import com.algorand.algosdk.v2.client.algod.GetBlock;
import com.algorand.algosdk.v2.client.algod.GetBlockHash;
import com.algorand.algosdk.v2.client.algod.GetTransactionProof;
import com.algorand.algosdk.v2.client.algod.GetSupply;
import com.algorand.algosdk.v2.client.algod.GetStatus;
import com.algorand.algosdk.v2.client.algod.WaitForBlock;
import com.algorand.algosdk.v2.client.algod.RawTransaction;
import com.algorand.algosdk.v2.client.algod.TransactionParams;
import com.algorand.algosdk.v2.client.algod.GetPendingTransactions;
import com.algorand.algosdk.v2.client.algod.PendingTransactionInformation;
import com.algorand.algosdk.v2.client.algod.GetStateProof;
import com.algorand.algosdk.v2.client.algod.GetLightBlockHeaderProof;
import com.algorand.algosdk.v2.client.algod.GetApplicationByID;
import com.algorand.algosdk.v2.client.algod.GetApplicationBoxes;
import com.algorand.algosdk.v2.client.algod.GetApplicationBoxByName;
import com.algorand.algosdk.v2.client.algod.GetAssetByID;
import com.algorand.algosdk.v2.client.algod.TealCompile;
import com.algorand.algosdk.v2.client.algod.TealDisassemble;
import com.algorand.algosdk.v2.client.algod.TealDryrun;
import com.algorand.algosdk.crypto.Address;

@Service
public class ServiceTransaction {

	private AlgodClient algod;
	private Account wallet;
	
	public ServiceTransaction() throws GeneralSecurityException {
		algod=ClientAlgo.getClient();
		wallet=Wallet.getAccount();
	}
	
	
	public String sendTransaction(String note) {
		try {
			// Construct the transaction
	        String RECEIVER =wallet.getAddress().toString();
	       
	        Response<TransactionParametersResponse> resp =
	        algod.TransactionParams().execute();
	        if (!resp.isSuccessful()) {
	        	throw new Exception(resp.message());
	        }
	        TransactionParametersResponse params = resp.body();
	        if (params == null) {
	        	throw new Exception("Params retrieval error");
	        }
	        Transaction txn = Transaction.PaymentTransactionBuilder()
		        .sender(wallet.getAddress())
		        .note(note.getBytes())
		        .amount(1)
		        .receiver(new Address(RECEIVER))
		        .suggestedParams(params)
		        .build();
	
	        // sign the transaction
	        SignedTransaction signedTxn = wallet.signTransaction(txn);
	        System.out.println("Signed transaction with txid: " +
	        signedTxn.transactionID);
	
	        // Submit the transaction to the network
	        byte[] encodedTxBytes = Encoder.encodeToMsgPack(signedTxn);
	        Response<PostTransactionsResponse> rawtxresponse =
	        algod.RawTransaction().rawtxn(encodedTxBytes).execute();
	        if (!rawtxresponse.isSuccessful()) {
	        	throw new Exception(rawtxresponse.message());
	        }
	        String id = rawtxresponse.body().txId;
	        System.out.println("Successfully sent tx with ID: " + id);
	
	        //wait for confirm from the network
	        PendingTransactionResponse pTrx = waitForConfirmation(algod, id, 4);
	
	        System.out.println("Transaction " + id + " confirmed in round " +
	        pTrx.confirmedRound);
	        // Read the transaction
	        JSONObject jsonObj = new JSONObject(pTrx.toString());
	        System.out.println("Transaction information (with notes): " +
	        jsonObj.toString(2));
	        System.out.println("Decoded note: " + new String(pTrx.txn.tx.note));
	        
	        return id;
		}catch(Exception e) {
			return "";
		}
		
		
	}
	
	
	
	private PendingTransactionResponse waitForConfirmation(AlgodClient myclient, String txID, Integer timeout)
            throws Exception {
        if (myclient == null || txID == null || timeout < 0) {
            throw new IllegalArgumentException("Bad arguments for waitForConfirmation.");
        }
        Response<NodeStatusResponse> resp = myclient.GetStatus().execute();
        if (!resp.isSuccessful()) {
            throw new Exception(resp.message());
        }
        NodeStatusResponse nodeStatusResponse = resp.body();
        Long startRound = nodeStatusResponse.lastRound + 1;
        Long currentRound = startRound;
        while (currentRound < (startRound + timeout)) {
            // Check the pending transactions
            Response<PendingTransactionResponse> resp2 = myclient.PendingTransactionInformation(txID).execute();
            if (resp2.isSuccessful()) {
                PendingTransactionResponse pendingInfo = resp2.body();
                if (pendingInfo != null) {
                    if (pendingInfo.confirmedRound != null && pendingInfo.confirmedRound > 0) {
                        // Got the completed Transaction
                        return pendingInfo;
                    }
                    if (pendingInfo.poolError != null && pendingInfo.poolError.length() > 0) {
                        // If there was a pool error, then the transaction has been rejected!
                        throw new Exception(
                                "The transaction has been rejected with a pool error: " + pendingInfo.poolError);
                    }
                }
            }
            resp = myclient.WaitForBlock(currentRound).execute();
            if (!resp.isSuccessful()) {
                throw new Exception(resp.message());
            }
            currentRound++;
        }
        throw new Exception("Transaction not confirmed after " + timeout + " rounds!");
    }
	
	
}
