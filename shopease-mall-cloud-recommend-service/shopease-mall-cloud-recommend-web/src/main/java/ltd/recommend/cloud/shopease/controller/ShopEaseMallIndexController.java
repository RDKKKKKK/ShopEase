 
package ltd.recommend.cloud.shopease.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ltd.common.cloud.shopease.dto.Result;
import ltd.common.cloud.shopease.dto.ResultGenerator;
import ltd.common.cloud.shopease.enums.IndexConfigTypeEnum;
import ltd.recommend.cloud.shopease.controller.vo.IndexInfoVO;
import ltd.recommend.cloud.shopease.controller.vo.ShopEaseMallIndexCarouselVO;
import ltd.recommend.cloud.shopease.controller.vo.ShopEaseMallIndexConfigGoodsVO;
import ltd.recommend.cloud.shopease.service.ShopEaseMallCarouselService;
import ltd.recommend.cloud.shopease.service.ShopEaseMallIndexConfigService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(value = "v1", tags = "新蜂商城首页接口")
@RequestMapping("/mall/index")
public class ShopEaseMallIndexController {

    @Resource
    private ShopEaseMallCarouselService shopEaseMallCarouselService;

    @Resource
    private ShopEaseMallIndexConfigService shopEaseMallIndexConfigService;

    @GetMapping("/recommondInfos")
    @ApiOperation(value = "获取首页数据", notes = "轮播图、新品、推荐等")
    public Result<IndexInfoVO> indexInfo() {
        IndexInfoVO indexInfoVO = new IndexInfoVO();
        List<ShopEaseMallIndexCarouselVO> carousels = shopEaseMallCarouselService.getCarouselsForIndex(5);
        List<ShopEaseMallIndexConfigGoodsVO> hotGoodses = shopEaseMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(), 4);
        List<ShopEaseMallIndexConfigGoodsVO> newGoodses = shopEaseMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_NEW.getType(), 6);
        List<ShopEaseMallIndexConfigGoodsVO> recommendGoodses = shopEaseMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_RECOMMOND.getType(), 10);
        indexInfoVO.setCarousels(carousels);
        indexInfoVO.setHotGoodses(hotGoodses);
        indexInfoVO.setNewGoodses(newGoodses);
        indexInfoVO.setRecommendGoodses(recommendGoodses);
        return ResultGenerator.genSuccessResult(indexInfoVO);
    }
}
