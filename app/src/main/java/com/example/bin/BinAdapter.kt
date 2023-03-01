package com.example.bin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bin.databinding.ItemBinFragmentBinding

class BinAdapter(private val itemClicked: (itemId: Int) -> Unit) :
    ListAdapter<Bin, BinAdapter.BinHolder>(BinDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BinHolder {
        return BinHolder(itemClicked, LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bin_fragment, parent, false))
    }

    override fun onBindViewHolder(holder: BinHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    override fun getItemCount(): Int = currentList.size

    class BinDiffUtil : DiffUtil.ItemCallback<Bin>() {
        override fun areItemsTheSame(oldItem: Bin, newItem: Bin): Boolean = (oldItem == newItem)

        override fun areContentsTheSame(oldItem: Bin, newItem: Bin): Boolean =
            (oldItem.id == newItem.id)
    }

    class BinHolder(private val itemClicked: (itemId: Int) -> Unit, view: View) :
        RecyclerView.ViewHolder(view) {

        init {
            itemView.setOnClickListener {
                itemClicked(adapterPosition)
            }
        }

        fun onBind(bin: Bin) {
            val binding = ItemBinFragmentBinding.bind(itemView)
            with(binding) {
                txtId.text = bin.bin
                txtScheme.text = bin.scheme
                txtBrand.text = bin.brand
            }
        }
    }
}