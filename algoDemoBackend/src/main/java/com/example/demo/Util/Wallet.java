package com.example.demo.Util;

import java.security.GeneralSecurityException;

import com.algorand.algosdk.account.Account;

public class Wallet {
	
	//testent -> private static final String mnemonic="spice wash upon maple state income neglect normal face struggle virus arrest flush throw amateur balcony more possible double actual erode health consider absorb page";
	//ghisson -> 
	private static final String mnemonic="brand receive harbor again duty pattern raw grape hill kitchen nominee hidden measure grid apple beyond orange appear media volume olive forum clerk absorb fiction";
	 
	
	//marcio -> private static final String mnemonic ="sphere daring end riot ribbon doll wrong scrub example hollow mesh crush alcohol quiz modify puzzle relief park session thing mask wrist silly absorb uniform";
	private static Account wallet;
	
	
	public static Account getAccount() throws GeneralSecurityException {
		if(wallet==null) {
			wallet=new Account(mnemonic);
			return wallet;
		}
		return wallet;
	}
	
	

}
