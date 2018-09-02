package com.mikyou.test;

import com.mikyou.bean.Customer;
import com.mikyou.dao.CustomerDAO;

import java.sql.SQLException;
import java.util.List;

public class DaoTest {
    /**
     测试类，在这里实现的业务逻辑类似与service 等情况，service层的接口代码跟dao层是一样的情况
     */
    private  CustomerDAO customerDAO;

    public DaoTest() {
        this.customerDAO =new CustomerDAO();
    }

    private void findAll(){
        //service层调用 customerDAO对象即可，完成对数据库的操作过程等情况
        List<Customer> customers = customerDAO.findAll();
        for(Customer customer:customers){
            System.out.println(customer.toString());
        }
    }

    private void findOne()
    {
        List<Customer> customers=customerDAO.findAll();
        for (Customer customer : customers) {
            System.out.println(customer.toString());
        }


    }
    private void add() {
        try {
            Customer customer=new Customer(1L, "Mikyou", "男", "123456789", "中国");
            customerDAO.save(customer);
            Customer customer2=new Customer(2L, "Alice", "女", "123456789", "美国");
            customerDAO.save(customer2);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
