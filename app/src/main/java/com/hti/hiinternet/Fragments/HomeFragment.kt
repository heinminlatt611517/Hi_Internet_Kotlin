package com.hti.hiinternet.Fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.firebase.messaging.FirebaseMessaging
import com.hti.hiinternet.MainActivity
import com.hti.hiinternet.R
import com.hti.hiinternet.adapter.AdapterOurApplication
import com.hti.hiinternet.adapter.AdapterTopPromtoin
import com.hti.hiinternet.adapter.MultiMediaAdapter
import com.hti.hiinternet.adapter.PromotionAdapter
import com.hti.hiinternet.autoScroll
import com.hti.hiinternet.base.BaseFrgment
import com.hti.hiinternet.data.NetworkResult
import com.hti.hiinternet.data.Status
import com.hti.hiinternet.data.repo.PreFerenceRepo
import com.hti.hiinternet.data.request.RequestHome
import com.hti.hiinternet.data.response.ResponseHomeData
import com.hti.hiinternet.util.Constant
import com.hti.hiinternet.util.LanguageUtils
import com.hti.hiinternet.util.ui.PeekRatioLayoutManager
import com.hti.hiinternet.viewModel.HomeViewModel
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.fragment_homes.*
import kotlinx.android.synthetic.main.layout_home_header.*


class HomeFragment : BaseFrgment() {
    lateinit var promotionLayoutManager: LinearLayoutManager
    lateinit var promtionAdapter: PromotionAdapter
    lateinit var ourAppAdapter: AdapterOurApplication
    lateinit var homeViewModel: HomeViewModel
    lateinit var recOurApp: RecyclerView
    lateinit var topPromotioinSliderAdapter: AdapterTopPromtoin
    lateinit var shrimmerLayout: ShimmerFrameLayout
    lateinit var imageSlider: SliderView
    lateinit var sanpHelper: LinearSnapHelper
    lateinit var dialog: Dialog

    var player: SimpleExoPlayer? = null
    private lateinit var mediaSource: MediaSource
    private lateinit var dataSourceFactory: DefaultDataSourceFactory
    var countDownTimer: CountDownTimer? = null
    private var playbackPosition: Long = 0
    private var playWhenReady: Boolean = false
    var currentWindow = 0


    companion object {
        fun newIntance() = HomeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        subscribleFirebaseService()

    }

    // change spinner language

    private fun subscribleFirebaseService() {
        FirebaseMessaging.getInstance().isAutoInitEnabled = false
        FirebaseMessaging.getInstance().subscribeToTopic("hti")
        //registerFirebasetokne()
        // ( activity as BaseActivity ).registerFirebasetokne()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_homes, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        observeViewModel(homeViewModel)
        homeViewModel.loadHomeData(getHomeRequest())

    }


    private fun initView(view: View) {


        if (PreFerenceRepo.lang.equals("0")) {
            langSpinner.setSelection(0)
        } else if (PreFerenceRepo.lang.equals("1")) {
            langSpinner.setSelection(1)
        }

        langSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectItem = p0?.getItemAtPosition(p2).toString()

                if (!PreFerenceRepo.lang.equals(p2.toString())) {
                    PreFerenceRepo.lang = p2.toString()

                    if (p2 == 0) {
                        activity?.let {
                            LanguageUtils.setLocale("EN", it)
                        }
                    } else if (p2 == 1) {
                        activity?.let { LanguageUtils.setLocale("UNI", it) }
                    }

                }

            }
        }

        sanpHelper = LinearSnapHelper()
        promotionLayoutManager = PeekRatioLayoutManager(context, RecyclerView.HORIZONTAL, false)
        ourAppAdapter = AdapterOurApplication(arrayListOf(), this!!.context!!)
        homeViewModel = getViewModel()


