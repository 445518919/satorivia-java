package com.satoriviacafe.common.exception;

import java.io.Serial;

/**
 * 演示模式异常
 *
 * @author satoriviacafe
 */
public class DemoModeException extends ServiceException {
    @Serial
    private static final long serialVersionUID = 1L;

    public DemoModeException() {
        super("demo.mode.not.supported");
    }
}
