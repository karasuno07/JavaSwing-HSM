package utils;

import java.awt.Color;
import java.util.ResourceBundle;

public class Theme {
	
	public static boolean isLight = true;
	
	public static ResourceBundle getThemePack() {
		ResourceBundle themePack = ResourceBundle.getBundle(isLight ? "light_theme" : "dark_theme");
		return themePack;
	}
	
	public static Color getColor(String key) {
		Color color = Color.decode(Theme.getThemePack().getString(key));
		return color;
	}
}
