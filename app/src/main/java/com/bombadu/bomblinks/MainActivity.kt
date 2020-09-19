package com.bombadu.bomblinks

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bombadu.bomblinks.viewModel.LinkViewModel
import kotlinx.android.synthetic.main.activity_main.*


open class MainActivity : AppCompatActivity() {
    lateinit var linkViewModel: LinkViewModel
    private val adapter = MainAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        getLinkList()
        fetchSharedLink()

        floatingActionButton.setOnClickListener {
            startActivity(Intent(this, AddLinkActivity::class.java))
        }
    }

    private fun fetchSharedLink() {
        val receivedIntent = intent
        val receivedAction = receivedIntent.action
        val receivedType = receivedIntent.type
        if (receivedAction.equals(Intent.ACTION_SEND)){
            if (receivedType != null) {
                if (receivedType.startsWith("text/")) {
                    val receivedUrl = receivedIntent.getStringExtra(Intent.EXTRA_TEXT)
                    intent = Intent(this, AddLinkActivity::class.java)
                    intent.putExtra("url_key", receivedUrl)
                    startActivity(intent)
                }
            }
        }
    }

    private fun getLinkListByCategory(myCategory: String) {

        linkViewModel.getLinksByCategory(myCategory).observe(this,
        Observer { list ->
            list?.let {
                adapter.setLinks(it)
                adapter.notifyDataSetChanged()
            }
        })

    }

    private fun getLinkList() {
        linkViewModel.getAllLinks().observe(this,
        Observer { list ->
            list?.let {
                adapter.setLinks(it)
                adapter.notifyDataSetChanged()
            }
        })
    }

    private fun setupRecyclerView() {
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = adapter
        this.linkViewModel = ViewModelProvider(this).get(LinkViewModel::class.java)
        linkViewModel.getAllLinks().observe(this, Observer { allLinks ->
            allLinks?.let { adapter.setLinks(it) }
        })
        adapter.onItemClick = { pos, view ->
            val myLink = adapter.getLinkAt(pos)
            val myUrl = myLink!!.weburl
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(myUrl))
            startActivity(browserIntent)

        }

        ItemTouchHelper(object :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.getLinkAt(viewHolder.adapterPosition)
                    ?.let { linkViewModel.deleteLink(it) }
                makeAToast("Link Deleted")
            }
        }).attachToRecyclerView(recycler_view)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_all -> {
                val deleteDialog = AlertDialog.Builder(this)
                deleteDialog.setTitle("Delete All Records")
                deleteDialog.setMessage("Are you sure?")
                deleteDialog.setIcon(R.mipmap.ic_launcher)
                deleteDialog.setPositiveButton("delete") { _, _ ->
                    linkViewModel.deleteAllLinks()
                    makeAToast("All Data Deleted")
                }
                deleteDialog.setNegativeButton("cancel") { _, _ ->
                    //Closes Dialog
                }
                deleteDialog.show()

            }
        }

        if (item.itemId == R.id.about) {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("BombLinks v1.0")
            alertDialog.setMessage("by Michael May\nBombadu 2020")
            alertDialog.setIcon(R.mipmap.ic_launcher)
            alertDialog.show()
        }

        when (item.itemId) {
            R.id.category -> {
                showSortDialog()
            }
        }


        when (item.itemId) {
            R.id.action_backup -> {
                Log.d("TEST", "Clicked")
                linkViewModel.backupToFirebase()

            }
        }

        when(item.itemId) {
            R.id.action_restore -> {
                Log.d("TEST", "RESTORE CLICKED")
                linkViewModel.restoreFromFirebase()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSortDialog() {
        val catAdapter = CategoryAdapter()
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.category_layout_dialog)
        dialog.show()



        val recView = dialog.findViewById<RecyclerView>(R.id.category_recycler_view)
        val showAllButton = dialog.findViewById<Button>(R.id.allCategoriesButton)
        showAllButton.setOnClickListener {
            getLinkList()
            dialog.cancel()
        }
        recView.layoutManager = LinearLayoutManager(this)
        recView.setHasFixedSize(true)
        recView.adapter = catAdapter
        this.linkViewModel = ViewModelProvider(this).get(LinkViewModel::class.java)
        linkViewModel.getAllCategories().observe(this, Observer { allCategories ->
            allCategories?.let { catAdapter.setCategories(it) }
        })

        linkViewModel.getAllCategories().observe(this,
            Observer { list ->
                list?.let {
                    catAdapter.setCategories(it)
                    catAdapter.notifyDataSetChanged()
                }
            })

        catAdapter.onItemClick = { pos, _ ->
            val myCategory = catAdapter.getCategoryAt(pos)
            getLinkListByCategory(myCategory!!.category)


            dialog.cancel()
        }


    }




    private fun makeAToast(tMessage: String) {
        Toast.makeText(this, tMessage, Toast.LENGTH_SHORT).show()
    }




}
