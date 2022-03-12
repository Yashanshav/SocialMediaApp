package com.example.socialmediaapp.daos


import com.example.socialmediaapp.models.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDao {
    private val db = FirebaseFirestore.getInstance()
    private val userCollection = db.collection("users")

    fun addUser(user: User?) {
        user?.let {
            GlobalScope.launch {
                userCollection.document(user.uid).set(it)
            }
        }
    }
}