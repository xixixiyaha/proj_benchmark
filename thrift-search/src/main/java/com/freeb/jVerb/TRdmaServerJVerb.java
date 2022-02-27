package com.freeb.jVerb;


import com.freeb.thrift.SearchServer.SearchServiceServerImpl;
import com.freeb.thrift.SearchService;
import com.ibm.disni.RdmaActiveEndpointGroup;
import com.ibm.disni.RdmaServerEndpoint;
import org.apache.commons.cli.*;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class TRdmaServerJVerb {

    private RdmaActiveEndpointGroup<TRdmaServerEndpoint> group_;
    private static final Logger logger = LoggerFactory.getLogger(TRdmaServerJVerb.class);

    private boolean testMode = true;
    private String host;
    private int port = 1919;
    private int poolsize = 3;
    private int recvQueue = 32;
    private int sendQueue = 32;
    private int wqSize = recvQueue;
    private int servicetimeout = 0;
    private boolean polling = false;
    private int maxinline = 0;

    public void run() throws Exception{
        long[] clusterAffinities = new long[poolsize];
        for (int i = 0; i < poolsize; i++){
            long cpu = 1L << i;
            clusterAffinities[i] = cpu;
        }
        logger.info("running...server " + host + ", poolsize " + poolsize + ", maxinline " + maxinline + ", polling " + polling + ", recvQueue " + recvQueue + ", sendQueue " + sendQueue + ", wqSize " + wqSize + ", rpcservice-timeout " + servicetimeout);
        TRdmaService rpcService = new TRdmaService(new SearchService.Processor<SearchService.Iface>(new SearchServiceServerImpl()));
        rpcService.setProtocolFactory(new TBinaryProtocol.Factory());
        logger.info("rpcService Initialize Success");
        group_ = new RdmaActiveEndpointGroup<TRdmaServerEndpoint>(1000, false, 128, 4, 128);
        logger.info("group Initialize Success");
        RdmaServerEndpoint<DaRPCServerEndpoint<RdmaRpcRequest, RdmaRpcResponse>> serverEp = group.createServerEndpoint();
        logger.info("server Ep Initialize Success");
        InetSocketAddress address = new InetSocketAddress(host, port);
        serverEp.bind(address, 100);
        logger.info("server bind Initialize Success");

        while(true){
            serverEp.accept();
        }
    }

    public void launch(String[] args) throws Exception {
        Option addressOption = Option.builder("a").required().desc("server address").hasArg().build();

        Option portOption = Option.builder("o").required().desc("server port").hasArg().build();

        Option poolsizeOption = Option.builder("p").desc("pool size").hasArg().build();
        Option servicetimeoutOption = Option.builder("t").desc("service timeout").hasArg().build();
        Option pollingOption = Option.builder("d").desc("if polling, default false").build();
        Option maxinlineOption = Option.builder("i").desc("max inline data").hasArg().build();
        Option wqSizeOption = Option.builder("w").desc("wq size").hasArg().build();
        Option recvQueueOption = Option.builder("r").desc("receive queue").hasArg().build();
        Option sendQueueOption = Option.builder("s").desc("send queue").hasArg().build();
        Option serializedSizeOption = Option.builder("l").desc("serialized size").hasArg().build();
        Options options = new Options();
        options.addOption(addressOption);
        options.addOption(portOption);
        options.addOption(poolsizeOption);
        options.addOption(servicetimeoutOption);
        options.addOption(pollingOption);
        options.addOption(maxinlineOption);
        options.addOption(wqSizeOption);
        options.addOption(recvQueueOption);
        options.addOption(sendQueueOption);
        options.addOption(serializedSizeOption);
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine line = parser.parse(options, args);
            host = line.getOptionValue(addressOption.getOpt());
            port = Integer.parseInt(line.getOptionValue(portOption.getOpt()));
            if (line.hasOption(poolsizeOption.getOpt())) {
                poolsize = Integer.parseInt(line.getOptionValue(poolsizeOption.getOpt()));
            }
            if (line.hasOption(servicetimeoutOption.getOpt())) {
                servicetimeout = Integer.parseInt(line.getOptionValue(servicetimeoutOption.getOpt()));
            }
            if (line.hasOption(pollingOption.getOpt())) {
                polling = true;
            }
            if (line.hasOption(maxinlineOption.getOpt())) {
                maxinline = Integer.parseInt(line.getOptionValue(maxinlineOption.getOpt()));
            }
            if (line.hasOption(wqSizeOption.getOpt())) {
                wqSize = Integer.parseInt(line.getOptionValue(wqSizeOption.getOpt()));
            }
            if (line.hasOption(recvQueueOption.getOpt())) {
                recvQueue = Integer.parseInt(line.getOptionValue(recvQueueOption.getOpt()));
            }
            if (line.hasOption(sendQueueOption.getOpt())) {
                sendQueue = Integer.parseInt(line.getOptionValue(sendQueueOption.getOpt()));
            }
            if (line.hasOption(serializedSizeOption.getOpt())) {
                RdmaRpcRequest.SERIALIZED_SIZE = Integer.parseInt(line.getOptionValue(serializedSizeOption.getOpt()));
                RdmaRpcResponse.SERIALIZED_SIZE = RdmaRpcRequest.SERIALIZED_SIZE;
            }
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("DaRPCServer", options);
            System.exit(-1);
        }

        this.run();
    }

    @Override
    public String toString() {
        return "TRdmaServerRaw{" +
                "testMode=" + testMode +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", poolsize=" + poolsize +
                ", recvQueue=" + recvQueue +
                ", sendQueue=" + sendQueue +
                ", wqSize=" + wqSize +
                ", servicetimeout=" + servicetimeout +
                ", polling=" + polling +
                ", maxinline=" + maxinline +
                '}';
    }
}
