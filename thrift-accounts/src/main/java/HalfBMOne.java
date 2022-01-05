import com.freeb.Clients.AccountsClients;
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
import thrift.AccountsForeignClients;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class HalfBMOne{
    public static final int CONCURRENCY = 32;
    public static int callNum = 0;
    private final AccountsForeignClients accClients = new AccountsForeignClients();

    @TearDown
    public void close() throws IOException {
        accClients.close();
    }

    @Benchmark
    @BenchmarkMode({ Mode.Throughput, Mode.AverageTime, Mode.SampleTime })
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public boolean testSearchRe() throws Exception {
        System.out.println("<AccClient.testSearchRe>"+callNum++);
        List<Long> re = accClients.IdealResEfficiencyTest(10000,10);
        return re != null;
    }

    public static void main(String[] args) throws Exception {

        HalfBMOne client = new HalfBMOne();
        for (int i = 0; i < 10; i++) {
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
                .include(HalfBMOne.class.getSimpleName())//
                .warmupIterations(3)//
                .warmupTime(TimeValue.seconds(10))//
                .measurementIterations(3)//
                .measurementTime(TimeValue.seconds(10))//
                .threads(CONCURRENCY)//
                .forks(1)//
                .build();

        System.out.println("in thrift HalfBMOne main() === 4 === ");
        new Runner(opt).run();
        System.out.println("in thrift HalfBMOne main() === 5 === ");

    }

}
