 
package ltd.shopcart.cloud.shopease.controller;

import io.seata.core.context.RootContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ltd.common.cloud.shopease.enums.ServiceResultEnum;
import ltd.common.cloud.shopease.dto.PageQueryUtil;
import ltd.common.cloud.shopease.dto.PageResult;
import ltd.common.cloud.shopease.dto.Result;
import ltd.common.cloud.shopease.dto.ResultGenerator;
import ltd.common.cloud.shopease.exception.ShopEaseMallException;
import ltd.common.cloud.shopease.pojo.MallUserToken;
import ltd.shopcart.cloud.shopease.config.annotation.TokenToMallUser;
import ltd.shopcart.cloud.shopease.controller.param.SaveCartItemParam;
import ltd.shopcart.cloud.shopease.controller.param.UpdateCartItemParam;
import ltd.shopcart.cloud.shopease.controller.vo.ShopEaseMallShoppingCartItemVO;
import ltd.shopcart.cloud.shopease.entity.ShopEaseMallShoppingCartItem;
import ltd.shopcart.cloud.shopease.service.ShopEaseMallShoppingCartService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "v1", tags = "新蜂商城购物车相关接口")
public class ShopEaseMallShoppingCartController {

    @Resource
    private ShopEaseMallShoppingCartService shopEaseMallShoppingCartService;

    @GetMapping("/shop-cart/page")
    @ApiOperation(value = "购物车列表(每页默认5条)", notes = "传参为页码")
    public Result<PageResult<List<ShopEaseMallShoppingCartItemVO>>> cartItemPageList(Integer pageNumber, @TokenToMallUser MallUserToken loginMallUserToken) {
        Map params = new HashMap(8);
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }
        params.put("userId", loginMallUserToken.getUserId());
        params.put("page", pageNumber);
        params.put("limit", 5);
        //封装分页请求参数
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(shopEaseMallShoppingCartService.getMyShoppingCartItems(pageUtil));
    }

    @GetMapping("/shop-cart")
    @ApiOperation(value = "购物车列表(网页移动端不分页)", notes = "")
    public Result<List<ShopEaseMallShoppingCartItemVO>> cartItemList(@TokenToMallUser MallUserToken loginMallUserToken) {
        return ResultGenerator.genSuccessResult(shopEaseMallShoppingCartService.getMyShoppingCartItems(loginMallUserToken.getUserId()));
    }

    @PostMapping("/shop-cart")
    @ApiOperation(value = "添加商品到购物车接口", notes = "传参为商品id、数量")
    public Result saveShopEaseMallShoppingCartItem(@RequestBody SaveCartItemParam saveCartItemParam,
                                                 @TokenToMallUser MallUserToken loginMallUserToken) {
        String saveResult = shopEaseMallShoppingCartService.saveShopEaseMallCartItem(saveCartItemParam, loginMallUserToken.getUserId());
        //添加成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(saveResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //添加失败
        return ResultGenerator.genFailResult(saveResult);
    }

    @PutMapping("/shop-cart")
    @ApiOperation(value = "修改购物项数据", notes = "传参为购物项id、数量")
    public Result updateShopEaseMallShoppingCartItem(@RequestBody UpdateCartItemParam updateCartItemParam,
                                                   @TokenToMallUser MallUserToken loginMallUserToken) {
        String updateResult = shopEaseMallShoppingCartService.updateShopEaseMallCartItem(updateCartItemParam, loginMallUserToken.getUserId());
        //修改成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(updateResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //修改失败
        return ResultGenerator.genFailResult(updateResult);
    }

    @DeleteMapping("/shop-cart/{shopEaseMallShoppingCartItemId}")
    @ApiOperation(value = "删除购物项", notes = "传参为购物项id")
    public Result updateShopEaseMallShoppingCartItem(@PathVariable("shopEaseMallShoppingCartItemId") Long shopEaseMallShoppingCartItemId,
                                                   @TokenToMallUser MallUserToken loginMallUserToken) {
        ShopEaseMallShoppingCartItem shopEaseMallCartItemById = shopEaseMallShoppingCartService.getShopEaseMallCartItemById(shopEaseMallShoppingCartItemId);
        if (!loginMallUserToken.getUserId().equals(shopEaseMallCartItemById.getUserId())) {
            return ResultGenerator.genFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        Boolean deleteResult = shopEaseMallShoppingCartService.deleteById(shopEaseMallShoppingCartItemId, loginMallUserToken.getUserId());
        //删除成功
        if (deleteResult) {
            return ResultGenerator.genSuccessResult();
        }
        //删除失败
        return ResultGenerator.genFailResult(ServiceResultEnum.OPERATE_ERROR.getResult());
    }

    @GetMapping("/shop-cart/settle")
    @ApiOperation(value = "根据购物项id数组查询购物项明细", notes = "确认订单页面使用")
    public Result<List<ShopEaseMallShoppingCartItemVO>> toSettle(Long[] cartItemIds, @TokenToMallUser MallUserToken loginMallUserToken) {
        if (cartItemIds.length < 1) {
            ShopEaseMallException.fail("参数异常");
        }
        int priceTotal = 0;
        List<ShopEaseMallShoppingCartItemVO> itemsForSettle = shopEaseMallShoppingCartService.getCartItemsForSettle(Arrays.asList(cartItemIds), loginMallUserToken.getUserId());
        if (CollectionUtils.isEmpty(itemsForSettle)) {
            //无数据则抛出异常
            ShopEaseMallException.fail("参数异常");
        } else {
            //总价
            for (ShopEaseMallShoppingCartItemVO shopEaseMallShoppingCartItemVO : itemsForSettle) {
                priceTotal += shopEaseMallShoppingCartItemVO.getGoodsCount() * shopEaseMallShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                ShopEaseMallException.fail("价格异常");
            }
        }
        return ResultGenerator.genSuccessResult(itemsForSettle);
    }

    @GetMapping("/shop-cart/listByCartItemIds")
    @ApiOperation(value = "购物车列表", notes = "")
    public Result<List<ShopEaseMallShoppingCartItem>> cartItemListByIds(@RequestParam("cartItemIds") List<Long> cartItemIds) {
        if (CollectionUtils.isEmpty(cartItemIds)) {
            return ResultGenerator.genFailResult("error param");
        }
        return ResultGenerator.genSuccessResult(shopEaseMallShoppingCartService.getCartItemsByCartIds(cartItemIds));
    }

    @DeleteMapping("/shop-cart/deleteByCartItemIds")
    @ApiOperation(value = "批量删除购物项", notes = "")
    public Result<Boolean> deleteByCartItemIds(@RequestParam("cartItemIds") List<Long> cartItemIds, HttpServletRequest request) {
        System.out.println("RootContext.getXID()="+ RootContext.getXID());
        if (CollectionUtils.isEmpty(cartItemIds)) {
            return ResultGenerator.genFailResult("error param");
        }
        return ResultGenerator.genSuccessResult(shopEaseMallShoppingCartService.deleteCartItemsByCartIds(cartItemIds) > 0);
    }
}
