package com.example.socialmediaapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediaapp.daos.PostDao
import com.example.socialmediaapp.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity(), IPostAdapter {

    private lateinit var postDao : PostDao
    private lateinit var adapter : PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, CreatePostActivity::class.java)
            startActivity(intent)
        }
        setUpRecyclerView()



    }

    private fun setUpRecyclerView() {

        /*
        We need the query to obtains all the data from postCollection and pass in options.
         */
        postDao = PostDao()
        val postCollection = postDao.postCollection
        val query = postCollection.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()

        // Now the recycler options are pass in adapter to display in firestore recycler view
        adapter = PostAdapter(recyclerOptions, this)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onLikeClicked(postId: String) {
        postDao.updateLikes(postId)
    }

    // create an action bar button
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // handle button activities
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()
        if (id == R.id.signOut) {

            val builder = AlertDialog.Builder(this)

            builder.setMessage(R.string.logOut)
                .setNegativeButton(
                    R.string.yes,
                    DialogInterface.OnClickListener { dialog, id ->
                        FirebaseAuth.getInstance().signOut()
                        val intent = Intent(this, SignInActivity::class.java)
                        startActivity(intent)
                    }
                )
                .setPositiveButton(
                            R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                        dialog.dismiss()
                    })
            builder.show()


        }
        return super.onOptionsItemSelected(item)
    }
}