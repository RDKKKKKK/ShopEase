 
package ltd.goods.cloud.shopease.openfeign;

import ltd.common.cloud.shopease.dto.Result;
import ltd.goods.cloud.shopease.dto.ShopEaseMallGoodsDTO;
import ltd.goods.cloud.shopease.dto.UpdateStockNumDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "goods-service", path = "/goods", url = "http://goods-service:29010")
public interface ShopEaseCloudGoodsServiceFeign {

    @GetMapping(value = "/admin/goodsDetail")
    Result<ShopEaseMallGoodsDTO> getGoodsDetail(@RequestParam(value = "goodsId") Long goodsId);

    @GetMapping(value = "/admin/listByGoodsIds")
    Result<List<ShopEaseMallGoodsDTO>> listByGoodsIds(@RequestParam(value = "goodsIds") List<Long> goodsIds);

    @PutMapping(value = "/admin/updateStock")
    Result<Boolean> updateStock(@RequestBody UpdateStockNumDTO updateStockNumDTO);
}
