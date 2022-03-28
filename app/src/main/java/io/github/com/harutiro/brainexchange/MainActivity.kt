package io.github.com.harutiro.brainexchange

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.AppLaunchChecker
import com.cloudinary.android.MediaManager
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import io.github.com.harutiro.brainexchange.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply { setContentView(this.root) }

        //＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝初回起動チェック
        if(!AppLaunchChecker.hasStartedFromLauncher(this)){
            AlertDialog.Builder(this) // FragmentではActivityを取得して生成
                .setTitle("ようこそ")
                .setMessage("ようこそ、ここではキャンプで書いた脳内シートを保存して、プロフ帳のように保存ができます。" +
                        "初めに、あなたのプロフィールを登録しましょう。")
                .setPositiveButton("OK") { _, _ ->
                    val intent = Intent(this, StarterActivity::class.java)
                    intent.putExtra("newInstall",true)
                    startActivity(intent)
                }
                .show()
        }
        AppLaunchChecker.onActivityCreate(this)

        val config = mapOf(
            "cloud_name" to "dlg3xe2l2",
            "api_key" to "693697224285166",
            "api_secret" to "OTkrTsmgoXdEyBEtw2gTjiOs9oo"
        )
        MediaManager.init(this, config);

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
                            // バーコードリーダの設定を作成
                            val options = ScanOptions()
                            // 画面回転のオンオフ trueで回転しない，falseで回転する
                            options.setOrientationLocked(true)
                            // 読み込み画面で表示されるテキストを設定
                            options.setPrompt("枠内にバーコードを収めてください")
                            // バーコードリーダの立ち上げ
                            barcodeLauncher.launch(options)
                        }
                        else -> {}
                    }
                }
                .show()
        }




        binding.mainView.setOnClickListener {
            val intent = Intent(this, qrCodeViewActivity::class.java)
            startActivity(intent)
        }

        binding.mainEdit.setOnClickListener {
            val intent = Intent(this, StarterActivity::class.java)
            startActivity(intent)
        }

        binding.mainRead.setOnClickListener {
            // バーコードリーダの設定を作成
            val options = ScanOptions()
            // 画面回転のオンオフ trueで回転しない，falseで回転する
            options.setOrientationLocked(true)
            // 読み込み画面で表示されるテキストを設定
            options.setPrompt("枠内にバーコードを収めてください")
            // バーコードリーダの立ち上げ
            barcodeLauncher.launch(options)
        }





    }

    // バーコードの読み込み後の処理．フィールドとして定義すること
    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
        // 読み込んだ内容がnull，つまり何も読み込まなかったときの処理
        if (result.contents == null){
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
        } else{
            // 何かしらを読み込んだときの処理

            val intent = Intent(this,NewFlendRegisterActivity::class.java)
            intent.putExtra("getYourProfile", result.contents)
            Log.d("debag",result.contents)
            startActivity(intent)
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