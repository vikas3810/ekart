package com.ekart.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

    private static final String AROUND_LOG_POINTCUT = "execution(* com.ekart.controller.*.*(..))";

    // Define a pointcut to target controller methods
    @Pointcut("execution(* com.ekart.controller.*.*(..))")
    public void around() {
    }

    // Log exceptions thrown from service methods
    @AfterThrowing(value = "execution(* com.ekart.serviceImpl.*.*(..))", throwing = "e")
    public void afterThrowingException(JoinPoint joinPoint, Exception e) {
        String methodName = joinPoint.getSignature().getName();
        log.info("Exception in method: " + methodName + " - " + e.getMessage());
    }

    // Log method execution time
    @Around("around()")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();

        // Log info before method call
        LocalDateTime startTime = LocalDateTime.now();
        log.info("Method: " + methodName + " called at " + startTime);

        Object result = joinPoint.proceed();

        // Calculate and log method execution time
        Duration duration = Duration.between(startTime, LocalDateTime.now());
        long hours = duration.toHours();
        long minutes = duration.toHours() % 60;
        long second = duration.getSeconds() % 60;
        long nanoSec = duration.getNano() % 60;
        log.info("Method: " + methodName + " Execution complete at: " + LocalDateTime.now() +
                " Time taken = " + hours + " hours " +
                minutes + " minutes " +
                second + " seconds " +
                nanoSec + " nanoSecond"
        );

        return result;
    }
}
