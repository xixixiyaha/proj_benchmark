package benchmark.rpc;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import benchmark.bean.Page;
import benchmark.bean.User;
import benchmark.rpc.thrift.UserServiceThriftClientImpl;
import benchmark.service.UserService;

@State(Scope.Benchmark)
public class Client extends AbstractClient {
	public static final int CONCURRENCY = 32;

	private final UserServiceThriftClientImpl userService = new UserServiceThriftClientImpl();

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
	public boolean existUser() throws Exception {
		return super.existUser();
	}

	@Benchmark
	@BenchmarkMode({ Mode.Throughput, Mode.AverageTime, Mode.SampleTime })
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	@Override
	public boolean createUser() throws Exception {
		return super.createUser();
	}

	@Benchmark
	@BenchmarkMode({ Mode.Throughput, Mode.AverageTime, Mode.SampleTime })
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	@Override
	public User getUser() throws Exception {
		return super.getUser();
	}

	@Benchmark
	@BenchmarkMode({ Mode.Throughput, Mode.AverageTime, Mode.SampleTime })
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	@Override
	public Page<User> listUser() throws Exception {
		return super.listUser();
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

		Options opt = new OptionsBuilder()//
				.include(Client.class.getSimpleName())//
				.warmupIterations(3)//
				.warmupTime(TimeValue.seconds(10))//
				.measurementIterations(3)//
				.measurementTime(TimeValue.seconds(10))//
				.threads(CONCURRENCY)//
				.forks(1)//
				.build();

		new Runner(opt).run();
	}

}
