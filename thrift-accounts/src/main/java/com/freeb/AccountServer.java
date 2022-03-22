package com.freeb;

import com.freeb.DaRPC.RawVersion.RdmaRpcRequest;
import com.freeb.DaRPC.RawVersion.RdmaRpcResponse;
import com.freeb.Utils.IPUtil;
import com.ibm.disni.util.StopWatch;
import org.apache.commons.cli.*;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransportException;
import com.freeb.thrift.AccountService;
import com.freeb.thrift.AccountServer.AccountsServiceServerImpl;
import com.freeb.thrift.AccountClients.AccountForeignClients;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class AccountServer {
    public static final int CONCURRENCY = 16;
    public static int callNum = 0;
    public static int RDMA_MODE = 0;
    public static int TEST_LOOP = 3000;

    private final AccountForeignClients accClients;

    // JMH Test in here
    public AccountServer(){
        accClients= new AccountForeignClients("10.137.144.83",8080);
    }


    public AccountServer(String[] args) throws Exception {


        int port=0;
        String host="";
        int maxInline = 1;
        int timeout=3000,recvQueueDepth=0, sendQueueDepth=0;
        int poolSize=2,endpointSize=1;

        Option rdmaOption = Option.builder("m").required().desc("commute mode").hasArg().build();
        Option addressOption = Option.builder("a").required().desc("server address").hasArg().build();
        Option portOption = Option.builder("o").required().desc("server port").hasArg().build();

        Option loopOption = Option.builder("l").desc("loop times in test latency").hasArg().build();

        Option timeoutOption = Option.builder("t").desc("time out").hasArg().build();
        Option maxinlineOption = Option.builder("i").desc("max inline data").hasArg().build();
        Option poolSizeOption = Option.builder("p").desc("pool size").hasArg().build();
        Option epSizeOption = Option.builder("e").desc("pool size").hasArg().build();

        Option recvQueueOption = Option.builder("r").desc("receive queue").hasArg().build();
        Option sendQueueOption = Option.builder("s").desc("send queue").hasArg().build();
        Option serializedSizeOption = Option.builder("l").desc("serialized size").hasArg().build();



        org.apache.commons.cli.Options options = new Options();
        options.addOption(rdmaOption);
        options.addOption(addressOption);
        options.addOption(portOption);
        options.addOption(loopOption);
        options.addOption(timeoutOption);
        options.addOption(maxinlineOption);
        options.addOption(poolSizeOption);
        options.addOption(recvQueueOption);
        options.addOption(sendQueueOption);
        options.addOption(serializedSizeOption);
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine line = parser.parse(options, args);
            RDMA_MODE = Integer.parseInt(line.getOptionValue(rdmaOption.getOpt()));
            host = line.getOptionValue(addressOption.getOpt());
            port = Integer.parseInt(line.getOptionValue(portOption.getOpt()));
            if (line.hasOption(loopOption.getOpt())) {
                TEST_LOOP = Integer.parseInt(line.getOptionValue(loopOption.getOpt()));
            }
            if (line.hasOption(timeoutOption.getOpt())) {
                timeout = Integer.parseInt(line.getOptionValue(timeoutOption.getOpt()));
            }
            if (line.hasOption(maxinlineOption.getOpt())) {
                maxInline = Integer.parseInt(line.getOptionValue(maxinlineOption.getOpt()));
            }
            if (line.hasOption(poolSizeOption.getOpt())) {
                poolSize = Integer.parseInt(line.getOptionValue(poolSizeOption.getOpt()));
            }
            if (line.hasOption(epSizeOption.getOpt())) {
                endpointSize = Integer.parseInt(line.getOptionValue(poolSizeOption.getOpt()));
            }
            if (line.hasOption(recvQueueOption.getOpt())) {
                recvQueueDepth = Integer.parseInt(line.getOptionValue(recvQueueOption.getOpt()));
            }
            if (line.hasOption(sendQueueOption.getOpt())) {
                sendQueueDepth = Integer.parseInt(line.getOptionValue(sendQueueOption.getOpt()));
            }
            if (line.hasOption(serializedSizeOption.getOpt())) {
                RdmaRpcRequest.SERIALIZED_SIZE = Integer.parseInt(line.getOptionValue(serializedSizeOption.getOpt()));
                RdmaRpcResponse.SERIALIZED_SIZE = RdmaRpcRequest.SERIALIZED_SIZE;
            }
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("DaRPCForeign Clients", options);
            System.exit(-1);
        }
        if(RDMA_MODE>0){
            accClients= new AccountForeignClients(host,port,timeout,maxInline,sendQueueDepth,recvQueueDepth,poolSize,endpointSize);

        }else if(RDMA_MODE==0){
            accClients= new AccountForeignClients(host,port);
        }else{
            accClients = null;
        }

        System.out.println("accClients Initialization Success");
    }

    @TearDown
    public void close() throws IOException {
        accClients.close();
        System.out.println("<AccClient.testSearchRe>"+callNum++);

    }

    @Benchmark
    @BenchmarkMode({ Mode.Throughput, Mode.AverageTime, Mode.SampleTime })
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public boolean testSearchRe() throws Exception{
        List<Long> re = accClients.IdealResEfficiencyTest(10000,10);
        return re != null;
    }

    private static void initAccountService(){
        try {

//            InetSocketAddress serverAddress = new InetSocketAddress("10.0.16.14", 8081);
            String serverHost = IPUtil.getIPAddress();
            System.out.println("init Server socket IP addr = "+serverHost);
            InetSocketAddress serverAddress = new InetSocketAddress(serverHost, 8080);
            TNonblockingServerTransport serverSocket = new TNonblockingServerSocket(serverAddress);

            TThreadedSelectorServer.Args serverParams = new TThreadedSelectorServer.Args(serverSocket);
            serverParams.protocolFactory(new TBinaryProtocol.Factory());
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            serverParams.processor(new AccountService.Processor<AccountService.Iface>(new AccountsServiceServerImpl()));
            TServer server = new TThreadedSelectorServer(serverParams);
            timestamp = new Timestamp(System.currentTimeMillis());
            System.out.println("in com.freeb.thrift AccountServer main() ==  =="+timestamp.toString());
            server.serve();
        }catch (TTransportException e){
            System.out.println("Server Exception is "+e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    private void TestDaRpcThriftLatency(){
        StopWatch w = new StopWatch();
        w.start();
        for(int i=0;i<TEST_LOOP;i++){
            accClients.IdealResEfficiencyTest(10000,i);
//            client.accClients.IdealResEfficiencyTest(100,2);
        }
        long time = w.getExecutionTime();
        System.out.println("time = "+time+"| exec round = "+TEST_LOOP);
    }

    public static void main(String[] args) throws Exception {
        AccountServer server = new AccountServer(args);
        if(AccountServer.RDMA_MODE>0){
            server.TestDaRpcThriftLatency();
            System.out.println("com.freeb.thrift.AccountServer <main> RDMA latency finish");
        }else if(AccountServer.RDMA_MODE == 0){
            org.openjdk.jmh.runner.options.Options opt = new OptionsBuilder()
                .include(AccountServer.class.getSimpleName())
                .warmupIterations(1)
                .measurementIterations(1)
                .measurementTime(TimeValue.seconds(10))
                .threads(CONCURRENCY)
                .forks(1)
                .build();
            new Runner(opt).run();
            System.out.println("com.freeb.thrift.AccountServer <main> JMH finish");
        }else{
            initAccountService();
        }

    }
}
