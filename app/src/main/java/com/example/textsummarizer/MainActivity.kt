package com.example.textsummarizer

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    private var summaryValue: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val predict: Button =findViewById(R.id.button)
        try{
            predict.setOnClickListener {
                predict.setEnabled(false);
             //   summaryValue!!.text="Hey Human let me think!!"
               var domain: TextView=findViewById(R.id.domainText)
                val feedValue: TextView=findViewById(R.id.editTextTextMultiLine)
                summaryValue=findViewById(R.id.textView3)
                //val textSummary= MainActivity.TextSummarizer(feedValue.text.toString())
                Textsummarize(feedValue.text.toString(),domain.text.toString(),predict)
                //predict.setEnabled(true)
            }
        }catch(e: Exception){
            Log.w("exception for app crash",e.toString())
            predict.setEnabled(true)
        }





    }
    fun Textsummarize(textReqVal:String,tdomain:String,predButton:Button) {
        summaryValue!!.text ="Hey Human let me think, please wait for a moment !!"
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            //.writeTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .build()
        val retrofit = Retrofit.Builder().baseUrl(tdomain).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create(Summary::class.java)
        val call = service.GetSummary(textReqVal)
        Log.i("TAG","call "+call)
        call.enqueue(object : Callback<SummaryResponse> {
            override fun onResponse(
                call: Call<SummaryResponse>,response: Response<SummaryResponse>) {
                if (response != null) {
                    if (response.code() == 200) {
                        val summaryResponseValue = response.body()

                        summaryValue!!.text =summaryResponseValue.summary.toString()


                    }
                }
                predButton.setEnabled(true);
            }

            override fun onFailure(call: Call<SummaryResponse>, t: Throwable) {
                summaryValue!!.text = t.message.toString()

            }
        })


    }
    companion object{

        //private var domain: TextView?=null
        var BaseUrl = "domain!!.text.toString()"


    }
}