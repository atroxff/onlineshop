package cn.ff.onlineshop.mapper.service;

import cn.ff.onlineshop.mapper.dao.UserDao;
import cn.ff.onlineshop.mapper.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class UserBatchServiceImpl implements UserBatchService {
    @Autowired
    MybatisUserService mybatisUserService;

    @Override
    //将一个整体事务分发，定义内部事务的传播行为
    //传播行为Propagation.REQUIRED有则沿用，无则新建  REQUIRES_NEW总是新建 NESTED异常回调子方法
    //Transactional自调用失效，需要调用另一个service的方法才能实现AOP约定流程
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    public int insertUsers(List<User> userList) {
        int count = 0;
        for (User user : userList) {
            count += mybatisUserService.insertUser(user);
        }
        return count;
    }
}
