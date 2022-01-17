import com.freeb.Dao.CategoryStorage;
import com.freeb.Dao.CommentStorage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class TestCategoryDao {

    private static final Logger logger = LoggerFactory.getLogger(TestCategoryDao.class);

    static String CATE_DB_URL = "jdbc:mysql://sh-cynosdbmysql-grp-fdsb56no.sql.tencentcdb.com:24262/FREEB";
    static String CATE_USER ="root";
    static String CATE_PWD = "1204Adzq";

    private CategoryStorage cStorage;

    private CommentStorage cmtStorage;
    @Before
    public void Init() throws ClassNotFoundException {
//        cStorage = new CategoryStorage(CATE_DB_URL, CATE_USER, CATE_PWD);
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
            PreparedStatement stmt = conn.prepareStatement("truncate table CATEGORY_INFO");
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


    @Test
    public void CreateComments(){
        Random r=new Random(25);
        // 50*500 = 2w5
        String detail = "comment details ";
        for(int i=0;i<50;i++){
            Long pid = (long)r.nextInt(10000);
            for(int j=0;j<500;j++){
                Long uid = (long)r.nextInt(1000);
                cmtStorage.CreateComments(uid,pid,detail+uid+detail+pid,"invalid");
            }
        }
        for(int i=0;i<10000;i++){
            Long pid = (long)r.nextInt(10000);
            Long uid = (long)r.nextInt(100000);
            cmtStorage.CreateComments(uid,pid,detail+uid+detail+pid,"invalid");
        }
    }

}
