/*
 * $Id$
 *
 * Last changed on : $Date$
 * Last changed by : $Author$
 */
package com.samaxes.webreport.plugin;

import javax.servlet.ServletException;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

import com.samaxes.webreport.birt.BirtEngine;

/**
 * BIRT Report Engine initialization and shutdown Plugin.
 * 
 * @author : samaxes
 * @version : $Revision$
 */
public class BirtInitializationPlugin implements PlugIn {

    /**
     * Initialization of the servlet. <br />
     * 
     * @param servlet ActionServlet
     * @param conf ModuleConfig
     * @throws ServletException if an error occure
     */
    public void init(ActionServlet servlet, ModuleConfig conf) throws ServletException {
        BirtEngine.initBirtConfig();
    }

    /**
     * Destruction of the servlet.
     */
    public void destroy() {
        BirtEngine.destroyBirtEngine();
    }

}
