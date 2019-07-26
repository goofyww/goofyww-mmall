package com.oecoo.gf.controller.common.interceptor;

import com.google.common.collect.Maps;
import com.oecoo.gf.common.Const;
import com.oecoo.gf.common.ResponseOut;
import com.oecoo.gf.common.ServerResponse;
import com.oecoo.gf.pojo.User;
import com.oecoo.gf.util.CookieUtil;
import com.oecoo.gf.util.JsonUtil;
import com.oecoo.gf.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class AuthorityInterceptor extends ResponseOut implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle");

        //获得请求中的方法信息
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        //解析HandlerMethod
        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBean().getClass().getSimpleName();
        String routeURL = request.getRequestURI();

        //解析参数，具体的参数key以及value是什么，用作打印日志
        StringBuffer requestParamBuffer = new StringBuffer();
        Map paramMap = request.getParameterMap();
        Iterator it = paramMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String mapKey = (String) entry.getKey();
            String mapValue = StringUtils.EMPTY;

            //request这个参数返回的map,里面的value返回的是一个string[]
            Object obj = entry.getValue();
            if (obj instanceof String[]) {
                String[] strs = (String[]) obj;
                mapValue = Arrays.toString(strs);
            }
            requestParamBuffer.append(mapKey).append("=").append(mapValue);
        }

        log.info("权限拦截器拦截到请求，className:{},methodName:{},url:{},param:{}",className,methodName,routeURL,requestParamBuffer);

        User user = null;
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isNotEmpty(loginToken)) {
            user = JsonUtil.string2Obj(RedisShardedPoolUtil.get(loginToken), User.class);
        }
        if (user == null || (user.getRole().intValue() != Const.Role.ROLE_ADMIN)) {
            // 返回false,即不会调用controller中的方法
            if (user == null) {
                if (StringUtils.equals(routeURL, "/manage/product/richtext_img_upload.do")) {
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success", false);
                    resultMap.put("msg", "用户未登录,请先登陆");
                    send(JsonUtil.obj2String(resultMap), response);
                } else {
                    send(JsonUtil.obj2String(ServerResponse.createByErrorMessage("拦截器拦截，用户未登录")), response);
                }
            } else {
                if (StringUtils.equals(routeURL, "/manage/product/richtext_img_upload.do")) {
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success", false);
                    resultMap.put("msg", "无权限操作，需要管理员权限");
                    send(JsonUtil.obj2String(resultMap), response);
                } else {
                    send(JsonUtil.obj2String(ServerResponse.createByErrorMessage("拦截器拦截，用户无权限登录")), response);
                }
            }
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("afterCompletion");
    }
}
