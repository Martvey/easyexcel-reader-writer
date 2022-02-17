package com.github.martvey.excel.write.annotation;

import com.github.martvey.excel.write.handle.IWriterHandler;

import java.lang.annotation.*;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/8/4 8:30
 * 指定{@link IWriterHandler}的注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WriteHandler {
    /**
     * 类型
     * @return 需要创建的{@link IWriterHandler}类
     */
    Class<? extends IWriterHandler> value();

    /**
     * 排序升序
     * @return 序号
     */
    int order();
}
