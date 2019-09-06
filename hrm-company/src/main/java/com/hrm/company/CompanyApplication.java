package com.hrm.company;

import com.hrm.common.utils.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 *
 * </p>
 *
 * @author guozy
 * @create 2019/08/31
 */
@SpringBootApplication
@EntityScan("com.hrm.entity.company")
public class CompanyApplication {
    public static void main(String[] args) {
        SpringApplication.run(CompanyApplication.class,args);
    }
    @Bean
    public IdWorker idWorker(){
        return new IdWorker();
    }
}