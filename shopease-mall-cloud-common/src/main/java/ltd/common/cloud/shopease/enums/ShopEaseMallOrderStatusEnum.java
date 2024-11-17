package ltd.common.cloud.shopease.enums;

import lombok.Data;


public enum ShopEaseMallOrderStatusEnum {

    DEFAULT(-9, "ERROR"),
    ORDER_PRE_PAY(0, "Pending Payment"),
    ORDER_PAID(1, "Payment Done"),
    ORDER_PACKAGED(2, "Invoice Done"),
    ORDER_EXPRESS(3, "Delivery Done"),
    ORDER_SUCCESS(4, "Success"),
    ORDER_CLOSED_BY_MALLUSER(-1, "Cancelled Manually"),
    ORDER_CLOSED_BY_EXPIRED(-2, "Cancelled Timeout"),
    ORDER_CLOSED_BY_JUDGE(-3, "Cancelled");

    private int orderStatus;

    private String name;

    ShopEaseMallOrderStatusEnum(int orderStatus, String name) {
        this.orderStatus = orderStatus;
        this.name = name;
    }

    public static ShopEaseMallOrderStatusEnum getShopEaseMallOrderStatusEnumByStatus(int orderStatus) {
        for (ShopEaseMallOrderStatusEnum shopEaseMallOrderStatusEnum : ShopEaseMallOrderStatusEnum.values()) {
            if (shopEaseMallOrderStatusEnum.getOrderStatus() == orderStatus) {
                return shopEaseMallOrderStatusEnum;
            }
        }
        return DEFAULT;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
