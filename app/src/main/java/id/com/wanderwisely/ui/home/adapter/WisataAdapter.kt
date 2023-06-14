package id.com.wanderwisely.ui.home.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.paging.filter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.com.wanderwisely.data.model.response.WisataResponse
import id.com.wanderwisely.databinding.ItemAllTouristBinding
import id.com.wanderwisely.ui.detail.DetailActivity
import kotlinx.coroutines.flow.MutableStateFlow

class WisataAdapter : PagingDataAdapter<WisataResponse, WisataAdapter.WisataViewHolder>(
    DIFF_CALLBACK
){
    private val originalDataFlow = MutableStateFlow<PagingData<WisataResponse>>(PagingData.empty())
    class WisataViewHolder(private val binding :ItemAllTouristBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(wisata : WisataResponse){
            binding.itemTouristName.text = wisata.name
            binding.tvLocation.text = wisata.city
            val fromPrice = wisata.costFrom
            val costTo = wisata.costTo
            val totalPrice = fromPrice + costTo
            val priceText = if (totalPrice == 0) {
                "Free"
            } else {
                "Rp. ${(totalPrice / 2)}"
            }
            binding.tvPrice.text = priceText
            val mediaItem = wisata.tourismFiles?.firstOrNull() // Assuming you want to load the first media item
            val imageUrl = mediaItem?.path
            Glide.with(itemView.context)
                .load(imageUrl)
                .into(binding.photo)
            itemView.setOnClickListener {
                val intentDetail = Intent(itemView.context, DetailActivity::class.java)
                intentDetail.putExtra("id", wisata.id)
                intentDetail.putExtra("lat",wisata.latitude)
                intentDetail.putExtra("lon",wisata.longitude)
                intentDetail.putExtra("city", wisata.city)
                intentDetail.putExtra("costto", wisata.costTo)
                intentDetail.putExtra("costfrom", wisata.costFrom)
                intentDetail.putExtra("tourismfile", imageUrl)
                intentDetail.putExtra("name", wisata.name)
                itemView.context.startActivity(intentDetail)
            }
            binding.favorite.visibility = View.GONE
        }
    }
    suspend fun filterData(query: String?, category: String?) {
        val originalData = originalDataFlow.value
        val filteredData = if (query.isNullOrBlank() && (category.isNullOrBlank() || category == "All")) {
            originalData
        } else {
            originalData.filter {
                (query.isNullOrBlank() || it.name?.contains(query, ignoreCase = true) == true) &&
                        (category.isNullOrBlank() || it.tourismType?.name == category || category == "All")
            }
        }
        submitData(filteredData)
    }

    fun updateData(data: PagingData<WisataResponse>) {
        originalDataFlow.value = data
    }
    override fun onBindViewHolder(holder: WisataViewHolder, position: Int) {
        val wisata = getItem(position)
        if(wisata != null){
            holder.bind(wisata)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WisataViewHolder {
        val binding = ItemAllTouristBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WisataViewHolder(binding)
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<WisataResponse>() {
            override fun areItemsTheSame(oldItem: WisataResponse, newItem: WisataResponse): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: WisataResponse, newItem: WisataResponse): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}