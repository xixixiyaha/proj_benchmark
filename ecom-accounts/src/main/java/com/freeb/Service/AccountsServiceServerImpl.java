package com.freeb.Service;

import com.freeb.Dao.AccountInfoStorage;
import com.freeb.Entity.AccountsInfo;
import com.freeb.Enum.IdType;

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
    public Boolean CreateAccount(AccountsInfo info) {
        if(storage.GetAccountInfoByName(info.getUserName())!=null)return false;
        UUID uuid = UUID.randomUUID();
        String strUuid = String.valueOf(uuid).replace("-","");
        Integer accountId = strUuid.hashCode();
        accountId = accountId < 0 ? -accountId : accountId;
        info.setAccountId(accountId);
        return storage.CreateAccountInfo(info);
    }

    @Override
    public AccountsInfo GetAccountInfo(Integer id) {

        return storage.GetAccountInfoById(id);
    }
}
