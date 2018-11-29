/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 15. 10. 2017
 */
package sk.vilten.vauth.data.models;

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