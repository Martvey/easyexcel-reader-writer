package com.github.martvey.excel.read.listener.impl;

import com.alibaba.excel.context.AnalysisContext;
import com.github.martvey.excel.common.Order;
import com.github.martvey.excel.read.chain.ReadListenerChain;
import com.github.martvey.excel.read.listener.IReadListener;

import java.util.function.BiFunction;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/8/31 16:05
 */
public class FilterRowReadListener<T> implements IReadListener<T>, Order {
    public static final int FILTER_ORDER = -5000;
    private BiFunction<T,AnalysisContext,Boolean> filter = (t,context) -> {throw new RuntimeException("未指定过滤条件");};

    @Override
    public void invoke(T data, AnalysisContext analysisContext, ReadListenerChain<T> chain) {
        if (filter.apply(data,analysisContext)){
            chain.invoke(data,analysisContext);
        }
    }

    public void setFilter(BiFunction<T, AnalysisContext, Boolean> filter) {
        this.filter = filter;
    }

    @Override
    public int getOrder() {
        return FILTER_ORDER;
    }
}
