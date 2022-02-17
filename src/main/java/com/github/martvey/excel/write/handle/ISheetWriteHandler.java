package com.github.martvey.excel.write.handle;

import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.github.martvey.excel.write.chain.ISheetWriteHandlerChain;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/8/4 9:28
 */
public interface ISheetWriteHandler extends IWriterHandler {

    default void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder, ISheetWriteHandlerChain chain){
        chain.beforeSheetCreate(writeWorkbookHolder, writeSheetHolder);
    }


    default void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder, ISheetWriteHandlerChain chain){
        chain.afterSheetCreate(writeWorkbookHolder, writeSheetHolder);
    }

}
