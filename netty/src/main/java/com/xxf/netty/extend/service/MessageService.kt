package com.xxf.netty.extend.service

import android.text.TextUtils
import com.xxf.database.xxf.objectbox.ObjectBoxFactory
import com.xxf.netty.core.LocalDataSender
import com.xxf.netty.extend.enums.MsgDirection
import com.xxf.netty.extend.enums.MsgState
import com.xxf.netty.extend.exceptions.MessageException
import com.xxf.netty.extend.model.*
import io.objectbox.BoxStore
import io.objectbox.annotation.apihint.Internal
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers
import net.x52im.mobileimsdk.server.protocal.ErrorCode

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 2/22/21 11:54 AM
 * Description: 消息服务
 */
object MessageService : NettyService {

    private fun getBox(from: String, to: String): BoxStore {
        return if (TextUtils.equals(from, getLoginUserId())) {
            ObjectBoxFactory.getBoxStore(getApplication(), MyObjectBox.builder(), "chat_msg_${from}_${to}")
        } else {
            ObjectBoxFactory.getBoxStore(getApplication(), MyObjectBox.builder(), "chat_msg_${to}_${from}")
        }
    }

    /**
     * 发送消息
     * 1. 插入本地
     * 2. 更新session
     *
     * @param message [Message]
     */
    fun sendMessage(message: Message): Observable<Message> {
        return insertMessage(message)
                .doOnSubscribe {
                    message.state = MsgState.sending;
                }
                .map { it.get(0) }
                .flatMap {
                    val code = LocalDataSender.getInstance().sendCommonData(message)
                    if (code != ErrorCode.COMMON_CODE_OK) {
                        throw MessageException(code, "send fail");
                    }
                    Observable.just(message);
                }.onErrorResumeNext {
                    message.state = MsgState.fail;
                    updateMessage(message)
                }.flatMap {
                    message.state = MsgState.success;
                    updateMessage(message)
                }
    }

    /**
     * 更新消息
     *
     * @param message
     */
    fun updateMessage(message: Message): Observable<Message> {
        return insertMessage(message)
                .map { it.get(0) }
    }

    /**
     * 插入消息  消息已经存在就更新消息
     *
     * @param messages
     */
    fun insertMessage(vararg messages: Message): @NonNull Observable<List<Message>> {
        return Observable
                .fromCallable<List<Message>> {
                    val first = messages.get(0);
                    val toList = messages.toList();
                    getBox(first.from, first.to)
                            .boxFor(Message::class.java)
                            .put(toList);
                    toList;
                }
                .subscribeOn(Schedulers.io())
                .flatMap { list ->
                    updateSession(list.get(0))
                            .map {
                                list
                            }
                };
    }

    /**
     * 获取消息方向
     */
    fun getMessageDirection(message: Message): MsgDirection {
        return if (TextUtils.equals(message.from, MessageService.getLoginUserId())) MsgDirection.OUT else MsgDirection.IN;
    }

    /**
     * 获取对方的id 相对于登陆者
     */
    fun getTargetContactId(message: Message): String {
        if (getMessageDirection(message) == MsgDirection.IN) {
            return message.from;
        } else {
            return message.to;
        }
    }

    /**
     * 更新session快照
     */
    private fun updateSession(message: Message): Observable<Session> {
        val target: Message = message.clone() as Message;
        target.timestamp = System.currentTimeMillis();
        return Observable.zip(
                ContactService.queryContact(getTargetContactId(message))
                        .switchIfEmpty {
                            /**
                             * 第一次本地没有联系人
                             */
                            val contact = Contact();
                            contact.id = message.from;
                            contact.name = message.fromNickname;
                            contact.avatar = message.fromAvatar;
                            ContactService.insertContact(contact)
                        },
                /**
                 * 获取最后一条消息
                 */
                queryMessage(getTargetContactId(message), System.currentTimeMillis(), 1)
                        .map {
                            it.get(0)
                        },
                BiFunction<Contact, Message, Session> { t1, t2 ->
                    val session = Session()
                    session.target = t1;
                    session.message = t2;
                    session.timestamp = t2.timestamp;
                    session;
                })
                .flatMap {
                    SessionService.insertSession(it)
                }
    }

    /**
     * 删除消息
     *
     * @param message
     */
    fun deleteMessage(message: Message): Observable<Boolean> {
        return Observable.fromCallable<Boolean> {
            getBox(message.from, message.to)
                    .boxFor(Message::class.java)
                    .remove(message);
        }.subscribeOn(Schedulers.io());
    }

    /**
     * 查询消息
     * 默认倒序
     * @param
     * @return
     */
    fun queryMessage(contactId: String, startTime: Long, limit: Long): Observable<List<Message>> {
        return Observable.fromCallable<List<Message>> {
            getBox(getLoginUserId(), contactId)
                    .boxFor(Message::class.java)
                    .query()
                    .lessOrEqual(Message_.timestamp, startTime)
                    .orderDesc(Message_.timestamp)
                    .build()
                    .find(0, limit)
        }.subscribeOn(Schedulers.io());
    }

    /**
     * 查询未读消息数量
     */
    fun queryUnReadNum(target: Message): Observable<Long> {
        return Observable.fromCallable<Long> {
            getBox(target.from, target.to)
                    .boxFor(Message::class.java)
                    .query()
                    .equal(Message_.isRead, false)
                    .build()
                    .count();
        }.subscribeOn(Schedulers.io());
    }

    /**
     * 清除所有未读消息
     */
    @Internal
    internal fun clearUnreadCount(target: Message): Observable<Boolean> {
        return Observable.fromCallable<Boolean> {
            val find = getBox(target.from, target.to)
                    .boxFor(Message::class.java)
                    .query()
                    .equal(Message_.isRead, false)
                    .build()
                    .find()
            if (find != null) {
                find.forEach {
                    it.isRead = true;
                }
                getBox(target.from, target.to)
                        .boxFor(Message::class.java)
                        .put(find);
            }
            true;
        }.subscribeOn(Schedulers.io());
    }
}