 
package ltd.order.cloud.shopease.dao;

import ltd.common.cloud.shopease.dto.PageQueryUtil;
import ltd.order.cloud.shopease.entity.ShopEaseMallOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShopEaseMallOrderMapper {
    int deleteByPrimaryKey(Long orderId);

    int insert(ShopEaseMallOrder record);

    int insertSelective(ShopEaseMallOrder record);

    ShopEaseMallOrder selectByPrimaryKey(Long orderId);

    ShopEaseMallOrder selectByOrderNo(String orderNo);

    int updateByPrimaryKeySelective(ShopEaseMallOrder record);

    int updateByPrimaryKey(ShopEaseMallOrder record);

    List<ShopEaseMallOrder> findShopEaseMallOrderList(PageQueryUtil pageUtil);

    int getTotalShopEaseMallOrders(PageQueryUtil pageUtil);

    List<ShopEaseMallOrder> selectByPrimaryKeys(@Param("orderIds") List<Long> orderIds);

    int checkOut(@Param("orderIds") List<Long> orderIds);

    int closeOrder(@Param("orderIds") List<Long> orderIds, @Param("orderStatus") int orderStatus);

    int checkDone(@Param("orderIds") List<Long> asList);
}