//        recmiddlepromotion.apply {
//
//            layoutManager = promotionLayoutManager
//        }


        recOurApp = view.findViewById(R.id.recOurApp)
        recOurApp.apply {
            layoutManager = com.hti.hiinternet.util.ui.GridLayoutManager(context, 2);
            adapter = ourAppAdapter
        }

        imageSlider = view.findViewById(R.id.topPromotionSlider)
        topPromotioinSliderAdapter = AdapterTopPromtoin(arrayListOf(), this!!.context!!)
        imageSlider.sliderAdapter = topPromotioinSliderAdapter
        shrimmerLayout = view.findViewById(R.id.loadingLaoyut)
        setupImageSlider()
        //just for test

        topPromotioinSliderAdapter.onItemClick = {
            if (it.equals("Slider 3")) {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.main_frame, ApplicationGuideFragment.newIntance(), null)
                    ?.addToBackStack(Constant.TAG_HOME)?.commit()
            }

        }


        ourAppAdapter.onItemClick = {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(it.link)
            if (it.link.equals("")) {

            } else
                startActivity(openURL)
        }

    }

    private fun setupImageSlider() {
        imageSlider.setIndicatorAnimation(IndicatorAnimations.FILL); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        imageSlider.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_RIGHT;
        imageSlider.indicatorSelectedColor = Color.WHITE;
        imageSlider.indicatorUnselectedColor = Color.GRAY;

    }

    private fun getViewModel(): HomeViewModel {
        return ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }

    private fun getHomeRequest(): RequestHome {
        return RequestHome()
    }

    override fun showLoadingView() {


        normalLaoyut.visibility = View.GONE
        //  loadingLaoyut.visibility = View.VISIBLE
        shrimmerLayout.startShimmer()
    }

    override fun showNormalView() {
        normalLaoyut.visibility = View.VISIBLE
        //  loadingLaoyut.visibility = View.GONE
        shrimmerLayout.stopShimmer()

    }

    fun observeViewModel(v: HomeViewModel) {
        Log.d("homeFragment", "Observer")
        v.status!!.observe(this, Observer<Status> {
            when (it) {
                Status.SUCCESS -> {
                    Log.d("homeFragment", "success")
                    showNormalView()
                }
                Status.ERROR -> {
                    v.homeResponseLiveData!!.value.let {
                        if (it!!.errorCode == Constant.SESSION_EXPIRE) {
                            (activity as MainActivity).onSessionExpire(
                                it.message!!,
                                it.statusMessage
                            )
                        }
                    }

                }
                Status.LOADING -> {
                    showLoadingView()
                }

            }

            v.homeResponseLiveData?.observe(this, Observer<NetworkResult<ResponseHomeData>> {

                Log.d("homeFragment", it.toString())

                Log.d("RequiredUpdate", it.is_require_update.toString())

                saveHotlinePhone(it)
                saveVideoUrl(it)
                //setUpVideoView(it)
                //initializePlayer("https://horizoninternet.myanmaronlinecreations.com/sites/default/files/promo_slide/usage_video.mp4")
                it.data?.let {
                    if (it.middleImage != null && it.middleImage.size > 0) {
                        ///    promtionAdapter.setData(it.middleImage)
                        Log.d("homeFragment", "bind data ${it.middleImage.size}")
                        Log.d("homeData", it.toString())

                        displaypromotionlist(it)
                        displayOurappList(it)
                        displayPromiton(it)


                    }
                }

                if (it.is_require_update!!.equals(true)) {
                    showUpdateAppDialog()
                }

            })

        })
    }

    private fun initializePlayer(videoUrl: String?) {

        player = ExoPlayerFactory.newSimpleInstance(context)
        //videoView.setPlayer(this.player)
        player!!.seekTo(currentWindow, playbackPosition)
        //player!!.audioComponent?.volume = 0f

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


    private fun saveVideoUrl(it: NetworkResult<ResponseHomeData>?) {
        // it?.videoUrl.let { PreFerenceRepo.videoUrl = it }
    }

//    private fun setUpVideoView(it: NetworkResult<ResponseHomeData>?) {
//        if (mediaControls == null) {
//            // create an object of media controller class
//            mediaControls = MediaController(context)
//            mediaControls!!.setAnchorView(videoView)
//        }
//
//        Log.d("videoUrl",it?.videoUrl)
//       it?.videoUrl.let {
//           videoView?.setVideoURI(Uri.parse("$it"))
//       }
//        videoView.setOnPreparedListener(MediaPlayer.OnPreparedListener { mp ->
//            mp.setVolume(0f, 0f)
//            mp.isLooping = true
//            videoView.start()
//        })
//
//        videoView.setOnErrorListener { mp, what, extra -> true }
//
//        videoView.setOnCompletionListener {
////            videoView?.setVideoURI(Uri.parse(PreFerenceRepo.videoUrl))
////            videoView?.start()
//        }
//
//    }


    private fun showUpdateAppDialog() {
        val builder =
            AlertDialog.Builder(context!!)
        builder.setTitle(getString(R.string.str_txt_playStore_update))
        builder.setPositiveButton(
            "Update"
        ) { dialog, which ->
            startActivity(
                Intent(
                    Intent.ACTION_VIEW, Uri.parse(
                        "http://play.google.com/store/apps/details?id=com.hti.hiinternet"
                    )
                )
            )
            dialog.dismiss()
        }

        builder.setCancelable(false)
        dialog = builder.show()


    }


    private fun saveHotlinePhone(it: NetworkResult<ResponseHomeData>) {
        it.hotline_phone.let { PreFerenceRepo.hotline_phone = it }
    }

    private fun displaypromotionlist(it: ResponseHomeData) {
        val viewPagerAdapter =
            activity?.supportFragmentManager?.let { it1 -> MultiMediaAdapter(it1, it.middleImage) }
        viewPager.adapter = viewPagerAdapter
        viewPager.currentItem = 0

        //viewPager.autoScroll(3000)

        dotsIndicator.setViewPager(viewPager)
        viewPager.adapter?.registerDataSetObserver(dotsIndicator.dataSetObserver)
        //promtionAdapter = PromotionAdapter(it.middleImage)
        //  promtionAdapter.setData(it.middleImage)

//        recmiddlepromotion.adapter = promtionAdapter
//        recyclerview_pager_indicator.attachToRecyclerView(recmiddlepromotion)
//        sanpHelper.attachToRecyclerView(recmiddlepromotion)

    }

    private fun displayOurappList(it: ResponseHomeData) {

        ourAppAdapter.setData(it.downlImagea)
    }

    private fun displayPromiton(it: ResponseHomeData) {
        topPromotioinSliderAdapter.setData(it.upImage)
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

            initializePlayer("https://horizoninternet.myanmaronlinecreations.com/sites/default/files/promo_slide/usage_video.mp4")


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

//    private fun setUpVidewView(){
//
//        if (mediaControls == null) {
//            // create an object of media controller class
//            mediaControls = MediaController(context)
//            mediaControls!!.setAnchorView(videoView)
//        }
//
////        videoView?.setVideoURI(Uri.parse("android.resource://"
////                + context?.packageName + "/" + R.raw.test))
//        videoView?.setVideoURI(Uri.parse("https://developers.google.com/training/images/tacoma_narrows.mp4"))
//        videoView?.start()
//
//
//        videoView.setOnCompletionListener {
//            videoView?.start()
//        }
//
//        videoView?.setOnPreparedListener(OnPreparedListener { mp ->
//            mp.setVolume(0f, 0f)
//            mp.isLooping = true
//        })
//
//    }

}

