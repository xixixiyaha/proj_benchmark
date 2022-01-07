package com.freeb.thrift;


import org.apache.thrift.TException;

import java.util.List;

public class SearchServiceServerImpl implements SearchService.Iface {

    private final com.freeb.Service.SearchService searchService = new com.freeb.Service.SearchServiceImpl();
    @Override
    public List<Long> IdealResEfficiencyTest(int totalComputationLoad, int threadName) throws TException {
        System.out.println("DBG@ Server Thrift IdealResEfficiencyTest "+totalComputationLoad+"  LoadNum"+threadName);
        return searchService.IdealResEfficiencyTest(totalComputationLoad,threadName);
    }
}
