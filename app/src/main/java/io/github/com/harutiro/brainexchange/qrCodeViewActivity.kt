package io.github.com.harutiro.brainexchange

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.journeyapps.barcodescanner.BarcodeEncoder
import io.github.com.harutiro.brainexchange.databinding.ActivityNewFlendRegisterBinding
import io.github.com.harutiro.brainexchange.databinding.ActivityQrCodeViewBinding
import io.github.com.harutiro.brainexchange.databinding.ActivityStarterBinding
import io.github.com.harutiro.brainexchange.date.ProfileDateClass

class qrCodeViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQrCodeViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrCodeViewBinding.inflate(layoutInflater).apply { setContentView(this.root) }

        val sp: SharedPreferences = getSharedPreferences("DateStore", Context.MODE_PRIVATE)

        // データをバーコードに変更するためのインスタンス
        val barcodeEncoder = BarcodeEncoder()

        try{
            // ImageViewにBitmap形式の画像を設定
            binding.qrCodeViewQrImageView.setImageBitmap(
                // Bitmap形式でQRコードを生成
                barcodeEncoder.encodeBitmap(
                    Gson().toJson(sp.getString("myProfile","")), BarcodeFormat.QR_CODE, 400, 400, mapOf(
                        EncodeHintType.CHARACTER_SET to "UTF-8"))
            )
        } catch (e:Exception){
            // 生成に失敗したらToastで通知
            // ex) データが何も無い，エンコード可能なデータ量を超えた，etc...
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }

        binding.qrCodeViewFinishButton.setOnClickListener {
            finish()
        }
    }
}