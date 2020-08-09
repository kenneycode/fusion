package io.github.kenneycode.fusion.demo

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.kenneycode.fusion.demo.SimpleActivity.Companion.KEY_SAMPLE_INDEX
import io.github.kenneycode.fusion.util.BitmapUtil
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import io.github.kenneycode.fusion.util.Utils




/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * fusion demo
 *
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        BitmapUtil.context = applicationContext
        Utils.copyAssetsFiles(this, "test.mp4", "/sdcard/test.mp4")
        checkPermissions()
        val samplesList = findViewById<RecyclerView>(R.id.list)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        samplesList.layoutManager = layoutManager
        samplesList.adapter = MyAdapter()

    }


    private fun checkPermissions() {
        val permissions = mutableListOf<String>()
        if (ContextCompat.checkSelfPermission(application, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (ContextCompat.checkSelfPermission(application, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA)
        }
        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), 1)
        }
    }

    inner class MyAdapter : RecyclerView.Adapter<VH>() {

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): VH {
            val view = LayoutInflater.from(p0.context).inflate(R.layout.layout_sample_list_item, p0, false)
            return VH(view)
        }

        override fun getItemCount(): Int {
            return SimpleActivity.samples.size
        }

        override fun onBindViewHolder(p0: VH, p1: Int) {
            p0.button.text = getString(SimpleActivity.samples[p1].first!!)
            p0.button.setOnClickListener {
                val intent = Intent(this@MainActivity, SimpleActivity::class.java)
                intent.putExtra(KEY_SAMPLE_INDEX, p1)
                this@MainActivity.startActivity(intent)
            }
        }

    }

    inner class VH(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var button : Button = itemView.findViewById(R.id.button)
    }

}
