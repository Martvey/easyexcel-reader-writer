package com.github.martvey.excel.write.chain.impl;

import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.github.martvey.excel.write.chain.IWorkbookWriteHandlerChain;
import com.github.martvey.excel.write.handle.IWorkbookWriteHandler;

import java.util.List;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/9/11 9:16
 */
public class IWorkbookWriteHandlerChainImpl implements IWorkbookWriteHandlerChain {
    private final int index;
    private final List<IWorkbookWriteHandler> iWorkbookWriteHandlerList;
    private IWorkbookWriteHandlerChainImpl next;

    public IWorkbookWriteHandlerChainImpl(List<IWorkbookWriteHandler> iWorkbookWriteHandlerList) {
        this.index=0;
        this.iWorkbookWriteHandlerList = iWorkbookWriteHandlerList;
    }

    public IWorkbookWriteHandlerChainImpl(IWorkbookWriteHandlerChainImpl parent,int index) {
        this.index=index;
        this.iWorkbookWriteHandlerList=parent.iWorkbookWriteHandlerList;
    }

    @Override
    public void beforeWorkbookCreate() {
        if (this.index < this.iWorkbookWriteHandlerList.size()){
            IWorkbookWriteHandler iWorkbookWriteHandler = this.iWorkbookWriteHandlerList.get(this.index);
            iWorkbookWriteHandler.beforeWorkbookCreate(nextChain());
        }
    }

    @Override
    public void afterWorkbookCreate(WriteWorkbookHolder writeWorkbookHolder) {
        if (this.index < this.iWorkbookWriteHandlerList.size()){
            IWorkbookWriteHandler iWorkbookWriteHandler = this.iWorkbookWriteHandlerList.get(this.index);
            iWorkbookWriteHandler.afterWorkbookCreate(writeWorkbookHolder,nextChain());
        }
    }

    @Override
    public void afterWorkbookDispose(WriteWorkbookHolder writeWorkbookHolder) {
        if (this.index < this.iWorkbookWriteHandlerList.size()){
            IWorkbookWriteHandler iWorkbookWriteHandler = this.iWorkbookWriteHandlerList.get(this.index);
            iWorkbookWriteHandler.afterWorkbookDispose(writeWorkbookHolder,nextChain());
        }
    }

    private IWorkbookWriteHandlerChainImpl nextChain(){
        if (this.next == null) {
            this.next = new IWorkbookWriteHandlerChainImpl(this,index+1);
        }
        return this.next;
    }
}
