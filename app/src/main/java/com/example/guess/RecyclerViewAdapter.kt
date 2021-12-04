package com.example.guess

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_row.view.*
class RecyclerViewAdapter(private val guessArray : List<String>):RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder (itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return  MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_row,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
      val result = guessArray[position]
        holder.itemView.apply {
            tvPhrase.text = result
            if (result.startsWith("Found"))
                tvPhrase.setTextColor(Color.GREEN)

            else if (result.startsWith("Wrong")|| result.startsWith("Game"))
                tvPhrase.setTextColor(Color.RED)
            else
                tvPhrase.setTextColor(Color.BLACK)

        }
    }

    override fun getItemCount()= guessArray.size
}