package com.freeb.thrift.SearchServer;


import com.freeb.thrift.SearchClients.SearchForeignClients;
import com.freeb.thrift.SearchServer.SearchService;
import org.apache.thrift.TException;

import java.util.List;

public class SearchServiceServerImpl implements SearchService.Iface {

    private final com.freeb.Service.SearchService searchService = new com.freeb.Service.SearchServiceImpl(new SearchForeignClients());
    @Override
    public List<Long> IdealResEfficiencyTest(int totalComputationLoad, int threadName) throws TException {
        System.out.println("DBG@ Server Thrift IdealResEfficiencyTest "+totalComputationLoad+"  LoadNum"+threadName);
        return searchService.IdealResEfficiencyTest(totalComputationLoad,threadName);
    }
}
