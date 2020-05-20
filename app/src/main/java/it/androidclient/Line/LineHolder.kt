package it.androidclient.Line

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.androidclient.R

class LineHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var title: TextView = itemView.findViewById<View>(R.id.cardTextView) as TextView
    var isCompleted: ImageView = itemView.findViewById<ImageView>(R.id.isCompleted) as ImageView
}