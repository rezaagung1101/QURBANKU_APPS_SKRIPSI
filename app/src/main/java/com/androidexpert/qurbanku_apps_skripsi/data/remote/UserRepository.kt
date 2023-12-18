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

    fun getMasjidList(setMasjidUserList: (List<MasjidUser>?) -> Unit) {
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

    fun updatePanitiaProfile(
        uid: String,
        name: String,
        headName: String,
        phoneNumber: String,
        latitude: Double,
        longitude: Double,
        accountNumber: String,
        bankName: String,
        accountName: String,
        onResult: (Boolean, User?) -> Unit,
    ) {
        val documentReference = firestore.collection("user").document(uid)
        // Create a map with the fields to be updated
        val updates = mapOf(
            "name" to name,
            "headName" to headName,
            "phoneNumber" to phoneNumber,
            "latitude" to latitude,
            "longitude" to longitude,
            "accountNumber" to accountNumber,
            "bankName" to bankName,
            "accountName" to accountName
        )
        // Update the document with the new data
        documentReference
            .update(updates)
            .addOnSuccessListener {
                // After successful update, retrieve the updated user data
                getProfile(uid) { user ->
                    onResult(true, user)
                }
            }
            .addOnFailureListener {
                onResult(false, null)
            }

    }

    fun updateJemaahProfile(
        uid: String,
        phoneNumber: String,
        name: String,
        address: String,
        headName: String,
        onResult: (Boolean, User?) -> Unit,
    ) {
        val documentReference = firestore.collection("user").document(uid)
        // Create a map with the fields to be updated
        val updates = mapOf(
            "phoneNumber" to phoneNumber,
            "name" to name,
            "address" to address,
            "headName" to headName
        )
        // Update the document with the new data
        documentReference
            .update(updates)
            .addOnSuccessListener {
                // After successful update, retrieve the updated user data
                getProfile(uid) { user ->
                    onResult(true, user)
                }
            }
            .addOnFailureListener {
                onResult(false, null)
            }

    }

    fun getQurbaniAmount(
        uid: String,
        setTransactionAmount: (Int) -> Unit,
    ) {
        firestore.collection("transaction")
            .whereEqualTo("idJemaah", uid)
            .whereEqualTo("status", true)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    setTransactionAmount(0)
                } else {
                    setTransactionAmount(result.size())
                }
            }
            .addOnFailureListener {
                setTransactionAmount(0)
            }
    }

    fun logout() {
        firebaseAuth.signOut()
    }


}