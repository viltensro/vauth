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

import javax.ejb.Singleton;
import javax.inject.Named;
import sk.vilten.common.ConfigFile;
import sk.vilten.common.Strings;
import sk.vilten.vauth.data.preferences.AppPreferences;

/**
 * bean na spravu stringsov
 * @author vt
 * @version 1
 * @since 2017-05-11
 */
@Singleton(name = "VauthStringsBeanSingleton")
@Named("VauthStringsBean")
public class StringsBean {
    private final Strings strings;

    public Strings getStrings() {
        return strings;
    }

    public StringsBean() throws Exception {
        this.strings = new Strings(
                ConfigFile.getString(AppPreferences.DEFAULT_LANGUAGE_CONF_NAME, AppPreferences.DEFAULT_LANGUAGE_DEF_VALUE),
                ConfigFile.getString(AppPreferences.STRINGS_PATH_CONF_NAME, AppPreferences.STRINGS_PATH_DEF_VALUE)
        );
    }
}
