package com.github.martvey.excel.read.annotation;

import com.github.martvey.excel.read.listener.IReadListener;

import java.lang.annotation.*;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/8/3 14:26
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReadListener {
    /**
     * 具体的{@link IReadListener}类型
     */
    Class<? extends IReadListener> value();

    /**
     *
     * @return 顺序
     */
    int order();
}
