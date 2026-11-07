package seek.util.common.bean.register.util.util;

import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

import java.util.List;

public class QuickRegisterUtil {

    /**
     * 批量按类名注册 bean。
     * <p>
     * 普通类直接注册为 bean 定义；{@code @Configuration} 配置类注册后
     * 会重新触发 {@link ConfigurationClassPostProcessor}（CCPP）解析，
     * 使其 @Bean 方法、@Import 等与常规配置类行为完全一致。
     *
     * @param classes 需要注册的类集合
     * @return 可通过 @Bean 声明的后置处理器
     */
    public static BeanDefinitionRegistryPostProcessor quickRegistrarByClassName(List<Class<?>> classes) {
        return new BeanDefinitionRegistryPostProcessor() {
            @Override
            public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
                for (Class<?> beanClass : classes) {
                    registry.registerBeanDefinition(beanClass.getName(),
                            BeanDefinitionBuilder.genericBeanDefinition(beanClass).getBeanDefinition());
                }
            }

            @Override
            public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
                //通过 @Bean 声明的本处理器必然在 CCPP 之后执行，
                //其后注册的 @Configuration 类不会被自动解析，需要手动重新触发 CCPP
                if (classes.stream().noneMatch(QuickRegisterUtil::isConfigurationCandidate)) return;
                if (!(beanFactory instanceof BeanDefinitionRegistry registry)) return;
                if (!beanFactory.containsBean(AnnotationConfigUtils.CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME)) return;
                ConfigurationClassPostProcessor ccpp = beanFactory.getBean(
                        AnnotationConfigUtils.CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME,
                        ConfigurationClassPostProcessor.class);
                //重新解析全部配置类（幂等：已解析过的 @Bean 定义会被跳过），让新注册的配置类生效
                ccpp.postProcessBeanDefinitionRegistry(registry);
                //对新解析出的 full 模式配置类做 CGLIB 增强（已增强过的会被跳过）
                ccpp.postProcessBeanFactory(beanFactory);
            }
        };
    }

    //判断是否为需要 CCPP 解析的配置类，判定规则与 ConfigurationClassUtils.isConfigurationCandidate 一致
    //（该方法为包级私有无法直接调用，此处等价实现）
    private static boolean isConfigurationCandidate(Class<?> clazz) {
        AnnotationMetadata metadata = AnnotationMetadata.introspect(clazz);
        return metadata.isAnnotated(Component.class.getName())
                || metadata.isAnnotated(ComponentScan.class.getName())
                || metadata.isAnnotated(Import.class.getName())
                || metadata.isAnnotated(ImportResource.class.getName())
                || metadata.isAnnotated(Configuration.class.getName())
                || metadata.hasAnnotatedMethods(Bean.class.getName());
    }
}
