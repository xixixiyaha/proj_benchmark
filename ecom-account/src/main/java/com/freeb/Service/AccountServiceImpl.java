package com.freeb.Service;

import com.freeb.Clients.AccountClients;
import com.freeb.Dao.AccountInfoStorage;
import com.freeb.Entity.AccountInfo;
import com.freeb.Enum.IdType;
import com.mysql.cj.util.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.io.*;

import ch.ethz.ssh2.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);


    private AccountInfoStorage storage = new AccountInfoStorage();
    private Boolean useStorage = true;
    private AccountClients client ;

    private static String BM1_IP; //= "124.223.34.172"; // account 到 search host 下载 json
    private static Integer BM1_PORT; // = 22;
    private static String BM1_USER_NAME; // = "benchmark";
    private static String BM1_PSW; // = "benchmark";
    private static String BM1_LOCAL_PATH; // = "/home/benchmark/";
    private static String BM1_LOCAL_FILENAME; // = "bm1test1.json";

    static {


        Properties properties = new Properties();
        // 使用ClassLoader加载properties配置文件生成对应的输入流
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader("./proj_benchmark.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 使用properties对象加载输入流
        try {
            assert in != null;
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BM1_IP = properties.getProperty("BM1_IP");
        BM1_PORT = Integer.valueOf(properties.getProperty("BM1_PORT"));
        BM1_USER_NAME = properties.getProperty("BM1_USER_NAME");
        BM1_PSW = properties.getProperty("BM1_PSW");
        BM1_LOCAL_PATH = properties.getProperty("BM1_LOCAL_PATH");
        BM1_USER_NAME = properties.getProperty("BM1_USER_NAME");
        BM1_LOCAL_FILENAME = properties.getProperty("BM1_LOCAL_FILENAME");



    }

    public AccountServiceImpl(AccountClients accClient){
        this.client = accClient;

    }

    //Notice Tag1
    @Override
    public Boolean AccountExists(Long id) {
        return storage.GetAccountInfoById(id) != null;
    }


    public Boolean AccountExists(String name) {
        return storage.GetAccountInfoByName(name) != null;
    }

    @Override
    public Boolean ChangeAccountPwd(AccountInfo info, String newPwd) {
        AccountInfo oldInfo;
        if((oldInfo=storage.GetAccountInfoByName(info.getUserName()))==null)return false;
        if(!oldInfo.getUserPasswd().equals(info.getUserPasswd())){
            return false;
        }
        oldInfo.setUserPasswd(newPwd);
        return storage.UpdateAccountInfoById(oldInfo);
    }

    @Override
    public Boolean VerifyAccessByAccount(Long accountId, Long targetId, IdType idType) {
        //TODO@ low priority -- wrong design
        return true;
    }

    @Override
    public List<Integer> GetAccountTag(Long id) {
        if(!AccountExists(id)){
            logger.warn("Non-exists user_id="+id);
            return null;
        }
        List<Integer> re = storage.GetAccountTagById(id);
        if(null==re){
            logger.warn("getTag Failed user_id="+id);
        }
        return null;
    }

    @Override
    public Boolean SetAccountTag(Long id, String jsonStr) {
        if(!AccountExists(id)){
            logger.warn("Non-exists user_id="+id);
            return false;
        }
        Boolean re = storage.SetAccountTagById(id,jsonStr);
        if(!re){
            logger.warn("setTag Failed user_id="+id);
        }
        return re;
    }

//    @Override
//    public HashMap<Integer, Double> GetUserTags(Long id) {
//        logger.error("Method Not Implemented");
//        return null;
//    }
//
//    @Override
//    public Boolean SetUserTags(Long id, HashMap<Integer, Double> tags) {
//        logger.error("Method Not Implemented");
//        return null;
//    }

    @Override
    public Boolean CreateAccount(AccountInfo info) {
        if(storage.GetAccountInfoByName(info.getUserName())!=null)return false;
        UUID uuid = UUID.randomUUID();
        String strUuid = String.valueOf(uuid).replace("-","");
        long accountId = (long) strUuid.hashCode();
        accountId = accountId < 0 ? -accountId : accountId;
        info.setUserId(accountId);
        return storage.CreateAccountInfo(info);
    }

    @Override
    public AccountInfo GetAccountInfo(Long id) {

        return storage.GetAccountInfoById(id);
    }

    class IdealComputationThread extends Thread{
        private Integer loopTime;
        private Long computationRe;

        IdealComputationThread(Integer loopt){
            this.loopTime = loopt;
        }

        @Override
        public void run() {
            Random seed = new Random(7L);
            Integer a = seed.nextInt(100)+50;
            Integer b= seed.nextInt(100)+50;
            long temp = 0L;
            for(int time = 0; time<this.loopTime; time++){
                if((time&3)==0){
                    temp = a+b;
                }else if((time&3)==1){
                    temp = a-b;
                }else if((time&3)==2){
                    temp = a*b;
                }else {
                    temp = a/b;
                }
            }
            this.computationRe=temp;
        }

        public Long getComputationRe(){
            return this.computationRe; //
        }
    }

    private List<Long> LocalIdealResEfficiencyTest(Integer totalComputationLoad, Integer threadNum) {
        long begintime = System.currentTimeMillis();
        List<Long> results = new ArrayList<>();
        Integer loopPerThread = totalComputationLoad/threadNum;
        List<IdealComputationThread> threads = new ArrayList<>();
        for(int i=0;i<threadNum;i++){
            IdealComputationThread thread = new IdealComputationThread(loopPerThread);
            thread.start();
            System.out.println("DBG@ threads len = "+threads.size());
            threads.add(thread);
            System.out.println("DBG@ put "+i+" thread");
        }
        for(IdealComputationThread thread:threads){
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for(Integer i=0;i<threadNum;i++){
            results.add(threads.get(i).getComputationRe());
        }
        long endtime = System.currentTimeMillis();
        long costtime = (endtime-begintime);
        logger.info("beginTime = "+begintime);
        logger.info("endTime = "+endtime);
        logger.info("costTime = "+costtime);
        return results;
    }

    private Connection conn = null;
    private Session session = null;

    private boolean preConnection(String ip,int port,String userName,String password){
        try {
            if (StringUtils.isNullOrEmpty(ip) || StringUtils.isNullOrEmpty(userName) || StringUtils.isNullOrEmpty(password)) {
                return false;
            }
            conn = new Connection(ip, port);
            conn.connect();
            boolean isAuth = conn.authenticateWithPassword(userName, password);
            if (!isAuth) {
                logger.info("算法主机连接失败");
                return false;
            }

            //执行命令
            session = conn.openSession();
        }catch (Exception e){
            e.printStackTrace();
            logger.info(e.getMessage());
        }
        return true;
    }

    private void postConnection(){
        if (null != session){
            session.close();
        }
        if (null != conn) {
            conn.close();
        }
    }

    //远程下载服务器文件
    public boolean copyFile(String sourceFile,String targetFile,String targetFileName){
        boolean bool = false;
        try {
            if (StringUtils.isNullOrEmpty(sourceFile) || StringUtils.isNullOrEmpty(targetFile)){
                return bool;
            }

            //下载文件到本地
            SCPClient scpClient = new SCPClient(conn);
            SCPInputStream scpis = scpClient.get(sourceFile);

            //判断指定目录是否存在，不存在则先创建目录
            File file = new File(targetFile);
            if (!file.exists()){
                file.mkdirs();
            }

            FileOutputStream fos = new FileOutputStream(targetFile + targetFileName);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = scpis.read(buffer)) != -1){
                fos.write(buffer,0,len);
            }
            fos.close();
            bool = true;
            //SFTP
            /*SFTPv3Client sftPClient = new SFTPv3Client(conn);
            sftPClient.createFile("");
            sftPClient.close();*/
        }catch (Exception e){
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info("保存失败：" + sourceFile);
        }finally {
            if (null != session){
                session.close();
            }
            if (null != conn) {
                conn.close();
            }
        }

        return bool;
    }



    @Override
    public String CompareResEfficiencyBM1(String remoteFilePath,Integer testType) {
        logger.info("DBG@ AccountServiceImpl/ecom-Account");
        List<Long> results;
        long bengintime = System.nanoTime();
        Integer totalWorkLoad=0,threadNum = 1;
        // 1.
        boolean conn = preConnection(BM1_IP,BM1_PORT,BM1_USER_NAME,BM1_PSW);
        if(!conn){
            logger.error("unable to connect");
            return "";
        }
        boolean fecth = copyFile("/home/benchmark/bm/bm1test1.json",BM1_LOCAL_PATH,BM1_LOCAL_FILENAME);
        if(!fecth){
            logger.error("unable to download file");
            return "";
        }
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader("./bm1test1.json"));

            JSONObject jsonObject =  (JSONObject) obj;

            totalWorkLoad = (Long.valueOf((long) jsonObject.get("totalWorkLoad"))).intValue();
            System.out.println(totalWorkLoad);

            threadNum = (Long.valueOf((long) jsonObject.get("threadNum"))).intValue();
            System.out.println(threadNum);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        long midtime = System.nanoTime();
        long fetchtime = (midtime-bengintime)/1000;
        logger.info("fetchtime = "+fetchtime);
        testType=1;
        if(testType==0){
            results = LocalIdealResEfficiencyTest(totalWorkLoad,threadNum);

        }else{
            results = client.IdealResEfficiencyTest(totalWorkLoad,threadNum);
        }

        long endtime = System.nanoTime();
        long costtime = (endtime-bengintime)/1000;
        logger.info("costTime = "+costtime);
        return BM1_LOCAL_PATH+BM1_LOCAL_FILENAME;
    }
}
