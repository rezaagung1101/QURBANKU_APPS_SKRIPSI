package com.androidexpert.qurbanku_apps_skripsi.data.remote

import android.net.Uri
import com.androidexpert.qurbanku_apps_skripsi.data.lib.Animal
import com.androidexpert.qurbanku_apps_skripsi.data.lib.Transaction
import com.androidexpert.qurbanku_apps_skripsi.data.lib.TransactionDetail
import com.androidexpert.qurbanku_apps_skripsi.data.lib.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class TransactionRepository() {
    private val storageRef = FirebaseStorage.getInstance().reference
    private val firestore = FirebaseFirestore.getInstance()

    fun uploadPhoto(photoFile: File, onResult: (String?) -> Unit) {
        val photoRef = storageRef.child("transaction_photos/${photoFile.name}")
        photoRef.putFile(Uri.fromFile(photoFile))
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Get the download URL
                    photoRef.downloadUrl.addOnSuccessListener { uri ->
                        onResult(uri.toString())
                    }.addOnFailureListener {
                        onResult(null)
                    }
                } else {
                    onResult(null)
                }
            }
    }

    fun addTransaction(
        transaction: Transaction,
        photoFile: File, onResult: (Boolean, TransactionDetail?) -> Unit
    ) {
        uploadPhoto(photoFile) { photoUrl ->
            if (photoUrl != null) {
                val newTransaction = transaction.copy(photoUrl = photoUrl)
                firestore.collection("transaction")
                    .add(newTransaction)
                    .addOnCompleteListener { firestoreTask ->
                        if (firestoreTask.isSuccessful) {
                            val documentId = firestoreTask.result?.id
                            if (documentId != null) {
                                val updatedTransaction = newTransaction.copy(id = documentId)
                                firestore.collection("transaction")
                                    .document(documentId)
                                    .set(updatedTransaction)
                                    .addOnCompleteListener { updateTask ->
                                        getDetailTransaction(updatedTransaction.id) { transactionData ->
                                            onResult(updateTask.isSuccessful, transactionData)
                                        }
                                    }
                            } else {
                                onResult(false, null)
                            }
                        } else {
                            onResult(false, null)
                        }
                    }
                    .addOnFailureListener {
                        onResult(false, null)
                    }
            } else {
                onResult(false, null)
            }
        }
    }

    fun getDetailTransaction(id: String?, setTransaction: (TransactionDetail?) -> Unit) {
        if (id != null) {
            firestore.collection("transaction").document(id)
                .get()
                .addOnSuccessListener { transactionDocument ->
                    val transaction = transactionDocument.toObject<Transaction>()
                    if (transaction != null) {
                        getDetailUser(transaction.idJemaah) { jemaah ->
                            getDetailUser(transaction.idMasjid) { masjid ->
                                getDetailAnimal(transaction.idAnimal) { animal ->
                                    val transactionDetail = TransactionDetail(
                                        transaction = transaction,
                                        jemaah = jemaah,
                                        masjid = masjid,
                                        animal = animal
                                    )
                                    setTransaction(transactionDetail)
                                }
                            }
                        }
                    } else {
                        setTransaction(null)
                    }
                }
                .addOnFailureListener {
                    setTransaction(null)
                }
        } else {
            setTransaction(null)
        }
    }

    fun getDetailUser(uid: String, setUser: (User?) -> Unit) {
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

    fun getDetailAnimal(id: String?, setAnimal: (Animal?) -> Unit) {
        if (id != null) {
            firestore.collection("animal").document(id)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val animal = documentSnapshot.toObject<Animal>()
                    setAnimal(animal)
                }
                .addOnFailureListener {
                    setAnimal(null)
                }
        } else {
            setAnimal(null)
        }
    }

    fun getTransactionList(
        uid: String,
        isAdmin: Boolean,
        setTransactionList: (List<TransactionDetail>?) -> Unit
    ) {
        val field = if (isAdmin) "idMasjid" else "idJemaah"

        firestore.collection("transaction")
            .whereEqualTo(field, uid)
            .get()
            .addOnSuccessListener { result ->
                val transactionList = mutableListOf<TransactionDetail>()
                if (result.isEmpty) {
                    setTransactionList(null)
                } else {
                    for (document in result) {
                        val transaction = document.toObject<Transaction>()
                        getDetailUser(transaction.idJemaah) { jemaah ->
                            getDetailUser(transaction.idMasjid) { masjid ->
                                getDetailAnimal(transaction.idAnimal) { animal ->
                                    val transactionDetail = TransactionDetail(
                                        transaction = transaction,
                                        jemaah = jemaah,
                                        masjid = masjid,
                                        animal = animal
                                    )
                                    transactionList.add(transactionDetail)
                                    if (transactionList.size == result.size()) {
                                        setTransactionList(transactionList)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            .addOnFailureListener {
                setTransactionList(null)
            }
    }

    fun getAcceptedTransaction(
        uid: String,
        setTransactionList: (List<TransactionDetail>?) -> Unit,
    ) {
        firestore.collection("transaction")
            .whereEqualTo("idJemaah", uid)
            .whereEqualTo("status", true)
            .get()
            .addOnSuccessListener { result ->
                val transactionList = mutableListOf<TransactionDetail>()
                if (result.isEmpty) {
                    setTransactionList(null)
                } else {
                    for (document in result) {
                        val transaction = document.toObject<Transaction>()
                        getDetailUser(transaction.idJemaah) { jemaah ->
                            getDetailUser(transaction.idMasjid) { masjid ->
                                getDetailAnimal(transaction.idAnimal) { animal ->
                                    val transactionDetail = TransactionDetail(
                                        transaction = transaction,
                                        jemaah = jemaah,
                                        masjid = masjid,
                                        animal = animal
                                    )
                                    transactionList.add(transactionDetail)
                                    if (transactionList.size == result.size()) {
                                        setTransactionList(transactionList)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            .addOnFailureListener {
                setTransactionList(null)
            }
    }

    fun confirmTransaction(
        idJemaah: String,
        idAnimal: String,
        idTransaction: String,
        status: Boolean,
        note: String?,
        onResult: (Boolean, TransactionDetail?) -> Unit,
    ) {
        firestore.collection("transaction").document(idTransaction)
            .update(
                "status", status,
                "note", note
            )
            .addOnCompleteListener { updateTask ->
                if (updateTask.isSuccessful) {
                    if (status) {
                        updateAnimalShohibulQurbaniList(idJemaah, idAnimal) { isAnimalUpdateSuccessful ->
                            if (isAnimalUpdateSuccessful) {
                                getDetailTransaction(idTransaction) { transactionData ->
                                    onResult(true, transactionData)
                                }
                            } else {
                                onResult(false, null)
                            }
                        }
                    } else {
                        getDetailTransaction(idTransaction) { transactionData ->
                            onResult(true, transactionData)
                        }
                    }
                } else {
                    onResult(false, null)
                }
            }
            .addOnFailureListener {
                onResult(false, null)
            }
    }

//    fun updateAnimalShohibulQurbaniList(idJemaah: String, idAnimal: String, onUpdateResult: (Boolean) -> Unit) {
//        firestore.collection("animal").document(idAnimal)
//            .update(
//                "idShohibulQurbaniList", FieldValue.arrayUnion(idJemaah)
//            )
//            .addOnCompleteListener { animalUpdateTask ->
//                onUpdateResult(animalUpdateTask.isSuccessful)
//            }
//            .addOnFailureListener {
//                onUpdateResult(false)
//            }
//    }

    fun updateAnimalShohibulQurbaniList(idJemaah: String, idAnimal: String, onUpdateResult: (Boolean) -> Unit) {
        // Fetch the current document to get the existing idShohibulQurbaniList
        firestore.collection("animal").document(idAnimal)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val currentList = documentSnapshot.get("idShohibulQurbaniList") as? List<String> ?: emptyList()

                // Update the local list with the new idJemaah
                val updatedList = currentList.toMutableList()
                updatedList.add(idJemaah)

                // Update the document with the new array
                firestore.collection("animal").document(idAnimal)
                    .update("idShohibulQurbaniList", updatedList)
                    .addOnCompleteListener { animalUpdateTask ->
                        onUpdateResult(animalUpdateTask.isSuccessful)
                    }
                    .addOnFailureListener {
                        onUpdateResult(false)
                    }
            }
            .addOnFailureListener {
                onUpdateResult(false)
            }
    }

}