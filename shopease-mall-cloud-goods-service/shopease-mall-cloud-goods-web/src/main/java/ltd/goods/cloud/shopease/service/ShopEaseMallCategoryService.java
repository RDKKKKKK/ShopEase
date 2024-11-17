
package ltd.goods.cloud.shopease.service;

import ltd.common.cloud.shopease.dto.PageQueryUtil;
import ltd.common.cloud.shopease.dto.PageResult;
import ltd.goods.cloud.shopease.controller.vo.ShopEaseMallIndexCategoryVO;
import ltd.goods.cloud.shopease.entity.GoodsCategory;

import java.util.List;

public interface ShopEaseMallCategoryService {

    String saveCategory(GoodsCategory goodsCategory);

    String updateGoodsCategory(GoodsCategory goodsCategory);

    GoodsCategory getGoodsCategoryById(Long id);

    Boolean deleteBatch(Long[] ids);

    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getCategorisPage(PageQueryUtil pageUtil);

    /**
     * 根据parentId和level获取分类列表
     *
     * @param parentIds
     * @param categoryLevel
     * @return
     */
    List<GoodsCategory> selectByLevelAndParentIdsAndNumber(List<Long> parentIds, int categoryLevel);

    /**
     * 返回分类数据(首页调用)
     *
     * @return
     */
    List<ShopEaseMallIndexCategoryVO> getCategoriesForIndex();

}
