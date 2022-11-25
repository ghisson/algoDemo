package com.example.demo.Controller;

import java.security.NoSuchAlgorithmException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.example.demo.Util.ClientAlgo;


@Controller
public class MainController {
	
    private static String token = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

	@CrossOrigin(origins="*")
	@GetMapping("/a")
	public ResponseEntity<Object> createWallet() {
		
        AlgodClient algod = ClientAlgo.getClient();

		Account account;
		try {
			account = new Account();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<Object>("Errore", HttpStatus.BAD_REQUEST);
 
		}
		return new ResponseEntity<Object>(account.getAddress(), HttpStatus.OK);
	}
	
	
	

}
