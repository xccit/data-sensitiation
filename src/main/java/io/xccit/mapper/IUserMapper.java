package io.xccit.mapper;

import io.xccit.domain.User;

/**
 * @author CH_ywx
 * @version 1.0
 * @create 2024/9/29
 * <p>用户dao接口</p>
 */
public interface IUserMapper {

    /**
     * 根据id查询用户
     * @param id 用户ID
     * @return 用户信息
     */
    User getUser(Long id);
}
