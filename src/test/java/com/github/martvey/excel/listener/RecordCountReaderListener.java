package com.github.martvey.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.github.martvey.excel.common.Order;
import com.github.martvey.excel.read.chain.ReadListenerChain;
import com.github.martvey.excel.read.listener.IReadListener;
import com.github.martvey.excel.read.listener.impl.ValidReadListener;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2021/5/11 11:18
 */
public class RecordCountReaderListener implements IReadListener<Object>, Order {
    private int count;
    private int errCount;
    public RecordCountReaderListener() {
        this.count = 0;
        this.errCount = 0;
    }

    @Override
    public void invoke(Object data, AnalysisContext analysisContext, ReadListenerChain<Object> chain) {
        count++;
        chain.invoke(data, analysisContext);
    }

    @Override
    public void onErrorData(Object data, AnalysisContext analysisContext, ReadListenerChain<Object> chain) {
        errCount++;
        chain.onErrorData(data,analysisContext);
    }

    public String getMessage(){
        return "总计" + count + "条，" + "成功" + (count - errCount) + "条，" + "失败" + errCount + "条";
    }

    @Override
    public int getOrder() {
        return ValidReadListener.VALID_ORDER - 1;
    }
}
