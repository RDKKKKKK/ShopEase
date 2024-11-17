 
package ltd.user.cloud.shopease.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import ltd.common.cloud.shopease.enums.ServiceResultEnum;
import ltd.common.cloud.shopease.dto.Result;
import ltd.common.cloud.shopease.dto.ResultGenerator;
import ltd.common.cloud.shopease.pojo.MallUserToken;
import ltd.common.cloud.shopease.util.BeanUtil;
import ltd.common.cloud.shopease.util.NumberUtil;
import ltd.user.cloud.shopease.config.annotation.TokenToMallUser;
import ltd.user.cloud.shopease.controller.param.MallUserLoginParam;
import ltd.user.cloud.shopease.controller.param.MallUserRegisterParam;
import ltd.user.cloud.shopease.controller.param.MallUserUpdateParam;
import ltd.user.cloud.shopease.controller.vo.ShopEaseMallUserVO;
import ltd.user.cloud.shopease.entity.MallUser;
import ltd.user.cloud.shopease.service.ShopEaseMallUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@Api(value = "v1", tags = "新蜂商城用户操作相关接口")
@RequestMapping("/users/mall")
public class ShopEaseMallCloudPersonalController {

    @Resource
    private ShopEaseMallUserService shopEaseMallUserService;

    private static final Logger logger = LoggerFactory.getLogger(ShopEaseMallCloudPersonalController.class);

    @PostMapping("/login")
    @ApiOperation(value = "登录接口", notes = "返回token")
    public Result<String> login(@RequestBody @Valid MallUserLoginParam mallUserLoginParam) {
        if (!NumberUtil.isPhone(mallUserLoginParam.getLoginName())){
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_NAME_IS_NOT_PHONE.getResult());
        }
        String loginResult = shopEaseMallUserService.login(mallUserLoginParam.getLoginName(), mallUserLoginParam.getPasswordMd5());

        logger.info("login api,loginName={},loginResult={}", mallUserLoginParam.getLoginName(), loginResult);

        //登录成功
        if (StringUtils.hasText(loginResult) && loginResult.length() == 32) {
            Result result = ResultGenerator.genSuccessResult();
            result.setData(loginResult);
            return result;
        }
        //登录失败
        return ResultGenerator.genFailResult(loginResult);
    }


    @PostMapping("/logout")
    @ApiOperation(value = "登出接口", notes = "清除token")
    public Result<String> logout(@TokenToMallUser MallUserToken loginMallUserToken) {
        Boolean logoutResult = shopEaseMallUserService.logout(loginMallUserToken.getToken());

        logger.info("logout api,loginMallUser={}", loginMallUserToken.getUserId());

        //登出成功
        if (logoutResult) {
            return ResultGenerator.genSuccessResult();
        }
        //登出失败
        return ResultGenerator.genFailResult("logout error");
    }


    @PostMapping("/register")
    @ApiOperation(value = "用户注册", notes = "")
    public Result register(@RequestBody @Valid MallUserRegisterParam mallUserRegisterParam) {
        if (!NumberUtil.isPhone(mallUserRegisterParam.getLoginName())){
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_NAME_IS_NOT_PHONE.getResult());
        }
        String registerResult = shopEaseMallUserService.register(mallUserRegisterParam.getLoginName(), mallUserRegisterParam.getPassword());

        logger.info("register api,loginName={},loginResult={}", mallUserRegisterParam.getLoginName(), registerResult);

        //注册成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(registerResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //注册失败
        return ResultGenerator.genFailResult(registerResult);
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改用户信息", notes = "")
    public Result updateInfo(@RequestBody @ApiParam("用户信息") MallUserUpdateParam mallUserUpdateParam, @TokenToMallUser MallUserToken loginMallUserToken) {
        Boolean flag = shopEaseMallUserService.updateUserInfo(mallUserUpdateParam, loginMallUserToken.getUserId());
        if (flag) {
            //返回成功
            Result result = ResultGenerator.genSuccessResult();
            return result;
        } else {
            //返回失败
            Result result = ResultGenerator.genFailResult("修改失败");
            return result;
        }
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取用户信息", notes = "")
    public Result<ShopEaseMallUserVO> getUserDetail(@TokenToMallUser MallUserToken loginMallUserToken) {
        ShopEaseMallUserVO mallUserVO = new ShopEaseMallUserVO();
        MallUser userDetailByToken = shopEaseMallUserService.getUserDetailByToken(loginMallUserToken.getToken());
        BeanUtil.copyProperties(userDetailByToken, mallUserVO);
        return ResultGenerator.genSuccessResult(mallUserVO);
    }

    @RequestMapping(value = "/getDetailByToken", method = RequestMethod.GET)
    public Result getMallUserByToken(@RequestParam("token") String token) {
        MallUser userDetailByToken = shopEaseMallUserService.getUserDetailByToken(token);
        if (userDetailByToken != null) {
            Result result = ResultGenerator.genSuccessResult();
            result.setData(userDetailByToken);
            return result;
        }
        return ResultGenerator.genFailResult("无此用户数据");
    }
}
