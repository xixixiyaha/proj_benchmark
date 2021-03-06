package com.freeb;


import com.freeb.DaRPC.*;
import com.freeb.Utils.IPUtil;
import com.freeb.thrift.SearchService;
import com.freeb.thrift.SearchServer.SearchServiceServerImpl;

import org.apache.commons.cli.*;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.util.Arrays;

public class SearchServer {
	private static final Logger logger = LoggerFactory.getLogger(SearchServer.class);

	private static Boolean rdma= true;
	public static void main(String[] args) {
		try {
			if(rdma){
				TRdmaServerRaw server = new TRdmaServerRaw();
				server.launch(args);
				return;
			}
			String serverHost="";
			int port=0;
			Option addressOption = Option.builder("a").required().desc("com.TRdma.server address").hasArg().build();
			Option portOption = Option.builder("o").required().desc("com.TRdma.server port").hasArg().build();

			Options options = new Options();
			options.addOption(addressOption);
			options.addOption(portOption);
			CommandLineParser parser = new DefaultParser();

			try {
				CommandLine line = parser.parse(options, args);
				serverHost = line.getOptionValue(addressOption.getOpt());
				port = Integer.parseInt(line.getOptionValue(portOption.getOpt()));
			} catch (ParseException e) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("com.TRdma.client.Client parse Args failed", options);
				System.exit(-1);
			}
			System.out.println("init Server socket IP addr = "+serverHost);
			InetSocketAddress serverAddress = new InetSocketAddress(serverHost, port);

			TNonblockingServerTransport serverSocket = new TNonblockingServerSocket(serverAddress);

			TThreadedSelectorServer.Args serverParams = new TThreadedSelectorServer.Args(serverSocket);
			serverParams.protocolFactory(new TBinaryProtocol.Factory());
			Timestamp timestamp;
			serverParams.processor(new SearchService.Processor<SearchService.Iface>(new SearchServiceServerImpl()));
			TServer server = new TThreadedSelectorServer(serverParams);
			timestamp = new Timestamp(System.currentTimeMillis());
			System.out.println("in com.freeb.thrift SearchServer main() ==  =="+timestamp.toString());
			server.serve();
		}catch (TTransportException e){
			System.out.println("SearchServer Exception is "+e.getMessage());
			System.out.println(Arrays.toString(e.getStackTrace()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
