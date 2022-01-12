package com.freeb.Clients;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AccountsClients {

    private final AtomicInteger counter = new AtomicInteger(0);

    // 赋值
    // TODO 看继承函数的覆盖条件
    protected abstract AccountsClients getClient();

    public abstract List<Long> IdealResEfficiencyTest(Integer totalComputationLoad, Integer threadNum);



}
