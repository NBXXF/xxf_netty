package com.xxf.netty.extend.enums

import com.google.gson.annotations.SerializedName

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 2/23/21 12:06 PM
 * Description: 消息状态
 */
enum class MsgState(val value: Int) {
    /**
     * 草稿
     */
    @SerializedName("-3")
    draft(-3),

    /**
     * 正在发送中
     */
    @SerializedName("-2")
    sending(-2),

    /**
     * 发送失败
     */
    @SerializedName("-1")
    fail(-1),

    /**
     * 发送成功
     */
    @SerializedName("0")
    success(0)
}