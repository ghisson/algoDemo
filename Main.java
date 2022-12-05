package com.example.demo;

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

public class Main {

    private static final String SHA256_ALG = "SHA256";

    public static void printAssetHolding(AlgodClient client,Account account, Long assetID) throws Exception {


        // String myAddress = account.getAddress().toString();
        Response<com.algorand.algosdk.v2.client.model.Account> respAcct = client
                .AccountInformation(account.getAddress()).execute();
        if (!respAcct.isSuccessful()) {
            throw new Exception(respAcct.message());
        }
        com.algorand.algosdk.v2.client.model.Account accountInfo = respAcct.body();
        JSONObject jsonObj = new JSONObject(accountInfo.toString());
        JSONArray jsonArray = (JSONArray) jsonObj.get("assets");
        if (jsonArray.length() > 0)
            try {
                for (Object o : jsonArray) {
                    JSONObject ca = (JSONObject) o;
                    Integer myassetIDInt = (Integer) ca.get("asset-id");
                    if (assetID.longValue() == myassetIDInt.longValue()) {
                        System.out.println("Asset Holding Info: " + ca.toString(2)); // pretty print
                        break;
                    }
                }
            } catch (Exception e) {
                throw (e);
            }
    }


    public static void printCreatedAsset(AlgodClient client, Account account, Long assetID) throws Exception {
        
        // String myAddress = account.getAddress().toString();
        Response<com.algorand.algosdk.v2.client.model.Account> respAcct = client
                .AccountInformation(account.getAddress()).execute();
        if (!respAcct.isSuccessful()) {
            throw new Exception(respAcct.message());
        }
        com.algorand.algosdk.v2.client.model.Account accountInfo = respAcct.body();
        JSONObject jsonObj = new JSONObject(accountInfo.toString());
        JSONArray jsonArray = (JSONArray) jsonObj.get("created-assets");
        if (jsonArray.length() > 0)
            try {
                for (Object o : jsonArray) {
                    JSONObject ca = (JSONObject) o;
                    Integer myassetIDInt = (Integer) ca.get("index");
                    if (assetID.longValue() == myassetIDInt.longValue()) {
                        System.out.println("Created Asset Info: " + ca.toString(2)); // pretty print
                        break;
                    }
                }
            } catch (Exception e) {
                throw (e);
            }
    }

    public static byte[] digest(byte[] data) throws NoSuchAlgorithmException {
        CryptoProvider.setupIfNeeded();
        java.security.MessageDigest digest = java.security.MessageDigest.getInstance(SHA256_ALG);
        digest.update(Arrays.copyOf(data, data.length));
        return digest.digest();
    }


