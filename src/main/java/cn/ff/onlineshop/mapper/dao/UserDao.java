package cn.ff.onlineshop.mapper.dao;

import cn.ff.onlineshop.mapper.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
@Mapper
@Component
//@Repository
public interface UserDao {
    //全注解方式   xml方式需要在application中添加mapper-locations，xml文件在resource目录下
    //@Select("select id, username as userName , sex , note from t_user where id= #{id}")
    public User getUser(Long id);

    public int insertUser(User user);
}
