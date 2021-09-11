package windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import utils.ImageHandler;
import utils.Theme;
import utils.WindowProperties;

public class Info extends JDialog {
	
	private JLabel lblAppName;
    private JLabel lblAuthor;
    private JLabel lblCompany;
    private JLabel lblIcon;
    private JLabel lblTitle;
    private JLabel lblVersion;
    private JPanel pnlInfo;
    private JPanel pnlMain;
    private JPanel pnlTitle;
    private JLabel txtAppName;
    private JLabel txtAuthor;
    private JLabel txtCompany;
    private JLabel txtVersion;

    public Info(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        Customize();
    }
    
    private void Customize() {
    	WindowProperties.centeringWindow(this);
    	WindowProperties.setApplicationIcon(this);
    	//
    	lblIcon.setIcon(ImageHandler.imageMaker("resources/icons/info.png", 28, 28));
    	lblAuthor.setIcon(ImageHandler.imageMaker("resources/icons/author.png", 28, 28));
    	lblAppName.setIcon(ImageHandler.imageMaker("resources/icons/app.png", 28, 28));
    	lblVersion.setIcon(ImageHandler.imageMaker("resources/icons/version.png", 28, 28));
    	lblCompany.setIcon(ImageHandler.imageMaker("resources/icons/company.png", 28, 28));
    }
                          
    private void initComponents() {
    	addWindowListener(new WindowAdapter() {
    		@Override
    		public void windowDeactivated(WindowEvent e) {
    			dispose();
    		}
    	});
        pnlMain = new JPanel();
        pnlTitle = new JPanel();
        lblIcon = new JLabel();
        lblTitle = new JLabel();
        pnlInfo = new JPanel();
        lblAuthor = new JLabel();
        txtAuthor = new JLabel();
        lblAppName = new JLabel();
        txtAppName = new JLabel();
        lblVersion = new JLabel();
        txtVersion = new JLabel();
        lblCompany = new JLabel();
        txtCompany = new JLabel();
        
//        setUndecorated(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        pnlMain.setBackground(Theme.getColor("Info.main.background"));
        pnlInfo.setBackground(Theme.getColor("Info.container.background"));
        pnlTitle.setBackground(Theme.getColor("Info.main.background"));

        lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
        lblIcon.setHorizontalTextPosition(SwingConstants.CENTER);
        lblIcon.setPreferredSize(new Dimension(32, 32));

        lblTitle.setFont(new Font("Tahoma", 1, 12)); // NOI18N
        lblTitle.setForeground(Theme.getColor("Chart.foreground"));
        lblTitle.setText("Application Information");

        GroupLayout pnlTitleLayout = new GroupLayout(pnlTitle);
        pnlTitle.setLayout(pnlTitleLayout);
        pnlTitleLayout.setHorizontalGroup(
            pnlTitleLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(pnlTitleLayout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(lblIcon, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTitle, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                .addGap(80, 80, 80))
        );
        pnlTitleLayout.setVerticalGroup(
            pnlTitleLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(pnlTitleLayout.createSequentialGroup()
                .addGroup(pnlTitleLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(lblIcon, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, GroupLayout.PREFERRED_SIZE))
        );

        lblAuthor.setHorizontalAlignment(SwingConstants.CENTER);
        lblAuthor.setHorizontalTextPosition(SwingConstants.CENTER);
        lblAuthor.setPreferredSize(new Dimension(30, 30));

        txtAuthor.setFont(new Font("Tahoma", 0, 12)); // NOI18N
        txtAuthor.setForeground(Theme.getColor("Chart.foreground"));
        txtAuthor.setText("This application was created by Chien Kieu");

        lblAppName.setHorizontalAlignment(SwingConstants.CENTER);
        lblAppName.setHorizontalTextPosition(SwingConstants.CENTER);
        lblAppName.setPreferredSize(new Dimension(30, 30));

        txtAppName.setFont(new Font("Tahoma", 0, 12)); // NOI18N
        txtAppName.setForeground(Theme.getColor("Chart.foreground"));
        txtAppName.setText("Applicaton name: Hotaru Sales Management");

        lblVersion.setHorizontalAlignment(SwingConstants.CENTER);
        lblVersion.setText("");
        lblVersion.setHorizontalTextPosition(SwingConstants.CENTER);
        lblVersion.setPreferredSize(new Dimension(30, 30));

        txtVersion.setFont(new Font("Tahoma", 0, 12)); // NOI18N
        txtVersion.setForeground(Theme.getColor("Chart.foreground"));;
        txtVersion.setText("Version: 1.0.0");

        lblCompany.setHorizontalAlignment(SwingConstants.CENTER);
        lblCompany.setHorizontalTextPosition(SwingConstants.CENTER);
        lblCompany.setPreferredSize(new Dimension(30, 30));

        txtCompany.setFont(new Font("Tahoma", 0, 12)); // NOI18N
        txtCompany.setForeground(Theme.getColor("Chart.foreground"));
        txtCompany.setText("Company: Firefly Co.");

        GroupLayout pnlInfoLayout = new GroupLayout(pnlInfo);
        pnlInfo.setLayout(pnlInfoLayout);
        pnlInfoLayout.setHorizontalGroup(
            pnlInfoLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(pnlInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInfoLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInfoLayout.createSequentialGroup()
                        .addComponent(lblAuthor, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtAuthor, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlInfoLayout.createSequentialGroup()
                        .addComponent(lblAppName, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtAppName, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlInfoLayout.createSequentialGroup()
                        .addComponent(lblVersion, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtVersion, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlInfoLayout.createSequentialGroup()
                        .addComponent(lblCompany, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCompany, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlInfoLayout.setVerticalGroup(
            pnlInfoLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(pnlInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInfoLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAuthor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAuthor, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlInfoLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAppName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAppName, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlInfoLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblVersion, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtVersion, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlInfoLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCompany, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCompany, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GroupLayout pnlMainLayout = new GroupLayout(pnlMain);
        pnlMain.setLayout(pnlMainLayout);
        pnlMainLayout.setHorizontalGroup(
            pnlMainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(pnlMainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(pnlInfo, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );
        pnlMainLayout.setVerticalGroup(
            pnlMainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(pnlTitle, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlInfo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

                 
}
