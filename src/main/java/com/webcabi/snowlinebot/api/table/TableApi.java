package com.webcabi.snowlinebot.api.table;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TableApi {

	@Autowired
	@Qualifier("zipCodeSearchRestTemplate")
	RestTemplate restTemplate;
	
	@Value("${snow.tableapi}")
	private String uri;
	
	@Value("${snow.chat.userid}")
	private String userid;
	
	@Value("${snow.chat.password}")
	private String password;
	
	/**
	 * sys_userテーブルから、LINE IDが格納されているものだけ抽出する。
	 * @return LINE IDリスト
	 * @throws URISyntaxException
	 */
	public List<String> getLineId() throws URISyntaxException {
		String plainCredentials = userid + ":" + password;
		String base64Credentials = Base64.getEncoder()
		        .encodeToString(plainCredentials.getBytes(StandardCharsets.UTF_8));

		RequestEntity requestEntity = RequestEntity
		      .get(new URI(uri + "/sys_user" + "?sysparm_fields=u_line_api"))
		      .header("Authorization", "Basic " + base64Credentials)
		      .accept(MediaType.APPLICATION_JSON_UTF8)
		      .build();		
		
		
		ResponseEntity<TableApiResponse> res = restTemplate.exchange(requestEntity, TableApiResponse.class);
		List<String> lineIds = new ArrayList<>();
		for (Map<String, Object> result : res.getBody().getResult()) {
			Object lineIdObj = result.get("u_line_api");
			if (lineIdObj != null && !"".equals(lineIdObj.toString())) {
				String lineId = (String) lineIdObj;
				lineIds.add(lineId);
				System.out.println(lineId);
			}
		}
		
		return lineIds;
	}
}
