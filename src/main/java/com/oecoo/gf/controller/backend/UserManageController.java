package com.oecoo.gf.controller.backend;

import com.oecoo.gf.common.Const;
import com.oecoo.gf.common.ServerResponse;
import com.oecoo.gf.pojo.User;
import com.oecoo.gf.service.IUserService;
import com.oecoo.gf.util.CookieUtil;
import com.oecoo.gf.util.JsonUtil;
import com.oecoo.gf.util.RedisShardedPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by gf on 2018/4/28.
 */
@Controller
@RequestMapping("/manage/user/")
public class UserManageController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "login.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> login(HttpServletResponse httpServletResponse, HttpSession session, String username, String password) {
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            User user = response.getData();
            //Role 1 管理员
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                // 写入 Cookie
                CookieUtil.writeLoginToken(httpServletResponse, session.getId());
                // 将登录用户信息存入redis，有效时间为30分钟
                RedisShardedPoolUtil.setEx(session.getId(), JsonUtil.obj2String(response.getData()), Const.RedisCacheExTime.REDIS_SESSION_EXTIME);
                return response;
            } else {
                return ServerResponse.createByErrorMessage("不是管理员,无法登录");
            }
        }
        return response;
    }
}
