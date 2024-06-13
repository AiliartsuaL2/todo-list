package moais.todolist.global.config;

import moais.todolist.global.aop.Aspects;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AopConfig {

    @Bean
    public Aspects.LogAspect logAspect() {
        return new Aspects.LogAspect();
    }
}
