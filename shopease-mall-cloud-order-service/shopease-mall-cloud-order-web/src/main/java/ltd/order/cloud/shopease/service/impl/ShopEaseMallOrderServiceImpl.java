 
package ltd.order.cloud.shopease.service.impl;

import io.seata.spring.annotation.GlobalTransactional;
import ltd.common.cloud.shopease.enums.ServiceResultEnum;
import ltd.common.cloud.shopease.dto.PageQueryUtil;
import ltd.common.cloud.shopease.dto.PageResult;
import ltd.common.cloud.shopease.dto.Result;
import ltd.common.cloud.shopease.enums.ShopEaseMallOrderStatusEnum;
import ltd.common.cloud.shopease.enums.PayStatusEnum;
import ltd.common.cloud.shopease.enums.PayTypeEnum;
import ltd.common.cloud.shopease.exception.ShopEaseMallException;
import ltd.common.cloud.shopease.util.BeanUtil;
import ltd.common.cloud.shopease.util.NumberUtil;
import ltd.goods.cloud.shopease.dto.ShopEaseMallGoodsDTO;
import ltd.goods.cloud.shopease.dto.StockNumDTO;
import ltd.goods.cloud.shopease.dto.UpdateStockNumDTO;
import ltd.goods.cloud.shopease.openfeign.ShopEaseCloudGoodsServiceFeign;
import ltd.order.cloud.shopease.controller.vo.ShopEaseMallOrderDetailVO;
import ltd.order.cloud.shopease.controller.vo.ShopEaseMallOrderItemVO;
import ltd.order.cloud.shopease.controller.vo.ShopEaseMallOrderListVO;
import ltd.order.cloud.shopease.dao.ShopEaseMallOrderAddressMapper;
import ltd.order.cloud.shopease.dao.ShopEaseMallOrderItemMapper;
import ltd.order.cloud.shopease.dao.ShopEaseMallOrderMapper;
import ltd.order.cloud.shopease.entity.MallUserAddress;
import ltd.order.cloud.shopease.entity.ShopEaseMallOrder;
import ltd.order.cloud.shopease.entity.ShopEaseMallOrderAddress;
import ltd.order.cloud.shopease.entity.ShopEaseMallOrderItem;
import ltd.order.cloud.shopease.service.ShopEaseMallOrderService;
import ltd.shopcart.cloud.shopease.dto.ShopEaseMallShoppingCartItemDTO;
import ltd.shopcart.cloud.shopease.openfeign.ShopEaseCloudShopCartServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class ShopEaseMallOrderServiceImpl implements ShopEaseMallOrderService {

    @Autowired
    private ShopEaseMallOrderMapper shopEaseMallOrderMapper;

    @Autowired
    private ShopEaseMallOrderItemMapper shopEaseMallOrderItemMapper;

    @Autowired
    private ShopEaseMallOrderAddressMapper shopEaseMallOrderAddressMapper;

    @Autowired
    private ShopEaseCloudGoodsServiceFeign goodsService;

    @Autowired
    private ShopEaseCloudShopCartServiceFeign shopCartService;

    @Override
    public ShopEaseMallOrderDetailVO getOrderDetailByOrderId(Long orderId) {
        ShopEaseMallOrder shopEaseMallOrder = shopEaseMallOrderMapper.selectByPrimaryKey(orderId);
        if (shopEaseMallOrder == null) {
            ShopEaseMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        List<ShopEaseMallOrderItem> orderItems = shopEaseMallOrderItemMapper.selectByOrderId(shopEaseMallOrder.getOrderId());
        //获取订单项数据
        if (!CollectionUtils.isEmpty(orderItems)) {
            List<ShopEaseMallOrderItemVO> shopEaseMallOrderItemVOS = BeanUtil.copyList(orderItems, ShopEaseMallOrderItemVO.class);
            ShopEaseMallOrderDetailVO shopEaseMallOrderDetailVO = new ShopEaseMallOrderDetailVO();
            BeanUtil.copyProperties(shopEaseMallOrder, shopEaseMallOrderDetailVO);
            shopEaseMallOrderDetailVO.setOrderStatusString(ShopEaseMallOrderStatusEnum.getShopEaseMallOrderStatusEnumByStatus(shopEaseMallOrderDetailVO.getOrderStatus()).getName());
            shopEaseMallOrderDetailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(shopEaseMallOrderDetailVO.getPayType()).getName());
            shopEaseMallOrderDetailVO.setShopEaseMallOrderItemVOS(shopEaseMallOrderItemVOS);
            return shopEaseMallOrderDetailVO;
        } else {
            ShopEaseMallException.fail(ServiceResultEnum.ORDER_ITEM_NULL_ERROR.getResult());
            return null;
        }
    }

    @Override
    public ShopEaseMallOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId) {
        ShopEaseMallOrder shopEaseMallOrder = shopEaseMallOrderMapper.selectByOrderNo(orderNo);
        if (shopEaseMallOrder == null) {
            ShopEaseMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        if (!userId.equals(shopEaseMallOrder.getUserId())) {
            ShopEaseMallException.fail(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        List<ShopEaseMallOrderItem> orderItems = shopEaseMallOrderItemMapper.selectByOrderId(shopEaseMallOrder.getOrderId());
        //获取订单项数据
        if (CollectionUtils.isEmpty(orderItems)) {
            ShopEaseMallException.fail(ServiceResultEnum.ORDER_ITEM_NOT_EXIST_ERROR.getResult());
        }
        List<ShopEaseMallOrderItemVO> shopEaseMallOrderItemVOS = BeanUtil.copyList(orderItems, ShopEaseMallOrderItemVO.class);
        ShopEaseMallOrderDetailVO shopEaseMallOrderDetailVO = new ShopEaseMallOrderDetailVO();
        BeanUtil.copyProperties(shopEaseMallOrder, shopEaseMallOrderDetailVO);
        shopEaseMallOrderDetailVO.setOrderStatusString(ShopEaseMallOrderStatusEnum.getShopEaseMallOrderStatusEnumByStatus(shopEaseMallOrderDetailVO.getOrderStatus()).getName());
        shopEaseMallOrderDetailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(shopEaseMallOrderDetailVO.getPayType()).getName());
        shopEaseMallOrderDetailVO.setShopEaseMallOrderItemVOS(shopEaseMallOrderItemVOS);
        return shopEaseMallOrderDetailVO;
    }


    @Override
    public PageResult getMyOrders(PageQueryUtil pageUtil) {
        int total = shopEaseMallOrderMapper.getTotalShopEaseMallOrders(pageUtil);
        List<ShopEaseMallOrder> shopEaseMallOrders = shopEaseMallOrderMapper.findShopEaseMallOrderList(pageUtil);
        List<ShopEaseMallOrderListVO> orderListVOS = new ArrayList<>();
        if (total > 0) {
            //数据转换 将实体类转成vo
            orderListVOS = BeanUtil.copyList(shopEaseMallOrders, ShopEaseMallOrderListVO.class);
            //设置订单状态中文显示值
            for (ShopEaseMallOrderListVO shopEaseMallOrderListVO : orderListVOS) {
                shopEaseMallOrderListVO.setOrderStatusString(ShopEaseMallOrderStatusEnum.getShopEaseMallOrderStatusEnumByStatus(shopEaseMallOrderListVO.getOrderStatus()).getName());
            }
            List<Long> orderIds = shopEaseMallOrders.stream().map(ShopEaseMallOrder::getOrderId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(orderIds)) {
                List<ShopEaseMallOrderItem> orderItems = shopEaseMallOrderItemMapper.selectByOrderIds(orderIds);
                Map<Long, List<ShopEaseMallOrderItem>> itemByOrderIdMap = orderItems.stream().collect(groupingBy(ShopEaseMallOrderItem::getOrderId));
                for (ShopEaseMallOrderListVO shopEaseMallOrderListVO : orderListVOS) {
                    //封装每个订单列表对象的订单项数据
                    if (itemByOrderIdMap.containsKey(shopEaseMallOrderListVO.getOrderId())) {
                        List<ShopEaseMallOrderItem> orderItemListTemp = itemByOrderIdMap.get(shopEaseMallOrderListVO.getOrderId());
                        //将ShopEaseMallOrderItem对象列表转换成ShopEaseMallOrderItemVO对象列表
                        List<ShopEaseMallOrderItemVO> shopEaseMallOrderItemVOS = BeanUtil.copyList(orderItemListTemp, ShopEaseMallOrderItemVO.class);
                        shopEaseMallOrderListVO.setShopEaseMallOrderItemVOS(shopEaseMallOrderItemVOS);
                    }
                }
            }
        }
        PageResult pageResult = new PageResult(orderListVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String cancelOrder(String orderNo, Long userId) {
        ShopEaseMallOrder shopEaseMallOrder = shopEaseMallOrderMapper.selectByOrderNo(orderNo);
        if (shopEaseMallOrder != null) {
            //验证是否是当前userId下的订单，否则报错
            if (!userId.equals(shopEaseMallOrder.getUserId())) {
                ShopEaseMallException.fail(ServiceResultEnum.NO_PERMISSION_ERROR.getResult());
            }
            //订单状态判断
            if (shopEaseMallOrder.getOrderStatus().intValue() == ShopEaseMallOrderStatusEnum.ORDER_SUCCESS.getOrderStatus()
                    || shopEaseMallOrder.getOrderStatus().intValue() == ShopEaseMallOrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()
                    || shopEaseMallOrder.getOrderStatus().intValue() == ShopEaseMallOrderStatusEnum.ORDER_CLOSED_BY_EXPIRED.getOrderStatus()
                    || shopEaseMallOrder.getOrderStatus().intValue() == ShopEaseMallOrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()) {
                return ServiceResultEnum.ORDER_STATUS_ERROR.getResult();
            }
            if (shopEaseMallOrderMapper.closeOrder(Collections.singletonList(shopEaseMallOrder.getOrderId()), ShopEaseMallOrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String finishOrder(String orderNo, Long userId) {
        ShopEaseMallOrder shopEaseMallOrder = shopEaseMallOrderMapper.selectByOrderNo(orderNo);
        if (shopEaseMallOrder != null) {
            //验证是否是当前userId下的订单，否则报错
            if (!userId.equals(shopEaseMallOrder.getUserId())) {
                return ServiceResultEnum.NO_PERMISSION_ERROR.getResult();
            }
            //订单状态判断 非出库状态下不进行修改操作
            if (shopEaseMallOrder.getOrderStatus().intValue() != ShopEaseMallOrderStatusEnum.ORDER_EXPRESS.getOrderStatus()) {
                return ServiceResultEnum.ORDER_STATUS_ERROR.getResult();
            }
            shopEaseMallOrder.setOrderStatus((byte) ShopEaseMallOrderStatusEnum.ORDER_SUCCESS.getOrderStatus());
            shopEaseMallOrder.setUpdateTime(new Date());
            if (shopEaseMallOrderMapper.updateByPrimaryKeySelective(shopEaseMallOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String paySuccess(String orderNo, int payType) {
        ShopEaseMallOrder shopEaseMallOrder = shopEaseMallOrderMapper.selectByOrderNo(orderNo);
        if (shopEaseMallOrder != null) {
            //订单状态判断 非待支付状态下不进行修改操作
            if (shopEaseMallOrder.getOrderStatus().intValue() != ShopEaseMallOrderStatusEnum.ORDER_PRE_PAY.getOrderStatus()) {
                return ServiceResultEnum.ORDER_STATUS_ERROR.getResult();
            }
            shopEaseMallOrder.setOrderStatus((byte) ShopEaseMallOrderStatusEnum.ORDER_PAID.getOrderStatus());
            shopEaseMallOrder.setPayType((byte) payType);
            shopEaseMallOrder.setPayStatus((byte) PayStatusEnum.PAY_SUCCESS.getPayStatus());
            shopEaseMallOrder.setPayTime(new Date());
            shopEaseMallOrder.setUpdateTime(new Date());
            if (shopEaseMallOrderMapper.updateByPrimaryKeySelective(shopEaseMallOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    @Transactional
    @GlobalTransactional
    public String saveOrder(Long mallUserId, MallUserAddress address, List<Long> cartItemIds) {
        //调用购物车服务feign获取数据
        Result<List<ShopEaseMallShoppingCartItemDTO>> cartItemDTOListResult = shopCartService.listByCartItemIds(cartItemIds);
        if (cartItemDTOListResult == null || cartItemDTOListResult.getResultCode() != 200) {
            ShopEaseMallException.fail("参数异常");
        }
        List<ShopEaseMallShoppingCartItemDTO> itemsForSave = cartItemDTOListResult.getData();
        if (CollectionUtils.isEmpty(itemsForSave)) {
            //无数据
            ShopEaseMallException.fail("参数异常");
        }
        List<Long> itemIdList = itemsForSave.stream().map(ShopEaseMallShoppingCartItemDTO::getCartItemId).collect(Collectors.toList());
        List<Long> goodsIds = itemsForSave.stream().map(ShopEaseMallShoppingCartItemDTO::getGoodsId).collect(Collectors.toList());
        //调用商品服务feign获取数据
        Result<List<ShopEaseMallGoodsDTO>> goodsDTOListResult = goodsService.listByGoodsIds(goodsIds);
        if (goodsDTOListResult == null || goodsDTOListResult.getResultCode() != 200) {
            ShopEaseMallException.fail("参数异常");
        }
        List<ShopEaseMallGoodsDTO> shopEaseMallGoods = goodsDTOListResult.getData();
        //检查是否包含已下架商品
        List<ShopEaseMallGoodsDTO> goodsListNotSelling = shopEaseMallGoods.stream()
                .filter(shopEaseMallGoodsTemp -> shopEaseMallGoodsTemp.getGoodsSellStatus() != 0)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(goodsListNotSelling)) {
            //goodsListNotSelling 对象非空则表示有下架商品
            ShopEaseMallException.fail(goodsListNotSelling.get(0).getGoodsName() + "已下架，无法生成订单");
        }
        Map<Long, ShopEaseMallGoodsDTO> shopEaseMallGoodsMap = shopEaseMallGoods.stream().collect(Collectors.toMap(ShopEaseMallGoodsDTO::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
        //判断商品库存
        for (ShopEaseMallShoppingCartItemDTO cartItemDTO : itemsForSave) {
            //查出的商品中不存在购物车中的这条关联商品数据，直接返回错误提醒
            if (!shopEaseMallGoodsMap.containsKey(cartItemDTO.getGoodsId())) {
                ShopEaseMallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
            }
            //存在数量大于库存的情况，直接返回错误提醒
            if (cartItemDTO.getGoodsCount() > shopEaseMallGoodsMap.get(cartItemDTO.getGoodsId()).getStockNum()) {
                ShopEaseMallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
            }
        }
        //删除购物项
        if (!CollectionUtils.isEmpty(itemIdList) && !CollectionUtils.isEmpty(goodsIds) && !CollectionUtils.isEmpty(shopEaseMallGoods)) {

            //调用购物车服务feign删除数据
            Result<Boolean> deleteByCartItemIdsResult = shopCartService.deleteByCartItemIds(itemIdList);
            if (deleteByCartItemIdsResult != null && deleteByCartItemIdsResult.getResultCode() == 200) {


                List<StockNumDTO> stockNumDTOS = BeanUtil.copyList(itemsForSave, StockNumDTO.class);
                UpdateStockNumDTO updateStockNumDTO = new UpdateStockNumDTO();
                updateStockNumDTO.setStockNumDTOS(stockNumDTOS);

                //调用商品服务feign修改库存数据
                Result<Boolean> updateStockResult = goodsService.updateStock(updateStockNumDTO);
                if (updateStockResult == null || updateStockResult.getResultCode() != 200) {
                    ShopEaseMallException.fail(ServiceResultEnum.PARAM_ERROR.getResult());
                }
                if (!updateStockResult.getData()) {
                    ShopEaseMallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
                }
                //生成订单号
                String orderNo = NumberUtil.genOrderNo();
                int priceTotal = 0;
                //保存订单
                ShopEaseMallOrder shopEaseMallOrder = new ShopEaseMallOrder();
                shopEaseMallOrder.setOrderNo(orderNo);
                shopEaseMallOrder.setUserId(mallUserId);
                //总价
                for (ShopEaseMallShoppingCartItemDTO cartItemDTO : itemsForSave) {
                    priceTotal += cartItemDTO.getGoodsCount() * shopEaseMallGoodsMap.get(cartItemDTO.getGoodsId()).getSellingPrice();
                }
                if (priceTotal < 1) {
                    ShopEaseMallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                shopEaseMallOrder.setTotalPrice(priceTotal);
                String extraInfo = "";
                shopEaseMallOrder.setExtraInfo(extraInfo);
                //生成订单项并保存订单项纪录
                if (shopEaseMallOrderMapper.insertSelective(shopEaseMallOrder) > 0) {
                    //生成订单收货地址快照，并保存至数据库
                    ShopEaseMallOrderAddress shopEaseMallOrderAddress = new ShopEaseMallOrderAddress();
                    BeanUtil.copyProperties(address, shopEaseMallOrderAddress);
                    shopEaseMallOrderAddress.setOrderId(shopEaseMallOrder.getOrderId());
                    //生成所有的订单项快照，并保存至数据库
                    List<ShopEaseMallOrderItem> shopEaseMallOrderItems = new ArrayList<>();
                    for (ShopEaseMallShoppingCartItemDTO cartItemDTO : itemsForSave) {
                        ShopEaseMallOrderItem shopEaseMallOrderItem = new ShopEaseMallOrderItem();
                        //使用BeanUtil工具类将cartItemDTO中的属性复制到shopEaseMallOrderItem对象中
                        BeanUtil.copyProperties(cartItemDTO, shopEaseMallOrderItem);
                        shopEaseMallOrderItem.setGoodsCoverImg(shopEaseMallGoodsMap.get(cartItemDTO.getGoodsId()).getGoodsCoverImg());
                        shopEaseMallOrderItem.setGoodsName(shopEaseMallGoodsMap.get(cartItemDTO.getGoodsId()).getGoodsName());
                        shopEaseMallOrderItem.setSellingPrice(shopEaseMallGoodsMap.get(cartItemDTO.getGoodsId()).getSellingPrice());
                        //ShopEaseMallOrderMapper文件insert()方法中使用了useGeneratedKeys因此orderId可以获取到
                        shopEaseMallOrderItem.setOrderId(shopEaseMallOrder.getOrderId());
                        shopEaseMallOrderItems.add(shopEaseMallOrderItem);
                    }
                    //保存至数据库
                    if (shopEaseMallOrderItemMapper.insertBatch(shopEaseMallOrderItems) > 0 && shopEaseMallOrderAddressMapper.insertSelective(shopEaseMallOrderAddress) > 0) {
                        //所有操作成功后，将订单号返回，以供Controller方法跳转到订单详情
                        return orderNo;
                    }
                    ShopEaseMallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                ShopEaseMallException.fail(ServiceResultEnum.DB_ERROR.getResult());
            }
            ShopEaseMallException.fail(ServiceResultEnum.DB_ERROR.getResult());
        }
        ShopEaseMallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        return ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult();
    }


    @Override
    public PageResult getShopEaseMallOrdersPage(PageQueryUtil pageUtil) {
        List<ShopEaseMallOrder> shopEaseMallOrders = shopEaseMallOrderMapper.findShopEaseMallOrderList(pageUtil);
        int total = shopEaseMallOrderMapper.getTotalShopEaseMallOrders(pageUtil);
        PageResult pageResult = new PageResult(shopEaseMallOrders, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    @Transactional
    public String updateOrderInfo(ShopEaseMallOrder shopEaseMallOrder) {
        ShopEaseMallOrder temp = shopEaseMallOrderMapper.selectByPrimaryKey(shopEaseMallOrder.getOrderId());
        //不为空且orderStatus>=0且状态为出库之前可以修改部分信息
        if (temp != null && temp.getOrderStatus() >= 0 && temp.getOrderStatus() < 3) {
            temp.setTotalPrice(shopEaseMallOrder.getTotalPrice());
            temp.setUpdateTime(new Date());
            if (shopEaseMallOrderMapper.updateByPrimaryKeySelective(temp) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            }
            return ServiceResultEnum.DB_ERROR.getResult();
        }
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkDone(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<ShopEaseMallOrder> orders = shopEaseMallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (ShopEaseMallOrder shopEaseMallOrder : orders) {
                if (shopEaseMallOrder.getIsDeleted() == 1) {
                    errorOrderNos += shopEaseMallOrder.getOrderNo() + " ";
                    continue;
                }
                if (shopEaseMallOrder.getOrderStatus() != 1) {
                    errorOrderNos += shopEaseMallOrder.getOrderNo() + " ";
                }
            }
            if (!StringUtils.hasText(errorOrderNos)) {
                //订单状态正常 可以执行配货完成操作 修改订单状态和更新时间
                if (shopEaseMallOrderMapper.checkDone(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行出库操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单的状态不是支付成功无法执行出库操作";
                } else {
                    return "你选择了太多状态不是支付成功的订单，无法执行配货完成操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkOut(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<ShopEaseMallOrder> orders = shopEaseMallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (ShopEaseMallOrder shopEaseMallOrder : orders) {
                if (shopEaseMallOrder.getIsDeleted() == 1) {
                    errorOrderNos += shopEaseMallOrder.getOrderNo() + " ";
                    continue;
                }
                if (shopEaseMallOrder.getOrderStatus() != 1 && shopEaseMallOrder.getOrderStatus() != 2) {
                    errorOrderNos += shopEaseMallOrder.getOrderNo() + " ";
                }
            }
            if (!StringUtils.hasText(errorOrderNos)) {
                //订单状态正常 可以执行出库操作 修改订单状态和更新时间
                if (shopEaseMallOrderMapper.checkOut(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行出库操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单的状态不是支付成功或配货完成无法执行出库操作";
                } else {
                    return "你选择了太多状态不是支付成功或配货完成的订单，无法执行出库操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String closeOrder(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<ShopEaseMallOrder> orders = shopEaseMallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (ShopEaseMallOrder shopEaseMallOrder : orders) {
                // isDeleted=1 一定为已关闭订单
                if (shopEaseMallOrder.getIsDeleted() == 1) {
                    errorOrderNos += shopEaseMallOrder.getOrderNo() + " ";
                    continue;
                }
                //已关闭或者已完成无法关闭订单
                if (shopEaseMallOrder.getOrderStatus() == 4 || shopEaseMallOrder.getOrderStatus() < 0) {
                    errorOrderNos += shopEaseMallOrder.getOrderNo() + " ";
                }
            }
            if (!StringUtils.hasText(errorOrderNos)) {
                //订单状态正常 可以执行关闭操作 修改订单状态和更新时间
                if (shopEaseMallOrderMapper.closeOrder(Arrays.asList(ids), ShopEaseMallOrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行关闭操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单不能执行关闭操作";
                } else {
                    return "你选择的订单不能执行关闭操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    public List<ShopEaseMallOrderItemVO> getOrderItems(Long orderId) {
        ShopEaseMallOrder shopEaseMallOrder = shopEaseMallOrderMapper.selectByPrimaryKey(orderId);
        if (shopEaseMallOrder != null) {
            List<ShopEaseMallOrderItem> orderItems = shopEaseMallOrderItemMapper.selectByOrderId(shopEaseMallOrder.getOrderId());
            //获取订单项数据
            if (!CollectionUtils.isEmpty(orderItems)) {
                List<ShopEaseMallOrderItemVO> shopEaseMallOrderItemVOS = BeanUtil.copyList(orderItems, ShopEaseMallOrderItemVO.class);
                return shopEaseMallOrderItemVOS;
            }
        }
        return null;
    }
}
