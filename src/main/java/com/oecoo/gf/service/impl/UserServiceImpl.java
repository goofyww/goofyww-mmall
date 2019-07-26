package com.oecoo.gf.service.impl;

import com.oecoo.gf.common.Const;
import com.oecoo.gf.common.ServerResponse;
import com.oecoo.gf.dao.UserMapper;
import com.oecoo.gf.pojo.User;
import com.oecoo.gf.service.IUserService;
import com.oecoo.gf.util.MD5Util;
import com.oecoo.gf.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * Created by gf on 2018/4/26.
 */
@Service("iUserService")
@Transactional(readOnly = true)
@Slf4j
public class UserServiceImpl implements IUserService {

    @Resource
    private UserMapper userMapper;

    public ServerResponse<User> login(String username, String password) {
        if (username.length() == 0 || username == null) {
            log.info("----------username为空----------------");
        }
        //代码复用 登陆之前先校验
        ServerResponse validResponse = this.checkValid(username, Const.USER_NAME);

        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        String md5Password = MD5Util.MD5EncodeUtf8(password);//md5 明文加密
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码输入错误");
        }
        user.setPassword(null);//设置返回user.password为空 ,避免被网路截获
        return ServerResponse.createBySuccessMsgData("登录成功", user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ServerResponse<String> register(User user) {
        if (StringUtils.isBlank(user.getPassword())) {
            return ServerResponse.createByErrorMessage("注册时的密码不应为空");
        }
        //代码复用 注册之前先校验
        ServerResponse validResponse = this.checkValid(user.getUsername(), Const.USER_NAME);//检查用户名是否有重复
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        validResponse = this.checkValid(user.getEmail(), Const.EMAIL);//检查Email是否有重复
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        //校验通过再开始对user进行相关操作
        user.setRole(Const.Role.ROLE_CUSTOMER);//设置角色

        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));//MD5明文加密
        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    //校验    不存在 则为 true
    public ServerResponse<String> checkValid(String str, String type) {
        //type不为空再判断  注意: NotBlank 认为“ ”空格 不存在内容 为false
        if (StringUtils.isNotBlank(type)) {
            //开始校验
            if (Const.USER_NAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("email已存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    //获取用户问题
    public ServerResponse<String> selectQuestion(String username) {
        ServerResponse validResponse = checkValid(username, Const.USER_NAME);//校验
        // 不存在 则为 true
        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        //开始通过username查询question
        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccessData(question);
        }
        return ServerResponse.createByErrorMessage("该用户未设置找回密码问题");
    }

    //检查问题答案
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        ServerResponse validResponse = checkValid(username, Const.USER_NAME);//校验
        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        if (StringUtils.isBlank(answer)) {
            return ServerResponse.createByErrorMessage("并没有接收到提交的答案，请重新提交");
        }
        //开始检查
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            //生成令牌  作为value
            String forgetToken = UUID.randomUUID().toString();
            //防止横向越权 纵向越权 保证这个key是唯一的
            RedisShardedPoolUtil.setEx(Const.TokenCache.TOKEN_PREFIX + username, forgetToken, 60 * 30);
            return ServerResponse.createBySuccessData(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题的答案错误");
    }

    //未登录 ======>  修改密码
    public ServerResponse<String> beforeLoginRestPassword(String username, String passwordNew, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("参数错误,后台没有收到Token");
        }
        ServerResponse validResponse = checkValid(username, Const.USER_NAME);//校验
        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        String token = RedisShardedPoolUtil.get(Const.TokenCache.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("Token已失效过过期,请重新获取token");
        }
        if (StringUtils.equals(token, forgetToken)) {
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int resultCount = userMapper.updatePasswordByUsername(username, md5Password);
            if (resultCount > 0) {
                RedisShardedPoolUtil.del(Const.TokenCache.TOKEN_PREFIX + username);
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        } else {
            return ServerResponse.createByErrorMessage("Token错误,请重新获取重置密码的Token ");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    //已登录  =======>  修改密码
    public ServerResponse<String> loginRestPassword(String passwordOld, String passwordNew, User user) {
        //防止横向越权 在修改数据库时 一定要指定某条数据一定是属于当前这个用户的
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMessage("修改密码成功");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    //用户更新信息
    public ServerResponse<User> updateInformation(User user) {
        // username是唯一键 不可更新
        // email也要进行一个校验,校验新的email是不是已经存在,并且存在的email如果相同的话,不能是我们当前的这个用户的.
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("Email已存在,请更换Email再进行更新操作");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMsgData("更新用户信息成功", updateUser);
        }
        return ServerResponse.createByErrorMessage("更新用户信息失败");
    }

    //获取当前登录用户的详细信息
    public ServerResponse<User> getInformation(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorMessage("获取信息失败,找不到这个用户");
        }
        user.setPassword(null);
        return ServerResponse.createBySuccessData(user);
    }

    //bankend

    //判断当前用户是否是管理员
    public ServerResponse checkAdminRole(User user) {
        if (user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }
}
