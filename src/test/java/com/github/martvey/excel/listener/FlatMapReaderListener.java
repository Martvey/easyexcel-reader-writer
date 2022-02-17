package com.github.martvey.excel.listener;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.github.martvey.excel.common.Order;
import com.github.martvey.excel.exception.ReadListenerInitException;
import com.github.martvey.excel.read.chain.ReadListenerChain;
import com.github.martvey.excel.read.listener.IReadListener;
import com.github.martvey.excel.read.listener.impl.ValidReadListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2021/4/30 14:34
 */
public class FlatMapReaderListener implements IReadListener<Object>, Order {
    public static String INIT_FLAT_FILE_DIR = "FlatMapReaderListener@flatFileDir";
    public static String PROPERTIES_FILE_NAME = "data_menu.properties";
    private List<Object> dataList;
    private int dataCount;
    private int errDataCount;
    private List<Object> errDataList;
    private ExcelWriter dataExcelWriter;
    private WriteSheet dataExcelSheet;
    private ExcelWriter errDataExcelWriter;
    private WriteSheet errDataSheet;
    private File dataFile;
    private File errDataFile;
    private File flatFileDir;

    @Override
    public void init(Map<String, Object> properties, Class<Object> clazz) {
        Object o = properties.get(INIT_FLAT_FILE_DIR);
        if (!(o instanceof File)){
            throw new ReadListenerInitException("FlatMapReaderListener 监听器的 FlatMapReaderListener@flatFileDir 配置为空或不为 java.io.File 类型");
        }
        this.flatFileDir = (File) o;

        this.dataList = new LinkedList<>();
        this.errDataList = new LinkedList<>();
        try {
            this.dataFile = File.createTempFile("NORMAL_DATE_", ".xlsx", flatFileDir);
            this.errDataFile = File.createTempFile("ERROR_DATE_", ".xlsx", flatFileDir);
        }catch (IOException e){
            throw new ReadListenerInitException("创建正常数据文件和异常数据文件错误");
        }

        dataExcelWriter = EasyExcel.write(dataFile, clazz)
                .needHead(Boolean.TRUE)
                .build();
        dataExcelSheet = EasyExcel.writerSheet().build();
        dataExcelWriter.write(Collections.emptyList(), dataExcelSheet);

        errDataExcelWriter = EasyExcel.write(errDataFile, clazz)
                .needHead(Boolean.TRUE)
                .build();
        errDataSheet = EasyExcel.writerSheet().build();
        errDataExcelWriter.write(Collections.emptyList(), errDataSheet);
    }

    @Override
    public void invoke(Object data, AnalysisContext analysisContext, ReadListenerChain<Object> chain) {
        if (dataList.size() < 1000){
            dataCount ++;
            dataList.add(data);
            return;
        }
        dataExcelWriter.write(dataList, dataExcelSheet);
        dataList.clear();
    }

    @Override
    public void onErrorData(Object data, AnalysisContext analysisContext, ReadListenerChain<Object> chain) {
        if (errDataList.size() < 1000){
            errDataCount ++;
            errDataList.add(data);
            return;
        }
        errDataExcelWriter.write(errDataList, errDataSheet);
        errDataList.clear();
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext, ReadListenerChain<Object> chain) {
        if (dataList.size() > 0) {
            dataExcelWriter.write(dataList, dataExcelSheet);
            dataList.clear();
        }

        if (errDataList.size() > 0){
            errDataExcelWriter.write(errDataList, errDataSheet);
            errDataList.clear();
        }

        dataExcelWriter.finish();
        errDataExcelWriter.finish();
        storeProperties();
    }

    private void storeProperties() {
        Properties properties = new Properties();
        properties.setProperty("dataFile",dataFile.getName());
        properties.setProperty("errDataFile",errDataFile.getName());
        properties.setProperty("dataCount",String.valueOf(dataCount));
        properties.setProperty("errDataCount",String.valueOf(errDataCount));
        try {
            properties.store(new FileWriter(new File(flatFileDir, PROPERTIES_FILE_NAME)), null);
        } catch (IOException ignore) {

        }
    }

    @Override
    public int getOrder() {
        return ValidReadListener.VALID_ORDER + 1;
    }
}
