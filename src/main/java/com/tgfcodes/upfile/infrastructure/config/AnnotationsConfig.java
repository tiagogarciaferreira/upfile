package com.tgfcodes.upfile.infrastructure.config;

import com.tgfcodes.upfile.domain.annotations.AppComponent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        basePackages = {"com.tgfcodes.upfile.domain", "com.tgfcodes.upfile.application"},
        includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {AppComponent.class})
)
public class AnnotationsConfig {
}
