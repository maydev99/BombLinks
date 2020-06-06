package com.bombadu.bomblinks.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "link_table")
class LinkData(
    @ColumnInfo(name = "date") var date:String,
    @ColumnInfo(name = "image_url") var imageurl:String,
    @ColumnInfo(name = "description") var description:String,
    @ColumnInfo(name = "title") var title:String,
    @ColumnInfo(name = "source") var source: String,
    @ColumnInfo(name = "web_url") var weburl: String,
    @ColumnInfo(name = "category") var category: String

) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0



}

data class CategoryMinimal(val category: String)
