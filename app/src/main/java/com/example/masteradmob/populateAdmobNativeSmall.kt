package com.example.masteradmob

import android.text.TextUtils
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView


fun populateAdmobNativeSmall(nativeAd: NativeAd, adView: NativeAdView) {
    val primaryView =adView.findViewById<TextView>(R.id.primary)
    val secondaryView =adView.findViewById<TextView>(R.id.secondary)
    val tertiaryView =adView.findViewById<TextView>(R.id.primary)

    val ratingBar =adView.findViewById<RatingBar>(R.id.rating_bar)
    ratingBar.setEnabled(false);

    val callToActionView =adView.findViewById<Button>(R.id.cta)
    val iconView =adView.findViewById<ImageView>(R.id.icon)


    val store = nativeAd.store
    val advertiser = nativeAd.advertiser
    val headline = nativeAd.headline
    val body = nativeAd.body
    val cta = nativeAd.callToAction
    val starRating = nativeAd.starRating
    val icon: NativeAd.Image? = nativeAd.icon

    val secondaryText: String

    adView.setCallToActionView(callToActionView)
    adView.setHeadlineView(primaryView)
    secondaryView.visibility = VISIBLE
    secondaryText = if (adHasOnlyStore(nativeAd)) {
        adView.setStoreView(secondaryView)
        store
    } else if (!TextUtils.isEmpty(advertiser)) {
        adView.setAdvertiserView(secondaryView)
        advertiser
    } else {
        ""
    }

    primaryView.text = headline
    callToActionView.text = cta

    //  Set the secondary view to be the star rating if available.

    //  Set the secondary view to be the star rating if available.
    if (starRating != null && starRating > 0) {
        secondaryView.visibility = GONE
        ratingBar.visibility = VISIBLE
        ratingBar.max = 5
        adView.starRatingView = ratingBar
    } else {
        secondaryView.text = secondaryText
        secondaryView.visibility = VISIBLE
        ratingBar.visibility = GONE
    }

    if (icon != null) {
        iconView.visibility = VISIBLE
        iconView.setImageDrawable(icon.drawable)
    } else {
        iconView.visibility = GONE
    }

    if (tertiaryView != null) {
        tertiaryView.text = body
        adView.bodyView = tertiaryView
    }

    adView.setNativeAd(nativeAd)
}

private fun adHasOnlyStore(nativeAd: NativeAd): Boolean {
    val store = nativeAd.store
    val advertiser = nativeAd.advertiser
    return !TextUtils.isEmpty(store) && TextUtils.isEmpty(advertiser)
}