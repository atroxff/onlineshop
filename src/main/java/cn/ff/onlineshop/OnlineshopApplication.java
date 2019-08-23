package cn.ff.onlineshop;

import org.mybatis.spring.annotation.MapperScan;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;


//@ServletComponentScan
@SpringBootApplication
//@EnableScheduling  //定时任务
@MapperScan(basePackages = "cn.ff.onlineshop.mapper.dao",annotationClass = Mapper.class)
public class OnlineshopApplication {

    @Autowired
    private RedisTemplate redisTemplate = null;
    //自定义初始化方法
    @PostConstruct//在依赖注入完成后被自动调用
    public void init(){
        initRedisTemplate();
    }
    //设置redisTemplate序列化器
    private void initRedisTemplate() {
        RedisSerializer stringSerializer=redisTemplate.getStringSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);

        //redisTemplate.setValueSerializer(stringSerializer);
        //redisTemplate.setHashValueSerializer(stringSerializer);
    }

    public static void main(String[] args) {
        SpringApplication.run(OnlineshopApplication.class, args);

    }
}
