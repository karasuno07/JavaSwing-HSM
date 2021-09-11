package utils;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.formdev.flatlaf.FlatIntelliJLaf;

public class WindowProperties {

	public static void centeringWindow(Window window) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();
		window.setLocation((width - window.getWidth()) / 2, (height - window.getHeight()) / 2);
	}

	public static void resizableWindow(JFrame window, int w, int h) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();
		window.setMaximizedBounds(new Rectangle((width - w) / 2, (height - h) / 2, w, h));
	}

	public static void setFlatLaf_LookAndFeel() {
		FlatIntelliJLaf.install();
//		FlatDarkFlatIJTheme.install();
		UIManager.put("Component.focusWidth", 1);
		UIManager.put("CheckBox.icon.style", "filled");
		System.setProperty("flatlaf.menuBarEmbedded", "true");
		System.setProperty("flatlaf.useWindowDecorations", "true");
	}

	public static void setApplicationIcon(Window window) {
		window.setIconImage(ImageHandler.imageMaker("resources/icons/icon-app.png", 32, 32).getImage());
	}

	public static void CellRenderTable(JTable table, int columnCount) {

		TableCellRenderer renderer = new DefaultTableCellRenderer() {

			SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);
				c.setHorizontalAlignment(JLabel.CENTER);
				if (value instanceof Date) {
					value = f.format(value);
				}
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}

		};

		for (int i = 0; i < columnCount; i++) {
			if (table.getColumnClass(i) == ImageIcon.class) {
				table.getColumnModel().getColumn(i).setCellRenderer(new IconCellRenderer());
			} else {
				table.getColumnModel().getColumn(i).setCellRenderer(renderer);
			}
		}
	}

}
