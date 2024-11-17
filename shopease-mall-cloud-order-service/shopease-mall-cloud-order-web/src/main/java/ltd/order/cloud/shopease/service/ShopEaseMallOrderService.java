 
package ltd.order.cloud.shopease.service;

import ltd.common.cloud.shopease.dto.PageQueryUtil;
import ltd.common.cloud.shopease.dto.PageResult;
import ltd.order.cloud.shopease.controller.vo.ShopEaseMallOrderDetailVO;
import ltd.order.cloud.shopease.controller.vo.ShopEaseMallOrderItemVO;
import ltd.order.cloud.shopease.entity.MallUserAddress;
import ltd.order.cloud.shopease.entity.ShopEaseMallOrder;

import java.util.List;

public interface ShopEaseMallOrderService {
    /**
     * 获取订单详情
     *
     * @param orderId
     * @return
     */
    ShopEaseMallOrderDetailVO getOrderDetailByOrderId(Long orderId);

    /**
     * 获取订单详情
     *
     * @param orderNo
     * @param userId
     * @return
     */
    ShopEaseMallOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId);

    /**
     * 我的订单列表
     *
     * @param pageUtil
     * @return
     */
    PageResult getMyOrders(PageQueryUtil pageUtil);

    /**
     * 手动取消订单
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String cancelOrder(String orderNo, Long userId);

    /**
     * 确认收货
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String finishOrder(String orderNo, Long userId);

    String paySuccess(String orderNo, int payType);

    /**
     * 生成订单
     * @param mallUserId
     * @param address
     * @param cartItemIds
     * @return
     */
    String saveOrder(Long mallUserId, MallUserAddress address, List<Long> cartItemIds);

    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getShopEaseMallOrdersPage(PageQueryUtil pageUtil);

    /**
     * 订单信息修改
     *
     * @param shopEaseMallOrder
     * @return
     */
    String updateOrderInfo(ShopEaseMallOrder shopEaseMallOrder);

    /**
     * 配货
     *
     * @param ids
     * @return
     */
    String checkDone(Long[] ids);

    /**
     * 出库
     *
     * @param ids
     * @return
     */
    String checkOut(Long[] ids);

    /**
     * 关闭订单
     *
     * @param ids
     * @return
     */
    String closeOrder(Long[] ids);

    List<ShopEaseMallOrderItemVO> getOrderItems(Long orderId);
}
