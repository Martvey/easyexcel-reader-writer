package com.github.martvey.excel.write.handle;

import com.github.martvey.excel.write.chain.IRowWriteHandlerChain;
import org.apache.poi.ss.usermodel.Row;

import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/8/4 9:27
 */
public interface IRowWriteHandler extends IWriterHandler {

    default void beforeRowCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Integer rowIndex,
                         Integer relativeRowIndex, Boolean isHead, IRowWriteHandlerChain chain){
        chain.beforeRowCreate(writeSheetHolder, writeTableHolder, rowIndex, relativeRowIndex, isHead);
    }


    default void afterRowCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row,
                        Integer relativeRowIndex, Boolean isHead, IRowWriteHandlerChain chain){
        chain.afterRowCreate(writeSheetHolder, writeTableHolder, row, relativeRowIndex, isHead);
    }


    default void afterRowDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row,
                        Integer relativeRowIndex, Boolean isHead, IRowWriteHandlerChain chain){
        chain.afterRowDispose(writeSheetHolder, writeTableHolder, row, relativeRowIndex, isHead);
    }
}
