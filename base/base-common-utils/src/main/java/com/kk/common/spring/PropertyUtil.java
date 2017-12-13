package com.kk.common.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Properties;

/**
 * Desc:properties文件获取工具类
 */
public class PropertyUtil extends PropertyPlaceholderConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);

    private static Properties properties;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {
        super.processProperties(beanFactory, props);
        properties = props;
    }

    public static String getValue(String key) {
        return properties.getProperty(key);
    }

    public static int getIntValue(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    public static boolean getBooleanValue(String key){
        return Boolean.parseBoolean(properties.getProperty(key));
    }
}