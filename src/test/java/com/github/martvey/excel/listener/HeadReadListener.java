package com.github.martvey.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.CellData;
import com.github.martvey.excel.common.Order;
import com.github.martvey.excel.exception.ReadListenerInitException;
import com.github.martvey.excel.read.chain.ReadListenerChain;
import com.github.martvey.excel.read.listener.IReadListener;
import com.github.martvey.excel.read.listener.impl.ValidReadListener;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2021/5/10 17:41
 */
public class HeadReadListener implements IReadListener<Object>, Order {
    public static final String INIT_HEAD_LIST = "HeadReadListener@headList";
    private List<String> headList;

    @Override
    public void init(Map<String, Object> properties, Class<Object> clazz) {
        Object o = properties.get(INIT_HEAD_LIST);
        if (!(o instanceof List)){
            throw new ReadListenerInitException("HeadReadListener 监听器的 HeadReadListener 配置为空或非 java.util.List 类型");
        }
        this.headList = (List) o;
    }

    @Override
    public void invokeHead(Map<Integer, CellData> headMap, AnalysisContext analysisContext, ReadListenerChain<Object> chain) {
        List<String> list = headMap.values()
                .stream()
                .map(CellData::getStringValue)
                .collect(Collectors.toList());
        headList.addAll(list);
    }

    @Override
    public int getOrder() {
        return ValidReadListener.VALID_ORDER - 1;
    }
}
