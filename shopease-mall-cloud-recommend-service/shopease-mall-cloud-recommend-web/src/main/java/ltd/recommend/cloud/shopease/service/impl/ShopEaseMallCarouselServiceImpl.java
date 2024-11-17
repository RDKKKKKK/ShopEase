 
package ltd.recommend.cloud.shopease.service.impl;

import ltd.common.cloud.shopease.enums.ServiceResultEnum;
import ltd.common.cloud.shopease.dto.PageQueryUtil;
import ltd.common.cloud.shopease.dto.PageResult;
import ltd.common.cloud.shopease.util.BeanUtil;
import ltd.recommend.cloud.shopease.controller.vo.ShopEaseMallIndexCarouselVO;
import ltd.recommend.cloud.shopease.dao.CarouselMapper;
import ltd.recommend.cloud.shopease.entity.Carousel;
import ltd.recommend.cloud.shopease.service.ShopEaseMallCarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ShopEaseMallCarouselServiceImpl implements ShopEaseMallCarouselService {

    @Autowired
    private CarouselMapper carouselMapper;


    @Override
    public PageResult getCarouselPage(PageQueryUtil pageUtil) {
        List<Carousel> carousels = carouselMapper.findCarouselList(pageUtil);
        int total = carouselMapper.getTotalCarousels(pageUtil);
        PageResult pageResult = new PageResult(carousels, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String saveCarousel(Carousel carousel) {
        if (carouselMapper.insertSelective(carousel) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateCarousel(Carousel carousel) {
        Carousel temp = carouselMapper.selectByPrimaryKey(carousel.getCarouselId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        temp.setCarouselRank(carousel.getCarouselRank());
        temp.setRedirectUrl(carousel.getRedirectUrl());
        temp.setCarouselUrl(carousel.getCarouselUrl());
        temp.setUpdateTime(new Date());
        if (carouselMapper.updateByPrimaryKeySelective(temp) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public Carousel getCarouselById(Integer id) {
        return carouselMapper.selectByPrimaryKey(id);
    }

    @Override
    public Boolean deleteBatch(Long[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除数据
        return carouselMapper.deleteBatch(ids) > 0;
    }

    @Override
    public List<ShopEaseMallIndexCarouselVO> getCarouselsForIndex(int number) {
        List<ShopEaseMallIndexCarouselVO> shopEaseMallIndexCarouselVOS = new ArrayList<>(number);
        List<Carousel> carousels = carouselMapper.findCarouselsByNum(number);
        if (!CollectionUtils.isEmpty(carousels)) {
            shopEaseMallIndexCarouselVOS = BeanUtil.copyList(carousels, ShopEaseMallIndexCarouselVO.class);
        }
        return shopEaseMallIndexCarouselVOS;
    }

}
