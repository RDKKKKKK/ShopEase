 
package ltd.order.cloud.shopease.config.handler;

import ltd.common.cloud.shopease.enums.ServiceResultEnum;
import ltd.common.cloud.shopease.dto.Result;
import ltd.common.cloud.shopease.exception.ShopEaseMallException;

import ltd.order.cloud.shopease.config.annotation.TokenToAdminUser;
import ltd.order.cloud.shopease.entity.LoginAdminUser;
import ltd.user.cloud.shopease.openfeign.ShopEaseCloudUserServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.LinkedHashMap;

@Component
public class TokenToAdminUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private ShopEaseCloudUserServiceFeign shopEaseCloudUserService;

    public TokenToAdminUserMethodArgumentResolver() {
    }

    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(TokenToAdminUser.class)) {
            return true;
        }
        return false;
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        if (parameter.getParameterAnnotation(TokenToAdminUser.class) instanceof TokenToAdminUser) {
            String token = webRequest.getHeader("token");
            if (null != token && !"".equals(token) && token.length() == 32) {
                // 通过用户中心获取用户信息
                Result result = shopEaseCloudUserService.getAdminUserByToken(token);

                if (result == null || result.getResultCode() != 200 || result.getData() == null) {
                    ShopEaseMallException.fail(ServiceResultEnum.ADMIN_NOT_LOGIN_ERROR.getResult());
                }

                LinkedHashMap resultData = (LinkedHashMap) result.getData();

                // 将返回的字段封装到LoginAdminUser对象中
                LoginAdminUser loginAdminUser = new LoginAdminUser();
                loginAdminUser.setAdminUserId(Long.valueOf(resultData.get("adminUserId").toString()));
                loginAdminUser.setLoginUserName((String) resultData.get("loginUserName"));
                loginAdminUser.setNickName((String) resultData.get("nickName"));
                loginAdminUser.setLocked(Byte.valueOf(resultData.get("locked").toString()));
                return loginAdminUser;
            } else {
                ShopEaseMallException.fail(ServiceResultEnum.ADMIN_NOT_LOGIN_ERROR.getResult());
            }
        }
        return null;
    }

}
