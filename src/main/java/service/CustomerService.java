package service;

import model.Customer;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XR on 2015/12/13 0013.
 * 提供客户数据服务
 */
public class CustomerService {
    private static final Logger logger = Logger.getLogger(CustomerService.class);
    /**
     * 获取客户列表(带参)
     */
    public List<Customer> getCustomerList(String keyWord){
        //todo
        return null;
    }

    /**
     * 获取客户列表(无参)
     */
    public List<Customer> getCustomerList(){
        //todo
        List<Customer> users = new ArrayList<Customer>();
        Connection connection = null;
        Statement stam = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
        try {
            String url = "jdbc:mysql://localhost:3306/chapter2?useUnicode=true&characterEncoding=utf8";
            String user = "root";
            String password = "root";
            String sql = "SELECT * FROM customer";
            connection =  DriverManager.getConnection(url, user, password);
            stam =  connection.createStatement();
            rs = stam.executeQuery(sql);

            Class customerClass = Customer.class;
            Field[] fields =  customerClass.getDeclaredFields();
            while (rs.next()){
                Customer customer = new Customer();
                for (Field field : fields){
                    String fieldName  = field.getName();
                    String fieldType = field.getType().getSimpleName();
                    fieldName = fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
                    fieldType = fieldType.substring(0,1).toUpperCase()+fieldType.substring(1);

                    Method method = null;
                    Method rsmethod = null;
                    try {
                        method = customerClass.getMethod("set"+fieldName,field.getType());
                        rsmethod = rs.getClass().getMethod("get"+fieldType,String.class);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }

                    try {
                        Object param =  rsmethod.invoke(rs, field.getName());//rs.getxxx("xx")
                        method.invoke(customer,param);//customer.setxxx(param)
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                users.add(customer);
            }

        } catch (SQLException e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }finally {
            try {
                rs.close();
                stam.close();
                connection.close();
            } catch (SQLException e) {
                logger.info(e.getMessage());
                e.printStackTrace();
            }
        }

        return users;
    }

    /**
     * 取得客户信息
     */
    public Customer getCustomer(long id){
        //todo
        return null;
    }

    /**
     * 创建客户
     */
    public boolean createCustomer(Customer user){
        //todo
        return false;
    }

    /**
     * 更新客户信息
     */
    public boolean updateCustomer(Customer user){
        //todo
        return false;
    }

    /**
     * 删除客户
     */
    public boolean deleteCustomer(long id){
        //todo
        return false;
    }
}
