package com.github.martvey.excel.read.listener.impl;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.util.StringUtils;
import com.github.martvey.excel.common.Order;
import com.github.martvey.excel.entity.IExcelError;
import com.github.martvey.excel.read.chain.ReadListenerChain;
import org.hibernate.validator.internal.engine.messageinterpolation.InterpolationTermType;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.Token;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.TokenCollector;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.TokenIterator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/8/3 14:40
 */
public class ValidReadListener<T extends IExcelError> extends ThrowErrorDataReadListener<T> implements Order {
    public static final int VALID_ORDER = -4000;
    public static String INIT_CUSTOM_VALIDATOR = "ValidReadListener@validator";
    public static String INIT_ERR_MSG_FMT = "ValidReadListener@errMessageFormat";
    public static String INIT_ERR_MSG_SPLIT = "ValidReadListener@errSplit";
    private static final String DEFAULT_ERR_MSG = "${errMsg}";
    private static final String DEFAULT_ERR_ROW = "${rowIndex}";
    private static final Validator defaultValidator = Validation.buildDefaultValidatorFactory().getValidator();
    private Function<T,List<String>> validateFunction;
    private Validator validator;
    private String errMsgDemo;
    private String errSplit;

    @Override
    public void init(Map<String,Object> properties, Class<T> clazz) {
        Object tmp = properties.getOrDefault(INIT_CUSTOM_VALIDATOR, defaultValidator);
        if (!(tmp instanceof Validator)){
            throw new RuntimeException("ValidReadListener.validator 属性必须为 javax.validation.Validator 对象");
        }
        this.errSplit = (String) properties.getOrDefault(INIT_ERR_MSG_SPLIT, ";");
        this.errMsgDemo = (String) properties.getOrDefault(INIT_ERR_MSG_FMT, DEFAULT_ERR_MSG);
        this.validator = (Validator) tmp;
    }

    @Override
    public void invoke(T data, AnalysisContext analysisContext, ReadListenerChain<T> chain) {
        String errMsg = validData(data);
        if (StringUtils.isEmpty(errMsg)){
            chain.invoke(data,analysisContext);
            return;
        }
        String message = formatErrorMessage(analysisContext.readRowHolder().getRowIndex(), errMsg);
        data.setErrMsg(message);
        throwErrorData(data, analysisContext, chain);
    }


    private String validData(T data){
        LinkedList<String> errMsgList = new LinkedList<>();
        Set<ConstraintViolation<Object>> set = validator.validate(data, Default.class);
        for (ConstraintViolation<Object> constraintViolation : set) {
            errMsgList.add(constraintViolation.getMessage());
        }
        if (validateFunction != null){
            errMsgList.addAll(validateFunction.apply(data));
        }
        return String.join(errSplit, errMsgList);
    }

    private String formatErrorMessage(Integer rowIndex, String errMsg) {
        List<Token> tokenList = new TokenCollector(errMsgDemo, InterpolationTermType.EL).getTokenList();
        TokenIterator tokenIterator = new TokenIterator(tokenList);
        while (tokenIterator.hasMoreInterpolationTerms()) {
            String term = tokenIterator.nextInterpolationTerm();
            String resolvedExpression;
            switch (term){
                case DEFAULT_ERR_MSG:
                    resolvedExpression = errMsg;
                    break;
                case DEFAULT_ERR_ROW:
                    resolvedExpression = String.valueOf(rowIndex);
                    break;
                default:
                    resolvedExpression = term;
            }
            tokenIterator.replaceCurrentInterpolationTerm(resolvedExpression);
        }
        return tokenIterator.getInterpolatedMessage();
    }

    public void setValidFunction(Function<T, List<String>> validateFunction) {
        this.validateFunction = validateFunction;
    }

    @Override
    public int getOrder() {
        return VALID_ORDER;
    }
}
