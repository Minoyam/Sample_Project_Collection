package com.mino.sampleprojectcollection

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mino.sampleprojectcollection.databinding.ItemQuoteBinding

class QuotePagerAdapter(
    private val quotes: List<QuoteResponse>,
    private val isNameRevealed: Boolean
) :
    RecyclerView.Adapter<QuotePagerAdapter.QuoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val binding = DataBindingUtil.inflate<ItemQuoteBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_quote,
            parent,
            false
        )
        return QuoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val actualPosition = position % quotes.size

        holder.bind(quotes[actualPosition], isNameRevealed)
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    inner class QuoteViewHolder(private val binding: ItemQuoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(quote: QuoteResponse, isNameRevealed: Boolean) {
            binding.run {
                quoteTextView.text = "\"${quote.quote}\""

                if (isNameRevealed) {
                    nameTextView.text = "- ${quote.name}"
                    nameTextView.isVisible = true
                } else {
                    nameTextView.isVisible = false
                }
            }
        }
    }
}