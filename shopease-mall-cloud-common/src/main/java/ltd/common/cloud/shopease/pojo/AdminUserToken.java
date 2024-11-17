 
package ltd.common.cloud.shopease.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AdminUserToken implements Serializable {
    private Long adminUserId;

    private String token;
}