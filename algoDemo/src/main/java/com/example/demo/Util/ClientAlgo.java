package com.example.demo.Util;

import com.algorand.algosdk.v2.client.common.AlgodClient;

public class ClientAlgo {
	
	
    private static String token = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
	private static AlgodClient algod;
	
	public static AlgodClient getClient() {
		if(algod==null) {
			algod = new AlgodClient("http://localhost", 4001, token);
			return algod;
		}
		return algod;
	}
}
