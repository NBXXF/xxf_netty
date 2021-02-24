package com.xxf.netty.extend.model;

import com.google.gson.Gson;
import com.xxf.database.xxf.objectbox.converter.MapPropertyConverter;
import com.xxf.database.xxf.objectbox.id.IdUtils;
import com.xxf.netty.extend.enums.MsgState;

import net.x52im.mobileimsdk.server.protocal.Protocal;

import java.util.Map;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.converter.PropertyConverter;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 2/22/21 10:39 AM
 * Description: 自定义消息体 http://gitlab.channelthree.net/channel3/channel3-doc/-/wikis/im-api-doc
 * 需要显示声明父类的属性 否则ob 注解不会生成对应的字段,存储不进数据库
 */
@Entity
public class Message extends Protocal {
    @Id(assignable = true)
    private long _id;
    private long timestamp;
    private String fromNickname;
    private String fromAvatar;
    @Convert(converter = MsgStatePropertyConverter.class, dbType = Integer.class)
    private MsgState state;
    @Convert(converter = MapPropertyConverter.class, dbType = String.class)
    private Map<String, Object> extra;
    private boolean isRead;

    /**
     * -----------------------------------
     * Protocal 本身自带属性如下 必须显示声明一边
     * -----------------------------------
     */
    private boolean bridge;
    private int type;
    private String dataContent;
    private String from;
    private String to;
    private String fp;
    private boolean QoS;
    private int typeu;
    private transient int retryCount;

    public Message(int type, String dataContent, String from, String to, String fp, int typeu, long timestamp) {
        super(type, dataContent, from, to, false, fp, typeu);
        this.type = type;
        this.dataContent = dataContent;
        this.from = from;
        this.typeu = typeu;
        this.fp = fp;
        this.to = to;
        this.timestamp = timestamp;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long get_id() {
        return IdUtils.generateId(fp);
    }


    @Override
    public Object clone() {
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(this), Message.class);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getFromNickname() {
        return fromNickname;
    }

    public void setFromNickname(String fromNickname) {
        this.fromNickname = fromNickname;
    }

    public String getFromAvatar() {
        return fromAvatar;
    }

    public void setFromAvatar(String fromAvatar) {
        this.fromAvatar = fromAvatar;
    }

    public MsgState getState() {
        return state;
    }

    public void setState(MsgState state) {
        this.state = state;
    }

    public Map<String, Object> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, Object> extra) {
        this.extra = extra;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    @Override
    public boolean isBridge() {
        return bridge;
    }

    @Override
    public void setBridge(boolean bridge) {
        this.bridge = bridge;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String getDataContent() {
        return dataContent;
    }

    @Override
    public void setDataContent(String dataContent) {
        this.dataContent = dataContent;
    }

    @Override
    public String getFrom() {
        return from;
    }

    @Override
    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public String getTo() {
        return to;
    }

    @Override
    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String getFp() {
        return fp;
    }

    public void setFp(String fp) {
        this.fp = fp;
    }

    @Override
    public boolean isQoS() {
        return QoS;
    }

    @Override
    public void setQoS(boolean qoS) {
        QoS = qoS;
    }

    @Override
    public int getTypeu() {
        return typeu;
    }

    @Override
    public void setTypeu(int typeu) {
        this.typeu = typeu;
    }

    @Override
    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    @Override
    public String toString() {
        return "Message2{" +
                "_id=" + _id +
                ", timestamp=" + timestamp +
                ", fromNickname='" + fromNickname + '\'' +
                ", fromAvatar='" + fromAvatar + '\'' +
                ", state=" + state +
                ", extra=" + extra +
                ", isRead=" + isRead +
                ", bridge=" + bridge +
                ", type=" + type +
                ", dataContent='" + dataContent + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", fp='" + fp + '\'' +
                ", QoS=" + QoS +
                ", typeu=" + typeu +
                ", retryCount=" + retryCount +
                '}';
    }

    static class MsgStatePropertyConverter implements PropertyConverter<MsgState, Integer> {
        @Override
        public MsgState convertToEntityProperty(Integer databaseValue) {
            MsgState[] values = MsgState.values();
            for (int i = 0; i < values.length; i++) {
                if (values[i].getValue() == databaseValue) {
                    return values[i];
                }
            }
            return null;
        }

        @Override
        public Integer convertToDatabaseValue(MsgState entityProperty) {
            return entityProperty.getValue();
        }
    }

}
