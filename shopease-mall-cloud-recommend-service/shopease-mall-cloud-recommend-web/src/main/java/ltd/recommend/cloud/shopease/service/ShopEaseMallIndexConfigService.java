 
package ltd.recommend.cloud.shopease.service;

import ltd.common.cloud.shopease.dto.PageQueryUtil;
import ltd.common.cloud.shopease.dto.PageResult;
import ltd.recommend.cloud.shopease.controller.vo.ShopEaseMallIndexConfigGoodsVO;
import ltd.recommend.cloud.shopease.entity.IndexConfig;

import java.util.List;

public interface ShopEaseMallIndexConfigService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getConfigsPage(PageQueryUtil pageUtil);

    String saveIndexConfig(IndexConfig indexConfig);

    String updateIndexConfig(IndexConfig indexConfig);

    IndexConfig getIndexConfigById(Long id);

    Boolean deleteBatch(Long[] ids);

    /**
     * 返回固定数量的首页配置商品对象(首页调用)
     *
     * @param number
     * @return
     */
    List<ShopEaseMallIndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number);
}
