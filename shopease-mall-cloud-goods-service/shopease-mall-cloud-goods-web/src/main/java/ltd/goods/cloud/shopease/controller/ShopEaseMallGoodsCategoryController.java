 
package ltd.goods.cloud.shopease.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ltd.common.cloud.shopease.enums.ServiceResultEnum;
import ltd.common.cloud.shopease.dto.Result;
import ltd.common.cloud.shopease.dto.ResultGenerator;
import ltd.common.cloud.shopease.exception.ShopEaseMallException;
import ltd.goods.cloud.shopease.controller.vo.ShopEaseMallIndexCategoryVO;
import ltd.goods.cloud.shopease.service.ShopEaseMallCategoryService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(value = "v1", tags = "新蜂商城分类页面接口")
@RequestMapping("/categories/mall")
public class ShopEaseMallGoodsCategoryController {

    @Resource
    private ShopEaseMallCategoryService shopEaseMallCategoryService;

    @GetMapping("/listAll")
    @ApiOperation(value = "获取分类数据", notes = "分类页面使用")
    public Result<List<ShopEaseMallIndexCategoryVO>> getCategories() {
        List<ShopEaseMallIndexCategoryVO> categories = shopEaseMallCategoryService.getCategoriesForIndex();
        if (CollectionUtils.isEmpty(categories)) {
            ShopEaseMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return ResultGenerator.genSuccessResult(categories);
    }
}
