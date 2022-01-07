package com.freeb.Entity;

import com.freeb.Enum.RespCode;

public class BaseResp {
    /**
     * Status : 200
     * Msg : feedback msg
     */

    private RespCode Status;
    private String Msg;

    public RespCode getStatus() {
        return Status;
    }

    public void setStatus(RespCode Status) {
        this.Status = Status;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }
}
