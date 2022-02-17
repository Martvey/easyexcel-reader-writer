package com.github.martvey.excel.read.listener.impl;

import com.alibaba.excel.util.CollectionUtils;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/8/3 19:20
 */
public class BlankRowReadListener<T> extends FilterRowReadListener<T> {
    public BlankRowReadListener() {
        setFilter((t,context) -> !CollectionUtils.isEmpty(context.readRowHolder().getCellMap()));
    }
}
