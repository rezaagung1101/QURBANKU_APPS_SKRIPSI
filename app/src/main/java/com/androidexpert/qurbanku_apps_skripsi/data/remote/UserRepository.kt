package com.androidexpert.qurbanku_apps_skripsi.data.remote

import com.androidexpert.qurbanku_apps_skripsi.data.lib.User
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
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

    fun getJemaahList(idJemaah: List<String>, setUserList: (List<User>?) -> Unit) {
        val tasks = mutableListOf<Task<DocumentSnapshot>>()
        idJemaah.forEach { id ->
            val task = firestore.collection("user").document(id).get()
            tasks.add(task)
        }
        Tasks.whenAllSuccess<DocumentSnapshot>(tasks)
            .addOnSuccessListener { snapshots ->
                val userList = snapshots.mapNotNull { it.toObject<User>() }
                setUserList(userList)
            }
            .addOnFailureListener {
                setUserList(null)
            }
    }
}