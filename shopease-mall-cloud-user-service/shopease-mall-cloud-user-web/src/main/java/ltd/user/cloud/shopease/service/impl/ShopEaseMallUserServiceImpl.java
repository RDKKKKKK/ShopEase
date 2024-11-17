 
package ltd.user.cloud.shopease.service.impl;

import ltd.common.cloud.shopease.enums.ServiceResultEnum;
import ltd.common.cloud.shopease.dto.PageQueryUtil;
import ltd.common.cloud.shopease.dto.PageResult;
import ltd.common.cloud.shopease.exception.ShopEaseMallException;
import ltd.common.cloud.shopease.pojo.MallUserToken;
import ltd.common.cloud.shopease.util.MD5Util;
import ltd.common.cloud.shopease.util.NumberUtil;
import ltd.common.cloud.shopease.util.SystemUtil;
import ltd.user.cloud.shopease.controller.param.MallUserUpdateParam;
import ltd.user.cloud.shopease.dao.MallUserMapper;
import ltd.user.cloud.shopease.entity.MallUser;
import ltd.user.cloud.shopease.service.ShopEaseMallUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ShopEaseMallUserServiceImpl implements ShopEaseMallUserService {

    @Autowired
    private MallUserMapper mallUserMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public String register(String loginName, String password) {
        if (mallUserMapper.selectByLoginName(loginName) != null) {
            return ServiceResultEnum.SAME_LOGIN_NAME_EXIST.getResult();
        }
        MallUser registerUser = new MallUser();
        registerUser.setLoginName(loginName);
        registerUser.setNickName(loginName);
        registerUser.setIntroduceSign("随新所欲，蜂富多彩");
        String passwordMD5 = MD5Util.MD5Encode(password, "UTF-8");
        registerUser.setPasswordMd5(passwordMD5);
        if (mallUserMapper.insertSelective(registerUser) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String login(String loginName, String passwordMD5) {
        MallUser user = mallUserMapper.selectByLoginNameAndPasswd(loginName, passwordMD5);
        if (user != null) {
            if (user.getLockedFlag() == 1) {
                return ServiceResultEnum.LOGIN_USER_LOCKED_ERROR.getResult();
            }
            //登录后即执行修改token的操作
            String token = getNewToken(System.currentTimeMillis() + "", user.getUserId());
            MallUserToken mallUserToken = new MallUserToken();
            mallUserToken.setUserId(user.getUserId());
            mallUserToken.setToken(token);
            ValueOperations<String, MallUserToken> setToken = redisTemplate.opsForValue();
            setToken.set(token, mallUserToken, 7 * 24 * 60 * 60, TimeUnit.SECONDS);//过期时间7天
            return token;

        }
        return ServiceResultEnum.LOGIN_ERROR.getResult();
    }

    /**
     * 获取token值
     *
     * @param timeStr
     * @param userId
     * @return
     */
    private String getNewToken(String timeStr, Long userId) {
        String src = timeStr + userId + NumberUtil.genRandomNum(4);
        return SystemUtil.genToken(src);
    }

    @Override
    public Boolean updateUserInfo(MallUserUpdateParam mallUser, Long userId) {
        MallUser user = mallUserMapper.selectByPrimaryKey(userId);
        if (user == null) {
            ShopEaseMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        user.setNickName(mallUser.getNickName());
        //若密码为空字符，则表明用户不打算修改密码，使用原密码保存
        if (!MD5Util.MD5Encode("", "UTF-8").equals(mallUser.getPasswordMd5())) {
            user.setPasswordMd5(mallUser.getPasswordMd5());
        }
        user.setIntroduceSign(mallUser.getIntroduceSign());
        if (mallUserMapper.updateByPrimaryKeySelective(user) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public MallUser getUserDetailByToken(String token) {
        ValueOperations<String, MallUserToken> opsForMallUserToken = redisTemplate.opsForValue();
        MallUserToken mallUserToken = opsForMallUserToken.get(token);
        if (mallUserToken != null) {
            MallUser mallUser = mallUserMapper.selectByPrimaryKey(mallUserToken.getUserId());
            if (mallUser == null) {
                ShopEaseMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
            }
            if (mallUser.getLockedFlag().intValue() == 1) {
                ShopEaseMallException.fail(ServiceResultEnum.LOGIN_USER_LOCKED_ERROR.getResult());
            }
            return mallUser;
        }
        ShopEaseMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        return null;
    }

    @Override
    public Boolean logout(String token) {
        redisTemplate.delete(token);
        return true;
    }

    @Override
    public PageResult getShopEaseMallUsersPage(PageQueryUtil pageUtil) {
        List<MallUser> mallUsers = mallUserMapper.findMallUserList(pageUtil);
        int total = mallUserMapper.getTotalMallUsers(pageUtil);
        PageResult pageResult = new PageResult(mallUsers, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public Boolean lockUsers(Long[] ids, int lockStatus) {
        if (ids.length < 1) {
            return false;
        }
        return mallUserMapper.lockUserBatch(ids, lockStatus) > 0;
    }
}
