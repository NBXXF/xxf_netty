package com.xxf.netty.extend.service

import com.xxf.database.xxf.objectbox.ObjectBoxFactory
import com.xxf.database.xxf.objectbox.id.IdUtils
import com.xxf.netty.extend.model.*
import io.objectbox.BoxStore
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 2/22/21 11:24 AM
 * Description: 会话Service
 */
object SessionService : NettyService {

    internal var currentContactId: String? = null;

    private fun getBox(): BoxStore {
        return ObjectBoxFactory.getBoxStore(getApplication(), MyObjectBox.builder(), "chat_session_${getLoginUserId()}")
    }

    /**
     * 插入或者更新session
     *  从消息做备份 提高速度
     */
    fun insertSession(session: Session): Observable<Session> {
        return Observable.fromCallable<Session> {
            getBox().boxFor(Session::class.java)
                    .put(session);
            session
        }.subscribeOn(Schedulers.io());
    }

    /**
     * 查询所有会话
     */
    fun querySession(): Observable<List<Session>> {
        return Observable.fromCallable<List<Session>> {
            getBox().boxFor(Session::class.java)
                    .query()
                    .orderDesc(Session_.timestamp)
                    .build()
                    .find();
        }.subscribeOn(Schedulers.io());
    }

    /**
     * 清除指定session 的未读数量
     */
    fun clearUnreadCount(session: Session): Observable<Boolean> {
        return clearUnreadCount(session.target!!.id!!)
    }

    /**
     * 清除指定session 的未读数量
     */
    fun clearUnreadCount(contactId: String): Observable<Boolean> {
        return Observable.fromCallable<Boolean> {
            val find = getBox().boxFor(Session::class.java)
                    .query()
                    .equal(Session_._id, IdUtils.generateId(contactId))
                    .build()
                    .findFirst();
            if (find != null) {
                find.unReadNum = 0;
                find.message?.let { MessageService.clearUnreadCount(it).onErrorReturnItem(true).blockingFirst() };
                getBox().boxFor(Session::class.java)
                        .put(find)
            }
            true;
        }.subscribeOn(Schedulers.io());
    }

    /**
     * 清除所有session的未读数量
     */
    fun clearAllUnreadCount(): Observable<Boolean> {
        return Observable.fromCallable<Boolean> {
            val find = getBox().boxFor(Session::class.java)
                    .query()
                    .orderDesc(Session_.timestamp)
                    .greater(Session_.unReadNum, 0)
                    .build()
                    .find()
            if (!find.isNullOrEmpty()) {
                find.forEach {
                    it.message?.let { it1 -> MessageService.clearUnreadCount(it1).onErrorReturnItem(true).blockingFirst() };
                }
                getBox().boxFor(Session::class.java).put(find);
            }
            true;
        }.subscribeOn(Schedulers.io());
    }

    /**
     * 查询所有消息未读数量
     */
    fun getAllUnreadCount(): Observable<Long> {
        return querySession()
                .map { sessons ->
                    var num: Long = 0;
                    sessons.forEach {
                        num += it.unReadNum;
                    }
                    num;
                }
    }

    /**
     * 对方id
     * 设置正在进行聊天对象
     * 一个act,frag onresume setChattingAccount("xxx")
     * onPause setChattingAccount(null)
     */
    fun setChattingAccount(contactId: String?) {
        this.currentContactId = contactId;
    }

}