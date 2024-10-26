package ltd.goods.cloud.newbee.config;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "aliyun.oss")
@Component
@Data
public class AliyunOSSConfig implements InitializingBean {
    private String endPoint;
    private String keyId;
    private String keySecret;
    private String bucketName;

    public static String END_POINT;
    public static String KEY_ID;
    public static String KEY_SECRET;
    public static String BUCKET_NAME;
    //在给上面属性通过配置文件注入值之后，定义static变量，让外界直接用类名.的方式获取属性
    //此方法在上面私有属性被注入值之后，可以执行下面的方法
    @Override
    public void afterPropertiesSet() throws Exception {
        END_POINT = endPoint;
        KEY_ID = keyId;
        KEY_SECRET = keySecret;
        BUCKET_NAME = bucketName;
    }
}

