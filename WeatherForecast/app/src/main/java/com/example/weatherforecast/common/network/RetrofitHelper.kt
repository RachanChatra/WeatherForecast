package com.example.myapplication.common.network

import com.example.myapplication.common.constants.ApiConstants
import com.example.weatherforecast.common.util.LogUtils
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Helper class to provide retrofit instance
 */
class RetrofitHelper {

    private var retrofit: Retrofit? = null

    fun getRetrofit(): Retrofit?{
        if (retrofit == null){

            val okHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
                val request = chain.request()
                val requestBody = getRequestBody(request)
                val newRequest = request.newBuilder().build()
                logRequest(newRequest, requestBody)

                val response = chain.proceed(newRequest)
                val responseString = response.body()?.string()
                logResponse(response, responseString!!)
                response.newBuilder()
                    .body(ResponseBody.create(response.body()?.contentType(), responseString))
                    .build()
            }.build()

            retrofit = Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }

    /**
     * This method will log the request sent to the server
     *
     * @param request Request Object
     */
    private fun logRequest(request: Request, requestBody: String) {
        LogUtils.d("REQUEST URL : " + request.url())
        LogUtils.d("REQUEST METHOD : " + request.method())
        LogUtils.d("REQUEST HEADER : " + request.headers())
        LogUtils.d("REQUEST BODY : $requestBody")
    }

    private fun getRequestBody(request: Request) : String{
        val buffer = Buffer()
        request.body()?.writeTo(buffer)
        return buffer.readUtf8()
    }

    /**
     * This method will log the response received from the server
     *
     * @param response Response Object
     * @param responseString Response String
     */
    private fun logResponse(response: Response, responseString: String) {
        LogUtils.d("RESPONSE URL : " + response.request().url())
        LogUtils.d("RESPONSE HEADER : " + response.headers())
        LogUtils.d("RESPONSE BODY : " + getPrettyPrintedJson(responseString))
    }

    private fun getPrettyPrintedJson(unformattedString: String): String {
        var prettyJsonString: String
        prettyJsonString = try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            val jp = JsonParser()
            val je = jp.parse(unformattedString)
            gson.toJson(je)

        } catch (ignored: JsonSyntaxException) {
            unformattedString
        }

        return prettyJsonString
    }
}
