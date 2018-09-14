package com.webcabi.snowlinebot.api.connect;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * ServiceNow Connect Chatとお話しする
 * @author tanakanbb
 *
 */
@Component
public class ConnectChatApi {
	
	@Autowired
	@Qualifier("zipCodeSearchRestTemplate")
	RestTemplate restTemplate;
	
	@Value("${snow.chat.endpoint}")
	private URI uri;
	
	@Value("${snow.chat.userid}")
	private String userid;
	
	@Value("${snow.chat.password}")
	private String password;
	
	public ConnectChatResponse get(String zipcode) throws URISyntaxException {
		String plainCredentials = userid + ":" + password;
		String base64Credentials = Base64.getEncoder()
		        .encodeToString(plainCredentials.getBytes(StandardCharsets.UTF_8));

		RequestEntity requestEntity = RequestEntity
		      .get(uri)
		      .header("Authorization", "Basic " + base64Credentials)
		      .accept(MediaType.APPLICATION_JSON_UTF8)
		      .build();		
		
		
		ResponseEntity<ConnectChatResponse> res = restTemplate.exchange(requestEntity, ConnectChatResponse.class);
		return res.getBody();
	}
	
	public ConnectChatResponse post(String message) throws URISyntaxException {
		ConnectChatRequest body = new ConnectChatRequest();
		body.setMessage(message);
		String plainCredentials = userid + ":" + password;
		String base64Credentials = Base64.getEncoder()
		        .encodeToString(plainCredentials.getBytes(StandardCharsets.UTF_8));

		RequestEntity<ConnectChatRequest> requestEntity = RequestEntity
		      .post(uri)
		      .header("Authorization", "Basic " + base64Credentials)
		      .accept(MediaType.APPLICATION_JSON_UTF8)
		      .body(body);
		
		
		ResponseEntity<ConnectChatResponse> res = restTemplate.exchange(requestEntity, ConnectChatResponse.class);
		return res.getBody();
	}
	
	

}
