package com.freeb.Service;

import com.freeb.Entity.AccountsInfo;
import com.freeb.Enum.IdType;

import java.util.HashMap;
import java.util.List;

public interface AccountsService {


    /*
     * Normal RPC call
     * */
    public Boolean AccountExists(Long id);
//    public Boolean AccountExists(String name);
    public Boolean VerifyAccessByAccount(Long accountId, Long targetId, IdType idType);
    public List<Integer> GetAccountTag(Long id);
    public Boolean SetAccountTag(Long id, String jsonStr);


//    //TODO current deprecated
//    public HashMap<Integer,Double> GetUserTags(Long id);
//    public Boolean SetUserTags(Long id,HashMap<Integer,Double> tags);

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
    public AccountsInfo GetAccountInfo(Long id);

    public String CompareResEfficiencyBM1(String remoteFilePath,Integer testType);



}
