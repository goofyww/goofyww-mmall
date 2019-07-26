package com.oecoo.gf.controller.common;

import com.oecoo.gf.common.Const;
import com.oecoo.gf.pojo.User;
import com.oecoo.gf.util.CookieUtil;
import com.oecoo.gf.util.JsonUtil;
import com.oecoo.gf.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 重置session过期时间
 */
public class SessionExpireFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if (StringUtils.isNotEmpty(loginToken)) {
            String userStr = RedisShardedPoolUtil.get(loginToken);
            User user = JsonUtil.string2Obj(userStr, User.class);
            if (user != null) {
                RedisShardedPoolUtil.expire(loginToken, Const.RedisCacheExTime.REDIS_SESSION_EXTIME);
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

}
