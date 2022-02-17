package com.github.martvey.excel;


import com.alibaba.excel.EasyExcel;
import com.github.martvey.excel.entity.ExcelPageInfo;
import com.github.martvey.excel.entity.PersonXls;
import com.github.martvey.excel.listener.FlatMapReaderListener;
import com.github.martvey.excel.listener.HeadReadListener;
import com.github.martvey.excel.listener.PageReaderListener;
import com.github.martvey.excel.listener.PersonSaveReadListener;
import com.github.martvey.excel.read.chain.ReadListenerChain;
import com.github.martvey.excel.read.listener.impl.ValidReadListener;
import com.github.martvey.excel.util.SpringContextHolder;
import com.github.martvey.excel.write.chain.impl.WriteHandlerChainImpl;
import com.github.martvey.excel.write.handle.impl.CellMergeWriteHandler;
import com.github.martvey.excel.write.handle.impl.FreezeHeadRowWriteHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest(classes = TestConfiguration.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ExcelTest {

    /**
     * 读取EXCEL并保存
     */
    @Test
    public void readAndSave(){
        ReadListenerChain<PersonXls> readListenerChain = EasyReaderWriter
                .initReadListener(PersonXls.class, SpringContextHolder::getPrototypeBean) // 工厂结合Spring可以实现监听器的依赖注入
                .initListener(new HashMap<String, Object>(4){{ // 可以设置监听器的一些初始化属性
                    put(PersonSaveReadListener.INIT_THRESHOLD, 5);
                    put(ValidReadListener.INIT_ERR_MSG_FMT, "${rowIndex}行数据错误，原因：${errMsg}");
                }})
                .doValid(personXls -> {// 支持自手动校验数据和gsr303校验
                    if ("小王12".equals(personXls.getName())){
                        return Collections.singletonList("不能为小王12");
                    }
                    return Collections.emptyList();
                })
                .ifHasErrData(errList -> {// 错误数据回调
                    System.out.println("校验出的错误数据===>" + errList);
                })
                .build();
        EasyExcel.read(getClass().getClassLoader().getResourceAsStream("人员信息.xlsx"), PersonXls.class, readListenerChain)
                .sheet()
                .doRead();
    }

    /**
     * 读取头和分页读取EXCEL
     */
    @Test
    public void readHeadAndReadPage(){
        LinkedList<String> headList = new LinkedList<>();
        ExcelPageInfo excelPageInfo = new ExcelPageInfo(5,10);
        ReadListenerChain<PersonXls> readListenerChain = EasyReaderWriter
                .initReadListener(PersonXls.class, SpringContextHolder::getPrototypeBean)// 工厂结合Spring可以实现监听器的依赖注入
                .initListener(new HashMap<String, Object>(4){{// 可以设置监听器的一些初始化属性
                    put(HeadReadListener.INIT_HEAD_LIST, headList);
                    put(PageReaderListener.INIT_PAGE, excelPageInfo);
                }})
                .addListener(HeadReadListener.class, 0)// 添加读头监听器
                .addListener(PageReaderListener.class, 0)// 添加分页读取监听器
                .build();

        EasyExcel.read(getClass().getClassLoader().getResourceAsStream("人员信息.xlsx"), PersonXls.class, readListenerChain)
                .sheet()
                .doRead();

        System.out.println("解析到的文件头===>" + headList);
        System.out.println("读取到的数据内容===>" + excelPageInfo.getPersonXlsList());
    }

    /**
     * EXCEL正确数据和错误数据的分离
     */
    @Test
    public void flatMapExcel() throws IOException {
        File dir = Files.createTempDirectory("test_flat").toFile();
        ReadListenerChain<PersonXls> readListenerChain = EasyReaderWriter
                .initReadListener(PersonXls.class, SpringContextHolder::getPrototypeBean)// 工厂结合Spring可以实现监听器的依赖注入
                .initListener(new HashMap<String, Object>(4){{// 可以设置监听器的一些初始化属性
                    put(FlatMapReaderListener.INIT_FLAT_FILE_DIR, dir);
                }})
                .doValid()
                .addListener(FlatMapReaderListener.class, ValidReadListener.VALID_ORDER+1)// 添加分离监听器
                .build();

        EasyExcel.read(getClass().getClassLoader().getResourceAsStream("人员信息.xlsx"), PersonXls.class, readListenerChain)
                .sheet()
                .doRead();

        System.out.println(dir);
        for (File file : dir.listFiles()) {
            System.out.println("\t" + file.getName());
        }
    }

    /**
     * 导出数据，冻住头及垂直合并相同字段
     */
    @Test
    public void freezeHeadAndMerge() throws IOException {
        List<PersonXls> personXlsList = getDemoDataList();

        WriteHandlerChainImpl writeHandlerChain = EasyReaderWriter
                .initWriteHandler(PersonXls.class, SpringContextHolder::getPrototypeBean)// 工厂结合Spring可以实现句柄的依赖注入
                .initHandler(Collections.singletonMap(CellMergeWriteHandler.INIT_DATA_SIZE, 10L))// 可以设置句柄的一些初始化属性
                .addHandler(FreezeHeadRowWriteHandler.class, 0)// 冻结头句柄
                .addHandler(CellMergeWriteHandler.class, 1)// 合并单元格句柄
                .build();

        File tempFile = File.createTempFile("freeze", ".xlsx");
        EasyExcel.write(tempFile, PersonXls.class)
                .excludeColumnFiledNames(Collections.singleton("errMsg"))
                .registerWriteHandler(writeHandlerChain)
                .sheet()
                .doWrite(personXlsList);
        System.out.println(tempFile);
    }

    private List<PersonXls> getDemoDataList() {
        LinkedList<PersonXls> list = new LinkedList<>();
        list.add(new PersonXls("小王1", "333333333333333333", "杭州1"));
        list.add(new PersonXls("小王1", "333333333333333334", "杭州1"));
        list.add(new PersonXls("小王1", "333333333333333335", "杭州2"));
        list.add(new PersonXls("小王1", "333333333333333336", "杭州2"));
        list.add(new PersonXls("小王1", "333333333333333337", "杭州3"));
        list.add(new PersonXls("小王1", "333333333333333338", "杭州3"));
        list.add(new PersonXls("小王1", "333333333333333339", "杭州4"));
        list.add(new PersonXls("小王1", "333333333333333332", "杭州4"));
        list.add(new PersonXls("小王1", "333333333333333331", "杭州5"));
        list.add(new PersonXls("小王1", "333333333333333332", "杭州5"));
        return list;
    }
}
