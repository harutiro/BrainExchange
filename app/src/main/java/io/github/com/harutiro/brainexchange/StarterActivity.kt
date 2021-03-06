package io.github.com.harutiro.brainexchange

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_OPEN_DOCUMENT
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View.VISIBLE
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.api.load
import com.airbnb.lottie.utils.Logger.error
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.journeyapps.barcodescanner.BarcodeEncoder
import io.github.com.harutiro.brainexchange.databinding.ActivityStarterBinding
import io.github.com.harutiro.brainexchange.date.ProfileDateClass
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.text.isNullOrEmpty as isNullOrEmpty1


class StarterActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1234
    }

    private lateinit var binding: ActivityStarterBinding

    val editor: SharedPreferences.Editor? = null


    private val TAG = "Upload ###"

    var photoState = false

    //脳内シート画像
    lateinit var resitoImage: Bitmap
    lateinit var currentPhotoUri : Uri
    var uriString = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStarterBinding.inflate(layoutInflater).apply { setContentView(this.root) }

        requestPermission()

        if(intent.getBooleanExtra("newInstall",false)){
            AlertDialog.Builder(this) // FragmentではActivityを取得して生成
                .setTitle("操作方法")
                .setMessage("脳内シートのところを押すと写真をとることができます。\nまた、下の項目では、気になるワードがあれば押してみてください。")
                .setPositiveButton("OK") { _, _ ->
                }
                .show()
        }

//      シェアプリのインスタンス化
        val sp: SharedPreferences = getSharedPreferences("DateStore", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sp.edit()

//        はめ込み部分
        if(!sp.getString("myProfile","").isNullOrEmpty1()){
            val getMyProfile: ProfileDateClass = Gson().fromJson(sp.getString("myProfile",""), ProfileDateClass::class.java)
            binding.starterUserNameEditText.setText(getMyProfile.userName)
            binding.starterFacebookIdEditText.setText(getMyProfile.facebookId)
            if(getMyProfile.brainImageUrl.isNotEmpty()){
                binding.starterBrainImage.load(getMyProfile.brainImageUrl)
            }

            setChip(getMyProfile.favNumbers)

        }else{
            setChip("")
        }






        binding.staerterFav.setOnClickListener {
            if(photoState){
                MediaManager.get().upload(currentPhotoUri).callback(object : UploadCallback {
                    override fun onStart(requestId: String) {
                        Log.d(TAG, "onStart: " + "started")
                    }

                    override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                        Log.d(TAG, "onStart: " + "uploading")
                    }

                    override fun onSuccess(requestId: String, resultData: Map<*, *>?) {
                        Log.d(TAG, "onStart: " + "usuccess" + resultData?.get("secure_url"))

                        val myProfile = ProfileDateClass()
                        myProfile.userName = binding.starterUserNameEditText.text.toString()
                        myProfile.facebookId = binding.starterFacebookIdEditText.text.toString()
                        myProfile.brainImageUrl = resultData?.get("secure_url").toString()

                        val getChipCheckedList = binding.starterChipGroup.checkedChipIds
                        for(i in getChipCheckedList){
                            val favString = findViewById<Chip>(i).text.toString()
                            Log.d("debag", favString)
                            myProfile.favNumbers += "$favString,"
                        }

                        //            gsonでmyProfileDateClassをJson化してStringで保存
                        val jsonText = Gson().toJson(myProfile)
                        editor?.putString("myProfile",jsonText)
                        editor?.apply()

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
//                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                        }

                        binding.starterQrMessageTextView.text = "QRコードが作成されました。"

                    }

                    override fun onError(requestId: String?, error: ErrorInfo) {
                        Log.d(TAG, "onStart: $error")
                    }

                    override fun onReschedule(requestId: String?, error: ErrorInfo) {
                        Log.d(TAG, "onStart: $error")
                    }
                }).dispatch()
            }else{
                val getMyProfile: ProfileDateClass = Gson().fromJson(sp.getString("myProfile",""), ProfileDateClass::class.java)

                val myProfile = ProfileDateClass()
                myProfile.userName = binding.starterUserNameEditText.text.toString()
                myProfile.facebookId = binding.starterFacebookIdEditText.text.toString()
                myProfile.brainImageUrl = getMyProfile.brainImageUrl

                val getChipCheckedList = binding.starterChipGroup.checkedChipIds
                for(i in getChipCheckedList){
                    val favString = findViewById<Chip>(i).text.toString()
                    Log.d("debag", favString)
                    myProfile.favNumbers += "$favString,"
                }

                //            gsonでmyProfileDateClassをJson化してStringで保存
                val jsonText = Gson().toJson(myProfile)
                editor?.putString("myProfile",jsonText)
                editor?.apply()

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
//                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }

                binding.starterQrMessageTextView.text = "QRコードが作成されました。"
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
            val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            val cameraRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
            if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
                if (cameraRationale) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CODE)

                }else{
                    val snackbar = Snackbar.make(findViewById(android.R.id.content),"カメラの権限が許可されていません", Snackbar.LENGTH_SHORT)
                    snackbar.view.setBackgroundResource(R.color.error)
                    snackbar.show()
                }
            }else{
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
            photoState = true

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

    private fun requestPermission() {
        val permissionAccessCoarseLocationApproved =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED

        if (permissionAccessCoarseLocationApproved) {
        } else {
            // 位置情報の権限が無いため、許可を求める
            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                ),
                PERMISSION_REQUEST_CODE
            )
        }
    }
}