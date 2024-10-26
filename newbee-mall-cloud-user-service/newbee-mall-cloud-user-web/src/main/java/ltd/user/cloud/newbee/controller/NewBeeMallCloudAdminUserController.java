/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本系统已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2022 程序员十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package ltd.user.cloud.newbee.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import ltd.common.cloud.newbee.dto.PageQueryUtil;
import ltd.common.cloud.newbee.dto.Result;
import ltd.common.cloud.newbee.dto.ResultGenerator;
import ltd.user.cloud.newbee.config.annotation.TokenToAdminUser;
import ltd.user.cloud.newbee.controller.param.AdminLoginParam;
import ltd.user.cloud.newbee.controller.param.UpdateAdminNameParam;
import ltd.user.cloud.newbee.controller.param.UpdateAdminPasswordParam;
import ltd.user.cloud.newbee.entity.AdminUser;
import ltd.common.cloud.newbee.pojo.AdminUserToken;
import ltd.user.cloud.newbee.service.AdminUserService;

import ltd.user.cloud.newbee.service.NewBeeMallUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Api(value = "v1", tags = "新蜂商城管理员操作相关接口")
@RestController
public class NewBeeMallCloudAdminUserController {

    private static final Logger logger = LoggerFactory.getLogger(NewBeeMallCloudAdminUserController.class);

    @Resource
    private AdminUserService adminUserService;
    @Resource
    private NewBeeMallUserService newBeeMallUserService;

    @ApiOperation(value = "登录接口", notes = "返回token")
    @RequestMapping(value = "/users/admin/login", method = RequestMethod.POST)
    public Result<String> login(@RequestBody @Valid AdminLoginParam adminLoginParam) {
        String loginResult = adminUserService.login(adminLoginParam.getUserName(), adminLoginParam.getPasswordMd5());
        logger.info("manage login api,adminName={},loginResult={}", adminLoginParam.getUserName(), loginResult);

        //登录成功
        if (StringUtils.hasText(loginResult) && loginResult.length() == 32) {
            Result result = ResultGenerator.genSuccessResult();
            result.setData(loginResult);
            return result;
        }
        //登录失败
        return ResultGenerator.genFailResult(loginResult);
    }

    @ApiOperation(value = "获取管理员信息接口")
    @RequestMapping(value = "/users/admin/profile", method = RequestMethod.POST)
    public Result profile(@TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        AdminUser adminUserEntity = adminUserService.getUserDetailById(adminUser.getAdminUserId());
        if (adminUserEntity != null) {
            adminUserEntity.setLoginPassword("******");
            Result result = ResultGenerator.genSuccessResult();
            result.setData(adminUserEntity);
            return result;
        }
        return ResultGenerator.genFailResult("无此用户数据");
    }

    @ApiOperation(value = "修改管理员密码接口")
    @RequestMapping(value = "/users/admin/password", method = RequestMethod.PUT)
    public Result passwordUpdate(@RequestBody @Valid UpdateAdminPasswordParam adminPasswordParam, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        if (adminUserService.updatePassword(adminUser.getAdminUserId(), adminPasswordParam.getOriginalPassword(), adminPasswordParam.getNewPassword())) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("DB ERROR");
        }
    }

    @ApiOperation(value = "修改管理员信息接口")
    @RequestMapping(value = "/users/admin/name", method = RequestMethod.PUT)
    public Result nameUpdate(@RequestBody @Valid UpdateAdminNameParam adminNameParam, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        if (adminUserService.updateName(adminUser.getAdminUserId(), adminNameParam.getLoginUserName(), adminNameParam.getNickName())) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("DB ERROR");
        }
    }

    @ApiOperation(value = "管理员退出登录的接口")
    @RequestMapping(value = "/users/admin/logout", method = RequestMethod.DELETE)
    public Result logout(@TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        adminUserService.logout(adminUser.getToken());
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "根据token获取管理员信息的接口", notes = "OpenFeign调用")
    @RequestMapping(value = "/users/admin/{token}", method = RequestMethod.GET)
    public Result<AdminUser> getAdminUserByToken(@PathVariable("token") String token) {
        AdminUser adminUser = adminUserService.getUserDetailByToken(token);
        if (adminUser != null) {
            adminUser.setLoginPassword("******");
            Result result = ResultGenerator.genSuccessResult();
            result.setData(adminUser);
            return result;
        }
        return ResultGenerator.genFailResult("无此用户数据");
    }

    @ApiOperation(value = "获取所有商城用户")
    @RequestMapping(value = "/users/admin/users",method = RequestMethod.GET)
    public Result getAllUsers(@RequestParam(required = false) @ApiParam(value = "页码") Integer pageNumber,
                              @RequestParam(required = false) @ApiParam(value = "每页条数") Integer pageSize,
                              @RequestParam(required = false) @ApiParam(value = "用户状态") Integer lockStatus, @TokenToAdminUser AdminUserToken adminUser){
        logger.info("adminUser:{}", adminUser.toString());
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 10) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        Map params = new HashMap(8);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        if (lockStatus != null) {
            params.put("orderStatus", lockStatus);
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(newBeeMallUserService.getNewBeeMallUsersPage(pageUtil));
    }

    @ApiOperation(value = "启用禁用商城用户")
    @PutMapping(value = "/users/admin/users/{lockStatus}")
    public Result updateLockedFlag(@RequestBody Long[] ids, @PathVariable int lockStatus, @TokenToAdminUser AdminUserToken adminUser){
        logger.info("adminUser:{}", adminUser.toString());
        if (ids==null||ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (lockStatus != 0 && lockStatus != 1) {
            return ResultGenerator.genFailResult("操作非法！");
        }
        if (newBeeMallUserService.lockUsers(ids, lockStatus)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("禁用失败");
        }
    }
}