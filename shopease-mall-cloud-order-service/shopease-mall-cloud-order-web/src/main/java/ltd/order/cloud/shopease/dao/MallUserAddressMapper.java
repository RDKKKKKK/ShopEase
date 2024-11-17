 
package ltd.order.cloud.shopease.dao;

import ltd.order.cloud.shopease.entity.MallUserAddress;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MallUserAddressMapper {
    int deleteByPrimaryKey(Long addressId);

    int insert(MallUserAddress record);

    int insertSelective(MallUserAddress record);

    MallUserAddress selectByPrimaryKey(Long addressId);

    MallUserAddress getMyDefaultAddress(Long userId);

    List<MallUserAddress> findMyAddressList(Long userId);

    int updateByPrimaryKeySelective(MallUserAddress record);

    int updateByPrimaryKey(MallUserAddress record);
}