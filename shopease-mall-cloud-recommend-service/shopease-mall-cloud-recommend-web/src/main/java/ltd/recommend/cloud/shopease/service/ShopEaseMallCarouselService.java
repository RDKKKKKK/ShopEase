 
package ltd.recommend.cloud.shopease.service;

import ltd.common.cloud.shopease.dto.PageQueryUtil;
import ltd.common.cloud.shopease.dto.PageResult;
import ltd.recommend.cloud.shopease.controller.vo.ShopEaseMallIndexCarouselVO;
import ltd.recommend.cloud.shopease.entity.Carousel;

import java.util.List;

public interface ShopEaseMallCarouselService {

    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getCarouselPage(PageQueryUtil pageUtil);

    String saveCarousel(Carousel carousel);

    String updateCarousel(Carousel carousel);

    Carousel getCarouselById(Integer id);

    Boolean deleteBatch(Long[] ids);

    /**
     * 返回固定数量的轮播图对象(首页调用)
     *
     * @param number
     * @return
     */
    List<ShopEaseMallIndexCarouselVO> getCarouselsForIndex(int number);
}
