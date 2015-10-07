package no.imr.nmdapi.dataset.queue.web.config;


import no.imr.messaging.exception.CriticalException;
import no.imr.messaging.exception.ProcessingException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.javaconfig.SingleRouteCamelConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Terry Hannant <a5119>
 */
@Configuration
public class CamelConfig  extends SingleRouteCamelConfiguration implements InitializingBean {
 
    @Autowired
    @Qualifier("configuration")
    PropertiesConfiguration configuration;
 
    @Override
    public RouteBuilder route() {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                onException(CriticalException.class).handled(true).
                        to("errorProcessor").
                        to("jms:queue:".concat(configuration.getString("queue.outgoing.criticalFailure")));
                onException(ProcessingException.class).handled(true).
                        to("errorProcessor").
                        to("jms:queue:".concat(configuration.getString("queue.outgoing.processingFailure")));
                
                from("jms:queue:".concat(configuration.getString("queue.incoming")))
                        .to("log:begin?level=INFO&showHeaders=true&showBody=false")
                        .to("datasetProcessor")
                        .to("jms:queue:".concat(configuration.getString("queue.outgoing.success")));
            }
        };
    }
       
    @Override
    public void afterPropertiesSet() throws Exception {
        // Do nothing. 
    }

    
}
