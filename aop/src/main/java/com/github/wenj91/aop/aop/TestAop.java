package com.github.wenj91.aop.aop;

import com.github.wenj91.aop.annotation.ArgAnnotation;
import com.github.wenj91.aop.annotation.TestAnnotation2;
import com.github.wenj91.aop.annotation.TestAnotation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Aspect
@Component
public class TestAop {

    @Around(value = "@within(org.springframework.web.bind.annotation.RestController)))")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("test aop!");

        String str = pjp.getSignature().getName();

        MethodSignature methodSignature = (MethodSignature)pjp.getSignature();
        Method method = methodSignature.getMethod();

        // get method annotation
        TestAnotation testAnotation = method.getAnnotation(TestAnotation.class);
        Assert.notNull(testAnotation, "test annotation aop fail");
        System.out.println(testAnotation.value());

        TestAnnotation2 testAnnotation2 = method.getAnnotation(TestAnnotation2.class);
        Assert.isNull(testAnnotation2, "test annotation2 is not null");

        // get params annotation
        Annotation[][] as = method.getParameterAnnotations();
        for (Annotation[] aArr : as){
            for (Annotation a : aArr){
                if (a instanceof ArgAnnotation)
                    System.out.println(a.toString() + " arg annotation val:" + ((ArgAnnotation) a).value());
            }
        }

        return pjp.proceed();
    }
}
