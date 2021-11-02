package benchmark.rpc.thrift;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.io.Closeable;
import java.io.IOException;

public class ThriftUserServiceClient implements Closeable {

	// not thread safe
	public final TTransport transport;
	public final TProtocol protocol;
	public final UserService.Client client;

	public ThriftUserServiceClient(String host, int port) {
		System.out.println("in ThriftUserServiceClient == 1 ==");
		transport = new TFramedTransport(new TSocket(host, port));
		protocol = new TBinaryProtocol(transport);
		System.out.println("in ThriftUserServiceClient == 2 ==");
		client = new UserService.Client(protocol);
		System.out.println("in ThriftUserServiceClient == 3 ==");
		try {
			transport.open();
		} catch (TTransportException e) {
			throw new Error(e);
		}
		System.out.println("in ThriftUserServiceClient == 4 ==");
	}

	@Override
	public void close() throws IOException {
		transport.close();
	}

}
