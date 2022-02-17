package com.github.martvey.excel.write.handle;

import java.util.Map;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/8/4 9:41
 */
public interface IWriterHandler {
    default void init(Map<String,Object> properties, Class<?> clazz){};
}
