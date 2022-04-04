package uz.context.wallpaperapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import uz.context.wallpaperapp.model.WallpapersModel
import uz.context.wallpaperapp.repository.FirebaseRepository

class WallpapersViewModel: ViewModel() {

    private val firebaseRepository: FirebaseRepository = FirebaseRepository()
    private val wallpaperList: MutableLiveData<List<WallpapersModel>> by lazy {
        MutableLiveData<List<WallpapersModel>>().also {
            loadWallpapersData()
        }
    }

    fun getWallpapersList(): LiveData<List<WallpapersModel>> {
        return wallpaperList
    }

    fun loadWallpapersData() {
        firebaseRepository.queryWallpapers().addOnCompleteListener {
            if (it.isSuccessful) {
                val result = it.result
                if (result!!.isEmpty) {

                } else {
                    if (wallpaperList.value == null) {
                        wallpaperList.value = result.toObjects(WallpapersModel::class.java)
                    } else {
                        wallpaperList.value = wallpaperList.value!!.plus(result.toObjects(WallpapersModel::class.java))
                    }
                    val listItem: DocumentSnapshot = result.documents[result.size() -1]
                    firebaseRepository.lastVisible = listItem
                }
            } else {
                Log.d("ViewModelLog","Error ${it.exception?.message}")
            }
        }
    }
}