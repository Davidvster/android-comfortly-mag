package com.dv.comfortly.ui.dashboard

import android.util.TypedValue
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dv.comfortly.R
import com.dv.comfortly.databinding.ItemTripBinding
import com.dv.comfortly.domain.models.TripSummary
import com.dv.comfortly.ui.base.extensions.setThrottleClickListener
import com.dv.comfortly.ui.ext.layoutInflater
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class AnsweredTripsAdapter(
    private val onItemClickListener: (tripId: Long) -> Unit,
    private val onItemLongPressListener: (trip: TripSummary) -> Unit,
) : ListAdapter<TripSummary, RecyclerView.ViewHolder>(
        object : DiffUtil.ItemCallback<TripSummary>() {
            override fun areItemsTheSame(
                oldItem: TripSummary,
                newItem: TripSummary,
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: TripSummary,
                newItem: TripSummary,
            ): Boolean = oldItem == newItem
        },
    ) {
    companion object {
        private const val TRIP_DATE_FORMAT = "HH:mm\ndd.MM.yyyy"
    }

    private val tripDateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern(TRIP_DATE_FORMAT, Locale.getDefault())

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder = AnsweredAdapterItemView(ItemTripBinding.inflate(parent.context.layoutInflater, parent, false))

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        (holder as AnsweredAdapterItemView).bind(getItem(position))
    }

    private inner class AnsweredAdapterItemView(private val binding: ItemTripBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(trip: TripSummary) =
            with(binding) {
                val value = TypedValue()
                root.context.theme.resolveAttribute(R.attr.colorItemBackground, value, true)
                root.setCardBackgroundColor(value.data)
                tripName.text = trip.name
                tripId.text = trip.id.toString()
                fromTime.text =
                    trip.startTime?.let { startTime ->
                        tripDateFormat.format(startTime.toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime())
                    }
                toTime.text =
                    trip.endTime?.let { endTime ->
                        tripDateFormat.format(endTime.toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime())
                    }
                root.setThrottleClickListener {
                    onItemClickListener(trip.id)
                }
                root.setOnLongClickListener {
                    onItemLongPressListener(trip)
                    true
                }
            }
    }
}
