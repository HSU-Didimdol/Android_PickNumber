package com.example.picknumberproject.view.extension

import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.picknumberproject.databinding.ContentLoadStateBinding
import java.net.ConnectException

/**
 * [ContentLoadStateBinding]의 프로그래스바, 실패 텍스트, 예약 실패 글자 등을 필요한 순간에 보이게하는 리스너를 설정한다.
 */
fun <A : ListAdapter<T, VH>, T, VH> ContentLoadStateBinding.setListeners(
    adapter: A
) {

}

/**
 * 아이템이 첫 번째 인덱스에 추가되면 리스트 최상단으로 스크롤하는 옵저버를 등록한다.
 *
 * 추가적으로 아이템 순서가 변한 경우에 최상단으로 스크롤 하려면 [whenItemRangeMoved]를 `true`로 전달하면 된다.
 */
fun <T : Any, VH : RecyclerView.ViewHolder> ListAdapter<T, VH>.registerObserverForScrollToTop(
    recyclerView: RecyclerView,
    whenItemInsertedFirst: Boolean = true,
    whenItemRangeMoved: Boolean = false
) {
    this.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            if (whenItemInsertedFirst && positionStart == 0) {
                recyclerView.scrollToPosition(0)
            }
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            if (whenItemRangeMoved) {
                recyclerView.scrollToPosition(0)
            }
        }
    })
}
