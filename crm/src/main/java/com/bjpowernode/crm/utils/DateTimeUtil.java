package com.bjpowernode.crm.utils;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DateTimeUtil {
	/**
	 * getSysTime 获取系统时间
 	 * @return
	 */
	public static String getSysTime(){
		//日期化当前系统时间格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date date = new Date();
		String dateStr = sdf.format(date);
		
		return dateStr;
		
	}
	
}
