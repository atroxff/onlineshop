package cn.ff.onlineshop.service;

import cn.ff.onlineshop.jpa.entities.User;
import cn.ff.onlineshop.exception.UserExistException;

public interface UserService {

    /*注册新用户*/
    void register(User user) throws UserExistException;

    /*用户认证*/
    User authUser(String username, String password);

    /*根据uid查找用户*/
    User findByUid(String uid);

    /*查找用户名*/
    boolean isExitsUsername(String username);

    /*更新用户信息*/
    void updateUserInfo(User user);
}
