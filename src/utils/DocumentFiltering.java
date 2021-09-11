package utils;

import java.text.SimpleDateFormat;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;


public class DocumentFiltering {
	
	public static void IntFilter(PlainDocument document) {
		DocumentFilter docFilter = new DocumentFilter() {

			@Override
			public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
					throws BadLocationException {
				Document doc = fb.getDocument();
				StringBuilder sb = new StringBuilder();
				sb.append(doc.getText(0, doc.getLength()));
				sb.insert(offset, string);

				if (isInt(sb.toString())) {
					super.insertString(fb, offset, string, attr);
				} else {
					MessageBox.alert("Kiểu dữ liệu không phù hợp");
				}
			}

			@Override
			public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {

				Document doc = fb.getDocument();
				StringBuilder sb = new StringBuilder();
				sb.append(doc.getText(0, doc.getLength()));
				sb.delete(offset, offset + length);

				if (isInt(sb.toString())) {
					super.remove(fb, offset, length);
				} else {
					MessageBox.alert("Kiểu dữ liệu không phù hợp");
				}

			}

			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
					throws BadLocationException {

				Document doc = fb.getDocument();
				StringBuilder sb = new StringBuilder();
				sb.append(doc.getText(0, doc.getLength()));
				sb.replace(offset, offset + length, text);

				if (isInt(sb.toString())) {
					super.replace(fb, offset, length, text, attrs);
				} else {
					MessageBox.alert("Kiểu dữ liệu không phù hợp");
				}

			}

			private boolean isInt(String text) {
				try {
					if (text.isEmpty())
						return true;
					Integer.parseInt(text);
					return true;
				} catch (Exception e) {
					return false;
				}
			}
		};

		document.setDocumentFilter(docFilter);
	}
	
	public static void LongFilter(PlainDocument document) {
		DocumentFilter documentFilter = new DocumentFilter() {
			
			@Override
			public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
					throws BadLocationException {
				Document doc = fb.getDocument();
				StringBuilder sb = new StringBuilder();
				sb.append(doc.getText(0, doc.getLength()));
				sb.insert(offset, string);

				if (isLong(sb.toString())) {
					super.insertString(fb, offset, string, attr);
				} else {
					MessageBox.alert("Kiểu dữ liệu không phù hợp");
				}
			}
			
			@Override
			public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {

				Document doc = fb.getDocument();
				StringBuilder sb = new StringBuilder();
				sb.append(doc.getText(0, doc.getLength()));
				sb.delete(offset, offset + length);

				if (isLong(sb.toString())) {
					super.remove(fb, offset, length);
				} else {
					MessageBox.alert("Kiểu dữ liệu không phù hợp");
				}

			}

			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
					throws BadLocationException {

				Document doc = fb.getDocument();
				StringBuilder sb = new StringBuilder();
				sb.append(doc.getText(0, doc.getLength()));
				sb.replace(offset, offset + length, text);

				if (isLong(sb.toString())) {
					super.replace(fb, offset, length, text, attrs);
				} else {
					MessageBox.alert("Kiểu dữ liệu không phù hợp");
				}

			}

			private boolean isLong(String text) {
				try {
					if (text.isEmpty())
						return true;
					Long.parseLong(text);
					return true;
				} catch (Exception e) {
					return false;
				}
			}
		};

		document.setDocumentFilter(documentFilter);
	}
	
	public static void FloatFilter(PlainDocument document) {
		DocumentFilter documentFilter = new DocumentFilter() {
			
			@Override
			public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
					throws BadLocationException {
				Document doc = fb.getDocument();
				StringBuilder sb = new StringBuilder();
				sb.append(doc.getText(0, doc.getLength()));
				sb.insert(offset, string);

				if (isFloat(sb.toString())) {
					super.insertString(fb, offset, string, attr);
				} else {
					MessageBox.alert("Kiểu dữ liệu không phù hợp");
				}
			}
			
			@Override
			public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {

				Document doc = fb.getDocument();
				StringBuilder sb = new StringBuilder();
				sb.append(doc.getText(0, doc.getLength()));
				sb.delete(offset, offset + length);

				if (isFloat(sb.toString())) {
					super.remove(fb, offset, length);
				} else {
					MessageBox.alert("Kiểu dữ liệu không phù hợp");
				}

			}

			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
					throws BadLocationException {

				Document doc = fb.getDocument();
				StringBuilder sb = new StringBuilder();
				sb.append(doc.getText(0, doc.getLength()));
				sb.replace(offset, offset + length, text);

				if (isFloat(sb.toString())) {
					super.replace(fb, offset, length, text, attrs);
				} else {
					MessageBox.alert("Kiểu dữ liệu không phù hợp");
				}

			}

			private boolean isFloat(String text) {
				try {
					if (text.isEmpty())
						return true;
					Float.parseFloat(text);
					return true;
				} catch (Exception e) {
					return false;
				}
			}
		};

		document.setDocumentFilter(documentFilter);
	}
	
	public static void DateFilter(PlainDocument document) {
		DocumentFilter docfilter = new DocumentFilter() {
			@Override
			public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
					throws BadLocationException {
				Document doc = fb.getDocument();
				StringBuilder sb = new StringBuilder();
				sb.append(doc.getText(0, doc.getLength()));
				sb.insert(offset, string);
				if (isDate(sb.toString())) {
					super.insertString(fb, offset, string, attr);
				} else {
					MessageBox.alert("Định dạng ngày không phù hợp");
				}
			}
			@Override
			public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
				Document doc = fb.getDocument();
				StringBuilder sb = new StringBuilder();
				sb.append(doc.getText(0, doc.getLength()));
				sb.delete(offset, offset + length);
				if (isDate(sb.toString())) {
					super.remove(fb, offset, length);
				} else {
					MessageBox.alert("Định dạng ngày không phù hợp");
				}
			}
			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
					throws BadLocationException {
				Document doc = fb.getDocument();
				StringBuilder sb = new StringBuilder();
				sb.append(doc.getText(0, doc.getLength()));
				sb.replace(offset, offset + length, text);
				if (isDate(sb.toString())) {
					super.replace(fb, offset, length, text, attrs);
				} else {
					MessageBox.alert("Định dạng ngày không phù hợp");
				}
			}
			private boolean isDate(String text) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				try {
					if (text.isEmpty()) return false;
					dateFormat.parse(text);
					return true;
				} catch (Exception e) {
					return false;
				}
			}
		};
		
		document.setDocumentFilter(docfilter);
	}
}
