package io.xccit.desensitization.strategy.impl;

import io.xccit.desensitization.SensitiveType;
import io.xccit.desensitization.strategy.IDesensitizationStrategy;
import org.springframework.stereotype.Component;

/**
 * @author CH_ywx
 * @version 1.0
 * @create 2024/9/29
 * <p>银行卡脱敏</p>
 */
@Component
public class BankCardDesensitization implements IDesensitizationStrategy {
    /**
     * 判断是否支持脱敏
     *
     * @param type 待脱敏的类型
     * @return 是否支持
     */
    @Override
    public boolean support(SensitiveType type) {
        return type.equals(SensitiveType.BANK_CARD);
    }

    /**
     * 脱敏
     *
     * @param value 中国所有银行支持的银行卡号
     * @return 脱敏后的值
     */
    @Override
    public String desensitize(String value) {
        if (value != null && value.length() == 19){
            return "**** **** **** " + value.substring(12,19);
        }
        return "不支持的类型";
    }
}
