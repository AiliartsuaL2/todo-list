package moais.todolist.global.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* moais.todolist..ViewRouter.*(..))")
    public void allView(){}

    @Pointcut("execution(* moais.todolist..*Api*.*(..))")
    public void allApi(){}

    @Pointcut("execution(* moais.todolist..*EventListener.*(..))")
    public void allEventListener(){}

    @Pointcut("execution(* moais.todolist..*UseCase.*(..))")
    public void allUseCase(){}

    @Pointcut("execution(* moais.todolist..*Repository.*(..))")
    public void allRepository(){}

    @Pointcut("allView() || allApi() || allEventListener() || allUseCase() || allRepository()")
    public void all() {}
}
