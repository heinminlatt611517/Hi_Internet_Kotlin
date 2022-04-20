package com.hti.hiinternet.Fragments

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.Util
import com.hti.hiinternet.R
import com.hti.hiinternet.data.repo.PreFerenceRepo
import kotlinx.android.synthetic.main.fragment_video.*

private const val ARG_VIDEO_PATH = "VIDEO_PATH"


class VideoFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            VideoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_VIDEO_PATH, param1)

                }
            }
    }

    private var videoPath: String? = null
    var player: SimpleExoPlayer? = null
    private lateinit var mediaSource: MediaSource
    private lateinit var dataSourceFactory: DefaultDataSourceFactory
    var countDownTimer: CountDownTimer? = null
    private var playbackPosition: Long = 0
    private var playWhenReady : Boolean = false
    var currentWindow = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            videoPath = it.getString(ARG_VIDEO_PATH)

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_video, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializePlayer(videoPath)

    }



    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer(videoPath)
        }
    }

    private fun releasePlayer() {
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
        }
        if (player != null) {
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            playWhenReady = player!!.playWhenReady
            player!!.release()
            player = null
        }
    }


    private fun initializePlayer(videoUrl: String?) {
        player = ExoPlayerFactory.newSimpleInstance(context)
        videoView.setPlayer(this.player)
        player!!.seekTo(currentWindow, playbackPosition)
        //player!!.audioComponent?.volume = 0f
        Log.d("VideoUrl",videoUrl)
        val userAgent: String = Util.getUserAgent(context, "exoPlayerSample")

        val httpDataSourceFactory = DefaultHttpDataSourceFactory(
            userAgent,
            DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
            DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
            true
        )

        dataSourceFactory = DefaultDataSourceFactory(context, null, httpDataSourceFactory)

        mediaSource =
            ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(videoUrl))
        val loopingSource = LoopingMediaSource(mediaSource)

        player!!.playWhenReady = false

        player!!.prepare(loopingSource)

    }
}