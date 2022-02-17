package com.github.martvey.excel.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import com.github.martvey.excel.annotation.IncludeMerge;
import com.github.martvey.excel.listener.PersonSaveReadListener;
import com.github.martvey.excel.read.annotation.ReadListener;
import com.github.martvey.excel.read.annotation.WithReadListeners;
import com.github.martvey.excel.write.annotation.WithWriteHandlers;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;

import javax.validation.constraints.NotBlank;


@HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND, fillForegroundColor = 1)
@HeadRowHeight(20)
@HeadFontStyle(fontHeightInPoints = 12, bold = false)
@ContentStyle(dataFormat = 49, borderTop = BorderStyle.THIN, borderRight = BorderStyle.THIN, borderBottom = BorderStyle.THIN, borderLeft = BorderStyle.THIN)
@ContentFontStyle(fontName = "宋体", fontHeightInPoints = 12)
@ExcelIgnoreUnannotated
@WithReadListeners(value = {
        @ReadListener(value = PersonSaveReadListener.class, order = 100)
})
public class PersonXls extends IExcelError{
    @ColumnWidth(10)
    @ExcelProperty("姓名")
    @NotBlank(message = "姓名不能为空")
    private String name;
    @ColumnWidth(10)
    @ExcelProperty("身份证")
    private String idCard;
    @ColumnWidth(10)
    @ExcelProperty("住址")
    @IncludeMerge
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "PersonXls{" +
                "name='" + name + '\'' +
                ", idCard='" + idCard + '\'' +
                ", address='" + address + '\'' +
                ", errMsg='" + getErrMsg() + '\'' + '}';
    }

    public PersonXls() {
    }

    public PersonXls(String name, String idCard, String address) {
        this.name = name;
        this.idCard = idCard;
        this.address = address;
    }
}