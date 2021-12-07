package java.com.test;
import com.freeb.Clients.SearchClients;
import com.freeb.Service.Recommend;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public class TestRecommend {

    private static final Logger logger = LoggerFactory.getLogger(TestRecommend.class);


    private SearchClients clients;
    private Recommend rcmdService;

    @Before
    public void initi(){
        rcmdService = new Recommend(clients);
    }


    @Test
    public void TestAssembleUserClicks(){
        long startTime = System.nanoTime();
        ConcurrentHashMap<Long, HashSet<Long>> rcmdProd = rcmdService.AssembleUserClicks();
        long endTime = System.nanoTime();

        long duration = (long) ((endTime - startTime)/1e6);  //divide by 1000000 to get milliseconds.

        logger.debug("duration is "+duration);
        logger.debug(rcmdProd.toString());

    }


}
