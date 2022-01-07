package com.freeb.Service;

import com.freeb.Entity.AccountsInfo;
import com.freeb.Enum.IdType;

import java.util.HashMap;
import java.util.List;

public interface AccountsService {


    /*
     * Normal RPC call
     * */
    public Boolean AccountExists(Integer id);
    public Boolean AccountExists(String name);
    public Boolean VerifyAccessByAccount(Integer accountId,Integer targetId, IdType idType);
    public List<Integer> GetAccountTag(Long id);
    public HashMap<Integer,Double> GetUserTags(Long id);
    public Boolean SetUserTags(Long id,HashMap<Integer,Double> tags);

    /*
     * todo CPU contention
     * */
    public Boolean ChangeAccountPwd(AccountsInfo info,String pwd);


    /*
     * Mem bandwidth WRITE
     * */
    public Boolean CreateAccount(AccountsInfo info);
    /*
    * Mem bandwidth READ
    * */
    public AccountsInfo GetAccountInfo(Integer id);

    public void CompareResEfficiencyBM1(String remoteFilePath,Integer testType);



}
