 
package ltd.order.cloud.shopease.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import ltd.common.cloud.shopease.enums.ServiceResultEnum;
import ltd.common.cloud.shopease.dto.PageQueryUtil;
import ltd.common.cloud.shopease.dto.PageResult;
import ltd.common.cloud.shopease.dto.Result;
import ltd.common.cloud.shopease.dto.ResultGenerator;
import ltd.common.cloud.shopease.exception.ShopEaseMallException;
import ltd.common.cloud.shopease.pojo.MallUserToken;
import ltd.order.cloud.shopease.config.annotation.TokenToMallUser;
import ltd.order.cloud.shopease.controller.param.SaveOrderParam;
import ltd.order.cloud.shopease.controller.vo.ShopEaseMallOrderDetailVO;
import ltd.order.cloud.shopease.controller.vo.ShopEaseMallOrderListVO;
import ltd.order.cloud.shopease.entity.MallUserAddress;
import ltd.order.cloud.shopease.service.ShopEaseMallOrderService;
import ltd.order.cloud.shopease.service.ShopEaseMallUserAddressService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "v1", tags = "新蜂商城订单操作相关接口")
@RequestMapping("/orders/mall")
public class ShopEaseMallOrderController {

    @Resource
    private ShopEaseMallOrderService shopEaseMallOrderService;
    @Resource
    private ShopEaseMallUserAddressService shopEaseMallUserAddressService;

    @PostMapping("/saveOrder")
    @ApiOperation(value = "生成订单接口", notes = "传参为地址id和待结算的购物项id数组")
    public Result<String> saveOrder(@ApiParam(value = "订单参数") @RequestBody SaveOrderParam saveOrderParam, @TokenToMallUser MallUserToken loginMallUserToken) {
        if (saveOrderParam == null || saveOrderParam.getCartItemIds() == null || saveOrderParam.getAddressId() == null) {
            ShopEaseMallException.fail(ServiceResultEnum.PARAM_ERROR.getResult());
        }
        if (saveOrderParam.getCartItemIds().length < 1) {
            ShopEaseMallException.fail(ServiceResultEnum.PARAM_ERROR.getResult());
        }
        MallUserAddress address = shopEaseMallUserAddressService.getMallUserAddressById(saveOrderParam.getAddressId());
        if (!loginMallUserToken.getUserId().equals(address.getUserId())) {
            return ResultGenerator.genFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        //保存订单并返回订单号
        String saveOrderResult = shopEaseMallOrderService.saveOrder(loginMallUserToken.getUserId(), address, Arrays.asList(saveOrderParam.getCartItemIds()));
        Result result = ResultGenerator.genSuccessResult();
        result.setData(saveOrderResult);
        return result;
    }

    @GetMapping("/order/{orderNo}")
    @ApiOperation(value = "订单详情接口", notes = "传参为订单号")
    public Result<ShopEaseMallOrderDetailVO> orderDetailPage(@ApiParam(value = "订单号") @PathVariable("orderNo") String orderNo, @TokenToMallUser MallUserToken loginMallUserToken) {
        return ResultGenerator.genSuccessResult(shopEaseMallOrderService.getOrderDetailByOrderNo(orderNo, loginMallUserToken.getUserId()));
    }

    @GetMapping("/order")
    @ApiOperation(value = "订单列表接口", notes = "传参为页码")
    public Result<PageResult<List<ShopEaseMallOrderListVO>>> orderList(@ApiParam(value = "页码") @RequestParam(required = false) Integer pageNumber,
                                                                     @ApiParam(value = "订单状态:0.待支付 1.待确认 2.待发货 3:已发货 4.交易成功") @RequestParam(required = false) Integer status,
                                                                     @TokenToMallUser MallUserToken loginMallUserToken) {
        Map params = new HashMap(8);
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }
        params.put("userId", loginMallUserToken.getUserId());
        params.put("orderStatus", status);
        params.put("page", pageNumber);
        params.put("limit", 5);
        //封装分页请求参数
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(shopEaseMallOrderService.getMyOrders(pageUtil));
    }

    @PutMapping("/order/{orderNo}/cancel")
    @ApiOperation(value = "订单取消接口", notes = "传参为订单号")
    public Result cancelOrder(@ApiParam(value = "订单号") @PathVariable("orderNo") String orderNo, @TokenToMallUser MallUserToken loginMallUserToken) {
        String cancelOrderResult = shopEaseMallOrderService.cancelOrder(orderNo, loginMallUserToken.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(cancelOrderResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(cancelOrderResult);
        }
    }

    @PutMapping("/order/{orderNo}/finish")
    @ApiOperation(value = "确认收货接口", notes = "传参为订单号")
    public Result finishOrder(@ApiParam(value = "订单号") @PathVariable("orderNo") String orderNo, @TokenToMallUser MallUserToken loginMallUserToken) {
        String finishOrderResult = shopEaseMallOrderService.finishOrder(orderNo, loginMallUserToken.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(finishOrderResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(finishOrderResult);
        }
    }

    @GetMapping("/paySuccess")
    @ApiOperation(value = "模拟支付成功回调的接口", notes = "传参为订单号和支付方式")
    public Result paySuccess(@ApiParam(value = "订单号") @RequestParam("orderNo") String orderNo, @ApiParam(value = "支付方式") @RequestParam("payType") int payType) {
        String payResult = shopEaseMallOrderService.paySuccess(orderNo, payType);
        if (ServiceResultEnum.SUCCESS.getResult().equals(payResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(payResult);
        }
    }

}
