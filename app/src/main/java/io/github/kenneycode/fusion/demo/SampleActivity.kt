package io.github.kenneycode.fusion.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.kenneycode.fusion.demo.fragment.SampleBasicUsage

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion
 *
 * demo list activity
 *
 */

class SimpleActivity : AppCompatActivity() {

    companion object {

        const val KEY_SAMPLE_INDEX = "KEY_SAMPLE_INDEX"

        val samples = listOf(
            Pair(R.string.sample_0, SampleBasicUsage::class.java)
        )

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)
        val sampleIndex = intent.getIntExtra(KEY_SAMPLE_INDEX, -1)
        title = getString(samples[sampleIndex].first)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content, samples[sampleIndex].second.newInstance())
        transaction.commit()
    }

}
