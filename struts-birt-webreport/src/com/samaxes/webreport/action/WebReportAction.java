/*
 * $Id$
 *
 * Last changed on : $Date$
 * Last changed by : $Author$
 */
package com.samaxes.webreport.action;

import java.io.ByteArrayOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.MappingDispatchAction;
import org.eclipse.birt.report.engine.api.EXCELRenderOption;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.HTMLServerImageHandler;
import org.eclipse.birt.report.engine.api.IExcelRenderOption;
import org.eclipse.birt.report.engine.api.IHTMLRenderOption;
import org.eclipse.birt.report.engine.api.IPDFRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;

import com.samaxes.webreport.birt.BirtEngine;
import com.samaxes.webreport.common.Constants;

/**
 * Actions related to the reports generation.
 * 
 * @author : samaxes
 * @version : $Revision$
 */
public class WebReportAction extends MappingDispatchAction {

    private IReportEngine birtReportEngine = null;

    protected static Logger logger = Logger.getLogger("org.eclipse.birt");

    /**
     * The html report action. <br />
     * Gets the report's html output stream.
     * 
     * @param actionMapping ActionMapping
     * @param actionForm ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     */
    public ActionForward htmlReportAction(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            out = renderReportPage(request);
        } catch (ServletException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        request.setAttribute("reportHTML", out);

        return actionMapping.findForward(Constants.FORWARD_SUCCESS);
    }

    /**
     * Generates the html report.
     * 
     * @param request HttpServletRequest
     * @return ByteArrayOutputStreamwith the report generated
     * @throws ServletException
     */
    private ByteArrayOutputStream renderReportPage(HttpServletRequest request) throws ServletException {

        // get report name and launch the engine
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String reportName = request.getParameter("ReportName");
        ServletContext sc = request.getSession().getServletContext();
        this.birtReportEngine = BirtEngine.getBirtEngine(sc);

        IReportRunnable design;
        try {
            // Open report design
            design = birtReportEngine.openReportDesign(sc.getRealPath("/reports") + "/" + reportName);
            // create task to run and render report
            IRunAndRenderTask task = birtReportEngine.createRunAndRenderTask(design);

            // set output options
            IHTMLRenderOption options = new HTMLRenderOption();
            options.setOutputFormat(IHTMLRenderOption.OUTPUT_FORMAT_HTML);
            options.setEmbeddable(true);
            options.setOutputStream(out);

            // set the image handler to a HTMLServerImageHandler if you plan on using the base image url.
            options.setImageHandler(new HTMLServerImageHandler());
            options.setBaseImageURL(request.getContextPath() + "/images");
            options.setImageDirectory(sc.getRealPath("/images"));

            // run report
            task.setRenderOption(options);
            task.run();
            task.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new ServletException(e);
        }

        return out;
    }

    /**
     * The pdf report action. <br />
     * Generates the pdf report.
     * 
     * @param actionMapping ActionMapping
     * @param actionForm ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException
     */
    public void pdfReportAction(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws ServletException {

        // get report name and launch the engine
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=WebReport.pdf"); // inline, attachment
        String reportName = request.getParameter("ReportName");
        ServletContext sc = request.getSession().getServletContext();
        this.birtReportEngine = BirtEngine.getBirtEngine(sc);

        IReportRunnable design;
        try {
            // Open report design
            design = birtReportEngine.openReportDesign(sc.getRealPath("/reports") + "/" + reportName);
            // create task to run and render report
            IRunAndRenderTask task = birtReportEngine.createRunAndRenderTask(design);

            // set output options
            IPDFRenderOption options = new PDFRenderOption();
            options.setOutputFormat(IPDFRenderOption.OUTPUT_FORMAT_PDF);
            options.setOutputStream(response.getOutputStream());

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
     * The excel report action. <br />
     * Generates the excel report.
     * 
     * @param actionMapping ActionMapping
     * @param actionForm ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException
     */
    public void xlsReportAction(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws ServletException {

        // get report name and launch the engine
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "inline; filename=WebReport.xls"); // inline, attachment
        String reportName = request.getParameter("ReportName");
        ServletContext sc = request.getSession().getServletContext();
        this.birtReportEngine = BirtEngine.getBirtEngine(sc);

        IReportRunnable design;
        try {
            // Open report design
            design = birtReportEngine.openReportDesign(sc.getRealPath("/reports") + "/" + reportName);
            // create task to run and render report
            IRunAndRenderTask task = birtReportEngine.createRunAndRenderTask(design);

            // set output options
            IExcelRenderOption options = new EXCELRenderOption();
            options.setOutputFormat(Constants.XLS_FORMAT);
            options.setOutputStream(response.getOutputStream());

            // run report
            task.setRenderOption(options);
            task.run();
            task.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new ServletException(e);
        }
    }

}
