package net.codejava.servlet;
 
import java.io.IOException;
import java.util.Random;
import java.io.PrintWriter;
import SheetPackageTest.SheetsQuickstart;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.api.services.sheets.v4.Sheets;

import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    


public class QuickServlet extends HttpServlet {
    /**
     * this life-cycle method is invoked when this servlet is first accessed
     * by the client
     */

    public void init(ServletConfig config) {
        System.out.println("Servlet is being initialized");
        
    }
 
    /**
     * handles HTTP GET request
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        PrintWriter writer = response.getWriter();
        writer.println("<html>Welcome to Attendance Tracker</html>");
    	writer.flush();  		
    }
    
    /**
     * handles HTTP POST request
     * @throws ServletException 
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    		throws IOException, ServletException, NullPointerException {
    	
    	PrintWriter writer = response.getWriter();
    	String formName = request.getParameter("FormName");
    	RequestDispatcher rd;
 
    	if (formName.equals("StudentEntry")) {	
    		HttpSession session = request.getSession();
    		String paramID = request.getParameter("Student ID");
    	    String paramKey = request.getParameter("Key");
    	    String paramComment = request.getParameter("Comment");
    	    String[]arr = null;
    	    
    	    String action = request.getParameter("action");
    	    String[] classId = action.split(",");
    	    String course = classId[0];
    	    int sheetId = Integer.parseInt(classId[1]);
    	    if(session.getAttribute("student") == null) {
    	    	arr = SheetsQuickstart.checkIn(course ,sheetId ,paramKey ,paramID ,paramComment);
    	    	if (arr[0].equals("t")) {
                    session.setAttribute("student", paramID);
                    request.setAttribute("stuID", paramID);
                    request.setAttribute("key", paramKey);
                    rd = request.getRequestDispatcher("Receipt.jsp");
                    rd.forward(request, response);
        	    }
        	    else {
        	    	rd = request.getRequestDispatcher("Invalid.html");
                    rd.forward(request, response);
        	    }
    	    }
    	    else {
    	    	if(session.getAttribute("student").equals(paramID)) {
    	    		arr = SheetsQuickstart.checkIn(course ,sheetId ,paramKey ,paramID ,paramComment);
        	    	if (arr[0].equals("t")) {
        	    		//session.setAttribute("student", paramID);
                        request.setAttribute("stuID", paramID);
                        request.setAttribute("key", paramKey);
                        rd = request.getRequestDispatcher("Receipt.jsp");
                        rd.forward(request, response);
            	    }
            	    else {
            	    	rd = request.getRequestDispatcher("Invalid.html");
                        rd.forward(request, response);
            	    }
    	    	}
    	    	else {
    	    		rd = request.getRequestDispatcher("AttendanceError.html");
    	    		rd.forward(request, response);
    	    	}
    	    }
    	
    	}else if(formName.equals("TeacherLogin")){//Teacher.jsp 
    	    String teacher = request.getParameter("userTeacher");
    	    String password = request.getParameter("passTeacher");
            
    	    if(SheetsQuickstart.checkLogin(teacher, password)) { 	
                HttpSession session = request.getSession();
                session.setAttribute("teacher", teacher);
    	    	rd = request.getRequestDispatcher("Course.jsp");
                rd.forward(request, response); 
            }else {
            	rd = request.getRequestDispatcher("Invalid.html");
                rd.forward(request, response);
	    	} 
    	}else if(formName.equals("CourseSelect")) { //Course.jsp   
    		String course = request.getParameter("myList");    	    	

    		if(course.equals("CSC20")) {
    			//arr[0] has value for the timer
    			//arr size is 2
    			String[]arr = SheetsQuickstart.getTkey("CSC20",1996570317);
	    		request.setAttribute("randomKey", arr[1] );
	    	    request.getRequestDispatcher("RandKey.jsp").forward(request, response);
    		}
    		else if(course.equals("CSC130")) {
    			String[]arr = SheetsQuickstart.getTkey("CSC30",1472870202);
    			request.setAttribute("randomKey", arr[1] );
	    	    request.getRequestDispatcher("RandKey.jsp").forward(request, response);
    		}
    		else if(course.equals("CSC131")) {
    			String[]arr = SheetsQuickstart.getTkey("CSC131",0);
    			request.setAttribute("randomKey", arr[1] );
	    	    request.getRequestDispatcher("RandKey.jsp").forward(request, response);
    		}
    		else if(course.equals("CSC133")) {
    			String[]arr = SheetsQuickstart.getTkey("CSC133",756897706);
    			request.setAttribute("randomKey", arr[1] );
	    	    request.getRequestDispatcher("RandKey.jsp").forward(request, response);
    		}
    		else if(course.equals("CSC135")) {
    			String[]arr = SheetsQuickstart.getTkey("CSC135",543682871);
    			request.setAttribute("randomKey", arr[1] );
	    	    request.getRequestDispatcher("RandKey.jsp").forward(request, response);
    		} 
    	}else {
    		writer.println("Key has not been created");
        	writer.flush();
    	}
    }

    /**
     * this life-cycle method is invoked when the application or the server
     * is shutting down
     */
    public void destroy() {
        System.out.println("Servlet is being destroyed");
    }
}