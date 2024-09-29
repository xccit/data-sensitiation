package io.xccit.r;

import lombok.Builder;
import lombok.Data;

/**
 * @author CH_ywx
 * @version 1.0
 * @create 2024/9/29
 * <p>全局返回对象</p>
 */
@Data
@Builder
public class AjaxResult<T> {
    private Integer code;
    private String message;
    private T data;

    public AjaxResult() {}
    public AjaxResult(ResultEnum resultEnum, T data){
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
        this.data = data;
    }
    public AjaxResult(Integer code, String message,T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 请求成功
     * @param data 数据
     * @return AjaxResult
     * @param <T> 参数类型
     */
    public static <T> AjaxResult success(T data){
        if (data != null){
            return AjaxResult.builder().code(200).message("SUCCESS").data(data).build();
        }
        return AjaxResult.builder().code(200).message("SUCCESS").build();
    }

    /**
     * 请求成功
     * @param resultEnum 返回类型枚举
     * @param data 数据
     * @return AjaxResult
     * @param <T> 参数类型
     */
    public static <T> AjaxResult success(ResultEnum resultEnum,T data){
        if (data != null){
            return AjaxResult.builder().code(resultEnum.getCode()).message(resultEnum.getMessage()).data(data).build();
        }
        return AjaxResult.builder().code(resultEnum.getCode()).message(resultEnum.getMessage()).build();
    }

    /**
     * 请求失败
     * @return AjaxResult
     * @param <T> 参数类型
     */
    public static <T> AjaxResult fail(){
        return new AjaxResult(ResultEnum.SERVER_ERROR,null);
    }

    /**
     * 请求失败
     * @param resultEnum 返回类型枚举
     * @return AjaxResult
     * @param <T> 参数类型
     */
    public static <T> AjaxResult fail(ResultEnum resultEnum){
        return new AjaxResult(resultEnum,null);
    }
}
