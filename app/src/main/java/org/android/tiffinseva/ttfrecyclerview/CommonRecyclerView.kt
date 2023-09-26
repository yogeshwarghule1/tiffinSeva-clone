package org.android.tiffinseva.ttfrecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class TtfViewHolder<T>(val binding: ViewDataBinding, private val bindingId: IItemBinding<T>) : RecyclerView.ViewHolder(binding.root) {
    fun bind(obj: T) {
        binding.setVariable(bindingId.getBindingVariableId(obj), obj)
        binding.executePendingBindings()
    }
}

class TtsBaseAdapter<T>(private val arrayList: ObservableArrayList<T>,
                        private val itemBindingListener: IItemBinding<T>) : RecyclerView.Adapter<TtfViewHolder<T>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TtfViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
                layoutInflater, viewType, parent, false)
        return TtfViewHolder(binding, itemBindingListener)
    }

    override fun onBindViewHolder(holder: TtfViewHolder<T>, position: Int) {
        val obj = arrayList[position]
        holder.bind(obj)
    }

    override fun getItemViewType(position: Int): Int {
        return itemBindingListener.getLayoutIdForPosition(arrayList[position])
    }

    override fun getItemCount(): Int = arrayList.size
}

interface IItemBinding<T> {
    fun getLayoutIdForPosition(base: T): Int
    fun getBindingVariableId(base: T): Int
}
