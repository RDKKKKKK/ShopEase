/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2022 程序员十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package ltd.order.cloud.newbee.dao;


import ltd.order.cloud.newbee.entity.NewBeeMallOrderAddress;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NewBeeMallOrderAddressMapper {
    int deleteByPrimaryKey(Long orderId);

    int insert(NewBeeMallOrderAddress record);

    int insertSelective(NewBeeMallOrderAddress record);

    NewBeeMallOrderAddress selectByPrimaryKey(Long orderId);

    int updateByPrimaryKeySelective(NewBeeMallOrderAddress record);

    int updateByPrimaryKey(NewBeeMallOrderAddress record);
}