package ub.es.motorent.app.model

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object URL {

    private const val isDeploy = true // Change to true for deployment

    private const val urlTest = "https://motorent-apitest.herokuapp.com"
    private const val urlDeploy = "https://motorent-deploy.herokuapp.com"

    fun getUrl() : String{
        return if (isDeploy){ urlDeploy } else { urlTest }
    }

}

object ServiceBuilder {
    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL.getUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
}