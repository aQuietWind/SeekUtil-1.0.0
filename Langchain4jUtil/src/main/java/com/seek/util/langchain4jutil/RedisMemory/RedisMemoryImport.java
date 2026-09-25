package com.seek.util.langchain4jutil.RedisMemory;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AiMemoryConfig.class)
public @interface RedisMemoryImport {
}
