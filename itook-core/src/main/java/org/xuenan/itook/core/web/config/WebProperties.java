package org.xuenan.itook.core.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.xuenan.itook.core.model.SysModel;
import org.xuenan.itook.core.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties("globalegrow")
public class WebProperties extends SysModel {
    private static final long serialVersionUID = 1L;
    static final String prefix = "globalegrow";
    private boolean debug = false;
    private WebProperties.Limiter limiter = new WebProperties.Limiter();
    private List<Cors> cors;
    private String[] jsonpQueryParamNames;

    public WebProperties() {
    }

    public List<WebProperties.Cors> getCors() {
        return this.cors;
    }

    public void setCors(List<WebProperties.Cors> cors) {
        this.cors = cors;
    }

    public String[] getJsonpQueryParamNames() {
        return this.jsonpQueryParamNames;
    }

    public void setJsonpQueryParamNames(String[] jsonpQueryParamNames) {
        this.jsonpQueryParamNames = jsonpQueryParamNames;
    }

    public boolean isDebug() {
        return this.debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public WebProperties.Limiter getLimiter() {
        return this.limiter;
    }

    public void setLimiter(WebProperties.Limiter limiter) {
        this.limiter = limiter;
    }

    public static class Cors extends SysModel {
        private static final long serialVersionUID = 1L;
        private String pathPattern = "*";
        private Long maxAge = 86400L;
        private List<String> allowedOrigins = new ArrayList();
        private List<HttpMethod> allowedMethods;
        private List<String> allowedHeaders;
        private List<String> exposedHeaders;
        private Boolean allowCredentials;

        public Cors() {
            this.allowedMethods = ListUtils.n(new HttpMethod[]{HttpMethod.HEAD, HttpMethod.OPTIONS}).to();
            this.allowedHeaders = ListUtils.n("Accept,Accept-Encoding,Accept-Language,Connection,Content-Length,Content-Type,Cookie,Host,Origin,Referer,User-Agent,timestamp,sign,curPlatform".split(",")).to();
            this.exposedHeaders = new ArrayList();
            this.allowCredentials = false;
        }

        public String getPathPattern() {
            return this.pathPattern;
        }

        public void setPathPattern(String pathPattern) {
            this.pathPattern = pathPattern;
        }

        public List<String> getAllowedOrigins() {
            return this.allowedOrigins;
        }

        public void setAllowedOrigins(List<String> allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }

        public List<HttpMethod> getAllowedMethods() {
            return this.allowedMethods;
        }

        public void setAllowedMethods(List<HttpMethod> allowedMethods) {
            this.allowedMethods = allowedMethods;
        }

        public List<String> getAllowedHeaders() {
            return this.allowedHeaders;
        }

        public void setAllowedHeaders(List<String> allowedHeaders) {
            this.allowedHeaders = allowedHeaders;
        }

        public List<String> getExposedHeaders() {
            return this.exposedHeaders;
        }

        public void setExposedHeaders(List<String> exposedHeaders) {
            this.exposedHeaders = exposedHeaders;
        }

        public Boolean getAllowCredentials() {
            return this.allowCredentials;
        }

        public void setAllowCredentials(Boolean allowCredentials) {
            this.allowCredentials = allowCredentials;
        }

        public Long getMaxAge() {
            return this.maxAge;
        }

        public void setMaxAge(Long maxAge) {
            this.maxAge = maxAge;
        }
    }

    public static class Limiter extends SysModel {
        private static final long serialVersionUID = 1L;
        private boolean enabled = true;

        public Limiter() {
        }

        public boolean isEnabled() {
            return this.enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
