package oracle.interview.implementation;

import java.io.BufferedReader;
import oracle.interview.metrics.MetricReader;
import oracle.interview.metrics.TargetMetricsContainer;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class MetricReaderImplementation implements MetricReader {
    @Override
    public List<TargetMetricsContainer> readMetrics(InputStream metricInputStream) {
        // TODO: implement this, reading data from the input stream, returning a list of containers read from the stream
        
        List<TargetMetricsContainer> resultList = new ArrayList<TargetMetricsContainer>();
        
        try{            
            //BufferedReader Instance to Read the InputStream Info
            BufferedReader reader = new BufferedReader( new InputStreamReader( metricInputStream ) );
            
            //StringBuilder Instance to Get de XML data
            StringBuilder sb = new StringBuilder();
            String line = "";
            
            //Get the Whole Data as String
            while( (line = reader.readLine()) != null ){
                sb.append(line);
            }
            
            //Create XML Document by String (StringBuilder.toString())
            Document doc = convertStringToXMLDocument(sb.toString());
            
            //NodeList from All Tags with the "Target" Name
            NodeList list = doc.getElementsByTagName("target");
            
            //Loop for Target Tags
            for (int temp = 0; temp < list.getLength(); temp++) {

                //Each Tag is Used as a Node
                Node target = list.item(temp);

                //If this Tag is an Element Node...
                if (target.getNodeType() == Node.ELEMENT_NODE) {

                    //We threat this Node as Element, to get Attributes and Inner Elements
                    Element tagElement = (Element) target;

                    // Get Element (Target's) Attributes
                    String targetName = tagElement.getAttribute("name");
                    String targetType = tagElement.getAttribute("type");                    
                                    
                    //New Instance of TargetMetricsContainer by every Target
                    TargetMetricsContainer container = new TargetMetricsContainer(targetName, targetType);
                                        
                    //Get Metric Nodes from Actual Target Node
                    NodeList metricsList = tagElement.getElementsByTagName("metric");
                    
                    //Loop for Metric Nodes
                    for(int met = 0; met < metricsList.getLength(); met++){
                        
                        //Each Metric is Used as a Node
                        Node metric = metricsList.item(met);
                        
                        //If this Metric is an Element Node...
                        if( metric.getNodeType() == Node.ELEMENT_NODE){
                            Element metricElement = (Element) metric;
                            
                            //Get Metric's Attributes
                            String metricType = metricElement.getAttribute("type");
                            String metricDate = metricElement.getAttribute("timestamp");
                            int metricVal = Integer.parseInt(metricElement.getAttribute("value"));
                            
                            //Cast from String Date to Instant
                            Instant metricTms = null;
                            try{
                                //Set Formatter with Date Format givven in XML
                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                                
                                //Parsing String Date to Date
                                Date parsedDate = formatter.parse(metricDate);
                                
                                //Parsing Date to Instant
                                metricTms = parsedDate.toInstant();
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                            
                            //Add Metric to Container
                            container.addMetric(metricType, metricTms, metricVal);                            
                        }                        
                    }
                    
                    //Add Container to the ResultList
                    resultList.add(container);
                }
            }                        
        }catch(Exception e){
            e.printStackTrace();
        }     
        
        return resultList;
    }
    
    private static Document convertStringToXMLDocument(String xmlString){
      //Parser that produces DOM object trees from XML content
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

      //API to obtain DOM Document instance
      DocumentBuilder builder = null;
      try
      {
        //Create DocumentBuilder with default configuration
        builder = factory.newDocumentBuilder();

        //Parse the content to Document object
        Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
        return doc;
      } 
      catch (Exception e) 
      {
        e.printStackTrace();
      }
      return null;
    }

}
