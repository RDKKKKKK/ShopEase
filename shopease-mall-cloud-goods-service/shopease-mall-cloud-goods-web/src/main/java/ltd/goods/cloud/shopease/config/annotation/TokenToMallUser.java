
package ltd.goods.cloud.shopease.config.annotation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TokenToMallUser {

    /**
     * 当前用户在request中的名字
     *
     * @return
     */
    String value() default "user";

}
