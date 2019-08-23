package cn.ff.onlineshop.mapper.service;

import cn.ff.onlineshop.mapper.dao.UserDao;
import cn.ff.onlineshop.mapper.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MybatisUserServiceImpl implements MybatisUserService {

    @Autowired
    UserDao userDao;
    @Override
    public User getUser(Long id) {
        return userDao.getUser(id);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRES_NEW,
            //propagation = Propagation.NESTED, //JPA(Hibernate)不支持NESTED
            timeout = 1)//限制超时时间为1s
    public int insertUser(User user) {
        return userDao.insertUser(user);
    }


}
