package com.github.martvey.excel.read.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.CellExtra;
import com.github.martvey.excel.read.chain.ReadListenerChain;
import com.github.martvey.excel.read.chain.ReadListenerChainBuilder;
import com.github.martvey.excel.read.listener.impl.ThrowErrorDataReadListener;

import java.util.Map;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/8/3 13:59
 */
public interface IReadListener<T> {
    /**
     * 初始化函数，参数由{@link ReadListenerChainBuilder#initListener(java.util.Map)}指定
     * @param properties  初始化参数
     * @param clazz
     */
    default void init(Map<String,Object> properties, Class<T> clazz){};

    /**
     * 发生异常会调用此函数
     * @param exception 异常对象
     * @param analysisContext 上下文
     * @param chain 调用链
     * @throws Exception
     */
    default void onException(Exception exception, AnalysisContext analysisContext, ReadListenerChain<T> chain) throws Exception{chain.onException(exception,analysisContext);};

    /**
     * 读取头部会调用此函数
     * @param headMap 头部信息
     * @param analysisContext 上下文
     * @param chain 调用链
     */
    default void invokeHead(Map<Integer, CellData> headMap, AnalysisContext analysisContext, ReadListenerChain<T> chain){chain.invokeHead(headMap,analysisContext);};

    /**
     * 每行数据会调用此函数
     * @param data 具体数据
     * @param analysisContext 上下文
     * @param chain 调用链
     */
    default void invoke(T data, AnalysisContext analysisContext, ReadListenerChain<T> chain){chain.invoke(data,analysisContext);};

    default void extra(CellExtra extra, AnalysisContext analysisContext, ReadListenerChain<T> chain){chain.extra(extra,analysisContext);};

    /**
     * 所有数据解析完调用此函数
     * @param analysisContext 上下文
     * @param chain 调用链
     */
    default void doAfterAllAnalysed(AnalysisContext analysisContext, ReadListenerChain<T> chain){chain.doAfterAllAnalysed(analysisContext);};

    /**
     * 是否有下行数据
     * @param analysisContext 上下文
     * @param chain 调用链
     * @return true有数据 false无数据
     */
    default boolean hasNext(AnalysisContext analysisContext, ReadListenerChain<T> chain){return chain.hasNext(analysisContext);};

    /**
     * 错误的数据被抛出会被此方法捕捉
     * @param data
     * @param analysisContext
     * @param chain
     */
    default void onErrorData(T data, AnalysisContext analysisContext, ReadListenerChain<T> chain){chain.onErrorData(data,analysisContext);};
}
