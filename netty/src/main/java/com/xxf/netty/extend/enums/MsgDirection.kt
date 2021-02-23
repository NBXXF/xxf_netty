package com.xxf.netty.extend.enums

import com.google.gson.annotations.SerializedName

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 2/23/21 12:06 PM
 * Description: 消息相对于登陆者的方向
 */
enum class MsgDirection(val value: Int) {
    @SerializedName("0")
    IN(0),

    @SerializedName("1")
    OUT(1);
}