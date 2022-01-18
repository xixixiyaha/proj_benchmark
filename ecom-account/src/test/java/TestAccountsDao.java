import com.freeb.Dao.AccountInfoStorage;
import com.freeb.Entity.AccountInfo;
import org.json.simple.JSONArray;
import org.junit.Before;
import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class TestAccountsDao {

    static String ACC_DB_URL = "jdbc:mysql://sh-cynosdbmysql-grp-fdsb56no.sql.tencentcdb.com:24262/FREEB";
    static String ACC_USER ="root";
    static String ACC_PWD = "1204Adzq";

    AccountInfoStorage storage;

    @Before
    public void Init() throws ClassNotFoundException {
        storage = new AccountInfoStorage(ACC_DB_URL, ACC_USER, ACC_PWD);
    }

    @Test
    public void TestGetAccountInfoById(){
        Random r = new Random();
        for(int i=0;i<1000;i++){
            Long uid = (long) r.nextInt(100000);
            AccountInfo info = storage.GetAccountInfoById(uid);

            assert info!=null;
            if(i<10){
                System.out.println("uid="+uid);
                System.out.println(info.toString());
            }
        }
    }

    @Test
    public void TestSetAndGetAccountTagById(){
        Random r = new Random();
        Set<Integer> mset;

        for(int j=0;j<10;j++){
            mset = new HashSet<>();
            while (mset.size()<10){
                mset.add(r.nextInt(50));
            }
            List<Integer> list = new ArrayList<>(mset);
            JSONArray jarr = new JSONArray();
            jarr.addAll(list);
            String jstr = jarr.toJSONString();
            for(int i=0;i<100;i++){
                Long uid = (long) (j*100+i+1);
                Boolean re = storage.SetAccountTagById(uid,jstr);
                assert re;
                List<Integer> lt = storage.GetAccountTagById(uid);
                assert lt.equals(list);
                List<Integer> lt1 = storage.GetAccountTagById(uid+10000);
                assert lt1==null;
            }
        }
    }

    @Test
    public void TestCreateUser() throws NoSuchAlgorithmException {
        String description = "description#description#description#description#description#description#description#description#description#description#" +
                "description#description#description#description#description#description#description#description#description#description#description#" +
                "description#description#description#description#description#description#description#description#description#description#description#";
        String userName = "username";
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        for(int i=0;i<100000;i++){
            String content = "password"+i; // i = user_id-1
            String a = messageDigest.digest(content.getBytes()).toString();
            Boolean re = storage.CreateAccountInfo(userName+i,a,description);
            if(!re){
                System.out.println("insert failed round"+i);
            }
        }

    }

    @Test
    public void TestUpdateAccountInfoById(){
        for(int i=0;i<1000;i++){
            long uid = 90000+i;
            AccountInfo info = storage.GetAccountInfoById(uid);
            info.setUserName("UpdateUsername"+uid);
            Boolean re = storage.UpdateAccountInfoById(info);
//            System.out.println(re);
        }
    }

    @Test
    public void TestDeleteAccountInfoById(){
        for(int i=99000;i<99999;i++){
            Boolean re = storage.DeleteAccountInfoById((long)i);
            System.out.println(re);

        }
    }

}
