
package ltd.shopcart.cloud.shopease.service;

import ltd.common.cloud.shopease.dto.PageQueryUtil;
import ltd.common.cloud.shopease.dto.PageResult;
import ltd.shopcart.cloud.shopease.controller.param.SaveCartItemParam;
import ltd.shopcart.cloud.shopease.controller.param.UpdateCartItemParam;
import ltd.shopcart.cloud.shopease.controller.vo.ShopEaseMallShoppingCartItemVO;
import ltd.shopcart.cloud.shopease.entity.ShopEaseMallShoppingCartItem;

import java.util.List;

public interface ShopEaseMallShoppingCartService {

    /**
     * 保存商品至购物车中
     *
     * @param saveCartItemParam
     * @param userId
     * @return
     */
    String saveShopEaseMallCartItem(SaveCartItemParam saveCartItemParam, Long userId);

    /**
     * 修改购物车中的属性
     *
     * @param updateCartItemParam
     * @param userId
     * @return
     */
    String updateShopEaseMallCartItem(UpdateCartItemParam updateCartItemParam, Long userId);

    /**
     * 获取购物项详情
     *
     * @param shopEaseMallShoppingCartItemId
     * @return
     */
    ShopEaseMallShoppingCartItem getShopEaseMallCartItemById(Long shopEaseMallShoppingCartItemId);

    /**
     * 删除购物车中的商品
     *
     * @param shoppingCartItemId
     * @param userId
     * @return
     */
    Boolean deleteById(Long shoppingCartItemId, Long userId);

    /**
     * 获取我的购物车中的列表数据
     *
     * @param shopEaseMallUserId
     * @return
     */
    List<ShopEaseMallShoppingCartItemVO> getMyShoppingCartItems(Long shopEaseMallUserId);

    /**
     * 根据userId和cartItemIds获取对应的购物项记录
     *
     * @param cartItemIds
     * @param shopEaseMallUserId
     * @return
     */
    List<ShopEaseMallShoppingCartItemVO> getCartItemsForSettle(List<Long> cartItemIds, Long shopEaseMallUserId);


    /**
     * 根据cartItemIds获取对应的购物项记录
     *
     * @param cartItemIds
     * @return
     */
    List<ShopEaseMallShoppingCartItem> getCartItemsByCartIds(List<Long> cartItemIds);

    /**
     * 批量删除购物项记录
     *
     * @param cartItemIds
     * @return
     */
    int deleteCartItemsByCartIds(List<Long> cartItemIds);

    /**
     * 我的购物车(分页数据)
     *
     * @param pageUtil
     * @return
     */
    PageResult getMyShoppingCartItems(PageQueryUtil pageUtil);
}
