package cn.hrm.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * <p>
 *
 * </p>
 *
 * @author guozy
 * @create 2019/09/29
 */
@SpringBootApplication
@EnableEurekaServer
public class HrmServer {
    public static void main(String[] args) {
        SpringApplication.run(HrmServer.class,args);
    }
}
