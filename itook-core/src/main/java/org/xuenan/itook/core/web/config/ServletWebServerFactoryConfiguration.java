package org.xuenan.itook.core.web.config;

import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.ConnectorStatistics;
import io.undertow.server.handlers.GracefulShutdownHandler;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.ProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.embedded.jetty.JettyWebServer;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.undertow.UndertowBuilderCustomizer;
import org.springframework.boot.web.embedded.undertow.UndertowDeploymentInfoCustomizer;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServer;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Configuration
public class ServletWebServerFactoryConfiguration {
    private static final Logger log = LoggerFactory.getLogger(ServletWebServerFactoryConfiguration.class);
    private static final int waitTime = 10;

    ServletWebServerFactoryConfiguration() {
    }

    private static void stop(ApplicationContext applicationContext) {
        try {
            log.info("开始取消Eureka注册的服务");
            Class<?> claz = Class.forName("com.netflix.discovery.EurekaClient");
            Method method = ReflectionUtils.findMethod(claz, "shutdown");
            applicationContext.getBeansOfType(claz).values().forEach((eurekaClient) -> {
                try {
                    method.invoke(eurekaClient);
                } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException var3) {
                    log.info("关闭注册服务失败", var3);
                }

            });
            Thread.sleep(10000L);
            log.info("取消Eureka注册的服务完毕");
        } catch (Exception var3) {
            log.info("取消Eureka注册的服务失败  ", var3);
        }

    }

    @Configuration
    @ConditionalOnClass(
            name = {"javax.servlet.Servlet", "io.undertow.Undertow", "org.xnio.SslClientAuthMode"}
    )
    public static class EmbeddedUndertow implements ApplicationListener<ContextClosedEvent> {
        private GracefulShutdownHandler gracefulShutdownHandler;

        public EmbeddedUndertow() {
        }

        @Bean
        public UndertowServletWebServerFactory undertowServletWebServerFactory() {
            ServletWebServerFactoryConfiguration.log.info("使用undertow启动服务");
            UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
            factory.addDeploymentInfoCustomizers(new UndertowDeploymentInfoCustomizer[]{(deploymentInfo) -> {
                deploymentInfo.addOuterHandlerChainWrapper((handler) -> {
                    return this.gracefulShutdownHandler = new GracefulShutdownHandler(handler);
                });
            }});
            factory.addBuilderCustomizers(new UndertowBuilderCustomizer[]{(builder) -> {
                builder.setServerOption(UndertowOptions.ENABLE_STATISTICS, true);
            }});
            return factory;
        }

        public void onApplicationEvent(ContextClosedEvent event) {
            try {
                ServletWebServerFactoryConfiguration.log.info("服务开始关闭");
                ApplicationContext context = event.getApplicationContext();
                ServletWebServerFactoryConfiguration.stop(context);
                if (this.gracefulShutdownHandler == null) {
                    ServletWebServerFactoryConfiguration.log.warn("无法完成服务的优雅关机。。。");
                    return;
                }

                this.gracefulShutdownHandler.shutdown();
                UndertowServletWebServer webServer = (UndertowServletWebServer) ((ServletWebServerApplicationContext) context).getWebServer();
                Field field = ReflectionUtils.findField(webServer.getClass(), "undertow");
                field.setAccessible(true);
                Undertow undertow = (Undertow) ReflectionUtils.getField(field, webServer);
                List<Undertow.ListenerInfo> listenerInfo = undertow.getListenerInfo();
                Undertow.ListenerInfo listener = (Undertow.ListenerInfo) listenerInfo.get(0);
                ConnectorStatistics connectorStatistics = listener.getConnectorStatistics();
                long start = System.currentTimeMillis();

                while (connectorStatistics != null && connectorStatistics.getActiveConnections() > 0L) {
                    if (System.currentTimeMillis() - start > TimeUnit.MINUTES.toMillis(10L)) {
                        throw new Exception("优雅关机失败，等待时间超过 10分钟");
                    }

                    Thread.sleep(10L);
                }

                ServletWebServerFactoryConfiguration.log.info("服务关闭完成！！");
            } catch (InterruptedException var11) {
                ServletWebServerFactoryConfiguration.log.info("服务关闭发生中断", var11);
                Thread.currentThread().interrupt();
            } catch (Exception var12) {
                ServletWebServerFactoryConfiguration.log.warn("undertow优雅关机失败", var12);
            }

        }
    }

    @Configuration
    @ConditionalOnClass(
            name = {"javax.servlet.Servlet", "org.eclipse.jetty.server.Server", "org.eclipse.jetty.util.Loader", "org.eclipse.jetty.webapp.WebAppContext"}
    )
    public static class EmbeddedJetty implements ApplicationListener<ContextClosedEvent> {
        public EmbeddedJetty() {
        }

        @Bean
        public JettyServletWebServerFactory JettyServletWebServerFactory() {
            ServletWebServerFactoryConfiguration.log.info("使用jetty启动服务");
            return new JettyServletWebServerFactory();
        }

        public void onApplicationEvent(ContextClosedEvent event) {
            ServletWebServerFactoryConfiguration.log.info("服务开始关闭");
            ApplicationContext context = event.getApplicationContext();
            ServletWebServerFactoryConfiguration.stop(context);
            JettyWebServer webServer = (JettyWebServer) ((ServletWebServerApplicationContext) context).getWebServer();
            webServer.stop();
            ServletWebServerFactoryConfiguration.log.info("服务关闭完成！！");
        }
    }

    @Configuration
    @ConditionalOnClass(
            name = {"javax.servlet.Servlet", "org.apache.catalina.startup.Tomcat", "org.apache.coyote.UpgradeProtocol"}
    )
    public static class EmbeddedTomcat implements ApplicationListener<ContextClosedEvent> {
        private Connector connector;

        public EmbeddedTomcat() {
        }

        @Bean
        public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
            ServletWebServerFactoryConfiguration.log.info("使用tomcat启动服务");
            TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
            tomcat.addConnectorCustomizers(new TomcatConnectorCustomizer[]{(connector) -> {
                this.connector = connector;
            }});
            return tomcat;
        }

        public void onApplicationEvent(ContextClosedEvent event) {
            ServletWebServerFactoryConfiguration.log.info("服务开始关闭");
            ApplicationContext context = event.getApplicationContext();
            ServletWebServerFactoryConfiguration.stop(context);
            if (this.connector == null) {
                ServletWebServerFactoryConfiguration.log.warn("无法完成服务的优雅关机。。。");
            } else {
                ProtocolHandler protocolHandler = this.connector.getProtocolHandler();
                if (protocolHandler == null) {
                    ServletWebServerFactoryConfiguration.log.warn("无法完成服务的优雅关机。。。");
                } else {
                    Executor executor = protocolHandler.getExecutor();
                    if (executor == null) {
                        ServletWebServerFactoryConfiguration.log.warn("无法完成服务的优雅关机。。。");
                    } else {
                        if (executor instanceof ExecutorService) {
                            ExecutorService executorService = (ExecutorService) executor;
                            executorService.shutdown();

                            try {
                                if (!executorService.awaitTermination(10L, TimeUnit.MINUTES)) {
                                    throw new Exception("优雅关机失败，等待时间超过 10分钟");
                                }

                                ServletWebServerFactoryConfiguration.log.info("服务关闭完成！！");
                            } catch (InterruptedException var7) {
                                ServletWebServerFactoryConfiguration.log.info("关闭服务收到中断信息", var7);
                                Thread.currentThread().interrupt();
                            } catch (Exception var8) {
                                ServletWebServerFactoryConfiguration.log.warn("tomcat优雅关机失败", var8);
                            }
                        }

                    }
                }
            }
        }
    }
}
