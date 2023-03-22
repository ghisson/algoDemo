package com.example.demo.Util;

import java.security.GeneralSecurityException;

import com.algorand.algosdk.account.Account;

public class Wallet {
	
	//testent -> private static final String mnemonic="spice wash upon maple state income neglect normal face struggle virus arrest flush throw amateur balcony more possible double actual erode health consider absorb page";
	//ghisson -> private static final String mnemonic="brand receive harbor again duty pattern raw grape hill kitchen nominee hidden measure grid apple beyond orange appear media volume olive forum clerk absorb fiction";
	 
	//wallet raspbarrypi -> erase chair tenant save coral ginger hybrid pluck ripple this satoshi sight proud online excite put license critic then prevent category waste sick abandon picture
	//marcio -> 
	private static final String mnemonic ="erase chair tenant save coral ginger hybrid pluck ripple this satoshi sight proud online excite put license critic then prevent category waste sick abandon picture";
	private static Account wallet;
	
	
	public static Account getAccount() throws GeneralSecurityException {
		if(wallet==null) {
			wallet=new Account(mnemonic);
			return wallet;
		}
		return wallet;
	}
	
	

}
