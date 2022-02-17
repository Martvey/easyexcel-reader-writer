package com.github.martvey.excel.read.annotation;

import com.github.martvey.excel.factory.ListenerFactory;
import com.github.martvey.excel.read.listener.IReadListener;

import java.lang.annotation.*;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/8/3 14:25
 * {@link IReadListener}的注解，通过{@link ListenerFactory}创建
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WithReadListeners {
    ReadListener[] value() default {};
}
