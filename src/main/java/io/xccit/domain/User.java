package io.xccit.domain;

import lombok.Data;

/**
  *@author CH_ywx
  *@create 2024/9/29
  *<p>用户</p>
  *@version 1.0
*/
@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String nickName;
    private String bankCard;
    private String phone;
    private String idCard;
}
