package benchmark.rpc;


import Bean.User;
import Clients.AbstractUserClient;
import benchmark.rpc.thrift.UserServiceClientImpl;
import com.freeb.Service.UserService;
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
public class Client extends AbstractUserClient {
	public static final int CONCURRENCY = 32;
	public static int callNum = 0;
	private final UserServiceClientImpl userService = new UserServiceClientImpl();

	@Override
	protected UserService getUserService() {
		return userService;
	}

	@TearDown
	public void close() throws IOException {
		userService.close();
	}

	@Benchmark
	@BenchmarkMode({ Mode.Throughput, Mode.AverageTime, Mode.SampleTime })
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	@Override
	public boolean createUser() throws Exception {
		System.out.println("<Client.createUser>"+callNum++);
		return super.createUser();
	}

	@Benchmark
	@BenchmarkMode({ Mode.Throughput, Mode.AverageTime, Mode.SampleTime })
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	@Override
	public User getUser() throws Exception {
		System.out.println("<Client.getUser>"+callNum++);
		return super.getUser();
	}


	public static void main(String[] args) throws Exception {

		Client client = new Client();
		for (int i = 0; i < 60; i++) {
			try {
				System.out.println(client.getUser());
				break;
			} catch (Exception e) {
				Thread.sleep(1000);
			}
		}
		
		client.close();

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		Options opt = new OptionsBuilder()//
				.include(Client.class.getSimpleName())//
				.warmupIterations(3)//
				.warmupTime(TimeValue.seconds(10))//
				.measurementIterations(3)//
				.measurementTime(TimeValue.seconds(10))//
				.threads(CONCURRENCY)//
				.forks(1)//
				.build();

		System.out.println("in com.freeb.thrift Client main() === 4 === ");
		new Runner(opt).run();
		System.out.println("in com.freeb.thrift Client main() === 5 === ");

	}

}
