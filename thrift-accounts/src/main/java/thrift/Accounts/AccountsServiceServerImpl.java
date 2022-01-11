package thrift.Accounts;

import com.freeb.Service.AccountsServiceImpl;
import org.apache.thrift.TException;
import thrift.AccountsTypeConvert;
import thrift.search.AccountsForeignClients;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountsServiceServerImpl implements AccountsService.Iface{

    private static boolean IS_DBG=true; 

    private final com.freeb.Service.AccountsService accountsService = new AccountsServiceImpl(new AccountsForeignClients());
    @Override
    public boolean AccountExists(long id) throws TException {
        return accountsService.AccountExists(id);
    }

    @Override
    public boolean VerifyAccessByAccount(long accountId, long targetId, IdType idType) throws TException {
        return accountsService.VerifyAccessByAccount(accountId,targetId,AccountsTypeConvert.IdTypeThr2Ori(idType));
    }

    @Override
    public List<Integer> GetAccountTag(long id) throws TException {
        return accountsService.GetAccountTag(id);
    }

    @Override
    public Map<Integer, Double> GetUserTags(long id) throws TException {
        return accountsService.GetUserTags(id);
    }

    @Override
    public boolean SetUserTags(long id, Map<Integer, Double> tags) throws TException {
        return accountsService.SetUserTags(id, (HashMap<Integer, Double>) tags);
    }

    @Override
    public boolean ChangeAccountPwd(AccountsInfo info, String passwd) throws TException {
        return accountsService.ChangeAccountPwd(AccountsTypeConvert.AccountsInfoThr2Ori(info),passwd);
    }

    @Override
    public boolean CreateAccount(AccountsInfo info) throws TException {
        return accountsService.CreateAccount(AccountsTypeConvert.AccountsInfoThr2Ori(info));
    }

    @Override
    public AccountsInfo GetAccountInfo(long id) throws TException {
        return AccountsTypeConvert.AccountsInfoOri2Thr(accountsService.GetAccountInfo(id));
    }

    @Override
    public String CompareResEfficiencyBM1(String remoteFilePath, int testType) throws TException {
        if(IS_DBG){
            System.out.println("IN AccountsServiceImpl/thrift CompareResEfficiencyBM1");
        }
        String str = accountsService.CompareResEfficiencyBM1(remoteFilePath,testType);
        System.out.println("POST AccountsServiceImpl/thrift CompareResEfficiencyBM1");
        return str;
    }
}
