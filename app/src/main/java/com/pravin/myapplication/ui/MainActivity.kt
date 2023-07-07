package com.pravin.myapplication.ui

import android.os.Bundle
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.pravin.myapplication.MainViewModel
import com.pravin.myapplication.R
import com.pravin.myapplication.domain.model.Country
import com.pravin.myapplication.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var countryAdapter: CountryAdapter
    var countryList: List<Country> = ArrayList()

    val viewModel: MainViewModel by viewModels()

    // murali@wrkspot.com
    // siva@wrkspot.com
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.fetchCountries()

        val tvDate = binding.toolbar.findViewById<TextView>(R.id.tvDate)

        viewModel.getCurrentTime()

        viewModel.changingTime.observe(this) {
            tvDate.text = it
        }

        viewModel.filterData.observe(this) {

            if (::countryAdapter.isInitialized) {
                countryList = it
                countryAdapter.notifyDataSetChanged()
            }

        }

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.countries.collect { data ->
                when {
                    data.isLoading -> {
                        binding.progressBar.isVisible = true
                    }

                    data.data.isNotEmpty() -> {
                        Toast.makeText(
                            this@MainActivity, "Size: " + data.data.size, Toast.LENGTH_SHORT
                        ).show()

                        binding.progressBar.isVisible = false
                        countryList = data.data
                        setListAdapter()
                    }

                    data.error.isNotBlank() -> {
                        binding.progressBar.isVisible = false
                        Toast.makeText(this@MainActivity, data.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        binding.ivFilter.setOnClickListener {
            showFilterMenu()
        }



        binding.searchView.setOnQueryTextListener(object :
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(this@MainActivity, "Pending", Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return false
            }
        })

    }

    private fun showFilterMenu() {
        val popupMenu = PopupMenu(this, binding.ivFilter)
        popupMenu.menuInflater.inflate(
            R.menu.filter_menu, popupMenu.menu
        )

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_clear -> {
                    viewModel.applyFilter(-1)
                }

                R.id.menu_population_1 -> {

                    viewModel.applyFilter(1)
                }

                R.id.menu_population_5 -> {
                    viewModel.applyFilter(5)

                }

                R.id.menu_population_10 -> {
                    viewModel.applyFilter(10)

                }
            }
            true
        }
        popupMenu.show()
    }

    private fun setListAdapter() {
        binding.rvCountries.layoutManager = LinearLayoutManager(this)

        countryAdapter = CountryAdapter(countryList)
        binding.rvCountries.adapter = countryAdapter
    }
}