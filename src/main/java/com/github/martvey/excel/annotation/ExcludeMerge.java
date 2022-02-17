package com.github.martvey.excel.annotation;

import java.lang.annotation.*;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/9/23 13:51
 * 排除的字段，其他字段都会合并，不能和{@link IncludeMerge}注解合用
 * @see com.github.martvey.excel.write.handle.impl.CellMergeWriteHandler
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcludeMerge {
}
