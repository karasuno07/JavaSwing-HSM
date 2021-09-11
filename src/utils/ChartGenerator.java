package utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.HorizontalAlignment;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.ui.VerticalAlignment;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.xy.XYDataset;

public class ChartGenerator {

	public static JPanel createPieChart(String chartTitle, HashMap<String, Object> map) {
		DefaultPieDataset<String> dataset = new DefaultPieDataset<String>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			dataset.setValue(key, (double) value);
		}
		JFreeChart chart = ChartFactory.createPieChart(null, dataset, true, true, false);
		ChartPanel panel = new ChartPanel(chart);;
		chart.setBackgroundPaint(Theme.getColor("Chart.background"));
		chart.getPlot().setBackgroundPaint(Theme.getColor("Chart.background"));
		chart.getLegend().setBackgroundPaint(Theme.getColor("Chart.background"));
		chart.getLegend().setItemPaint(Theme.getColor("Chart.foreground"));
		chart.getPlot().setOutlineVisible(false);
		chart.setBorderPaint(new Color(200, 200, 200));
		chart.setBorderVisible(true);
		chart.setTitle(new TextTitle(chartTitle, new Font("Tahoma", 1, 14), Theme.getColor("Chart.foreground"), RectangleEdge.TOP,
				HorizontalAlignment.CENTER, VerticalAlignment.CENTER, new RectangleInsets(5, 5, 5, 5)));

