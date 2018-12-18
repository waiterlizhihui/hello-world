package com.waiter.common.aop;

import com.google.gson.Gson;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @ClassName MonitorRuntimeAspect
 * @Description 监听方法的执行时间的切片类，需要与MonitorRuntime注解结合使用
 * @Author lizhihui
 * @Date 2018/12/18 13:53
 * @Version 1.0
 */
//注意：要想使用spring aop实现切片，被切片的目标类必须被spring容器管理
@Aspect
@Component("monitorRuntimeAspect")
public class MonitorExectimeAspect {
    private static Logger logger = LoggerFactory.getLogger("exectime");

    /**
     * 获取@MonitorRuntime注解的切片
     */
    @Pointcut("@annotation(com.waiter.common.annotation.MonitorExectime)")
    public void logPointCut(){
    }

    /**
     *
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long startTimestamp = System.currentTimeMillis();
        Object result = point.proceed();
        long exectime = System.currentTimeMillis() - startTimestamp;
        //可以在调用这个方法之前对runtime做一下判断，执行时间超过了某个值的方法才记录下来
        this.writeRuntimeLog(point,exectime,result);
        return result;
    }

    /**
     * 获取切片方法的相关参数，并记录下来
     * @param point
     * @param exectime
     * @param result
     */
    private void writeRuntimeLog(ProceedingJoinPoint point,long exectime,Object result){
        MethodSignature signature = (MethodSignature) point.getSignature();
        //获取执行了切面的方法，可以通过method.getAnnotation()方法的获取注解
        Method method = signature.getMethod();

        String className = point.getTarget().getClass().getName();
        String methodMame = new StringBuilder(className).append(".").append(signature.getName()).toString();
        Object[] args = point.getArgs();

        Gson gson = new Gson();
        String params = gson.toJson(args).toString();
        String resultStr = result!= null?gson.toJson(result).toString():"";

        //将这些获取到的参数记录到日志之中隔一段时间检查一次，看看那些方法的执行拖累了系统性能
        logger.info("method:{};exectime:{}ms;params:{};return:{}",methodMame,exectime,params,result);
    }


}
