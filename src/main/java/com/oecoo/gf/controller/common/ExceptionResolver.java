package com.oecoo.gf.controller.common;

import com.oecoo.gf.common.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常处理
 */
@Slf4j
@Component
public class ExceptionResolver implements HandlerExceptionResolver {

    public ModelAndView resolveException(HttpServletRequest httpServletRequest,
                                         HttpServletResponse httpServletResponse,
                                         Object o,
                                         Exception e) {
        // 日志打印具体的异常堆栈信息，否则异常线索会丢失，难以进行日志分析
        log.error("{} Exception ", httpServletRequest.getRequestURI(), e);
        // 返回前端友好且简短的 json方式的异常信息
        ModelAndView mv = new ModelAndView(new MappingJacksonJsonView());
        // 当前使用的Jackson是1.9版本
        // 当pom.xml依赖的是Jackson2以上版本时，此处给ModelAndView构造器中需传入 MappingJackson2JsonView实例
        mv.addObject("status", ResponseCode.ERROR.getCode());
        mv.addObject("msg", "接口异常，详情请查看服务端日志的异常信息");
        mv.addObject("data", e.toString());
        return mv;
    }

}
