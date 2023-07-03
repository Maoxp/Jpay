package com.github.maoxp.core.exception;

import lombok.NoArgsConstructor;

/**
 * CheckedException
 *
 * @author mxp
 * @date 2023/7/3年07月03日 14:07
 * @since JDK 1.8
 */
@NoArgsConstructor
public class CheckedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CheckedException(String message) {
        super(message);
    }

    public CheckedException(Throwable cause) {
        super(cause);
    }

    public CheckedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
