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
        Assert.assertEquals(2,customers.size());
    }

    /**
     * 查询用户
     */
    @Test
    public void getCustomerTest(){
        long id = 1;
        Customer customer = customerService.getCustomer(id);
        Assert.assertNotNull(customer);
    }

    /**
     * 创建用户
     */
    public void CreateCustomerTest(){
        Customer customer = new Customer();
        customer.setId(999);
        customer.setName("customer999");
        customer.setContact("mike");
        boolean result = customerService.createCustomer(customer);
        Assert.assertTrue(result);
    }

    /**
     * 更新用户
     */
    public void updateCustomerTest(){
        long id = 999;
        Customer customer = customerService.getCustomer(id);
        customer.setContact("judy");
        customer.setEmail("judy@mail.com");
        boolean result = customerService.updateCustomer(customer);
        Assert.assertTrue(result);
    }

    /**
     * 删除用户
     */
    public void deleteCustomerTest(){
        long id = 999;
        boolean result = customerService.deleteCustomer(id);
        Assert.assertTrue(result);
    }
}
