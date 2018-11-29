/*
 * Copyright (C) 2017 vt
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package sk.vilten.vauth.web.responses;

import sk.vilten.vauth.data.responses.BaseResponse;
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