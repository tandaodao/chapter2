package utils;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by XR on 2015/12/16.
 */
public class JdbcUtil {
    private static final Logger logger = Logger.getLogger(JdbcUtil.class);
    private static final ThreadLocal<Connection> THREAD_LOCAL;
    private static final BasicDataSource DATA_SOURCE;
    private static final QueryRunner QUERY_RUNNER;

    static {
        THREAD_LOCAL = new ThreadLocal<Connection>();
        QUERY_RUNNER = new QueryRunner();

        Properties props = PropsUtil.getProps();
        String driver = PropsUtil.getString(props, "jdbc.driver");
        String url = PropsUtil.getString(props, "jdbc.url");
        String user = PropsUtil.getString(props, "jdbc.user");
        String password = PropsUtil.getString(props, "jdbc.password");

        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(driver);
        DATA_SOURCE.setUrl(url);
        DATA_SOURCE.setUsername(user);
        DATA_SOURCE.setPassword(password);
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection() {
        Connection conn = THREAD_LOCAL.get();
        if (conn == null) {
            try {
                conn = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                THREAD_LOCAL.set(conn);
            }
        }
        return conn;
    }

    /**
     * 获取对象列表
     * @param entityClass
     * @param filterSql
     * @param <T>
     * @return
     */
    public static <T> List<T> getEntityList(Class<T> entityClass, String filterSql) {
        List<T> entityList = null;
        String tableName = getTableName(entityClass);
        String sql = "select * from " + tableName + " " + filterSql;
        try {
            Connection conn = getConnection();
            entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entityClass));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entityList;
    }

    /**
     * 获取单个对象
     * @param entityClass
     * @param id
     * @param <T>
     * @return
     */
    public static <T> T getEntity(Class<T> entityClass, long id) {
        T entity = null;
        String tableName = getTableName(entityClass);
        String sql = "select * from " + tableName + " where id=" + id;
        try {
            Connection conn = getConnection();
            entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<T>(entityClass));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    /**
     * 创建一条数据
     * @param entityClass //对象的类型
     * @param fieldMap //对象的属性Map
     * @param <T>
     * @return
     */
    public static <T> boolean createEntity(Class<T> entityClass, Map<String,Object> fieldMap) {
        Connection conn = getConnection();
        String sql = "insert into " + getTableName(entityClass) + " ";

        /*Sql拼凑开始*/
        StringBuffer columns = new StringBuffer("(");
        StringBuffer values = new StringBuffer("(");
        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName + ", ");
            values.append("?, ");
        }
        columns.replace(columns.lastIndexOf(", "), columns.length(), ")");
        values.replace(values.lastIndexOf(", "), values.length(), ")");
        sql += columns + " values " + values;
        /*sql拼凑结束*/

        Object[] params = fieldMap.values().toArray();
        return executeUpdate(sql,params) == 1;
    }

    /**
     * 通过ID更新一条数据
     * @param entityClass
     * @param fieldMap
     * @return
     */
    public static <T> boolean updateEntity(Class<T> entityClass, Map<String, Object> fieldMap,long id) {
        Connection conn = getConnection();
        StringBuilder sql = new StringBuilder("update "+getTableName(entityClass)+" set");
        for (String fieldName : fieldMap.keySet()){
            sql.append(" "+fieldName+"=?,");
        }
        sql.replace(sql.lastIndexOf(","), sql.length(), " where id=" + id);
        return executeUpdate(sql.toString(),fieldMap.values().toArray()) == 1;
    }

    /**
     * 通过ID删除一条数据
     * @param entityClass
     * @param id
     * @param <T>
     * @return
     */
    public static <T> boolean removeEntity(Class<T> entityClass, long id) {
        StringBuilder sql = new StringBuilder("delete from ");
        sql.append(getTableName(entityClass)).append(" where id = ?");
        return executeUpdate(sql.toString(),id) == 1;
    }

    /**
     * 执行更新操作，如增、删、改，供内部方法调用
     * @param sql
     * @param params
     * @return
     */
    private static int executeUpdate(String sql,Object... params) {
        Connection conn = getConnection();
        int row = 0;
        try {
                row = QUERY_RUNNER.update(conn, sql,params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
    }

    /**
     * 获得反射里的一个field的值，原理是通过其get方法来获取
     * @param field
     * @param entity
     * @param <T>
     * @return
     */
    private static <T> String getFieldValue(Field field, T entity) {
        String fieldValue = "";
        try {
            Method getMethod = entity.getClass().getMethod("get" + firstCharToUpCase(field.getName()));
            try {
                fieldValue = String.valueOf(getMethod.invoke(entity));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return fieldValue;
    }

    /**
     * 转换成首字母大写
     * @param name
     * @return
     */
    private static String firstCharToUpCase(String name) {
        return new StringBuilder().append(Character.toUpperCase(name.charAt(0))).append(name.substring(1)).toString();
    }

    /**
     * 获得表名(Entity类名)
     * @param entityClass
     * @param <T>
     * @return
     */
    private static <T> String getTableName(Class<T> entityClass) {
        return entityClass.getSimpleName();
    }



}
