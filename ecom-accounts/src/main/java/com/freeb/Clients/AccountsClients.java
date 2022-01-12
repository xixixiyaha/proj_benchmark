package com.freeb.Clients;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AccountsClients {

    private final AtomicInteger counter = new AtomicInteger(0);

    // 赋值
    protected abstract AccountsClients getClient();

    public abstract List<Long> IdealResEfficiencyTest(Integer totalComputationLoad, Integer threadNum);



}
