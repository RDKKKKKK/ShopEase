 
package ltd.goods.cloud.shopease.service.impl;

import ltd.common.cloud.shopease.enums.ShopEaseMallCategoryLevelEnum;
import ltd.common.cloud.shopease.enums.ServiceResultEnum;
import ltd.common.cloud.shopease.dto.PageQueryUtil;
import ltd.common.cloud.shopease.dto.PageResult;
import ltd.common.cloud.shopease.exception.ShopEaseMallException;
import ltd.common.cloud.shopease.util.BeanUtil;
import ltd.goods.cloud.shopease.controller.vo.ShopEaseMallSearchGoodsVO;
import ltd.goods.cloud.shopease.dao.GoodsCategoryMapper;
import ltd.goods.cloud.shopease.dao.ShopEaseMallGoodsMapper;
import ltd.goods.cloud.shopease.entity.GoodsCategory;
import ltd.goods.cloud.shopease.entity.ShopEaseMallGoods;
import ltd.goods.cloud.shopease.entity.StockNumDTO;
import ltd.goods.cloud.shopease.service.ShopEaseMallGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ShopEaseMallGoodsServiceImpl implements ShopEaseMallGoodsService {

    @Autowired
    private ShopEaseMallGoodsMapper goodsMapper;
    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;

    @Override
    public PageResult getShopEaseMallGoodsPage(PageQueryUtil pageUtil) {
        List<ShopEaseMallGoods> goodsList = goodsMapper.findShopEaseMallGoodsList(pageUtil);
        int total = goodsMapper.getTotalShopEaseMallGoods(pageUtil);
        PageResult pageResult = new PageResult(goodsList, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String saveShopEaseMallGoods(ShopEaseMallGoods goods) {
        GoodsCategory goodsCategory = goodsCategoryMapper.selectByPrimaryKey(goods.getGoodsCategoryId());
        // 分类不存在或者不是三级分类，则该参数字段异常
        if (goodsCategory == null || goodsCategory.getCategoryLevel().intValue() != ShopEaseMallCategoryLevelEnum.LEVEL_THREE.getLevel()) {
            return ServiceResultEnum.GOODS_CATEGORY_ERROR.getResult();
        }
        if (goodsMapper.selectByCategoryIdAndName(goods.getGoodsName(), goods.getGoodsCategoryId()) != null) {
            return ServiceResultEnum.SAME_GOODS_EXIST.getResult();
        }
        if (goodsMapper.insertSelective(goods) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public void batchSaveShopEaseMallGoods(List<ShopEaseMallGoods> shopEaseMallGoodsList) {
        if (!CollectionUtils.isEmpty(shopEaseMallGoodsList)) {
            goodsMapper.batchInsert(shopEaseMallGoodsList);
        }
    }

    @Override
    public String updateShopEaseMallGoods(ShopEaseMallGoods goods) {
        GoodsCategory goodsCategory = goodsCategoryMapper.selectByPrimaryKey(goods.getGoodsCategoryId());
        // 分类不存在或者不是三级分类，则该参数字段异常
        if (goodsCategory == null || goodsCategory.getCategoryLevel().intValue() != ShopEaseMallCategoryLevelEnum.LEVEL_THREE.getLevel()) {
            return ServiceResultEnum.GOODS_CATEGORY_ERROR.getResult();
        }
        ShopEaseMallGoods temp = goodsMapper.selectByPrimaryKey(goods.getGoodsId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        ShopEaseMallGoods temp2 = goodsMapper.selectByCategoryIdAndName(goods.getGoodsName(), goods.getGoodsCategoryId());
        if (temp2 != null && !temp2.getGoodsId().equals(goods.getGoodsId())) {
            //name和分类id相同且不同id 不能继续修改
            return ServiceResultEnum.SAME_GOODS_EXIST.getResult();
        }
        goods.setUpdateTime(new Date());
        if (goodsMapper.updateByPrimaryKeySelective(goods) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public ShopEaseMallGoods getShopEaseMallGoodsById(Long id) {
        ShopEaseMallGoods shopEaseMallGoods = goodsMapper.selectByPrimaryKey(id);
        if (shopEaseMallGoods == null) {
            ShopEaseMallException.fail(ServiceResultEnum.GOODS_NOT_EXIST.getResult());
        }
        return shopEaseMallGoods;
    }

    @Override
    public List<ShopEaseMallGoods> getShopEaseMallGoodsByIds(List<Long> goodsIds) {
        return goodsMapper.selectByPrimaryKeys(goodsIds);
    }

    @Override
    public Boolean batchUpdateSellStatus(Long[] ids, int sellStatus) {
        return goodsMapper.batchUpdateSellStatus(ids, sellStatus) > 0;
    }


    @Override
    public PageResult searchShopEaseMallGoods(PageQueryUtil pageUtil) {
        List<ShopEaseMallGoods> goodsList = goodsMapper.findShopEaseMallGoodsListBySearch(pageUtil);
        int total = goodsMapper.getTotalShopEaseMallGoodsBySearch(pageUtil);
        List<ShopEaseMallSearchGoodsVO> shopEaseMallSearchGoodsVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsList)) {
            shopEaseMallSearchGoodsVOS = BeanUtil.copyList(goodsList, ShopEaseMallSearchGoodsVO.class);
            for (ShopEaseMallSearchGoodsVO shopEaseMallSearchGoodsVO : shopEaseMallSearchGoodsVOS) {
                String goodsName = shopEaseMallSearchGoodsVO.getGoodsName();
                String goodsIntro = shopEaseMallSearchGoodsVO.getGoodsIntro();
                // 字符串过长导致文字超出的问题
                if (goodsName.length() > 28) {
                    goodsName = goodsName.substring(0, 28) + "...";
                    shopEaseMallSearchGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 30) {
                    goodsIntro = goodsIntro.substring(0, 30) + "...";
                    shopEaseMallSearchGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        PageResult pageResult = new PageResult(shopEaseMallSearchGoodsVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public Boolean updateStockNum(List<StockNumDTO> stockNumDTOS) {
        return goodsMapper.updateStockNum(stockNumDTOS) > 0;
    }
}
