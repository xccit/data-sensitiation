package io.xccit.domain.vo;

import io.xccit.desensitization.Desensitization;
import io.xccit.desensitization.SensitiveType;
import lombok.Data;

/**
 * @author CH_ywx
 * @version 1.0
 * @create 2024/9/29
 * <p>用户结果封装</p>
 */
@Data
public class UserVO {
    private Long id;
    private String username;
    private String password;
    private String nickName;
    @Desensitization(type = SensitiveType.BANK_CARD)
    private String bankCard;
    @Desensitization(type = SensitiveType.PHONE)
    private String phone;
    @Desensitization(type = SensitiveType.ID_CARD)
    private String idCard;
}
