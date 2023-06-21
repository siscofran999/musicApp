package com.sisco.musicapp.ui

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.sisco.musicapp.R
import com.sisco.musicapp.data.model.ItemTrack
import com.sisco.musicapp.data.model.RequestArtist
import com.sisco.musicapp.data.model.RequestToken
import com.sisco.musicapp.databinding.ActivityMainBinding
import com.sisco.musicapp.ui.adapter.MainAdapter
import com.sisco.musicapp.ui.adapter.SearchAdapter
import com.sisco.musicapp.utils.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModel()
    private val rxDisposer: RxDisposer by inject()
    private val adapter by lazy { MainAdapter { item, name -> itemClick(item, name) } }
    private val searchAdapter by lazy { SearchAdapter() }

    private var token: String = ""
    private var mediaPlayer: MediaPlayer? = null
    private var timer: Timer? = null
    private var time = 0
    private var duration = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rv.adapter = adapter
        binding.rvSearch.adapter = searchAdapter
        binding.viewControl.seekbar.isEnabled = false
        rxDisposer.bind(lifecycle)

        mainViewModel.setTokenRequestModel(
            RequestToken(
                GlobalConst.GRANT_TYPE,
                GlobalConst.CLIENT_ID,
                GlobalConst.CLIENT_SECRET
            )
        )

        mainViewModel.getToken.observe(this) { state ->
            state.apply {
                doIfLoading {
                    Log.i("TAG", "onCreate: loading")
                }
                doIfError { throwable, _ ->
                    Toast.makeText(this@MainActivity, "Error $throwable", Toast.LENGTH_SHORT).show()
                    Log.i("TAG", "onCreate: $throwable")
                }
                doIfFailed { _, errorMessage, _ ->
                    Toast.makeText(this@MainActivity, "Failed $errorMessage", Toast.LENGTH_SHORT).show()
                    Log.i("TAG", "onCreate: $errorMessage")
                }
                doIfSuccess { response ->
                    Log.i("TAG", "onCreate: $response")
                    token = response.access_token
                    mainViewModel.setTokenRequest(token)
                }
            }
        }

        mainViewModel.getList.observe(this) { state ->
            state.apply {
                doIfLoading {
                    binding.progressBar.visibility = View.VISIBLE
                    Log.i("TAG", "list: loading")
                }
                doIfError { throwable, _ ->
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this@MainActivity, "Error $throwable", Toast.LENGTH_SHORT).show()
                    Log.i("TAG", "error list: $throwable")
                }
                doIfFailed { _, errorMessage, _ ->
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this@MainActivity, "Failed $errorMessage", Toast.LENGTH_SHORT).show()
                    Log.i("TAG", "failed list: $errorMessage")
                }
                doIfSuccess { response ->
                    binding.progressBar.visibility = View.GONE
                    submitData(response)
                    Log.i("TAG", "success list: $response")
                }
            }
        }

        mainViewModel.searchArtist.observe(this) { state ->
            state.apply {
                doIfLoading {
                    binding.progressBar.visibility = View.VISIBLE
                    Log.i("TAG", "list: loading")
                }
                doIfError { throwable, _ ->
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this@MainActivity, "Error $throwable", Toast.LENGTH_SHORT).show()
                    Log.i("TAG", "error list: $throwable")
                }
                doIfFailed { _, errorMessage, _ ->
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this@MainActivity, "Failed $errorMessage", Toast.LENGTH_SHORT).show()
                    Log.i("TAG", "failed list: $errorMessage")
                }
                doIfSuccess { response ->
                    binding.progressBar.visibility = View.GONE
                    Log.i("TAG", "success search: $response")
                    searchAdapter.submitList(response)
                    binding.rv.visibility = View.GONE
                    binding.rvSearch.visibility = View.VISIBLE
                }
            }
        }

        binding.viewControl.imgPlayPause.setOnClickListener {
            if (mediaPlayer?.isPlaying == true) {
                pauseAudio()
                binding.viewControl.imgPlayPause.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_24))
            }else {
                if(time >= duration) {
                    playAudio()
                    time = 0
                    runTimer()
                }else {
                    playAudio()
                    binding.viewControl.imgPlayPause.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause_24))
                }
            }
        }

        binding.edtSearch.addTextChangedListener {
            if (it?.length!! < 4) {
                if (it.isEmpty()){
                    binding.txvError.visibility = View.GONE
                }else {
                    binding.txvError.visibility = View.VISIBLE
                }
                binding.txvError.text = getString(R.string.label_min_input)
                binding.rv.visibility = View.VISIBLE
                binding.rvSearch.visibility = View.GONE
            }else {
                binding.txvError.visibility = View.GONE
                // request get Artist
                mainViewModel.setTokenSearch(RequestArtist(token, binding.edtSearch.text.toString()))
                binding.rv.visibility = View.GONE
            }
        }

        binding.viewControl.imgClose.setOnClickListener {
            stopAudio()
            binding.viewControl.root.visibility = View.GONE
        }
    }

    private fun submitData(response: List<ItemTrack>) {
        adapter.submitList(response)
    }

    private fun itemClick(item: String, name: String) {
        time = 0
        binding.viewControl.root.visibility = View.VISIBLE
        binding.viewControl.txvTitle.text = name
        stopAudio()
        mediaPlayer = MediaPlayer.create(this, Uri.parse(item))
        duration = (mediaPlayer?.duration?.div(1000)) ?: 0
        binding.viewControl.seekbar.max = duration
        playAudio()
        if (timer != null){
            timer?.cancel()
        }
        runTimer()
    }

    private fun runTimer() {
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    if (time >= duration) {
                        stopAudio()
                        timer?.cancel()
                    }else {
                        time += 1
                        updateSeekBar()
                    }
                }
            }
        }, 0, 1000) // Update SeekBar every 1 second
        binding.viewControl.seekbar.progress = 0
        binding.viewControl.imgPlayPause.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause_24))
    }

    private fun updateSeekBar() {
        if (mediaPlayer?.isPlaying == true) {
            binding.viewControl.seekbar.progress = time
        }
    }

    private fun playAudio() {
        if (mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
        }
    }

    private fun pauseAudio() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
    }

    private fun stopAudio() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.stop()
            mediaPlayer?.prepare()
            binding.viewControl.imgPlayPause.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_24))
        }
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        timer?.cancel()
        super.onDestroy()
    }
}