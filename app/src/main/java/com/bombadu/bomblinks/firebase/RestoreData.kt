package com.bombadu.bomblinks.firebase

import android.app.Application
import androidx.lifecycle.*
import com.bombadu.bomblinks.db.CategoryMinimal
import com.bombadu.bomblinks.db.LinkData
import com.bombadu.bomblinks.db.LinkDatabase
import com.bombadu.bomblinks.repository.LinkRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RestoreData(application: Application) : AndroidViewModel(application), ViewModelStoreOwner {
    private var rootRef = FirebaseDatabase.getInstance().reference
    private var fbDataRef = rootRef.child("data_ref")

    private val repository: LinkRepository
    private val allLinks: LiveData<List<LinkData>>
    private val allCategories: LiveData<List<CategoryMinimal>>



    init {
        val linkDao = LinkDatabase.getDatabase(application, viewModelScope).linkDao()
        repository = LinkRepository(linkDao)
        allLinks = repository.allLinks
        allCategories = repository.allCategories
    }

    fun getDataFromFBInsertIntoRoom() {
        val keyList = mutableListOf<String>()

        val myListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (i in snapshot.children) {
                    keyList.add(i.key.toString())

                }

                for (i in 0 until keyList.size) {
                    val imageUrl = snapshot.child(keyList[i]).child("image_url").value.toString()
                    val date = snapshot.child(keyList[i]).child("date").value.toString()
                    val description = snapshot.child(keyList[i]).child("description").value.toString()
                    val title = snapshot.child(keyList[i]).child("title").value.toString()
                    val source = snapshot.child(keyList[i]).child("source").value.toString()
                    val webUrl = snapshot.child(keyList[i]).child("web_url").value.toString()
                    val category = snapshot.child(keyList[i]).child("category").value.toString()

                    val restoreData = LinkData(date, imageUrl, description, title, source, webUrl, category)
                    repository.insertLink(restoreData)

                }

            }

            override fun onCancelled(error: DatabaseError) {
               //Nothing
            }

        }

        fbDataRef.addListenerForSingleValueEvent(myListener)

    }

    override fun getViewModelStore(): ViewModelStore {
        TODO("Not yet implemented")
    }


}