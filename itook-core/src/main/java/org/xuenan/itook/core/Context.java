package org.xuenan.itook.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;
import org.xuenan.itook.core.code.Sequence;
import org.xuenan.itook.core.consts.Const;
import org.xuenan.itook.core.exception.GlobalException;
import org.xuenan.itook.core.exception.GlobalExceptionStatus;
import org.xuenan.itook.core.model.PlatformEnum;
import org.xuenan.itook.core.model.SysUser;
import org.xuenan.itook.core.utils.Assert;
import org.xuenan.itook.core.utils.ContextUtils;
import org.xuenan.itook.core.utils.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;


public final class Context {

    private static final ThreadLocal<Context> contextHolder = new ThreadLocal();
    private static Logger logger = LoggerFactory.getLogger(Context.class);
    private Map<String, Object> contextMap;
    private HttpServletResponse response;
    private HttpServletRequest request;
    private String message = null;
    private Long beginTime = null;
    private String title = null;
    private String clientid = null;
    private String host;
    private String callback = null;
    private SysUser user = null;
    private String hostname = null;
    private String requestUri = null;
    private String userid = null;
    private Boolean ispc = null;
    private Boolean isRest = false;
    private Boolean now = null;
    private static final String error_uri_key = "javax.servlet.error.request_uri";
    private static final String[] keys = new String[]{"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP"};
    private static final String sign_in_para = "sign";
    private static final String timestamp_in_para = "timestamp";
    private static final String deviceid_in_para = "deviceId";
    private static final String curplatform_in_para = "curPlatform";
    private static final String curversioncode_in_para = "curVersionCode";
    private static final String cur_app_channel_id = "appChannelId";

    public Context() {
    }

    public static Context getContext() {
        now();
        return (Context) contextHolder.get();
    }

    public static boolean now() {
        Context context = (Context) contextHolder.get();
        if (context != null) {
            return false;
        } else {
            contextHolder.set(new Context());
            return true;
        }
    }

    public static Context init(HttpServletRequest request, HttpServletResponse response) {
        if (request == null) {
            return null;
        } else {
            Context context = getContext();
            context.request = request;
            context.response = response;
            context.beginTime = System.currentTimeMillis();
            context.host = request.getHeader("Host");
            context.clientid = null;
            context.user = null;
            context.hostname = null;
            context.requestUri = null;
            context.userid = null;
            context.ispc = null;
            context.now = null;
            context.isRest = false;
            return context;
        }
    }

    public static boolean isPc() {
        Context context = getContext();
        if (context.ispc != null) {
            return context.ispc;
        } else {
            return context.request == null ? context.ispc = true : true;
        }
    }

    public static boolean isLogin() {
        SysUser user = getUser0();
        return user != null && user.isLogin();
    }

    public static void sendUserId() {
        String userid = getCookie("USERID_COOKIE_NAME");
        if (userid == null) {
            userid = "UID-" + Sequence.generate();
            addCookie("USERID_COOKIE_NAME", userid);
            getContext().userid = userid;
        }

    }

    public static String getCallback() {
        Context context = getContext();
        if (context.callback == null && context.request != null) {
            context.callback = context.request.getParameter("jsoncallback");
        }

        return context.callback;
    }

    public static String getHostName() {
        Context context = getContext();
        if (context.hostname != null) {
            return context.hostname;
        } else {
            int endIndex;
            int beginIndex;
            return context.host != null && 0 < (endIndex = context.host.lastIndexOf(46)) && 0 <= (beginIndex = context.host.lastIndexOf(46, endIndex - 1)) && 1 < endIndex - beginIndex ? (context.hostname = context.host.substring(beginIndex + 1, endIndex)) : (context.hostname = "");
        }
    }

    public static String getHost() {
        return getContext().host;
    }

    public static void isRest(boolean isResEntity) {
        getContext().isRest = isResEntity;
    }

    public static boolean isRest() {
        return getContext().isRest;
    }

    public static boolean isNow() {
        Context context = getContext();
        if (context.now == null) {
            if (get("now") == null) {
                context.now = false;
            } else {
                context.now = true;
            }
        }

        return context.now;
    }

