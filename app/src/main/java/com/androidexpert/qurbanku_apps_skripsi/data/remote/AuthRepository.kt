package com.androidexpert.qurbanku_apps_skripsi.data.remote

import com.androidexpert.qurbanku_apps_skripsi.data.lib.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

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
                        // Include the UID in the User object
                        val userWithUid = user.copy(uid = uid)

                        // Save the User object with UID to Firestore
                        firestore.collection("user").document(uid)
                            .set(userWithUid)
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
    fun login(email: String, password: String, onResult: (Boolean, User?) -> Unit){
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val user = firebaseAuth.currentUser
                    // Get user data from Firestore and set it in _user LiveData
                    getUserData(user?.uid) { userData ->
                        onResult(true, userData)
                    }
                }else{
                    onResult(false, null)
                }
            }
    }

    fun getUserData(uid: String?, setUser: (User?) -> Unit){
        if (uid != null) {
            firestore.collection("user").document(uid)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject<User>()
                    setUser(user)
                }
                .addOnFailureListener {
                    setUser(null)
                }
        } else {
            setUser(null)
        }
    }
}

