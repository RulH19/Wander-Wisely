package id.com.wanderwisely.ui.home.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.com.wanderwisely.data.local.favorite.entity.FavoriteEntity
import id.com.wanderwisely.databinding.ItemFavoriteBinding
import id.com.wanderwisely.ui.detail.DetailActivity
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


class FavoriteAdapter :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private val wisata = ArrayList<FavoriteEntity>()
    private var onFavoriteClickListener: ((FavoriteEntity) -> Unit)? = null

    fun setList(wisatas : ArrayList<FavoriteEntity>){
        wisata.clear()
        wisata.addAll(wisatas)
        notifyDataSetChanged()
    }
    inner class FavoriteViewHolder(val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root){

            fun bind(favorite : FavoriteEntity){
                itemView.setOnClickListener {
                    val intentDetail = Intent(itemView.context, DetailActivity::class.java)
                    intentDetail.putExtra("id", favorite.id)
                    intentDetail.putExtra("costto", favorite.costTo)
                    intentDetail.putExtra("costfrom", favorite.costFrom)
                    intentDetail.putExtra("tourismfile", favorite.path)
                    intentDetail.putExtra("city", favorite.city)
                    intentDetail.putExtra("name", favorite.name)
                    itemView.context.startActivity(intentDetail)
                }
                binding.favorite.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        val item = wisata[position]
                        onFavoriteClickListener?.invoke(item)
                    }
                }
            }
        }
    fun setOnFavoriteClickListener(listener: (FavoriteEntity) -> Unit) {
        onFavoriteClickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding =
            ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun getItemCount(): Int = wisata.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.binding.itemTouristName.text = wisata[position].name
        holder.binding.tvLocation.text = wisata[position].city
        val fromPrice = wisata[position].costFrom
        val costTo = wisata[position].costTo
        val totalPrice = fromPrice + costTo
        val priceText = if (totalPrice == 0) {
            "Free"
        } else {
            (totalPrice / 2).toString()
        }
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        val formattedPrice = if (priceText == "Free") {
            priceText
        } else {
            currencyFormat.format(priceText.toBigDecimal())
        }
        holder.binding.tvPrice.text = formattedPrice
        val imageUrl = wisata[position].path
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .into(holder.binding.photo)
        holder.bind(wisata[position])
        holder.binding.favorite.setOnClickListener {
            val item = wisata[position]
            onFavoriteClickListener?.invoke(item)
        }
    }
}