package com.example.picknumberproject.view.main.homepage

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.fragment.app.activityViewModels
import com.example.picknumberproject.databinding.FragmentHomePageBinding
import com.example.picknumberproject.view.common.ViewBindingFragment
import kotlinx.android.synthetic.main.fragment_home_page.*

class HomePageFragment(
    private val url: String
) : ViewBindingFragment<FragmentHomePageBinding>() {

    private val viewModel: HomePageViewModel by activityViewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomePageBinding
        get() = FragmentHomePageBinding::inflate

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.bind(url)

        webView.settings.apply {
            this.setSupportMultipleWindows(false) // 새창 띄우기 허용
            this.setSupportZoom(false) // 화면 확대 허용
            this.javaScriptEnabled = true
            this.javaScriptCanOpenWindowsAutomatically = true // 자바스크립트 새창 띄우기 허용
            this.loadWithOverviewMode = true // html의 컨텐츠가 웹뷰보다 클 경우 스크린 크기에 맞게 조정
            this.useWideViewPort = true // html의 viewport 메타 태그 지원
            this.builtInZoomControls = false // 화면 확대/축소 허용
            this.displayZoomControls = false
            this.cacheMode = WebSettings.LOAD_NO_CACHE // 브라우저 캐쉬 허용
            this.domStorageEnabled = true // 로컬 저장 허용
            this.databaseEnabled = true

            /**
             * This request has been blocked; the content must be served over HTTPS
             * https 에서 이미지가 표시 안되는 오류를 해결하기 위한 처리
             */
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        webView.webViewClient = WebViewClient()
        webView.loadUrl(viewModel.uiState.value.url)

    }
}