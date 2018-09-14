package com.webcabi.snowlinebot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.webcabi.api.zipcloud.model.ZipSearchResponse;

@Service
public class ZipCodeService {
	
	@Autowired
	@Qualifier("zipCodeSearchRestTemplate")
	RestTemplate restTemplate;

	/** 郵便番号検索API リクエストURL */
	private static final String URL = "http://zipcloud.ibsnet.co.jp/api/search?zipcode={zipcode}";

	public ZipSearchResponse service(String zipcode) {
		return restTemplate.getForObject(URL, ZipSearchResponse.class, zipcode);
	}

}
