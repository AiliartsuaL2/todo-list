package moais.todolist.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
public class Aspects {

    @Aspect
    public static class LogAspect {

        @Around("moais.todolist.global.aop.Pointcuts.all()")
        public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[진입 log] class = {}, method = {}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            return joinPoint.proceed();
        }
    }
}
