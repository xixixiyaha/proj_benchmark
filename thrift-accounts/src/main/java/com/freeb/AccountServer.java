package com.freeb;

import com.freeb.DaRPC.RawVersion.RdmaRpcRequest;
import com.freeb.DaRPC.RawVersion.RdmaRpcResponse;
import com.freeb.Utils.IPUtil;
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

import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

//@State(Scope.Benchmark)
public class AccountServer {
    public static final int CONCURRENCY = 32;
    public static int callNum = 0;
    private final AccountForeignClients accClients;

    public AccountServer(String[] args) throws Exception {

        int port=0;
        String host="";
        int maxInline = 1;
        int timeout=3000,recvQueueDepth=0, sendQueueDepth=0;

        Option addressOption = Option.builder("a").required().desc("server address").hasArg().build();

        Option portOption = Option.builder("o").required().desc("server port").hasArg().build();
        Option timeoutOption = Option.builder("t").desc("time out").hasArg().build();
        Option maxinlineOption = Option.builder("i").desc("max inline data").hasArg().build();
        Option recvQueueOption = Option.builder("r").desc("receive queue").hasArg().build();
        Option sendQueueOption = Option.builder("s").desc("send queue").hasArg().build();
        Option serializedSizeOption = Option.builder("l").desc("serialized size").hasArg().build();
        org.apache.commons.cli.Options options = new Options();
        options.addOption(addressOption);
        options.addOption(portOption);
        options.addOption(timeoutOption);
        options.addOption(maxinlineOption);
        options.addOption(recvQueueOption);
        options.addOption(sendQueueOption);
        options.addOption(serializedSizeOption);
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine line = parser.parse(options, args);
            host = line.getOptionValue(addressOption.getOpt());
            port = Integer.parseInt(line.getOptionValue(portOption.getOpt()));
            if (line.hasOption(timeoutOption.getOpt())) {
                timeout = Integer.parseInt(line.getOptionValue(timeoutOption.getOpt()));
            }
            if (line.hasOption(maxinlineOption.getOpt())) {
                maxInline = Integer.parseInt(line.getOptionValue(maxinlineOption.getOpt()));
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

         accClients= new AccountForeignClients(host,port,timeout,maxInline,sendQueueDepth,recvQueueDepth);

        System.out.println("accClients Initialization Success");

        List<Long> re = accClients.IdealResEfficiencyTest(100,1);
        System.out.println(re);
    }

//    @TearDown
//    public void close() throws IOException {
//        accClients.close();
//        System.out.println("<AccClient.testSearchRe>"+callNum++);
//
//    }
//
//    @Benchmark
//    @BenchmarkMode({ Mode.Throughput, Mode.AverageTime, Mode.SampleTime })
//    @OutputTimeUnit(TimeUnit.MILLISECONDS)
//    public boolean testSearchRe() throws Exception {
//        List<Long> re = accClients.IdealResEfficiencyTest(1000000,10);
//        return re != null;
//    }

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

//    public static void HalfTest(String[] args) throws Exception {
//        AccountServer client = new AccountServer(args);
//        for (int i = 0; i < 1; i++) {
//            try {
//                System.out.println(client.testSearchRe());
//                break;
//            } catch (Exception e) {
//                Thread.sleep(1000);
//            }
//        }

//        client.close();

//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//
//        Options opt = new OptionsBuilder()//
//                .include(AccountServer.class.getSimpleName())//
//                .warmupIterations(3)//
//                .warmupTime(TimeValue.seconds(10))//
//                .measurementIterations(3)//
//                .measurementTime(TimeValue.seconds(10))//
//                .threads(CONCURRENCY)//
//                .forks(1)//
//                .build();
//
//        System.out.println("in com.freeb.thrift com.freeb.thrift.AccountServer main() === 4 === ");
//        new Runner(opt).run();
//        System.out.println("in com.freeb.thrift com.freeb.thrift.AccountServer main() === 5 === ");
//    }

    public static void main(String[] args) throws Exception {
        AccountServer client = new AccountServer(args);
        System.out.println(client.toString());
//        HalfTest(args);

//        initAccountService();

    }

}
