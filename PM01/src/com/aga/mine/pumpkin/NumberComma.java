package com.aga.mine.pumpkin;

import java.text.DecimalFormat;

public class NumberComma {
	
	public NumberComma() {
	}
	
	// String에 comma 삽입
	public String numberComma(final String string) {
		if (string.length() < 4) 
			return string;
		
		String value = string;
		int len = value.length();
		int position = 3;
		for (int i = 0; i < (len -1) / 3; i++) {
			value = value.substring(0, (value.length() - position)) + "," + value.substring((value.length() - position), value.length());
			position += 4;
		}
		return value;
	}
	
	public String numberComma(final int value) {
		return numberComma(String.valueOf(value));
	}
	
	
	public String numberComma(final long value) {
		return numberComma(String.valueOf(value));
	}
	
	
	// int 값을 String으로 변환하여 comma 삽입
	public String Decimal(int value) {
		DecimalFormat format = new DecimalFormat("###,###,###,###");
		String ret = format.format(value);
		return ret;
	}
	
//	// int 값을 String으로 변환하여 comma 삽입
//	private String numberComma(int value) {
//		String s = Integer.toString(value);
//		return numberComma(s);
//	}
//	
//	// String에 comma 삽입
//	private String numberComma(String string) {
//		if (string.length() < 4) 
//			return string;
//		
//		String value = string;
//		int len = value.length();
//		int position = 3;
//		for (int i = 0; i < (len -1) / 3; i++) {
//			value = value.substring(0, (value.length() - position)) + "," + value.substring((value.length() - position), value.length());
//			position += 4;
//		}
//		return value;
//	}
	
	
}
