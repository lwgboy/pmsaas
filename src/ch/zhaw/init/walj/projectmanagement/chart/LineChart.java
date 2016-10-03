package ch.zhaw.init.walj.projectmanagement.chart;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import ch.zhaw.init.walj.projectmanagement.util.Effort;
import ch.zhaw.init.walj.projectmanagement.util.Project;
import ch.zhaw.init.walj.projectmanagement.util.ProjectTask;

public class LineChart {

	private Project project;
	private ArrayList<ProjectTask> tasks = new ArrayList<ProjectTask>();
	
		
	public LineChart(Project project) {
		this.project = project;
    	tasks = project.getTasks();
			
	}
    
    private XYDataset createDataset() throws SQLException{
    	Effort effort = new Effort(tasks);
    	double plannedEffort = 0;
    	double bookedEffort = 0;
    	XYSeries planned = new XYSeries("Planned");
    	XYSeries booked = new XYSeries("Booked");
    	int projectMonths = project.getNumberOfMonths();
    	for (double i = 1; i <= projectMonths; i++){
        	effort.getPlannedEffort(i);
    		plannedEffort += effort.getPlannedEffort(i);
    		planned.add(i, plannedEffort);
    		
    		bookedEffort += effort.getBookedEffort(i);
    		booked.add(i, bookedEffort);
    			
    	}
    	XYSeriesCollection dataset = new XYSeriesCollection();
    	dataset.addSeries(booked);
        dataset.addSeries(planned);
        
    	
    	return dataset;
    }
    
	private XYSeriesCollection createDataset(int employee) throws SQLException {
		Effort effort = new Effort(tasks);
    	double bookedEffort = 0;
    	XYSeries booked = new XYSeries("Booked");
    	int projectMonths = project.getNumberOfMonths();
    	for (double i = 1; i <= projectMonths; i++){
    		
    		bookedEffort += effort.getBookedEffort(i, employee);
    		booked.add(i, bookedEffort);
    			
    	}
    	XYSeriesCollection dataset = new XYSeriesCollection();
    	dataset.addSeries(booked);
        
    	
    	return dataset;
	}
        
    public void createChart(String path) {
    	
    	XYSeriesCollection dataset = null;
		try {
			dataset = (XYSeriesCollection) createDataset();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	JFreeChart xylineChart = ChartFactory.createXYLineChart(
    	         "",
    	         "Month" ,
    	         "PM" ,
    	         dataset ,
    	         PlotOrientation.VERTICAL ,
    	         true , true , false);
    	
	    XYPlot plot = xylineChart.getXYPlot( );
	    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
	    renderer.setSeriesPaint( 0 , new Color(0, 101, 166) );
	    renderer.setSeriesPaint( 1 , new Color(0, 62, 102) );
	    plot.setRenderer(renderer); 
	    
	    
	    int width = 600; /* Width of the image */
	    int height = 400; /* Height of the image */ 
	    File lineChart = new File(path + "EffortProject" + project.getID() + ".jpg" ); 
	    try {
			ChartUtilities.saveChartAsJPEG( lineChart , xylineChart , width , height );
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    width = 1200; /* Width of the image */
	    height = 600; /* Height of the image */ 
	    lineChart = new File(path + "EffortProject" + project.getID() + "_large.jpg" ); 
	    try {
			ChartUtilities.saveChartAsJPEG( lineChart , xylineChart , width , height );
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    
    public void createChart(String path, int employee) {
    	XYSeriesCollection dataset = null;
		try {
			dataset = createDataset(employee);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	JFreeChart xylineChart = ChartFactory.createXYLineChart(
    	         "",
    	         "Month" ,
    	         "Hours" ,
    	         dataset ,
    	         PlotOrientation.VERTICAL ,
    	         true , true , false);
    	
	    XYPlot plot = xylineChart.getXYPlot( );
	    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
	    renderer.setSeriesPaint( 0 , new Color(0, 101, 166) );
	    renderer.setSeriesPaint( 1 , new Color(0, 62, 102) );
	    plot.setRenderer(renderer); 
	    
	    
	    int width = 1200; /* Width of the image */
	    int height = 600; /* Height of the image */ 
	    File lineChart = new File(path + "EffortProject" + project.getID() + "_Employee" + employee + ".jpg" ); 
	    try {
			ChartUtilities.saveChartAsJPEG( lineChart , xylineChart , width , height );
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    
    
}
