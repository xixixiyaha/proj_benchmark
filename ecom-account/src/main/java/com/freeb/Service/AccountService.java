package com.freeb.Service;

import com.freeb.Entity.AccountInfo;
import com.freeb.Enum.IdType;

import java.util.HashMap;
import java.util.List;

public interface AccountService {


    /*
     * Normal RPC call
     * */
    public Boolean AccountExists(Long id);
    public Boolean VerifyAccessByAccount(Long accountId, Long targetId, IdType idType);
    public List<Integer> GetAccountTag(Long id);
    public Boolean SetAccountTag(Long id, String jsonStr);


//   current deprecated
//    public HashMap<Integer,Double> GetUserTags(Long id);
//    public Boolean SetUserTags(Long id, HashMap<Integer,Double> tags);

    public Boolean ChangeAccountPwd(AccountInfo info, String pwd);


    /*
     * Mem bandwidth WRITE
     * */
    public Boolean CreateAccount(AccountInfo info);
    /*
    * Mem bandwidth READ
    * */
    public AccountInfo GetAccountInfo(Long id);

    public String CompareResEfficiencyBM1(String remoteFilePath,Integer testType);



}
