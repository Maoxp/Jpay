package com.github.maoxp.core.exception;

import lombok.NoArgsConstructor;

/**
 * BizException
 *
 * @author mxp
 * @date 2022年07月27日 10:30
 * @since JDK 1.8
 */
@NoArgsConstructor
public class BizException extends RuntimeException {
    private static final long serialVersionUID = 3207132025253776163L;

    public BizException(String message) {
        super(message);
    }

    public BizException(Throwable cause) {
        super(cause);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
