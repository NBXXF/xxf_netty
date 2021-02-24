package com.xxf.netty.extend.service

import com.xxf.database.xxf.objectbox.ObjectBoxFactory
import com.xxf.database.xxf.objectbox.id.IdUtils
import com.xxf.netty.MyObjectBox
import com.xxf.netty.extend.model.Contact
import io.objectbox.BoxStore
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 2/22/21 11:25 AM
 * Description: 联系人service
 */
object ContactService : NettyService {
    private fun getBox(): BoxStore {
        return ObjectBoxFactory.getBoxStore(getApplication(), MyObjectBox.builder(), "chat_contact_${getLoginUserId()}")
    }

    /**
     * 插入联系人  联系人已经存在就更新联系人
     *
     * @param contacts
     */
    fun insertContact(vararg contacts: Contact): Observable<List<Contact>> {
        return Observable.fromCallable<List<Contact>> {
            val toList = contacts.toList();
            getBox()
                    .boxFor(Contact::class.java)
                    .put(toList);
            toList;
        }.subscribeOn(Schedulers.io());
    }

    /**
     * 删除联系人
     *
     * @param contact
     */
    fun deleteContact(contact: Contact): Observable<Boolean> {
        return Observable.fromCallable<Boolean> {
            getBox()
                    .boxFor(Contact::class.java)
                    .remove(contact);
        }.subscribeOn(Schedulers.io());
    }

    /**
     * 查询联系人
     * @return
     */
    fun queryContact(): Observable<List<Contact>> {
        return Observable.fromCallable<List<Contact>> {
            getBox()
                    .boxFor(Contact::class.java)
                    .query()
                    .build()
                    .find();
        }.subscribeOn(Schedulers.io());
    }

    /**
     * 查询联系人
     * 如果不存在 返回 Observable.empty<Contact>() 不能修改
     *
     * @return
     */
    fun queryContact(id: String): Observable<Contact> {
        return Observable.defer<Contact> {
            val findFirst = getBox()
                    .boxFor(Contact::class.java)
                    .get(IdUtils.generateId(id));
            if (findFirst == null) {
                Observable.empty<Contact>()
            } else {
                Observable.just(findFirst);
            }
        }.subscribeOn(Schedulers.io());
    }
}