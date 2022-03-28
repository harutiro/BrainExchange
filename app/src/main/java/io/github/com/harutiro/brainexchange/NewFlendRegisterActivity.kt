package io.github.com.harutiro.brainexchange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import io.github.com.harutiro.brainexchange.databinding.ActivityMainBinding
import io.github.com.harutiro.brainexchange.databinding.ActivityNewFlendRegisterBinding
import io.github.com.harutiro.brainexchange.date.ProfileDateClass

class NewFlendRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewFlendRegisterBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewFlendRegisterBinding.inflate(layoutInflater).apply { setContentView(this.root) }


        val getYourProfile = intent.getStringExtra("getYourProfile")

        //        はめ込み部分
        if(!getYourProfile.isNullOrEmpty()){
            val getMyProfile: ProfileDateClass = Gson().fromJson(getYourProfile, ProfileDateClass::class.java)
            binding.newFlendUserNameTextView.text = getMyProfile.userName
            binding.newFlendFacebookIdTextView.text = getMyProfile.facebookId

            setChip(getMyProfile.favNumbers)

        }else{
            setChip("")
        }

        Log.d("debag",getYourProfile.toString())

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
            val chip = Chip(binding.newFlendChipGroup.context)
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

            binding.newFlendChipGroup.addView(chip)

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