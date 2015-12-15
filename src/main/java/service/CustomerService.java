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

            while (rs.next()){
                Customer customer = new Customer();
                Class customerClass = Customer.class;
                Field[] fields =  customerClass.getFields();
                for (Field field : fields){
                    String fieldName  = field.getName();
                    fieldName = fieldName.substring(0,1).toUpperCase()+fieldName.substring(1).toLowerCase();

                    Method method = null;
                    Method rsmethod = null;
                    try {
                        method = customerClass.getMethod("set"+fieldName,long.class);
                        rsmethod = rs.getClass().getMethod("get"+field.getGenericType());
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }

                    try {
                        method.invoke(customer,rsmethod.invoke(rs,field.getName()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
//                customer.setId(rs.getLong("id"));
//                customer.setName(rs.getString("name"));
//                customer.setContact(rs.getString("contact"));
//                customer.setEmail(rs.getString("email"));
//                customer.setTelePhone(rs.getString("telePhone"));
//                customer.setRemark(rs.getString("remark"));
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
