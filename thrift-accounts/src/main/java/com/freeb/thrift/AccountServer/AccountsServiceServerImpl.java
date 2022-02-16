package com.freeb.thrift.AccountServer;

import com.freeb.thrift.AccountClients.AccountForeignClients;
import com.freeb.Service.AccountServiceImpl;
import com.freeb.thrift.AccountInfo;
import com.freeb.thrift.AccountService;
import com.freeb.thrift.IdType;
import org.apache.thrift.TException;
import com.freeb.AccountTypeConvert;

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
        return accountService.VerifyAccessByAccount(accountId,targetId, AccountTypeConvert.IdTypeThr2Ori(idType));
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
        return accountService.ChangeAccountPwd(AccountTypeConvert.AccountsInfoThr2Ori(info),passwd);
    }

    @Override
    public boolean CreateAccount(AccountInfo info) throws TException {
        return accountService.CreateAccount(AccountTypeConvert.AccountsInfoThr2Ori(info));
    }

    @Override
    public AccountInfo GetAccountInfo(long id) throws TException {
        return AccountTypeConvert.AccountsInfoOri2Thr(accountService.GetAccountInfo(id));
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