    public static Long createNFTAsset(AlgodClient client,Account aliceAccount) throws Exception {
        System.out.println(String.format(""));
        System.out.println(String.format("==> CREATE ASSET"));    

        // get changing network parameters for each transaction
        Response<TransactionParametersResponse> resp = client.TransactionParams().execute();
        if (!resp.isSuccessful()) {
            throw new Exception(resp.message());
        }
        TransactionParametersResponse params = resp.body();
        if (params == null) {
            throw new Exception("Params retrieval error");
        }
        JSONObject jsonObj = new JSONObject(params.toString());
        System.out.println("Algorand suggested parameters: " + jsonObj.toString(2));

        // Create the Asset:

        boolean defaultFrozen = false;
        String unitName = "SIFNFT3";
        String assetName = "Sif";
        String url = "https://gateway.pinata.cloud/ipfs/QmNprbvr6XB7h8w4hJYxWNqeKNx2Cx5znjijsEkWyQfnQ7";
        byte[] imageFile = Files.readAllBytes(Paths.get(System.getProperty("user.dir") +"/src/main/java/com/example/demo/NFT/sif.png"));
        byte[] imgHash = digest(imageFile); 
        String imgSRI = "sha256-" + Base64.getEncoder().encodeToString(imgHash);
        System.out.println("image_integrity : " + String.format(imgSRI));
        // Use imgSRI as the metadata  for "image_integrity": 
        // "sha256-/tih/7ew0eziEZIVD4qoTWb0YrElAuRG3b40SnEstyk=",

    
        byte[] metadataFILE = Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "/src/main/java/com/example/demo/NFT/metadata.json"));
        // use this to verify that the metadatahash displayed in the asset creation response is correct
        // cat metadata.json | openssl dgst -sha256 -binary | openssl base64 -A     
        byte[] assetMetadataHash = digest(metadataFILE); 
        // String assetMetadataSRI = Base64.getEncoder().encodeToString(assetMetadataHash);
        // System.out.println(String.format(assetMetadataSRI));

       
        String assetMetadataHashString = "4FEUyFv3t0G3GEJTNXy18aWfwN8CcCgRyzpE/F1a58I=";

        // var metadataJSON = {
        //     "name": "ALICEART",
        //     "description": "Alice's Artwork",
        //     "image": "https:\/\/s3.amazonaws.com\/your-bucket\/images\/alice-nft.png",
        //     "image_integrity": "sha256-/tih/7ew0eziEZIVD4qoTWb0YrElAuRG3b40SnEstyk=",
        //     "properties": {
        //         "simple_property": "Alice's first artwork",
        //         "rich_property": {
        //             "name": "AliceArt",
        //             "value": "001",
        //             "display_value": "001",
        //             "class": "emphasis",
        //             "css": {
        //                 "color": "#ffffff",
        //                 "font-weight": "bold",
        //                 "text-decoration": "underline"
        //             }
        //         },
        //         "array_property": {
        //             "name": "Artwork",
        //             "value": [1, 2, 3, 4],
        //             "class": "emphasis"
        //         }
        //     }
        // }


        Address manager = aliceAccount.getAddress();  // OPTIONAL: FOR DEMO ONLY, USED TO DESTROY ASSET WITHIN THIS SCRIPT
        Address reserve = aliceAccount.getAddress();
        Address freeze = aliceAccount.getAddress();
        Address clawback = aliceAccount.getAddress();
        //
        // For non-fractional NFT ASA simply set the 
        // assetTotal to 1 and decimals to 0
   
        // or
        // A fractional non-fungible token (fractional NFT)
        // has the following properties:

        // Total Number of Units (t) MUST be a power of 10 larger than 1: 10, 100, 1000,
        // ...
        // Number of Digits after the Decimal Point MUST be equal to the
        // logarithm in base 10 of total number of units.
        // In other words, the total supply of the ASA is exactly 1.

        // fractional example for total number of this asset available
        // for circulation 100 units of .01 fraction each.
        // fractional example
        // Integer decimals = 2;
        // BigInteger assetTotal = BigInteger.valueOf(100);

        // Use actual total > 1 to create a Fungible Token

        // example 1:(fungible Tokens)
        // assetTotal = 10, decimals = 0, result is 10 total actual

        // example 2: (fractional NFT, each is 0.1)
        // assetTotal = 10, decimals = 1, result is 1.0 total actual

        // example 3: (NFT)
        // assetTotal = 1, decimals = 0, result is 1 total actual

        // set quantity and decimal placement
        BigInteger assetTotal = BigInteger.valueOf(1);
        // decimals and assetTotal
        Integer decimals = 0;

        Transaction tx = Transaction.AssetCreateTransactionBuilder()
                .sender(aliceAccount.getAddress().toString())
                .assetTotal(assetTotal)
                .assetDecimals(decimals)
                .assetUnitName(unitName)
                .assetName(assetName)
                .url(url)
                //.metadataHashB64(assetMetadataHashString)
                .manager(manager)
                .reserve(reserve)
                .freeze(freeze)
                .defaultFrozen(defaultFrozen)
                .clawback(clawback)
                .suggestedParams(params)
                .build();

        // Sign the Transaction with creator account
        SignedTransaction signedTxn = aliceAccount.signTransaction(tx);
        Long assetID = null;
        try {
            // Submit the transaction to the network
            String[] headers = { "Content-Type" };
            String[] values = { "application/x-binary" };
            // Submit the transaction to the network
            byte[] encodedTxBytes = Encoder.encodeToMsgPack(signedTxn);
            Response<PostTransactionsResponse> rawtxresponse = client.RawTransaction().rawtxn(encodedTxBytes)
                    .execute(headers, values);
            if (!rawtxresponse.isSuccessful()) {
                throw new Exception(rawtxresponse.message());
            }
            String id = rawtxresponse.body().txId;

            // Wait for transaction confirmation
            PendingTransactionResponse pTrx = waitForConfirmation(client,id,10);          
            System.out.println("Transaction " + id + " confirmed in round " + pTrx.confirmedRound);

            assetID = pTrx.assetIndex;
            System.out.println("AssetID = " + assetID);
            printCreatedAsset(client,aliceAccount, assetID);
            printAssetHolding(client,aliceAccount, assetID);
            return assetID;
        } catch (Exception e) {
            e.printStackTrace();
            return assetID;
        }

    }



    public static void printBalance(AlgodClient client, Address address, Long assetID) throws Exception {

        String accountInfo = client.AccountInformation(address).execute().toString();
        JSONObject jsonObj = new JSONObject(accountInfo.toString());
        JSONArray jsonArray = (JSONArray) jsonObj.get("assets");
        if (jsonArray.length() > 0) {
            try {
                for (Object o : jsonArray) {
                    JSONObject ca = (JSONObject) o;
                    Integer myassetIDInt = (Integer) ca.get("asset-id");
                    if (assetID.longValue() == myassetIDInt.longValue()) {
                        System.out.println("Asset Holding Info: " + ca.toString(2)); // pretty print
                        break;
                    }
                }
            } catch (Exception e) {
                throw (e);
            }
        }
    }

    public static String submitTransaction(AlgodClient client, SignedTransaction signedTx) throws Exception {
        try {
            // Msgpack encode the signed transaction
            byte[] encodedTxBytes = Encoder.encodeToMsgPack(signedTx);
            String[] headers = { "Content-Type" };
            String[] values = { "application/x-binary" };
            Response<PostTransactionsResponse> rawtxresponse = client.RawTransaction().rawtxn(encodedTxBytes)
                    .execute(headers, values);
            if (!rawtxresponse.isSuccessful()) {
                throw new Exception(rawtxresponse.message());
            }
            String id = rawtxresponse.body().txId;

            // String id =
            // client.RawTransaction().rawtxn(encodedTxBytes).execute().body().txId;
            // ;
            return (id);
        } catch (ApiException e) {
            throw (e);
        }
    }

    public static void printBalance(AlgodClient algod, Account myAccount) throws Exception {

        Response<com.algorand.algosdk.v2.client.model.Account> respAcct = algod
                .AccountInformation(myAccount.getAddress()).execute();
        if (!respAcct.isSuccessful()) {
            throw new Exception(respAcct.message());
        }
        com.algorand.algosdk.v2.client.model.Account accountInfo = respAcct.body();
        System.out.println(String.format("Account Balance: %d microAlgos", accountInfo.amount));

    }

    public static PendingTransactionResponse waitForConfirmation(AlgodClient myclient, String txID, Integer timeout)
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

    private static String token = "b1ad6466589942ea3e161b5292b4a2f40825675ac5ac823bdc8f49246c3ca63c";
    private static KmdApi kmd = null;

    public static void main(String[] args) throws Exception {
        // Initialize algod/indexer v2 clients.
        /*AlgodClient algod = new AlgodClient("http://localhost", 4001, token);
        IndexerClient indexer = new IndexerClient("http://localhost", 8980);
         Account account=new Account();
        System.out.println(account.toMnemonic());
        System.out.println(account.getAddress());*/


        //String PASSPHRASEMAINNET="blanket embody tail staff weird relief vessel powder seven actor bundle favorite burst economy clever anchor exchange emerge security giggle uphold yellow again able luggage";
        // String PASSPHRASE2 = "spy resemble welcome omit favorite raccoon hip dress purse warrior quote wish advance deliver finger oxygen sugar page sunny pole arm special twist absorb mouse";

        // Account myAccount = new Account(PASSPHRASE);
        // Account acc = new Account(PASSPHRASE2);
        // String myAddress = myAccount.getAddress().toString();
        // printBalance(algod, myAccount);

        // Construct the transaction
        // final String RECEIVER =
        // "AG423YYNJR6VR2ZB5FAR3HG57JIKOWF3P756544F5LHQQDFAMLJXIB43LQ";
        // String note = "first transaction";
        // Response<TransactionParametersResponse> resp =
        // algod.TransactionParams().execute();
        // if (!resp.isSuccessful()) {
        // throw new Exception(resp.message());
        // }
        // TransactionParametersResponse params = resp.body();
        // if (params == null) {
        // throw new Exception("Params retrieval error");
        // }
        // Transaction txn = Transaction.PaymentTransactionBuilder()
        // .sender(myAddress)
        // .note(note.getBytes())
        // .amount(100000)
        // .receiver(new Address(RECEIVER))
        // .suggestedParams(params)
        // .build();

        // // sign the transaction
        // SignedTransaction signedTxn = myAccount.signTransaction(txn);
        // System.out.println("Signed transaction with txid: " +
        // signedTxn.transactionID);

        // // Submit the transaction to the network
        // byte[] encodedTxBytes = Encoder.encodeToMsgPack(signedTxn);
        // Response<PostTransactionsResponse> rawtxresponse =
        // algod.RawTransaction().rawtxn(encodedTxBytes).execute();
        // if (!rawtxresponse.isSuccessful()) {
        // throw new Exception(rawtxresponse.message());
        // }
        // String id = rawtxresponse.body().txId;
        // System.out.println("Successfully sent tx with ID: " + id);

        // //wait for confirm from the network
        // PendingTransactionResponse pTrx = waitForConfirmation(algod, id, 4);

        // System.out.println("Transaction " + id + " confirmed in round " +
        // pTrx.confirmedRound);
        // // Read the transaction
        // JSONObject jsonObj = new JSONObject(pTrx.toString());
        // System.out.println("Transaction information (with notes): " +
        // jsonObj.toString(2));
        // System.out.println("Decoded note: " + new String(pTrx.txn.tx.note));
        // printBalance(algod,myAccount);

        // //CREATE FT
        // String creator = myAccount.getAddress().toString();
        // boolean defaultFrozen = false;
        // String unitName = "ALICE";
        // String assetName = "FLAVIO FAI SCHIFO";
        // String url = "https://path/to/my/fungible/asset/metadata.json";
        // Address manager = null;
        // Address reserve = null;
        // Address freeze = null;
        // Address clawback = null;
        // BigInteger assetTotal = BigInteger.valueOf(100); // Fungible tokens have
        // totalIssuance greater than 1
        // Integer decimals = 0; // Fungible tokens typically have decimals greater than
        // 0

        // Response<TransactionParametersResponse> resp =
        // algod.TransactionParams().execute();
        // if (!resp.isSuccessful()) {
        // throw new Exception(resp.message());
        // }
        // TransactionParametersResponse params = resp.body();
        // if (params == null) {
        // throw new Exception("Params retrieval error");
        // }
        // Transaction tx = Transaction.AssetCreateTransactionBuilder()
        // .sender(creator)
        // .assetTotal(assetTotal)
        // .assetDecimals(decimals)
        // .assetUnitName(unitName)
        // .assetName(assetName)
        // .url(url)
        // .manager(manager)
        // .reserve(reserve)
        // .freeze(freeze)
        // .defaultFrozen(defaultFrozen)
        // .clawback(clawback)
        // .suggestedParams(params)
        // .build();

        // SignedTransaction signedTxn = myAccount.signTransaction(tx);
        // System.out.println("Signed transaction with txid: " +
        // signedTxn.transactionID);

        // // Submit the transaction to the network
        // byte[] encodedTxBytes = Encoder.encodeToMsgPack(signedTxn);
        // Response<PostTransactionsResponse> rawtxresponse =
        // algod.RawTransaction().rawtxn(encodedTxBytes).execute();
        // if (!rawtxresponse.isSuccessful()) {
        // throw new Exception(rawtxresponse.message());
        // }
        // String id = rawtxresponse.body().txId;
        // System.out.println("Successfully sent tx with ID: " + id);

        // //wait for confirm from the network
        // PendingTransactionResponse pTrx = waitForConfirmation(algod, id, 4);

        // System.out.println("Transaction " + id + " confirmed in round " +
        // pTrx.confirmedRound);
        // // Read the transaction
        // JSONObject jsonObj = new JSONObject(pTrx.toString());
        // System.out.println("Transaction information (with notes): " +
        // jsonObj.toString(2));
        // System.out.println("Decoded note: " + new String(pTrx.txn.tx.note));
        // printBalance(algod,myAccount);

    //     // TRANSFER ASSET
    //     // Transfer the Asset:
    //     // assetID = Long.valueOf((your asset id));
    //     // get changing network parameters for each transaction
    //     Response<TransactionParametersResponse> resp = algod.TransactionParams().execute();
    //     if (!resp.isSuccessful()) {
    //         throw new Exception(resp.message());
    //     }
    //     TransactionParametersResponse params = resp.body();
    //     if (params == null) {
    //         throw new Exception("Params retrieval error");
    //     }
    //     // params.fee = (long) 1000;
    //     // set asset xfer specific parameters
    //     BigInteger assetAmount = BigInteger.valueOf(10);
    //     Long assetID = new Long("144910342");
    //     Address sender = myAccount.getAddress();
    //     Address receiver = new Address("NUMI62MRF2H6EOF5P566H2BXLQ2V3QHZILHD55TRJSGRMRMDMMPUYPNRXM");
    //     Transaction tx = Transaction.AssetTransferTransactionBuilder()
    //             .sender(sender)
    //             .assetReceiver(receiver)
    //             .assetAmount(assetAmount)
    //             .assetIndex(assetID)
    //             .suggestedParams(params)
    //             .build();
    //     // The transaction must be signed by the sender account
    //     SignedTransaction signedTx = myAccount.signTransaction(tx);
    //     // send the transaction to the network
    //     try {
    //         String id = submitTransaction(algod, signedTx);
    //         System.out.println("Transaction ID: " + id);
    //         PendingTransactionResponse pTrx = waitForConfirmation(algod, id, 4);
    //         System.out.println("Transaction " + id + " confirmed in round " + pTrx.confirmedRound);
    //         // list the account information for acct1 and acct3
    //         System.out.println("Account 3  = " + receiver.toString());
    //         printAssetHolding(algod, receiver, assetID);
    //         System.out.println("Account 1  = " + myAccount.getAddress().toString());
    //         printAssetHolding(algod, myAccount.getAddress(), assetID);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return;
    //     }

        //createNFTAsset(algod,myAccount);

        AlgodClient algod = new AlgodClient("http://127.0.0.1", 8080, token);

        String PASSPHRASE = "make sleep scatter stove author rail truth venture like fever reform fitness dry kit practice once team wrap come arm sentence ghost rail abstract figure";

        Account myAccount=new Account(PASSPHRASE);


       System.out.println(myAccount.getAddress());

       //createNFTAsset(algod,myAccount);
    }

    
}