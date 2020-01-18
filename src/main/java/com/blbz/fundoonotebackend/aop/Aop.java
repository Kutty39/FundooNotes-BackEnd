package com.blbz.fundoonotebackend.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class Aop {
    private Object value=null;

    @Around(value = "execution(public * com.blbz.fundoonotebackend.controller.*.*(..))")
    public Object forController(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("Method: " + proceedingJoinPoint.getSignature().toString());
        value = proceedingJoinPoint.proceed();
        log.info(value != null ? value.toString() : null);
        log.info("Method: Exit from " + proceedingJoinPoint.getSignature().getName());
        return value;
    }

    @Around(value = "execution(public * com.blbz.fundoonotebackend.service.*.*(..))")
    public Object forService(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("Method: " + proceedingJoinPoint.getSignature().toString());
        value = proceedingJoinPoint.proceed();
        log.info(value != null ? value.toString() : null);
        log.info("Method: Exit from " + proceedingJoinPoint.getSignature().getName());
        return value;
    }
    @Around(value = "execution(public * com.blbz.fundoonotebackend.exception.*.*(..))")
    public Object forException(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("Method: " + proceedingJoinPoint.getSignature().toString());
        value = proceedingJoinPoint.proceed();
        log.info(value != null ? value.toString() : null);
        log.info("Method: Exit from " + proceedingJoinPoint.getSignature().getName());
        return value;
    }
}
