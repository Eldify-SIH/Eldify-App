package com.sih.eldify.alz.faceRecognition

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.FileProvider
import com.sih.eldify.R
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.File
import java.io.IOException

class FaceRecognitionActivity : AppCompatActivity() {
    var mCurrentPhotoPath: String? = null
    var imageName: String? = null
    var name: String? = null
    var mProgressDialog: ProgressDialog? = null
    var service: Service? = null
    private val cameraRequestId  = 1222

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_recognition)
        overridePendingTransition(R.anim.right_to_left_slide_in, R.anim.right_to_left_slide_out)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        MY_CAMERA_REQUEST_CODE)
            }
        }
    }

    fun onRegisterFaceClick(view: View?) {
        val alert = AlertDialog.Builder(this)
        val edittext = EditText(applicationContext)
        alert.setTitle("Enter person's name: ")
        alert.setView(edittext)
        alert.setPositiveButton("TAKE PHOTO") { _, _ ->
            name = edittext.text.toString()
            Log.d("mCurrent", name!!)
            takePhoto()
        }
        alert.setNegativeButton("CANCEL") { _, _ -> }
        alert.show()
    }

    fun onRecognizeFaceClick(view: View?) {

    }

    private fun takePhoto() {
        val cameraInt = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraInt,cameraRequestId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == cameraRequestId){
            /**save to Image In layout*/
            val images:Bitmap = data?.extras?.get("data") as Bitmap
            saveBitmap(this, images, Bitmap.CompressFormat.JPEG, "image/jpeg","myimage" )
        }
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            sendImageToServerDatabase()
        }
    }

    @Throws(IOException::class)
    fun saveBitmap(
        context: Context, bitmap: Bitmap, format: Bitmap.CompressFormat,
        mimeType: String, displayName: String
    ): Uri {

        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
        }

        var uri: Uri? = null

        return runCatching {
            with(context.contentResolver) {
                insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)?.also {
                    uri = it // Keep uri reference so it can be removed on failure

                    openOutputStream(it)?.use { stream ->
                        if (!bitmap.compress(format, 95, stream))
                            throw IOException("Failed to save bitmap.")
                    } ?: throw IOException("Failed to open output stream.")

                } ?: throw IOException("Failed to create new MediaStore record.")
            }
        }.getOrElse {
            uri?.let { orphanUri ->
                // Don't leave an orphan entry in the MediaStore
                context.contentResolver.delete(orphanUri, null, null)
            }

            throw it
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                name,  /* prefix */
                ".jpg",  /* suffix */
                storageDir /* directory */
        )
        imageName = image.name
        imageName = image.name.substring(0, image.name.length - 4)
        Log.d("mCurrent", imageName!!)
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        Log.d("mCurrentPhoto", image.absolutePath)
        return image
    }

    private fun sendImageToServerDatabase() {
        val FILE_DIR = "/storage/emulated/0/Android/data/com.sih.eldify/files/Pictures/"
        val f = File("$FILE_DIR$imageName.jpg")
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

//        service = Retrofit.Builder().baseUrl("http://192.168.43.110:5000/")
//                .client(client).build()
//                .create(Service::class.java)
//        val reqFile = RequestBody.create("image/*".toMediaTypeOrNull(), f)
//        val body = MultipartBody.Part.createFormData("test", f.name, reqFile)
//        val name = RequestBody.create("text/plain".toMediaTypeOrNull(), "upload_test")
//        val req = service?.postImage(body, name)
//        mProgressDialog = ProgressDialog(this@FaceRecognitionActivity)
//        mProgressDialog?.setTitle("Loading...")
//        mProgressDialog?.setMessage("Please wait")
//        mProgressDialog?.show()
//        req?.enqueue(object : Callback<ResponseBody>, retrofit2.Callback<ResponseBody?> {
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                try {
//                    mProgressDialog!!.dismiss()
//                    val responseStr = response.body()!!.string()
//                    responseDialog(responseStr)
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//                Log.d("Response Flask", response.message() + "")
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                t.printStackTrace()
//                mProgressDialog!!.dismiss()
//                Toast.makeText(applicationContext, t.toString() + "", Toast.LENGTH_LONG).show()
//            }
//        })
    }

    fun responseDialog(response: String?) {
        val alert = AlertDialog.Builder(this)
        alert.setTitle(response)
        alert.setNegativeButton("OK") { _, _ -> }
        alert.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera Permission Granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun onBackPressed(view: View?) {
        finish()
        overridePendingTransition(R.anim.left_to_right_slide_in, R.anim.left_to_right_slide_out)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.left_to_right_slide_in, R.anim.left_to_right_slide_out)
    }

    companion object {
        const val REQUEST_TAKE_PHOTO = 1
        private const val REQUEST_WRITE_PERMISSION = 786
        private const val MY_CAMERA_REQUEST_CODE = 100
    }
}