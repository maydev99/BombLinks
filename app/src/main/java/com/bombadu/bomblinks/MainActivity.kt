package com.bombadu.bomblinks

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bombadu.bomblinks.db.LinkData
import com.bombadu.bomblinks.viewModel.LinkViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var linkViewModel: LinkViewModel
    private val adapter = MainAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        getMovieList()


        floatingActionButton.setOnClickListener {
            startActivity(Intent(this, AddLinkActivity::class.java))
        }
    }

    private fun getMovieList() {
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
            val myLink = adapter.getMovieAt(pos)
            val myUrl = myLink!!.weburl
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(myUrl))
            startActivity(browserIntent)

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_all) {
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
        return super.onOptionsItemSelected(item)
    }

    private fun makeAToast(tMessage: String) {
        Toast.makeText(this, tMessage, Toast.LENGTH_SHORT).show()
    }


}
