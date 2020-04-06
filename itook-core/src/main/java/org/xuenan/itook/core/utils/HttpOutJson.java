package org.xuenan.itook.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.xuenan.itook.core.Context;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class HttpOutJson {
    private static final Logger logger = LoggerFactory.getLogger(HttpOutJson.class);
    private static AbstractJackson2HttpMessageConverter jsonConverter;

    public HttpOutJson() {
    }

    @Autowired
    public void setJsonConverter(AbstractJackson2HttpMessageConverter jsonConverter) {
        HttpOutJson.jsonConverter = jsonConverter;
    }

    public static boolean isOutEd() {
        return null != Context.get("http is out ed !");
    }

    public static void out(Object model) {
        out(model, 200);
    }

    public static void out(Object model, int status) {
        try {
            if (isOutEd()) {
                logger.warn("0. 第二次尝试http输出‘{}’ ", model);
                return;
            }

            Context.put("http is out ed !", true);
            HttpServletResponse response = Context.getResponse();
            if (response != null) {
                response.setStatus(status);
                jsonConverter.write(model, MediaType.APPLICATION_JSON, new ServletServerHttpResponse(response));
            } else {
                logger.warn("0。无法完成资源的输出，response不存在，资源‘{}’", model);
            }
        } catch (IOException | HttpMessageNotWritableException var3) {
            logger.warn("1. 输出资源‘{}’异常 ：{}", model, var3.getMessage());
        } catch (Exception var4) {
            logger.warn("2.  输出资源‘{}’异常：{}", new Object[]{model, var4.getMessage(), var4});
        }

    }
}
