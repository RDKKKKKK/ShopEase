 
package ltd.goods.cloud.shopease.entity;

import lombok.Data;

@Data
public class LoginAdminUser {
    private Long adminUserId;

    private String loginUserName;

    private String loginPassword;

    private String nickName;

    private Byte locked;
}