package utils;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * Created by XR on 2015/12/16.
 */
public class JdbcUtil {
    private static final Logger logger = Logger.getLogger(JdbcUtil.class);
    private static final ThreadLocal<Connection> THREAD_LOCAL;
    private static final BasicDataSource DATA_SOURCE ;
    private static final QueryRunner QUERY_RUNNER;
    static {
        THREAD_LOCAL = new ThreadLocal<Connection>();
        QUERY_RUNNER = new QueryRunner();

        Properties props =  PropsUtil.getProps();
        String driver = PropsUtil.getString(props,"jdbc.driver");
        String url = PropsUtil.getString(props,"jdbc.url");
        String user = PropsUtil.getString(props,"jdbc.user");
        String password = PropsUtil.getString(props,"jdbc.password");

        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(driver);
        DATA_SOURCE.setUrl(url);
        DATA_SOURCE.setUsername(user);
        DATA_SOURCE.setPassword(password);
    }

    /**
     * 获取数据库连接
     */
    public Connection getConnection(){
        Connection conn = THREAD_LOCAL.get();
        if (conn == null){
            try {
                conn = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                THREAD_LOCAL.set(conn);
            }
        }
        return conn;
    }

    public <T> List<T> getEntityList(Class<T> entityClass,String sql,) {
        Connection conn = getConnection();
        List<T> entityList;
        String tableName = entityClass.getSimpleName();
        entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>());
        return entityList;
    }
}
