package com.github.martvey.excel.write.chain.impl;

import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.github.martvey.excel.write.chain.ICellWriteHandlerChain;
import com.github.martvey.excel.write.handle.ICellWriteHandler;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/9/11 9:11
 */
public class ICellWriteHandlerChainImpl implements ICellWriteHandlerChain {
    private final int index;
    private final List<ICellWriteHandler> iCellWriteHandlerList;
    private ICellWriteHandlerChainImpl next;

    public ICellWriteHandlerChainImpl(List<ICellWriteHandler> iCellWriteHandlerList) {
        this.index = 0;
        this.iCellWriteHandlerList = iCellWriteHandlerList;
    }

    public ICellWriteHandlerChainImpl(ICellWriteHandlerChain parent, int index){
        this.iCellWriteHandlerList = ((ICellWriteHandlerChainImpl) parent).iCellWriteHandlerList;
        this.index = index;
    }

    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head,
                                 Integer columnIndex, Integer relativeRowIndex, Boolean isHead) {
        if (index < iCellWriteHandlerList.size()){
            ICellWriteHandler iCellWriteHandler = iCellWriteHandlerList.get(index);
            iCellWriteHandler.beforeCellCreate(writeSheetHolder,writeTableHolder,row,head,columnIndex,relativeRowIndex,isHead,nextChain());
        }
    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell,
                                Head head, Integer relativeRowIndex, Boolean isHead) {
        if (index < iCellWriteHandlerList.size()){
            ICellWriteHandler iCellWriteHandler = iCellWriteHandlerList.get(index);
            iCellWriteHandler.afterCellCreate(writeSheetHolder,writeTableHolder,cell,head,relativeRowIndex,isHead,nextChain());
        }
    }

    @Override
    public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder,
                                       CellData cellData,Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        if (index < iCellWriteHandlerList.size()){
            ICellWriteHandler iCellWriteHandler = iCellWriteHandlerList.get(index);
            iCellWriteHandler.afterCellDataConverted(writeSheetHolder,writeTableHolder,cellData,cell,head,relativeRowIndex,isHead,nextChain());
        }
    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder,
                                 List<CellData> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        if (index < iCellWriteHandlerList.size()){
            ICellWriteHandler iCellWriteHandler = iCellWriteHandlerList.get(index);
            iCellWriteHandler.afterCellDispose(writeSheetHolder,writeTableHolder,cellDataList,cell,head,relativeRowIndex,isHead,nextChain());
        }
    }

    private ICellWriteHandlerChain nextChain(){
        if (this.next == null) {
            this.next = new ICellWriteHandlerChainImpl(this,index+1);
        }
        return this.next;
    }
}
