 
package ltd.goods.cloud.shopease.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import ltd.common.cloud.shopease.enums.ServiceResultEnum;
import ltd.common.cloud.shopease.dto.PageQueryUtil;
import ltd.common.cloud.shopease.dto.PageResult;
import ltd.common.cloud.shopease.dto.Result;
import ltd.common.cloud.shopease.dto.ResultGenerator;
import ltd.common.cloud.shopease.exception.ShopEaseMallException;
import ltd.common.cloud.shopease.pojo.MallUserToken;
import ltd.common.cloud.shopease.util.BeanUtil;
import ltd.goods.cloud.shopease.config.annotation.TokenToMallUser;
import ltd.goods.cloud.shopease.controller.vo.ShopEaseMallGoodsDetailVO;
import ltd.goods.cloud.shopease.controller.vo.ShopEaseMallSearchGoodsVO;
import ltd.goods.cloud.shopease.entity.ShopEaseMallGoods;
import ltd.goods.cloud.shopease.service.ShopEaseMallGoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "v1", tags = "新蜂商城商品相关接口")
@RequestMapping("/goods/mall")
public class ShopEaseMallGoodsController {

    private static final Logger logger = LoggerFactory.getLogger(ShopEaseMallGoodsController.class);

    @Resource
    private ShopEaseMallGoodsService shopEaseMallGoodsService;

    @GetMapping("/test1")
    public Result<String> test1() throws BindException {
        throw new BindException(1,"BindException");
    }

    @GetMapping("/test2")
    public Result<String> test2() throws ShopEaseMallException {
        ShopEaseMallException.fail("ShopEaseMallException");
        return ResultGenerator.genSuccessResult("test2");
    }

    @GetMapping("/test3")
    public Result<String> test3() throws Exception {
        int i=1/0;
        return ResultGenerator.genSuccessResult("test2");
    }

    @GetMapping("/search")
    @ApiOperation(value = "商品搜索接口", notes = "根据关键字和分类id进行搜索")
    public Result<PageResult<List<ShopEaseMallSearchGoodsVO>>> search(@RequestParam(required = false) @ApiParam(value = "搜索关键字") String keyword,
                                                                    @RequestParam(required = false) @ApiParam(value = "分类id") Long goodsCategoryId,
                                                                    @RequestParam(required = false) @ApiParam(value = "orderBy") String orderBy,
                                                                    @RequestParam(required = false) @ApiParam(value = "页码") Integer pageNumber,
                                                                    @TokenToMallUser MallUserToken loginMallUserToken) {
        
        logger.info("goods search api,keyword={},goodsCategoryId={},orderBy={},pageNumber={},userId={}", keyword, goodsCategoryId, orderBy, pageNumber, loginMallUserToken.getUserId());

        Map params = new HashMap(8);
        //两个搜索参数都为空，直接返回异常
        if (goodsCategoryId == null && !StringUtils.hasText(keyword)) {
            ShopEaseMallException.fail("非法的搜索参数");
        }
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }
        params.put("goodsCategoryId", goodsCategoryId);
        params.put("page", pageNumber);
        params.put("limit", 10);
        //对keyword做过滤 去掉空格
        if (StringUtils.hasText(keyword)) {
            params.put("keyword", keyword);
        }
        if (StringUtils.hasText(orderBy)) {
            params.put("orderBy", orderBy);
        }
        //搜索上架状态下的商品
        params.put("goodsSellStatus", 0);
        //封装商品数据
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(shopEaseMallGoodsService.searchShopEaseMallGoods(pageUtil));
    }

    @GetMapping("/detail/{goodsId}")
    @ApiOperation(value = "商品详情接口", notes = "传参为商品id")
    public Result<ShopEaseMallGoodsDetailVO> goodsDetail(@ApiParam(value = "商品id") @PathVariable("goodsId") Long goodsId, @TokenToMallUser MallUserToken loginMallUserToken) {
        logger.info("goods detail api,goodsId={},userId={}", goodsId, loginMallUserToken.getUserId());
        if (goodsId < 1) {
            return ResultGenerator.genFailResult("参数异常");
        }
        ShopEaseMallGoods goods = shopEaseMallGoodsService.getShopEaseMallGoodsById(goodsId);
        if (0 != goods.getGoodsSellStatus()) {
            ShopEaseMallException.fail(ServiceResultEnum.GOODS_PUT_DOWN.getResult());
        }
        ShopEaseMallGoodsDetailVO goodsDetailVO = new ShopEaseMallGoodsDetailVO();
        BeanUtil.copyProperties(goods, goodsDetailVO);
        goodsDetailVO.setGoodsCarouselList(goods.getGoodsCarousel().split(","));
        return ResultGenerator.genSuccessResult(goodsDetailVO);
    }

}
