package cn.ff.onlineshop.config;



import cn.ff.onlineshop.constant.ConfigParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;

//使用WebMvcConfigurerAdapter可以来扩展SpringMVC的功能
//@EnableWebMvc   //不要接管SpringMVC
@Configuration
public class MyMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private ConfigParam configParam;


    //所有的WebMvcConfigurerAdapter组件都会一起起作用
    @Bean //将组件注册在容器
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter(){
        WebMvcConfigurerAdapter adapter = new WebMvcConfigurerAdapter() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("forward:/index.do");
                registry.addViewController("/index.html").setViewName("login/login1");
                //registry.addViewController("/main.html").setViewName("dashboard");
                //registry.addViewController("/register.html").setViewName("register");
            }

            //注册拦截器
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                //super.addInterceptors(registry);
                //静态资源；  *.css , *.js
                //SpringBoot已经做好了静态资源映射
          /*      registry.addInterceptor(new LoginHandlerInterceptor())
                        .addPathPatterns("/**")
                        .excludePathPatterns("/index.html","/","/user/login",
                               "/user/register","/user/registerSubmit");*/
            }

            //配置自定义静态资源路径
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                //虚拟url 真实工程环境  http://127.0.0.1:8080/ff/resources/img/1.jpg
                registry.addResourceHandler("/ff/resources/**").addResourceLocations("classpath:/static/");
                String filepath = configParam.getFilepath();
                registry.addResourceHandler("/pic/**").addResourceLocations("file:"+filepath);
            }

            /* 跨域请求 */
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedHeaders("*")
//                        .allowedMethods("*")
//                        .allowedOrigins("*");
//            }

        };
        return adapter;
    }

    @Bean
    public LocaleResolver localeResolver(){

        return new MyLocaleResolver();
    }



}
