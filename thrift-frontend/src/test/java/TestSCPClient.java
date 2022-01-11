import com.freeb.Entity.AccountsInfo;
import com.freeb.Enum.IdType;
import com.mysql.cj.util.StringUtils;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.io.*;

import ch.ethz.ssh2.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TestSCPClient {


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
                System.out.println("算法主机连接失败");
                return false;
            }

            //执行命令
            session = conn.openSession();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
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
        System.out.println("in copyFile");

        boolean bool = false;
        try {
            if (StringUtils.isNullOrEmpty(sourceFile) || StringUtils.isNullOrEmpty(targetFile)){
                return bool;
            }
            System.out.println("pre 1");

            // //执行命令并打印执行结果
            // session.execCommand("df -h");
            // logger.info("pre 2");

            // InputStream staout = new StreamGobbler(session.getStdout());
            // logger.info("pre 3");

            // BufferedReader br = new BufferedReader(new InputStreamReader(staout));
            // String line = null;
            // while ((line = br.readLine()) != null){
            //     System.out.println(line);
            // }
            // br.close();
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
            // e.printStackTrace();
            System.out.println("e.getMsg()="+e.getMessage());
            System.out.println("保存失败：" + sourceFile);
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

    public String CompareResEfficiencyBM1(String remoteFilePath,Integer testType) {
        System.out.println("CompareResEfficiencyBM1/Accounts");
        List<Long> results;
        long bengintime = System.nanoTime();
        Integer totalWorkLoad=0,threadNum = 1;
        // 1.
        boolean conn = preConnection(IP,PORT,USER_Name,PASSWORD);
        if(!conn){
            System.out.println("unable to connect");
            return "";
        }
        boolean fecth = copyFile("/home/benchmark/bm/bm1test1.json","./","bm1test1.json");
        if(!fecth){
            System.out.println("unable to download file");
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
        return "TODO@ BM1 return filepath";
    }

    @Test
    public void TestSCPGet(){
        CompareResEfficiencyBM1("1",2);
    }

}
