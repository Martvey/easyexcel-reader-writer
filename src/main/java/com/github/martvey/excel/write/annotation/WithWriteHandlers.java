package com.github.martvey.excel.write.annotation;

import com.github.martvey.excel.factory.HandlerFactory;
import com.github.martvey.excel.write.handle.IWriterHandler;

import java.lang.annotation.*;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/8/4 8:29
 * {@link IWriterHandler}的注解，通过{@link HandlerFactory}创建
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WithWriteHandlers {
    WriteHandler[] value() default {};
}
