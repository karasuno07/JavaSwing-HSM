package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {
	
	
	public static java.sql.Date toSqlDate(Date date) {
		return new java.sql.Date(date.getTime());
	}
	
	public static String toString(Object object) {
		String text = new SimpleDateFormat("yyyy-MM-dd").format(object);
		return text;
	}
}
