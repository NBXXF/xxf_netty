package com.xxf.netty.extend;


import com.xxf.netty.extend.enums.MsgState;

import com.xxf.netty.protocal.Protocal;
import com.xxf.netty.protocal.ProtocalType;

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
    public static Protocal createCommonData(String dataContent,
                                            String from_user_id,
                                            String fromNickname,
                                            String fromAvatar,
                                            String to_user_id,
                                            int typeu) {
        String fingerPrint = Protocal.genFingerPrint();
        long timestamp = System.currentTimeMillis();
        Protocal commonProtocal = new Protocal(ProtocalType.C.FROM_CLIENT_TYPE_OF_COMMON$DATA,
                dataContent,
                from_user_id,
                to_user_id,
                false,
                fingerPrint,
                typeu);
        commonProtocal.setTimestamp(timestamp);
        commonProtocal.setFromNickname(fromNickname);
        commonProtocal.setFromAvatar(fromAvatar);
        commonProtocal.setExtra(new HashMap<>());
        commonProtocal.setState(MsgState.sending);
        commonProtocal.setRead(true);
        return commonProtocal;
    }
}
