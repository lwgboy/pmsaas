package ch.zhaw.walj.projectmanagement;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/newProject")
public class NewProject extends HttpServlet {
	
	// Variables for the database connection
	String					url			= "jdbc:mysql://localhost:3306/";
	String					dbName		= "projectmanagement";
	String					userName	= "Janine";
	String					password	= "test123";
	
	// create a new DB connection
	private DBConnection	con			= new DBConnection(url, dbName, userName, password);
	
	// Variables for POST parameters
	private int				pID;
	private String			pName;
	private String			pShortname;
	private String			pBudget;
	private String			pCurrency;
	private String			pStart;
	private String			pEnd;
	private String			pPartners;
	private String			wpName[];
	private String			wpStart[];
	private String			wpEnd[];
	private String			taskName[];
	private String			taskStart[];
	private String			taskEnd[];
	private String			taskPM[];
	private String			taskBudget[];
	private String			taskWP[];
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// set response content type to HTML
		response.setContentType("text/html;charset=UTF8");
		
		// get the parameters
		pName = request.getParameter("pName");
		pShortname = request.getParameter("pShortname");
		pBudget = request.getParameter("pBudget");
		pCurrency = request.getParameter("pCurrency");
		pStart = request.getParameter("pStart");
		pEnd = request.getParameter("pEnd");
		pPartners = request.getParameter("pPartners");
		wpName = request.getParameterValues("wpName");
		wpStart = request.getParameterValues("wpStart");
		wpEnd = request.getParameterValues("wpEnd");
		taskName = request.getParameterValues("taskName");
		taskStart = request.getParameterValues("taskStart");
		taskEnd = request.getParameterValues("taskEnd");
		taskPM = request.getParameterValues("taskPM");
		taskBudget = request.getParameterValues("taskBudget");
		taskWP = request.getParameterValues("taskWP");
		
		final PrintWriter out = response.getWriter();
		
		// show the new project to the user
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<body>");
		out.println("<p>Neues Projekt:</p>");
		out.println("<p>Projektname: " + pName + "</p>");
		out.println("<p>Projekt Kurzname: " + pShortname + "</p>");
		out.println("<p>Budget: " + pCurrency + " " + pBudget + "</p>");
		out.println("<p>Projektstart: " + pStart + "</p>");
		out.println("<p>Projektende: " + pEnd + "</p>");
		out.println("<p>Projektpartner: " + pPartners + "</p>");
		
		// create a new project in the DB
		try {
			pID = con.newProject(pName, pShortname, pBudget, pCurrency, pStart, pEnd, pPartners);
		} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// show the new workpackages and create them in the DB
		out.println("<p><br>Workpackages:</p>");
		out.println("<table><tr><th>Name</th><th>Start</th><th>End</th></tr>");
		for (int i = 0; i < wpName.length; ++i) {
			
			try {
				con.newWorkpackage(pID, wpName[i], wpStart[i], wpEnd[i]);
			} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			out.println("</tr>");
			out.println("<td>" + wpName[i] + "</td>");
			out.println("<td>" + wpStart[i] + "</td>");
			out.println("<td>" + wpEnd[i] + "</td>");
			out.println("</tr>");
		}
		out.println("</table>");
		
		// show the new tasks and create them in the DB
		out.println("<p><br>Tasks:</p>");
		out.println(
				"<table><tr><th>Name</th><th>Start</th><th>End</th><th>PMs</th><th>Budget</th><th>Workpackage</th></tr>");
		for (int i = 0; i < taskName.length; i++) {
			
			try {
				con.newTask(pID, taskWP[i], taskName[i], taskStart[i], taskEnd[i], taskPM[i], taskBudget[i]);
			} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			out.println("</tr>");
			out.println("<td>" + taskName[i] + "</td>");
			out.println("<td>" + taskStart[i] + "</td>");
			out.println("<td>" + taskEnd[i] + "</td>");
			out.println("<td>" + taskPM[i] + "</td>");
			out.println("<td>" + taskBudget[i] + "</td>");
			out.println("<td>" + taskWP[i] + "</td>");
			out.println("</tr>");
		}
		out.println("</table>");
		
		out.println("</body>");
		out.println("</html>");
		
		// close the DB connection
		con.closeConnection();
	}
}