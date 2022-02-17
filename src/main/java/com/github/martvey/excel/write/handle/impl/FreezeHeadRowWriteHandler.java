package com.github.martvey.excel.write.handle.impl;

import com.alibaba.excel.write.handler.AbstractRowWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.github.martvey.excel.write.chain.IRowWriteHandlerChain;
import com.github.martvey.excel.write.handle.IRowWriteHandler;
import org.apache.poi.ss.usermodel.Row;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/8/4 10:44
 */
public class FreezeHeadRowWriteHandler extends AbstractRowWriteHandler implements IRowWriteHandler {
    @Override
    public void afterRowDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Integer relativeRowIndex, Boolean isHead, IRowWriteHandlerChain chain) {
        afterRowDispose(writeSheetHolder,writeTableHolder,row,relativeRowIndex,isHead);
        chain.afterRowDispose(writeSheetHolder,writeTableHolder,row,relativeRowIndex,isHead);
    }

    /**
     * 冻结头部
     */
    @Override
    public void afterRowDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Integer relativeRowIndex, Boolean isHead) {
        if (isHead){
            writeSheetHolder.getSheet().createFreezePane(0,row.getRowNum()+1);
        }
    }
}
