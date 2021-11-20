package common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	
	public static void main(String[] args) {
		Logger logger = new Logger(new Object());
		logger.log("here");
		logger.log("here");
		logger.log("here");
		logger.log("here");
	}

	private Object caller;

	/**
	 * @param caller
	 */
	public Logger(Object caller) {
		this.caller = caller;
	}

	public void log(String msg){
		StringBuilder sb = new StringBuilder();
		String timeStamp = new SimpleDateFormat("[yyyy.MM.dd HH:mm:ss] ").format(new Date());
		sb.append(timeStamp);
		sb.append(caller.getClass().getSimpleName());
		sb.append(": ");
		sb.append(msg);
		System.out.println(sb.toString());
	}


}
