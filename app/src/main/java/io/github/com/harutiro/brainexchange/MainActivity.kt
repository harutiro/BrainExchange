package io.github.com.harutiro.brainexchange

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.github.com.harutiro.brainexchange.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply { setContentView(this.root) }

        binding.mainFav.setOnClickListener {
            val intent = Intent(this,StarterActivity::class.java)
            startActivity(intent)
        }



    }
}