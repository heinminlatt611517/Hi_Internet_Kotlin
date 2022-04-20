package com.hti.hiinternet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.os.postDelayed
import androidx.lifecycle.ViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import com.gauravbhola.ripplepulsebackground.RipplePulseLayout
import com.hti.hiinternet.Fblogin.ActivityFbLogin
import com.hti.hiinternet.base.BaseActivity
import com.hti.hiinternet.data.repo.PreFerenceRepo
import com.hti.hiinternet.login.ActivityLogin
import com.hti.hiinternet.viewModel.LogInViewModel
import kotlinx.android.synthetic.main.activity_splash_screen.*
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class SplashScreen : BaseActivity() {
    lateinit var imageLogo: ImageView
    override fun showLoadingView() {

    }

    override fun showNormalView() {
    }

    //just return null
    override fun getViewModel(): ViewModel = LogInViewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        layout_ripplepulse.startRippleAnimation()
        Handler().postDelayed(object : Runnable {
            override fun run() {
                //checkAlreadyFblogin()
                checkLocalLanguage()
                layout_ripplepulse.startRippleAnimation()
                gotoMainActivity()
            }

        }, 3000)

    }


    private fun checkAlreadyFblogin(){
        if (PreFerenceRepo.ufid.isNullOrEmpty()){
            startActivity(Intent(this@SplashScreen, ActivityFbLogin::class.java))
            finish()
        }
        else{

            startActivity(Intent(this@SplashScreen, MainActivity::class.java))
            finish()
        }
    }


    private fun checkLocalLanguage() {
        if (PreFerenceRepo.lang.equals("")) {
            setLocal("eng")
        } else if (PreFerenceRepo.lang.equals("0")) {
            setLocal("eng")
        } else if (PreFerenceRepo.lang.equals("1")) {
            setLocal("uni")
        }
    }

    fun gotoLogInScreen() {
        var activityCompact =
            ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageLogo, "logo")
        var intentLogIn = Intent(this@SplashScreen, ActivityLogin::class.java)
        startActivity(intentLogIn, activityCompact.toBundle())
        finish()
    }

    fun gotoMainActivity(){
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

    private fun setLocal(lang : String){
        val myLocale = Locale(lang)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)
    }

}
