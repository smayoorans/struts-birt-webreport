/*
 * $Id$
 *
 * Last changed on : $Date$
 * Last changed by : $Author$
 */
package com.samaxes.webreport.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.HTMLServerImageHandler;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;

import com.samaxes.webreport.birt.BirtEngine;

/**
 * WebReportServlet for report generation.
 * 
 * @author : samaxes
 * @version : $Revision$
 */
public class WebReportServlet extends HttpServlet {

    private static final long serialVersionUID = -3656202991475539637L;

    private IReportEngine birtReportEngine = null;

    protected static Logger logger = Logger.getLogger("org.eclipse.birt");

    /**
     * Constructor of the object.
     */
    public WebReportServlet() {
        super();
    }

    /**
     * Destruction of the servlet.
     */
    public void destroy() {
        super.destroy();
        BirtEngine.destroyBirtEngine();
    }

    /**
     * The doGet method of the servlet. <br />
     * 
     * This method is called when a form has its tag value method equals to get.
     * 
     * @param req the request send by the client to the server
     * @param resp the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException if an error occurred
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // get report name and launch the engine
        resp.setContentType("text/html");
        String reportName = req.getParameter("ReportName");
        ServletContext sc = req.getSession().getServletContext();
        this.birtReportEngine = BirtEngine.getBirtEngine(sc);

        IReportRunnable design;
        try {
            // Open report design
            design = birtReportEngine.openReportDesign(sc.getRealPath("/reports") + "/" + reportName);
            // create task to run and render report
            IRunAndRenderTask task = birtReportEngine.createRunAndRenderTask(design);

            // set output options
            HTMLRenderOption options = new HTMLRenderOption();
            options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_HTML);
            options.setOutputStream(resp.getOutputStream());

            // set the image handler to a HTMLServerImageHandler if you plan on using the base image url.
            options.setImageHandler(new HTMLServerImageHandler());
            options.setBaseImageURL(req.getContextPath() + "/images");
            options.setImageDirectory(sc.getRealPath("/images"));

            // run report
            task.setRenderOption(options);
            task.run();
            task.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new ServletException(e);
        }
    }

    /**
     * The doPost method of the servlet. <br>
     * 
     * This method is called when a form has its tag value method equals to post.
     * 
     * @param request the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException if an error occurred
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        out.println("<HTML>");
        out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
        out.println("  <BODY>");
        out.println(" Post Not Supported");
        out.println("  </BODY>");
        out.println("</HTML>");
        out.flush();
        out.close();
    }

    /**
     * Initialization of the servlet. <br>
     * 
     * @throws ServletException if an error occurred
     */
    public void init() throws ServletException {
        BirtEngine.initBirtConfig();
    }

}
