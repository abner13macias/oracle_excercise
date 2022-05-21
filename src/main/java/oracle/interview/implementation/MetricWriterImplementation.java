package oracle.interview.implementation;

import oracle.interview.metrics.MetricStorage;
import oracle.interview.metrics.MetricWriter;
import oracle.interview.metrics.TargetMetricsContainer;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MetricWriterImplementation implements MetricWriter {

    private final MetricStorage storage;

    public MetricWriterImplementation(MetricStorage storage) {
        this.storage = storage;
    }

    @Override
    public void writeMetricsContainer(TargetMetricsContainer metricsContainer) {
        // TODO: write this metricsContainer to the MetricStorage. Hint
        //      storage.write(metricsContainer);
        //  partially works.  Since the write could fail, retry the write on failure
        //  as appropriate.
        
        //Infinite Loop, it breaks until the Write is Successful
        boolean band = true;
        while(band){
            //Tries Container Writing
            try{
                storage.write(metricsContainer);
                //Writing Succesfull; end looping
                band = false;
            }catch(SQLException sqlEX){
                //SQLException; Send Message and Try again
                System.out.println("Error Writing Info at " + metricsContainer.getTargetName() + "... Trying Again");
            }
        }
        

    }
}
