package com.freeb.Clients;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AccountClients {


    // 赋值
    protected abstract AccountClients getClient();

    public abstract List<Long> IdealResEfficiencyTest(Integer totalComputationLoad, Integer threadNum);



}
