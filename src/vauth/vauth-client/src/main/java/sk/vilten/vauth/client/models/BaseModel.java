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
package sk.vilten.vauth.client.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Viliam
 * @version 2016-04-16
 * zakladny model z ktoreho sa odvodzuju ostatne modely
 */
@XmlRootElement
public class BaseModel implements Serializable
{
	
        /**
         * constructor
         */
        public BaseModel()
	{}

	/**
	 * vytvori z objektu string json
	 * @return
	 */
        @Override
	public String toString()
	{
		try
		{
			return this.toJson();
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	/**
	 * vytvori z objektu json
	 * @return
	 */
	public String toJson()
	{
		try
		{
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").setPrettyPrinting().serializeNulls().create();
			return gson.toJson(this);
		}
		catch (Exception e)
		{
			return null;
		}
	}
}