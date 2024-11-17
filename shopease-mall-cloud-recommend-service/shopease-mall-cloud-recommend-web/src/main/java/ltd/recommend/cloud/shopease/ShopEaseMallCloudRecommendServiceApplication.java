 
package ltd.recommend.cloud.shopease;

import ltd.user.cloud.shopease.openfeign.ShopEaseCloudUserServiceFeign;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author 程序员十三
 * @qq交流群 791509631
 * @email 2449207463@qq.com
 * @link https://github.com/shopease-ltd
 */
@SpringBootApplication
@MapperScan("ltd.recommend.cloud.shopease.dao")
@EnableFeignClients(basePackageClasses =
        {ShopEaseCloudUserServiceFeign.class,
         ltd.goods.cloud.shopease.openfeign.ShopEaseCloudGoodsServiceFeign.class})
public class ShopEaseMallCloudRecommendServiceApplication {

    public static void main(String[] args) {
        System.setProperty("nacos.logging.default.config.enabled","false");
        SpringApplication.run(ShopEaseMallCloudRecommendServiceApplication.class, args);
    }

}
