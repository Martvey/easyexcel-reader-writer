package com.github.martvey.excel.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentFontStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;

import static org.apache.poi.ss.usermodel.Font.COLOR_RED;

/**
 * @author VVnnl
 * @version 1.0
 * @date 2020/6/22 14:11
 * excel bean对象继承此类会记录错误信息
 */

public class IExcelError {
    @ColumnWidth(40)
    @ExcelProperty("错误信息")
    @HeadFontStyle(color = COLOR_RED)
    @ContentFontStyle(fontName = "宋体",fontHeightInPoints = 12,color = COLOR_RED)
    private String errMsg;

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
