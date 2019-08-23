package cn.ff.onlineshop.jpa.reposity;

import cn.ff.onlineshop.jpa.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findById(Integer id);

    List<User> findByNameLike(String name);

    User findByUsernameAndPassword(String username, String password);

    User findByUsername(String username);

    User findByUid(String uid);
}
