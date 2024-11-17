 
package ltd.order.cloud.shopease.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ShopEaseMallOrderItem {
    private Long orderItemId;

    private Long orderId;

    private Long goodsId;

    private String goodsName;

    private String goodsCoverImg;

    private Integer sellingPrice;

    private Integer goodsCount;

    private Date createTime;
}