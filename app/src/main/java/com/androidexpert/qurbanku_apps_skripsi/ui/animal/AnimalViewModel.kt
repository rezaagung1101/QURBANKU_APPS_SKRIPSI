package com.androidexpert.qurbanku_apps_skripsi.ui.animal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidexpert.qurbanku_apps_skripsi.data.lib.Animal
import com.androidexpert.qurbanku_apps_skripsi.data.remote.AnimalRepository
import com.androidexpert.qurbanku_apps_skripsi.utils.SingleLiveEvent
import java.io.File

class AnimalViewModel(private val animalRepository: AnimalRepository) : ViewModel() {

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private var _addAnimalResult = SingleLiveEvent<Boolean>()
    val addAnimalResult: LiveData<Boolean> = _addAnimalResult
    private var _deleteAnimalResult = SingleLiveEvent<Boolean>()
    val deleteAnimalResult: LiveData<Boolean> = _deleteAnimalResult
    private var _updateAnimalStatusResult = SingleLiveEvent<Boolean>()
    val updateAnimalStatusResult: LiveData<Boolean> = _updateAnimalStatusResult
    private var _animal = MutableLiveData<Animal>()
    val animal: LiveData<Animal> = _animal
    private var _listAnimal = MutableLiveData<List<Animal>>()
    val listAnimal: LiveData<List<Animal>> = _listAnimal

    fun addAnimal(animal: Animal, photoFile: File) {
        _isLoading.value = true
        animalRepository.addAnimal(animal, photoFile){ isSuccess, animalData ->
            _isLoading.value = false
            _addAnimalResult.value = isSuccess
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
}