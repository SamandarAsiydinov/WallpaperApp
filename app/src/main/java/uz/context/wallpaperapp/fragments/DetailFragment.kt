package uz.context.wallpaperapp.fragments

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import es.dmoral.toasty.Toasty
import uz.context.wallpaperapp.R
import uz.context.wallpaperapp.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private var image: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        image = DetailFragmentArgs.fromBundle(requireArguments()).wallpaperImage

        binding.btnSet.setOnClickListener {
            setWallpaper()
        }
    }

    private fun setWallpaper() {
        binding.btnSet.isEnabled = false
        binding.btnSet.text = getString(R.string.wall)
        val bitmap: Bitmap = binding.imageView.drawable.toBitmap()
        val task = SetWallpaperTask(requireContext(),bitmap)
        task.execute(true)
        Toasty.success(requireContext(),"Successfully set", Toasty.LENGTH_LONG).show()
    }

    companion object {
        class SetWallpaperTask internal constructor(val myContext: Context, val bitmap: Bitmap):
            AsyncTask<Boolean, String, String>() {
            override fun doInBackground(vararg p0: Boolean?): String {
                val wallpaperManager: WallpaperManager = WallpaperManager.getInstance(myContext)
                wallpaperManager.setBitmap(bitmap)
                return "Wallpaper Set"
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (image != null) {
            Glide.with(requireContext())
                .load(image)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.btnSet.isVisible = true
                        binding.progressBar.isVisible = false
                        return false
                    }
                })
                .into(binding.imageView)
        }
    }

    override fun onStop() {
        super.onStop()
        Glide.with(requireContext()).clear(binding.imageView)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}