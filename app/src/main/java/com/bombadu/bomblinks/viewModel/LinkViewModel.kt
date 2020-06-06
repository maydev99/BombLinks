package com.bombadu.bomblinks.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.bombadu.bomblinks.db.CategoryMinimal
import com.bombadu.bomblinks.db.LinkData
import com.bombadu.bomblinks.db.LinkDatabase
import com.bombadu.bomblinks.repository.LinkRepository

class LinkViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LinkRepository
    private val allLinks: LiveData<List<LinkData>>
    private val allCategories: LiveData<List<CategoryMinimal>>
    //var query = MutableLiveData<String>()
    private val linksByCategory: LiveData<List<LinkData>>
    private val mCategory = ""
    //private fun temp(query: String) = repository.allLinksByCategory(query)





    init {
        val linkDao = LinkDatabase.getDatabase(application, viewModelScope).linkDao()
        repository = LinkRepository(linkDao)
        allLinks = repository.allLinks
        allCategories = repository.allCategories
        linksByCategory = repository.allLinksByCategory(mCategory)
        Log.i("CATEGORY", "$mCategory")

    }



    fun insertLink(linkData: LinkData) {
        repository.insertLink(linkData)
    }

    fun deleteAllLinks() {
        repository.deleteAllLinks()
    }

    fun deleteLink(linkData: LinkData) {
        repository.deleteLink(linkData)
        repository.deleteLink(linkData)
    }

    fun updateLink(linkData: LinkData) {
        repository.updateLink(linkData)
    }

    fun getAllLinks(): LiveData<List<LinkData>> {
        return allLinks
    }

    fun getAllCategories(): LiveData<List<CategoryMinimal>> {
        return allCategories
    }

    fun getLinksByCategory(myCategory: String): LiveData<List<LinkData>> {
        return linksByCategory
    }

}