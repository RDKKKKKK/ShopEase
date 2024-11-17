 
package ltd.user.cloud.shopease.openfeign;

import ltd.user.cloud.shopease.dto.MallUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ltd.common.cloud.shopease.dto.Result;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", path = "/users", url = "http://user-service:29000")
public interface ShopEaseCloudUserServiceFeign {

    @GetMapping(value = "/admin/{token}")
    Result getAdminUserByToken(@PathVariable(value = "token") String token);

    @GetMapping(value = "/mall/getDetailByToken")
    Result<MallUserDTO> getMallUserByToken(@RequestParam(value = "token") String token);
}
