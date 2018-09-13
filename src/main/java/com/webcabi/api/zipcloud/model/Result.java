package com.webcabi.api.zipcloud.model;

import lombok.Getter;
import lombok.Setter;

/**
 * zipcode	郵便番号	7桁の郵便番号。ハイフンなし。
prefcode	都道府県コード	JIS X 0401 に定められた2桁の都道府県コード。
address1	都道府県名	
address2	市区町村名	
address3	町域名	
kana1	都道府県名カナ	
kana2	市区町村名カナ	
kana3	町域名カナ
 * @author nobutnk
 *
 */
@Getter
@Setter
public class Result {

	private String zipcode;
	private String address1;
	private String address2;
	private String address3;
	private String kana1;
	private String kana2;
	private String kana3;
}
