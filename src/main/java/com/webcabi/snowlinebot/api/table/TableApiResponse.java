package com.webcabi.snowlinebot.api.table;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableApiResponse {
	
	private List<Map<String, Object>> result;

}
