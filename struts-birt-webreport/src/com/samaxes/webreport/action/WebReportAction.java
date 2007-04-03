/*
 * $Id$
 *
 * Last changed on : $Date$
 * Last changed by : $Author$
 */
package com.samaxes.webreport.action;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
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
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;

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

        // setup image directory
        HTMLRenderContext renderContext = new HTMLRenderContext();
        renderContext.setBaseImageURL(request.getContextPath() + "/images");
        renderContext.setImageDirectory(sc.getRealPath("/images"));

        logger.log(Level.FINE, "image directory " + sc.getRealPath("/images"));

        HashMap<String, HTMLRenderContext> contextMap = new HashMap<String, HTMLRenderContext>();
        contextMap.put(EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT, renderContext);

        IReportRunnable design;
        try {
            // Open report design
            design = birtReportEngine.openReportDesign(sc.getRealPath("/reports") + "/" + reportName);
            // create task to run and render report
            IRunAndRenderTask task = birtReportEngine.createRunAndRenderTask(design);
            task.setAppContext(contextMap);

            // set output options
            HTMLRenderOption options = new HTMLRenderOption();
            options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_HTML);
            options.setEmbeddable(true);
            options.setOutputStream(out);
            task.setRenderOption(options);

            // run report
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

        // setup image directory
        HTMLRenderContext renderContext = new HTMLRenderContext();
        renderContext.setBaseImageURL(request.getContextPath() + "/images");
        renderContext.setImageDirectory(sc.getRealPath("/images"));

        logger.log(Level.FINE, "image directory " + sc.getRealPath("/images"));

        HashMap<String, HTMLRenderContext> contextMap = new HashMap<String, HTMLRenderContext>();
        contextMap.put(EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT, renderContext);

        IReportRunnable design;
        try {
            // Open report design
            design = birtReportEngine.openReportDesign(sc.getRealPath("/reports") + "/" + reportName);
            // create task to run and render report
            IRunAndRenderTask task = birtReportEngine.createRunAndRenderTask(design);
            task.setAppContext(contextMap);

            // set output options
            HTMLRenderOption options = new HTMLRenderOption();
            options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_PDF);
            options.setOutputStream(response.getOutputStream());
            task.setRenderOption(options);

            // run report
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

        // setup image directory
        HTMLRenderContext renderContext = new HTMLRenderContext();
        renderContext.setBaseImageURL(request.getContextPath() + "/images");
        renderContext.setImageDirectory(sc.getRealPath("/images"));

        logger.log(Level.FINE, "image directory " + sc.getRealPath("/images"));

        HashMap<String, HTMLRenderContext> contextMap = new HashMap<String, HTMLRenderContext>();
        contextMap.put(EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT, renderContext);

        IReportRunnable design;
        try {
            // Open report design
            design = birtReportEngine.openReportDesign(sc.getRealPath("/reports") + "/" + reportName);
            // create task to run and render report
            IRunAndRenderTask task = birtReportEngine.createRunAndRenderTask(design);
            task.setAppContext(contextMap);

            // set output options
            HTMLRenderOption options = new HTMLRenderOption();
            options.setOutputFormat(Constants.XLS_FORMAT);
            options.setOutputStream(response.getOutputStream());
            task.setRenderOption(options);

            // run report
            task.run();
            task.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new ServletException(e);
        }
    }

}
