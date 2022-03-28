package io.github.com.harutiro.brainexchange

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.journeyapps.barcodescanner.BarcodeEncoder
import io.github.com.harutiro.brainexchange.databinding.ActivityStarterBinding
import io.github.com.harutiro.brainexchange.date.ProfileDateClass

class StarterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStarterBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStarterBinding.inflate(layoutInflater).apply { setContentView(this.root) }

//      シェアプリのインスタンス化
        val sp: SharedPreferences = getSharedPreferences("DateStore", Context.MODE_PRIVATE)
        val editor = sp.edit()

//        はめ込み部分
        if(!sp.getString("myProfile","").isNullOrEmpty()){
            val getMyProfile: ProfileDateClass = Gson().fromJson(sp.getString("myProfile",""), ProfileDateClass::class.java)
            binding.starterUserNameEditText.setText(getMyProfile.userName)
            binding.starterFacebookIdEditText.setText(getMyProfile.facebookId)

            setChip(getMyProfile.favNumbers)

        }else{
            setChip("")
        }






        binding.staerterFav.setOnClickListener {
            val myProfile = ProfileDateClass()
            myProfile.userName = binding.starterUserNameEditText.text.toString()
            myProfile.facebookId = binding.starterFacebookIdEditText.text.toString()

            val getChipCheckedList = binding.starterChipGroup.checkedChipIds
            for(i in getChipCheckedList){
                val favString = findViewById<Chip>(i).text.toString()
                Log.d("debag", favString)
                myProfile.favNumbers += "$favString,"
            }

//            gsonでmyProfileDateClassをJson化してStringで保存
            val jsonText = Gson().toJson(myProfile)
            editor.putString("myProfile",jsonText)
            editor.apply()

            // データをバーコードに変更するためのインスタンス
            val barcodeEncoder = BarcodeEncoder()

            try{
                // ImageViewにBitmap形式の画像を設定
                binding.starterQrImageView.setImageBitmap(
                    // Bitmap形式でQRコードを生成
                    barcodeEncoder.encodeBitmap(Gson().toJson(myProfile), BarcodeFormat.QR_CODE, 400, 400, mapOf(EncodeHintType.CHARACTER_SET to "UTF-8"))
                )
            } catch (e:Exception){
                // 生成に失敗したらToastで通知
                // ex) データが何も無い，エンコード可能なデータ量を超えた，etc...
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }

            binding.starterQrImageView.visibility = VISIBLE
            binding.starterQrMessageTextView.visibility = VISIBLE
            binding.starterFinishButton.visibility = VISIBLE
            binding.starterBackgroundView.visibility = VISIBLE
            binding.staerterFav.isEnabled = false


        }

        binding.starterFinishButton.setOnClickListener {
            finish()
        }

//        戻る→の表示
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    fun setChip(favNumbers: String) {
        val favoliteList:List<String> = listOf(
            "スポーツ",
            "アニメ",
            "開発",
            "ゲーム",
            "デザイン",
            "ドラマ",
            "映画",
            "音楽",
            "旅行",
            "Android",
            "Unity",
            "DTM",
            "iPhone",
            "WebD",
            "WebS",
            "Minecraft",
            "アニメーション",
            "メディアアート",
            "デザイナー",
            "映像制作",
            "カメラフォト",
        )

        for (i in favoliteList) {
            val chip = Chip(binding.starterChipGroup.context)
            chip.text = i

            // necessary to get single selection working
            chip.isCheckable = true
            chip.isClickable = true

            val favLists = favNumbers.split(",")
            for(j in favLists){
                if(i == j){
                    chip.isChecked = true
                    break
                }
            }

            binding.starterChipGroup.addView(chip)

        }
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