package com.github.maoxp.core.exception;

/**
 * Class: BizException
 *
 * @author mxp
 * @date 2022年07月27日 10:30
 * @since JDK 1.8
 */
public class BizException extends RuntimeException {
    private static final long serialVersionUID = 3207132025253776163L;

    public BizException(String msg) {
        super(msg);
    }
    public BizException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
