package cn.ff.onlineshop.mapper.service;

import cn.ff.onlineshop.mapper.pojo.User;

import java.util.List;

public interface UserBatchService {
    public int insertUsers(List<User> userList);
}
