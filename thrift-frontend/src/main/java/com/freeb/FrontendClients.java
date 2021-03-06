package com.freeb;

import com.freeb.Utils.LockObjectPool;
import com.freeb.thrift.AccountInfo;
import com.freeb.thrift.IdType;
import com.freeb.Accounts.ThriftAccountClientImpl;
import org.apache.thrift.TException;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public class FrontendClients implements Closeable {

    //Notice Tag2
    private static String host ="bm-accounts-server";
    private static int port = 8081;
    private static Boolean IS_DBG=true;
    static {
        System.out.println("in FrontendClients");
    }
    private final LockObjectPool<ThriftAccountClientImpl> clientPool = new LockObjectPool<>(32,()->new ThriftAccountClientImpl(host,port));


    public boolean AccountExists(long id) throws TException {
        ThriftAccountClientImpl client = clientPool.borrow();
        try{
            return client.client.AccountExists(id);
        } catch (TException e) {
            e.printStackTrace();
        }finally {
            clientPool.release(client);
        }
        return false;
    }

    public boolean VerifyAccessByAccount(long accountId, long targetId, IdType idType) throws TException {
        ThriftAccountClientImpl client = clientPool.borrow();
        try{
            return client.client.VerifyAccessByAccount(accountId, targetId, idType);
        } catch (TException e) {
            e.printStackTrace();
        }finally {
            clientPool.release(client);
        }
        return false;
    }

    public List<Integer> GetAccountTag(long id) throws TException {
        ThriftAccountClientImpl client = clientPool.borrow();
        try{
            return client.client.GetAccountTag(id);
        } catch (TException e) {
            e.printStackTrace();
        }finally {
            clientPool.release(client);
        }
        return null;
    }

    public boolean ChangeAccountPwd(AccountInfo info, String passwd) throws TException {
        ThriftAccountClientImpl client = clientPool.borrow();
        try{
            return client.client.ChangeAccountPwd(info,passwd);
        } catch (TException e) {
            e.printStackTrace();
        }finally {
            clientPool.release(client);
        }
        return false;
    }

    public boolean CreateAccount(AccountInfo info) throws TException {
        ThriftAccountClientImpl client = clientPool.borrow();
        try{
            return client.client.CreateAccount(info);
        } catch (TException e) {
            e.printStackTrace();
        }finally {
            clientPool.release(client);
        }
        return false;
    }

    public AccountInfo GetAccountInfo(long id) throws TException {
        ThriftAccountClientImpl client = clientPool.borrow();
        try{
            return client.client.GetAccountInfo(id);
//            return AccountsTypeConvert.AccountsInfoThr2Ori(client.client.GetAccountInfo(id));
        } catch (TException e) {
            e.printStackTrace();
        }finally {
            clientPool.release(client);
        }
        return null;
    }

    public String CompareResEfficiencyBM1(String remoteFilePath, int testType) throws TException {
        ThriftAccountClientImpl client = clientPool.borrow();
        try{
            if(IS_DBG){
                System.out.println("IN FrontendClients CompareResEfficiencyBM1");
            }
            return client.client.CompareResEfficiencyBM1(remoteFilePath, testType);
        } catch (TException e) {
            e.printStackTrace();
        }finally {
            clientPool.release(client);
        }
        return "";
    }


    @Override
    public void close() throws IOException {
        clientPool.close();
    }
}
