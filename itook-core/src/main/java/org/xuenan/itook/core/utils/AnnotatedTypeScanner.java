package org.xuenan.itook.core.utils;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AnnotatedTypeScanner implements ResourceLoaderAware, EnvironmentAware {
    private final Iterable<Class<? extends Annotation>> annotationTypess;
    private final boolean considerInterfaces;
    private ResourceLoader resourceLoader;
    private Environment environment;

    @SafeVarargs
    public AnnotatedTypeScanner(Class<? extends Annotation>... annotationTypes) {
        this(true, annotationTypes);
    }

    @SafeVarargs
    public AnnotatedTypeScanner(boolean considerInterfaces, Class<? extends Annotation>... annotationTypes) {
        this.annotationTypess = Arrays.asList(annotationTypes);
        this.considerInterfaces = considerInterfaces;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Set<Class<?>> findTypes(String... basePackages) {
        return this.findTypes((Iterable)Arrays.asList(basePackages));
    }

    public Set<Class<?>> findTypes(Iterable<String> basePackages) {
        AnnotatedTypeScanner.InterfaceAwareScanner provider = new AnnotatedTypeScanner.InterfaceAwareScanner(this.considerInterfaces);
        if (this.resourceLoader != null) {
            provider.setResourceLoader(this.resourceLoader);
        }

        if (this.environment != null) {
            provider.setEnvironment(this.environment);
        }

        Iterator var3 = this.annotationTypess.iterator();

        while(var3.hasNext()) {
            Class<? extends Annotation> annotationType = (Class)var3.next();
            provider.addIncludeFilter(new AnnotationTypeFilter(annotationType, true, this.considerInterfaces));
        }

        Set<Class<?>> types = new HashSet();
        Iterator var11 = basePackages.iterator();

        while(var11.hasNext()) {
            String basePackage = (String)var11.next();
            Iterator var6 = provider.findCandidateComponents(basePackage).iterator();

            while(var6.hasNext()) {
                BeanDefinition definition = (BeanDefinition)var6.next();

                try {
                    types.add(ClassUtils.forName(definition.getBeanClassName(), this.resourceLoader == null ? null : this.resourceLoader.getClassLoader()));
                } catch (ClassNotFoundException var9) {
                    throw new IllegalStateException(var9);
                }
            }
        }

        return types;
    }

    private static class InterfaceAwareScanner extends ClassPathScanningCandidateComponentProvider {
        private final boolean considerInterfaces;

        public InterfaceAwareScanner(boolean considerInterfaces) {
            super(false);
            this.considerInterfaces = considerInterfaces;
        }

        protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
            return super.isCandidateComponent(beanDefinition) || this.considerInterfaces && beanDefinition.getMetadata().isInterface();
        }
    }
}
