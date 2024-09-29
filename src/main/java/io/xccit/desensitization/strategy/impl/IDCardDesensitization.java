package io.xccit.desensitization.strategy.impl;

import io.xccit.desensitization.SensitiveType;
import io.xccit.desensitization.strategy.IDesensitizationStrategy;
import org.springframework.stereotype.Component;

/**
 * @author CH_ywx
 * @version 1.0
 * @create 2024/9/29
 * <p>身份证脱敏策略</p>
 */
@Component
public class IDCardDesensitization implements IDesensitizationStrategy {
    /**
     * 判断是否支持脱敏
     *
     * @param type 待脱敏的类型
     * @return 是否支持
     */
    @Override
    public boolean support(SensitiveType type) {
        return type.equals(SensitiveType.ID_CARD);
    }

    /**
     * 脱敏
     *
     * @param value 中国居民身份证号
     * @return 脱敏后的值
     */
    @Override
    public String desensitize(String value) {
        if (value != null && value.length() == 18){
            String first = value.substring(0,6);
            String last = value.substring(14,18);
            return first + "********" + last;
        }
        return "数据类型不支持";
    }
}
