package com.xxf.netty.extend.model

import com.xxf.database.xxf.objectbox.converter.MapPropertyConverter
import com.xxf.database.xxf.objectbox.id.IdUtils
import com.xxf.netty.extend.enums.ContactType
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter
import java.io.Serializable

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 2/22/21 11:27 AM
 * Description: 联系人 包括 个人和群组
 */
@Entity
class Contact : Serializable {

    @Id(assignable = true)
    var _id: Long = 0
        get() = IdUtils.generateId(id);

    @Convert(converter = ContactTypePropertyConverter::class, dbType = Int::class)
    var type: ContactType = ContactType.TYPE_USER;
    var id: String? = null
    var name: String? = null
    var avatar: String? = null

    @Convert(converter = MapPropertyConverter::class, dbType = String::class)
    var extra: Map<String, Any>? = null
    override fun toString(): String {
        return "Contact(type=$type, id=$id, name=$name, avatar=$avatar, extra=$extra)"
    }

    internal class ContactTypePropertyConverter : PropertyConverter<ContactType, Int> {
        override fun convertToEntityProperty(databaseValue: Int?): ContactType? {
            return ContactType.values().firstOrNull {
                it.value == databaseValue
            }
        }

        override fun convertToDatabaseValue(entityProperty: ContactType?): Int {
            return entityProperty!!.value;
        }

    }
}