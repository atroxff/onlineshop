package cn.ff.onlineshop.constant;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({"classpath:config.properties","classpath:config1.properties"})
public class ConfigParam {

    @Value("${ip}")
    private String ip;

    @Value("${port}")
    private String port;

    @Value("${protocol}")
    private String protocol;

    @Value("${application}")
    private String application;

    @Value("${contextpath}")
    private String contextpath;

    @Value("${app}")
    private String app;

    @Value("${dbclass}")
    private String dbclass;

    @Value("${file-upload-path}")
    private String filepath;

    @Value("${pagesize}")
    private String pagesize;

    public String getPagesize() {
        return pagesize;
    }

    public void setPagesize(String pagesize) {
        this.pagesize = pagesize;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getContextpath() {
        return contextpath;
    }

    public void setContextpath(String contextpath) {
        this.contextpath = contextpath;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getDbclass() {
        return dbclass;
    }

    public void setDbclass(String dbclass) {
        this.dbclass = dbclass;
    }

}
