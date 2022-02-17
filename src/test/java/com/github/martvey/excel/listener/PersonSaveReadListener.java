package com.github.martvey.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.github.martvey.excel.entity.PersonXls;
import com.github.martvey.excel.exception.ReadListenerInitException;
import com.github.martvey.excel.read.chain.ReadListenerChain;
import com.github.martvey.excel.read.listener.impl.ThrowErrorDataReadListener;
import com.github.martvey.excel.service.PersonService;

import java.util.*;

public class PersonSaveReadListener extends ThrowErrorDataReadListener<PersonXls> {
    public static final String INIT_THRESHOLD = "PersonSaveReadListener@threshold";
    private final PersonService personService;
    private final List<PersonXls> dataList;
    private Integer threshold = 1000;

    public PersonSaveReadListener(PersonService personService) {
        this.personService = personService;
        this.dataList = new ArrayList<>();
    }


    @Override
    public void init(Map<String, Object> properties, Class<PersonXls> clazz) {
        Object o = properties.get(INIT_THRESHOLD);
        if (o != null){
            this.threshold = (Integer) o;
        }
    }

    @Override
    public void invoke(PersonXls data, AnalysisContext analysisContext, ReadListenerChain<PersonXls> chain) {
        dataList.add(data);
        if (dataList.size() >= threshold){
            saveDataBatch(dataList,analysisContext, chain);
            dataList.clear();
        }
        chain.invoke(data, analysisContext); //如果需要调用后续方法，请条用chain的对应方法
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext, ReadListenerChain<PersonXls> chain) {
        if (dataList.size() > 0){
            saveDataBatch(dataList,analysisContext, chain);
            dataList.clear();
        }
        chain.doAfterAllAnalysed(analysisContext);
    }

    private List<PersonXls> saveDataBatch(List<PersonXls> dataList, AnalysisContext analysisContext, ReadListenerChain<PersonXls> chain) {
        try {
            personService.insertPersonXlsBatch(dataList);
        }catch (Exception e){
            int size = dataList.size();
            int mid = size / 2;
            if (size == 1){
                return dataList;
            }
            List<PersonXls> leftList = saveDataBatch(dataList.subList(0, mid), analysisContext, chain);
            if (leftList.size() == 1){
                throwErrorData(leftList.get(0), analysisContext, chain);
                return Collections.emptyList();
            }
            List<PersonXls> rightList = saveDataBatch(dataList.subList(mid, size), analysisContext, chain);
            if (rightList.size() == 1){
                throwErrorData(rightList.get(0), analysisContext, chain);
                return Collections.emptyList();
            }
        }
        return Collections.emptyList();
    }


}
