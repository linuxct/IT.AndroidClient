package it.androidclient.Views

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import it.androidclient.Line.LineAdapter
import it.androidclient.Line.LineModel
import it.androidclient.R
import it.androidclient.UserCtx.UserDataDto
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var mAdapter: LineAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userDataDto =
            UserDataDto(applicationContext)
        if (userDataDto.userName.isNullOrBlank()){
            val intent = Intent(applicationContext, WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.toolbar_title).apply {
            text = resources.getString(R.string.greeting).replace("{0}", userDataDto.userName.toString())
        }
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
