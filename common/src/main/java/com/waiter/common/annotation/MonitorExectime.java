package com.waiter.common.annotation;

import java.lang.annotation.*;

/**
 * 与Spring AOP结合后，只要使用了这个注解的方法的执行时间都会被记录到日志当中
 */
@Target(ElementType.METHOD)//注解的作用目标仅限于方法级别
@Retention(RetentionPolicy.RUNTIME)//注解的保留策略为会在class文件中存在，在运行时可以通过反射获取到
@Documented//该注解将会被包含在javadoc中
public @interface MonitorExectime {
}
