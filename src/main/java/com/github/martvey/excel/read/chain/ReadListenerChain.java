package com.github.martvey.excel.read.chain;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/9/10 19:33
 */
public interface ReadListenerChain<T> extends ReadListener<T> {
    void onErrorData(T data, AnalysisContext analysisContext);
    ReadListenerChain<T> skip();
}
