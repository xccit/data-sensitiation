package io.xccit.service;

import io.xccit.domain.vo.UserVO;

/**
 * @author CH_ywx
 * @version 1.0
 * @create 2024/9/29
 * <p>用户业务层</p>
 */
public interface IUserService {

    /**
     * 根据id查询用户
     * @param id 用户id
     * @return 用户信息
     */
    UserVO getUser(Long id);
}
