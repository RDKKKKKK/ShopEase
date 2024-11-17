 
package ltd.user.cloud.shopease;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients  // 启用 Feign 客户端
@MapperScan("ltd.user.cloud.shopease.dao")
public class ShopEaseMallCloudUserServiceApplication {


    //test
    public static void main(String[] args) {
        System.setProperty("nacos.logging.default.config.enabled","false");
        SpringApplication.run(ShopEaseMallCloudUserServiceApplication.class, args);
    }



}
