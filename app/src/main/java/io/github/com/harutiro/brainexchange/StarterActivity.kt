package io.github.com.harutiro.brainexchange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem

class StarterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starter)


//        戻る→の表示
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // android.R.id.home に戻るボタンを押した時のidが取得できる
        if (item.itemId == android.R.id.home) {
            // 今回はActivityを終了させている
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}