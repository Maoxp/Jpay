package com.github.maoxp.core.exception;

/**
 * Class: BaseResultEnum
 *
 * @author mxp
 * @date 2022年07月28日 16:51
 * @since JDK 1.8
 */
public enum ExceptionEnum {
    UNKNOWN_ERROR("9999", "未知错误"),
    RUNTIME("9998", "运行时异常"),
    NPE("9997", "空指针异常"),
    CLASS_CAST("9996", "类型转换异常"),
    IO("9995", "IO异常"),
    NO_SUCH_METHOD("9994", "未知方法异常"),
    INDEX_OUT_OF_BOUNDS("9993", "数组越界异常"),
    STACKOVERFLOW_ERROR("9992", "栈溢出错误"),

    TYPE_MISMATCH("8001", "400: 请求参数类型与server定义的参数类型不匹配"),
    HTTP_REQUEST_METHOD_NOT_SUPPORTED("8002", "405: Request Method Not Allowed"),
    HTTP_MEDIA_TYPE_NOT_ACCEPTABLE("8003", "406: 服务器响应的结构体与客户端预期能接收的结构体不一致，无法解析内容"),
    HTTP_MEDIA_TYPE_NOT_SUPPORTED("8004", "415: Unsupported Media Type"),
    HTTP_MESSAGE_NOT_READABLE("8005", "400: 无法正常接收请求体"),
    MISSING_SERVLET_REQUEST_PARAMETER("8006", "400: 缺少请求参数"),
    METHOD_ARGUMENT_NOT_VALID("8007", "参数校验不通过"),
    MISSING_PATH_VARIABLE("8008", "400: 缺少路径参数"),
    NO_HANDLER_FOUND("8009", "404: 请求路径未找到"),
    CONVERSION_NOT_SUPPORTED("8010", "500: 响应体转换异常,返回失败"),

    HTTP_URL_NOT_NULL("9983", "入参请求URL不能为空"),
    HTTP_BODY_NOT_NULL("9982", "请求HTTP协议实体信息不能为空"),
    UN_SUPPORTED_ENCODING("9981", "组装请求参数失败，未知的编码"),
    ;

    private final String code;
    private final String message;

    ExceptionEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
