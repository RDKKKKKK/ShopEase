 
package ltd.order.cloud.shopease.dao;


import ltd.order.cloud.shopease.entity.ShopEaseMallOrderAddress;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShopEaseMallOrderAddressMapper {
    int deleteByPrimaryKey(Long orderId);

    int insert(ShopEaseMallOrderAddress record);

    int insertSelective(ShopEaseMallOrderAddress record);

    ShopEaseMallOrderAddress selectByPrimaryKey(Long orderId);

    int updateByPrimaryKeySelective(ShopEaseMallOrderAddress record);

    int updateByPrimaryKey(ShopEaseMallOrderAddress record);
}