package ru.slevyns.testtask.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class ExecutionTimeAspect {
    @Pointcut("@annotation(ru.slevyns.testtask.aspect.annotation.ExecutionTime)")
    private void loggingPointCut() {
    }

    @Around("loggingPointCut()")
    public Object loggingAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        var watch = new StopWatch();
        Object proceed;
        try {
            watch.start();
            proceed = joinPoint.proceed();
        } finally {
            watch.stop();
            var executionTime = watch.getTotalTimeMillis();
            var clazz = joinPoint.getTarget().getClass();
            var methodName = joinPoint.getSignature().getName();
            var log = LoggerFactory.getLogger(clazz);
            log.info("method:'{}' completed in: {} ms", methodName, executionTime);
        }

        return proceed;
    }
}
