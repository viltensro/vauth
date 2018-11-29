package sk.vilten.vauth.web.facade;

import sk.vilten.vauth.data.preferences.AppPreferences;
import sk.vilten.common.ConfigFile;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.apache.logging.log4j.Logger;
import org.eclipse.persistence.config.PersistenceUnitProperties;

public abstract class AbstractFacade<E, P> {

    private final Class<E> entityClass;

    public AbstractFacade(Class<E> entityClass) {
        this.entityClass = entityClass;
    }
    
    public Properties getEntityProperties() {
        Properties properties = new Properties();
        
        properties.put(PersistenceUnitProperties.JDBC_URL, ConfigFile.getString(AppPreferences.VAUTH_DB_URL_CONF_NAME, AppPreferences.VAUTH_DB_URL_DEF_VALUE));
        properties.put(PersistenceUnitProperties.JDBC_USER, ConfigFile.getString(AppPreferences.VAUTH_DB_USER_CONF_NAME, AppPreferences.VAUTH_DB_USER_DEF_VALUE));
        properties.put(PersistenceUnitProperties.JDBC_PASSWORD, ConfigFile.getString(AppPreferences.VAUTH_DB_PASS_CONF_NAME, AppPreferences.VAUTH_DB_PASS_DEF_VALUE));
        
        return properties;
    }

    protected abstract EntityManager getEntityManager();
    
    protected abstract Logger getLogger();
    
    public void flush() {
        try
        {
            getEntityManager().flush();
        }
        catch (Exception e)
        {
            getLogger().trace("Unable to flush, error={}, trace={}", e.getLocalizedMessage(), e.getStackTrace());
        }
    }

    public E create(E entity) {
        getEntityManager().persist(entity);
        try
        {
            getEntityManager().flush();
        }
        catch (Exception e)
        {
            getLogger().trace("Unable to flush create, error={}, trace={}", e.getLocalizedMessage(), e.getStackTrace());
        }
        getEntityManager().refresh(entity);
        return (E)entity;
    }

    public E edit(P key,E entity) throws Exception {
        if (find(key)==null)
            throw new Exception("Entity not found.");
        else
        {
            E tmpEntity = getEntityManager().merge(entity);
            try
            {
                getEntityManager().flush();
            }
            catch (Exception e)
            {
                getLogger().trace("Unable to flush edit, error={}, trace={}", e.getLocalizedMessage(), e.getStackTrace());
            }
            getEntityManager().refresh(tmpEntity);
            return (E)tmpEntity;
        }
    }

    public void remove(E entity) {
        getEntityManager().remove(entity);
        try
        {
            getEntityManager().flush();
        }
        catch (Exception e)
        {
            getLogger().trace("Unable to flush remove, error={}, trace={}", e.getLocalizedMessage(), e.getStackTrace());
        }
    }

    public P getIdentifier(E entity) {
        return (P) getEntityManager().getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
    }

    public E find(P id) {
        getEntityManager().getEntityManagerFactory().getCache().evict(entityClass, id);
        return (E) getEntityManager().find(entityClass, id);
    }

    public List<E> findAll() {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return (List<E>)getEntityManager().createQuery(cq).getResultList();
    }

    public List<E> findRange(int startPosition, int size) {
        return findRange(startPosition, size, null);
    }

    public List<E> findRange(int startPosition, int size, String entityGraph) {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(size);
        q.setFirstResult(startPosition);
        if (entityGraph != null) {
            q.setHint("javax.persistence.loadgraph", getEntityManager().getEntityGraph(entityGraph));
        }
        return (List<E>)q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        Root<E> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public Optional<E> findSingleByNamedQuery(String namedQueryName, Class<E> classT) {
        return (Optional<E>) findOrEmpty(() -> getEntityManager().createNamedQuery(namedQueryName, classT).getSingleResult());
    }

    public Optional<E> findSingleByNamedQuery(String namedQueryName, Map<String, Object> parameters, Class<E> classT) {
        return (Optional<E>) findSingleByNamedQuery(namedQueryName, null, parameters, classT);
    }

    public Optional<E> findSingleByNamedQuery(String namedQueryName, String entityGraph, Map<String, Object> parameters, Class<E> classT) {
        Set<Entry<String, Object>> rawParameters = parameters.entrySet();
        TypedQuery<E> query = getEntityManager().createNamedQuery(namedQueryName, classT);
        rawParameters.stream().forEach((entry) -> {
            query.setParameter(entry.getKey(), entry.getValue());
        });
        if (entityGraph != null) {
            query.setHint("javax.persistence.loadgraph", getEntityManager().getEntityGraph(entityGraph));
        }
        return (Optional<E>) findOrEmpty(() -> query.getSingleResult());
    }

    public List<E> findByNamedQuery(String namedQueryName) {
        return (List<E>)getEntityManager().createNamedQuery(namedQueryName).getResultList();
    }

    public List<E> findByNamedQuery(String namedQueryName, Map<String, Object> parameters) {
        return (List<E>)findByNamedQuery(namedQueryName, parameters, 0);
    }

    public List<E> findByNamedQuery(String queryName, int resultLimit) {
        return (List<E>)getEntityManager().createNamedQuery(queryName).setMaxResults(resultLimit).getResultList();
    }

    public List<E> findByNamedQuery(String namedQueryName, Map<String, Object> parameters, int resultLimit) {
        Set<Entry<String, Object>> rawParameters = parameters.entrySet();
        Query query = getEntityManager().createNamedQuery(namedQueryName);
        if (resultLimit > 0) {
            query.setMaxResults(resultLimit);
        }
        rawParameters.stream().forEach((entry) -> {
            query.setParameter(entry.getKey(), entry.getValue());
        });
        return (List<E>)query.getResultList();
    }

    public static <E> Optional<E> findOrEmpty(final DaoRetriever<E> retriever) {
        try {
            return (Optional<E>)Optional.of(retriever.retrieve());
        } catch (NoResultException ex) {
            //log
        }
        return (Optional<E>)Optional.empty();
    }

    @FunctionalInterface
    public interface DaoRetriever<E> {

        E retrieve() throws NoResultException;
    }

}
