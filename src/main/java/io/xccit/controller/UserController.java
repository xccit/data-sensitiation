package io.xccit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.xccit.domain.vo.UserVO;
import io.xccit.r.AjaxResult;
import io.xccit.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CH_ywx
 * @version 1.0
 * @create 2024/9/29
 * <p>用户控制器</p>
 */
@Tag(name = "用户接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "根据id查询用户")
    @GetMapping("/{id}")
    public AjaxResult<UserVO> getUser(@PathVariable Long id){
        return AjaxResult.success(userService.getUser(id));
    }
}
