import model.Customer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import service.CustomerService;

import java.util.List;

/**
 * Created by XR on 2015/12/13 0013.
 * 单元测试
 */
public class CustomerServiceTest {
    private CustomerService customerService;

    public CustomerServiceTest() {
        this.customerService = new CustomerService();
    }

    @Before
    public void init(){
        //todo 初始化数据
    }

    /**
     * 查询用户列表
     */
    @Test
    public void getCustomerListTest(){
        List<Customer> customers = customerService.getCustomerList();
        System.out.println(customers);
        Assert.assertTrue(customers.size() > 0);
    }

    /**
     * 查询用户
     */
    @Test
    public void getCustomerTest(){
        long id = 2;
        Customer customer = customerService.getCustomer(id);
        Assert.assertNotNull(customer);
    }

    /**
     * 创建用户
     */
    @Test
    public void createCustomerTest(){
        long id = 999;
        Customer customer = new Customer();
        customer.setId(id);
        customer.setName("customer999");
        customer.setContact("mike");
        boolean result = customerService.createCustomer(customer);
        customerService.deleteCustomer(id);
        Assert.assertTrue(result);
    }

    /**
     * 更新用户
     */
    @Test
    public void updateCustomerTest(){
        long id = 999;
        Customer customer = createTestCustomer(id);
        customerService.createCustomer(customer);
        customer = new Customer();
        customer.setContact("judy");
        customer.setEmail("judy@mail.com");
        boolean result = customerService.updateCustomer(customer,id);
        Assert.assertTrue(result);
    }

    /**
     * 删除用户
     */
    @Test
    public void deleteCustomerTest(){
        long id = 999;
        Customer customer = createTestCustomer(id);
        customerService.createCustomer(customer);
        boolean result = customerService.deleteCustomer(id);
        Assert.assertTrue(result);
    }

    private Customer createTestCustomer(long id){
        Customer customer = new Customer();
        customer.setId(id);
        customer.setName("customer999");
        customer.setContact("mike");
        return customer;
    }
}
