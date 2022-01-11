import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class launchBM1{


    // TODO 查找命令行
    private final static String jvmOps = "java -server -Xmx1g -Xms1g -XX:MaxDirectMemorySize=1g -XX:+UseG1GC";

    private final static File resultFolder = new File("launchBM1-result");


    private final static List<String> funOrder = Arrays.asList("LaunchBM1");

    Item emptyItem = new Item(null, Typ.Thrpt, null, 0D);


    public static void main(String[] args) throws Exception {
        installSearchService();

        var allTasks = getAllTasks()
                .filter(t -> !t.startsWith("jupiter"))
                //.filter(t -> t.compareTo("jupiter") > 0)
                .collect(Collectors.toList());

        System.out.println("找到以下benchmark项目:");
        System.out.println(allTasks);
        // //TODO Notice Here
        allTasks.forEach(launchBM1::benchmark);

        // report();
    }

    private static void installSearchService() throws Exception {
        //TODO 看一下 proj_bm 要不要 install
        exec("mvn clean install");
        // exec("ecom-common","mvn clean install");
        // exec("ecom-search", "mvn clean install");
        // exec("ecom-accounts","mvn clean install");
    }

    private static Stream<String> getAllTasks() {
        var folder = new File(".");

        return Stream.of(folder.list())
                .filter(name -> (name.endsWith("-frontend")))
                .map(name -> name.substring("thrift-".length()))
                .sorted();
    }

    private static void benchmark(String taskName) {
        try {
            System.out.println("<benchmark> [taskName]:"+taskName);
            var searchServerPackage = packageAndGet(new File("thrift-search"));
            var accountsServerPackage = packageAndGet(new File("thrift-accounts"));
            var frontendPackage = packageAndGet(new File("thrift-frontend"));
            startServer(searchServerPackage,"bm-search-server");
            System.out.println("<benchmark server> [bm-search-server]");

            startServer(accountsServerPackage,"bm-accounts-server");
            System.out.println("<benchmark server> [bm-accounts-server]");

//            startLocalServer(accountsServerPackage);
            //等服务器启动起来在启动客户端
            TimeUnit.SECONDS.sleep(10);

            startClient(frontendPackage);

            stopServer(accountsServerPackage,"bm-accounts-server");
            stopServer(searchServerPackage,"bm-search-server");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static File packageAndGet(File project) throws Exception {
        exec(project, "mvn clean package", null);

        var childList = new File(project, "target").listFiles();

        for(File f:childList){
            System.out.println("<packageAndGet> [f]:"+f.getName());
        }
        var opt = Stream.of(childList)
                .filter(f -> f.getName().endsWith("-jar-with-dependencies.jar"))
                .findFirst();

        if (opt.isPresent()) {
            return opt.get();
        }

        return Stream.of(childList)
                .filter(f -> !f.getName().startsWith("original-"))
                .filter(f -> f.getName().endsWith(".jar"))
                .findFirst()
                .get();
    }
    private static String taskName(File pkg) {
        var name = pkg.getName();

        if (name.endsWith("-jar-with-dependencies.jar")) {
            return name.substring(0, name.length() - "-jar-with-dependencies.jar".length());
        } else {
            return name.substring(0, name.length() - ".jar".length());
        }
    }
    private static void startLocalServer(File serverPackage) throws Exception{
        var name = serverPackage.getName();
        System.out.println("start server:" + name);

        var resultFile = new File(resultFolder, taskName(serverPackage) + ".log");

        var command = jvmOps + " -jar " + name;
        System.out.println("[server result file] "+resultFile.getPath());
        //启动客户端
        exec(serverPackage.getParentFile(), command, resultFile);
    }

    private static void startServer(File serverPackage,String hostname) throws Exception {
        var name = serverPackage.getName();
        System.out.printf("[start server] %s\r\n", name);

        var resultPath = taskName(serverPackage) + ".log";

        //copy到benchmark-server
        exec(serverPackage.getParentFile(), "scp " + name + " benchmark@"+hostname+":~", null);

        if (name.contains("servicecomb")) {
            //杀掉benchmark-server上的老servicecomb-service-center进程
            exec("ssh", "benchmark@"+hostname, "killall service-center");

            //benchmark-server上启动servicecomb-service-center服务
            var downloadCommand = "wget https://mirrors.tuna.tsinghua.edu.cn/apache/servicecomb/servicecomb-service-center/1.1.0/apache-servicecomb-service-center-1.1.0-linux-amd64.tar.gz";
            var unzipCommand = "tar xvf apache-servicecomb-service-center-1.1.0-linux-amd64.tar.gz";
            var confCommand = "sed -i \"s/127.0.0.1/"+hostname+"/g\" ~/apache-servicecomb-service-center-1.1.0-linux-amd64/conf/app.conf";
            var runCommand = "bash apache-servicecomb-service-center-1.1.0-linux-amd64/start-service-center.sh";

            exec("ssh", "benchmark@"+hostname, downloadCommand);
            exec("ssh", "benchmark@"+hostname, unzipCommand);
            exec("ssh", "benchmark@"+hostname, confCommand);
            exec("ssh", "benchmark@"+hostname, runCommand);
        }

        //杀掉benchmark-server上的老进程
        exec("ssh", "benchmark@"+hostname, "killall java");

        //benchmark-server上启动服务器
        var remoteCommand = String.format("nohup %s -jar %s >> %s &", jvmOps, name, resultPath);
        exec("ssh", "benchmark@"+hostname, remoteCommand);
    }

    private static void stopServer(File serverPackage,String hostname) throws Exception {
        var name = serverPackage.getName();
        System.out.println("stop " + name);

        //benchmark-server上启动服务器
        exec("ssh", "benchmark@"+hostname, "killall java");

        if (name.contains("servicecomb")) {
            //杀掉benchmark-server上的老servicecomb-service-center进程
            exec("ssh", "benchmark@"+hostname, "killall service-center");
        }
    }


    private static void startClient(File clientPackage) throws Exception {
        var name = clientPackage.getName();
        System.out.println("start " + name);

        var resultFile = new File(resultFolder, taskName(clientPackage) + ".log");

        var command = jvmOps + " -jar " + name;
        System.out.println("[client result file] "+resultFile.getPath());
        //启动客户端
        exec(clientPackage.getParentFile(), command, resultFile);
    }

    private static void exec(String path, String command) throws Exception {
        if (path != null) {
            exec(new File(path), command, null);
        } else {
            exec(command);
        }
    }

    private static void exec(String command) throws Exception {
        exec((File) null, command, null);
    }

    private static void exec(File file, String command, File redirect) throws Exception {
        java.lang.Process process;
        if(file !=null){

            process = Runtime.getRuntime().exec(command, null, file);
            System.out.println("[file]:"+file.getName()+"[command]:"+command);
        }else{
            process = Runtime.getRuntime().exec(command);
            System.out.println("[file]: Null [command]:"+command);
        }


        if (redirect != null && !redirect.exists()) {
            redirect.getParentFile().mkdirs();
        }

        try (var inputStream = process.getInputStream();
             var inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             var reader = new BufferedReader(inputStreamReader);
             var output = redirect != null ? new FileOutputStream(redirect) : null;) {

            var line = "";
            while ((line = reader.readLine()) != null) {
                System.out.println(line);

                if (output != null) {
                    output.write(line.getBytes("UTF-8"));
                    output.write("\r\n".getBytes("UTF-8"));
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        process.waitFor();
        process.destroy();
    }

    private static void exec(String... commands) throws Exception {
        System.out.println(Arrays.toString(commands));
        var process = Runtime.getRuntime().exec(commands);

        try (var inputStream = process.getInputStream();
             var inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             var reader = new BufferedReader(inputStreamReader);) {

            var line = "";
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        process.waitFor();
        process.destroy();
    }

    public static void report() throws Exception {
        var reportFile = new File(resultFolder, "benchmark-report.md");
//        var reportWriter = new FileWriter(reportFile);
        BufferedWriter reportWriter = new BufferedWriter (new OutputStreamWriter (new FileOutputStream (reportFile,true),"UTF-8"));

        var props = System.getProperties();

        reportWriter.write("# RPC性能报告\r\n");

        reportWriter.write("> 生成时间: " + LocalDateTime.now() + "<br>\r\n");
        reportWriter.write("> 运行环境: " + props.getProperty("os.name") + ", " + props.getProperty("java.vm.name") + " " + props.getProperty("java.runtime.version") + "<br>\r\n");
        reportWriter.write("> 启动参数: " + jvmOps + "<br>\r\n");

        reportWriter.write("\r\n");

        var header = "| framework | thrpt (ops/ms) | avgt (ms) | p90 (ms) | p99 (ms) | p999 (ms) |\r\n"
                + "|:--- |:---:|:---:|:---:|:---:|:---:|\r\n";

        Stream.of(resultFolder.listFiles())
                .filter(f -> f.getName().endsWith(".log"))
                .filter(f -> f.getName().contains("-client-"))
                .flatMap(resultFile -> {
                    var name = resultFile.getName();
                    var index = name.indexOf("-client-");
                    var task = name.substring(0, index);

                    return uncheck(() -> Files
                            .lines(resultFile.toPath(), StandardCharsets.UTF_8)
                            .filter(line -> line.startsWith("Client."))
                            .map(line -> extract(task, line))
                            .filter(item -> item != null));
                })
                .collect(Collectors.groupingBy(i -> i.fun))
                .entrySet()
                .stream()
                .sorted(Comparator.comparingInt(kv -> funOrder.indexOf(kv.getKey())))
                .forEach(kv -> {
                    var fun = kv.getKey();
                    var items = kv.getValue();
                    var records = toRecords(items);

                    uncheck(() -> reportWriter.write("\r\n## " + fun + "\r\n"));
                    uncheck(() -> reportWriter.write(header));

                    records.forEach(record -> {
                        var line = Stream
                                .of(record.task, record.thrpt, record.avgt, record.p90, record.p99, record.p999)
                                .map(Objects::toString)
                                .collect(Collectors.joining("|", "|", "|\r\n"));

                        uncheck(() -> reportWriter.write(line));
                    });

                    uncheck(() -> reportWriter.write("\r\n"));
                });

        reportWriter.flush();
        reportWriter.close();

        System.out.println("成功生成性能报告: " + reportFile.getAbsolutePath());
    }

    private static List<Record> toRecords(List<Item> items) {
        return items
                .stream()
                .collect(Collectors.groupingBy(item -> item.task))
                .values()
                .stream()
                .map(list -> {
                    var task = list.get(0).task;

                    var thrpt = getScore(list, Typ.Thrpt);
                    var avgt = getScore(list, Typ.Avgt);
                    var p90 = getScore(list, Typ.P90);
                    var p99 = getScore(list, Typ.P99);
                    var p999 = getScore(list, Typ.P999);

                    return new Record(task, thrpt, avgt, p90, p99, p999);
                })
                .sorted(Comparator.comparingDouble(r -> -r.thrpt))
                .collect(Collectors.toList());
    }

    private static double getScore(List<Item> items, Typ typ) {
        return items.stream().filter(i -> i.typ == typ).findFirst().get().score;
    }

    private static Item extract(String task, String line) {
        if (line == null || line.length() == 0) {
            return null;
        }

        if (line.contains(" thrpt ")) {
            var array = line.split("\\s+");

            var fun = array[0].replace("Client.", "");
            var score = Double.parseDouble(array[3]);

            return new Item(task, Typ.Thrpt, fun, score);
        }

        if (line.contains(" avgt ")) {
            var array = line.split("\\s+");

            var fun = array[0].replace("Client.", "");
            var score = Double.parseDouble(array[3]);

            return new Item(task, Typ.Avgt, fun, score);
        }

        if (line.contains("·p0.90 ")) {
            var array = line.split("\\s+");

            var fun = array[0];
            var begin = fun.indexOf(':') + 1;
            var end = fun.indexOf('·');
            fun = fun.substring(begin, end);

            var score = Double.parseDouble(array[2]);

            return new Item(task, Typ.P90, fun, score);
        }

        if (line.contains("·p0.99 ")) {
            var array = line.split("\\s+");

            var fun = array[0];
            var begin = fun.indexOf(':') + 1;
            var end = fun.indexOf('·');
            fun = fun.substring(begin, end);

            var score = Double.parseDouble(array[2]);

            return new Item(task, Typ.P99, fun, score);
        }

        if (line.contains("·p0.999 ")) {
            var array = line.split("\\s+");

            var fun = array[0];
            var begin = fun.indexOf(':') + 1;
            var end = fun.indexOf('·');
            fun = fun.substring(begin, end);

            var score = Double.parseDouble(array[2]);

            return new Item(task, Typ.P999, fun, score);
        }

        return null;
    }

    private static <T> T uncheck(UncheckedSupplier<T> fun) {
        try {
            return fun.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void uncheck(UncheckedFunction fun) {
        try {
            fun.apply();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FunctionalInterface
    private static interface UncheckedSupplier<T> {
        T get() throws Exception;
    }

    @FunctionalInterface
    private static interface UncheckedFunction {
        void apply() throws Exception;
    }
}

enum Typ {
    Thrpt("Thrpt"), Avgt("Avgt"), P90("P90"), P99("P99"), P999("P999");

    public final String name;

    private Typ(String name) {
        this.name = name;
    }
}


class Item {
    public final String task;
    public final Typ typ;
    public final String fun;
    public final double score;

    public Item(String task, Typ typ, String fun, double score) {
        this.task = task;
        this.typ = typ;
        this.fun = fun;
        this.score = score;
    }
}

class Record {
    public final String task;
    public final double thrpt;
    public final double avgt;
    public final double p90;
    public final double p99;
    public final double p999;

    public Record(String task, double thrpt, double avgt, double p90, double p99, double p999) {
        this.task = task;
        this.thrpt = thrpt;
        this.avgt = avgt;
        this.p90 = p90;
        this.p99 = p99;
        this.p999 = p999;
    }
}
