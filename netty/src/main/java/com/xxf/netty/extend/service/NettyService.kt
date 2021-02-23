package com.xxf.netty.extend.service

import android.app.Application
import com.xxf.netty.ClientCoreSDK

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 2/21/21 2:50 PM
 * Description: im服务约束
 */
interface NettyService {

    fun getApplication(): Application {
        return ClientCoreSDK.getInstance().context.applicationContext as Application;
    }

    fun getLoginUserId(): String {
        return ClientCoreSDK.getInstance().currentLoginUserId;
    }
}