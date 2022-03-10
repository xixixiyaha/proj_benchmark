package com.freeb.jVerb;

import com.ibm.disni.verbs.IbvContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TRdmaNIC  {
    private static final Logger logger = LoggerFactory.getLogger(TRdmaNIC.class);

    private TRdmaServerCluster[] cqProcessorArray;

    public TRdmaNIC(IbvContext context, int cqSize, int wrSize, long[] affinities, int timeout, boolean polling) throws IOException {
        logger.info("affinity size = "+affinities.length);
        cqProcessorArray = new TRdmaServerCluster[affinities.length];
        for(int i=0;i<affinities.length;i++){
            cqProcessorArray[i] = new TRdmaServerCluster(context,cqSize,wrSize,affinities[i],i,timeout,polling);
            cqProcessorArray[i].start();
        }
    }

    public TRdmaServerCluster getCqProcessor(int cId){
        return cqProcessorArray[cId];
    }

    public void close() throws IOException, InterruptedException {
        for(TRdmaServerCluster c:cqProcessorArray){
            c.close();
        }
    }

}
