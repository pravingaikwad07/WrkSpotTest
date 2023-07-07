package com.pravin.myapplication.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pravin.myapplication.R
import com.pravin.myapplication.databinding.ItemCountryBinding
import com.pravin.myapplication.data.model.Country
import com.squareup.picasso.Picasso


class CountryAdapter(
    private val values: List<Country>
) : RecyclerView.Adapter<CountryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ItemCountryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]

        if(item.media?.flag?.isNotEmpty()!!) {
            Picasso.get().load(item.media.emblem)
                .resize(150, 150)
                .placeholder(R.drawable.baseline_image_24)
                .error(R.drawable.baseline_broken_image_24)
                .into(holder.ivFlag)
        }else{
            Picasso.get().load(R.drawable.baseline_image_24)
                .into(holder.ivFlag)
        }

        holder.tvCountry.text = item.name
        holder.tvCapital.text = "Capital: ${item.capital}"
        holder.tvPopulation.text = "Population: ${item.population}"
        holder.tvCurrency.text = "Currency: ${item.currency}"


    }

    override fun getItemCount(): Int {
        return values.size
    }

    inner class ViewHolder(binding: ItemCountryBinding) : RecyclerView.ViewHolder(binding.root) {
        val ivFlag: ImageView = binding.ivFlag
        val tvCountry: TextView = binding.tvCountry
        val tvCapital: TextView = binding.tvCapital
        val tvPopulation: TextView = binding.tvPopulation
        val tvCurrency: TextView = binding.tvCurrency

    }

}