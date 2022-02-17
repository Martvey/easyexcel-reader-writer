package com.github.martvey.excel.service;

import com.github.martvey.excel.entity.PersonXls;

import java.util.List;

public interface PersonService {
    void insertPersonXlsBatch(List<PersonXls> personXlsList);
}
