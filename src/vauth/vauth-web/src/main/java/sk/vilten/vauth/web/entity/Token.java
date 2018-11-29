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
package sk.vilten.vauth.web.entity;

import sk.vilten.vauth.data.models.BaseModel;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * entity pre token oauth2
 * @author vt
 * @version 1
 * @since 2017-03-19
 */
/**
 * @apiDefine tokenParam
 * @apiParam (token) {String} token token hash
 * @apiParam (token) {String} expirationToken expiration token hash
 * @apiParam (token) {Date} created date of creation entity
 * @apiParam (token) {Date} expire_in date of entity expiration
 * @apiParam (token) {String[]} roles list of roles external id
 * @apiParam (token) {Long} user_id id of vauth user
 * @apiParam (token) {String} user_external_id external id of vauth user
 * @apiParam (token) {String} ip ip address of client
 * @apiParam (token) {String} userAgent user agent of client
 * @apiParamExample {json} token:
 *      {
 *          "token": "c8210a4a0d10dbb1f3160cb61b188923",
 *          "expirationToken": "4235e9b95904c39c7f06a75ed0de700c",
 *          "created": "2017-01-01 12:39:00.000",
 *          "expire_in": "2017-01-01 12:39:00.000",
 *          "roles":
 *              [
 *                  "vauth_admin",
 *                  "vauth_user_write"
 *              ],
 *          "user_id" : 12,
 *          "user_external_id": "manager",
 *          "ip": "192.168.1.12",
 *          "userAgent": "VauthClient v1.1.0"
 *      }
 */
@XmlRootElement
public class Token extends BaseModel implements Serializable {
    private String token;
    private String expiration_token;
    private Date created;
    private Date expire_in;
    private List<String> roles;
    private Long user_id;
    private String user_external_id;
    private String ip;
    private String user_agent;

    public Token(String token, String expiration_token, Date expire_in, List<String> roles, Long user_id, String user_external_id, String ip, String user_agent) {
        super();
        this.token = token;
        this.created = new Date();
        this.expiration_token = expiration_token;
        this.expire_in = expire_in;
        this.roles = roles;
        this.user_id = user_id;
        this.user_external_id = user_external_id;
        this.ip = ip;
        this.user_agent = user_agent;
    }

    public Token() {
        super();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUser_agent() {
        return user_agent;
    }

    public void setUser_agent(String user_agent) {
        this.user_agent = user_agent;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getUser_external_id() {
        return user_external_id;
    }

    public void setUser_external_id(String user_external_id) {
        this.user_external_id = user_external_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpiration_token() {
        return expiration_token;
    }

    public void setExpiration_token(String expiration_token) {
        this.expiration_token = expiration_token;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getExpire_in() {
        return expire_in;
    }

    public void setExpire_in(Date expire_in) {
        this.expire_in = expire_in;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
