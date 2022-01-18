package com.freeb;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import thrift.Accounts.AccountsService;
import thrift.Accounts.AccountsServiceServerImpl;
import thrift.search.AccountForeignClients;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class AccountsServer {
    public static final int CONCURRENCY = 32;
    public static int callNum = 0;
    private final AccountForeignClients accClients = new AccountForeignClients();

    @TearDown
    public void close() throws IOException {
        accClients.close();
        System.out.println("<AccClient.testSearchRe>"+callNum++);

    }

    @Benchmark
    @BenchmarkMode({ Mode.Throughput, Mode.AverageTime, Mode.SampleTime })
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public boolean testSearchRe() throws Exception {
        List<Long> re = accClients.IdealResEfficiencyTest(1000000,10);
        return re != null;
    }

    private static void initAccountService(){
        try {
            //TODO auto-acquire private addr

            InetSocketAddress serverAddress = new InetSocketAddress("10.0.16.14", 8081);

            TNonblockingServerTransport serverSocket = new TNonblockingServerSocket(serverAddress);

            TThreadedSelectorServer.Args serverParams = new TThreadedSelectorServer.Args(serverSocket);
            serverParams.protocolFactory(new TBinaryProtocol.Factory());
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            serverParams.processor(new AccountsService.Processor<AccountsService.Iface>(new AccountsServiceServerImpl()));
            TServer server = new TThreadedSelectorServer(serverParams);
            timestamp = new Timestamp(System.currentTimeMillis());
            System.out.println("in thrift Server main() ==  =="+timestamp.toString());
            server.serve();
        }catch (TTransportException e){
            System.out.println("Server Exception is "+e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    public static void HalfTest() throws InterruptedException, RunnerException, IOException {
        AccountsServer client = new AccountsServer();
        for (int i = 0; i < 1; i++) {
            try {
                System.out.println(client.testSearchRe());
                break;
            } catch (Exception e) {
                Thread.sleep(1000);
            }
        }

        client.close();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Options opt = new OptionsBuilder()//
                .include(AccountsServer.class.getSimpleName())//
                .warmupIterations(3)//
                .warmupTime(TimeValue.seconds(10))//
                .measurementIterations(3)//
                .measurementTime(TimeValue.seconds(10))//
                .threads(CONCURRENCY)//
                .forks(1)//
                .build();

        System.out.println("in thrift com.freeb.AccountsServer main() === 4 === ");
        new Runner(opt).run();
        System.out.println("in thrift com.freeb.AccountsServer main() === 5 === ");
    }

    public static void main(String[] args) throws Exception {

//        HalfTest();

        initAccountService();

    }

}
