package com.zhenai.xunta.listener;

import com.zhenai.xunta.model.User;

/**
 * Created by wenjing.tang on 2017/7/26.
 */

public interface OnLoginListener {

    void loginSuccess(User user);

    void loginFailure();

}
