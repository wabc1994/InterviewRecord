package com.mikyou.dao;

import com.mikyou.bean.Customer;
import com.mikyou.common.JDBCTemplate;
import com.mikyou.common.OnIMapperListener;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


//dao 层调用 JDBCTemplate的一个对象情况，实现真正的业务
public class CustomerDAO implements OnIMapperListener<Customer> {
    private JDBCTemplate<Customer> temp;


    public CustomerDAO() {
        temp=new JDBCTemplate<Customer>();
        temp.setOnMapperListener(this);//给CustomerDAO注册映射监听器
    }


    /**
     * 以下三个方法都有一个共同的特点就是无需带有返回结果集，所以直接在JDBCTemplate模板类中直接封装成一个update方法即可
     * */


    public void save(Customer customer) throws SQLException{
        String sql="insert into tb1_customer(name,gender,telephone,address) values(?,?,?,?)";
        Object[] agrs={customer.getName(),customer.getGender(),customer.getTelephone(),customer.getAddress()};
        temp.update(sql,agrs);
    }



    public void update(Customer customer){
        String sql="update tb1_customer set name=?,gender=?,telephone=?,address=? where id=?";
        //字段值的设计
        Object[] agrs={customer.getName(),customer.getGender(),customer.getTelephone(),customer.getAddress(),customer.getId()};
        temp.update(sql, agrs);
    }


    public void delete(long id){
        String sql = "delete from tb1_customer where id=?";
        Object [] agrs = {id};
        temp.update(sql,agrs);
    }

    public Customer findByName(String name){
        String sql = "select * from tb1_customer where name=?";
      //代表从那一列来查询数据
        Object[] args ={name};
        Customer customer = temp.queryOne(sql,args);
        return customer;

    }


    public List<Customer> findAll(){
        String sql ="select * from tb1_customer";
        Object[] args = {};
        return temp.queryAll(sql,args);

    }

/**
 * 具体实现相应的回调方法
 * @param rs 回调返回的结果集
 * @deprecated:处理结果集 并且返回的类型是我们的Bean类型Customer

 */

    @Override
    public Customer mapper(ResultSet rs) {
        Customer customer = null;
        //关系映射，监听器，rs代表sql结果集等基础知识
        try{
            Long id = rs.getLong("id");
            String name = rs.getString("name");
            String gender = rs.getString("gender");
            String address =  rs.getString("addresss");
            String telephone = rs.getString("telephone");
            //从数据库中获得原始的数据然后构造新的对象
            customer = new Customer(id, name,gender,telephone,address);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return customer;
    }
}
