/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2022 程序员十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package ltd.common.cloud.newbee.enums;

public enum ServiceResultEnum {
    ERROR("error"),

    SUCCESS("success"),

    DATA_NOT_EXIST("No Records！"),

    PARAM_ERROR("Param Error！"),

    SAME_CATEGORY_EXIST("Category Existed！"),

    SAME_LOGIN_NAME_EXIST("User Existed！"),

    LOGIN_NAME_NULL("请输入登录名！"),

    LOGIN_NAME_IS_NOT_PHONE("Please Enter Correct Username！"),

    LOGIN_PASSWORD_NULL("请输入密码！"),

    LOGIN_VERIFY_CODE_NULL("请输入验证码！"),

    LOGIN_VERIFY_CODE_ERROR("验证码错误！"),

    SAME_INDEX_CONFIG_EXIST("已存在相同的首页配置项！"),

    GOODS_CATEGORY_ERROR("Category Exception!"),

    SAME_GOODS_EXIST("Product Existed！"),

    GOODS_NOT_EXIST("Product Not Exist！"),

    GOODS_PUT_DOWN("Product Removed！"),

    SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR("Exceeding the maximum purchase quantity for a single item！"),

    SHOPPING_CART_ITEM_NUMBER_ERROR("The number of products cannot be less than 1！"),

    SHOPPING_CART_ITEM_TOTAL_NUMBER_ERROR("Exceeding the maximum capacity of the shopping cart！"),

    SHOPPING_CART_ITEM_EXIST_ERROR("Already exists! No need to add repeatedly！"),

    LOGIN_ERROR("Login Failure！"),

    NOT_LOGIN_ERROR("Not logged in！"),

    ADMIN_NOT_LOGIN_ERROR("Administrator not logged in！"),

    TOKEN_EXPIRE_ERROR("Invalid authentication! Please log in again！"),

    ADMIN_TOKEN_EXPIRE_ERROR("Administrator login expired! Please re-login！"),

    USER_NULL_ERROR("Invalid user! Please log in again！"),

    LOGIN_USER_LOCKED_ERROR("User has been banned from logging in！"),

    ORDER_NOT_EXIST_ERROR("Order does not exist！"),

    ORDER_ITEM_NOT_EXIST_ERROR("Order item does not exist！"),

    NULL_ADDRESS_ERROR("地址不能为空！"),

    ORDER_PRICE_ERROR("Order price anomalies！"),

    ORDER_ITEM_NULL_ERROR("Order Item Exception！"),

    ORDER_GENERATE_ERROR("Generate order exceptions！"),

    SHOPPING_ITEM_ERROR("Shopping Cart Data Exception！"),

    SHOPPING_ITEM_COUNT_ERROR("Insufficient inventory！"),

    ORDER_STATUS_ERROR("Order Status Exception！"),

    OPERATE_ERROR("Failure of an operation！"),

    REQUEST_FORBIDEN_ERROR("Disable this operation！"),

    NO_PERMISSION_ERROR("No entitlement！"),

    DB_ERROR("Database error");

    private String result;

    ServiceResultEnum(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