		return panel;
	}

	public static JPanel createXYLineChart(String chartTitle, String XAxixTitle, String YAxisTitle,
			HashMap<String, List<TimeSeriesDataItem>> map) {
		XYDataset dataset = createXYDataset(map);
		JFreeChart chart = ChartFactory.createXYLineChart(chartTitle, XAxixTitle, YAxisTitle, dataset,
				PlotOrientation.VERTICAL, true, true, false);
		ChartPanel panel = new ChartPanel(chart);
		// Tinh chỉnh các thuộc tính chung của biểu đồ
		chart.setBackgroundPaint(Theme.getColor("Chart.background"));
		chart.getPlot().setBackgroundPaint(Theme.getColor("Chart.background"));
		chart.getLegend().setBackgroundPaint(Theme.getColor("Chart.background"));
		chart.getLegend().setItemPaint(Theme.getColor("Chart.foreground"));
		chart.getPlot().setOutlineVisible(false);
		chart.setBorderPaint(new Color(200, 200, 200));
		chart.setBorderVisible(true);
		chart.setTitle(new TextTitle(chartTitle, new Font("Tahoma", 1, 15), Theme.getColor("Chart.foreground"), RectangleEdge.TOP,
				HorizontalAlignment.CENTER, VerticalAlignment.CENTER, new RectangleInsets(5, 5, 5, 5)));
		XYPlot plot = chart.getXYPlot();
		// Tinh chỉnh hiển thị của các đường trong biểu đồ
		plot.getRangeAxis().setTickLabelPaint(Theme.getColor("Chart.foreground"));
		plot.getDomainAxis().setTickLabelPaint(Theme.getColor("Chart.foreground"));
		plot.getRangeAxis().setLabelFont(new Font("Tahoma", 1, 14));
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesPaint(0, Color.RED);
		renderer.setSeriesPaint(1, Color.GREEN);
		renderer.setSeriesPaint(2, Color.YELLOW);
		renderer.setSeriesStroke(0, new BasicStroke(2.0f));
		renderer.setSeriesStroke(1, new BasicStroke(2.0f));
		renderer.setSeriesStroke(2, new BasicStroke(2.0f));
		plot.setRenderer(renderer);
		// Hiển thị ngày ở X Axis
		setDateAxis(plot, true, XAxixTitle);
		//
		return panel;
	}

	private static XYDataset createXYDataset(HashMap<String, List<TimeSeriesDataItem>> map) {
		final TimeSeriesCollection dataset = new TimeSeriesCollection();
		for (Map.Entry<String, List<TimeSeriesDataItem>> entry : map.entrySet()) {
			TimeSeries series = new TimeSeries(entry.getKey());
			List<TimeSeriesDataItem> values = entry.getValue();
			for (TimeSeriesDataItem item : values) {
				series.add(item);
			}
			dataset.addSeries(series);
		}
		return dataset;
	}

	private static void setDateAxis(XYPlot plot, boolean XY, String labelTitle) {
		DateAxis dateAxis = new DateAxis();
		dateAxis.setDateFormatOverride(new SimpleDateFormat("dd/MM"));
		dateAxis.setLabel(labelTitle);
		dateAxis.setLabelFont(new Font("SansSerif", 1, 13));
		dateAxis.calculateHighestVisibleTickValue(new DateTickUnit(DateTickUnitType.DAY, 31));
		dateAxis.calculateLowestVisibleTickValue(new DateTickUnit(DateTickUnitType.DAY, 1));
		dateAxis.setTickUnit(new DateTickUnit(DateTickUnitType.DAY, 1));
		dateAxis.setTickLabelFont(new Font("Tahoma", 1, 12));
		dateAxis.setTickLabelPaint(Theme.getColor("Chart.foreground"));
		if (XY) {
			plot.setDomainAxis(dateAxis);
		} else {
			plot.setRangeAxis(dateAxis);
		}
	}

	public static JPanel createStackedBarChart(String chartTitle, String XAxisTitle, String YAxisTitle,
			HashMap<String, HashMap<String, Long>> map) {
		DefaultCategoryDataset dataset = (DefaultCategoryDataset) createBarDataset(map);
		JFreeChart chart = ChartFactory.createStackedBarChart(chartTitle, XAxisTitle, YAxisTitle, dataset,
				PlotOrientation.VERTICAL, true, true, false);
		ChartPanel panel = new ChartPanel(chart);
		chart.setBackgroundPaint(Theme.getColor("Chart.background"));
		chart.getPlot().setBackgroundPaint(Theme.getColor("Chart.background"));
		chart.getLegend().setBackgroundPaint(Theme.getColor("Chart.background"));
		chart.getLegend().setItemPaint(Theme.getColor("Chart.foreground"));
		chart.getPlot().setOutlineVisible(false);
		chart.setBorderPaint(new Color(200, 200, 200));
		chart.setBorderVisible(true);
		chart.setTitle(new TextTitle(chartTitle, new Font("Tahoma", 1, 15), Theme.getColor("Chart.foreground"), RectangleEdge.TOP,
				HorizontalAlignment.CENTER, VerticalAlignment.CENTER, new RectangleInsets(5, 5, 5, 5)));
		CategoryPlot plot = chart.getCategoryPlot();
		plot.getRangeAxis().setTickLabelPaint(Theme.getColor("Chart.foreground"));
		plot.getDomainAxis().setTickLabelPaint(Theme.getColor("Chart.foreground"));
		return panel;
	}
	
	public static JPanel createGroupBarChart(String chartTitle, String XAxisTitle, String YAxisTitle,
			HashMap<String, HashMap<String, Long>> map) {
		DefaultCategoryDataset dataset = (DefaultCategoryDataset) createBarDataset(map);
		JFreeChart chart = ChartFactory.createBarChart(chartTitle, XAxisTitle, YAxisTitle, dataset,
				PlotOrientation.VERTICAL, true, true, false);
		ChartPanel panel = new ChartPanel(chart);
		chart.setBackgroundPaint(Theme.getColor("Chart.background")); 
		chart.getPlot().setBackgroundPaint(Theme.getColor("Chart.background"));
		chart.getLegend().setBackgroundPaint(Theme.getColor("Chart.background"));
		chart.getLegend().setItemPaint(Theme.getColor("Chart.foreground"));
		chart.getPlot().setOutlineVisible(false);
		chart.setBorderPaint(new Color(200, 200, 200));
		chart.setBorderVisible(true);
		chart.setTitle(new TextTitle(chartTitle, new Font("Tahoma", 1, 14), Theme.getColor("Chart.foreground"), RectangleEdge.TOP,
				HorizontalAlignment.CENTER, VerticalAlignment.CENTER, new RectangleInsets(5, 5, 5, 5)));
		CategoryPlot plot = chart.getCategoryPlot();
		plot.getRangeAxis().setTickLabelPaint(Theme.getColor("Chart.foreground"));
		plot.getDomainAxis().setTickLabelPaint(Theme.getColor("Chart.foreground"));
		plot.getRangeAxis().setMinorTickCount(50000);
		BarRenderer br = (BarRenderer) plot.getRenderer();
		br.setItemMargin(0);
		return panel;
	}

	private static CategoryDataset createBarDataset(HashMap<String, HashMap<String, Long>> map) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (Map.Entry<String, HashMap<String, Long>> entry : map.entrySet()) {
			String key = entry.getKey();
			HashMap<String, Long> values = entry.getValue();
			for (Map.Entry<String, Long> value : values.entrySet()) {
				dataset.addValue(value.getValue(), key, value.getKey());
			}
		}
		return dataset;
	}
}
