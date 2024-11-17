 
package ltd.common.cloud.shopease.enums;

/**
 * @author 13
 * @qq交流群 796794009
 * @email 2449207463@qq.com
 * @link https://github.com/shopease-ltd
 * @apiNote 分类级别
 */
public enum ShopEaseMallCategoryLevelEnum {

    DEFAULT(0, "ERROR"),
    LEVEL_ONE(1, "一级分类"),
    LEVEL_TWO(2, "二级分类"),
    LEVEL_THREE(3, "三级分类");

    private int level;

    private String name;

    ShopEaseMallCategoryLevelEnum(int level, String name) {
        this.level = level;
        this.name = name;
    }

    public static ShopEaseMallCategoryLevelEnum getShopEaseMallOrderStatusEnumByLevel(int level) {
        for (ShopEaseMallCategoryLevelEnum shopEaseMallCategoryLevelEnum : ShopEaseMallCategoryLevelEnum.values()) {
            if (shopEaseMallCategoryLevelEnum.getLevel() == level) {
                return shopEaseMallCategoryLevelEnum;
            }
        }
        return DEFAULT;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
