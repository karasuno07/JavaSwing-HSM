package utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class Language {
	
	public static boolean isVi = true;
	
	public static ResourceBundle getLanguagePack() {
		Locale locale = new Locale(isVi ? "vi" : "en");
		ResourceBundle languagePack =  ResourceBundle.getBundle("languages", locale);
		return languagePack;
	}
	
	public static ResourceBundle getMessagePack() {
		Locale locale = new Locale(isVi ? "vi" : "en");
		ResourceBundle languagePack =  ResourceBundle.getBundle("messages", locale);
		return languagePack;
	}
}
