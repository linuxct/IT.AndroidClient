package it.androidclient

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class LineAdapter(users: ArrayList<LineModel>?) : RecyclerView.Adapter<LineHolder>() {
    private val mModel: MutableList<LineModel>?
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineHolder {
        return LineHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_main_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: LineHolder, position: Int) {
        holder.title.text = String.format(
            Locale.getDefault(), "%s",
            mModel!![position].title
        )
        holder.itemView.setOnClickListener(titleListener)
    }

    override fun getItemCount(): Int {
        return mModel?.size ?: 0
    }

    private val titleListener =
        View.OnClickListener { v ->
            Toast.makeText(v.context.applicationContext, "Works", Toast.LENGTH_SHORT).show()
            notifyDataSetChanged()
        }

    init {
        mModel = users
    }
}