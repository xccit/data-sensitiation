package io.xccit.service;

import io.xccit.desensitization.util.DesensitizationUtil;
import io.xccit.domain.User;
import io.xccit.domain.vo.UserVO;
import io.xccit.mapper.IUserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author CH_ywx
 * @version 1.0
 * @create 2024/9/29
 * <p>用户业务层</p>
 */
@Service
public class UserServiceImpl implements IUserService{

    @Autowired
    private IUserMapper userMapper;
    @Autowired
    private DesensitizationUtil desensitizationUtil;

    /**
     * 根据id查询用户,脱敏后返回
     *
     * @param id 用户id
     * @return 用户信息
     */
    @Override
    public UserVO getUser(Long id) {
        User user = userMapper.getUser(id);
        UserVO vo = new UserVO();
        /*try {
            return (UserVO) desensitizationUtil.desensitize(user, vo);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }*/
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}
