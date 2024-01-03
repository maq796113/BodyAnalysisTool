package com.example.bodyanalysistool.api


import com.example.bodyanalysistool.data.BodyAnalysisResponse
import com.example.bodyanalysistool.data.ImageDownloadedResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface MediaPipeAPI {

    @POST("/check_image_download")
    suspend fun getBooleanImageDownloaded(@Query("url") url: String): Response<ImageDownloadedResponse>

    @POST("/result")
    suspend fun getBodyAnalysis(): Response<BodyAnalysisResponse>

}