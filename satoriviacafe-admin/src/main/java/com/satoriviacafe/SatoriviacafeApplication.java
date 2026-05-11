package com.satoriviacafe;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动程序
 *
 * @author satoriviacafe
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@RequiredArgsConstructor
@EnableScheduling
public class SatoriviacafeApplication {

    static void main(String[] args) {
        SpringApplication.run(SatoriviacafeApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  Satoriviacafe启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
