package it.androidclient

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LineHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var title: TextView = itemView.findViewById<View>(R.id.cardTextView) as TextView

}