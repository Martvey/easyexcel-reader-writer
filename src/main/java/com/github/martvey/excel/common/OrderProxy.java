package com.github.martvey.excel.common;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * @author vvnn1_
 * @version 1.0
 * @date 2020/9/2 9:40
 */
public class OrderProxy<K> implements InvocationHandler {
    private final K o;
    private final Integer order;
    public OrderProxy(K o, Integer order) {
        this.o = o;
        this.order = order;
    }

    public K getProxy(){
        Class<?>[] cInterface = getAllInterface(o.getClass(),new Class[0]);
        if (!(o instanceof Order)){
            cInterface = Arrays.copyOf(cInterface, cInterface.length + 1);
            cInterface[cInterface.length - 1 ] = Order.class;
        }
        return (K)Proxy.newProxyInstance(o.getClass().getClassLoader(), cInterface,this);
    }

    private Class<?>[] getAllInterface(Class<?> clazz, Class<?>[] ary){
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length > 0){
            int aryLength = ary.length;
            ary = Arrays.copyOf(ary, aryLength + interfaces.length);
            System.arraycopy(interfaces,0, ary,aryLength, interfaces.length);
        }
        Class<?> superclass = clazz.getSuperclass();
        if (superclass == null){
            return ary;
        }
        return getAllInterface(superclass,ary);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.equals(Order.class.getMethod("getOrder"))) {
            return order;
        }
        return method.invoke(o,args);
    }
}
