package com.freeb.Service;

import com.freeb.Entity.AccountsInfo;
import com.freeb.Enum.IdType;

public interface AccountsService {



    public Boolean AccountExists(Integer id);

    public Boolean AccountExists(String name);

    public Boolean ChangeAccountPwd(AccountsInfo info,String pwd);

    public Boolean VerifyAccessByAccount(Integer accountId,Integer targetId, IdType idType);

    public Boolean CreateAccount(AccountsInfo info);

    public AccountsInfo GetAccountInfo(Integer id);

}
