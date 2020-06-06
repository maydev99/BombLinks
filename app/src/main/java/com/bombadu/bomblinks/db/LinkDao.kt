package com.bombadu.bomblinks.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LinkDao {
    @Insert
    fun insertLink(linkData: LinkData)

    @Update
    fun updateLink(linkData: LinkData)

    @Delete
    fun deleteLink(linkData: LinkData)

    @Query("DELETE FROM link_table")
    fun deleteAllLinks()

    @Query("SELECT * FROM link_table ORDER BY id ASC")
    fun getAllLinks(): LiveData<List<LinkData>>

    @Query("SELECT DISTINCT category FROM link_table")
    fun getCategories(): LiveData<List<CategoryMinimal>>

    @Query("SELECT * FROM link_table WHERE category = :category")
    fun getLinksByCategory(category: String?): LiveData<List<LinkData>>

}