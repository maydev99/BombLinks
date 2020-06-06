package com.bombadu.bomblinks.repository

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.bombadu.bomblinks.db.CategoryMinimal

import com.bombadu.bomblinks.db.LinkDao
import com.bombadu.bomblinks.db.LinkData

class LinkRepository(private val linkDao: LinkDao) {
    val allLinks: LiveData<List<LinkData>> = linkDao.getAllLinks()
    val allCategories: LiveData<List<CategoryMinimal>> = linkDao.getCategories()
    //val allLinksByCategory: LiveData<List<LinkData>> = linkDao.getLinksByCategory(category)

    /*fun allLinksByCategory(category: String) : LiveData<List<LinkData>> {
        return linkDao.getLinksByCategory(category)
    }*/

    fun insertLink(linkData: LinkData) {
        InsertLinkAsyncTask(
            linkDao
        ).execute(linkData)

    }

    fun deleteAllLinks() {
        DeleteAllLinksAsyncTask(
            linkDao
        ).execute()

    }

    fun deleteLink(linkData: LinkData){
        DeleteLinkAsyncTask(
            linkDao
        ).execute(linkData)
    }

    fun updateLink(linkData: LinkData) {
        UpdateLinkAsyncTask(
            linkDao
        ).execute(linkData)
    }

    fun allLinksByCategory(category: String): LiveData<List<LinkData>> {
        return linkDao.getLinksByCategory(category)
    }

    private class InsertLinkAsyncTask(val linkDao: LinkDao) : AsyncTask<LinkData, Unit, Unit>(){
        override fun doInBackground(vararg linkData: LinkData?) {
            linkDao.insertLink(linkData[0]!!)
        }

    }


    private class DeleteAllLinksAsyncTask(val linkDao: LinkDao) : AsyncTask<Unit, Unit, Unit>(){
        override fun doInBackground(vararg p0: Unit) {
            linkDao.deleteAllLinks()
        }

    }

    private class DeleteLinkAsyncTask(val linkDao: LinkDao) : AsyncTask<LinkData, Unit, Unit>(){
        override fun doInBackground(vararg linkData: LinkData?) {
            linkDao.deleteLink(linkData[0]!!)
        }

    }

    private class UpdateLinkAsyncTask(val linkDao: LinkDao) : AsyncTask<LinkData, Unit, Unit>(){
        override fun doInBackground(vararg linkData: LinkData?) {
            linkDao.updateLink(linkData[0]!!)
        }

    }
}