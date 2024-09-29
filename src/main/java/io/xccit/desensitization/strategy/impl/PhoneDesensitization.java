package io.xccit.desensitization.strategy.impl;

import io.xccit.desensitization.SensitiveType;
import io.xccit.desensitization.strategy.IDesensitizationStrategy;
import org.springframework.stereotype.Component;

/**
 * @author CH_ywx
 * @version 1.0
 * @create 2024/9/29
 * <p>手机号脱敏</p>
 */
@Component
public class PhoneDesensitization implements IDesensitizationStrategy {
    /**
     * 判断是否支持脱敏
     *
     * @param type 待脱敏的类型
     * @return 是否支持
     */
    @Override
    public boolean support(SensitiveType type) {
        return type.equals(SensitiveType.PHONE);
    }

    /**
     * 脱敏
     *
     * @param value 中国支持的手机号
     * @return 脱敏后的值
     */
    @Override
    public String desensitize(String value) {
        if (value != null && value.length() == 11){
            return value.substring(0,3) + "****" + value.substring(7,11);
        }
        return "不支持的类型";
    }
}
