/*
 * Copyright (C) 2017 Vilten,s.r.o. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Viliam Tencer <vilten@vilten.sk>, 15. 10. 2017
 */
package sk.vilten.vauth.data.models;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * trieda pre concurrent pool kde je limit poloziek a mazu sa stare ak presiahne limit
 * @author vt
 * @param <T>
 * @param <S>
 */
public class ConcurrentPool<T,S> extends ConcurrentHashMap<T, S>{
    private final int limit;
    
    public ConcurrentPool(int limit) {
        super();
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }
    
    private void shrinkPool()
    {
        int deleteCount = size() - limit;
        while (deleteCount > 0)
        {
            S keyForDelete = (S)entrySet().stream().findFirst().get().getKey();
            remove(keyForDelete);
            deleteCount++;
        }
    }

    @Override
    public void putAll(Map<? extends T, ? extends S> m) {
        super.putAll(m); //To change body of generated methods, choose Tools | Templates.
        shrinkPool();
    }

    @Override
    public S putIfAbsent(T key, S value) {
        S response = super.putIfAbsent(key, value);
        shrinkPool();
        return response;
    }

    @Override
    public S put(T key, S value) {
        S response = super.put(key, value);
        shrinkPool();
        return response;
    }
}