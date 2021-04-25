package cn.rypacker.productkeymanager.services;

import javax.servlet.http.HttpServletRequest;

public interface ClientIpService {
    String getClientIp(HttpServletRequest request);
}
