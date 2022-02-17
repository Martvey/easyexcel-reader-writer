package com.github.martvey.excel.write.chain.impl;

import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.github.martvey.excel.write.chain.ISheetWriteHandlerChain;
import com.github.martvey.excel.write.handle.ISheetWriteHandler;

import java.util.List;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/9/11 9:14
 */
public class ISheetWriteHandlerChainImpl implements ISheetWriteHandlerChain {
    private final int index;
    private final List<ISheetWriteHandler> iSheetWriteHandlerList;
    private ISheetWriteHandlerChainImpl next;
    public ISheetWriteHandlerChainImpl(List<ISheetWriteHandler> iSheetWriteHandlerList) {
        this.index = 0;
        this.iSheetWriteHandlerList = iSheetWriteHandlerList;
    }

    public ISheetWriteHandlerChainImpl(ISheetWriteHandlerChainImpl parent, int index) {
        this.iSheetWriteHandlerList=parent.iSheetWriteHandlerList;
        this.index=index;
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        if (this.index < this.iSheetWriteHandlerList.size()){
            ISheetWriteHandler iSheetWriteHandler = this.iSheetWriteHandlerList.get(this.index);
            iSheetWriteHandler.beforeSheetCreate(writeWorkbookHolder,writeSheetHolder,nextChain());
        }
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        if (this.index < this.iSheetWriteHandlerList.size()){
            ISheetWriteHandler iSheetWriteHandler = this.iSheetWriteHandlerList.get(this.index);
            iSheetWriteHandler.afterSheetCreate(writeWorkbookHolder,writeSheetHolder,nextChain());
        }
    }

    private ISheetWriteHandlerChainImpl nextChain(){
        if (this.next == null) {
            this.next = new ISheetWriteHandlerChainImpl(this,index+1);
        }
        return this.next;
    }
}
