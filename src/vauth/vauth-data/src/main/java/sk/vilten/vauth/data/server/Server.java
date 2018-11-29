/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 15. 10. 2017
 */
package sk.vilten.vauth.data.server;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import sk.vilten.vauth.data.models.BaseModel;

/**
 * trieda info o serveri
 * @author vt
 * @version 1
 * @since 2017-01-30
 */
@XmlRootElement
public class Server extends BaseModel implements Serializable{
    private Long maxMemory;
    private Long freeMemory;
    private Integer availCPU;
    private Double systemLoadAverage;
    private String cpuArchitecture;

    public Long getMaxMemory() {
        return maxMemory;
    }

    public void setMaxMemory(Long maxMemory) {
        this.maxMemory = maxMemory;
    }

    public Long getFreeMemory() {
        return freeMemory;
    }

    public void setFreeMemory(Long freeMemory) {
        this.freeMemory = freeMemory;
    }

    public Integer getAvailCPU() {
        return availCPU;
    }

    public void setAvailCPU(Integer availCPU) {
        this.availCPU = availCPU;
    }

    public Double getSystemLoadAverage() {
        return systemLoadAverage;
    }

    public void setSystemLoadAverage(Double systemLoadAverage) {
        this.systemLoadAverage = systemLoadAverage;
    }

    public String getCpuArchitecture() {
        return cpuArchitecture;
    }

    public void setCpuArchitecture(String cpuArchitecture) {
        this.cpuArchitecture = cpuArchitecture;
    }

    public Server() {
    }
}
