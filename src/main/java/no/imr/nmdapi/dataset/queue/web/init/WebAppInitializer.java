package no.imr.nmdapi.dataset.queue.web.init;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import no.imr.framework.logging.logback.initalize.InitalizeLogbackHandler;
import no.imr.framework.logging.slf4j.exceptions.LoggerInitalizationException;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

/**
 *
 * @author Terry Hannant <a5119>
 */
public class WebAppInitializer extends AbstractDispatcherServletInitializer {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(WebAppInitializer.class);

    @Override
    protected WebApplicationContext createServletApplicationContext() {
        return new AnnotationConfigWebApplicationContext();
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/*"};
    }

    @Override
    protected WebApplicationContext createRootApplicationContext() {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.scan("no.imr.nmdapi.dataset.queue.web.config", "no.imr.nmdapi.dataset.queue.web.processor");
        ctx.scan("no.imr.messaging.processor");
        ctx.scan("no.imr.nmdapi.dao.file.config");
        
        
        return ctx;
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);

     try {
            InitalizeLogbackHandler.getInstance().initalize(System.getProperty("catalina.base") 
                    + "/conf/dataset_queue_logback_v1.xml", true);
        } catch (LoggerInitalizationException ex) {
            LOG.error("Logging initializaton failed.", ex);
        }
        LOG.info("Entering application.");
    }
}
