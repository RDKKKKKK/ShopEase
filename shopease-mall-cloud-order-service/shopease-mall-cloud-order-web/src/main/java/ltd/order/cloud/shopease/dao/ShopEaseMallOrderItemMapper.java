 
package ltd.order.cloud.shopease.dao;

import ltd.order.cloud.shopease.entity.ShopEaseMallOrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShopEaseMallOrderItemMapper {
    int deleteByPrimaryKey(Long orderItemId);

    int insert(ShopEaseMallOrderItem record);

    int insertSelective(ShopEaseMallOrderItem record);

    ShopEaseMallOrderItem selectByPrimaryKey(Long orderItemId);

    /**
     * 根据订单id获取订单项列表
     *
     * @param orderId
     * @return
     */
    List<ShopEaseMallOrderItem> selectByOrderId(Long orderId);

    /**
     * 根据订单ids获取订单项列表
     *
     * @param orderIds
     * @return
     */
    List<ShopEaseMallOrderItem> selectByOrderIds(@Param("orderIds") List<Long> orderIds);

    /**
     * 批量insert订单项数据
     *
     * @param orderItems
     * @return
     */
    int insertBatch(@Param("orderItems") List<ShopEaseMallOrderItem> orderItems);

    int updateByPrimaryKeySelective(ShopEaseMallOrderItem record);

    int updateByPrimaryKey(ShopEaseMallOrderItem record);
}