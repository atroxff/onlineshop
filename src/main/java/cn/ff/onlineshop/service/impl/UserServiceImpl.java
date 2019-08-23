package cn.ff.onlineshop.service.impl;

import cn.ff.onlineshop.jpa.entities.User;
import cn.ff.onlineshop.jpa.reposity.UserRepository;
import cn.ff.onlineshop.exception.UserExistException;
import cn.ff.onlineshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    /**
     * 注册新用户
     * @param user
     */
    public void register(User user) throws UserExistException {
        //1.校验用户是否存在
        User a = userRepository.findByUsername(user.getUsername());

        if(a!=null){
            //2.存在则抛出用户已存在异常
            throw new UserExistException();//Å×³ö±àÒëÊ±Òì³£  ÌáÐÑweb²ã¶Ô¿Í»§ÏìÓ¦

        }else{
            //不存在就正常添加新用户
            userRepository.save(user);

            //System.out.println(userDao.getAllUsers().toString());
        }
    }

    /**
     * 验证用户名密码
     * @param username
     * @param password
     * @return
     */
    public User authUser(String username, String password){
        return userRepository.findByUsernameAndPassword(username,password);
    }

    /* 查找用户 */
    public User findByUid(String uid){
        return userRepository.findByUid(uid);
    }

    @Override
    public boolean isExitsUsername(String username) {

        User a = userRepository.findByUsername(username);
        if(a!=null){//用户已存在
           return true;
        }
        return false;
    }

    @Override
    public void updateUserInfo(User user) {
        userRepository.save(user);
    }
}
