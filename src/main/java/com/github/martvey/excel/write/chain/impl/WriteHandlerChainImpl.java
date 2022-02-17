package com.github.martvey.excel.write.chain.impl;

import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.github.martvey.excel.write.chain.*;
import com.github.martvey.excel.write.handle.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/8/4 8:39
 */
public class WriteHandlerChainImpl implements WriteHandlerChain {
    private final ICellWriteHandlerChain iCellWriteHandlerChain;
    private final IRowWriteHandlerChain iRowWriteHandlerChain;
    private final ISheetWriteHandlerChain iSheetWriteHandlerChain;
    private final IWorkbookWriteHandlerChain iWorkbookWriteHandlerChain;

    public WriteHandlerChainImpl(List<IWriterHandler> iWriterHandlerList) {


        this.iCellWriteHandlerChain = new ICellWriteHandlerChainImpl(
                iWriterHandlerList.stream()
                        .filter(iWriterHandler -> iWriterHandler instanceof ICellWriteHandler)
                        .map(iWriterHandler -> ((ICellWriteHandler) iWriterHandler))
                        .collect(Collectors.toList())
        );

        this.iRowWriteHandlerChain = new IRowWriteHandlerChainImpl(
                iWriterHandlerList.stream()
                        .filter(iWriterHandler -> iWriterHandler instanceof IRowWriteHandler)
                        .map(iWriterHandler -> ((IRowWriteHandler) iWriterHandler))
                        .collect(Collectors.toList())
        );

        this.iSheetWriteHandlerChain = new ISheetWriteHandlerChainImpl(
                iWriterHandlerList.stream()
                        .filter(iWriterHandler -> iWriterHandler instanceof ISheetWriteHandler)
                        .map(iWriterHandler -> ((ISheetWriteHandler) iWriterHandler))
                        .collect(Collectors.toList())
        );

        this.iWorkbookWriteHandlerChain = new IWorkbookWriteHandlerChainImpl(
                iWriterHandlerList.stream()
                        .filter(iWriterHandler -> iWriterHandler instanceof IWorkbookWriteHandler)
                        .map(iWriterHandler -> ((IWorkbookWriteHandler) iWriterHandler))
                        .collect(Collectors.toList())
        );

    }

    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head,
                                 Integer integer, Integer integer1, Boolean aBoolean) {
        iCellWriteHandlerChain.beforeCellCreate(writeSheetHolder, writeTableHolder, row, head, integer, integer1, aBoolean);
    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell,
                                Head head, Integer integer, Boolean aBoolean) {
        iCellWriteHandlerChain.afterCellCreate(writeSheetHolder, writeTableHolder, cell, head, integer, aBoolean);
    }

    @Override
    public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, CellData cellData, Cell cell, Head head, Integer integer, Boolean aBoolean) {
        iCellWriteHandlerChain.afterCellDataConverted(writeSheetHolder, writeTableHolder, cellData, cell, head, integer, aBoolean);
    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> list, Cell cell, Head head, Integer integer, Boolean aBoolean) {
        iCellWriteHandlerChain.afterCellDispose(writeSheetHolder, writeTableHolder, list, cell, head, integer, aBoolean);
    }



    @Override
    public void beforeRowCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Integer integer, Integer integer1, Boolean aBoolean) {
        iRowWriteHandlerChain.beforeRowCreate(writeSheetHolder, writeTableHolder, integer, integer1, aBoolean);
    }

    @Override
    public void afterRowCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Integer integer, Boolean aBoolean) {
        iRowWriteHandlerChain.afterRowCreate(writeSheetHolder, writeTableHolder, row, integer, aBoolean);
    }

    @Override
    public void afterRowDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Integer integer, Boolean aBoolean) {
        iRowWriteHandlerChain.afterRowDispose(writeSheetHolder, writeTableHolder, row, integer, aBoolean);
    }



    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        iSheetWriteHandlerChain.beforeSheetCreate(writeWorkbookHolder, writeSheetHolder);
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        iSheetWriteHandlerChain.afterSheetCreate(writeWorkbookHolder, writeSheetHolder);
    }


    @Override
    public void beforeWorkbookCreate() {
        iWorkbookWriteHandlerChain.beforeWorkbookCreate();
    }

    @Override
    public void afterWorkbookCreate(WriteWorkbookHolder writeWorkbookHolder) {
        iWorkbookWriteHandlerChain.afterWorkbookCreate(writeWorkbookHolder);
    }

    @Override
    public void afterWorkbookDispose(WriteWorkbookHolder writeWorkbookHolder) {
        iWorkbookWriteHandlerChain.afterWorkbookDispose(writeWorkbookHolder);
    }

}
