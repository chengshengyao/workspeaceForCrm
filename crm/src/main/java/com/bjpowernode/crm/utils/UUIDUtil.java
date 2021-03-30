package com.bjpowernode.crm.utils;

import java.util.UUID;
//生成随机数 用作数据库表主键
public class UUIDUtil {
	
	public static String getUUID(){
		//36-4=32位随机数  生成原理 ：随机数+时间+mac地址
		return UUID.randomUUID().toString().replaceAll("-","");
		
	}
	
}
