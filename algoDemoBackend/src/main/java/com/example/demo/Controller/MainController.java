package com.example.demo.Controller;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.example.demo.Service.ServiceJWT;
import com.example.demo.Service.ServiceTransaction;
import com.example.demo.Util.ClientAlgo;
import com.example.demo.Util.HashCreator;


@Controller
public class MainController {
	
	@Autowired
	private ServiceTransaction serviceTransaction;
	
	@Autowired
	private ServiceJWT serviceJWT;
	
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
	
	
	@CrossOrigin(origins="*")
	@GetMapping("/sendTransaction/{note}")
	public ResponseEntity<Object> sendTransaction(@PathVariable String note) {
		String idTransaction=serviceTransaction.sendTransaction(note);
		System.out.println("idTX: "+idTransaction);
		return new ResponseEntity<Object>(idTransaction, HttpStatus.OK);
	}
	
	@CrossOrigin(origins="*")
	@GetMapping("/cripta/{note}")
	public ResponseEntity<Object> cripta(@PathVariable String note) {
		String criptata=serviceJWT.create(note);
		return new ResponseEntity<Object>(criptata, HttpStatus.OK);
	}
	
	@CrossOrigin(origins="*")
	@GetMapping("/decripta/{note}")
	public ResponseEntity<Object> decripta(@PathVariable String note) {
		String decriptata=serviceJWT.verify(note);
		return new ResponseEntity<Object>(decriptata, HttpStatus.OK);
	}
	
	@CrossOrigin(origins="*")
	@GetMapping("/hash/{note}")
	public ResponseEntity<Object> hash(@PathVariable String note) throws NoSuchAlgorithmException {
		String decriptata=HashCreator.createSHAHash(note);
		return new ResponseEntity<Object>(decriptata, HttpStatus.OK);
	}
	
	
	

}
