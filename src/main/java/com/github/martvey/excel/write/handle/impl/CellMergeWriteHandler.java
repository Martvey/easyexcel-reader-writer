package com.github.martvey.excel.write.handle.impl;

import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.alibaba.excel.write.property.ExcelWriteHeadProperty;
import com.github.martvey.excel.annotation.ExcludeMerge;
import com.github.martvey.excel.annotation.IncludeMerge;
import com.github.martvey.excel.write.chain.IRowWriteHandlerChain;
import com.github.martvey.excel.write.chain.ISheetWriteHandlerChain;
import com.github.martvey.excel.write.handle.IRowWriteHandler;
import com.github.martvey.excel.write.handle.ISheetWriteHandler;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/9/23 13:41
 */
public class CellMergeWriteHandler implements IRowWriteHandler, ISheetWriteHandler {
    public static final String INIT_DATA_SIZE = "CellMergeWriteHandler@dataSize";
    private Row rowPointer;
    private List<ExcelContentProperty> needMergePropertyList;
    private Long dateSize;
    private boolean needMerge = true;
    private Map<ExcelContentProperty, CellRangeAddress> propertyMapAddress;

    @Override
    public void init(Map<String, Object> properties, Class<?> clazz) {
        Object o = properties.get(INIT_DATA_SIZE);
        if (o == null){
            throw new RuntimeException("CellMergeWriteHandler 监听器的 CellMergeWriteHandler@dataSize 配置为空");
        }
        this.dateSize = (Long) o;

    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder, ISheetWriteHandlerChain chain) {
        List<ExcelContentProperty> normalPropertyList = new LinkedList<>();
        List<ExcelContentProperty> excludeMergePropertyList = new LinkedList<>();
        List<ExcelContentProperty> includeMergePropertyList = new LinkedList<>();

        ExcelWriteHeadProperty excelWriteHeadProperty = writeSheetHolder.getExcelWriteHeadProperty();
        Map<Integer, ExcelContentProperty> contentPropertyMap = excelWriteHeadProperty.getContentPropertyMap();
        for (ExcelContentProperty property : contentPropertyMap.values()) {
            if (!property.getField().isAnnotationPresent(IncludeMerge.class)
                    && !property.getField().isAnnotationPresent(ExcludeMerge.class)){
                normalPropertyList.add(property);
                continue;
            }
            if (property.getField().isAnnotationPresent(IncludeMerge.class)) {
                includeMergePropertyList.add(property);
            }
            if (property.getField().isAnnotationPresent(ExcludeMerge.class)){
                excludeMergePropertyList.add(property);
            }
        }

        if (excludeMergePropertyList.size() > 0 && includeMergePropertyList.size() > 0){
            throw new RuntimeException("com.github.martvey.excel.annotation.ExcludeMerge 和 com.github.martvey.excel.annotation.IncludeMerge 不能同时存在");
        }

        if (excludeMergePropertyList.size() == 0 && includeMergePropertyList.size() == 0){
            this.needMerge = false;
            chain.beforeSheetCreate(writeWorkbookHolder, writeSheetHolder);
            return;
        }

        if (excludeMergePropertyList.size() > 0){
            needMergePropertyList =  normalPropertyList;
        }

        if (includeMergePropertyList.size() > 0){
            needMergePropertyList = includeMergePropertyList;
        }

        propertyMapAddress = new HashMap<>();
        for (ExcelContentProperty property : needMergePropertyList) {
            propertyMapAddress.put(property,null);
        }
        chain.beforeSheetCreate(writeWorkbookHolder, writeSheetHolder);
    }

    @Override
    public void afterRowDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Integer integer, Boolean isHead, IRowWriteHandlerChain chain) {
        if (!needMerge){
            chain.afterRowDispose(writeSheetHolder,writeTableHolder,row,integer,isHead);
        }
        if (!isHead){
            if (isRowCellValueEqual(rowPointer,row)) {
                updateRangeAddress(row.getRowNum());
            }else{
                createOrFlushRangeAddress2Sheet(writeSheetHolder.getSheet(), row.getRowNum());
            }
            rowPointer = row;
        }
        if (row.getRowNum() == dateSize){
            createOrFlushRangeAddress2Sheet(writeSheetHolder.getSheet(),row.getRowNum());
        }
        chain.afterRowDispose(writeSheetHolder,writeTableHolder,row,integer,isHead);
    }

    private void createOrFlushRangeAddress2Sheet(Sheet sheet, Integer rowIndex){
        for (ExcelContentProperty property : needMergePropertyList) {
            Integer columnIndex = property.getHead().getColumnIndex();
            CellRangeAddress cellRangeAddress = propertyMapAddress.computeIfAbsent(property, key -> new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex));
            if (cellRangeAddress.getNumberOfCells() > 1){
                sheet.addMergedRegionUnsafe(cellRangeAddress);
            }
            cellRangeAddress.setFirstRow(rowIndex);
            cellRangeAddress.setLastRow(rowIndex);
        }
    }

    private void updateRangeAddress(Integer rowIndex){
        for (ExcelContentProperty property : needMergePropertyList) {
            CellRangeAddress cellRangeAddress = propertyMapAddress.get(property);
            cellRangeAddress.setLastRow(rowIndex);
        }
    }

    private boolean isRowCellValueEqual(Row r1, Row r2){
        if (r1 == null && r2 == null){
            return true;
        }
        if (r1 == null || r2 == null){
            return false;
        }
        if (r1 == r2) {
            return true;
        }
        return  needMergePropertyList.stream()
                            .allMatch(property -> {
                                Integer cellIndex = property.getHead().getColumnIndex();
                                Cell c1 = r1.getCell(cellIndex);
                                Cell c2 = r2.getCell(cellIndex);
                                return c1.getCellTypeEnum()==c2.getCellTypeEnum()
                                        && c1.toString().equals(c2.toString());
        });
    }
}
