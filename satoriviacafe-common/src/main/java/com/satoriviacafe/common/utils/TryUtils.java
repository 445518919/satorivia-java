package com.satoriviacafe.common.utils;

import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

/**
 *
 *
 * @author shy
 * @since 2025年12月16日
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TryUtils {
    public static void tryRun(@NonNull final Runnable task, @NonNull final String errorMsg) {
        try {
            task.run();
        } catch (Throwable ex) {
            log.error(errorMsg, ex);
        }
    }

    public static <T> @Nullable T tryRun(@NonNull final Callable<? extends @NonNull T> task, @NonNull final String errorMsg) {
        try {
            return task.call();
        } catch (Throwable ex) {
            log.error(errorMsg, ex);
        }
        return null;
    }

    public static void tryRun(@NonNull final Runnable task, @NonNull final String errorMsg, @NonNull final Runnable finallyTask) {
        try {
            task.run();
        } catch (Throwable ex) {
            log.error(errorMsg, ex);
        } finally {
            finallyTask.run();
        }
    }
}
