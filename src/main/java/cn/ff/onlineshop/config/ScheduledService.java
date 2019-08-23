package cn.ff.onlineshop.config;

import cn.ff.onlineshop.controller.ProductController;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务
 */
@Slf4j
@Component
public class ScheduledService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledService.class);
    int count1=1;
    int count2=1;

//    @Scheduled(cron = "0/5 * * * * *")
//    public void scheduled(){
//        logger.info("=====>>>>>使用cron  {}",System.currentTimeMillis());
//    }
//    @Scheduled(fixedRate = 5000)
//    public void scheduled1() {
//        logger.info("=====>>>>>使用fixedRate{}", System.currentTimeMillis());
//    }
//    @Scheduled(fixedDelay = 5000)
//    public void scheduled2() {
//        logger.info("=====>>>>>fixedDelay{}",System.currentTimeMillis());
//    }

//    异步anysc
//    @Scheduled(cron = "0/2 * * * * *")
//    @Async
//    public void scheduled3(){
//        logger.info("异步线程1："+Thread.currentThread().getName()+"执行第"+count1+"次");
//        count1++;
//    }
//    @Scheduled(cron = "0/2 * * * * *")
//    @Async
//    public void scheduled4(){
//        logger.info("异步线程2："+Thread.currentThread().getName()+"执行第"+count2+"次");
//        count2++;
//    }
}
