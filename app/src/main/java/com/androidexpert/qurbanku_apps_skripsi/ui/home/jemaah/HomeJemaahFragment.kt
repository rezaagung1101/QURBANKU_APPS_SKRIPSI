package com.androidexpert.qurbanku_apps_skripsi.ui.home.jemaah

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidexpert.qurbanku_apps_skripsi.adapter.user.MasjidListAdapter
import com.androidexpert.qurbanku_apps_skripsi.data.lib.MasjidUser
import com.androidexpert.qurbanku_apps_skripsi.data.remote.UserRepository
import com.androidexpert.qurbanku_apps_skripsi.databinding.FragmentHomeJemaahBinding
import com.androidexpert.qurbanku_apps_skripsi.ui.ViewModelFactory
import com.androidexpert.qurbanku_apps_skripsi.ui.maps.MapsListMasjidActivity
import com.androidexpert.qurbanku_apps_skripsi.ui.profile.UserViewModel

class HomeJemaahFragment : Fragment() {
    private lateinit var binding: FragmentHomeJemaahBinding
    private lateinit var userViewModel: UserViewModel
    private val userRepository = UserRepository()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeJemaahBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel =
            ViewModelProvider(
                this, ViewModelFactory.UserViewModelFactory(userRepository)
            )[UserViewModel::class.java]

        binding.btnFindMasjidLocation.setOnClickListener {
            startActivity(Intent(requireContext(), MapsListMasjidActivity::class.java))
        }
        userViewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }
        userViewModel.getMasjidList()
        userViewModel.listMasjidUser.observe(viewLifecycleOwner){ masjidList ->
            setupInformation(masjidList)
        }
    }

    fun setupInformation(masjidList: List<MasjidUser>){
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvMasjidList.layoutManager = layoutManager
        val arrayListMasjid = arrayListOf<MasjidUser>()
        masjidList.forEach { masjidData ->
            arrayListMasjid.add(masjidData)
        }
        val adapter = MasjidListAdapter(arrayListMasjid)
        binding.rvMasjidList.adapter = adapter

        binding.swipeRefresh.setOnRefreshListener {
            userViewModel.getMasjidList()
            binding.swipeRefresh.isRefreshing = true
            // Use a Handler to post a delayed action
            Handler().postDelayed({
                binding.swipeRefresh.isRefreshing = false
                binding.rvMasjidList.smoothScrollToPosition(0)
            }, 1000)
        }
    }

    override fun onResume() {
        userViewModel.getMasjidList()
        super.onResume()
    }

    fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        userViewModel.apply {
            isLoading.removeObservers(viewLifecycleOwner)
            listMasjidUser.removeObservers(viewLifecycleOwner)
        }
        super.onDestroyView()
    }
}