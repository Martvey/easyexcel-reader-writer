package com.github.martvey.excel.write.handle;

import java.util.List;

import com.github.martvey.excel.write.chain.ICellWriteHandlerChain;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/8/4 9:25
 */
public interface ICellWriteHandler extends IWriterHandler{

    default void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head,
                          Integer columnIndex, Integer relativeRowIndex, Boolean isHead, ICellWriteHandlerChain chain){
        chain.beforeCellCreate(writeSheetHolder,writeTableHolder,row,head,columnIndex,relativeRowIndex,isHead);
    }


    default void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head,
                         Integer relativeRowIndex, Boolean isHead, ICellWriteHandlerChain chain){
        chain.afterCellCreate(writeSheetHolder,writeTableHolder,cell,head,relativeRowIndex,isHead);
    }

    default void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, CellData cellData,
                                Cell cell, Head head, Integer relativeRowIndex, Boolean isHead, ICellWriteHandlerChain chain){
        chain.afterCellDataConverted(writeSheetHolder,writeTableHolder,cellData,cell,head,relativeRowIndex,isHead);
    }


    default void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder,
                          List<CellData> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead, ICellWriteHandlerChain chain){
        chain.afterCellDispose(writeSheetHolder, writeTableHolder, cellDataList, cell, head, relativeRowIndex, isHead);
    }
}