    public static String getRequestUri() {
        Context context = getContext();
        if (context.requestUri != null) {
            return context.requestUri;
        } else if (context.request == null) {
            return context.requestUri = "";
        } else {
            String error_uri = (String) context.request.getAttribute("javax.servlet.error.request_uri");
            return error_uri != null ? (context.requestUri = error_uri) : (context.requestUri = context.request.getRequestURI());
        }
    }

    public static void remove() {
        contextHolder.remove();
    }

    private static SysUser getUser0() {
        Context context = getContext();
        if (context.user != null) {
            return context.user;
        } else {
            return context.request == null ? (context.user = null) : ContextUtils.initLoginInfo();
        }
    }

    public static SysUser getUser() {
        return getUser0();
    }

    public static SysUser getLoginUser() {
        if (!isLogin()) {
            GlobalException.exception(GlobalExceptionStatus.UNAUTHORIZED);
        }

        return getUser0();
    }

    public static void setUser(SysUser user) {
        getContext().user = user;
    }

    public static long getBeginTime() {
        Context context = getContext();
        return context.beginTime == null ? System.currentTimeMillis() : context.beginTime;
    }

    public static String getMessage() {
        return getContext().message;
    }

    public static void setMessage(String message) {
        getContext().message = message;
    }

    public static String getTitle() {
        return getContext().title;
    }

    public static void setTitle(String titleformat, Object... arg1) {
        getContext().title = String.format(titleformat, arg1);
    }

    public static void setRequest(HttpServletRequest request) {
        getContext().request = request;
    }

    public static void setResponse(HttpServletResponse response) {
        getContext().response = response;
    }

    public static HttpServletRequest getRequest() {
        return getContext().request;
    }

