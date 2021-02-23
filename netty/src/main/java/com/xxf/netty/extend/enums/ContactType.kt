package com.xxf.netty.extend.enums

import com.google.gson.annotations.SerializedName

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 2/23/21 12:06 PM
 * Description: 消息状态
 */
enum class ContactType(val value: Int) {
    @SerializedName("0")
    TYPE_USER(0),
    @SerializedName("1")
    TYPE_TEAM(1)
}