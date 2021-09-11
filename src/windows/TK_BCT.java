package windows;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.HashMap;

import javax.swing.JPanel;

import dao.ThongKeDAO;
import utils.ChartGenerator;
import utils.Theme;
import utils.WindowProperties;

public class TK_BCT extends javax.swing.JDialog {
                 
    private javax.swing.JComboBox<String> cboNam;
    private javax.swing.JLabel lblHeader;
    private javax.swing.JPanel pnlChart;
    private javax.swing.JPanel pnlFilter;
    private javax.swing.JPanel pnlTable;
    private javax.swing.JScrollPane scrollTable;
    private javax.swing.JTable tblList;
    
    Calendar calendar = Calendar.getInstance();
    int thisYear = calendar.get(Calendar.YEAR);
  
    public TK_BCT(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        Customize();
    }
    
    private void Customize() {
		WindowProperties.centeringWindow(this);
		WindowProperties.setApplicationIcon(this);
		WindowProperties.CellRenderTable(tblList, tblList.getColumnCount());
		loadCboNam();
		DrawBarChartPanel();
	}
    
    private void loadCboNam() {
    	for (int i = 0; i < 5; i++) {
    		cboNam.addItem(String.valueOf(thisYear - i));
    	}
    	cboNam.addItemListener(new ItemListener() {	
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					DrawBarChartPanel();
				}
			}
		});
    }
    
    private void DrawBarChartPanel() {
    	pnlChart.removeAll();
    	pnlChart.repaint();
    	pnlChart.revalidate();
    	//
    	String year = cboNam.getSelectedItem().toString();
    	HashMap<String, HashMap<String, Long>>  map = ThongKeDAO.ThongKeBCThang_getBarChartDataset((Integer.valueOf(year)));
    	JPanel chart = ChartGenerator.createGroupBarChart("Báo cáo kinh doanh năm " + year, null, null, map);
    	chart.setPreferredSize(new Dimension(pnlChart.getWidth(), pnlChart.getHeight()));
    	pnlChart.add(chart, BorderLayout.CENTER);
    	ThongKeDAO.ThongKeBCThang_byYear(tblList, Integer.valueOf(year));
    }
    
                    
    private void initComponents() {
    	setTitle("Báo cáo kinh doanh");
    	
        pnlFilter = new javax.swing.JPanel();
        lblHeader = new javax.swing.JLabel();
        cboNam = new javax.swing.JComboBox<>();
        pnlChart = new javax.swing.JPanel();
        pnlTable = new javax.swing.JPanel();
        scrollTable = new javax.swing.JScrollPane();
        tblList = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(500, 30));
        setResizable(false);
        
        lblHeader.setForeground(Theme.getColor("lblHeader.foreground"));
        lblHeader.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHeader.setText("BÁO CÁO KINH DOANH THEO THÁNG");
        lblHeader.setPreferredSize(new java.awt.Dimension(450, 30));

        cboNam.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cboNam.setMaximumSize(new java.awt.Dimension(100, 30));
        cboNam.setPreferredSize(new java.awt.Dimension(80, 30));

        javax.swing.GroupLayout pnlFilterLayout = new javax.swing.GroupLayout(pnlFilter);
        pnlFilter.setLayout(pnlFilterLayout);
        pnlFilterLayout.setHorizontalGroup(
            pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFilterLayout.createSequentialGroup()
                .addComponent(lblHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboNam, 0, javax.swing.GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
        );
        pnlFilterLayout.setVerticalGroup(
            pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(cboNam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pnlChart.setPreferredSize(new java.awt.Dimension(770, 225));
        pnlChart.setLayout(new java.awt.BorderLayout());

        tblList.setAutoCreateRowSorter(true);
        tblList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Thời gian", "Tiền hàng", "KM đơn hàng",  "Phụ thu", "Doanh số", "Tiền vốn", "Tiền lãi", "Số lượng hàng", "Số đơn hàng"
            }
        ) {
            Class[] types = new Class [] {
                String.class, Long.class, Long.class, Long.class, Long.class, Long.class, Long.class, Integer.class, Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblList.setColumnSelectionAllowed(true);
        tblList.getTableHeader().setReorderingAllowed(false);
        scrollTable.setViewportView(tblList);
        tblList.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        javax.swing.GroupLayout pnlTableLayout = new javax.swing.GroupLayout(pnlTable);
        pnlTable.setLayout(pnlTableLayout);
        pnlTableLayout.setHorizontalGroup(
            pnlTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollTable)
        );
        pnlTableLayout.setVerticalGroup(
            pnlTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollTable, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnlTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlFilter, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlChart, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(pnlFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );

        pack();
    }
               
}
