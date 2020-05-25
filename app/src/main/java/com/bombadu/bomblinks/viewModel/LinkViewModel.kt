package com.bombadu.bomblinks.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.bombadu.bomblinks.db.LinkData
import com.bombadu.bomblinks.db.LinkDatabase
import com.bombadu.bomblinks.repository.LinkRepository

class LinkViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LinkRepository
    private val allLinks: LiveData<List<LinkData>>


    init {
        val linkDao = LinkDatabase.getDatabase(application, viewModelScope).linkDao()
        repository = LinkRepository(linkDao)
        allLinks = repository.allLinks

    }

    fun insertLink(linkData: LinkData) {
        repository.insertLink(linkData)
    }

    fun deleteAllLinks() {
        repository.deleteAllLinks()
    }

    fun deleteLink(linkData: LinkData) {
        repository.deleteLink(linkData)
    }

    fun updateLink(linkData: LinkData) {
        repository.updateLink(linkData)
    }

    fun getAllLinks(): LiveData<List<LinkData>> {
        return allLinks
    }
 }