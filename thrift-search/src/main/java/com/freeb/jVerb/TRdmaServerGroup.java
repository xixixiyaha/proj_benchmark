package com.freeb.jVerb;


import com.ibm.disni.*;
import com.ibm.disni.verbs.IbvCQ;
import com.ibm.disni.verbs.IbvContext;
import com.ibm.disni.verbs.IbvQP;
import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class TRdmaServerGroup extends TRdmaGroup<TRdmaServerEndpoint> {
    private static final Logger logger = LoggerFactory.getLogger(TRdmaServerGroup.class);

    TProcessor processor_;
    TRdmaServerManager resourceManager;
    private ConcurrentHashMap<Integer, TRdmaNIC> deviceInstance;

    private long[] computeAffinities;
    private long[] resourceAffinities;
    private int currentCluster;
    private int nbrOfClusters;

    private boolean polling;
    private int pollSize;
    private int clusterSize;
    TProtocolFactory protocolFactory_;




    public TRdmaServerGroup(TProcessor processor,TProtocolFactory factory,int timeout, int maxinline, int recvQueue, int sendQueue,int bufferSize,long[] clusterAffinities, boolean polling, int pollSize, int clusterSize) throws Exception {
        super(timeout,maxinline,recvQueue,sendQueue,bufferSize);
        this.processor_ = processor;
        this.protocolFactory_ = factory;

//        deviceInstance = new ConcurrentHashMap<Integer, >();
        this.computeAffinities = clusterAffinities;
        this.resourceAffinities = clusterAffinities;
        this.nbrOfClusters = computeAffinities.length;
        this.currentCluster = 0;

        this.polling = polling;
        this.pollSize = pollSize;
        this.clusterSize = clusterSize;

        this.resourceManager = new TRdmaServerManager(resourceAffinities,timeout);

    }

    public RdmaCqProvider createCqProvider(TRdmaServerEndpoint ep) throws IOException {
        logger.info("setting up cq processor (multicore)");
        IbvContext context = ep.getIdPriv().getVerbs();
        if (context == null) {
            throw new IOException("setting up cq processor, no context found");
        }
        TRdmaNIC instance = null;
        int key = context.getCmd_fd();
        if(!deviceInstance.containsKey(key)){
            int cqSize = (this.recvQueueSize()+this.sendQueueSize())*clusterSize;
            instance = new TRdmaNIC(context,cqSize, this.pollSize, computeAffinities, this.getTimeout(), polling);
            deviceInstance.put(key,instance);
        }else {
            instance = deviceInstance.get(key);
        }
        TRdmaServerCluster cluster = instance.getCqProcessor(ep.getClusterId());

        return cluster;
    }


    public IbvQP createQpProvider(TRdmaServerEndpoint endpoint) throws IOException {
        logger.info("setting up QP");
        TRdmaServerCluster cqProcessor = this.lookupCqProcessor(endpoint);
        IbvCQ cq = cqProcessor.getCQ();
        IbvQP qp = this.createQP(endpoint.getIdPriv(), endpoint.getPd(), cq);
        cqProcessor.registerQP(qp.getQp_num(), endpoint);
        return qp;
    }

    public void allocateResources(TRdmaServerEndpoint endpoint) throws Exception {
        resourceManager.allocateResources(endpoint);
    }

    protected synchronized TRdmaServerCluster lookupCqProcessor(TRdmaServerEndpoint endpoint) throws IOException{
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

    synchronized int newClusterId() {
        int newClusterId = currentCluster;
        currentCluster = (currentCluster + 1) % nbrOfClusters;
        return newClusterId;
    }

    public void close() throws IOException, InterruptedException {
        super.close();
        for(TRdmaNIC instance:deviceInstance.values()){
            instance.close();
        }
        resourceManager.close();
        logger.info("Server group close");
    }

    public void processServerEvent(TProtocol protocol) {
        //TODO@track stealing
        try {
            processor_.process(protocol,protocol);
        } catch (TException e) {
            e.printStackTrace();
        }
    }
    public void setProtocolFactory(TProtocolFactory factory){
        this.protocolFactory_ = factory;
    }

}
