 
package ltd.shopcart.cloud.shopease.dao;

import ltd.common.cloud.shopease.dto.PageQueryUtil;
import ltd.shopcart.cloud.shopease.entity.ShopEaseMallShoppingCartItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShopEaseMallShoppingCartItemMapper {
    int deleteByPrimaryKey(Long cartItemId);

    int insert(ShopEaseMallShoppingCartItem record);

    int insertSelective(ShopEaseMallShoppingCartItem record);

    ShopEaseMallShoppingCartItem selectByPrimaryKey(Long cartItemId);

    ShopEaseMallShoppingCartItem selectByUserIdAndGoodsId(@Param("shopEaseMallUserId") Long shopEaseMallUserId, @Param("goodsId") Long goodsId);

    List<ShopEaseMallShoppingCartItem> selectByUserId(@Param("shopEaseMallUserId") Long shopEaseMallUserId, @Param("number") int number);

    List<ShopEaseMallShoppingCartItem> selectByUserIdAndCartItemIds(@Param("shopEaseMallUserId") Long shopEaseMallUserId, @Param("cartItemIds") List<Long> cartItemIds);

    List<ShopEaseMallShoppingCartItem> selectByCartItemIds(@Param("cartItemIds") List<Long> cartItemIds);

    int selectCountByUserId(Long shopEaseMallUserId);

    int updateByPrimaryKeySelective(ShopEaseMallShoppingCartItem record);

    int updateByPrimaryKey(ShopEaseMallShoppingCartItem record);

    int deleteBatch(List<Long> ids);

    List<ShopEaseMallShoppingCartItem> findMyShopEaseMallCartItems(PageQueryUtil pageUtil);

    int getTotalMyShopEaseMallCartItems(PageQueryUtil pageUtil);
}