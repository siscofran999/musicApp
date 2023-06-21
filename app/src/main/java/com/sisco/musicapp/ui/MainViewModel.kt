package com.sisco.musicapp.ui

import androidx.lifecycle.*
import com.sisco.musicapp.data.model.*
import com.sisco.musicapp.data.repository.MainRepository
import com.sisco.musicapp.utils.RxDisposer
import com.sisco.musicapp.utils.UiState
import com.sisco.musicapp.utils.addToDisposer
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject

class MainViewModel(
    private val mainRepository: MainRepository,
    private val rxDisposer: RxDisposer
) : ViewModel() {

    private val tokenRequestModel = MutableLiveData<RequestToken>()
    fun setTokenRequestModel(request: RequestToken) {
        this.tokenRequestModel.value = request
    }

    private val resultData = BehaviorSubject.create<UiState<ResponseToken>>()
    val getToken: LiveData<UiState<ResponseToken>> =
        tokenRequestModel.switchMap { tokenRequestModel ->
            resultData.onNext(UiState.Loading)
            mainRepository.fetchToken(tokenRequestModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.access_token != "") {
                        resultData.onNext(UiState.Success(response))
                    }
                }, { t ->
                    resultData.onNext(UiState.Error(t))
                })
                .addToDisposer(rxDisposer)
            resultData.toFlowable(BackpressureStrategy.BUFFER).toLiveData()
        }

    private val tokenRequest = MutableLiveData<String>()
    fun setTokenRequest(request: String) {
        this.tokenRequest.value = request
    }

    private val resultDataList = BehaviorSubject.create<UiState<List<ItemTrack>>>()
    val getList: LiveData<UiState<List<ItemTrack>>> = tokenRequest.switchMap { tokenRequest ->
        resultDataList.onNext(UiState.Loading)
        mainRepository.fetchList(tokenRequest)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                val mResponse = response.tracks.items.filter { itemTrack ->
                    itemTrack.track.preview_url != null
                }
                resultDataList.onNext(UiState.Success(mResponse))
            }, { t ->
                resultDataList.onNext(UiState.Error(t))
            })
            .addToDisposer(rxDisposer)
        resultDataList.toFlowable(BackpressureStrategy.BUFFER).toLiveData()
    }

    private val tokenSearch = MutableLiveData<RequestArtist>()
    fun setTokenSearch(request: RequestArtist) {
        this.tokenSearch.value = request
    }

    private val resultDataSearch = BehaviorSubject.create<UiState<List<ItemArtist>>>()
    val searchArtist: LiveData<UiState<List<ItemArtist>>> = tokenSearch.switchMap { tokenRequest ->
        resultDataSearch.onNext(UiState.Loading)
        mainRepository.searchArtist(tokenRequest.token, tokenRequest.query)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                resultDataSearch.onNext(UiState.Success(response.artists.items))
            }, { t ->
                resultDataSearch.onNext(UiState.Error(t))
            })
            .addToDisposer(rxDisposer)
        resultDataSearch.toFlowable(BackpressureStrategy.BUFFER).toLiveData()
    }
}