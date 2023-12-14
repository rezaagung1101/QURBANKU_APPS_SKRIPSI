package com.androidexpert.qurbanku_apps_skripsi.data.remote

import android.net.Uri
import com.androidexpert.qurbanku_apps_skripsi.data.lib.Animal
import com.androidexpert.qurbanku_apps_skripsi.data.lib.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class AnimalRepository() {

    private val storageRef = FirebaseStorage.getInstance().reference
    private val firestore = FirebaseFirestore.getInstance()

    fun uploadPhoto(file: File, onResult: (String?) -> Unit) {
        val photoRef = storageRef.child("animal_photos/${file.name}")
        photoRef.putFile(Uri.fromFile(file))
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


    fun addAnimal(animal: Animal, file: File, onResult: (Boolean, Animal?) -> Unit) {
        uploadPhoto(file) { photoUrl ->
            if (photoUrl != null) {
                val newAnimal = animal.copy(photoUrl = photoUrl)
                firestore.collection("animal")
                    .add(newAnimal)
                    .addOnCompleteListener { firestoreTask ->
                        if (firestoreTask.isSuccessful) {
                            val documentId = firestoreTask.result?.id
                            if (documentId != null) {
                                val updatedAnimal = newAnimal.copy(id = documentId)
                                firestore.collection("animal")
                                    .document(documentId)
                                    .set(updatedAnimal)
                                    .addOnCompleteListener { updateTask ->
                                        getDetailAnimal(updatedAnimal.id) { animalData ->
                                            onResult(updateTask.isSuccessful, animalData)
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

    fun getAnimalList(idMasjid: String?, setAnimalList: (List<Animal>?) -> Unit) {
        firestore.collection("animal")
            .whereEqualTo("idMasjid", idMasjid!!)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) setAnimalList(null)
                else {
                    val animalList = mutableListOf<Animal>()
                    for (document in result) {
                        val animal = document.toObject<Animal>()
                        animalList.add(animal)
                    }
                    setAnimalList(animalList)
                }
            }
            .addOnFailureListener {
                setAnimalList(null)
            }
    }

    fun deleteAnimal(id: String, photoUrl: String, onResult: (Boolean) -> Unit) {
        val documentReference = firestore.collection("animal").document(id)
        //Delete document from Firestore
        documentReference.delete()
            .addOnCompleteListener { firestoreTask ->
                if (firestoreTask.isSuccessful) {
                    //Delete photo from Storage using the photo URL
                    val photoRef = storageRef.storage.getReferenceFromUrl(photoUrl)
                    photoRef.delete()
                        .addOnCompleteListener { storageTask ->
                            if (storageTask.isSuccessful) {
                                onResult(true)
                            } else {
                                // Handle the failure to delete the photo from Storage
                                onResult(false)
                            }
                        }
                } else {
                    // Handle the failure to delete the document from Firestore
                    onResult(false)
                }
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

    fun updateAnimalStatus(id: String, onResult: (Boolean, Animal?) -> Unit) {
        val documentReference = firestore.collection("animal").document(id)
        documentReference.update("status", true)
            .addOnCompleteListener { firestoreTask ->
                if (firestoreTask.isSuccessful) {
                    getDetailAnimal(id) { updatedAnimalData ->
                        onResult(true, updatedAnimalData)
                    }
                } else {
                    onResult(false, null)
                }
            }
            .addOnFailureListener {
                onResult(false, null)
            }
    }

}