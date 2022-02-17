package com.github.martvey.excel.read.listener.impl;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.util.CollectionUtils;
import com.github.martvey.excel.common.Order;
import com.github.martvey.excel.entity.IExcelError;
import com.github.martvey.excel.read.chain.ReadListenerChain;
import com.github.martvey.excel.read.listener.IReadListener;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/8/3 14:45
 */
public class ErrorDataReadListener<T extends IExcelError> implements IReadListener<T>, Order {
    public static final int ERROR_DATA_ORDER = Order.HIGHEST_PRECEDENCE;
    private List<T> errorDataList;
    private Consumer<List<T>> operate = data -> {throw new RuntimeException("错误数据处理具柄不能为空");};

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext, ReadListenerChain<T> chain) {
        if (!CollectionUtils.isEmpty(errorDataList)) {
            operate.accept(errorDataList);
        }
        chain.doAfterAllAnalysed(analysisContext);
    }

    public void setOperate(Consumer<List<T>> operate) {
        this.operate = operate;
    }

    @Override
    public int getOrder() {
        return ERROR_DATA_ORDER;
    }

    @Override
    public void onErrorData(T data, AnalysisContext analysisContext, ReadListenerChain<T> chain) {
        if (CollectionUtils.isEmpty(errorDataList)) {
            errorDataList = new LinkedList<>();
        }
        errorDataList.add(data);
        chain.onErrorData(data,analysisContext);
    }
}
