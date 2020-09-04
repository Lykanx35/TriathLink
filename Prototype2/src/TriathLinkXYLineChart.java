import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.image.RescaleOp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.concurrent.TimeUnit;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.util.RelativeDateFormat;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class TriathLinkXYLineChart extends JFrame{

	public TriathLinkXYLineChart(ArrayList<AnalyzedRecordData> analyzedRecords) {

		super("TriathLink");
		super.setSize(1500, 800);
		super.setLocationRelativeTo(null);
		super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		super.setVisible(true);


		// Create dataset
		XYDataset dataset = createDataset(analyzedRecords);

		// Create chart
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				"Triathlink",
				"Time [s]",
				"Speed [m/s]",
				dataset,
				true, true, false);

		Date start = analyzedRecords.get(0).getRecordData().getTimestamp();


		RelativeDateFormat format = new RelativeDateFormat(start.getTime());


		format.setHourSuffix(":");
		format.setMinuteSuffix(":");
		format.setSecondSuffix("");

		DecimalFormat padded = new DecimalFormat("00");
		format.setHourFormatter(padded);
		format.setMinuteFormatter(padded);
		format.setSecondFormatter(padded);

		XYPlot plot = chart.getXYPlot();
		DateAxis xAxis = (DateAxis) plot.getDomainAxis();
		xAxis.setStandardTickUnits(DateAxis.createStandardDateTickUnits());
		xAxis.setDateFormatOverride(format);


		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
		renderer.setSeriesPaint( 0 , Color.BLACK );
		renderer.setSeriesPaint( 1 , Color.BLUE );
		renderer.setSeriesPaint( 2 , Color.BLACK );
		renderer.setSeriesPaint( 3 , Color.YELLOW );
		renderer.setSeriesPaint( 4 , Color.BLACK );
		renderer.setSeriesPaint( 5 , Color.RED );
		renderer.setSeriesPaint( 6 , Color.BLACK );
		renderer.setDefaultShapesVisible(false);
		plot.setRenderer( renderer ); 

		// Create Panel
		ChartPanel panel = new ChartPanel(chart);
		setContentPane(panel);

	}

	private XYDataset createDataset(ArrayList<AnalyzedRecordData> analyzedRecords) {


		XYSeriesCollection dataset = new XYSeriesCollection();

		XYSeries recordSeriesNotStarted = new XYSeries("NotStarted");
		XYSeries recordSeriesSwimming = new XYSeries("Swimming");
		XYSeries recordSeriesFirstTransition = new XYSeries("FirstTransition");
		XYSeries recordSeriesCycling = new XYSeries("Cycling");
		XYSeries recordSeriesSecondTransition = new XYSeries("SecondTransition");
		XYSeries recordSeriesRunning = new XYSeries("Running");
		XYSeries recordSeriesFinished = new XYSeries("Finished");


		for (AnalyzedRecordData temp : analyzedRecords){
			Date currentDate = temp.getRecordData().getTimestamp();

			switch(temp.getSportType()){
			case NOT_STARTED:
				recordSeriesNotStarted.add(currentDate.getTime(), temp.getRecordData().getSpeed());
				break;
			case SWIMMING:
				recordSeriesSwimming.add(currentDate.getTime(), temp.getRecordData().getSpeed());
				break;
			case FIRST_TRANSITION:
				recordSeriesFirstTransition.add(currentDate.getTime(), temp.getRecordData().getSpeed());
				break;
			case CYCLING:
				recordSeriesCycling.add(currentDate.getTime(), temp.getRecordData().getSpeed());
				break;
			case RUNNING:
				recordSeriesRunning.add(currentDate.getTime(), temp.getRecordData().getSpeed());
				break;
			case SECOND_TRANSITION:
				recordSeriesSecondTransition.add(currentDate.getTime(), temp.getRecordData().getSpeed());
				break;
			case FINISHED:
				recordSeriesFinished.add(currentDate.getTime(), temp.getRecordData().getSpeed());
				break;
			}
		}

		//Add series to dataset
		dataset.addSeries(recordSeriesNotStarted);
		dataset.addSeries(recordSeriesSwimming);
		dataset.addSeries(recordSeriesFirstTransition);
		dataset.addSeries(recordSeriesCycling);
		dataset.addSeries(recordSeriesSecondTransition);
		dataset.addSeries(recordSeriesRunning);
		dataset.addSeries(recordSeriesFinished);

		return dataset;
	}
}