package com.freeb.jVerb;

import com.ibm.disni.*;
import com.ibm.disni.verbs.IbvCQ;
import com.ibm.disni.verbs.IbvContext;
import com.ibm.disni.verbs.IbvQP;
import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TProtocol;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class TRdmaServerGroup<E extends RdmaActiveEndpoint> extends TRdmaGroup<E> {

    TProcessor processor_;

    private ConcurrentHashMap<Integer,> deviceInstance;
    private  resourceManager;
    private long[] computeAffinities;
    private long[] resourceAffinities;
    private int currentCluster;
    private int nbrOfClusters;

    private boolean polling;
    private int pollSize;
    private int clusterSize;

    public TRdmaServerGroup(TProcessor processor,int timeout, int maxinline, int recvQueue, int sendQueue,int bufferSize,long[] clusterAffinities, boolean polling, int pollSize, int clusterSize) throws IOException {
        super(timeout,maxinline,recvQueue,sendQueue,bufferSize);
        this.processor_ = processor;

        deviceInstance = new ConcurrentHashMap<Integer, >();
        this.computeAffinities = clusterAffinities;
        this.resourceAffinities = clusterAffinities;
        this.nbrOfClusters = computeAffinities.length;
        this.currentCluster = 0;

        this.polling = polling;
        this.pollSize = pollSize;
        this.clusterSize = clusterSize;
    }

    @Override
    public RdmaCqProvider createCqProvider(E ep) throws IOException {
        logger.info("setting up cq processor (multicore)");
        IbvContext context = ep.getIdPriv().getVerbs();
        if (context == null) {
            throw new IOException("setting up cq processor, no context found");
        }

        RdmaCqProvider cqProcessor = null;
        return cqProcessor;
    }

    @Override
    public IbvQP createQpProvider(E endpoint) throws IOException {
        logger.info("setting up QP");
        RdmaCqProcessor<E> cqProcessor = this.lookupCqProcessor(endpoint);
        IbvCQ cq = cqProcessor.getCQ();
        IbvQP qp = this.createQP(endpoint.getIdPriv(), endpoint.getPd(), cq);
        cqProcessor.registerQP(qp.getQp_num(), endpoint);
        return qp;
    }

    @Override
    public void allocateResources(E endpoint) throws Exception {
        resourceManager.allocateResources(endpoint);
    }

    protected synchronized RdmaCqProcessor<E> lookupCqProcessor(E endpoint) throws IOException{
        IbvContext context = endpoint.getIdPriv().getVerbs();
        if (context == null) {
            throw new IOException("setting up cq processor, no context found");
        }

        int key = context.getCmd_fd();
        if (!deviceInstance.containsKey(key)) {
            return null;
        } else {
            return null;
        }
    }

    public void processServerEvent(TProtocol protocol) throws IOException {
        //TODO@track stealing
        try {
            processor_.process(protocol,protocol);
        } catch (TException e) {
            e.printStackTrace();
        }
    }
}
