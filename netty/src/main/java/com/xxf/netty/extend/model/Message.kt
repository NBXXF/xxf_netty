package com.xxf.netty.extend.model

import com.xxf.database.xxf.objectbox.converter.MapPropertyConverter
import com.xxf.database.xxf.objectbox.id.IdUtils
import com.xxf.netty.extend.enums.MsgState
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter
import net.x52im.mobileimsdk.server.protocal.Protocal
import java.io.Serializable

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 2/22/21 10:39 AM
 * Description: 自定义消息体 http://gitlab.channelthree.net/channel3/channel3-doc/-/wikis/im-api-doc
 */
@Entity
class Message : Protocal, Serializable {
    @Id(assignable = true)
    var _id: Long = 0
        get() = IdUtils.generateId(fp);
    var timestamp: Long = 0
    var fromNickname: String? = null
    var fromAvatar: String? = null

    @Convert(converter = MsgStatePropertyConverter::class, dbType = Int::class)
    var state: MsgState = MsgState.success;

    @Convert(converter = MapPropertyConverter::class, dbType = String::class)
    var extra: Map<String, Any>? = null
    var isRead: Boolean = false;

    constructor(type: Int, dataContent: String?, from: String?, to: String?) : super(type, dataContent, from, to) {}
    constructor(type: Int, dataContent: String?, from: String?, to: String?, typeu: Int) : super(type, dataContent, from, to, typeu) {}
    constructor(type: Int, dataContent: String?, from: String?, to: String?, QoS: Boolean, fingerPrint: String?) : super(type, dataContent, from, to, QoS, fingerPrint) {}
    constructor(type: Int, dataContent: String?, from: String?, to: String?, QoS: Boolean, fingerPrint: String?, typeu: Int) : super(type, dataContent, from, to, QoS, fingerPrint, typeu) {}

    override fun clone(): Any {
        val cloneP = Message(getType(), getDataContent(), getFrom(), getTo(), this.isQoS, getFp())
        cloneP.isBridge = bridge
        cloneP.setTypeu(typeu)
        cloneP.fromAvatar = fromAvatar
        cloneP.fromNickname = fromNickname
        cloneP.timestamp = timestamp
        cloneP.extra = extra
        cloneP.state = state;
        cloneP.isRead = isRead;
        return cloneP
    }

    internal class MsgStatePropertyConverter : PropertyConverter<MsgState, Int> {
        override fun convertToEntityProperty(databaseValue: Int): MsgState? {
            return MsgState.values().firstOrNull { it.value == databaseValue }
        }

        override fun convertToDatabaseValue(entityProperty: MsgState): Int {
            return entityProperty.value;
        }

    }

    override fun toString(): String {
        return "Message{" +
                "timestamp=" + timestamp +
                ", fromNickname='" + fromNickname + '\'' +
                ", fromAvatar='" + fromAvatar + '\'' +
                ", extra=" + extra +
                ", bridge=" + bridge +
                ", type=" + type +
                ", isRead=" + isRead +
                ", state=" + state +
                ", dataContent='" + dataContent + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", fp='" + fp + '\'' +
                ", QoS=" + QoS +
                ", typeu=" + typeu +
                ", retryCount=" + retryCount +
                '}'
    }
}