package com.github.martvey.excel.common;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/8/3 14:47
 */
public interface Order {
    int HIGHEST_PRECEDENCE = -2147483648;
    int LOWEST_PRECEDENCE = 2147483647;

    int getOrder();
}
