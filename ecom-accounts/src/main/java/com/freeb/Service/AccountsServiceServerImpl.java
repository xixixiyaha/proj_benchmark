package com.freeb.Service;

import com.freeb.Dao.AccountInfoStorage;
import com.freeb.Entity.AccountsInfo;
import com.freeb.Enum.IdType;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AccountsServiceServerImpl implements AccountsService {

    private AccountInfoStorage storage = new AccountInfoStorage();
    private Boolean useStorage = true;

    @Override
    public Boolean AccountExists(Integer id) {
        if(storage.GetAccountInfoById(id)==null)return false;
        return true;
    }

    @Override
    public Boolean AccountExists(String name) {
        if(storage.GetAccountInfoByName(name)==null)return false;
        return true;
    }

    @Override
    public Boolean ChangeAccountPwd(AccountsInfo info, String newPwd) {
        AccountsInfo oldInfo;
        if((oldInfo=storage.GetAccountInfoByName(info.getUserName()))==null)return false;
        if(!oldInfo.getUserPwd().equals(info.getUserPwd())){
            return false;
        }
        oldInfo.setUserPwd(newPwd);
        return storage.UpdateAccountInfoById(oldInfo);
    }

    @Override
    public Boolean VerifyAccessByAccount(Integer accountId, Integer targetId, IdType idType) {
        //TODO
        return null;
    }

    @Override
    public List<Integer> GetAccountTag(Long id) {
        // todo
        return null;
    }

    @Override
    public HashMap<Integer, Double> GetUserTags(Long id) {
        return null;
    }

    @Override
    public Boolean SetUserTags(Long id, HashMap<Integer, Double> tags) {
        return null;
    }

    @Override
    public Boolean CreateAccount(AccountsInfo info) {
        if(storage.GetAccountInfoByName(info.getUserName())!=null)return false;
        UUID uuid = UUID.randomUUID();
        String strUuid = String.valueOf(uuid).replace("-","");
        long accountId = (long) strUuid.hashCode();
        accountId = accountId < 0 ? -accountId : accountId;
        info.setUserId(accountId);
        return storage.CreateAccountInfo(info);
    }

    @Override
    public AccountsInfo GetAccountInfo(Integer id) {

        return storage.GetAccountInfoById(id);
    }
}
