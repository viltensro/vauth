/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 15. 10. 2017
 */
package sk.vilten.vauth.data.responses;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import javax.xml.bind.annotation.XmlRootElement;
import sk.vilten.vauth.data.server.Server;

/**
 * odpoved pre server
 * @author vt
 * @version 1
 * @since 2017-01-30
 */
@XmlRootElement
public class ServerResponse extends BaseResponse{
    private Server server;

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public ServerResponse(Server server) {
        super();
        this.server = server;
    }

    public ServerResponse() {
        super();
        server = new Server();
        server.setMaxMemory(Runtime.getRuntime().totalMemory());
        server.setAvailCPU(Runtime.getRuntime().availableProcessors());
        server.setFreeMemory(Runtime.getRuntime().freeMemory());
        OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        server.setSystemLoadAverage(operatingSystemMXBean.getSystemLoadAverage());
        server.setCpuArchitecture(operatingSystemMXBean.getArch());
        
    }
    
}
