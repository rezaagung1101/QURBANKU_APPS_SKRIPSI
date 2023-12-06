package com.androidexpert.qurbanku_apps_skripsi.ui.home.panitia

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidexpert.qurbanku_apps_skripsi.adapter.animal.AnimalPanitiaAdapter
import com.androidexpert.qurbanku_apps_skripsi.data.lib.Animal
import com.androidexpert.qurbanku_apps_skripsi.data.remote.AnimalRepository
import com.androidexpert.qurbanku_apps_skripsi.databinding.FragmentHomePanitiaBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.ViewModelFactory
import com.androidexpert.qurbanku_apps_skripsi.ui.animal.AnimalViewModel
import com.androidexpert.qurbanku_apps_skripsi.utils.UserPreference
import java.util.Timer

class HomePanitiaFragment : Fragment() {
    private lateinit var binding: FragmentHomePanitiaBinding
    private lateinit var animalViewModel: AnimalViewModel
    private val animalRepository = AnimalRepository()
    private lateinit var userPreference: UserPreference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomePanitiaBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animalViewModel = ViewModelProvider(
            this,
            ViewModelFactory.AnimalViewModelFactory(animalRepository)
        )[AnimalViewModel::class.java]
        userPreference = UserPreference(requireContext())
        animalViewModel.getAnimalList(userPreference.getUid()!!)
        animalViewModel.listAnimal.observe(viewLifecycleOwner) { animalList ->
            setupInformation(animalList)
        }
        animalViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    fun setupInformation(animalList: List<Animal>?) {
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvAnimalList.layoutManager = layoutManager

        var arrayAnimalList = arrayListOf<Animal>()
        if (animalList != null) {
            animalList.forEach { animal ->
                arrayAnimalList.add(animal)
            }
            binding.rvAnimalList.adapter = AnimalPanitiaAdapter(arrayAnimalList)
            binding.swipeRefresh.setOnRefreshListener {
                animalViewModel.getAnimalList(userPreference.getUid()!!)
                binding.swipeRefresh.isRefreshing = true
                // Use a Handler to post a delayed action
                Handler().postDelayed({
                    binding.swipeRefresh.isRefreshing = false
                    binding.rvAnimalList.smoothScrollToPosition(0)
                }, 1000)
            }
        }

    }

    fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        animalViewModel.getAnimalList(userPreference.getUid()!!)
        super.onResume()
    }

    override fun onDestroyView() {
        animalViewModel.listAnimal.removeObservers(viewLifecycleOwner)
        animalViewModel.isLoading.removeObservers(viewLifecycleOwner)
        super.onDestroyView()
    }
}