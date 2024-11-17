package ltd.goods.cloud.shopease.dao;

import ltd.common.cloud.shopease.dto.PageQueryUtil;
import ltd.goods.cloud.shopease.entity.ShopEaseMallGoods;
import ltd.goods.cloud.shopease.entity.StockNumDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShopEaseMallGoodsMapper {
    int deleteByPrimaryKey(Long goodsId);

    int insert(ShopEaseMallGoods record);

    int insertSelective(ShopEaseMallGoods record);

    ShopEaseMallGoods selectByPrimaryKey(Long goodsId);

    ShopEaseMallGoods selectByCategoryIdAndName(@Param("goodsName") String goodsName, @Param("goodsCategoryId") Long goodsCategoryId);

    int updateByPrimaryKeySelective(ShopEaseMallGoods record);

    int updateByPrimaryKeyWithBLOBs(ShopEaseMallGoods record);

    int updateByPrimaryKey(ShopEaseMallGoods record);

    List<ShopEaseMallGoods> findShopEaseMallGoodsList(PageQueryUtil pageUtil);

    int getTotalShopEaseMallGoods(PageQueryUtil pageUtil);

    List<ShopEaseMallGoods> selectByPrimaryKeys(List<Long> goodsIds);

    List<ShopEaseMallGoods> findShopEaseMallGoodsListBySearch(PageQueryUtil pageUtil);

    int getTotalShopEaseMallGoodsBySearch(PageQueryUtil pageUtil);

    int batchInsert(@Param("shopEaseMallGoodsList") List<ShopEaseMallGoods> shopEaseMallGoodsList);

    int updateStockNum(@Param("stockNumDTOS") List<StockNumDTO> stockNumDTOS);

    int batchUpdateSellStatus(@Param("orderIds")Long[] orderIds,@Param("sellStatus") int sellStatus);

}