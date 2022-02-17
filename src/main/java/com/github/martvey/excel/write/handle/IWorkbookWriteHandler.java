package com.github.martvey.excel.write.handle;

import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.github.martvey.excel.write.chain.IWorkbookWriteHandlerChain;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/8/4 9:32
 */
public interface IWorkbookWriteHandler extends IWriterHandler {

    default void beforeWorkbookCreate(IWorkbookWriteHandlerChain chain){
        chain.beforeWorkbookCreate();
    }

    default void afterWorkbookCreate(WriteWorkbookHolder writeWorkbookHolder, IWorkbookWriteHandlerChain chain){
        chain.afterWorkbookCreate(writeWorkbookHolder);
    }

    default void afterWorkbookDispose(WriteWorkbookHolder writeWorkbookHolder, IWorkbookWriteHandlerChain chain){
        chain.afterWorkbookDispose(writeWorkbookHolder);
    }
}
