import com.freeb.Dao.MerchantStorage;
import org.junit.Before;
import org.junit.Test;

public class TestMerchantDao {

    static String MCT_DB_URL = "jdbc:mysql://sh-cynosdbmysql-grp-fdsb56no.sql.tencentcdb.com:24262/FREEB";
    static String MCT_USER ="root";
    static String MCT_PSW = "1204Adzq";
    private MerchantStorage mStorage;
    @Before
    public void Init() throws ClassNotFoundException {
//        cStorage = new CategoryStorage(CATE_DB_URL, CATE_USER, CATE_PWD);
        mStorage = new MerchantStorage(MCT_DB_URL, MCT_USER, MCT_PSW);
    }

    @Test
    public void CreateMerchant(){
        String str1= "Merchant Name:";
        for(int i=1;i<1001;i++){
            mStorage.CreateMerchantInfo(str1+i);
        }
    }


}
