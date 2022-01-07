import com.freeb.Dao.CategoryStorage;
import com.freeb.Dao.MerchantStorage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TestCategoryDao {

    private static final Logger logger = LoggerFactory.getLogger(TestCategoryDao.class);

    static String CATE_DB_URL = "jdbc:mysql://sh-cynosdbmysql-grp-fdsb56no.sql.tencentcdb.com:24262/FREEB";
    static String CATE_USER ="root";
    static String CATE_PWD = "1204Adzq";

    private CategoryStorage cStorage;
    private MerchantStorage mStorage;
    @Before
    public void Init() throws ClassNotFoundException {
//        cStorage = new CategoryStorage(CATE_DB_URL, CATE_USER, CATE_PWD);
        mStorage = new MerchantStorage(CATE_DB_URL, CATE_USER, CATE_PWD);
    }

    @Test
    public void CreateMerchant(){
        String str1= "Merchant Name:";
        for(int i=1;i<1001;i++){
            mStorage.CreateMerchantInfo(str1+i);
        }
    }

    @Test
    public void TestConnection(){
        assert cStorage.TestConn();
    }

    public void CreateCategory(Integer categoryNum){

        String str1= ":This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. This is a test. ";
        for(int i=0;i<categoryNum;i++){
            cStorage.CreateCategoryInfo(i + str1);
        }
    }

    @Test
    public void TestCreateData(){
        // 插入 param=100个用户 * param=100 个物品 => 1w条 => 20MB
        // 物品 [0,1000]
        // category [0,50]
        this.CreateCategory(50);
    }



    private void TruncateDB(){
        try(Connection conn = DriverManager.getConnection(CATE_DB_URL, CATE_USER, CATE_PWD)){
            PreparedStatement stmt = conn.prepareStatement("truncate table CATEGORY_INFOS");
            int re = stmt.executeUpdate();
            if(re<0){
                logger.warn("truncate table CATEGORY_INFOS FAILED !");
            }
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
        }
    }

    @After
    public void finale(){
//        TruncateDB();

    }

}
