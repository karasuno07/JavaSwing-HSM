package windows;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import org.jdatepicker.JDatePicker;
import org.jdatepicker.UtilDateModel;

import dao.ThongKeDAO;
import utils.ChartGenerator;
import utils.MessageBox;
import utils.Theme;
import utils.WindowProperties;

public class TKTC extends javax.swing.JDialog {

	private javax.swing.JLabel lblChi;
	private javax.swing.JLabel lblFrom;
	private javax.swing.JLabel lblHeader;
	private javax.swing.JLabel lblThu;
	private javax.swing.JLabel lblTo;
	private javax.swing.JPanel pnlChart;
	private javax.swing.JPanel pnlFilter;
	private javax.swing.JPanel pnlSummary;
	private javax.swing.JPanel pnlTable;
	private javax.swing.JScrollPane scrollTable;
	private javax.swing.JTable tblList;
	private JDatePicker txtFrom;
	private JDatePicker txtTo;
	
	UtilDateModel dateFromModel = new UtilDateModel();
	UtilDateModel dateToModel = new UtilDateModel();
	String dateFormat = "dd/MM/yyyy";

	public TKTC(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
		Customize();
	}

	private void Customize() {
		WindowProperties.centeringWindow(this);
		WindowProperties.setApplicationIcon(this);
		WindowProperties.CellRenderTable(tblList, tblList.getColumnCount());
		Date now = new Date();
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE, 1);
		Date firstDateofMonth = c.getTime();
		dateFromModel.setValue(firstDateofMonth);
		dateToModel.setValue(now);
		fillContent();
		dateFromModel.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("day")) {
					if (dateFromModel.getValue().compareTo(dateToModel.getValue()) > 0) {
						MessageBox.alert("Ngày bắt đầu không thể lớn hơn ngày kết thúc");
						dateFromModel.setValue(dateToModel.getValue());
						return;
					}
					fillContent();
				}
			}
		});
		dateToModel.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("day")) {
					if (dateToModel.getValue().compareTo(new Date()) > 0) {
						MessageBox.alert("Ngày kết thúc không thể lớn hơn ngày hiện tại");
						dateFromModel.setValue(new Date());
						return;
					}
					fillContent();
				}
			}
		});
	}
		
	private void fillContent() {
		Date from = dateFromModel.getValue();
		Date to = dateToModel.getValue();
		DrawBarChartPanel(from, to);
		fillTable(from, to);
		setTextSummary(from, to);
	}
	
	private void DrawBarChartPanel(Date from, Date to) {
		pnlChart.removeAll();
    	pnlChart.repaint();
    	pnlChart.revalidate();
    	HashMap<String, HashMap<String, Long>>  map = ThongKeDAO.ThongKeThuChi_getBarChartDataset(from, to);
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
    	String f = sdf.format(from), t = sdf.format(to);
    	String title = String.format("Thống kê thu/chi từ ngày %s đến %s", f, t);
    	JPanel chart = ChartGenerator.createGroupBarChart(title, null, null, map);
    	chart.setPreferredSize(new Dimension(pnlChart.getWidth(), pnlChart.getHeight()));
    	pnlChart.add(chart, BorderLayout.CENTER);
	}
	
	private void fillTable(Date from, Date to) {
		ThongKeDAO.ThongKeThuChi_byTime(tblList, from, to);
	}
	
	private void setTextSummary(Date from, Date to) {
		HashMap<String, String> map = ThongKeDAO.ThongKeThuChi_Summary(from, to);
		lblChi.setText("Tổng chi:  " + map.get("Chi"));
		lblThu.setText("Tổng thu:  " + map.get("Thu"));
	}

	private void initComponents() {
		setTitle("Thống kê thu chi");
		
		pnlFilter = new javax.swing.JPanel();
		lblHeader = new javax.swing.JLabel();
		lblFrom = new javax.swing.JLabel();
		txtFrom = new JDatePicker(dateFromModel, dateFormat);
		lblTo = new javax.swing.JLabel();
		txtTo = new JDatePicker(dateToModel, dateFormat);
		pnlChart = new javax.swing.JPanel();
		pnlTable = new javax.swing.JPanel();
		scrollTable = new javax.swing.JScrollPane();
		tblList = new javax.swing.JTable();
		pnlSummary = new javax.swing.JPanel();
		lblThu = new javax.swing.JLabel();
		lblChi = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setPreferredSize(new Dimension(700, 780));
		setResizable(false);

		lblHeader.setForeground(Theme.getColor("lblHeader.foreground"));
		lblHeader.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
		lblHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		lblHeader.setText("THỐNG KÊ THU CHI");
		lblHeader.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		lblHeader.setPreferredSize(new java.awt.Dimension(300, 31));

		lblFrom.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
		lblFrom.setText("Từ");
		lblFrom.setPreferredSize(new java.awt.Dimension(28, 22));

		txtFrom.setPreferredSize(new java.awt.Dimension(150, 22));

		lblTo.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
		lblTo.setText("Đến");
		lblTo.setPreferredSize(new java.awt.Dimension(28, 22));

		txtTo.setPreferredSize(new java.awt.Dimension(150, 22));

		javax.swing.GroupLayout pnlFilterLayout = new javax.swing.GroupLayout(pnlFilter);
        pnlFilter.setLayout(pnlFilterLayout);
        pnlFilterLayout.setHorizontalGroup(
            pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFilterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(lblFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFrom, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                .addGap(15, 15, 15)
                .addComponent(lblTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTo, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlFilterLayout.setVerticalGroup(
            pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFilterLayout.createSequentialGroup()
                .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lblHeader, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlFilterLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblTo, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                            .addComponent(lblFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(txtFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(txtTo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

		pnlChart.setLayout(new java.awt.BorderLayout());

		tblList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblList.setAutoCreateRowSorter(true);
		tblList.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

		}, new String[] { "Ngày", "Doanh thu", "Chi tiêu" }) {
			Class[] types = new Class[] { java.util.Date.class, Long.class, Long.class };
			boolean[] canEdit = new boolean[] { false, false, false };

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		scrollTable.setViewportView(tblList);

		javax.swing.GroupLayout pnlTableLayout = new javax.swing.GroupLayout(pnlTable);
		pnlTable.setLayout(pnlTableLayout);
		pnlTableLayout.setHorizontalGroup(pnlTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(scrollTable));
		pnlTableLayout.setVerticalGroup(pnlTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(scrollTable, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE));

		pnlSummary.setPreferredSize(new java.awt.Dimension(770, 30));

		lblThu.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
		lblThu.setText("Tổng thu:");
		lblThu.setPreferredSize(new java.awt.Dimension(160, 30));
		lblThu.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

		lblChi.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
		lblChi.setText("Tổng chi:");
		lblChi.setPreferredSize(new java.awt.Dimension(160, 30));
		lblChi.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

		javax.swing.GroupLayout pnlSummaryLayout = new javax.swing.GroupLayout(pnlSummary);
		pnlSummary.setLayout(pnlSummaryLayout);
		pnlSummaryLayout
				.setHorizontalGroup(pnlSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(pnlSummaryLayout.createSequentialGroup()
								.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblThu, javax.swing.GroupLayout.PREFERRED_SIZE, 158,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(33, 33, 33)
								.addComponent(lblChi, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addContainerGap()));
		pnlSummaryLayout
				.setVerticalGroup(pnlSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(pnlSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(lblThu, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblChi, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGap(15, 15, 15)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
								.addComponent(pnlSummary, javax.swing.GroupLayout.DEFAULT_SIZE, 670, Short.MAX_VALUE)
								.addComponent(pnlTable, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(pnlChart, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(pnlFilter, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGap(15, 15, 15)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGap(15, 15, 15)
						.addComponent(pnlFilter, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(pnlChart, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(pnlTable, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(pnlSummary, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(15, 15, 15)));

		pack();
	}
}
