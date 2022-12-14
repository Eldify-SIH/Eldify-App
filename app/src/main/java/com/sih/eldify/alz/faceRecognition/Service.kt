package com.sih.eldify.alz.faceRecognition

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface Service {
    @Multipart
    @POST("/upload")
    fun postImage(@Part image: MultipartBody.Part?, @Part("name") name: RequestBody?): Call<ResponseBody?>?
}