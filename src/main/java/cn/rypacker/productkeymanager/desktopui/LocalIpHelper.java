package cn.rypacker.productkeymanager.desktopui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Component
public class LocalIpHelper {

    private static Logger logger = LoggerFactory.getLogger(LocalIpHelper.class);

    public static List<String> getIpList(String port){
        try {
            logger.info("retrieving local ip addresses");
            InetAddress localhost = InetAddress.getLocalHost();
            InetAddress[] allMyIps = InetAddress.getAllByName(localhost.getCanonicalHostName());
            var ipV4Pattern = "\\d+\\.\\d+\\.\\d+\\.\\d+";
            List<String> list = new ArrayList<>();
            if (allMyIps != null && allMyIps.length > 1) {
                for (InetAddress hostNameAndIp : allMyIps) {
                    var ip = hostNameAndIp.getHostAddress();
                    if (ip.matches(ipV4Pattern)) {
                        list.add(String.format("http://%s:%s", ip, port));
                    }
                }
            }
            return list;
        } catch (UnknownHostException e) {
            logger.info(" (error retrieving server host name)");
            return new ArrayList<>();
        }
    }
}
