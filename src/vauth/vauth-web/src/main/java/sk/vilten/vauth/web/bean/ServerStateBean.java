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
package sk.vilten.vauth.web.bean;

import sk.vilten.vauth.data.models.ConcurrentPool;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Named;

/**
 * bean ktory nacitava stav servera a uklada ho
 * @author vt
 * @version 1
 * @since 2017-02-13
 */
@Singleton
public class ServerStateBean {

    private final ConcurrentPool<Long,String> statePool;
    //vytvori pool na 7 dni
    private final int poolSize = 24*60*12;

    public ConcurrentPool<Long, String> getStatePool() {
        return statePool;
    }

    public ServerStateBean() {
        this.statePool = new ConcurrentPool<>(poolSize);
    }

    @Schedule(hour = "*", minute = "*", second = "*/5", info = "timeout")
    public void timeout()
    {
        OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        statePool.put(System.currentTimeMillis(), operatingSystemMXBean.getSystemLoadAverage() + ";" + Runtime.getRuntime().totalMemory() + ";" + Runtime.getRuntime().freeMemory());
    }
}
