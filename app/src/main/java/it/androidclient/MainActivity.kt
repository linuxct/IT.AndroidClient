package it.androidclient

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var mAdapter: LineAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecycler()
    }

    private fun setupRecycler() {
        val layoutManager = LinearLayoutManager(this)
        rvMain.layoutManager = layoutManager

        val list = arrayListOf<LineModel>()
        list.add(LineModel("LECTURA"))
        list.add(LineModel("LENGUAJE"))
        list.add(LineModel("PENSAMIENTO ABSTRACTO"))
        list.add(LineModel("CONCENTRACION"))
        list.add(LineModel("PRAXIAS"))
        list.add(LineModel("PERCEPCION SENSORIAL"))
        mAdapter = LineAdapter(list)
        rvMain.adapter = mAdapter

        rvMain.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
    }
}
