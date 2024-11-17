 
package ltd.common.cloud.shopease.exception;

public class ShopEaseMallException extends RuntimeException {

    public ShopEaseMallException() {
    }

    public ShopEaseMallException(String message) {
        super(message);
    }

    /**
     * 丢出一个异常
     *
     * @param message
     */
    public static void fail(String message) {
        throw new ShopEaseMallException(message);
    }

}
