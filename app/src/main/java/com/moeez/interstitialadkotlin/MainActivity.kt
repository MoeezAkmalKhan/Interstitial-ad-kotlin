package com.moeez.interstitialadkotlin

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.moeez.interstitialadkotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private var mInterstitialAd: InterstitialAd? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        MobileAds.initialize(this) {status->
            Log.e(TAG, "onCreate: status $status")
        }

        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(listOf(""))
                .build())

        loadInterstitialAd()

        binding?.showInterstitialAdBtn?.setOnClickListener {
            showInterstitialAd()
        }

    }


    private fun loadInterstitialAd() {
        val adRequestInt = AdRequest.Builder().build()
        InterstitialAd.load(this, this.getString(R.string.testInterstitialAdID),
            adRequestInt, object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    Log.i(TAG, "onAdLoaded: Ad is loaded")
                    mInterstitialAd = interstitialAd


                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error
                    Log.i(TAG, "onAdFailed: $loadAdError")
                    mInterstitialAd = null
                }
            })
    }

    private fun showInterstitialAd() {
        if (mInterstitialAd != null) {
            Log.d(TAG, "The interstitial ad was loaded, we can show")
            mInterstitialAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {

                    mInterstitialAd = null
                    loadInterstitialAd()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    super.onAdFailedToShowFullScreenContent(adError)
                    mInterstitialAd = null
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                }
            }
            mInterstitialAd!!.show(this)
        } else {
            Log.d(TAG, "The interstitial ad wasn't ready yet.")
            Toast.makeText(this, "Ad was not Added", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val TAG = "INTERSTITIAL_AD"
    }

}