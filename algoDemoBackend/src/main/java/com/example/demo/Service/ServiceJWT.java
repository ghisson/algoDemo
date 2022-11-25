package com.example.demo.Service;

import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.Util.PemFile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Service
public class ServiceJWT {
	
	private Algorithm algorithm;
    private KeyPairGenerator keyGenerator;
    private KeyPair kp;
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;
    public final static String RESOURCES_DIR = "src/main/java/com/example/demo/Key/";

    
    public ServiceJWT()  throws NoSuchAlgorithmException{ 
    	/*
    	
    	keyGenerator = KeyPairGenerator.getInstance("RSA");
		keyGenerator.initialize(1024);

		kp = keyGenerator.genKeyPair();
		publicKey = (RSAPublicKey) kp.getPublic();
		privateKey = (RSAPrivateKey) kp.getPrivate();
		
        //this.algorithm = Algorithm.HMAC512(secret);
        this.algorithm=Algorithm.RSA256(publicKey, privateKey);*/
    	try {
	    	KeyFactory factory = KeyFactory.getInstance("RSA");
	    	privateKey = generatePrivateKey(factory, RESOURCES_DIR
	                + "jwtRSA256-private.pem");
	        System.out.println(String.format("Instantiated private key: %s", privateKey));
	
	        publicKey = generatePublicKey(factory, RESOURCES_DIR
	                + "jwtRSA256-public.pem");
	        System.out.println(String.format("Instantiated public key: %s", publicKey));
	        this.algorithm=Algorithm.RSA256(publicKey, privateKey);
    	}catch(Exception e) {
    		System.out.println(e);
    	}
    }
    
    
    public String create(String psw) {  	
        return JWT.create()
                .withClaim("token", psw)
                .sign(algorithm);
    }
    
   
    public String verify(String token) {
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        
        DecodedJWT jwt = verifier.verify(token);
        Map<String,Object> ret=jwt.getClaims().entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().as(Object.class)));
        String tk=(String) ret.get("token");
        
        return tk;   
    }
    
    
    
    private  RSAPrivateKey generatePrivateKey(KeyFactory factory,
            String filename) throws InvalidKeySpecException,
            FileNotFoundException, IOException {
    	
        PemFile pemFile = new PemFile(filename);
        byte[] content = pemFile.getPemObject().getContent();
        PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(content);
        return (RSAPrivateKey) factory.generatePrivate(privKeySpec);
    }

    private  RSAPublicKey generatePublicKey(KeyFactory factory,
            String filename) throws InvalidKeySpecException,
            FileNotFoundException, IOException {
    	
        PemFile pemFile = new PemFile(filename);
        byte[] content = pemFile.getPemObject().getContent();
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(content);
        return (RSAPublicKey) factory.generatePublic(pubKeySpec);
    }


}
