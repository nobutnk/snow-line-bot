package com.webcabi.api.zipcloud.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * status	ステータス	正常時は 200、エラー発生時にはエラーコードが返される。
message	メッセージ	エラー発生時に、エラーの内容が返される。
results	--- 検索結果が複数存在する場合は、以下の項目が配列として返される ---
zipcode	郵便番号	7桁の郵便番号。ハイフンなし。
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
public class ZipSearchResponse {

	private String message;
	private String status;
	private List<Result> results;
	
}
