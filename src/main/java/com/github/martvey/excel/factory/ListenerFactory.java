package com.github.martvey.excel.factory;

import com.github.martvey.excel.read.listener.IReadListener;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/8/3 16:20
 * {@link IReadListener}工厂接口
 */
public interface ListenerFactory {
    /**
     * 创建{@link IReadListener}
     * @param clazz
     * @param <T>
     * @return
     */
    <T> IReadListener<T> getListener(Class<? extends IReadListener> clazz);
}
