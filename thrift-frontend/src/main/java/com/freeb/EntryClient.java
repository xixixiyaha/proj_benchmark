package com.freeb;

import com.freeb.thrift.FrontendClients;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class EntryClient {
    public static final int CONCURRENCY = 32;
    public static int callNum = 0;
    private final FrontendClients client = new FrontendClients();


    @TearDown
    public void close() throws IOException {
        client.close();
    }

    @Benchmark
    @BenchmarkMode({ Mode.Throughput, Mode.AverageTime, Mode.SampleTime })
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public String LaunchBM1() throws Exception {
        System.out.println("<EntryClient.LaunchBM1>"+callNum++);
        return client.CompareResEfficiencyBM1("remotePath",0);
    }


    public static void main(String[] args) throws Exception {

        EntryClient client = new EntryClient();
        for (int i = 0; i < 60; i++) {
            try {
                System.out.println(client.LaunchBM1());
                break;
            } catch (Exception e) {
                Thread.sleep(1000);
            }
        }

        client.close();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Options opt = new OptionsBuilder()//
                .include(EntryClient.class.getSimpleName())//
                .warmupIterations(3)//
                .warmupTime(TimeValue.seconds(10))//
                .measurementIterations(3)//
                .measurementTime(TimeValue.seconds(10))//
                .threads(CONCURRENCY)//
                .forks(1)//
                .build();

        System.out.println("in com.freeb.thrift com.freeb.BM! main() === 4 === ");
        new Runner(opt).run();
        System.out.println("in com.freeb.thrift com.freeb.BM! main() === 5 === ");


    }
}