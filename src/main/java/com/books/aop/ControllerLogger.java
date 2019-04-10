package com.books.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Aspect
@Component
public class ControllerLogger
{
    private Logger logger = LoggerFactory.getLogger(ControllerLogger.class);

    @Around("within(com.books.web.*)")
    public Object logControllerRequests(ProceedingJoinPoint proceedingJoinPoint)
    {
        Object result;
        try
        {
            result = proceedingJoinPoint.proceed();
            logger.info("{} : Inside Controller {}.{}() method ", LocalDateTime.now(), proceedingJoinPoint.getSignature().getDeclaringTypeName(), proceedingJoinPoint.getSignature().getName());
            return result;
        }
        catch (Throwable throwable)
        {
            logger.warn("{} : Exception occurred around method{}. Exception message: {}", LocalDateTime.now(), proceedingJoinPoint.getSignature(), throwable.getLocalizedMessage());
        }
        return null;
    }
}
