/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 05. 06. 2017
 */
package sk.vilten.vauth.client.responses;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * response pre history servera
 * @author vt
 * @version 1
 * @since 2017-02-13
 */
@XmlRootElement
public class ServerHistoryResponse extends BaseResponse {
    private List<String> cpuLoad;

    public List<String> getCpuLoad() {
        return cpuLoad;
    }

    public void setCpuLoad(List<String> cpuLoad) {
        this.cpuLoad = cpuLoad;
    }

    public ServerHistoryResponse() {
        cpuLoad = new ArrayList<>();
    }
}