package com.androidexpert.qurbanku_apps_skripsi.data.remote

import com.androidexpert.qurbanku_apps_skripsi.data.lib.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class UserRepository() {
    private val firestore = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()

    fun logout() {
        firebaseAuth.signOut()
    }

    fun getProfile(uid: String, setUser: (User?) -> Unit) {
        firestore.collection("user").document(uid)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject<User>()
                setUser(user)
            }
            .addOnFailureListener {
                setUser(null)
            }

    }

    fun getMasjidList(setUserList: (List<User>?) -> Unit) {
        firestore.collection("user")
            .whereEqualTo("admin", true)
            .get()
            .addOnSuccessListener { result ->
                val userList = mutableListOf<User>()
                for (document in result) {
                    val user = document.toObject<User>()
                    userList.add(user)
                }
                setUserList(userList)
            }
            .addOnFailureListener {
                setUserList(null)
            }
    }
}