package com.bombadu.bomblinks


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bombadu.bomblinks.db.LinkData
import com.bombadu.bomblinks.util.Utils
import com.bombadu.bomblinks.viewModel.LinkViewModel
import com.freesoulapps.preview.android.Preview
import kotlinx.android.synthetic.main.activity_add_link.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


@Suppress("SENSELESS_COMPARISON", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AddLinkActivity : AppCompatActivity(), Preview.PreviewListener {

    private lateinit var url: String
    private lateinit var mPreview: Preview
    private lateinit var linkViewModel: LinkViewModel
    private val catList = mutableListOf<String>()


    companion object {
        private var myDescription = ""
        private var myTitle = ""
        private var myImageUrl = ""
        private var mySource = ""
        private var myDate = ""
        private var myWebUrl = ""
        private var myCategory = "No Category"
        private const val ADD_QR_SCAN = 1
        private const val TAG = "MyTag"


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_link)

        mPreview = findViewById(R.id.preview_view)
        linkViewModel = LinkViewModel(application)


        makeCategoryList()


        val intent = intent
        url = intent.getStringExtra("url_key") ?: ""
        if (url != "") {
            prepPreview()
            previewLink()
        }




        preview_button.setOnClickListener {

            if (url_edit_text.text.toString().trim().isBlank()) {
                makeAToast("paste url")
                Log.d(TAG, "MyPreview: TheButton")
                return@setOnClickListener
            }
            url = url_edit_text.text.toString()

            prepPreview()
            previewLink()

        }


    }


    private fun prepPreview() {
        preview_placeholder_text_view.visibility = View.INVISIBLE
        mPreview.visibility = View.VISIBLE
    }

    private fun previewLink() {
        val utils = Utils()
        mPreview.setListener(this)
        mPreview.setData(utils.extractUrl(url)) //Extract Url in correct format prior to preview

        url_edit_text.text = null
    }


    override fun onDataReady(preview: Preview?) {


        mPreview.run {

            try {
                if (mPreview != null) {
                    mPreview.setMessage(preview!!.link)
                    if (description != null && myTitle != null && myImageUrl
                        != null && mySource != null && myWebUrl != null
                    ) {
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

    private fun makeCategoryList() {
        this.linkViewModel = ViewModelProvider(this).get(LinkViewModel::class.java)
        linkViewModel.getAllCategories().observe(this,
            Observer { list ->
                list?.let {
                    for (value in it) {
                        catList.add(value.category)
                    }

                    val catAdapter =
                        ArrayAdapter(this, android.R.layout.select_dialog_item, catList)

                    val categoryTextView =
                        findViewById<AutoCompleteTextView>(R.id.categoryACTextView)
                    categoryTextView.threshold = 1
                    categoryTextView.setAdapter(catAdapter)
                }
            })


    }

}






