package view.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.R
import model.Weather


class MainFragmentAdapter(private var onItemViewClickListener: MainFragment.OnItemViewClickListener?) :
    RecyclerView.Adapter<MainFragmentAdapter.MainViewHolder>() {

    private var weatherData: List<Weather> = listOf()

    fun setWeather(data: List<Weather>) {
        val diffCallback = WeatherDiffCallback(weatherData, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        weatherData = data
        diffResult.dispatchUpdatesTo(this)
    }

    class WeatherDiffCallback(
        private val oldList: List<Weather>,
        private val newList: List<Weather>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldWeather = oldList[oldItemPosition]
            val newWeather = newList[newItemPosition]
            return oldWeather.city == newWeather.city
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldWeather = oldList[oldItemPosition]
            val newWeather = newList[newItemPosition]
            return oldWeather == newWeather
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_main_recycler_item, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(weatherData[position])
    }

    override fun getItemCount(): Int {
        return weatherData.size
    }

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(weather: Weather) {
            itemView.apply {
                findViewById<TextView>(R.id.mainFragmentRecyclerItemTextView).text =
                    weather.city.name
                setOnClickListener { onItemViewClickListener?.onItemViewClick(weather) }
            }
        }
    }
}
