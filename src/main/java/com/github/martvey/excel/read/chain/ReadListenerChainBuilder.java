package com.github.martvey.excel.read.chain;

import com.alibaba.excel.context.AnalysisContext;
import com.github.martvey.excel.common.Order;
import com.github.martvey.excel.common.OrderProxy;
import com.github.martvey.excel.factory.ListenerFactory;
import com.github.martvey.excel.read.annotation.ReadListener;
import com.github.martvey.excel.read.annotation.WithReadListeners;
import com.github.martvey.excel.read.listener.impl.ErrorDataReadListener;
import com.github.martvey.excel.read.listener.impl.FilterRowReadListener;
import com.github.martvey.excel.read.listener.impl.ValidReadListener;
import com.github.martvey.excel.entity.IExcelError;
import com.github.martvey.excel.read.chain.impl.ReadListenerChainImpl;
import com.github.martvey.excel.read.listener.IReadListener;
import com.github.martvey.excel.read.listener.impl.BlankRowReadListener;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/8/3 15:05
 */
public class ReadListenerChainBuilder<T> {
    private final ListenerFactory factory;
    private final List<IReadListener<T>> listeners;
    private final Class<T> clazz;
    private Map<String,Object> properties = Collections.emptyMap();

    public ReadListenerChainBuilder(Class<T> clazz, ListenerFactory factory) {
        this.clazz = clazz;
        this.factory = factory;
        this.listeners = new LinkedList<>();

        if (clazz.isAnnotationPresent(WithReadListeners.class)) {
            WithReadListeners withReadListeners = clazz.getAnnotation(WithReadListeners.class);
            ReadListener[] value = withReadListeners.value();
            for (ReadListener readListener : value) {
                IReadListener<T> iReadListener = factory.getListener(readListener.value());
                iReadListener = new OrderProxy<>(iReadListener,readListener.order()).getProxy();
                this.listeners.add(iReadListener);
            }
        }
    }

    public ReadListenerChainBuilder<T> initListener(Map<String,Object> properties){
        this.properties = properties;
        return this;
    }

    public ReadListenerChainBuilder<T> addListener(Class<? extends IReadListener<?>> clazz){
        IReadListener<T> iReadListener = factory.getListener(clazz);
        listeners.add(iReadListener);
        return this;
    }

    public ReadListenerChainBuilder<T> addListener(Class<? extends IReadListener<?>> clazz, Integer order){
        IReadListener<T> iReadListener = factory.getListener(clazz);
        iReadListener = new OrderProxy<>(iReadListener, order).getProxy();
        listeners.add(iReadListener);
        return this;
    }

    public ReadListenerChainBuilder<T> addListener(IReadListener<T> listener){
        listeners.add(listener);
        return this;
    }

    public ReadListenerChainBuilder<T> addListener(IReadListener<T> listener, Integer order){
        listener = new OrderProxy<>(listener, order).getProxy();
        listeners.add(listener);
        return this;
    }

    @SuppressWarnings("unchecked")
    public ReadListenerChainBuilder<T> doValid(Function<T,List<String>> valid){
        if (!IExcelError.class.isAssignableFrom(clazz)){
            throw new RuntimeException("如需校验，" + clazz + "请继承自" + IExcelError.class);
        }
        ValidReadListener validReadListener = new ValidReadListener<>();
        validReadListener.setValidFunction(valid);
        listeners.add(validReadListener);
        return this;
    }

    @SuppressWarnings("unchecked")
    public ReadListenerChainBuilder<T> doValid(){
        if (!IExcelError.class.isAssignableFrom(clazz)){
            throw new RuntimeException("如需校验，" + clazz + "请继承自" + IExcelError.class);
        }
        ValidReadListener validReadListener = new ValidReadListener<>();
        listeners.add(validReadListener);
        return this;
    }

    @SuppressWarnings("unchecked")
    public ReadListenerChainBuilder<T> ifHasErrData(Consumer<List<T>> operate){
        ErrorDataReadListener errorDataReadListener = new ErrorDataReadListener<>();
        errorDataReadListener.setOperate(operate);
        listeners.add(errorDataReadListener);
        return this;
    }

    public ReadListenerChainBuilder<T> filterBlank(){
        BlankRowReadListener<T> blankRowReadListener = new BlankRowReadListener<>();
        listeners.add(blankRowReadListener);
        return this;
    }

    public ReadListenerChainBuilder<T> filter(BiFunction<T, AnalysisContext, Boolean> filter){
        FilterRowReadListener<T> filterRowReadListener = new FilterRowReadListener<>();
        filterRowReadListener.setFilter(filter);
        listeners.add(filterRowReadListener);
        return this;
    }

    public ReadListenerChain<T> build(){
        listeners.sort(Comparator.comparingInt(this::getOrder));
        for (IReadListener<T> listener : listeners) {
            listener.init(properties,clazz);
        }
        return new ReadListenerChainImpl<>(listeners);
    }

    private Integer getOrder(IReadListener<T> listener){
        return listener instanceof Order ? ((Order)listener).getOrder() : Integer.MAX_VALUE;
    }
}
