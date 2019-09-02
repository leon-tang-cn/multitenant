package com.leon.solid.multitenant.repository;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

/**
 *    The template class for convenient approaches to use  native SQL query in JPA. It contains the common operations in native query.
 *    <p>
 *    It will inject the primary {@link EntityManager}  in spring bean container by default. 
 *    The method {@link #setEntityManager(EntityManager)} is prepared in multiple  {@link EntityManager entity managers} scene.
 *   <p>
 *   The SQL clause can be from argument or definitions in JPA {@linkplain orm.xml} 
 *   <pre>{@code
 * <named-native-query name="user.findAll">
 *   <description>query all users form tbl_user</description>
 *   <query><![CDATA[
 *      select id as "id", name as "name", age as "age" from test_user
 *      ]]></query> 
 * </named-native-query> 
 *   }</pre>
 *   Here we can only specify the value of 'name' named attribute in XML through methods {@link #namedQueryForSingle(String, Map, Class)} or {@link #namedQueryForList(String, Map, Class)}
 *   
 *   <p>
*   The input parameters can be positional or {@link Map name and value}, just like {@link JdbcTemplate}.
*   
 * @author Leon.Tang
 * @version 1.0
 * @see JdbcTemplate
 */
public abstract class JpaNativeQuerySupportRepository {

    private static final String ERR_MSG_EM_REQUIRE = "The entity manager is required!";
    /**
     *  The instance of {@link EntityManager  JPA entity manager}  
     *  which supplies many persistence operations
     */
    private EntityManager entityManager;

    /**
     *   Get not null {@link EntityManager } instance 
     *   It will be no-null checked before return. When it's null ,  a {@link IllegalArgumentException} exception will be thrown.
     * @return the no-null  {@link EntityManager} instance 
     */
    public EntityManager getEntityManager() {
        Assert.notNull(this.entityManager, ERR_MSG_EM_REQUIRE); 
        return entityManager;
    }

