package com.example.demo.Service;

import org.springframework.stereotype.Service;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Service
public class ServiceJWT {
	
	private Algorithm algorithm;
    private String secret = "atlantica";
    private KeyPairGenerator keyGenerator;
    private KeyPair kp;
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;
    
    public ServiceJWT()  throws NoSuchAlgorithmException{ 
    	
    	keyGenerator = KeyPairGenerator.getInstance("RSA");
		keyGenerator.initialize(1024);

		kp = keyGenerator.genKeyPair();
		publicKey = (RSAPublicKey) kp.getPublic();
		privateKey = (RSAPrivateKey) kp.getPrivate();
		
        //this.algorithm = Algorithm.HMAC512(secret);
        this.algorithm=Algorithm.RSA256(publicKey, privateKey);
    }
    
    
    public String create(String psw) {  	
       	ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

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
        
        ObjectMapper objectMapper = new ObjectMapper();
        /*try {
        	
			//tk=objectMapper.readValue(ret.get("token"));
			
		} catch (JsonMappingException e1) {
			System.out.println("primo catch");
			return null;
		} catch (JsonProcessingException e1) {
			System.out.println("secondo catch");

			return null;
		}	*/
        
        return tk;   
    }


}
