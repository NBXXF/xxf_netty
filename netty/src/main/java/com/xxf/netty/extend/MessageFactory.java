package com.xxf.netty.extend;


import com.xxf.netty.extend.model.Message;
import com.xxf.netty.extend.enums.MsgState;

import net.x52im.mobileimsdk.server.protocal.ProtocalType;

import java.util.HashMap;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 2/22/21 11:04 AM
 * Description:  自定义消息体工厂
 */
public class MessageFactory {


    /**
     * 创建自定义常见消息 结构
     *
     * @param dataContent  内容
     * @param from_user_id 发送方的uid
     * @param fromNickname 发送方的昵称
     * @param fromAvatar   发送方的头像
     * @param to_user_id   接收方/对方的uid
     * @param typeu        业务类型类型 1文本 2图片，3视频
     * @return
     */
    public static Message createCommonData(String dataContent,
                                           String from_user_id,
                                           String fromNickname,
                                           String fromAvatar,
                                           String to_user_id,
                                           int typeu) {
        boolean QoS = false;
        String fingerPrint = Message.genFingerPrint();
        long timestamp = System.currentTimeMillis();
        Message commonProtocal = new Message(ProtocalType.C.FROM_CLIENT_TYPE_OF_COMMON$DATA, dataContent, from_user_id, to_user_id, QoS, fingerPrint, typeu);
        commonProtocal.setFromNickname(fromNickname);
        commonProtocal.setFromAvatar(fromAvatar);
        commonProtocal.setTimestamp(timestamp);
        commonProtocal.setExtra(new HashMap<>());
        commonProtocal.setState(MsgState.sending);
        commonProtocal.setRead(true);
        return commonProtocal;
    }
}
