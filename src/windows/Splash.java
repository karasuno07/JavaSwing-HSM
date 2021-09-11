 package windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import utils.ImageHandler;
import utils.WindowProperties;

public class Splash extends JDialog {
	
	private static final long serialVersionUID = 1L;
	private JDialog splashDialog;
	private JLabel lblImage;
	private JProgressBar progressBar;
	
	public Splash() {
		setName("Splash");
		
		splashDialog = new JDialog();
		splashDialog.setUndecorated(true);
		splashDialog.setSize(450, 325);
		splashDialog.setLayout(new BorderLayout());
		
		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setFont(new Font("Tahoma", 1, 12));
		progressBar.setPreferredSize(new Dimension(450, 25));
		progressBar.setBackground(new Color(224, 102, 102));
		progressBar.setStringPainted(true);
		splashDialog.add(progressBar, BorderLayout.SOUTH);
		
		lblImage = new JLabel();
		lblImage.setHorizontalAlignment(SwingConstants.CENTER);
		lblImage.setHorizontalTextPosition(SwingConstants.CENTER);
		lblImage.setPreferredSize(new Dimension(450, 300));
		lblImage.setIcon(ImageHandler.imageMaker("resources/images/splash.png", 450, 300));
		splashDialog.add(lblImage, BorderLayout.CENTER);
		
		splashDialog.setVisible(true);
		splashDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		WindowProperties.centeringWindow(splashDialog);;
	}
	
	public void loading() {
		int count = 0;
		
		while (count <= 100) {
			progressBar.setValue(count);
			
			if (count == 100) {
				splashDialog.dispose();
				new Login().setVisible(true);
				
			}
			
			count++;
			
			try {
				Thread.sleep(18);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
