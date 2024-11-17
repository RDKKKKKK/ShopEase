
package ltd.goods.cloud.shopease.service;

import ltd.common.cloud.shopease.dto.PageQueryUtil;
import ltd.common.cloud.shopease.dto.PageResult;
import ltd.goods.cloud.shopease.entity.ShopEaseMallGoods;
import ltd.goods.cloud.shopease.entity.StockNumDTO;

import java.util.List;

public interface ShopEaseMallGoodsService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getShopEaseMallGoodsPage(PageQueryUtil pageUtil);

    /**
     * 添加商品
     *
     * @param goods
     * @return
     */
    String saveShopEaseMallGoods(ShopEaseMallGoods goods);

    /**
     * 批量新增商品数据
     *
     * @param shopEaseMallGoodsList
     * @return
     */
    void batchSaveShopEaseMallGoods(List<ShopEaseMallGoods> shopEaseMallGoodsList);

    /**
     * 修改商品信息
     *
     * @param goods
     * @return
     */
    String updateShopEaseMallGoods(ShopEaseMallGoods goods);

    /**
     * 批量修改销售状态(上架下架)
     *
     * @param ids
     * @return
     */
    Boolean batchUpdateSellStatus(Long[] ids, int sellStatus);

    /**
     * 获取商品详情
     *
     * @param id
     * @return
     */
    ShopEaseMallGoods getShopEaseMallGoodsById(Long id);

    /**
     * 获取商品数据
     *
     * @param goodsIds
     * @return
     */
    List<ShopEaseMallGoods> getShopEaseMallGoodsByIds(List<Long> goodsIds);

    /**
     * 商品搜索
     *
     * @param pageUtil
     * @return
     */
    PageResult searchShopEaseMallGoods(PageQueryUtil pageUtil);

    Boolean updateStockNum(List<StockNumDTO> stockNumDTOS);
}
