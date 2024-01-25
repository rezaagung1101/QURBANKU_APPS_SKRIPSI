package com.androidexpert.qurbanku_apps_skripsi.ui.animal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidexpert.qurbanku_apps_skripsi.data.lib.Animal
import com.androidexpert.qurbanku_apps_skripsi.data.lib.User
import com.androidexpert.qurbanku_apps_skripsi.data.remote.AnimalRepository
import com.androidexpert.qurbanku_apps_skripsi.utils.SingleLiveEvent
import java.io.File

class AnimalViewModel(private val animalRepository: AnimalRepository) : ViewModel() {

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private var _addAnimalStatusResult = SingleLiveEvent<Boolean>()
    val addAnimalStatusResult: LiveData<Boolean> = _addAnimalStatusResult
    private var _deleteAnimalResult = SingleLiveEvent<Boolean>()
    val deleteAnimalResult: LiveData<Boolean> = _deleteAnimalResult
    private var _updateAnimalStatusResult = SingleLiveEvent<Boolean>()
    val updateAnimalStatusResult: LiveData<Boolean> = _updateAnimalStatusResult
    private var _addShohibulQurbaniResult = SingleLiveEvent<Boolean>()
    val addShohibulQurbaniResult: LiveData<Boolean> = _addShohibulQurbaniResult
    private var _animal = MutableLiveData<Animal>()
    val animal: LiveData<Animal> = _animal
    private var _listAnimal = MutableLiveData<List<Animal>>()
    val listAnimal: LiveData<List<Animal>> = _listAnimal

    fun addAnimal(animal: Animal, photo: File) {
        _isLoading.value = true
        animalRepository.addAnimal(animal, photo){ isSuccess, animalData ->
            _isLoading.value = false
            _addAnimalStatusResult.value = isSuccess
            _animal.value = animalData
        }
    }

    fun getAnimalList(idMasjid: String){
        _isLoading.value = true
        animalRepository.getAnimalList(idMasjid){ animalList ->
            _isLoading.value = false
            _listAnimal.value = animalList
        }
    }

    fun deleteAnimal(id: String, photoUrl: String){
        _isLoading.value = true
        animalRepository.deleteAnimal(id, photoUrl){ isSuccess ->
            _isLoading.value = false
            _deleteAnimalResult.value = isSuccess
        }
    }

    fun updateAnimalStatus(id: String){
        _isLoading.value = true
        animalRepository.updateAnimalStatus(id){ isSuccess, animalData ->
            _isLoading.value = false
            _updateAnimalStatusResult.value = isSuccess
            _animal.value = animalData
        }
    }

    fun addShohibulQurbani(user: User, idAnimal: String) {
        _isLoading.value = true
        animalRepository.addShohibulQurbani(user, idAnimal){ isSuccess, updatedAnimal ->
            _isLoading.value = false
            _animal.value = updatedAnimal
            _addShohibulQurbaniResult.value = isSuccess
        }
    }
}