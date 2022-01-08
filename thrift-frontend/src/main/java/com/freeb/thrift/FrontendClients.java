package com.freeb.thrift;

import com.freeb.Utils.LockObjectPool;
import com.freeb.thrift.Accounts.ThriftAccountClientImpl;

public class FrontendClients {

    private static String host = "benchmark-server";
    private static int port = 8080;
    static {
        System.out.println("in ForeignClients");
    }
    private final LockObjectPool<ThriftAccountClientImpl> clientPool = new LockObjectPool<>(32,()->new ThriftAccountClientImpl(host,port));

    //TODO 很多很多的函数
}
