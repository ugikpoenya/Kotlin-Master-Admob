package com.example.masteradmob

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import com.example.masteradmob.databinding.ActivityMainBinding
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdmobAds()

        binding.btnBanner.setOnClickListener {
            initAdmobBanner()
        }

        binding.btnNativeMedium.setOnClickListener {
            initAdmobNativeMedium()
        }

        binding.btnNativeSmall.setOnClickListener {
            initAdmobNativeSmall()
        }

        binding.btnInterstitial.setOnClickListener {
            showAdmobInterstitial()
        }
    }

    fun initAdmobAds(){
        MobileAds.initialize(this) {
            binding.txtLog.append("\n Admob Ads Initialized")
            initAdmobInterstitial()
        }
    }

    fun initAdmobBanner(){
        binding.txtLog.append("\n Admob Banner Init")
        binding.lyBannerAds.removeAllViews()
        val adView = AdView(this)
        binding.lyBannerAds.addView(adView)
        adView.adUnitId = getString(R.string.banner_ad_unit_id)
        adView.adSize =getAdSize()
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                binding.txtLog.append("\n Admob Banner loaded")
            }
            override fun onAdFailedToLoad(adError: LoadAdError) {
                binding.txtLog.append("\n Admob Banner failed to load")
            }
        }
    }

    private fun getAdSize(): AdSize? {
        val display = windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val widthPixels = outMetrics.widthPixels.toFloat()
        val density = outMetrics.density
        val adWidth = (widthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
    }

    var admobInterstitial: InterstitialAd?=null
    fun initAdmobInterstitial(){
        binding.txtLog.append("\n Init Admob Interstitial ")
        admobInterstitial = InterstitialAd(this)
        admobInterstitial?.adUnitId= getString(R.string.interstitial_ad_unit_id)
        admobInterstitial?.loadAd(AdRequest.Builder().build())
        admobInterstitial?.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                binding.txtLog.append("\n Interstitial admob loaded")
            }
            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                binding.txtLog.append("\n Interstitial admob failed to load")
            }
            override fun onAdClosed() {
                super.onAdClosed()
                admobInterstitial?.loadAd(AdRequest.Builder().build())
            }
        }
    }

    fun showAdmobInterstitial(){
        if (admobInterstitial != null && admobInterstitial!!.isLoaded) {
            admobInterstitial?.show()
            binding.txtLog.append("\n Interstitial ads Show")
        }else binding.txtLog.append("\n Interstitial ads not loaded")
    }

    fun initAdmobNativeMedium(){
        binding.txtLog.append("\n Admob Native medium init ")
        binding.lyBannerAds.removeAllViews()
        val builder = AdLoader.Builder(this, getString(R.string.native_ad_unit_id))
        builder.forNativeAd { nativeAd ->
            val adView = layoutInflater
                .inflate(R.layout.native_ads_layout_admob_medium, null) as NativeAdView
            populateAdmobNativeMedium(nativeAd,adView)
            binding.lyBannerAds.addView(adView)
        }
        val videoOptions = VideoOptions.Builder().setStartMuted(false).build()
        val adOptions = NativeAdOptions.Builder().setVideoOptions(videoOptions).build()
        builder.withNativeAdOptions(adOptions)
        val adLoader = builder.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(errorCode: Int) {
                binding.txtLog.append("\n Failed to load Admob native medium " + errorCode)
            }
        }).build()
        adLoader.loadAd(AdRequest.Builder().build())
    }

    fun initAdmobNativeSmall(){
        binding.txtLog.append("\n Admob Native small init ")
        binding.lyBannerAds.removeAllViews()
        val builder = AdLoader.Builder(this, getString(R.string.native_ad_unit_id))
        builder.forNativeAd { nativeAd ->
            val adView = layoutInflater
                .inflate(R.layout.native_ads_layout_admob_small, null) as NativeAdView
            populateAdmobNativeSmall(nativeAd,adView)
            binding.lyBannerAds.addView(adView)
        }
        val adLoader = builder.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(errorCode: Int) {
                binding.txtLog.append("\n Failed to load Admob native small " + errorCode)
            }
        }).build()
        adLoader.loadAd(AdRequest.Builder().build())
    }
}