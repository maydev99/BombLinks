package com.bombadu.bomblinks


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import com.bombadu.bomblinks.db.LinkData
import com.bombadu.bomblinks.viewModel.LinkViewModel
import com.freesoulapps.preview.android.Preview
import kotlinx.android.synthetic.main.activity_add_link.*
import kotlinx.android.synthetic.main.category_item.*
import org.nibor.autolink.LinkExtractor
import org.nibor.autolink.LinkType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*


@Suppress("SENSELESS_COMPARISON", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AddLinkActivity : AppCompatActivity(), Preview.PreviewListener {

    private lateinit var url: String
    private lateinit var mPreview: Preview
    private lateinit var linkViewModel: LinkViewModel
    private val thingList = mutableListOf<String>()
    private val catList = mutableListOf<String>()



    companion object {
        private var myDescription = ""
        private var myTitle = ""
        private var myImageUrl = ""
        private var mySource = ""
        private var myDate = ""
        private var myWebUrl = ""
        private var myCategory = ""
        private const val ADD_QR_SCAN = 1


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_link)

        mPreview = findViewById(R.id.preview_view)
        linkViewModel = LinkViewModel(application)


        val intent = intent
        url = intent.getStringExtra("url_key") ?: ""
        if (url != "") {
            prepPreview()
            previewLink()
        }




        preview_button.setOnClickListener {

            if (url_edit_text.text.toString().trim().isBlank()) {
                makeAToast("paste url")
                return@setOnClickListener
            }
            url = url_edit_text.text.toString()

            prepPreview()
            previewLink()

        }


    }

    private fun getMovieList() {


    }





    private fun prepPreview(){
        preview_placeholder_text_view.visibility = View.INVISIBLE
        mPreview.visibility = View.VISIBLE
    }

    private fun previewLink() {
        preview_placeholder_text_view.visibility = View.INVISIBLE
        mPreview.visibility = View.VISIBLE
        mPreview.setListener(this)
        mPreview.setData(extractUrl(url)) //Extract Url in correct format prior to preview

        url_edit_text.text = null
    }

    private fun extractUrl(myUrl: String): String {
        val linkExtractor = LinkExtractor.builder()
            .linkTypes(EnumSet.of(LinkType.URL, LinkType.WWW, LinkType.EMAIL))
            .build()

        val links = linkExtractor.extractLinks(myUrl)
        val link = links.iterator().next()
        link.type
        link.beginIndex
        link.endIndex
        return myUrl.substring(link.beginIndex, link.endIndex)

    }

    override fun onDataReady(preview: Preview?) {



        mPreview.run {

            try {
                if (mPreview != null) {
                    mPreview.setMessage(preview!!.link)
                    if(description != null && myTitle != null && myImageUrl
                        != null && mySource != null && myWebUrl != null) {
                        myDescription = mPreview.description
                        myTitle = preview.title
                        myImageUrl = preview.imageLink
                        mySource = preview.siteName
                        myWebUrl = preview.link
                        myDate = getDate()
                        //myCategory = categoryACTextView.text.toString() get this on saveLink

                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }

        println("NEW: $myImageUrl")
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

        when (item.itemId) {
            R.id.qr_reader -> {
                startActivityForResult(
                    Intent(this, QrScannerActivity::class.java),
                    ADD_QR_SCAN
                )
            }
        }

        when (item.itemId) {
            R.id.categorize -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_QR_SCAN && resultCode == Activity.RESULT_OK && data != null) {
            url = data.getStringExtra(QrScannerActivity.EXTRA_RESULT)
            if (!url.contains("http") || !url.contains("https")) {
                makeAToast("QR code does not contain link")
            } else {
                prepPreview()
                previewLink()
            }

        } else {
            makeAToast("QR not scanned")
        }
    }


    private fun saveLink() {
        myCategory = categoryACTextView.text.toString()
        val newLink = LinkData(
            myDate,
            myImageUrl,
            myDescription,
            myTitle,
            mySource,
            myWebUrl,
            myCategory
        )

        linkViewModel.insertLink(newLink)

        makeAToast("Link Saved")
        finish()

    }

    private fun makeAToast(tMessage: String) {
        Toast.makeText(this, tMessage, Toast.LENGTH_SHORT).show()
    }


}




