package com.oecoo.gf.service;

import com.oecoo.gf.common.ServerResponse;
import com.oecoo.gf.pojo.User;

/**
 * Created by gf on 2018/4/26.
 */
public interface IUserService {

    ServerResponse<User> login(String username, String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String str,String type);

    ServerResponse<String> selectQuestion(String username);

    ServerResponse<String> checkAnswer(String username,String question,String answer);

    ServerResponse<String> beforeLoginRestPassword(String username,String passwordNew,String forgetToken);

    ServerResponse<String> loginRestPassword(String passwordOld,String passwordNew,User user);

    ServerResponse<User> updateInformation(User user);

    ServerResponse<User> getInformation(Integer userId);

    ServerResponse checkAdminRole(User user);
}
