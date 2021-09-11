package utils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.swing.JRViewer;

public class InvoicePrinting {

	public static String generateInvoiceID(String prefix) {
		if (prefix.length() != 2) {
			MessageBox.alert("invalid invoice pattern");
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		Date now = new Date();
		String pattern = sdf.format(now);
		return prefix.toUpperCase()+ String.join("", pattern.split("\\W+"));
	}

	public static void XuatHD(String key, Object value, String templateFile) {
		try {
			HashMap<String, Object> map = new HashMap<>();
			map.put(key, value);
			JasperReport report = JasperCompileManager.compileReport(templateFile);
			JasperPrint print = JasperFillManager.fillReport(report, map, JDBCHelper.getConnection());
			// Viewer là class kế thừa JFrame nhằm tinh chỉnh lại view hiển thị hoá đơn
			Viewer.viewReport("In hóa đơn", print, false);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}

class Viewer extends JFrame {

	protected JRViewer viewer = null;
	private boolean isExitOnClose = false;
	private JPanel pnlMain;

	public Viewer(String title, JasperPrint print, boolean isExitOnClose) throws JRException {
		this.isExitOnClose = isExitOnClose;
		initComponents(title);
		viewer = new JRViewer(print);
		pnlMain.add(viewer, BorderLayout.CENTER);
	}

	private void initComponents(String title) {
		pnlMain = new JPanel();

		setTitle(title);
		pnlMain.setLayout(new BorderLayout());
		getContentPane().add(pnlMain, BorderLayout.CENTER);
		setIconImage(ImageHandler.imageMaker("resources/icons/iconPrinter.png", 32, 32).getImage());
		pack();
		

		Dimension dimension = new Dimension(500, 780);
		setSize(dimension);
		WindowProperties.centeringWindow(this);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (isExitOnClose) {
					System.exit(0);
				} else {
					setVisible(false);
					viewer.clear();
					viewer = null;
					getContentPane().removeAll();
					dispose();
				}
			}

		});
	}
	
	public void setZoomRatio(float ratio) {
		viewer.setZoomRatio(ratio);
	}
	
	public void setFitWidthRatio() {
		viewer.setFitWidthZoomRatio();
	}
	
	public void setFitPageZoomRatio() {
		viewer.setFitPageZoomRatio();
	}
	
	public static void viewReport(String title, JasperPrint print, boolean isExitOnClose) throws JRException {
		Viewer jasperViewer = new Viewer(title, print, isExitOnClose);
		jasperViewer.setZoomRatio((float) 0.7);
		jasperViewer.setVisible(true);
	}
}
