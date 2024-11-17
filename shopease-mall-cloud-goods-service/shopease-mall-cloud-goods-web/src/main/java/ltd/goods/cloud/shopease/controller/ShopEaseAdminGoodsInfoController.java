 
package ltd.goods.cloud.shopease.controller;

import io.seata.core.context.RootContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import ltd.common.cloud.shopease.enums.ServiceResultEnum;
import ltd.common.cloud.shopease.dto.PageQueryUtil;
import ltd.common.cloud.shopease.dto.Result;
import ltd.common.cloud.shopease.dto.ResultGenerator;
import ltd.common.cloud.shopease.util.BeanUtil;
import ltd.goods.cloud.shopease.config.annotation.TokenToAdminUser;
import ltd.goods.cloud.shopease.controller.param.BatchIdParam;
import ltd.goods.cloud.shopease.controller.param.GoodsAddParam;
import ltd.goods.cloud.shopease.controller.param.GoodsEditParam;
import ltd.goods.cloud.shopease.entity.GoodsCategory;
import ltd.goods.cloud.shopease.entity.LoginAdminUser;
import ltd.goods.cloud.shopease.entity.ShopEaseMallGoods;
import ltd.goods.cloud.shopease.entity.UpdateStockNumDTO;
import ltd.goods.cloud.shopease.service.ShopEaseMallCategoryService;
import ltd.goods.cloud.shopease.service.ShopEaseMallGoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 13
 * @qq交流群 796794009
 * @email 2449207463@qq.com
 * @link https://github.com/shopease-ltd
 */
@RestController
@Api(value = "v1", tags = "后台管理系统商品模块接口")
@RequestMapping("/goods/admin")
public class ShopEaseAdminGoodsInfoController {

    private static final Logger logger = LoggerFactory.getLogger(ShopEaseAdminGoodsInfoController.class);

    @Resource
    private ShopEaseMallGoodsService shopEaseMallGoodsService;
    @Resource
    private ShopEaseMallCategoryService shopEaseMallCategoryService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation(value = "商品列表", notes = "可根据名称和上架状态筛选")
    public Result list(@RequestParam(required = false) @ApiParam(value = "页码") Integer pageNumber,
                       @RequestParam(required = false) @ApiParam(value = "每页条数") Integer pageSize,
                       @RequestParam(required = false) @ApiParam(value = "商品名称") String goodsName,
                       @RequestParam(required = false) @ApiParam(value = "上架状态 0-上架 1-下架") Integer goodsSellStatus, @TokenToAdminUser LoginAdminUser adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 10) {
            return ResultGenerator.genFailResult("分页参数异常！");
        }
        Map params = new HashMap(8);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        if (StringUtils.hasText(goodsName)) {
            params.put("goodsName", goodsName);
        }
        if (goodsSellStatus != null) {
            params.put("goodsSellStatus", goodsSellStatus);
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(shopEaseMallGoodsService.getShopEaseMallGoodsPage(pageUtil));
    }

    /**
     * 添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "新增商品信息", notes = "新增商品信息")
    public Result save(@RequestBody @Valid GoodsAddParam goodsAddParam, @TokenToAdminUser LoginAdminUser adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        ShopEaseMallGoods shopEaseMallGoods = new ShopEaseMallGoods();
        BeanUtil.copyProperties(goodsAddParam, shopEaseMallGoods);
        String result = shopEaseMallGoodsService.saveShopEaseMallGoods(shopEaseMallGoods);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }


    /**
     * 修改
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ApiOperation(value = "修改商品信息", notes = "修改商品信息")
    public Result update(@RequestBody @Valid GoodsEditParam goodsEditParam, @TokenToAdminUser LoginAdminUser adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        ShopEaseMallGoods shopEaseMallGoods = new ShopEaseMallGoods();
        BeanUtil.copyProperties(goodsEditParam, shopEaseMallGoods);
        String result = shopEaseMallGoodsService.updateShopEaseMallGoods(shopEaseMallGoods);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 详情
     */
    @GetMapping("/detail/{id}")
    @ApiOperation(value = "获取单条商品信息", notes = "根据id查询")
    public Result info(@PathVariable("id") Long id, @TokenToAdminUser LoginAdminUser adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        Map goodsInfo = new HashMap(8);
        ShopEaseMallGoods goods = shopEaseMallGoodsService.getShopEaseMallGoodsById(id);
        if (goods == null) {
            return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        goodsInfo.put("goods", goods);
        GoodsCategory thirdCategory;
        GoodsCategory secondCategory;
        GoodsCategory firstCategory;
        thirdCategory = shopEaseMallCategoryService.getGoodsCategoryById(goods.getGoodsCategoryId());
        if (thirdCategory != null) {
            goodsInfo.put("thirdCategory", thirdCategory);
            secondCategory = shopEaseMallCategoryService.getGoodsCategoryById(thirdCategory.getParentId());
            if (secondCategory != null) {
                goodsInfo.put("secondCategory", secondCategory);
                firstCategory = shopEaseMallCategoryService.getGoodsCategoryById(secondCategory.getParentId());
                if (firstCategory != null) {
                    goodsInfo.put("firstCategory", firstCategory);
                }
            }
        }
        return ResultGenerator.genSuccessResult(goodsInfo);
    }

    /**
     * 批量修改销售状态
     */
    @RequestMapping(value = "/updateStatus/{sellStatus}", method = RequestMethod.PUT)
    @ApiOperation(value = "批量修改销售状态", notes = "批量修改销售状态")
    public Result delete(@RequestBody BatchIdParam batchIdParam, @PathVariable("sellStatus") int sellStatus, @TokenToAdminUser LoginAdminUser adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        if (batchIdParam == null || batchIdParam.getIds().length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (sellStatus != 0 && sellStatus != 1) {
            return ResultGenerator.genFailResult("状态异常！");
        }
        if (shopEaseMallGoodsService.batchUpdateSellStatus(batchIdParam.getIds(), sellStatus)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("修改失败");
        }
    }

    /**
     * 详情
     */
    @GetMapping("/goodsDetail")
    @ApiOperation(value = "获取单条商品信息", notes = "根据id查询")
    public Result goodsDetail(@RequestParam("goodsId") Long goodsId) {
        ShopEaseMallGoods goods = shopEaseMallGoodsService.getShopEaseMallGoodsById(goodsId);
        return ResultGenerator.genSuccessResult(goods);
    }

    /**
     * 根据ids查询商品列表
     */
    @GetMapping("/listByGoodsIds")
    @ApiOperation(value = "根据ids查询商品列表", notes = "根据ids查询")
    public Result getShopEaseMallGoodsByIds(@RequestParam("goodsIds") List<Long> goodsIds) {
        List<ShopEaseMallGoods> shopEaseMallGoods = shopEaseMallGoodsService.getShopEaseMallGoodsByIds(goodsIds);
        return ResultGenerator.genSuccessResult(shopEaseMallGoods);
    }

    /**
     * 修改库存
     */
    @PutMapping("/updateStock")
    @ApiOperation(value = "修改库存", notes = "")
    public Result updateStock(@RequestBody UpdateStockNumDTO updateStockNumDTO) {
        System.out.println("RootContext.getXID()="+RootContext.getXID());
        return ResultGenerator.genSuccessResult(shopEaseMallGoodsService.updateStockNum(updateStockNumDTO.getStockNumDTOS()));
    }

}