    /**
     *  The setter of  {@link EntityManager instance}. 
     *  It can be injected the {@link EntityManager} instance bean in Spring container when it's a Spring bean.
     *  
     * @param em the specified  {@link EntityManager instance}
     */
    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.entityManager = em;
    }

    /**
     *  Create the {@link javax.persistence.Query} instance with <code>sql</code> and parameters in {@link Map} type 
     *  
     * @param sql the SQL clause to execute
     * @param parameters the parameters in  {@link Map} type
     * @return  the {@link javax.persistence.Query} instance
     */
    protected Query createSqlQuery(String sql, Map<String, Object> parameters) {
        return createNativeQuery(sql, parameters, null);
    }

    /**
     *   Execute SQL clause and transform result list with input <code>elementType</code> row type
     *   
     * @param <T> the target model type that row mapping to 
     * @param sql the SQL clause to execute
     * @param elementType the type for row mapping to
     * @return the {@link List} of result set transformed to, which contains <code>elementType</code> row.
     */
    protected <T> List<T> queryForList(String sql, Class<T> elementType) {
        return queryForList(sql, null, elementType);
    }
 
    /**
     *   Execute SQL clause and transform result list with input <code>elementType</code> row type
     *   
     * @param <T> the target model type that row mapping to 
     * @param sql the SQL clause to execute
     * @param parameters the parameters in  {@link Map} type
     * @param elementType the type for row mapping to
     * @return the {@link List} of result set transformed to, which contains <code>elementType</code> row.
     */
    @SuppressWarnings("unchecked")
    protected <T> List<T> queryForList(String sql, Map<String, Object> parameters, Class<T> elementType) {
        return createNativeQuery(sql, parameters, elementType).getResultList();
    }

    /**
     *   Execute SQL clause and return only one row in input <code>elementType</code> row type
     *   
     * @param <T> the target model type that row mapping to 
     * @param sql the SQL clause to execute
     * @param elementType the type for row mapping to
     * @return the {@link List} of result set transformed to, which contains <code>elementType</code> row.
     */
    protected <T> T queryForSingle(String sql, Class<T> elementType) {
        return queryForSingle(sql, null, elementType);
    }

    /**
     *   Execute SQL clause and return only one row in input <code>elementType</code> row type
     *   
     * @param <T> the target model type that row mapping to 
     * @param sql the SQL clause to execute
     * @param parameters the parameters in  {@link Map} type
     * @param elementType the type for row mapping to
     * @return the {@link List} of result set transformed to, which contains <code>elementType</code> row.
     */
    protected <T> T queryForSingle(String sql, Map<String, Object> parameters, Class<T> elementType) {
        List<T> result = this.queryForList(sql, parameters, elementType);
        return getFirstRowIfPresent(result);
    }

 

    /**
     *  Find the SQL clause by input <code>queryName</code> in <code>orm.xml</code>. 
     *  And it will be executed for transforming the result set to {@link List} which contains <code>elementType</code> row..
     *  
     * @param <T> the target model type that row mapping to 
     * @param queryName the attribute name of  <code>named-native-query</code> element to search SQL clause in <code>orm.xml</code>
     * @param elementType the type for row mapping to
     * @return the {@link List} of result set transformed to, which contains <code>elementType</code> row.
     */
    protected <T> List<T> namedQueryForList(String queryName, Class<T> elementType) {
        return namedQueryForList(queryName, null, elementType);
    }

    /**
     *  Find the SQL clause by input <code>queryName</code> in <code>orm.xml</code>. 
     *  And it will be executed for transforming the result set to {@link List} which contains <code>elementType</code> row..
     *  
     * @param <T> the target model type that row mapping to 
     * @param queryName the attribute name of  <code>named-native-query</code> element to search SQL clause in <code>orm.xml</code>
     * @param parameters the parameters in  {@link Map} type
     * @param elementType the type for row mapping to
     * @return the {@link List} of result set transformed to, which contains <code>elementType</code> row.
     */
    @SuppressWarnings("unchecked")
    protected <T> List<T> namedQueryForList(String queryName, Map<String, Object> parameters, Class<T> elementType) {
        return createNamedQuery(queryName, parameters, elementType).getResultList();
    }
 
    /**
     *  Find the SQL clause by input <code>queryName</code> in <code>orm.xml</code>. 
     *  And it will be executed for transform first row of  the result set into  <code>elementType</code> row..
     *  
     * @param <T> the target model type that row mapping to 
     * @param queryName the attribute name of  <code>named-native-query</code> element to search SQL clause in <code>orm.xml</code>
     * @param parameters the parameters in  {@link Map} type
     * @param elementType the type for row mapping to
     * @return the first result if it's present, otherwise null
     */
    protected <T> T namedQueryForSingle(String sqlName, Map<String, Object> parameters, Class<T> elementType) {
        List<T> result = this.namedQueryForList(sqlName, parameters, elementType); 
        return  getFirstRowIfPresent(result);
    }
    
    //TODO: Support positional parameters.
    //TODO: Support query single column as Object(eg. Long/Integer/String ...)

   
   
    /**
     *  Create a  {@link javax.persistence.Query} instance with SQL clause and parameters in {@link Map} type. 
     *  It also binds {@link org.hibernate.transform.AliasToBeanResultTransformer.AliasToBeanResultTransformer(Class)}  for row mapping from result set.
     *  
     * @param <T> the target model type that row mapping to 
     * @param sql the SQL clause to execute
     * @param parameters the parameters in {@link Map} type
     * @param elementType the type for row mapping to
     * @return  the {@link javax.persistence.Query} instance
     */
    private <T> Query createNativeQuery(String sql, Map<String, Object> parameters, Class<T> elementType) {
        return this.createNativeQuery(sql, parameters, elementType, true);
    }
    
    /**
     *  Create a  {@link javax.persistence.Query} instance with SQL clause and parameters in {@link Map} type. 
     *  It also binds {@link org.hibernate.transform.AliasToBeanResultTransformer.AliasToBeanResultTransformer(Class)}  for row mapping from result set.
     *  
     * @param <T> the target model type that row mapping to 
     * @param sql the SQL clause to execute
     * @param parameters the parameters in {@link Map} type
     * @param elementType the type for row mapping to
     * @param isQuery true presents it's query execution at this time, otherwise false
     * @return  the {@link javax.persistence.Query} instance
     */
    @SuppressWarnings("deprecation")
    private <T> Query createNativeQuery(String sql, Map<String, Object> parameters, Class<T> elementType, boolean isQuery) {
        Query nativeQuery = this.getEntityManager().createNativeQuery(sql);
        prepareParameters(parameters, nativeQuery);
        if(isQuery) {
            nativeQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(elementType));
        }
        return nativeQuery;
    }

    /**
     *  Create the {@link javax.persistence.Query} instance with <code>sql</code> and parameters in {@link Map} type 
     *  
     *  @param <T> the target model type that row mapping to 
     * @param queryName the attribute name of  <code>named-native-query</code> element to search SQL clause in <code>orm.xml</code>
     * @param parameters the parameters in  {@link Map} type
     * @param elementType the type for row mapping to
    * @param isQuery true presents it's query execution at this time, otherwise false
     * @return  the {@link javax.persistence.Query} instance
     */
    private <T> Query createNamedQuery(String queryName, Map<String, Object> parameters, Class<T> elementType) { 
        return this.createNamedQuery(queryName, parameters, elementType, true);
    }
    
    /**
     *  Create the {@link javax.persistence.Query} instance with <code>sql</code> and parameters in {@link Map} type 
     *  
     *  @param <T> the target model type that row mapping to 
     * @param queryName the attribute name of  <code>named-native-query</code> element to search SQL clause in <code>orm.xml</code>
     * @param parameters the parameters in  {@link Map} type
     * @param elementType the type for row mapping to
     * @param isQuery true presents it's query execution at this time, otherwise false
     * @return  the {@link javax.persistence.Query} instance
     */
    @SuppressWarnings("deprecation")
    private <T> Query createNamedQuery(String queryName, Map<String, Object> parameters, Class<T> elementType, boolean isQuery) {
        Query namedQuery = this.getEntityManager().createNamedQuery(queryName);
        prepareParameters(parameters, namedQuery);
        if(isQuery) {
          namedQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(elementType));
        }
        return namedQuery;
    }

    /**
     *  Binding the parameters of <code>nativeQuery</code> 
     *  
     * @param parameters the specified {@link Map} parameters for SQL execution
     * @param nativeQuery the {@link Query} instance to do query execution
     */
    private void prepareParameters(Map<String, Object> parameters, Query nativeQuery) {
        if (parameters != null) {
            for (Entry<String, Object> entry : parameters.entrySet()) {
                nativeQuery.setParameter(entry.getKey(), entry.getValue());
            }
        }
    }
    
    /**
     *  Get the first row from <code>result</code> if it's not empty,  otherwise null.
     * @param <T> the element type of <code>result</code>
     * @param result the {@link List} type result
     * @return the first result if it's present, otherwise null
     */
    private <T> T getFirstRowIfPresent(List<T> result) {
        return (result != null && !result.isEmpty()) ? result.get(0) : null;
    }
}