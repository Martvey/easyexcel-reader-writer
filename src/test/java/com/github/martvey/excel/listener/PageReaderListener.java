package com.github.martvey.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.github.martvey.excel.common.Order;
import com.github.martvey.excel.entity.ExcelPageInfo;
import com.github.martvey.excel.exception.ReadListenerInitException;
import com.github.martvey.excel.read.chain.ReadListenerChain;
import com.github.martvey.excel.read.listener.IReadListener;
import com.github.martvey.excel.read.listener.impl.ValidReadListener;

import java.util.Map;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2021/4/30 15:29
 */
public class PageReaderListener implements IReadListener<Object>, Order {
    public static String INIT_PAGE = "PageReaderListener@page";
    private Integer startRow;
    private Integer endRow;
    private ExcelPageInfo pageInfo;
    private Boolean hasNext = Boolean.TRUE;

    public PageReaderListener() {
    }

    @Override
    public void init(Map<String, Object> properties, Class<Object> clazz) {
        Object p = properties.get(INIT_PAGE);
        if (!(p instanceof ExcelPageInfo)){
            throw new ReadListenerInitException("PageReaderListener 监听器配置 PageReaderListener@page 为空或不为com.synway.martvey.demo.entity.ExcelPageInfo类型");
        }
        this.pageInfo = (ExcelPageInfo) p;
        this.startRow = pageInfo.getStartRow();
        this.endRow = pageInfo.getEndRow();
    }

    @Override
    public void invoke(Object data, AnalysisContext analysisContext, ReadListenerChain<Object> chain) {
        Integer rowIndex = analysisContext.readSheetHolder().getRowIndex();
        if (rowIndex < startRow){
            return;
        }
        if (rowIndex < endRow){
            pageInfo.add(data);
            return;
        }
        hasNext = Boolean.FALSE;
//        chain.invoke(); 没有调用chain的方法则不会执行后续监听器
    }

    @Override
    public boolean hasNext(AnalysisContext analysisContext, ReadListenerChain<Object> chain) {
        return hasNext;
    }

    @Override
    public int getOrder() {
        return ValidReadListener.VALID_ORDER - 1;
    }
}
