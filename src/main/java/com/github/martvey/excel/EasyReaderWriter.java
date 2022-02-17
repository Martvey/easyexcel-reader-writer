package com.github.martvey.excel;

import com.github.martvey.excel.factory.HandlerFactory;
import com.github.martvey.excel.factory.ListenerFactory;
import com.github.martvey.excel.read.chain.ReadListenerChainBuilder;
import com.github.martvey.excel.write.chain.WriteHandlerChainBuilder;


/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/8/3 16:17
 */
public class EasyReaderWriter {

    public static <T> ReadListenerChainBuilder<T> initReadListener(Class<T> clazz, ListenerFactory factory){
        return new ReadListenerChainBuilder<>(clazz,factory);
    }

    public static <T> WriteHandlerChainBuilder<T> initWriteHandler(Class<T> clazz, HandlerFactory factory){
        return new WriteHandlerChainBuilder(clazz, factory);
    }
}
