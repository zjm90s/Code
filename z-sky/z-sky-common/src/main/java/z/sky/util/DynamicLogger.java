package z.sky.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import lombok.Data;

/**
 * Logback动态调整日志级别
 * 
 * @author zhoujianming
 *
 */
public class DynamicLogger {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(DynamicLogger.class);

    private static Map<String, Level> loggerLevelMap = new HashMap<>();

    /**
     * 设置日志级别
     * @param loggerLevel
     * @return
     */
    public static String setLogLevel(LoggerLevel loggerLevel) {
        try {
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            // 设置全局日志级别
            if (StringUtils.isNotBlank(loggerLevel.getRootLevel())) {
                Logger rootLogger = loggerContext.getLogger("root");
                if (!loggerLevelMap.containsKey(rootLogger.getName())) {
                    loggerLevelMap.put(rootLogger.getName(), rootLogger.getLevel());
                }
                rootLogger.setLevel(Level.toLevel(loggerLevel.getRootLevel()));
            }

            // 设置指定类日志级别
            if (!CollectionUtils.isEmpty(loggerLevel.getLogNames())
                    && StringUtils.isNotBlank(loggerLevel.getLogLevel())) {
                List<Logger> loggerList = loggerContext.getLoggerList();
                Map<String, Logger> loggerSet = loggerList.stream().collect(Collectors.toMap(p -> p.getName(), p -> p));
                for (String loggerName : loggerLevel.getLogNames()) {
                    if (loggerSet.containsKey(loggerName)) {
                        Logger logger = loggerSet.get(loggerName);
                        if (!loggerLevelMap.containsKey(logger.getName())) {
                            Level level = logger.getLevel() != null ? logger.getLevel() : Level.INFO;
                            loggerLevelMap.put(logger.getName(), level);
                        }
                        logger.setLevel(Level.toLevel(loggerLevel.getLogLevel()));
                    } else {
                        LOG.warn("Logger[{}] is not exist", loggerName);
                        return String.format("Logger[%s] is not exist", loggerName);
                    }
                }
            }
            return "success";
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    /**
     * 重置日志级别
     * @return
     */
    public static String resetLogLevel() {
        try {
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            for (String loggerName : loggerLevelMap.keySet()) {
                Logger logger = loggerContext.getLogger(loggerName);
                logger.setLevel(loggerLevelMap.get(loggerName));
            }
            return "success";
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    @Data
    public static class LoggerLevel {
        private String rootLevel;
        private List<String> logNames;
        private String logLevel;
    }

}
