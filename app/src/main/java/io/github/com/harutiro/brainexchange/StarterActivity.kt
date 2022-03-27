package io.github.com.harutiro.brainexchange

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import io.github.com.harutiro.brainexchange.databinding.ActivityStarterBinding
import io.github.com.harutiro.brainexchange.date.FavListClass
import io.github.com.harutiro.brainexchange.date.ProfileDateClass
import io.realm.RealmList
import java.util.*

class StarterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStarterBinding

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
            editor.putString("myProfile",Gson().toJson(myProfile))
            editor.apply()

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
            "Kotlin",
            "Java",
            "C#",
            "Swift",
            "HTML",
            "CSS",
            "JavaScript",
            "Ruby",
            "Ruby","Ruby","Ruby","Ruby","Ruby","Ruby","Ruby","Ruby","Ruby","Ruby","Ruby","Ruby","Ruby","Ruby","Ruby","Ruby","Ruby","Ruby","Ruby","Ruby","Ruby","Ruby","Ruby",
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