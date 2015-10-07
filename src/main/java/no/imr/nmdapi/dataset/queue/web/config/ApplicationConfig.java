package no.imr.nmdapi.dataset.queue.web.config;


import no.imr.messaging.processor.ExceptionProcessor;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.configuration.reloading.ReloadingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 *
 * @author Terry Hannant <a5119>
 */
@Configuration
public class ApplicationConfig {
    
    @Bean(name = "configuration")
    public PropertiesConfiguration configuration() throws ConfigurationException {
        org.apache.commons.configuration.PropertiesConfiguration configuration = new org.apache.commons.configuration.PropertiesConfiguration(System.getProperty("catalina.base") + "/conf/dataset_queue_v1.properties");
        ReloadingStrategy reloadingStrategy = new FileChangedReloadingStrategy();
        configuration.setReloadingStrategy(reloadingStrategy);
        return configuration;
    }

    @Bean(name= "errorProcessor")
    public ExceptionProcessor exceptionProcessor(){
        return new ExceptionProcessor("datasetQueue");
    }
            
    
}
    
    
    


