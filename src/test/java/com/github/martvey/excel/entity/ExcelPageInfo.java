package com.github.martvey.excel.entity;

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;


public class ExcelPageInfo {
    private Integer startRow;
    private Integer endRow;
    private final List<Object> personXlsList;

    public ExcelPageInfo(Integer startRow, Integer endRow) {
        this.startRow = startRow;
        this.endRow = endRow;
        this.personXlsList = new ArrayList<>();
    }

    public Integer getStartRow() {
        return startRow;
    }

    public void setStartRow(Integer startRow) {
        this.startRow = startRow;
    }

    public Integer getEndRow() {
        return endRow;
    }

    public void setEndRow(Integer endRow) {
        this.endRow = endRow;
    }

    public void add(Object personXls){
        this.personXlsList.add(personXls);
    }

    public List<Object> getPersonXlsList() {
        return personXlsList;
    }
}
