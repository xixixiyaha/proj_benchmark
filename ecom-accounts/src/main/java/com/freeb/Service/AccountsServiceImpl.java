package com.freeb.Service;

import com.freeb.Clients.AccountsClients;
import com.freeb.Dao.AccountInfoStorage;
import com.freeb.Entity.AccountsInfo;
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


public class AccountsServiceImpl implements AccountsService {

    private static final Logger logger = LoggerFactory.getLogger(AccountsServiceImpl.class);


    private AccountInfoStorage storage = new AccountInfoStorage();
    private Boolean useStorage = true;
    private AccountsClients client ;

    public AccountsServiceImpl(AccountsClients accClient){
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
    public Boolean ChangeAccountPwd(AccountsInfo info, String newPwd) {
        AccountsInfo oldInfo;
        if((oldInfo=storage.GetAccountInfoByName(info.getUserName()))==null)return false;
        if(!oldInfo.getUserPasswd().equals(info.getUserPasswd())){
            return false;
        }
        oldInfo.setUserPasswd(newPwd);
        return storage.UpdateAccountInfoById(oldInfo);
    }

    @Override
    public Boolean VerifyAccessByAccount(Long accountId, Long targetId, IdType idType) {
        //TODO
        return null;
    }

    @Override
    public List<Integer> GetAccountTag(Long id) {
        // todo
        return null;
    }

    @Override
    public HashMap<Integer, Double> GetUserTags(Long id) {
        return null;
    }

    @Override
    public Boolean SetUserTags(Long id, HashMap<Integer, Double> tags) {
        return null;
    }

    @Override
    public Boolean CreateAccount(AccountsInfo info) {
        if(storage.GetAccountInfoByName(info.getUserName())!=null)return false;
        UUID uuid = UUID.randomUUID();
        String strUuid = String.valueOf(uuid).replace("-","");
        long accountId = (long) strUuid.hashCode();
        accountId = accountId < 0 ? -accountId : accountId;
        info.setUserId(accountId);
        return storage.CreateAccountInfo(info);
    }

    @Override
    public AccountsInfo GetAccountInfo(Long id) {

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
            //执行命令并打印执行结果
            // session.execCommand("df -h");
            // InputStream staout = new StreamGobbler(session.getStdout());
            // BufferedReader br = new BufferedReader(new InputStreamReader(staout));
            // String line = null;
            // while ((line = br.readLine()) != null){
            //     System.out.println(line);
            // }
            // br.close();

            //下载文件到本地
            System.out.println("pre SCPClient");
            //下载文件到本地
            SCPClient scpClient = new SCPClient(conn);
            System.out.println("pre SCPInputStream");
            SCPInputStream scpis = scpClient.get(sourceFile);

            //判断指定目录是否存在，不存在则先创建目录
            File file = new File(targetFile);
            if (!file.exists()){
                System.out.println("pre mkdirs");
                file.mkdirs();
            }
            System.out.println("pre FileOutputStream");

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

    //TODO Notice
    private static String IP = "124.223.34.172"; // account 到 search host 下载 json
    private static Integer PORT = 22;
    private static String USER_Name = "benchmark";
    private static String PASSWORD = "benchmark";


    @Override
    public String CompareResEfficiencyBM1(String remoteFilePath,Integer testType) {
        System.out.println("CompareResEfficiencyBM1/Accounts");
        logger.info("DBG@ AccountsServiceImpl/ecom");
        List<Long> results;
        long bengintime = System.nanoTime();
        Integer totalWorkLoad=0,threadNum = 1;
        // 1.
        boolean conn = preConnection(IP,PORT,USER_Name,PASSWORD);
        if(!conn){
            logger.error("unable to connect");
            return "";
        }
        boolean fecth = copyFile("/home/benchmark/bm/bm1test1.json","/home/benchmark/","bm1test1.json");
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
        return "TODO@ BM1 return filepath";
    }
}
