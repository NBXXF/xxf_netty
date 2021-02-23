package com.xxf.netty.extend.exceptions

import java.lang.RuntimeException

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 2/23/21 3:18 PM
 * Description: 消息异常
 */
class MessageException(val code: Int, msg: String) : RuntimeException("${code}:${msg}") {
}