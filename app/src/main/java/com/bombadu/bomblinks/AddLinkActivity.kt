package com.bombadu.bomblinks

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bombadu.bomblinks.db.LinkData
import com.bombadu.bomblinks.viewModel.LinkViewModel
import com.freesoulapps.preview.android.Preview
import kotlinx.android.synthetic.main.activity_add_link.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Suppress("SENSELESS_COMPARISON")
class AddLinkActivity : AppCompatActivity(), Preview.PreviewListener {

    private lateinit var url: String
    private lateinit var mPreview: Preview
    private lateinit var linkViewModel: LinkViewModel

    companion object {
        private var myDescription = ""
        private var myTitle = ""
        private var myImageUrl = ""
        private var mySource = ""
        private var myDate = ""
        private var myWebUrl = ""

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_link)

        mPreview = findViewById(R.id.preview)
        linkViewModel = LinkViewModel(application)

        val intent = intent
        url = intent.getStringExtra("url_key") ?: ""
        if (url != "") {
            previewLink()
        }




        preview_button.setOnClickListener {

            if (url_edit_text.text.toString().trim().isBlank()) {
                makeAToast("paste url")
                return@setOnClickListener
            }
            url = url_edit_text.text.toString()
            previewLink()

        }
    }

    private fun previewLink() {
        mPreview.setListener(this)
        mPreview.setData(url)
        url_edit_text.text = null
    }

    override fun onDataReady(preview: Preview?) {
        if (preview != null) {
            //preview_view.setData(preview.link)
            mPreview.run {
                setMessage(preview.link)

                myDescription = mPreview.description
                myTitle = mPreview.title
                myImageUrl = mPreview.imageLink
                mySource = mPreview.siteName
                myWebUrl = mPreview.link
                myDate = getDate()
                println("IMAGE URL: $myDate")
            }
        }
    }

    private fun getDate(): String {
        val currentDate = LocalDateTime.now()
        return currentDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_link_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_link -> {
                saveLink()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveLink() {
        val newLink = LinkData(
            myDate,
            myImageUrl,
            myDescription,
            myTitle,
            mySource,
            myWebUrl
        )

        linkViewModel.insertLink(newLink)
        makeAToast("Link Saved")
        finish()

    }

    private fun makeAToast(tMessage: String) {
        Toast.makeText(this, tMessage, Toast.LENGTH_SHORT).show()
    }
}


