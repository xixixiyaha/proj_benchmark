package java.com.test;

import com.freeb.Dao.ProductInfoStorage;
import com.freeb.Entity.ProductInfo;
import org.junit.Before;
import org.junit.Test;

public class TestProductDao {
    static String PROD_DB_URL = "sh-cynosdbmysql-grp-fdsb56no.sql.tencentcdb.com:24262";
    static String PROD_USER ="root";
    static String PROD_PWD = "1204Adzq";

    private ProductInfoStorage storage;
    @Before
    public void Init(){
        storage = new ProductInfoStorage(PROD_DB_URL,PROD_USER,PROD_PWD);
    }

    @Test
    public void TestConnection(){
        assert storage.TestConnection();
    }



}
