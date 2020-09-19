package com.bombadu.bomblinks.firebase

import androidx.lifecycle.LiveData
import com.bombadu.bomblinks.MainActivity
import com.bombadu.bomblinks.db.LinkData
import com.google.firebase.database.FirebaseDatabase

class BackupData(private val myData: LiveData<List<LinkData>>) : MainActivity() {
    private var rootRef = FirebaseDatabase.getInstance().reference
    private var fbDataRef = rootRef.child("data_ref")

    fun getRoomDataAddToFB() {

        val links = myData.value
        fbDataRef.removeValue()

        for (i in links!!.indices) {
            val taskMap: MutableMap<String, Any> = HashMap()
            taskMap["image_url"] = links[i].imageurl
            taskMap["date"] = links[i].date
            taskMap["description"] = links[i].description
            taskMap["title"] = links[i].title
            taskMap["source"] = links[i].source
            taskMap["web_url"] = links[i].weburl
            taskMap["category"] = links[i].category
            taskMap["id"] = links[i].id

            fbDataRef.push().updateChildren(taskMap).addOnCompleteListener {

            }

        }

    }

}

