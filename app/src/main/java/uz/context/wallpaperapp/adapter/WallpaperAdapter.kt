package uz.context.wallpaperapp.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import uz.context.wallpaperapp.databinding.ItemLayoutBinding
import uz.context.wallpaperapp.model.WallpapersModel

class WallpaperAdapter(
    var wallpaperList: List<WallpapersModel>,
    private val clickListener: (WallpapersModel) -> Unit
) :
    RecyclerView.Adapter<WallpaperAdapter.WallPaperViewHolder>() {

    inner class WallPaperViewHolder(val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(wallpapers: WallpapersModel, clickListener: (WallpapersModel) -> Unit) {
            Glide.with(itemView.context)
                .load(wallpapers.image)
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
                        binding.progressBar.isVisible = false
                        return false
                    }
                })
                .into(binding.imageView)

            itemView.setOnClickListener {
                clickListener(wallpapers)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallPaperViewHolder {
        return WallPaperViewHolder(
            ItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WallPaperViewHolder, position: Int) {
        holder.bind(wallpaperList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return wallpaperList.size
    }
}