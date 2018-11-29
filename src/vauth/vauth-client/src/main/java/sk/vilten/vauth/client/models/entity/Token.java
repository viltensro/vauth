/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 05. 06. 2017
 */
package sk.vilten.vauth.client.models.entity;

import sk.vilten.vauth.client.models.BaseModel;
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
