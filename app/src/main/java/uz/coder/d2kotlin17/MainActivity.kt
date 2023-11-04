package uz.coder.d2kotlin17

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.viewbinding.BuildConfig
import uz.coder.d2kotlin17.databinding.ActivityMainBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var filePath:String
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btn.setOnClickListener{
            val file = try {
                creatNewFile()
            }catch (e:Exception){
                null
            }

            takeCaptureMetod()
        }

    }


private fun takeCaptureMetod(){
    // bizga metod faylni olib keldi va u IOExseption da try catchdan foydalanamiz
    val file = try {
        // agar fayl rosta bolsa try
        creatNewFile()
    }catch (e:Exception){
        // agar bo'lmasa catch qaytarib yuboramiz
        return
    }

    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    intent.resolveActivity(packageManager)
    val uri = file?.let {
        FileProvider.getUriForFile(this,uz.coder.d2kotlin17.BuildConfig.APPLICATION_ID ,
            it
        )
    }
    intent.putExtra(MediaStore.EXTRA_OUTPUT,uri)
    startActivityForResult(intent,1)


}


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data==null) return
        if (::filePath.isLateinit){
            binding.apply {
                image.setImageURI(Uri.fromFile(File(filePath)))
            }
        }
    }











// bu metod fayl qaytaradi va fayl bo'sh yoki mavjud bolmasligi mumkin shuning uchun uni Throws orqali IOExseption qilib qo'yamiz
    @Throws(IOException::class)
    fun creatNewFile(): File? {
        // bu yerda fayilimiz yaratiladi timePathname bizga unikal takrorlanmagan nomni yaratishga yordam beradi
        val timePathName = SimpleDateFormat("yyMMdd_HHmmss", Locale.US).format(Date())
        // fileStorge bu bitta fayl qnday deysizmi bu papka (Environment. ichidagi Pictures nomli papka)
        // bu nega kerak deysizmi chunki bizni img fayilimiz ushbu Pictures ichida saqlanadi
        val fileStorge: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        //prefix bu faylimiz nomi desak xam bo'ladi
    //suffix fayilimizning farmati misol uchun docx,txt , jpg biz rasimni olmoqchimiz shuning uchun u .jpg farmatda bo'ladi
        val file = File.createTempFile("Rasm_{$timePathName}_", ".jpg", fileStorge).apply {
            filePath = absolutePath
        }
    //va bu yaratilgan faylni return qilib qaytaramiz
        return file
    }
}