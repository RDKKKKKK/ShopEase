 
package ltd.user.cloud.shopease.service;

import ltd.user.cloud.shopease.entity.AdminUser;

public interface AdminUserService {

    /**
     * 登录
     * @param userName
     * @param password
     * @return
     */
    String login(String userName, String password);

    /**
     * 获取用户信息
     *
     * @param loginUserId
     * @return
     */
    AdminUser getUserDetailById(Long loginUserId);

    /**
     * 获取用户信息 by token
     *
     * @param token
     * @return
     */
    AdminUser getUserDetailByToken(String token);

    /**
     * 修改当前登录用户的密码
     *
     * @param loginUserId
     * @param originalPassword
     * @param newPassword
     * @return
     */
    Boolean updatePassword(Long loginUserId, String originalPassword, String newPassword);

    /**
     * 修改当前登录用户的名称信息
     *
     * @param loginUserId
     * @param loginUserName
     * @param nickName
     * @return
     */
    Boolean updateName(Long loginUserId, String loginUserName, String nickName);

    /**
     * 登出接口
     * @param token
     * @return
     */
    Boolean logout(String token);


}
