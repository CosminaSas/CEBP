package common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	
	public static void log(Object caller,String msg){
		StringBuilder sb = new StringBuilder();
		String timeStamp = new SimpleDateFormat("[yyyy.MM.dd HH:mm:ss] ").format(new Date());
		sb.append(timeStamp);
		if(caller != null)
			sb.append(caller.getClass().getSimpleName());
		sb.append(": ");
		sb.append(msg);
		System.out.println(sb.toString());
	}


}
