package com.example.demo.Util;

import java.security.GeneralSecurityException;

import com.algorand.algosdk.account.Account;

public class Wallet {
	
	private static final String mnemonic="spice wash upon maple state income neglect normal face struggle virus arrest flush throw amateur balcony more possible double actual erode health consider absorb page";
	private static Account wallet;
	
	
	public static Account getAccount() throws GeneralSecurityException {
		if(wallet==null) {
			wallet=new Account(mnemonic);
			return wallet;
		}
		return wallet;
	}
	
	

}
