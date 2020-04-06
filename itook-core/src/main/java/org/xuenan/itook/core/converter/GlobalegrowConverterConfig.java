package org.xuenan.itook.core.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.util.NumberUtils;
import org.xuenan.itook.core.Context;
import org.xuenan.itook.core.utils.DateUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Configuration
public class GlobalegrowConverterConfig implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(GlobalegrowConverterConfig.class);
//    @Autowired
    private ObjectMapper objectMapper= new ObjectMapper();

    public GlobalegrowConverterConfig() {
    }

//    @Bean
//    public GenericConverter numberGenericConverter() {
//        return new GenericConverter() {
//            public Set<ConvertiblePair> getConvertibleTypes() {
//                return Collections.singleton(new ConvertiblePair(String.class, Number.class));
//            }
//
//            public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
//                String sourceStr = (String)source;
//                if (sourceStr != null && !(sourceStr = sourceStr.trim()).isEmpty()) {
//                    return NumberUtils.parseNumber(sourceStr, targetType.getType());
//                } else {
//                    GlobalegrowConverterConfig.log.info("接口{}，参数为空，视图转化为{}", Context.getRequestUri(), targetType.getType().getName());
//                    return null;
//                }
//            }
//        };
//    }

    @Bean
    public GenericConverter enumGenericConverter() {
        return new GenericConverter() {
            public Set<ConvertiblePair> getConvertibleTypes() {
                return Collections.singleton(new ConvertiblePair(String.class, Enum.class));
            }

            public Enum<?> convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
                String sourceStr = (String)source;
                if (sourceStr != null && !(sourceStr = sourceStr.trim()).isEmpty()) {
                    Class type = targetType.getType();

                    try {
                        return Enum.valueOf(type, sourceStr);
                    } catch (Exception var9) {
                        GlobalegrowConverterConfig.log.info("接口{}，参数{}为空，视图转化为枚举{} ，失败", new Object[]{Context.getRequestUri(), sourceStr, targetType.getType().getName()});

                        try {
                            return Enum.valueOf(type, sourceStr.toLowerCase());
                        } catch (Exception var8) {
                            return Enum.valueOf(type, sourceStr.toUpperCase());
                        }
                    }
                } else {
                    GlobalegrowConverterConfig.log.info("接口{}，参数为空，视图转化为{}", Context.getRequestUri(), targetType.getType().getName());
                    return null;
                }
            }
        };
    }

    @Bean
    public Converter<String, String> stringToStringConverter() {
        return new Converter<String, String>() {
            public String convert(String source) {
                if (source == null) {
                    GlobalegrowConverterConfig.log.info("接口{}，参数为空，视图转化为{}", Context.getRequestUri(), String.class.getName());
                    return null;
                } else {
                    return source;
                }
            }
        };
    }

    @Bean
    public Converter<String, Boolean> stringToBooleanConverter() {
        return new Converter<String, Boolean>() {
            public Boolean convert(String source) {
                return source != null && !(source = source.trim()).isEmpty() ? Boolean.valueOf(source) : null;
            }
        };
    }

    @Bean
    public Converter<String, Character> stringToCharConverter() {
        return new Converter<String, Character>() {
            public Character convert(String source) {
                if (source != null && !(source = source.trim()).isEmpty()) {
                    return source.charAt(0);
                } else {
                    GlobalegrowConverterConfig.log.info("接口{}，参数为空，视图转化为{}", Context.getRequestUri(), String.class.getName());
                    return null;
                }
            }
        };
    }

    @Bean
    public Converter<String, Date> stringToDateConverter() {
        return new Converter<String, Date>() {
            public Date convert(String source) {
                return DateUtils.parse(source);
            }
        };
    }

    public SimpleModule simpleModule() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                if (p.getValueAsString() == null) {
                    return null;
                } else {
                    Date data = DateUtils.parse(p.getValueAsString());
                    return data;
                }
            }
        });
        module.addSerializer(Long.class, ToStringSerializer.instance);
        module.addSerializer(Long.TYPE, ToStringSerializer.instance);
        module.addSerializer(Long.TYPE, ToStringSerializer.instance);
        return module;
    }

    public void afterPropertiesSet() throws Exception {
        log.info("初始化转化实例完毕.....");
        this.objectMapper.registerModule(this.simpleModule());
        final DateFormat dd = this.objectMapper.getDateFormat();
        this.objectMapper.setDateFormat(new SimpleDateFormat() {
            private static final long serialVersionUID = 1L;

            {
                if (dd != null) {
                    this.setTimeZone(dd.getTimeZone());
                    this.setCalendar(dd.getCalendar());
                    this.setNumberFormat(dd.getNumberFormat());

                    try {
                        this.setLenient(dd.isLenient());
                    } catch (Exception var4) {
                        GlobalegrowConverterConfig.log.debug("初始化异常", var4);
                    }
                }

            }

            public Date parse(String source, ParsePosition pos) {
                try {
                    pos.setIndex(1);
                    pos.setErrorIndex(0);
                    return DateUtils.parse(source);
                } catch (Throwable var4) {
                    pos.setIndex(0);
                    pos.setErrorIndex(1);
                    return null;
                }
            }

            public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
                return dd == null ? super.format(date, toAppendTo, fieldPosition) : dd.format(date, toAppendTo, fieldPosition);
            }

            public Object clone() {
                return this;
            }
        });
    }
}
