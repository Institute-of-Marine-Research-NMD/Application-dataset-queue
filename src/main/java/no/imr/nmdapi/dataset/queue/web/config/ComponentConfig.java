package no.imr.nmdapi.dataset.queue.web.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.configuration.reloading.ReloadingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Terry Hannant <a5119>
 * 
 * Class to load shared configuration files.
 */
@Configuration
public class ComponentConfig {
    
    @Autowired
    @Qualifier("configuration")
    PropertiesConfiguration configuration;
    
    @Bean(name = "activeMQConf")
    public PropertiesConfiguration activeMQConfiguration() throws ConfigurationException {
        org.apache.commons.configuration.PropertiesConfiguration activeMQConfiguration = new org.apache.commons.configuration.PropertiesConfiguration(configuration.getString("activeMQ.properties"));
        ReloadingStrategy reloadingStrategy = new FileChangedReloadingStrategy();
        activeMQConfiguration.setReloadingStrategy(reloadingStrategy);
        return activeMQConfiguration;
    }

    
}
