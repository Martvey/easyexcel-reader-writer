package com.github.martvey.excel.read.chain.impl;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.CellExtra;
import com.github.martvey.excel.read.chain.ReadListenerChain;
import com.github.martvey.excel.read.listener.IReadListener;

import java.util.List;
import java.util.Map;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/8/3 13:55
 */
public class ReadListenerChainImpl<T> implements ReadListenerChain<T> {
    private final int index;
    private final List<IReadListener<T>> listeners;
    private ReadListenerChain<T> nextChain;

    public ReadListenerChainImpl(List<IReadListener<T>> listeners) {
        this.index = 0;
        this.listeners = listeners;
    }

    public ReadListenerChainImpl(ReadListenerChain<T> parent, int index){
        this.index = index;
        this.listeners = ((ReadListenerChainImpl<T>) parent).listeners;
    }

    @Override
    public void onException(Exception e, AnalysisContext analysisContext) throws Exception {
        if (index < listeners.size()){
            IReadListener<T> iReadListener = listeners.get(index);
            iReadListener.onException(e,analysisContext,next());
        }
    }

    @Override
    public void invokeHead(Map<Integer, CellData> map, AnalysisContext analysisContext) {
        if (index < listeners.size()){
            IReadListener<T> iReadListener = listeners.get(index);
            iReadListener.invokeHead(map,analysisContext,next());
        }
    }

    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        if (index < listeners.size()){
            IReadListener<T> iReadListener = listeners.get(index);
            iReadListener.invoke(t,analysisContext,next());
        }
    }

    @Override
    public void extra(CellExtra cellExtra, AnalysisContext analysisContext) {
        if (index < listeners.size()){
            IReadListener<T> iReadListener = listeners.get(index);
            iReadListener.extra(cellExtra,analysisContext,next());
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (index < listeners.size()){
            IReadListener<T> iReadListener = listeners.get(index);
            iReadListener.doAfterAllAnalysed(analysisContext,next());
        }
    }

    @Override
    public void onErrorData(T data, AnalysisContext analysisContext){
        if (index < listeners.size()){
            IReadListener<T> iReadListener = listeners.get(index);
            iReadListener.onErrorData(data,analysisContext,next());
        }
    }

    @Override
    public ReadListenerChain<T> skip(){
        return next();
    }

    private ReadListenerChain<T> next(){
        if (nextChain != null)
            return nextChain;
        return nextChain = new ReadListenerChainImpl<>(this, index + 1);
    }

    @Override
    public boolean hasNext(AnalysisContext analysisContext) {
        if (index < listeners.size()){
            IReadListener<T> iReadListener = listeners.get(index);
            return iReadListener.hasNext(analysisContext,next());
        }
        return true;
    }

}
