package no.imr.nmdapi.dataset.queue.web.processor;

import java.util.Map;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import no.imr.messaging.exception.ProcessingException;
import no.imr.nmd.commons.dataset.jaxb.DataTypeEnum;
import no.imr.nmd.commons.dataset.jaxb.QualityEnum;
import no.imr.nmdapi.dao.file.NMDDatasetDao;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Terry Hannant <a5119>
 */
@Service(value = "datasetProcessor")
public class DatasetProcessor {

    
    @Autowired NMDDatasetDao nmdDatasetDao;
            
    private static final Logger LOG = LoggerFactory.getLogger(DatasetProcessor.class);
  
    private final String  dataTypeHeaderName="imr:datatype";  
    private final String  dataSetHeaderName="imr:datasetname";
    private final String  ownerHeaderName="imr:owner";
    private final String  readAccessHeaderName="imr:read";
    private final String  writeAcessHeaderName ="imr:write";
    private final String  qaHeaderName="imr:qualityassured";
    private final String  descriptionHeaderName="imr:description";
    private final String  updatedHeaderName="imr:updated";
    private final String  datasetContainerHeaderName="imr:datasetscontainer";
    private final String  datasetContainerDelimiter="/";
    
     public void handleMessage(Exchange exchange) throws DatatypeConfigurationException {
        Map<String, Object> headers = exchange.getIn().getHeaders();

        //Check mandatory headers
         if (headers.containsKey(dataTypeHeaderName) 
                &&  headers.containsKey(dataSetHeaderName)
                &&  headers.containsKey(ownerHeaderName)
                && headers.containsKey(readAccessHeaderName)
                && headers.containsKey(writeAcessHeaderName)
                && headers.containsKey(qaHeaderName)
                && headers.containsKey(updatedHeaderName) 
                && headers.containsKey(descriptionHeaderName)
                && headers.containsKey(datasetContainerHeaderName) )
         {
             
           DataTypeEnum  dataType = DataTypeEnum.valueOf((String)headers.get(dataTypeHeaderName));
           QualityEnum quality = QualityEnum.fromValue((String)headers.get(qaHeaderName));
           String dataSetName = (String) headers.get(dataSetHeaderName);
           String dataSetContainer = (String) headers.get(datasetContainerHeaderName) ;
           String[] dataSetContainerPath =dataSetContainer.split(datasetContainerDelimiter);
           
           XMLGregorianCalendar updatedCal = DatatypeFactory.newInstance().newXMLGregorianCalendar((String)headers.get(updatedHeaderName));
           
           String writeRole = (String) headers.get(writeAcessHeaderName);
           String readRole = (String) headers.get(readAccessHeaderName);
           String owner = (String) headers.get(ownerHeaderName);
           String description = (String) headers.get(descriptionHeaderName);
           
           if (nmdDatasetDao.hasDataset(dataType, dataSetName,dataSetContainerPath)) {
               nmdDatasetDao.updateDataset(dataType, dataSetName, updatedCal, dataSetContainerPath);
                                     
              exchange.getIn().setBody("Update dataset:"+dataType.toString()+" "+dataSetContainer);
            

           } else {
              nmdDatasetDao.createDataset(writeRole, readRole, description, owner,quality, dataType,
                   dataSetName,updatedCal, dataSetContainerPath);
               exchange.getIn().setBody("Create dataset:"+dataType.toString()+" "+dataSetContainer);
    
           }               
        } else {
           LOG.debug("Missing Header(s)");
            throw new ProcessingException("Missing header(s)");
        }

	 
    }

    
}
