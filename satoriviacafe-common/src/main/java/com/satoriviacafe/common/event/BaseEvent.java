package com.satoriviacafe.common.event;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * @author shy
 * @since 2025年05月28日
 */
public abstract class BaseEvent<T> extends ApplicationEvent {
    public BaseEvent(T source) {
        super(source);
    }

    public BaseEvent(T source, Clock clock) {
        super(source, clock);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getSource() {
        if (source == null) {
            throw new IllegalStateException("Event source cannot be null");
        }
        return (T) source;
    }
}
