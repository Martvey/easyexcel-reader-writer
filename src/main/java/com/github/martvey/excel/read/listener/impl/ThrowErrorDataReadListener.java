package com.github.martvey.excel.read.listener.impl;

import com.alibaba.excel.context.AnalysisContext;
import com.github.martvey.excel.read.chain.ReadListenerChain;
import com.github.martvey.excel.read.chain.impl.ReadListenerChainImpl;
import com.github.martvey.excel.read.listener.IReadListener;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/9/10 19:46
 */
public abstract class ThrowErrorDataReadListener<T> implements IReadListener<T> {
    private ReadListenerChain<T> errorChain;
    protected void throwErrorData(T data, AnalysisContext analysisContext, ReadListenerChain<T> chain){
        if (errorChain == null) {
            errorChain = new ReadListenerChainImpl<>(chain, 0);
        }
        errorChain.onErrorData(data, analysisContext);
    }
}
