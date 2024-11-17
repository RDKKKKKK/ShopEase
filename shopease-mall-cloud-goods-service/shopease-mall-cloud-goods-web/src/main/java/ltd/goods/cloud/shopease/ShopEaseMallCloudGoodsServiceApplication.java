 
package ltd.goods.cloud.shopease;

import ltd.user.cloud.shopease.openfeign.ShopEaseCloudUserServiceFeign;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("ltd.goods.cloud.shopease.dao")
@EnableFeignClients(basePackageClasses = {ShopEaseCloudUserServiceFeign.class})
public class ShopEaseMallCloudGoodsServiceApplication {

    public static void main(String[] args) {
        System.setProperty("nacos.logging.default.config.enabled","false");
        SpringApplication.run(ShopEaseMallCloudGoodsServiceApplication.class, args);
    }

}
