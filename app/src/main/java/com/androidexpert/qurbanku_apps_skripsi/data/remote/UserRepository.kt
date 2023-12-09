package com.androidexpert.qurbanku_apps_skripsi.data.remote

import com.androidexpert.qurbanku_apps_skripsi.data.lib.Animal
import com.androidexpert.qurbanku_apps_skripsi.data.lib.MasjidUser
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

    fun getMasjidListWithAnimals(setMasjidUserList: (List<MasjidUser>?) -> Unit) {
        firestore.collection("user")
            .whereEqualTo("admin", true)
            .get()
            .addOnSuccessListener { result ->
                val masjidUserList = mutableListOf<MasjidUser>()
                for (document in result) {
                    val user = document.toObject<User>()
                    // Ambil data hewan untuk setiap masjid
                    getAnimalList(user.uid) { animalList ->
                        val masjidUser = MasjidUser(user, animalList)
                        masjidUserList.add(masjidUser)
                        // Kembalikan data jika sudah mendapatkan semua data
                        if (masjidUserList.size == result.size()) {
                            setMasjidUserList(masjidUserList)
                        }
                    }
                }
            }
            .addOnFailureListener {
                setMasjidUserList(null)
            }
    }

    fun getAnimalList(idMasjid: String?, setAnimalList: (List<Animal>?) -> Unit) {
        firestore.collection("animal")
            .whereEqualTo("idMasjid", idMasjid!!)
            .get()
            .addOnSuccessListener { result ->
                val animalList = mutableListOf<Animal>()
                for (document in result) {
                    val animal = document.toObject<Animal>()
                    animalList.add(animal)
                }
                setAnimalList(animalList)
            }
            .addOnFailureListener {
                setAnimalList(null)
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