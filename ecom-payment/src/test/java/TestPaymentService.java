import com.freeb.Dao.PaymentInfoStorage;
import com.freeb.Entity.PaymentInfo;
import com.freeb.Enum.PaymentStatus;
import com.freeb.Service.PaymentServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.security.PublicKey;
import java.util.Random;

public class TestPaymentService {

    //Notice 测试需要在 storage 为 public时使用
    static String PAY_DB_URL = "jdbc:mysql://sh-cynosdbmysql-grp-fdsb56no.sql.tencentcdb.com:24262/FREEB";
    static String PAY_USER ="root";
    static String PAY_PSW = "1204Adzq";

    PaymentServiceImpl service;

    @Before
    public void Init() throws ClassNotFoundException {
        service = new PaymentServiceImpl(PAY_DB_URL, PAY_USER, PAY_PSW);
    }

    @Test
    public void TestVerifyAccess2Payment(){
        // 已经包括在了TestGetPaymentInfoById内 不单独写了

    }

//    @Test
//    public void TestCheckPaymentStatusById(){
//        Random r= new Random();
//        for(int i=0;i<1000;i++){
//            Long pid = (long)r.nextInt(10000);
//            PaymentInfo info = service.storage.GetPaymentInfoById(pid);
//            assert info!=null;
//            Long uid = info.getUserId();
//            assert uid!=null;
//            PaymentStatus status1 = service.CheckPaymentStatusById(uid,pid);
//            assert status1!=PaymentStatus.PAYMENT_NO_RECORD;
//            PaymentStatus status2 = service.CheckPaymentStatusById(uid+1,pid);
//            assert status2 ==PaymentStatus.PAYMENT_NO_RECORD;
//        }
//    }
//
//    @Test
//    public void TestCancelPayment(){
//        Random r= new Random();
//        for(long pid=1;pid<=1000;pid++){
//            PaymentInfo info = service.storage.GetPaymentInfoById(pid);
//            assert info!=null;
//            Long uid = info.getUserId();
//            assert uid!=null;
//            Boolean re1 = service.CancelPayment(uid,pid);
//            assert re1;
//        }
//    }


    private String genCardNum(){
        Random r= new Random(5L);
        Integer fir = r.nextInt(3000)+1000;
        Integer sec = r.nextInt(3000)+1000;
        return (fir +String.valueOf(sec));
    }

    @Test
    public void CreatePayment(){
        int record = 10000; // 1w
        Random r = new Random(13L);
        for(int i=0;i<record;i++){
            Double price = 1.0*r.nextInt(1000)+5;
            Double dicounts = price/((i&4)+1);
            Long uid = (long)r.nextInt(1000);
            PaymentInfo info = new PaymentInfo();
            info.setPaymentStatus((i&4));
            info.setPaymentVal(price);
            info.setDiscountsVal(dicounts);
            info.setPaymentCard(genCardNum());
            info.setUserId(uid);
            Long re = service.CreatePayment(info);
            assert re!=null;
        }
    }
//
//    @Test
//    public void TestGetPaymentInfoById(){
//        Random r= new Random();
//        for(int i=0;i<1000;i++){
//            Long pid = (long)r.nextInt(10000);
//            PaymentInfo info = service.storage.GetPaymentInfoById(pid);
//            assert info!=null;
//            Long uid = info.getUserId();
//            assert uid!=null;
//            PaymentInfo info1 = service.GetPaymentInfoById(uid,pid);
//            assert info1!=null;
//            PaymentInfo info2 = service.GetPaymentInfoById(uid+1,pid);
//            assert info2 ==null;
//        }
//    }


}
