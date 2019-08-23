package cn.ff.onlineshop.mapper.service;

import cn.ff.onlineshop.mapper.pojo.User;

public interface MybatisUserService {
    public User getUser(Long id);

    public int insertUser(User user);
}
