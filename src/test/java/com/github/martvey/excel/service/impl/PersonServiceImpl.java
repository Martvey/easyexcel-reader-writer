package com.github.martvey.excel.service.impl;

import com.github.martvey.excel.entity.PersonXls;
import com.github.martvey.excel.service.PersonService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {
    @Override
    public void insertPersonXlsBatch(List<PersonXls> personXlsList) {
        System.out.println("接收到数据=====>" + personXlsList);
    }
}
