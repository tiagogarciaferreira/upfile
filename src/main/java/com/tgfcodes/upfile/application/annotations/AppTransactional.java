package com.tgfcodes.upfile.application.annotations;

import org.springframework.core.annotation.AliasFor;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Transactional
public @interface AppTransactional {

    @AliasFor(annotation = Transactional.class)
    boolean readOnly() default false;
}