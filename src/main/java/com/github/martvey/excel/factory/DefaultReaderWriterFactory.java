package com.github.martvey.excel.factory;

import com.github.martvey.excel.write.handle.IWriterHandler;
import com.github.martvey.excel.read.listener.IReadListener;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/8/3 16:48
 * 默认的 {@link IWriterHandler}和{@link IReadListener}创建工厂
 */
public class DefaultReaderWriterFactory implements ListenerFactory,HandlerFactory {
    @Override
    public <T> IReadListener<T> getListener(Class<? extends IReadListener> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public IWriterHandler getHandler(Class<? extends IWriterHandler> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
