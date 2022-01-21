package com.freeb.AccountServer;

import com.freeb.AccountClients.AccountForeignClients;
import com.freeb.Service.AccountServiceImpl;
import org.apache.thrift.TException;
import com.freeb.AccountsTypeConvert;

import java.util.List;

public class AccountsServiceServerImpl implements AccountService.Iface{

    private static boolean IS_DBG=true; 

    private final com.freeb.Service.AccountService accountService = new AccountServiceImpl(new AccountForeignClients());
    @Override
    public boolean AccountExists(long id) throws TException {
        return accountService.AccountExists(id);
    }

    @Override
    public boolean VerifyAccessByAccount(long accountId, long targetId, IdType idType) throws TException {
        return accountService.VerifyAccessByAccount(accountId,targetId,AccountsTypeConvert.IdTypeThr2Ori(idType));
    }

    @Override
    public List<Integer> GetAccountTag(long id) throws TException {
        return accountService.GetAccountTag(id);
    }

    @Override
    public boolean SetAccountTag(long id, String jsonStr) throws TException {
        return accountService.SetAccountTag(id,jsonStr);
    }

    @Override
    public boolean ChangeAccountPwd(AccountInfo info, String passwd) throws TException {
        return accountService.ChangeAccountPwd(AccountsTypeConvert.AccountsInfoThr2Ori(info),passwd);
    }

    @Override
    public boolean CreateAccount(AccountInfo info) throws TException {
        return accountService.CreateAccount(AccountsTypeConvert.AccountsInfoThr2Ori(info));
    }

    @Override
    public AccountInfo GetAccountInfo(long id) throws TException {
        return AccountsTypeConvert.AccountsInfoOri2Thr(accountService.GetAccountInfo(id));
    }

    @Override
    public String CompareResEfficiencyBM1(String remoteFilePath, int testType) throws TException {
        if(IS_DBG){
            System.out.println("IN AccountServiceImpl/com.freeb.thrift CompareResEfficiencyBM1");
        }
        String str = accountService.CompareResEfficiencyBM1(remoteFilePath,testType);
        if(IS_DBG) {
            System.out.println("POST AccountServiceImpl/com.freeb.thrift CompareResEfficiencyBM1");
        }
        return str;
    }
}
