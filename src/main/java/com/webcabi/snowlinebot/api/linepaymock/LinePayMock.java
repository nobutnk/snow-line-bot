package com.webcabi.snowlinebot.api.linepaymock;

import org.springframework.stereotype.Component;

@Component
public class LinePayMock {
	
	/**
	 * amountを人数で割り算する
	 * @param num 人数
	 * @param amount 合計金額
	 * @return １人分の金額
	 */
	public Integer splitBill(int num, int amount) {
		
		int result = amount / num;
		
		return result;
	}

}
