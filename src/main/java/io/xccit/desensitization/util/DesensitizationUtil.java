package io.xccit.desensitization.util;

import io.xccit.desensitization.Desensitization;
import io.xccit.desensitization.SensitiveType;
import io.xccit.desensitization.strategy.IDesensitizationStrategy;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author CH_ywx
 * @version 1.0
 * @create 2024/9/29
 * <p>数据脱敏工具类</p>
 */
@Component
public class DesensitizationUtil {

    @Autowired
    private List<IDesensitizationStrategy> strategyList;

    /**
     * 根据类型脱敏
     * @param source 源类型
     * @param target 脱敏后类型
     * @return 脱敏后类型
     */
    public Object desensitize(Object source, Object target) throws IllegalAccessException {
        BeanUtils.copyProperties(source,target);
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Desensitization annotation = field.getAnnotation(Desensitization.class);
            if (annotation != null) {
                SensitiveType type = annotation.type();
                for (IDesensitizationStrategy strategy : strategyList) {
                    if (strategy.support(type)) {
                        String value = strategy.desensitize(field.get(target).toString());
                        field.set(target,value);
                   }
                }
            }
        }
        return target;
    }
}
