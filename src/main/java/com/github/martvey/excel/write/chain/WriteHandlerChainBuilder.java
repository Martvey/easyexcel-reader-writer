package com.github.martvey.excel.write.chain;

import com.github.martvey.excel.common.Order;
import com.github.martvey.excel.common.OrderProxy;
import com.github.martvey.excel.factory.HandlerFactory;
import com.github.martvey.excel.write.annotation.WithWriteHandlers;
import com.github.martvey.excel.write.annotation.WriteHandler;
import com.github.martvey.excel.write.handle.IWriterHandler;
import com.github.martvey.excel.write.chain.impl.WriteHandlerChainImpl;

import java.util.*;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/8/4 8:39
 */
public class WriteHandlerChainBuilder<T> {
    private final List<IWriterHandler> handlers;
    private final Class<T> clazz;
    private final HandlerFactory factory;
    private Map<String,Object> properties = Collections.emptyMap();

    public WriteHandlerChainBuilder(Class<T> clazz, HandlerFactory factory) {
        this.clazz = clazz;
        this.factory = factory;
        this.handlers =  new LinkedList<>();

        if(clazz.isAnnotationPresent(WithWriteHandlers.class)){
            WithWriteHandlers withWriteHandlers = clazz.getAnnotation(WithWriteHandlers.class);
            WriteHandler[] value = withWriteHandlers.value();
            Arrays.sort(value,Comparator.comparingInt(WriteHandler::order));
            for (WriteHandler writeHandler : value) {
                IWriterHandler handler = new OrderProxy<>(factory.getHandler(writeHandler.value()),writeHandler.order()).getProxy();
                this.handlers.add(handler);
            }
        }
    }

    public WriteHandlerChainBuilder<T> initHandler(Map<String,Object> properties){
        this.properties = properties;
        return this;
    }

    public WriteHandlerChainBuilder<T> addHandler(Class<? extends IWriterHandler> clazz){
        IWriterHandler iWriterHandler = factory.getHandler(clazz);
        this.handlers.add(iWriterHandler);
        return this;
    }

    public WriteHandlerChainBuilder<T> addHandler(Class<? extends IWriterHandler> clazz, Integer order){
        IWriterHandler iWriterHandler = factory.getHandler(clazz);
        iWriterHandler = new OrderProxy<>(iWriterHandler,order).getProxy();
        this.handlers.add(iWriterHandler);
        return this;
    }

    public WriteHandlerChainBuilder<T> addHandler(IWriterHandler iWriterHandler){
        this.handlers.add(iWriterHandler);
        return this;
    }

    public WriteHandlerChainBuilder<T> addHandler(IWriterHandler iWriterHandler, Integer order){
        iWriterHandler = new OrderProxy<>(iWriterHandler,order).getProxy();
        this.handlers.add(iWriterHandler);
        return this;
    }

    public WriteHandlerChainImpl build(){
        handlers.sort(Comparator.comparingInt(this::getOrder));
        for (IWriterHandler handler : handlers) {
            handler.init(properties, clazz);
        }
        return new WriteHandlerChainImpl(handlers);
    }

    private Integer getOrder(IWriterHandler handler){
        return handler instanceof Order ? ((Order)handler).getOrder() : Integer.MAX_VALUE;
    }
}
