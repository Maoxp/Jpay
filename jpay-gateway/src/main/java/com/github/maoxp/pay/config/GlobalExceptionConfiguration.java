package com.github.maoxp.pay.config;


import com.github.maoxp.core.exception.BizException;
import com.github.maoxp.core.exception.ExceptionCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 全局异常处理
 *
 * @author mxp
 * @date 2023/5/29年05月29日 15:12
 * @since JDK 1.8
 */
@Configuration
@RestControllerAdvice(annotations = {RestController.class, Controller.class})
@Slf4j
public class GlobalExceptionConfiguration {
    private static final String EX_FORMAT = "Capture Exception \n ExceptionClass: %s ==> ExceptionMessage: %s";

    /**
     * Controller上一层相关异常
     *
     * @param e 异常
     * @return ResponseEntity
     */
    @ExceptionHandler({
            NoHandlerFoundException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            HttpMediaTypeNotAcceptableException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            MissingServletRequestPartException.class,
            AsyncRequestTimeoutException.class
    })
    public ResponseEntity<?> servletException(Exception e) {
        // 首先根据请求 Url 查找有没有对应的控制器，若没有则会抛该异常，也就是大家非常熟悉的 404 异常。
        if (e instanceof NoHandlerFoundException) {
            return resultFormat(ExceptionCodeEnum.NO_HANDLER_FOUND, e, HttpStatus.NOT_FOUND);
        }
        // 若匹配到了（匹配结果是一个列表，不同的是 http 方法不同，如：Get、Post 等），则尝试将请求的 http 方法与列表的控制器做匹配，若没有对应 http 方法的控制器，则抛该异常。
        else if (e instanceof HttpRequestMethodNotSupportedException) {
            return resultFormat(ExceptionCodeEnum.HTTP_REQUEST_METHOD_NOT_SUPPORTED, e, HttpStatus.METHOD_NOT_ALLOWED);
        }
        // 然后再对请求头Content-type与服务器能支持的Content-type做比较。
        else if (e instanceof HttpMediaTypeNotSupportedException) {
            return resultFormat(ExceptionCodeEnum.HTTP_MEDIA_TYPE_NOT_SUPPORTED, e, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
        // 未检测到路径参数。比如 url 为：/licence/{licenceId}，参数签名包含 @PathVariable("licenceId")。
        else if (e instanceof MissingPathVariableException) {
            log.error("400，缺少路径参数 " + ((MissingPathVariableException) e).getVariableName());
            return resultFormat(ExceptionCodeEnum.MISSING_PATH_VARIABLE, e, HttpStatus.BAD_REQUEST);
        }
        // 缺少请求参数。比如定义了参数 @RequestParam("licenceId") String licenceId，但发起请求时，未携带该参数，则会抛该异常。
        if (e instanceof MissingServletRequestParameterException) {
            log.error("400，缺少请求参数 " + ((MissingServletRequestParameterException) e).getParameterName());
            return resultFormat(ExceptionCodeEnum.MISSING_SERVLET_REQUEST_PARAMETER, e, HttpStatus.BAD_REQUEST);
        }
        // 参数类型匹配失败。比如：接收参数为 Long 型，但传入的值确是一个字符串，那么将会出现类型转换失败的情况，这时会抛该异常。
        else if (e instanceof TypeMismatchException) {
            return resultFormat(ExceptionCodeEnum.TYPE_MISMATCH, e, HttpStatus.BAD_REQUEST);
        }
        // 与上面的 HttpMediaTypeNotSupportedException 举的例子完全相反。
        // 即请求头携带了"content-type: application/json;charset=UTF-8"，但接收参数却没有添加注解 @RequestBody，或者请求体携带的 json 串反序列化成 pojo 的过程中失败了，也会抛该异常。
        else if (e instanceof HttpMessageNotReadableException) {
            return resultFormat(ExceptionCodeEnum.HTTP_MESSAGE_NOT_READABLE, e, HttpStatus.BAD_REQUEST);
        }
        // 返回的 pojo 在序列化成 json 过程失败了，那么抛该异常。
        else if (e instanceof HttpMessageNotWritableException) {
            return resultFormat(ExceptionCodeEnum.CONVERSION_NOT_SUPPORTED, e, HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (e instanceof HttpMediaTypeNotAcceptableException) {
            return resultFormat(ExceptionCodeEnum.HTTP_MEDIA_TYPE_NOT_ACCEPTABLE, e, HttpStatus.NOT_ACCEPTABLE);
        }

        log.error(String.format(EX_FORMAT, e.getClass().getName(), e.getMessage()), e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }


    /**
     * Controller上一层相关异常.
     * 验参返回的异常（@Validated注解, 嵌套检验）
     *
     * @param ex ConstraintViolationException
     * @return ResponseEntity
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> constraintViolationException(ConstraintViolationException ex) {
        log.error(String.format(EX_FORMAT, ex.getClass().getName(), ex.getMessage()), ex);

        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        String message = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(";"));

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    /**
     * Controller上一层相关异常.
     * 参数校验异常（@RequestBody注解），将校验失败的所有异常组合成一条错误信息。
     *
     * @param ex 参数验证异常
     * @return ResponseEntity
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error(String.format(EX_FORMAT, ex.getClass().getName(), ex.getMessage()), ex);

        return wrapperBindingResult(ex.getBindingResult());
    }

    /**
     * Controller上一层相关异常.
     * 参数绑定异常，(只有@Valid在参数前，没有@RequestBody、@RequestParam)
     *
     * @param ex BindException
     * @return ResponseEntity
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> bindException(BindException ex) {
        log.error(String.format(EX_FORMAT, ex.getClass().getName(), ex.getMessage()), ex);

        return wrapperBindingResult(ex.getBindingResult());
    }

    /**
     * 按需重新封装需要返回的错误信息
     * <p>
     * 返回非法的字段名称，错误信息
     * </p>
     *
     * @param bindingResult 绑定结果
     * @return ResponseEntity
     */
    private ResponseEntity<?> wrapperBindingResult(BindingResult bindingResult) {
        StringBuilder msg = new StringBuilder();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError error : fieldErrors) {
            msg.append(error.getField()).append(": ");
            msg.append(
                    error.getDefaultMessage() == null ? "校验不通过" : error.getDefaultMessage()
            ).append(";");
        }
        return resultFormat(
                ExceptionCodeEnum.METHOD_ARGUMENT_NOT_VALID.getCode(),
                msg.toString(),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * 业务服务异常
     *
     * @param ex 业务异常
     * @return ResponseEntity
     */
    @ExceptionHandler({BizException.class})
    public ResponseEntity<?> businessException(BizException ex) {
        log.error(String.format(EX_FORMAT, ex.getClass().getName(), ex.getMessage()), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.OK);
    }


    /**
     * 运行时异常
     *
     * @param ex RuntimeException
     * @return ResponseEntity
     */
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<?> runtimeException(RuntimeException ex) {
        return resultFormat(ExceptionCodeEnum.RUNTIME, ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 空指针异常
     *
     * @param ex NullPointerException
     * @return ResponseEntity
     */
    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<?> nullPointerException(NullPointerException ex) {
        return resultFormat(ExceptionCodeEnum.NPE, ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 类型转换异常
     *
     * @param ex ClassCastException
     * @return ResponseEntity
     */
    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<?> classCastException(ClassCastException ex) {
        return resultFormat(ExceptionCodeEnum.CLASS_CAST, ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * IO异常
     *
     * @param ex IOException
     * @return ResponseEntity
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handlerIOException(IOException ex) {
        return resultFormat(ExceptionCodeEnum.IO, ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 当找不到特定方法时抛出异常
     *
     * @param ex NoSuchMethodException
     * @return ResponseEntity
     */
    @ExceptionHandler(NoSuchMethodException.class)
    public ResponseEntity<?> noSuchMethodException(NoSuchMethodException ex) {
        return resultFormat(ExceptionCodeEnum.NO_SUCH_METHOD, ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 数组越界异常
     *
     * @param ex IndexOutOfBoundsException
     * @return ResponseEntity
     */
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public ResponseEntity<?> indexOutOfBoundsException(IndexOutOfBoundsException ex) {
        return resultFormat(ExceptionCodeEnum.INDEX_OUT_OF_BOUNDS, ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 栈溢出
     *
     * @param ex StackOverflowError
     * @return ResponseEntity
     */
    @ExceptionHandler({StackOverflowError.class})
    public ResponseEntity<?> stackOverflowError(StackOverflowError ex) {
        return resultFormat(ExceptionCodeEnum.STACKOVERFLOW_ERROR, ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 其他错误
     *
     * @param ex Exception
     * @return ResponseEntity
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handleException(Exception ex) {
        return resultFormat(ExceptionCodeEnum.UNKNOWN_ERROR, ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 封装返回响应体 ResponseEntity
     *
     * @param code       业务状态码
     * @param msg        业务消息
     * @param httpStatus http状态码
     * @return ResponseEntity
     */
    private ResponseEntity<?> resultFormat(String code, String msg, HttpStatus httpStatus) {
        return new ResponseEntity<>(msg, httpStatus);
    }

    /**
     * 封装返回响应体 ResponseEntity
     *
     * @param baseResultEnum enum
     * @param ex             异常
     * @param <T>            异常类
     * @param httpStatus     http状态码
     * @return ResponseEntity
     */
    private <T extends Throwable> ResponseEntity<?> resultFormat(ExceptionCodeEnum baseResultEnum, T ex, HttpStatus httpStatus) {
        log.error(String.format(EX_FORMAT, ex.getClass().getName(), ex.getMessage()), ex);
        return new ResponseEntity<>(
//                ResultBOUtil.fail(baseResultEnum.getCode(), baseResultEnum.getMessage())
                baseResultEnum.getMessage(),
                httpStatus
        );
    }
}
