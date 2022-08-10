package com.dv.comfortly.ui.trip.setup

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dv.comfortly.R
import com.dv.comfortly.databinding.ItemDeviceBinding
import com.dv.comfortly.domain.models.HeartRateDevice
import com.dv.comfortly.ui.base.extensions.setThrottleClickListener
import com.dv.comfortly.ui.ext.layoutInflater

class HrDevicesAdapter(private val onDeviceSelected: (deviceId: String) -> Unit) :
    ListAdapter<HeartRateDevice, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<HeartRateDevice>() {
        override fun areItemsTheSame(oldItem: HeartRateDevice, newItem: HeartRateDevice): Boolean = oldItem.deviceId == newItem.deviceId

        override fun areContentsTheSame(oldItem: HeartRateDevice, newItem: HeartRateDevice): Boolean = oldItem == newItem
    }) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder = HearRateAdapterItemView(ItemDeviceBinding.inflate(parent.context.layoutInflater, parent, false))

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        (holder as HearRateAdapterItemView).bind(getItem(position))
    }

    private inner class HearRateAdapterItemView(private val binding: ItemDeviceBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(device: HeartRateDevice) = with(binding) {
            deviceId.text = root.context.getString(R.string.device_id, device.deviceId)
            deviceName.text = root.context.getString(R.string.device_name, device.name)
            deviceAddress.text = root.context.getString(R.string.device_address, device.address)
            root.setThrottleClickListener { onDeviceSelected(device.deviceId) }
        }
    }
}
