/*
 * $Id$
 *
 * Last changed on : $Date$
 * Last changed by : $Author$
 */
package com.samaxes.webreport.birt;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.IPlatformContext;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.framework.PlatformServletContext;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;

import com.samaxes.webreport.common.Constants;

/**
 * BIRT Report Engine.
 * 
 * @author : samaxes
 * @version : $Revision$
 */
public class BirtEngine {

    private static IReportEngine birtEngine = null;

    private static Properties configProps = new Properties();

    private final static String configFile = Constants.BIRT_CONFIG_FILE;

    protected static Logger logger = Logger.getLogger("org.eclipse.birt");

    /**
     * Initialize BIRT Report Engine configuration.
     */
    public static synchronized void initBirtConfig() {
        loadEngineProps();
    }

    /**
     * Gets the BIRT Report Engine.
     * 
     * @param sc ServletContext
     * @return IReportEngine
     */
    @SuppressWarnings("unchecked")
    public static synchronized IReportEngine getBirtEngine(ServletContext sc) {
        if (birtEngine == null) {
            EngineConfig config = new EngineConfig();
            if (configProps != null) {
                String logLevel = configProps.getProperty("logLevel");
                Level level = Level.OFF;
                if ("SEVERE".equalsIgnoreCase(logLevel)) {
                    level = Level.SEVERE;
                } else if ("WARNING".equalsIgnoreCase(logLevel)) {
                    level = Level.WARNING;
                } else if ("INFO".equalsIgnoreCase(logLevel)) {
                    level = Level.INFO;
                } else if ("CONFIG".equalsIgnoreCase(logLevel)) {
                    level = Level.CONFIG;
                } else if ("FINE".equalsIgnoreCase(logLevel)) {
                    level = Level.FINE;
                } else if ("FINER".equalsIgnoreCase(logLevel)) {
                    level = Level.FINER;
                } else if ("FINEST".equalsIgnoreCase(logLevel)) {
                    level = Level.FINEST;
                } else if ("OFF".equalsIgnoreCase(logLevel)) {
                    level = Level.OFF;
                }

                config.setLogConfig(configProps.getProperty("logDirectory"), level);
            }

            config.setEngineHome("");
            config.getAppContext().put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, BirtEngine.class.getClassLoader());
            IPlatformContext context = new PlatformServletContext(sc);
            config.setPlatformContext(context);

            try {
                Platform.startup(config);
            } catch (BirtException e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
            }

            IReportEngineFactory factory = (IReportEngineFactory) Platform
                    .createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
            birtEngine = factory.createReportEngine(config);

            // setup XLS emitter configuration
            Map<String, Comparable> xlsConfig = new HashMap<String, Comparable>();
            // Check out constants in XlsEmitterConfig.java for more configuration detail.
            xlsConfig.put(Constants.XLS_EMITTER_KEY_FIXED_COLUMN_WIDTH, new Integer(30));
            xlsConfig.put(Constants.XLS_EMITTER_KEY_SHOW_GRID_LINES, new Boolean(false));
            // Associate the configuration with the XLS output format.
            config.setEmitterConfiguration(Constants.XLS_FORMAT, xlsConfig);
        }
        return birtEngine;
    }

    /**
     * Destroys the BIRT Report Engine.
     */
    public static synchronized void destroyBirtEngine() {
        if (birtEngine == null) {
            return;
        }
        birtEngine.destroy();
        Platform.shutdown();
        birtEngine = null;
    }

    /**
     * Creates and returns a copy of this object.
     * 
     * @return a clone of this instance.
     * @exception CloneNotSupportedException if the object's class does not support the <code>Cloneable</code>
     *            interface. Subclasses that override the <code>clone</code> method can also throw this exception to
     *            indicate that an instance cannot be cloned.
     */
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /**
     * Loads the Engine properties.
     */
    private static void loadEngineProps() {
        try {
            // Config File must be in classpath
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            InputStream in = null;
            in = cl.getResourceAsStream(configFile);
            configProps.load(in);
            in.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

}
