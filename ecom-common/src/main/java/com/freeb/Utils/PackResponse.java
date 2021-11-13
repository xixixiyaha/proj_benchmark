package com.freeb.Utils;

import com.freeb.Entity.BaseResp;
import com.freeb.Enum.RespCode;

public class PackResponse {

    public static BaseResp packNoAuthority(){
        BaseResp baseResp = new BaseResp();
        baseResp.setStatus(RespCode.DENY_NO_AUTHORITY);
        return baseResp;
    }

    public static BaseResp packSuccess(){
        BaseResp baseResp = new BaseResp();
        baseResp.setStatus(RespCode.SUCCESS);
        return baseResp;
    }

}
