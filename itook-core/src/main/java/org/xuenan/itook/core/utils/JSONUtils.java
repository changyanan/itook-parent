package org.xuenan.itook.core.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public abstract class JSONUtils {
    private static final Logger logger = LoggerFactory.getLogger(JSONUtils.class);
    private static ObjectMapper jacksonObjectMapper = new ObjectMapper();

    public JSONUtils() {
    }

    @Autowired
    void setObjectMapper(ObjectMapper jacksonObjectMapper) {
        JSONUtils.jacksonObjectMapper = jacksonObjectMapper;
    }

    public static String toJSONString(Object obj) {
        try {
            return obj == null ? null : jacksonObjectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException var2) {
            logger.error("对象序列化为json字符串失败", var2);
            return null;
        }
    }

    public static <T> T fromObject(String content, TypeReference<T> typeReference) {
        try {
            return StringUtils.isEmpty(new String[]{content}) ? null : jacksonObjectMapper.readValue(content, typeReference);
        } catch (IOException var3) {
            logger.error(" 反序列化json失败", var3);
            return null;
        }
    }

    public static <T> T fromObject(String content, Class<T> tagcaz) {
        try {
            return StringUtils.isEmpty(new String[]{content}) ? null : jacksonObjectMapper.readValue(content, tagcaz);
        } catch (IOException var3) {
            logger.error(" 反序列化json失败  ", var3);
            return null;
        }
    }

    public static <T> T fromObject(Object obj, Class<T> tagcaz) {
        try {
            if (obj == null) {
                return null;
            } else {
                String content = toJSONString(obj);
                return jacksonObjectMapper.readValue(content, tagcaz);
            }
        } catch (IOException var3) {
            logger.error(" 反序列化json失败", var3);
            return null;
        }
    }

    public static <T1, T2> Map<T1, T2> fromObject(String content) {
        try {
            return StringUtils.isEmpty(new String[]{content}) ? null : (Map)jacksonObjectMapper.readValue(content, new TypeReference<Map<T1, T2>>() {
            });
        } catch (IOException var2) {
            logger.error(" 反序列化json为map失败", var2);
            return null;
        }
    }

    public static <T> List<T> fromArray(String content) {
        try {
            return StringUtils.isEmpty(new String[]{content}) ? null : (List)jacksonObjectMapper.readValue(content, new TypeReference<List<T>>() {
            });
        } catch (IOException var2) {
            logger.error(" 反序列化json数组失败", var2);
            return null;
        }
    }

    public static <T> T fromObject(File inputfile, Class<T> t) {
        try {
            return jacksonObjectMapper.readValue(inputfile, t);
        } catch (IOException var3) {
            logger.error("读文件{}反序列化为{}类型失败", new Object[]{inputfile.getAbsoluteFile(), t.getName(), var3});
            return null;
        }
    }

    public static <T> T fromObject(byte[] bytes, Class<T> tagcaz) {
        try {
            return bytes == null ? null : jacksonObjectMapper.readValue(bytes, 0, bytes.length, tagcaz);
        } catch (IOException var3) {
            logger.error(" 反序列化json失败", var3);
            return null;
        }
    }

    public static <T> T fromObject(byte[] bytes, TypeReference<T> ref) {
        try {
            return bytes == null ? null : jacksonObjectMapper.readValue(bytes, 0, bytes.length, ref);
        } catch (IOException var3) {
            logger.error(" 反序列化json失败", var3);
            return null;
        }
    }

    public static Object fromObject(String content, Type tagType) {
        try {
            if (StringUtils.isEmpty(new String[]{content})) {
                return null;
            } else {
                JavaType javaType = TypeFactory.defaultInstance().constructType(tagType);
                return jacksonObjectMapper.readValue(content, javaType);
            }
        } catch (IOException var3) {
            logger.error(" 反序列化json失败", var3);
            return null;
        }
    }

    public static List<Object> getJsonObjectByPath(String json, String... paths) {
        List<Object> ret = new ArrayList(paths.length);
        Map<String, Object> map = fromObject(json);
        String[] var4 = paths;
        int var5 = paths.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String path = var4[var6];
            Object node = map;
            String[] var9 = path.split("\\.");
            int var10 = var9.length;

            for(int var11 = 0; var11 < var10; ++var11) {
                String p = var9[var11];
                if (List.class.isInstance(node)) {
                    Integer index = Integer.parseInt(p);
                    if (((List)node).size() <= index) {
                        node = null;
                    } else {
                        node = ((List)node).get(index);
                    }
                } else {
                    node = ((Map)node).get(p);
                }

                if (node == null) {
                    break;
                }
            }

            ret.add(node);
        }

        return ret;
    }

    static {
        jacksonObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        jacksonObjectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        jacksonObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
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
        jacksonObjectMapper.registerModule(module);
    }
}
