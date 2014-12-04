package com.admtel.telephonyserver.tests;



public class EnumTest {
	enum Type { type1, type2};
	
	public static void main(String[] args) {
		System.out.println(Type.valueOf("type1"));
		System.out.println(Type.valueOf("type222"));
	}
}
