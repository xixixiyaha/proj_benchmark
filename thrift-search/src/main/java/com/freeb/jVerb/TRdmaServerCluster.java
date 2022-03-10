package com.freeb.jVerb;

import com.ibm.disni.RdmaCqProcessor;
import com.ibm.disni.verbs.IbvContext;
import com.ibm.disni.verbs.IbvWC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TRdmaServerCluster extends RdmaCqProcessor<TRdmaEndpoint> {
    private static final Logger logger = LoggerFactory.getLogger(TRdmaServerCluster.class);

    public TRdmaServerCluster(IbvContext context, int cqSize, int wrSize, long affinity, int clusterId, int timeout, boolean polling) throws IOException {
        super(context, cqSize, wrSize, affinity, clusterId, timeout, polling);
        logger.info("initialize new Cluster threadName = "+Thread.currentThread().getName());

    }

    @Override
    public void dispatchCqEvent(TRdmaEndpoint ep, IbvWC wc) throws IOException {
        ep.dispatchCqEvent(wc);
    }
}
