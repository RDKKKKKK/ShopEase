 
package ltd.order.cloud.shopease;

import ltd.goods.cloud.shopease.openfeign.ShopEaseCloudGoodsServiceFeign;
import ltd.shopcart.cloud.shopease.openfeign.ShopEaseCloudShopCartServiceFeign;
import ltd.user.cloud.shopease.openfeign.ShopEaseCloudUserServiceFeign;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author 程序员十三
 * @qq交流群 791509631
 * @email 2449207463@qq.com
 * @link https://github.com/shopease-ltd
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("ltd.order.cloud.shopease.dao")
@EnableFeignClients(basePackageClasses ={ShopEaseCloudUserServiceFeign.class, ShopEaseCloudGoodsServiceFeign.class, ShopEaseCloudShopCartServiceFeign.class})
public class ShopEaseMallCloudOrderServiceApplication {

    public static void main(String[] args) {
        System.setProperty("nacos.logging.default.config.enabled","false");
        SpringApplication.run(ShopEaseMallCloudOrderServiceApplication.class, args);
    }

}
