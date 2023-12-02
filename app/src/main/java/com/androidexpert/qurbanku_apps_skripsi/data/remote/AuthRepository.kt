package com.androidexpert.qurbanku_apps_skripsi.data.remote

import com.androidexpert.qurbanku_apps_skripsi.data.remote.lib.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun signUpUser(user: User, password: String, onResult: (Boolean) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(user.email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Registrasi berhasil, simpan data ke Firestore
                    val uid = task.result?.user?.uid
                    if (uid != null) {
                        firestore.collection("user").document(uid)
                            .set(user)
                            .addOnCompleteListener { firestoreTask ->
                                onResult(firestoreTask.isSuccessful)
                            }
                    } else {
                        onResult(false)
                    }
                } else {
                    // Registrasi gagal, panggil callback dengan nilai false
                    onResult(false)
                }
            }
    }
}

