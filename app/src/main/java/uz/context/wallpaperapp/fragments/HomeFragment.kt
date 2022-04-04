package uz.context.wallpaperapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import uz.context.wallpaperapp.R
import uz.context.wallpaperapp.adapter.WallpaperAdapter
import uz.context.wallpaperapp.databinding.FragmentHomeBinding
import uz.context.wallpaperapp.model.WallpapersModel
import uz.context.wallpaperapp.repository.FirebaseRepository
import uz.context.wallpaperapp.utils.toast
import uz.context.wallpaperapp.viewmodel.WallpapersViewModel

class HomeFragment : Fragment(), (WallpapersModel) -> Unit {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var navController: NavController? = null
    var isLoading = false
    private val wallpapersViewModel: WallpapersViewModel by viewModels()

    private val firebaseRepository = FirebaseRepository()
    private var wallpapersList: List<WallpapersModel> = ArrayList()
    private val wallpaperAdapter = WallpaperAdapter(wallpapersList, this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(view: View) {
        navController = Navigation.findNavController(view)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolBar)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = getString(R.string.fire_wall)

        if (firebaseRepository.getUser() == null) {
            navController?.navigate(R.id.action_homeFragment_to_registerFragment)
        }
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = wallpaperAdapter
        }
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!isLoading) {
                        wallpapersViewModel.loadWallpapersData()
                        isLoading = true
                    }
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        wallpapersViewModel.getWallpapersList().observe(viewLifecycleOwner) {
            wallpapersList = it
            wallpaperAdapter.wallpaperList = wallpapersList
            wallpaperAdapter.notifyDataSetChanged()
            isLoading = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun invoke(wallpaper: WallpapersModel) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(wallpaper.image)
        navController?.navigate(action)
    }
}