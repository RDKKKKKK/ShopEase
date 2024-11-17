 
package ltd.order.cloud.shopease.config.handler;

import ltd.common.cloud.shopease.enums.ServiceResultEnum;
import ltd.common.cloud.shopease.dto.Result;
import ltd.common.cloud.shopease.exception.ShopEaseMallException;
import ltd.common.cloud.shopease.pojo.MallUserToken;
import ltd.order.cloud.shopease.config.annotation.TokenToMallUser;
import ltd.user.cloud.shopease.dto.MallUserDTO;
import ltd.user.cloud.shopease.openfeign.ShopEaseCloudUserServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class TokenToMallUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private ShopEaseCloudUserServiceFeign shopEaseCloudUserService;

    public TokenToMallUserMethodArgumentResolver() {
    }

    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(TokenToMallUser.class)) {
            return true;
        }
        return false;
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        if (parameter.getParameterAnnotation(TokenToMallUser.class) instanceof TokenToMallUser) {
            String token = webRequest.getHeader("token");
            if (null != token && !"".equals(token) && token.length() == 32) {
                Result<MallUserDTO> result = shopEaseCloudUserService.getMallUserByToken(token);
                if (result == null || result.getResultCode() != 200 || result.getData() == null) {
                    ShopEaseMallException.fail(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
                }
                MallUserToken mallUserToken = new MallUserToken();
                mallUserToken.setToken(token);
                mallUserToken.setUserId(result.getData().getUserId());
                return mallUserToken;
            } else {
                ShopEaseMallException.fail(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            }
        }
        return null;
    }

}
