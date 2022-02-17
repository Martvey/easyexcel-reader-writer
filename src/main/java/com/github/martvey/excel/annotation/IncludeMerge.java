package com.github.martvey.excel.annotation;

import java.lang.annotation.*;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/9/23 13:50
 * 需要合并的字段，不能和{@link ExcludeMerge}注解合用
 * @see com.github.martvey.excel.write.handle.impl.CellMergeWriteHandler
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IncludeMerge {
}
