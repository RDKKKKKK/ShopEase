 
package ltd.shopcart.cloud.shopease.service.impl;

import ltd.common.cloud.shopease.enums.ServiceResultEnum;
import ltd.common.cloud.shopease.dto.PageQueryUtil;
import ltd.common.cloud.shopease.dto.PageResult;
import ltd.common.cloud.shopease.dto.Result;
import ltd.common.cloud.shopease.exception.ShopEaseMallException;
import ltd.common.cloud.shopease.util.BeanUtil;
import ltd.goods.cloud.shopease.dto.ShopEaseMallGoodsDTO;
import ltd.goods.cloud.shopease.openfeign.ShopEaseCloudGoodsServiceFeign;
import ltd.shopcart.cloud.shopease.controller.param.SaveCartItemParam;
import ltd.shopcart.cloud.shopease.controller.param.UpdateCartItemParam;
import ltd.shopcart.cloud.shopease.controller.vo.ShopEaseMallShoppingCartItemVO;
import ltd.shopcart.cloud.shopease.dao.ShopEaseMallShoppingCartItemMapper;
import ltd.shopcart.cloud.shopease.entity.ShopEaseMallShoppingCartItem;
import ltd.shopcart.cloud.shopease.service.ShopEaseMallShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ShopEaseMallShoppingCartServiceImpl implements ShopEaseMallShoppingCartService {

    @Autowired
    private ShopEaseMallShoppingCartItemMapper shopEaseMallShoppingCartItemMapper;

    @Autowired
    private ShopEaseCloudGoodsServiceFeign goodsService;

    @Override
    public String saveShopEaseMallCartItem(SaveCartItemParam saveCartItemParam, Long userId) {
        ShopEaseMallShoppingCartItem temp = shopEaseMallShoppingCartItemMapper.selectByUserIdAndGoodsId(userId, saveCartItemParam.getGoodsId());
        if (temp != null) {
            //已存在则修改该记录
            ShopEaseMallException.fail(ServiceResultEnum.SHOPPING_CART_ITEM_EXIST_ERROR.getResult());
        }
        Result<ShopEaseMallGoodsDTO> goodsDetailResult = goodsService.getGoodsDetail(saveCartItemParam.getGoodsId());
        //商品为空
        if (goodsDetailResult == null || goodsDetailResult.getResultCode() != 200) {
            return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
        }
        int totalItem = shopEaseMallShoppingCartItemMapper.selectCountByUserId(userId);
        //超出单个商品的最大数量
        if (saveCartItemParam.getGoodsCount() < 1) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_NUMBER_ERROR.getResult();
        }
        //超出单个商品的最大数量
        if (saveCartItemParam.getGoodsCount() > 5) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //超出最大数量
        if (totalItem > 20) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_TOTAL_NUMBER_ERROR.getResult();
        }
        ShopEaseMallShoppingCartItem shopEaseMallShoppingCartItem = new ShopEaseMallShoppingCartItem();
        BeanUtil.copyProperties(saveCartItemParam, shopEaseMallShoppingCartItem);
        shopEaseMallShoppingCartItem.setUserId(userId);
        //保存记录
        if (shopEaseMallShoppingCartItemMapper.insertSelective(shopEaseMallShoppingCartItem) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateShopEaseMallCartItem(UpdateCartItemParam updateCartItemParam, Long userId) {
        ShopEaseMallShoppingCartItem shopEaseMallShoppingCartItemUpdate = shopEaseMallShoppingCartItemMapper.selectByPrimaryKey(updateCartItemParam.getCartItemId());
        if (shopEaseMallShoppingCartItemUpdate == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        if (!shopEaseMallShoppingCartItemUpdate.getUserId().equals(userId)) {
            ShopEaseMallException.fail(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        //超出单个商品的最大数量
        if (updateCartItemParam.getGoodsCount() > 5) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //当前登录账号的userId与待修改的cartItem中userId不同，返回错误
        if (!shopEaseMallShoppingCartItemUpdate.getUserId().equals(userId)) {
            return ServiceResultEnum.NO_PERMISSION_ERROR.getResult();
        }
        //数值相同，则不执行数据操作
        if (updateCartItemParam.getGoodsCount().equals(shopEaseMallShoppingCartItemUpdate.getGoodsCount())) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        shopEaseMallShoppingCartItemUpdate.setGoodsCount(updateCartItemParam.getGoodsCount());
        shopEaseMallShoppingCartItemUpdate.setUpdateTime(new Date());
        //修改记录
        if (shopEaseMallShoppingCartItemMapper.updateByPrimaryKeySelective(shopEaseMallShoppingCartItemUpdate) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public ShopEaseMallShoppingCartItem getShopEaseMallCartItemById(Long shopEaseMallShoppingCartItemId) {
        ShopEaseMallShoppingCartItem shopEaseMallShoppingCartItem = shopEaseMallShoppingCartItemMapper.selectByPrimaryKey(shopEaseMallShoppingCartItemId);
        if (shopEaseMallShoppingCartItem == null) {
            ShopEaseMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return shopEaseMallShoppingCartItem;
    }

    @Override
    public Boolean deleteById(Long shoppingCartItemId, Long userId) {
        ShopEaseMallShoppingCartItem shopEaseMallShoppingCartItem = shopEaseMallShoppingCartItemMapper.selectByPrimaryKey(shoppingCartItemId);
        if (shopEaseMallShoppingCartItem == null) {
            return false;
        }
        //userId不同不能删除
        if (!userId.equals(shopEaseMallShoppingCartItem.getUserId())) {
            return false;
        }
        return shopEaseMallShoppingCartItemMapper.deleteByPrimaryKey(shoppingCartItemId) > 0;
    }

    @Override
    public List<ShopEaseMallShoppingCartItemVO> getMyShoppingCartItems(Long shopEaseMallUserId) {
        List<ShopEaseMallShoppingCartItemVO> shopEaseMallShoppingCartItemVOS = new ArrayList<>();
        List<ShopEaseMallShoppingCartItem> shopEaseMallShoppingCartItems = shopEaseMallShoppingCartItemMapper.selectByUserId(shopEaseMallUserId, 20);
        return getShopEaseMallShoppingCartItemVOS(shopEaseMallShoppingCartItemVOS, shopEaseMallShoppingCartItems);
    }

    @Override
    public List<ShopEaseMallShoppingCartItemVO> getCartItemsForSettle(List<Long> cartItemIds, Long shopEaseMallUserId) {
        List<ShopEaseMallShoppingCartItemVO> shopEaseMallShoppingCartItemVOS = new ArrayList<>();
        if (CollectionUtils.isEmpty(cartItemIds)) {
            ShopEaseMallException.fail("购物项不能为空");
        }
        List<ShopEaseMallShoppingCartItem> shopEaseMallShoppingCartItems = shopEaseMallShoppingCartItemMapper.selectByUserIdAndCartItemIds(shopEaseMallUserId, cartItemIds);
        if (CollectionUtils.isEmpty(shopEaseMallShoppingCartItems)) {
            ShopEaseMallException.fail("购物项不能为空");
        }
        if (shopEaseMallShoppingCartItems.size() != cartItemIds.size()) {
            ShopEaseMallException.fail("参数异常");
        }
        return getShopEaseMallShoppingCartItemVOS(shopEaseMallShoppingCartItemVOS, shopEaseMallShoppingCartItems);
    }

    @Override
    public List<ShopEaseMallShoppingCartItem> getCartItemsByCartIds(List<Long> cartItemIds) {
        return shopEaseMallShoppingCartItemMapper.selectByCartItemIds(cartItemIds);
    }

    @Override
    public int deleteCartItemsByCartIds(List<Long> cartItemIds) {
        return shopEaseMallShoppingCartItemMapper.deleteBatch(cartItemIds);
    }

    /**
     * 数据转换
     *
     * @param shopEaseMallShoppingCartItemVOS
     * @param shopEaseMallShoppingCartItems
     * @return
     */
    private List<ShopEaseMallShoppingCartItemVO> getShopEaseMallShoppingCartItemVOS(List<ShopEaseMallShoppingCartItemVO> shopEaseMallShoppingCartItemVOS, List<ShopEaseMallShoppingCartItem> shopEaseMallShoppingCartItems) {
        if (!CollectionUtils.isEmpty(shopEaseMallShoppingCartItems)) {
            //查询商品信息并做数据转换
            List<Long> shopEaseMallGoodsIds = shopEaseMallShoppingCartItems.stream().map(ShopEaseMallShoppingCartItem::getGoodsId).collect(Collectors.toList());
            Result<List<ShopEaseMallGoodsDTO>> shopEaseMallGoodsDTOResult = goodsService.listByGoodsIds(shopEaseMallGoodsIds);
            //商品为空
            if (shopEaseMallGoodsDTOResult == null || shopEaseMallGoodsDTOResult.getResultCode() != 200) {
                ShopEaseMallException.fail(ServiceResultEnum.GOODS_NOT_EXIST.getResult());
            }
            Map<Long, ShopEaseMallGoodsDTO> shopEaseMallGoodsDTOMap = new HashMap<>();
            List<ShopEaseMallGoodsDTO> shopEaseMallGoodsDTOS = shopEaseMallGoodsDTOResult.getData();
            if (!CollectionUtils.isEmpty(shopEaseMallGoodsDTOS)) {
                shopEaseMallGoodsDTOMap = shopEaseMallGoodsDTOS.stream().collect(Collectors.toMap(ShopEaseMallGoodsDTO::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
            }
            for (ShopEaseMallShoppingCartItem shopEaseMallShoppingCartItem : shopEaseMallShoppingCartItems) {
                ShopEaseMallShoppingCartItemVO shopEaseMallShoppingCartItemVO = new ShopEaseMallShoppingCartItemVO();
                BeanUtil.copyProperties(shopEaseMallShoppingCartItem, shopEaseMallShoppingCartItemVO);
                if (shopEaseMallGoodsDTOMap.containsKey(shopEaseMallShoppingCartItem.getGoodsId())) {
                    ShopEaseMallGoodsDTO shopEaseMallGoodsDtoTemp = shopEaseMallGoodsDTOMap.get(shopEaseMallShoppingCartItem.getGoodsId());
                    shopEaseMallShoppingCartItemVO.setGoodsCoverImg(shopEaseMallGoodsDtoTemp.getGoodsCoverImg());
                    String goodsName = shopEaseMallGoodsDtoTemp.getGoodsName();
                    // 字符串过长导致文字超出的问题
                    if (goodsName.length() > 28) {
                        goodsName = goodsName.substring(0, 28) + "...";
                    }
                    shopEaseMallShoppingCartItemVO.setGoodsName(goodsName);
                    shopEaseMallShoppingCartItemVO.setSellingPrice(shopEaseMallGoodsDtoTemp.getSellingPrice());
                    shopEaseMallShoppingCartItemVOS.add(shopEaseMallShoppingCartItemVO);
                }
            }
        }
        return shopEaseMallShoppingCartItemVOS;
    }

    @Override
    public PageResult getMyShoppingCartItems(PageQueryUtil pageUtil) {
        List<ShopEaseMallShoppingCartItemVO> shopEaseMallShoppingCartItemVOS = new ArrayList<>();
        List<ShopEaseMallShoppingCartItem> shopEaseMallShoppingCartItems = shopEaseMallShoppingCartItemMapper.findMyShopEaseMallCartItems(pageUtil);
        int total = shopEaseMallShoppingCartItemMapper.getTotalMyShopEaseMallCartItems(pageUtil);
        PageResult pageResult = new PageResult(getShopEaseMallShoppingCartItemVOS(shopEaseMallShoppingCartItemVOS, shopEaseMallShoppingCartItems), total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
}
