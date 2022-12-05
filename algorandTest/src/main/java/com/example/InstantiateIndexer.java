package com.example;

import com.algorand.algosdk.v2.client.common.IndexerClient;
import com.algorand.algosdk.v2.client.common.Client;


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

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class InstantiateIndexer {
    public Client indexerInstance = null;
    // utility function to connect to a node
    private Client connectToNetwork(){
        final String INDEXER_API_ADDR = "localhost";
        final int INDEXER_API_PORT = 8980;       
        IndexerClient indexerClient = new IndexerClient(INDEXER_API_ADDR, INDEXER_API_PORT); 
        return indexerClient;
    }
    public static void main(String args[]) throws Exception {
        InstantiateIndexer ex = new InstantiateIndexer();
        IndexerClient indexerClientInstance = (IndexerClient)ex.connectToNetwork();
        System.out.println(indexerClientInstance.searchForAccounts().toString());
        System.out.println("IndexerClient Instantiated : " + indexerClientInstance); // pretty print json

        String txID="JM24OIEPU7AOTYOF6Z42LHMCS5GSDKQR6Z7WU2OWDDLCYSTQD5ZQ";
        Response<TransactionsResponse> transactions = indexerClientInstance.searchForTransactions()
                .txid(txID)
                .execute();

        if (!transactions.isSuccessful()) {
            throw new RuntimeException("Failed to lookup transaction");
        }

        System.out.println("Transaction received! \n" + transactions.body().toString());
    }
}