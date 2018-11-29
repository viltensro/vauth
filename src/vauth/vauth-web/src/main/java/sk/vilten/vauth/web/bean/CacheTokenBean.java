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

import java.util.ArrayList;
import java.util.List;
import sk.vilten.vauth.web.entity.Token;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;
import javax.ejb.Singleton;
import javax.inject.Named;
import javax.inject.Inject;

/**
 * cache pre tokeny a jeho role
 * @author vt
 * @version 1
 * @since 2017-03-18
 */
@Singleton
public class CacheTokenBean{
    
    private final String CACHE_NAME = "activationTokenCache";
    private final Duration CACHE_EXPIRY_DURATION = Duration.ONE_DAY;

    //@NamedCache(cacheName = "activationTokenCacheManager", managementEnabled = true)
    //@Inject
    private Cache<String,Token> tokenCache;
    
    @Inject
    CachingProvider cachingProvider;

    public CacheTokenBean() {
    }
    
    private void initCache() throws Exception {
        try 
        {
            CacheManager cacheManager = cachingProvider.getCacheManager();

            MutableConfiguration<String, Integer> config = new MutableConfiguration<>();
            config.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(CACHE_EXPIRY_DURATION));

            tokenCache = cacheManager.getCache(CACHE_NAME);
            tokenCache = tokenCache == null ? cacheManager.createCache(CACHE_NAME, (Configuration)config) : null;
        }
        catch (Exception e) 
        {
            throw new Exception("Unable to init cache " + CACHE_NAME + ", error=" + e.getLocalizedMessage());
        }
    }
    
    public Token getToken(String token) throws Exception {
        if (tokenCache==null) initCache();
        return tokenCache.get(token);
    }
    
    public void putToken(Token token) throws Exception {
        if (tokenCache==null) initCache();
        if (tokenCache.containsKey(token.getToken()))
        {
            tokenCache.replace(token.getToken(), token);
        }
        else
        {
            tokenCache.put(token.getToken(), token);
        }
    }
    
    public boolean removeToken(String token) throws Exception {
        if (tokenCache==null) initCache();
        return tokenCache.remove(token);
    }
    
    public boolean removeTokenByUsername(String username) throws Exception {
        if (tokenCache==null) initCache();
        List<String> tokensToRemove = new ArrayList<>();
        // find tokens to remove
        tokenCache.forEach(token -> {
            if (token.getValue().getUser_external_id().equals(username)) {
                tokensToRemove.add(token.getKey());
            }
        });
        
        // remove tokens
        tokensToRemove.forEach(tokenKey -> {
            tokenCache.remove(tokenKey);
        });
        
        return true;
    }
}