    public static HttpSession getSession() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        } else {
            int i = 0;

            while (i < 3) {
                try {
                    return request.getSession();
                } catch (Exception var3) {
                    var3.printStackTrace();
                    ++i;
                }
            }

            return null;
        }
    }

    public static HttpServletResponse getResponse() {
        return getContext().response;
    }

    public static void put(String key, Object value) {
        Context context = getContext();
        if (context.request != null) {
            context.request.setAttribute(key, value);
        } else {
            if (context.contextMap == null) {
                context.contextMap = new LinkedHashMap();
            }

            context.contextMap.put(key, value);
        }

    }

    public static Object get(String key) {
        Object value = getForServer(key);
        return value != null ? value : getForRequest(key);
    }

    public static Object getForServer(String key) {
        Context context = getContext();
        Map<String, Object> map = context.contextMap;
        if (map != null && map.containsKey(key)) {
            return map.get(key);
        } else if (context.request == null) {
            return null;
        } else {
            HttpServletRequest request = context.request;
            Object value = null;
            if ((value = request.getAttribute(key)) != null) {
                return value;
            } else {
                ServletContext servletContext = request.getServletContext();
                if ((value = servletContext.getAttribute(key)) != null) {
                    return value;
                } else {
                    HttpSession session = getSession();
                    return session != null ? session.getAttribute(key) : null;
                }
            }
        }
    }

    public static String getForRequest(String key) {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        } else {
            String value = null;
            if ((value = request.getParameter(key)) != null) {
                return value;
            } else if ((value = request.getHeader(key)) != null) {
                return value;
            } else {
                ServletContext servletContext = request.getServletContext();
                return (value = servletContext.getInitParameter(key)) != null ? value : getCookie(key);
            }
        }
    }

    public static String getCookie(String name) {
        if (StringUtils.isEmpty(new String[]{name})) {
            logger.debug("Cookie Name 为空 ：CookieName={}", name);
            return null;
        } else {
            HttpServletRequest request = getRequest();
            if (request == null) {
                return null;
            } else {
                Cookie[] cookies = request.getCookies();
                if (cookies == null) {
                    logger.debug("没有取到 Cookie ：CookieName={}", name);
                    return null;
                } else {
                    Cookie[] var3 = cookies;
                    int var4 = cookies.length;

                    for (int var5 = 0; var5 < var4; ++var5) {
                        Cookie cookie = var3[var5];
                        if (name.equals(cookie.getName())) {
                            try {
                                String cookie_value = cookie.getValue();
                                cookie_value = URLDecoder.decode(cookie_value, Const.DEFAULT_CHARSET.name());
                                logger.debug(" Cookie中取到值 ：CookieName={},CookieValue={}", name, cookie_value);
                                return cookie_value;
                            } catch (UnsupportedEncodingException var8) {
                                logger.warn("读取cookie name={},出现异常", name, var8);
                            }
                        }
                    }

                    logger.debug(" Cookie中没有取到值 ：CookieName={}", name);
                    return null;
                }
            }
        }
    }

    public static void addCookie(String name, String value) {
        addCookie(name, value, (String) null, (Integer) null, (Boolean) null, (Integer) null);
    }

    public static void addCookie(String name, String value, String domain) {
        addCookie(name, value, domain, (Integer) null, (Boolean) null, (Integer) null);
    }

    public static void addCookie(String name, String value, Integer maxage) {
        addCookie(name, value, (String) null, maxage, (Boolean) null, (Integer) null);
    }

    public static void addCookie(String name, String value, String domain, Integer maxage) {
        addCookie(name, value, domain, maxage, (Boolean) null, (Integer) null);
    }

    public static void addCookie(String name, String value, String domain, Integer maxage, Boolean httpOnly, Integer version) {
        HttpServletResponse response = getResponse();
        if (response != null && !StringUtils.isEmpty(new String[]{name}) && !StringUtils.isEmpty(new String[]{value})) {
            try {
                Cookie cookie = new Cookie(name, URLEncoder.encode(value, Const.DEFAULT_CHARSET.name()));
                if (domain != null) {
                    cookie.setDomain(domain);
                }

                if (maxage == null) {
                    maxage = 2147483647;
                }

                cookie.setMaxAge(maxage);
                if (httpOnly != null) {
                    cookie.setHttpOnly(httpOnly);
                }

                if (version != null) {
                    cookie.setVersion(version);
                }

                cookie.setPath("/");
                response.addCookie(cookie);
            } catch (UnsupportedEncodingException var8) {
                logger.info("下发Cookie {}失败", name, var8);
            }

        }
    }

    public static void delCookie(String name) {
        delCookie(name, (String) null);
    }

    public static void delCookie(String name, String domain) {
        if (name != null) {
            HttpServletResponse response = getResponse();
            if (response == null) {
                logger.info("response对象为空....");
            } else {
                logger.debug("即将从cookie删除{},", name);
                Cookie delcookie = new Cookie(name, "");
                delcookie.setPath("/");
                if (domain != null) {
                    delcookie.setDomain(domain);
                }

                delcookie.setMaxAge(0);
                response.addCookie(delcookie);
            }
        }
    }

    public static String getClientIP() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        } else {
            String[] var1 = keys;
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                String key = var1[var3];
                String ip = request.getHeader(key);
                if (StringUtils.isNotEmpty(new String[]{ip}) && !"unknown".equalsIgnoreCase(ip)) {
                    return ip.split(",")[0];
                }
            }

            return request.getRemoteAddr();
        }
    }

    public static String getClientIPLink() {
        HttpServletRequest request = getRequest();
        return request == null ? null : request.getHeader(keys[0]);
    }

    public static void sendRedirect(String redirecturi) {
        logger.info("请求转发至" + redirecturi);
        HttpServletResponse response = getResponse();
        if (response != null) {
            try {
                response.sendRedirect(redirecturi);
            } catch (IOException var3) {
                logger.warn("请求转发至" + redirecturi + "出现异常", var3);
            }

        }
    }

    public static void forward(String forwarduri) {
        logger.info("请求转发至" + forwarduri);
        HttpServletRequest request = getRequest();
        if (request != null) {
            try {
                request.getRequestDispatcher(forwarduri).forward(request, getResponse());
            } catch (ServletException | IOException var3) {
                logger.warn("请求转发至" + forwarduri + "出现异常", var3);
            }

        }
    }

    public static void download(File file) throws IOException {
        Assert.notNull(file, "文件不存在", new Object[0]);
        Assert.isTrue(file.exists(), "文件不存在", new Object[0]);
        String fileName = file.getName();
        long length = file.length();
        FileInputStream inputStream = new FileInputStream(file);

        try {
            download(fileName, length, inputStream);
        } finally {
            inputStream.close();
        }

    }

    public static void download(String fileName, long length, InputStream inputStream) throws IOException {
        HttpServletResponse response = getResponse();
        response.reset();
        response.setContentType("application/octet-stream");
        if (fileName != null) {
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO-8859-1"));
        }

        response.addHeader("Content-Length", String.valueOf(length));
        ServletOutputStream out = response.getOutputStream();

        try {
            StreamUtils.copy(inputStream, out);
        } finally {
            out.flush();
            out.close();
        }

    }

    public static String getUserId() {
        if (isLogin()) {
            return getUser0().getUserId();
        } else {
            String userid = getContext().userid;
            return userid != null ? userid : getCookie("USERID_COOKIE_NAME");
        }
    }

    public static String getClientid() {
        Context context = getContext();
        if (context.clientid == null) {
            context.clientid = String.valueOf(get("client"));
        }

        return context.clientid;
    }

    public static void setClientId(String clientId) {
        getContext().clientid = clientId;
    }

    public static String getScheme() {
        return getRequest().getScheme();
    }

    public String toString() {
        String referer = getForRequest("Referer");
        String userAgent = getForRequest("User-Agent");
        String clientid = getClientid();
        String clientIP = getClientIP();
        String deviceid = getDeviceid();
        Integer versionCode = getCurversionCode();
        String userId = getUserId();
        PlatformEnum platform = getCurplatform();
        String clientIPLink = getClientIPLink();
        StringBuffer message = new StringBuffer(" [[请求上下文信息 ]]  ");
        message.append("\n    >>>> 请求地址:[").append(getHost()).append(getRequestUri()).append("]");
        if (referer != null) {
            message.append("\n      >>>>>>  请求来源: [").append(referer).append("]");
        }

        if (userAgent != null) {
            message.append("\n      >>>>>>  客户端信息:[").append(userAgent).append("]");
        }

        if (clientid != null) {
            message.append("\n      >>>>>>  客户端编号:[").append(clientid).append("]");
        }

        if (clientIP != null) {
            message.append("\n      >>>>>>  客户端IP:[").append(clientIP).append("]");
        }

        if (deviceid != null) {
            message.append("\n      >>>>>>  deviceid:[").append(deviceid).append("]");
        }

        if (versionCode != null) {
            message.append("\n      >>>>>>  versionCode:[").append(versionCode).append("]");
        }

        if (userId != null) {
            message.append("\n      >>>>>>  userId:[").append(userId).append("]");
        }

        if (platform != null) {
            message.append("\n      >>>>>>  platform:[").append(platform).append("]");
        }

        if (clientIPLink != null) {
            message.append("\n      >>>>>>  客户端ip链路:[").append(clientIPLink).append("]");
        }

        if (this.message != null) {
            message.append("\n      >>>>>>  上下文描述信息:[").append(this.message).append("]");
        }

        return message.toString();
    }

    public static String getSign() {
        return getForRequest("sign");
    }

    public static Long getTimestamp() {
        String timestamp = getForRequest("timestamp");
        if (StringUtils.isEmpty(new String[]{timestamp})) {
            return null;
        } else {
            try {
                return (long) Double.parseDouble(timestamp);
            } catch (Exception var2) {
                return null;
            }
        }
    }

    public static String getDeviceid() {
        return getForRequest("deviceId");
    }

    public static PlatformEnum getCurplatform() {
        String curPlatform = getForRequest("curPlatform");
        if (StringUtils.isEmpty(curPlatform)) {
            return null;
        } else {
            try {
                return PlatformEnum.valueOfName(curPlatform);
            } catch (Exception var2) {
                return null;
            }
        }
    }

    public static Integer getCurversionCode() {
        String curVersionCode = getForRequest("curVersionCode");
        if (StringUtils.isEmpty(new String[]{curVersionCode})) {
            return null;
        } else {
            try {
                return (int) Double.parseDouble(curVersionCode);
            } catch (Exception var2) {
                return null;
            }
        }
    }

    public static String getCurAppChannelId() {
        return getForRequest("appChannelId");
    }

}
