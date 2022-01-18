package thrift.Accounts;

import com.freeb.Service.AccountService;
import com.freeb.Service.AccountServiceImpl;
import org.apache.thrift.TException;
import thrift.AccountsTypeConvert;
import thrift.search.AccountForeignClients;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountsServiceServerImpl implements AccountsService.Iface{

    private static boolean IS_DBG=true; 

    private final AccountService accountService = new AccountServiceImpl(new AccountForeignClients());
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
    public Map<Integer, Double> GetUserTags(long id) throws TException {
        return accountService.GetUserTags(id);
    }

    @Override
    public boolean SetUserTags(long id, Map<Integer, Double> tags) throws TException {
        return accountService.SetUserTags(id, (HashMap<Integer, Double>) tags);
    }

    @Override
    public boolean ChangeAccountPwd(AccountsInfo info, String passwd) throws TException {
        return accountService.ChangeAccountPwd(AccountsTypeConvert.AccountsInfoThr2Ori(info),passwd);
    }

    @Override
    public boolean CreateAccount(AccountsInfo info) throws TException {
        return accountService.CreateAccount(AccountsTypeConvert.AccountsInfoThr2Ori(info));
    }

    @Override
    public AccountsInfo GetAccountInfo(long id) throws TException {
        return AccountsTypeConvert.AccountsInfoOri2Thr(accountService.GetAccountInfo(id));
    }

    @Override
    public String CompareResEfficiencyBM1(String remoteFilePath, int testType) throws TException {
        if(IS_DBG){
            System.out.println("IN AccountServiceImpl/thrift CompareResEfficiencyBM1");
        }
        String str = accountService.CompareResEfficiencyBM1(remoteFilePath,testType);
        System.out.println("POST AccountServiceImpl/thrift CompareResEfficiencyBM1");
        return str;
    }
}
