 
package ltd.shopcart.cloud.shopease.openfeign;

import ltd.common.cloud.shopease.dto.Result;
import ltd.shopcart.cloud.shopease.dto.ShopEaseMallShoppingCartItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "shop-cart-service", url = "http://shop-cart-service:29030", path = "/shop-cart")
public interface ShopEaseCloudShopCartServiceFeign {

    @GetMapping(value = "/listByCartItemIds")
    Result<List<ShopEaseMallShoppingCartItemDTO>> listByCartItemIds(@RequestParam(value = "cartItemIds") List<Long> cartItemIds);

    @DeleteMapping(value = "/deleteByCartItemIds")
    Result<Boolean> deleteByCartItemIds(@RequestParam(value = "cartItemIds") List<Long> cartItemIds);
}
