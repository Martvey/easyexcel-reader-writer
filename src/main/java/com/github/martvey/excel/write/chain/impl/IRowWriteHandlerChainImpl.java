package com.github.martvey.excel.write.chain.impl;

import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.github.martvey.excel.write.chain.IRowWriteHandlerChain;
import com.github.martvey.excel.write.handle.IRowWriteHandler;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/9/11 9:12
 */
public class IRowWriteHandlerChainImpl implements IRowWriteHandlerChain {
    private final int index;
    private final List<IRowWriteHandler> iRowWriteHandlerList;
    private IRowWriteHandlerChainImpl next;

    public IRowWriteHandlerChainImpl(List<IRowWriteHandler> iRowWriteHandlerList) {
        this.index = 0;
        this.iRowWriteHandlerList = iRowWriteHandlerList;
    }

    public IRowWriteHandlerChainImpl(IRowWriteHandlerChain parent,int index){
        this.index=index;
        this.iRowWriteHandlerList = ((IRowWriteHandlerChainImpl) parent).iRowWriteHandlerList;
    }

    @Override
    public void beforeRowCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Integer rowIndex,
                                Integer relativeRowIndex, Boolean isHead) {
        if (this.index < this.iRowWriteHandlerList.size()){
            IRowWriteHandler iRowWriteHandler = this.iRowWriteHandlerList.get(this.index);
            iRowWriteHandler.beforeRowCreate(writeSheetHolder,writeTableHolder,rowIndex,relativeRowIndex,isHead,nextChain());
        }
    }

    @Override
    public void afterRowCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row,
                               Integer relativeRowIndex, Boolean isHead) {
        if (this.index < this.iRowWriteHandlerList.size()){
            IRowWriteHandler iRowWriteHandler = this.iRowWriteHandlerList.get(this.index);
            iRowWriteHandler.afterRowCreate(writeSheetHolder,writeTableHolder,row,relativeRowIndex,isHead,nextChain());
        }
    }

    @Override
    public void afterRowDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row,
                                Integer relativeRowIndex, Boolean isHead) {
        if (this.index < this.iRowWriteHandlerList.size()){
            IRowWriteHandler iRowWriteHandler = this.iRowWriteHandlerList.get(this.index);
            iRowWriteHandler.afterRowDispose(writeSheetHolder,writeTableHolder,row,relativeRowIndex,isHead,nextChain());
        }
    }

    private IRowWriteHandlerChainImpl nextChain(){
        if (this.next == null) {
            this.next = new IRowWriteHandlerChainImpl(this,index+1);
        }
        return this.next;
    }
}
