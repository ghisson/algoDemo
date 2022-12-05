package com.example.demo.Util;

import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.common.IndexerClient;

public class ClientIndexer {
	

	private static IndexerClient indexerClient; 
	
	public static IndexerClient getClient() {
		if(indexerClient==null) {
			indexerClient = new IndexerClient("localhost", 8980); 
			return indexerClient;
		}
		return indexerClient;
	}

}
