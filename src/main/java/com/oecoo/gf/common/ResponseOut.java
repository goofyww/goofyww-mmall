package com.oecoo.gf.common;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 重置 response 向前端发送结果数据
 * @author gf
 * @date 2019/5/3
 */
@Slf4j
public abstract class ResponseOut {

    /**
     * 重置response 用于向前台发送数据
     * @param obj
     * @param response
     */
    public void send(Object obj, HttpServletResponse response) {
        response.reset(); // 重置response
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(obj);
            out.flush();
        } catch (IOException e) {
            log.error("ResponseResetOut send方法发生异常 {}", e);
        }
    }


}
