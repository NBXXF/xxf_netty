/*
 * Copyright (C) 2020  即时通讯网(52im.net) & Jack Jiang.
 * The MobileIMSDK v5.x Project.
 * All rights reserved.
 *
 * > Github地址：https://github.com/JackJiang2011/MobileIMSDK
 * > 文档地址：  http://www.52im.net/forum-89-1.html
 * > 技术社区：  http://www.52im.net/
 * > 技术交流群：320837163 (http://www.52im.net/topic-qqgroup.html)
 * > 作者公众号：“【即时通讯技术圈】”，欢迎关注！
 * > 联系作者：  http://www.52im.net/thread-2792-1-1.html
 *
 * "即时通讯网(52im.net) - 即时通讯开发者社区!" 推荐开源工程。
 *
 * Protocal.java at 2020-8-22 16:00:59, code by Jack Jiang.
 */
package com.xxf.netty.protocal;

import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;
import com.xxf.database.xxf.objectbox.converter.MapPropertyConverter;
import com.xxf.database.xxf.objectbox.id.IdUtils;
import com.xxf.netty.extend.enums.MsgState;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.converter.PropertyConverter;

@Entity
public class Protocal {
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
     * Protocal 本身自带属性如下
     * -----------------------------------
     */
    protected boolean bridge = false;
    protected int type = 0;
    protected String dataContent = null;
    protected String from = "-1";
    protected String to = "-1";
    protected String fp = null;
    protected boolean QoS = false;
    protected int typeu = -1;
    protected transient int retryCount = 0;

    public Protocal(int type, String dataContent, String from, String to) {
        this(type, dataContent, from, to, -1);
    }

    public Protocal(int type, String dataContent, String from, String to, int typeu) {
        this(type, dataContent, from, to, false, null, typeu);
    }

    public Protocal(int type, String dataContent, String from, String to
            , boolean QoS, String fingerPrint) {
        this(type, dataContent, from, to, QoS, fingerPrint, -1);
    }

    public Protocal(int type, String dataContent, String from, String to
            , boolean QoS, String fingerPrint, int typeu) {
        this.type = type;
        this.dataContent = dataContent;
        this.from = from;
        this.to = to;
        this.QoS = QoS;
        this.typeu = typeu;

        if (QoS && fingerPrint == null)
            fp = Protocal.genFingerPrint();
        else
            fp = fingerPrint;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long get_id() {
        return IdUtils.generateId(fp);
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

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDataContent() {
        return this.dataContent;
    }

    public void setDataContent(String dataContent) {
        this.dataContent = dataContent;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return this.to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFp() {
        return this.fp;
    }

    public int getRetryCount() {
        return this.retryCount;
    }

    public void increaseRetryCount() {
        this.retryCount += 1;
    }

    public boolean isQoS() {
        return QoS;
    }

    public void setQoS(boolean qoS) {
        this.QoS = qoS;
    }

    public boolean isBridge() {
        return bridge;
    }

    public void setBridge(boolean bridge) {
        this.bridge = bridge;
    }

    public int getTypeu() {
        return typeu;
    }

    public void setTypeu(int typeu) {
        this.typeu = typeu;
    }

    public String toGsonString() {
        return new Gson().toJson(this);
    }

    public byte[] toBytes() {
        return CharsetHelper.getBytes(toGsonString());
    }

    @Override
    public Object clone() {
      /*  Protocal cloneP = new Protocal(this.getType()
                , this.getDataContent(), this.getFrom(), this.getTo(), this.isQoS(), this.getFp());
        cloneP.setBridge(this.bridge); // since 3.0
        cloneP.setTypeu(this.typeu);   // since 3.0
        return cloneP;*/
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(this), Protocal.class);
    }

    /**
     * 获取sessionId
     *
     * @return
     */
    public long getSessionId() {
        return genSessionId(this.from, this.to);
    }

    public static String genFingerPrint() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成 唯一sessionId 无论 ab 正反顺序
     *
     * @param a
     * @param b
     * @return
     */
    public static long genSessionId(String a, String b) {
        return IdUtils.generateId(genSessionIdString(a, b));
    }

    /**
     * 生成 唯一session 标识 无论 ab 正反顺序
     *
     * @param a
     * @param b
     * @return
     */
    public static String genSessionIdString(String a, String b) {
        long aId = IdUtils.generateId(a);
        long bId = IdUtils.generateId(b);
        if (aId >= bId) {
            return (a + b);
        } else {
            return (b + a);
        }
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
