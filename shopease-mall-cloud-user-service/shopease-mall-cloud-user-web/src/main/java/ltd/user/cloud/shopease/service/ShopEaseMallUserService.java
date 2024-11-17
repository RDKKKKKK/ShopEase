 
package ltd.user.cloud.shopease.service;

import ltd.common.cloud.shopease.dto.PageQueryUtil;
import ltd.common.cloud.shopease.dto.PageResult;
import ltd.user.cloud.shopease.controller.param.MallUserUpdateParam;
import ltd.user.cloud.shopease.entity.MallUser;

public interface ShopEaseMallUserService {

    /**
     * 用户注册
     *
     * @param loginName
     * @param password
     * @return
     */
    String register(String loginName, String password);


    /**
     * 登录
     *
     * @param loginName
     * @param passwordMD5
     * @return
     */
    String login(String loginName, String passwordMD5);

    /**
     * 用户信息修改
     *
     * @param mallUser
     * @return
     */
    Boolean updateUserInfo(MallUserUpdateParam mallUser, Long userId);

    /**
     * 获取用户信息 by token
     *
     * @param token
     * @return
     */
    MallUser getUserDetailByToken(String token);

    /**
     * 登出接口
     * @param token
     * @return
     */
    Boolean logout(String token);

    /**
     * 用户禁用与解除禁用(0-未锁定 1-已锁定)
     *
     * @param ids
     * @param lockStatus
     * @return
     */
    Boolean lockUsers(Long[] ids, int lockStatus);

    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getShopEaseMallUsersPage(PageQueryUtil pageUtil);
}
