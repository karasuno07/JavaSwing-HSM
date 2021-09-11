package utils;

import javax.swing.JComboBox;
import javax.swing.text.JTextComponent;

import org.jdatepicker.JDatePicker;


public class Validator {
	public static boolean isNotEmpty(JTextComponent field, String errorMessage) {
		String text = field.getText();
		if (text.isBlank()) {
			MessageBox.alert(errorMessage);
			field.requestFocus();
			field.setToolTipText("Nhập thông tin tại đây");
			return false;
		}
		return true;
	}
	
	public static boolean isSetDate(JDatePicker datePicker, String errorMessage) {
		if (datePicker.getModel().getValue() == null) {
			MessageBox.alert(errorMessage);
			datePicker.requestFocus();
			datePicker.setToolTipText("Chọn ngày tại đây");
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean isMeetLengthCondition(JTextComponent field, int length, String errorMessage) {
		String text = field.getText();
		if (text.length() != length) {
			MessageBox.alert(String.format(errorMessage, length));
			field.requestFocus();
			return false;
		}
		return true;
	}
	
	public static boolean isEmptyComboBox(JComboBox cbo, String errorMessage) {
		if (cbo.getItemCount() == 0) {
			MessageBox.alert(errorMessage);
			cbo.requestFocus();
			return false;
		}
		return true;
	}
}
