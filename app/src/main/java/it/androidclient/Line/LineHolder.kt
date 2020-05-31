package it.androidclient.Line

import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.androidclient.R

class LineHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var title: TextView = itemView.findViewById<View>(R.id.cardTextView) as TextView
    var isCompleted: ImageView = itemView.findViewById<View>(R.id.isCompleted) as ImageView

    var leftTextView = itemView.findViewById<View>(R.id.leftTextView) as TextView
    var rightTextView = itemView.findViewById<View>(R.id.rightTextView) as TextView

    var coreLayout = itemView.findViewById<View>(R.id.coreLayout) as RelativeLayout
    var specialLayout = itemView.findViewById<View>(R.id.specialLayout) as RelativeLayout
}