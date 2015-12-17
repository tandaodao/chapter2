package service;

import model.Customer;
import org.apache.log4j.Logger;
import utils.JavaBeanUtil;
import utils.JdbcUtil;

import java.util.List;
import java.util.Map;

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
        List<Customer> users =  JdbcUtil.getEntityList(Customer.class, "");
        return users;
    }

    /**
     * 取得客户信息
     */
    public Customer getCustomer(long id){
        Customer user = JdbcUtil.getEntity(Customer.class,id);
        return user;
    }

    /**
     * 创建客户
     */
    public boolean createCustomer(Customer user) {
        Map<String,Object> fieldMap = JavaBeanUtil.obj2Map(user);
        boolean result = JdbcUtil.createEntity(Customer.class,fieldMap);
        return result;
    }

    /**
     * 更新客户信息
     */
    public boolean updateCustomer(Customer user, long id){
        Map<String ,Object> fieldMap = JavaBeanUtil.obj2Map(user);
        boolean result = JdbcUtil.updateEntity(Customer.class,fieldMap,id);
        return result;
    }

    /**
     * 删除客户
     */
    public boolean deleteCustomer(long id){
        return JdbcUtil.removeEntity(Customer.class,id);
    }
}
