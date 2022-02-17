package com.github.martvey.excel.factory;

import com.github.martvey.excel.write.handle.IWriterHandler;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/8/4 10:59
 * {@link IWriterHandler}工厂接口
 */
public interface HandlerFactory {
    /**
     * 创建 {@link IWriterHandler}
     * @param clazz
     * @return
     */
    IWriterHandler getHandler(Class<? extends IWriterHandler> clazz);
}
