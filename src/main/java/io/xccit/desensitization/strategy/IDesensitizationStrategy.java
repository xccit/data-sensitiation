package io.xccit.desensitization.strategy;

import io.xccit.desensitization.SensitiveType;

/**
 * @author CH_ywx
 * @version 1.0
 * @create 2024/9/29
 * <p>脱敏策略</p>
 */
public interface IDesensitizationStrategy {

    /**
     * 判断是否支持脱敏
     * @param type 待脱敏的类型
     * @return 是否支持
     */
    boolean support(SensitiveType type);

    /**
     * 脱敏
     * @param value 待脱敏的值
     * @return 脱敏后的值
     */
    String desensitize(String value);
}
