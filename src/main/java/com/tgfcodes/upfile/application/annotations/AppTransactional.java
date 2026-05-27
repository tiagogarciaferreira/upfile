package com.tgfcodes.upfile.application.annotations;

import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Transactional
public @interface AppTransactional {

}