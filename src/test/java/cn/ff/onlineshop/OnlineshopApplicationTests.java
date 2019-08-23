package cn.ff.onlineshop;

import cn.ff.onlineshop.jpa.entities.*;
import cn.ff.onlineshop.jpa.reposity.OrderItemRepository;
import cn.ff.onlineshop.jpa.reposity.OrderedRepository;
import cn.ff.onlineshop.jpa.reposity.ProductRepository;
import cn.ff.onlineshop.jpa.reposity.UserRepository;
import cn.ff.onlineshop.service.ProductService;
import cn.ff.onlineshop.utils.SysUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

import java.sql.*;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OnlineshopApplicationTests {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderedRepository orderedRepository;
    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    ProductService productService;
    @Test
    public void jdbcCrudTest() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://127.0.0.1:3306/testdb";
        String username = "root";
        String password = "123456";
        Connection connection = DriverManager.getConnection(url, username, password);
        String sql = "select * from phone";

        PreparedStatement ps = connection.prepareStatement(sql);
        //获得结果集
        ResultSet rs = ps.executeQuery();
        //结果集处理，
        while (rs.next()) {
            System.out.println(rs.getString("id") + "  " + rs.getString("phone"));
        }
        //释放资源
        rs.close();
        ps.close();
        connection.close();
    }

    @Test
    public void dbConnectTest() throws Exception {

        User testUser = userRepository.findById(1);
        System.out.println(testUser.toString());
        List<User> list = userRepository.findByNameLike("%8%");
        if(list!=null && list.size()>0) {
            for (User user : list) {
                System.out.println(user);
            }
        }else{
            System.out.println("no data");
        }

        System.out.println(UUID.randomUUID().toString());
        System.out.println(SysUtil.getUUID());
    }

    @Test
    public void  test(){
        String dir="";
        ObjectMapper mapper = new ObjectMapper();
        //mapper.readValue(,JSONObject.class);

        System.out.println(dir);
    }

    @Test
    public void  page(){
        String name = "电视";
        int pagenum=0;
        PageRequest pageable = new PageRequest(pagenum, 3, Sort.Direction.DESC, "pdate");
        Page<Product> page = productRepository.findByNameLike(name, pageable);

        //查询结果总行数
        System.out.println(page.getTotalElements());
        //按照当前分页大小，总页数
        System.out.println(page.getTotalPages());
        //按照当前页数、分页大小，查出的分页结果集合
        for (Product t: page.getContent()) {
            System.out.println(t.toString());
        }
        System.out.println("-------------------------------------------");
    }

    /*Redis*/
    @Test
    public void redisStr() {
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis("localhost");
        System.out.println("连接成功");


        /*redis String*/
        //设置 redis 字符串数据
        jedis.set("ff1", "www.runoob.com");
        jedis.set("ff2","hikhefeng10");
        // 获取存储的数据并输出
        System.out.println("redis 存储的字符串ff2为: "+ jedis.get("ff2"));

        /*redis list*/
        //存储数据到列表中
        jedis.lpush("flist", "Runoob");
        jedis.lpush("flist", "Google");
        jedis.lpush("flist", "Taobao");
        // 获取存储的数据并输出
        List<String> list = jedis.lrange("flist", 0 ,2);
        for(int i=0; i<list.size(); i++) {
            System.out.println("列表项为: "+list.get(i));
        }

        // 获取所有key数据并输出
        Set<String> keys = jedis.keys("*");
        Iterator<String> it=keys.iterator() ;
        while(it.hasNext()){
            String key = it.next();
            System.out.println(key);
        }
    }

    @Test
    public void  test1(){
        String s = null;
        System.out.println(s);
    }


}
