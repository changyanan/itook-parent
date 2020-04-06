package org.xuenan.itook.core.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.xuenan.itook.core.exception.ExceptionLevel;
import org.xuenan.itook.core.utils.JSONUtils;
import org.xuenan.itook.core.utils.ListUtils;
import org.xuenan.itook.core.utils.MapUtils;

import java.util.Map;
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Component
@EnableConfigurationProperties({RunTimeLog.TimeScale.class})
public class RunTimeLog implements DisposableBean {
    private static final Logger log = LoggerFactory.getLogger("globalegrow.time.log");
    private static Map<ExceptionLevel, Integer> timeScale;
    private static Map.Entry<ExceptionLevel, Integer>[] timeScaleList;
    private static int offtime;
    private static ThreadLocal<Stack<Long>> timeStack;
    private static ScheduledExecutorService executor;

    public RunTimeLog() {
    }

    private static void init(Map<ExceptionLevel, Integer> map) {
        timeScale = MapUtils.n(map).filter((k, v) -> {
            return v != null;
        }).to();
        Integer offtime_ = (Integer)timeScale.remove(ExceptionLevel.OFF);
        offtime = offtime_ == null ? -1 : offtime_;
        Map.Entry<ExceptionLevel, Integer>[] entrys = new Map.Entry[timeScale.size()];
        timeScaleList = (Map.Entry[]) ListUtils.n(timeScale.entrySet()).filter((t) -> {
            return t.getValue() != null;
        }).order((t) -> {
            return (Integer)t.getValue();
        }).to().toArray(entrys);
    }

    public static final void bedin() {
        ((Stack)timeStack.get()).push(System.currentTimeMillis());
    }

    private static final long getRuntime() {
        Long bedin = (Long)((Stack)timeStack.get()).pop();
        if (bedin == null) {
            return 0L;
        } else {
            long end = System.currentTimeMillis();
            return end - bedin;
        }
    }

    public static final void log(String f, Object... args) {
        log(getRuntime(), f, args);
    }

    public static final void log(long runtime, String f, Object... args) {
        logSync(runtime, f, args);
    }

    private static final void logSync(long runtime, String f, Object... args) {
        if (runtime != 0L && f != null && (long)offtime <= runtime && timeScaleList.length != 0) {
            Map.Entry<ExceptionLevel, Integer> suitmin = null;

            for(int i = 0; i < timeScaleList.length; ++i) {
                Map.Entry<ExceptionLevel, Integer> entry = timeScaleList[i];
                if ((long)(Integer)entry.getValue() > runtime) {
                    break;
                }

                suitmin = entry;
            }

            StringBuffer format = (new StringBuffer("执行时间:")).append(runtime).append("毫秒   ").append(f);
            output(suitmin, format.toString(), args);
        }
    }

    private static void output(Map.Entry<ExceptionLevel, Integer> suitmin, String f, Object... args) {
        if (suitmin == null) {
            if (log.isDebugEnabled()) {
                log.debug(f, args);
            }
        } else {
            switch(suitmin.getKey()) {
                case TRACE:
                    if (log.isTraceEnabled()) {
                        log.trace(f, args);
                    }
                    break;
                case DEBUG:
                    if (log.isDebugEnabled()) {
                        log.debug(f, args);
                    }
                    break;
                case WARN:
                    if (log.isWarnEnabled()) {
                        log.warn(f, args);
                    }
                    break;
                case ERROR:
                    if (log.isWarnEnabled()) {
                        log.error(f, args);
                    }
                    break;
                case INFO:
                default:
                    if (log.isInfoEnabled()) {
                        log.info(f, args);
                    }
            }
        }

    }

    public void destroy() throws Exception {
        executor.shutdownNow();
    }

    static {
        timeScale = MapUtils.<ExceptionLevel, Integer>n().a(ExceptionLevel.OFF, 1000).a(ExceptionLevel.TRACE, 10).a(ExceptionLevel.DEBUG, 100).a(ExceptionLevel.INFO, 200).a(ExceptionLevel.WARN, 2000).a(ExceptionLevel.ERROR, 8000).to();
        timeStack = ThreadLocal.withInitial(() -> new Stack());
        executor = Executors.newScheduledThreadPool(4, (r) -> {
            Thread thread = new Thread(r);
            thread.setName("run-time-log-task-" + thread.getId());
            return thread;
        });
        init(timeScale);
    }

    @ConfigurationProperties("logging")
    public class TimeScale implements InitializingBean {
        public TimeScale() {
        }

        public Map<ExceptionLevel, Integer> getTimeScale() {
            return RunTimeLog.timeScale;
        }

        public void setTimeScale(Map<ExceptionLevel, Integer> timeScale) {
            RunTimeLog.timeScale = timeScale;
        }

        public void afterPropertiesSet() throws Exception {
            RunTimeLog.init(RunTimeLog.timeScale);
            RunTimeLog.log.info("api调用日志输出时间参数配置信息{} 日志最少输出的时间 {}", JSONUtils.toJSONString(RunTimeLog.timeScaleList), RunTimeLog.offtime);
        }
    }
}
