package com.freeb.Utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class IPUtil {
    public static Boolean IS_DBG = Boolean.FALSE;

    private static final Pattern IPV4_PATTERN = Pattern.compile("^(([1-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){1}(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){2}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
    public static boolean isIPv4Address(String input) {
        return IPV4_PATTERN.matcher(input).matches();
    }

    public static String getIPAddress() {
        try {
            if(IS_DBG){
                List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface intf : interfaces) {
                    List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                    for (InetAddress addr : addrs) {
                        if (!addr.isLoopbackAddress()) {
                            String sAddr = addr.getHostAddress();
                            if(isIPv4Address(sAddr)){
                                System.out.println(intf+"addrs:"+sAddr);
                            }

                        }
                    }
                }
            }

            NetworkInterface itf = NetworkInterface.getByName("eth0");
            if(itf!=null){
                List<InetAddress> addrs = Collections.list(itf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        if(isIPv4Address(sAddr)){
                            System.out.println(itf+"addrs:"+sAddr);
                            return sAddr;
                        }

                    }
                }
            }else {
                System.out.println("IPUtil is null");
            }

        } catch (Exception ignored) { } // for now eat exceptions
        return "";
    }
}
