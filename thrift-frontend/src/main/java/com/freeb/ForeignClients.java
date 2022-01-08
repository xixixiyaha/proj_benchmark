package com.freeb;

import com.freeb.Utils.LockObjectPool;
import com.freeb.thrift.Accounts.AccountsInfo;
import com.freeb.thrift.Accounts.AccountsTypeConvert;
import com.freeb.thrift.Accounts.IdType;
import com.freeb.thrift.Accounts.ThriftAccountClientImpl;
import org.apache.thrift.TException;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ForeignClients implements Closeable {

    private static String host = "benchmark-server";
    private static int port = 8080;
    static {
        System.out.println("in AccountsForeignClients");
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

    public Map<Integer, Double> GetUserTags(long id) throws TException {
        ThriftAccountClientImpl client = clientPool.borrow();
        try{
            return client.client.GetUserTags(id);
        } catch (TException e) {
            e.printStackTrace();
        }finally {
            clientPool.release(client);
        }
        return null;
    }

    public boolean SetUserTags(long id, Map<Integer, Double> tags) throws TException {
        ThriftAccountClientImpl client = clientPool.borrow();
        try{
            return client.client.SetUserTags(id,tags);
        } catch (TException e) {
            e.printStackTrace();
        }finally {
            clientPool.release(client);
        }
        return false;
    }

    public boolean ChangeAccountPwd(AccountsInfo info, String passwd) throws TException {
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

    public boolean CreateAccount(AccountsInfo info) throws TException {
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

    public AccountsInfo GetAccountInfo(long id) throws TException {
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
