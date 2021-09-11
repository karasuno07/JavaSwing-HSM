package windows;

import java.util.List;

import javax.swing.*;
import javax.swing.table.*;

import dao.PhieuNhapDAO;

import java.awt.*;
import java.awt.event.*;

import utils.MessageBox;
import utils.Theme;
import utils.WindowProperties;

public class DSPN extends JDialog {

    private JLabel lblHeader;
    private JLabel lblMaHang;
    private JLabel lblTenHang;
    private JPanel pnlTenHang;
    private JScrollPane scrollTable;
    private JTable tblList;
    
    public DSPN(Frame parent, boolean modal, String MaHang, String TenHang) {
        super(parent, modal);
        initComponents();
        Customize();
        lblTenHang.setText(TenHang);
        lblMaHang.setText(String.format("(%s)", MaHang));
        fillForm(MaHang);
    }
    
    private void Customize() {
    	WindowProperties.centeringWindow(this);
    	WindowProperties.setApplicationIcon(this);
    	WindowProperties.CellRenderTable(tblList, tblList.getColumnCount());
    	tblList.setFocusable(false);
    }
    
    private void fillForm(String MaHang) {
    	PhieuNhapDAO.fillDSPN(tblList, MaHang);
    }
                     
    private void initComponents() {
//    	setUndecorated(true);
    	setTitle("Danh sách phiếu nhập kho");
    	
        lblHeader = new JLabel();
        pnlTenHang = new JPanel();
        lblTenHang = new JLabel();
        lblMaHang = new JLabel();
        scrollTable = new JScrollPane();
        tblList = new JTable();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        
        lblHeader.setForeground(Theme.getColor("lblHeader.foreground"));
        lblHeader.setFont(new Font("Tahoma", 1, 15)); // NOI18N
        lblHeader.setText("THẺ KHO");
        lblHeader.setMinimumSize(new Dimension(90, 25));

        lblTenHang.setFont(new Font("Tahoma", 1, 12)); // NOI18N
        lblTenHang.setHorizontalAlignment(SwingConstants.CENTER);
        lblTenHang.setText("Tên hàng");
        lblTenHang.setPreferredSize(new Dimension(60, 30));

        lblMaHang.setFont(new Font("Tahoma", 0, 12)); // NOI18N
        lblMaHang.setText("Mã hàng");
        lblMaHang.setPreferredSize(new Dimension(410, 30));

        GroupLayout pnlTenHangLayout = new GroupLayout(pnlTenHang);
        pnlTenHang.setLayout(pnlTenHangLayout);
        pnlTenHangLayout.setHorizontalGroup(
            pnlTenHangLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(pnlTenHangLayout.createSequentialGroup()
                .addComponent(lblTenHang, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblMaHang, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlTenHangLayout.setVerticalGroup(
            pnlTenHangLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(lblTenHang, GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
            .addComponent(lblMaHang, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tblList.setModel(new DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Ngày nhập", "Mã phiếu", "Số lượng nhập", "Ghi chú"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.util.Date.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollTable.setViewportView(tblList);
        if (tblList.getColumnModel().getColumnCount() > 0) {
            tblList.getColumnModel().getColumn(0).setPreferredWidth(30);
            tblList.getColumnModel().getColumn(2).setPreferredWidth(100);
            tblList.getColumnModel().getColumn(4).setPreferredWidth(120);
        }

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(lblHeader, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlTenHang, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scrollTable))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblHeader, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlTenHang, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollTable, GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }              
}
