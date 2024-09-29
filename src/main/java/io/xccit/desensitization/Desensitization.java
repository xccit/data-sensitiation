package io.xccit.desensitization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author CH_ywx
 * @version 1.0
 * @create 2024/9/29
 * <p>脱敏注解</p>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Desensitization {

    SensitiveType type();
}
