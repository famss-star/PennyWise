package com.example.pennywise.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pennywise.databinding.ItemTransactionBinding
import com.example.pennywise.data.Transaction
import com.example.pennywise.utils.formatCurrency
import java.time.format.DateTimeFormatter
import java.util.Locale

class TransactionAdapter(
    private val onItemClick: (Transaction) -> Unit
) : ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(TransactionDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemTransactionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TransactionViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }
    
    class TransactionViewHolder(
        private val binding: ItemTransactionBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(transaction: Transaction, onItemClick: (Transaction) -> Unit) {
            binding.tvTitle.text = transaction.title
            binding.tvAmount.text = formatCurrency(transaction.totalAmount)
            
            // Format date and time
            val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm", Locale.getDefault())
            val dateTimeStr = "${transaction.date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))} ${transaction.time.format(DateTimeFormatter.ofPattern("HH:mm"))}"
            binding.tvDate.text = dateTimeStr
            
            // Show items summary
            val itemsCount = transaction.items.size
            val itemsSummary = "$itemsCount item"
            binding.tvItems.text = itemsSummary
            
            binding.root.setOnClickListener {
                onItemClick(transaction)
            }
        }
    }
    
    class TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem == newItem
        }
    }
}
