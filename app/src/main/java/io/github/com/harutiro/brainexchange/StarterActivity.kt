package io.github.com.harutiro.brainexchange

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.journeyapps.barcodescanner.BarcodeEncoder
import io.github.com.harutiro.brainexchange.databinding.ActivityStarterBinding
import io.github.com.harutiro.brainexchange.date.ProfileDateClass
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class StarterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStarterBinding

    //脳内シート画像
    lateinit var resitoImage: Bitmap
    lateinit var currentPhotoUri : Uri
    var uriString = ""

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

        binding.starterBrainImage.setOnClickListener {
            val context: Context = applicationContext

            // 保存先のフォルダー
            val cFolder: File? = context.getExternalFilesDir(Environment.DIRECTORY_DCIM)

            //        *名前関係*       //
            //　フォーマット作成
            val fileDate: String = SimpleDateFormat("ddHHmmss", Locale.US).format(Date())
            //　名前作成
            val fileName: String = String.format("CameraIntent_%s.jpg", fileDate)

            //uriの前作成
            val cameraFile: File = File(cFolder, fileName)

            //uri最終作成
            currentPhotoUri = FileProvider.getUriForFile(this, context.packageName.toString() + ".fileprovider", cameraFile)


            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri)
            launcher.launch(intent)
        }

//        戻る→の表示
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    //    タグインテントにおける戻りデータの受け取り部分
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK){
            val intent = result.data

            binding.starterBrainImage.setImageURI(currentPhotoUri)
            uriString = currentPhotoUri.toString()

        }else{
            //メディアプレイヤーに追加したデータを消去する
            contentResolver.delete(currentPhotoUri, null, null)
        }
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