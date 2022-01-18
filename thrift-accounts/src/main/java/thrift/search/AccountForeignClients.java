package thrift.search;

import com.freeb.Clients.AccountClients;
import com.freeb.Utils.LockObjectPool;
import org.apache.thrift.TException;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public class AccountForeignClients extends AccountClients implements Closeable {
    private static String host = "bm-search-server";
    private static int port = 8080;
    static {
        System.out.println("in AccountForeignClients");
    }
    private final LockObjectPool<ThriftSearchClientImpl> clientPool = new LockObjectPool<>(32,()->new ThriftSearchClientImpl(host,port));

    @Override
    public void close() throws IOException {
        clientPool.close();
    }

    //Todo Notice if valid
    @Override
    protected AccountClients getClient() {
        return this;
    }

    @Override
    public List<Long> IdealResEfficiencyTest(Integer totalComputationLoad, Integer threadNum) {
        ThriftSearchClientImpl client = clientPool.borrow();
        System.out.println("IdealResEfficiencyTest/AccountForeignClients");
        try{
            return client.client.IdealResEfficiencyTest(totalComputationLoad,threadNum);
        } catch (TException e) {
            e.printStackTrace();
        }finally {
            clientPool.release(client);
        }
        return null;
    }
}
