package com.freeb.jVerb;


import com.ibm.disni.util.NativeAffinity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TRdmaServerManager {
    private static final Logger logger = LoggerFactory.getLogger(TRdmaServerManager.class);

    private TRdmaServerManager.RpcResourceAllocator[] allocaters;

    public TRdmaServerManager(long[] affinities, int timeout) throws Exception {
        this.allocaters = new TRdmaServerManager.RpcResourceAllocator[affinities.length];
        for (int i = 0; i < affinities.length; i++){
            allocaters[i] = new TRdmaServerManager.RpcResourceAllocator(affinities[i], i, timeout);
            allocaters[i].start();
        }
    }

    public void allocateResources(TRdmaServerEndpoint endpoint) throws Exception {
        logger.info("dispatching resource, clusterid " + endpoint.getClusterId());
        allocaters[endpoint.getClusterId()].initResource(endpoint);
    }

    public void close() throws IOException, InterruptedException {
        for (RpcResourceAllocator allocater : allocaters) {
            allocater.close();
        }
    }

    public static class RpcResourceAllocator implements Runnable {
        private static Logger logger = LoggerFactory.getLogger("com.ibm.zac.darpc");
        private LinkedBlockingQueue<TRdmaEndpoint> requestQueue;
        private long affinity;
        private int index;
        private boolean running;
        private Thread thread;
        private int timeout;

        public RpcResourceAllocator(long affinity, int index, int timeout) throws Exception {
            this.affinity = affinity;
            this.index = index;
            this.requestQueue = new LinkedBlockingQueue<TRdmaEndpoint>();
            this.running = false;
            this.timeout = timeout;
            if (timeout <= 0){
                this.timeout = Integer.MAX_VALUE;
            }
            this.thread = new Thread(this);
        }

        public void initResource(TRdmaServerEndpoint endpoint) {
            requestQueue.add(endpoint);
        }

        public synchronized void start(){
            running = true;
            thread.start();
        }

        public void run() {
            NativeAffinity.setAffinity(affinity);
            logger.info("running resource management, index " + index + ", affinity " + affinity + ", timeout " + timeout);
            while(running){
                try {
                    TRdmaEndpoint endpoint = requestQueue.poll(timeout, TimeUnit.MILLISECONDS);
                    if (endpoint != null){
                        logger.info("allocating resources, cluster " + index + ", endpoint " + endpoint.getEndpointId());
                        endpoint.allocateResources();
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }

        public void close() throws IOException, InterruptedException {
            running = false;
            thread.join();
            logger.info("resource management closed");
        }
    }
}
