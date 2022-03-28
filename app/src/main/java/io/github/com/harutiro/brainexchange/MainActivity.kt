package io.github.com.harutiro.brainexchange

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import io.github.com.harutiro.brainexchange.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply { setContentView(this.root) }

        binding.mainFav.setOnClickListener {
            val strList = arrayOf("QRコードを表示","QRコードを読み取る")

            AlertDialog.Builder(this) // FragmentではActivityを取得して生成
                .setTitle("リスト選択ダイアログ")
                .setItems(strList) { _, which ->
                    when (which) {
                        0 -> {
                            val intent = Intent(this,qrCodeViewActivity::class.java)
                            startActivity(intent)
                        }
                        1 -> {
                            val intent = Intent(this,NewFlendRegisterActivity::class.java)
                            startActivity(intent)
                        }
                        else -> {}
                    }
                }
                .show()
        }



    }


    //　アプリバーの部分
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.profile_settings -> {
            val intent = Intent(this,StarterActivity::class.java)
            startActivity(intent)

            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_activity_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }
}