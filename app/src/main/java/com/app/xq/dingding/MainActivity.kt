package com.app.xq.dingding

import android.Manifest
import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Rect
import android.graphics.RectF
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.app.xq.dingding.guidingmask.GuidingMaskView
import com.app.xq.dingding.guidingmask.MaskView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    companion object {

        /**
         * 时间 动画时间以及 遍历job 时间
         */
        const val DURATION = 1000 * 30L

        /**
         * 轮询 守护 协程 时间间隔
         */
        const val GUARDTIME = 1000 * 60L * 5

        /**
         * 是否打开了钉钉
         */
        var startDingding = false

        /**
         * 选中的时间
         */
        var millisecondValue = 0L
    }


    private var mMediaProjection: MediaProjection? = null
    private val REQUEST_PERMISSIONS: Int = 1001
    private val RECORD_REQUEST_CODE: Int = 1002
    private var mMediaProjectionManage: MediaProjectionManager? = null
    private var launchWhenCreatedPolling: Job? = null
    private var objectAnimator: ObjectAnimator? = null
    private var launchWhenCreated: Job? = null
    val mTextViewTimeStart by lazy {
        findViewById<TextView>(R.id.starttime)
    }
    val mTextViewContent by lazy {
        findViewById<TextView>(R.id.starttimecontent)
    }
    val mRadarView by lazy {
        findViewById<RadarView>(R.id.radarview)
    }
    val mToggleButton by lazy {
        findViewById<ToggleButton>(R.id.switch1)
    }
    val edittextMailbox by lazy {
        findViewById<TextInputEditText>(R.id.textedit)
    }
    val edittextSend by lazy {
        findViewById<TextInputEditText>(R.id.texteditsend)
    }
    val edittextSendPass by lazy {
        findViewById<TextInputEditText>(R.id.texteditsendpass)
    }

    /**
     * 2.设置 APP界面屏幕亮度值方法  0--1 之间
     */
    private fun setAppScreenBrightness(birghtessValue: Float) {
        val window: Window = window
        val lp: WindowManager.LayoutParams = window.getAttributes()
        lp.screenBrightness = birghtessValue
        window.setAttributes(lp)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        setListener()

        if (!SendMailboxManager.getVersionMsg(applicationContext)) {
            SendMailboxManager.saveVersionMsg(applicationContext)

            AlertDialog.Builder(this).setMessage("可以自定义发送邮件使用的邮箱地址，不过您的邮箱必须开启POP3/SMTP服务").show()
        }

    }

    private fun initView() {
        var bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "不支持蓝牙", Toast.LENGTH_SHORT).show()
            return
        }
        if (!bluetoothAdapter!!.isEnabled()) {
            val res: Boolean = bluetoothAdapter!!.enable()
            if (res) {
                Toast.makeText(this, "蓝牙打开成功", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "蓝牙打开失败", Toast.LENGTH_SHORT).show()
            }
        } else if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "蓝牙已打开", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "蓝牙打开失败", Toast.LENGTH_SHORT).show()
        }


        mTextViewTimeStart.post {
            if (true) {
                return@post
            }
            val rctf = Rect()
            mTextViewTimeStart.getGlobalVisibleRect(rctf)
            val rectF = RectF(rctf)
            val build = GuidingMaskView.newBuilder(this)
                .setRounBorderColor(Color.argb(150, 255, 255, 255))
                .setAddRoundBorder(true)
                .setRounBorderWidth(1F.dpInt())
                .setMaskLayer(rectF, true)
                .setSpacing(0F, 10F.dp(), 10F.dp(), 10F.dp())
                .setRadius(10F.dp())
                .setOpenAnimation(true)
                .setCallback { true }
                .build()
            val maskView = MaskView(this)
            val top: Float = rctf.bottom.toFloat()

            maskView.setFlickerViewPadingleft((rectF.right - rectF.width() / 2).toInt())
            maskView.setContentMsg(
                "先选择时间"
            )

            val layoutParams: FrameLayout.LayoutParams = maskView.createLayoutParams(
                0,
                top.toInt()
            )
            maskView.setBtnOnClickListener { v ->
                showStartMasking()
            }
            build.addView(maskView, layoutParams)
            build.show(this)
        }


        val mailbox = SendMailboxManager.getMailbox(applicationContext)
        edittextMailbox?.setText(mailbox)


        val sendMailbox = SendMailboxManager.getSendMailbox(applicationContext)
        edittextSend?.setText(sendMailbox)

        val sendPass = SendMailboxManager.getSendPass(applicationContext)
        edittextSendPass?.setText(sendPass)

    }

    var valueDouble: Double = 0.0

    private fun showStartMasking() {

        val rctf = Rect()
        mToggleButton.getGlobalVisibleRect(rctf)
        val rectF = RectF(rctf)
        val build = GuidingMaskView.newBuilder(this)
            .setRounBorderColor(Color.argb(150, 255, 255, 255))
            .setAddRoundBorder(true)
            .setRounBorderWidth(1F.dpInt())
            .setMaskLayer(rectF, true)
            .setSpacing(10F.dp(), 10F.dp(), 10F.dp(), 10F.dp())
            .setRadius(10F.dp())
            .setOpenAnimation(true)
            .setCallback { true }
            .build()
        val maskView = MaskView(this)
        val top: Float =
            20F.dp() + rctf.bottom

        maskView.setFlickerViewPadingleft((rectF.right - rectF.width() / 2).toInt())
        maskView.setContentMsg(
            "选好时间后开始计时"
        )

        val layoutParams: FrameLayout.LayoutParams = maskView.createLayoutParams(
            0,
            top.toInt()
        )
        maskView.setBtnOnClickListener { v ->
            build.dismiss()
            setShow()
            Toast.makeText(this, "先测试打开钉钉", Toast.LENGTH_SHORT).show()
            startActivityForPackName("com.alibaba.android.rimet")
        }
        build.addView(maskView, layoutParams)
        build.show(this)


    }

    private fun startDefend() {
        launchWhenCreatedPolling?.cancel()
        launchWhenCreatedPolling = lifecycleScope.launchWhenCreated {
            while (true) {
                delay(GUARDTIME)
                launchWhenCreated?.run {
                    if (!isActive) {
                        startTime()
                    }
                }

            }
        }
    }

    private fun setListener() {
        mTextViewTimeStart.setOnClickListener {
//            startActivity(Intent(this, AelectionTime::class.java))
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                val datePickerDialog = DatePickerDialog(this)
                datePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
                    val timePickerDialog = TimePickerDialog(this, { view, hourOfDay, minut ->
                        val apply = Calendar.getInstance().apply {
                            set(year, month, dayOfMonth, hourOfDay, minut)
                        }
                        millisecondValue = apply.timeInMillis
                        mTextViewTimeStart?.text =
                            "选中的时间是：${
                                SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                                    millisecondValue
                                )
                            }"

                    }, 0, 0, true)
                    timePickerDialog.show()
                }
                datePickerDialog.show()
            }
        }
        mToggleButton.setOnCheckedChangeListener { _, isChecked ->
            //关闭 键盘
            SendMailboxManager.hideInput(this)
            if (isChecked) {
                startTime()
            } else {
                closeTime()
            }
        }


    }


    override fun onStop() {

        val trim = edittextMailbox?.text.toString().trim()
        SendMailboxManager.saveMailbox(trim, applicationContext)

        val send = edittextSend?.text.toString().trim()
        SendMailboxManager.saveSendMailbox(send, applicationContext)

        val sendPass = edittextSendPass?.text.toString().trim()
        SendMailboxManager.saveSendPass(sendPass, applicationContext)
        super.onStop()
    }

    private fun closeTime() {
        launchWhenCreated?.cancel()
        launchWhenCreated = null
        objectAnimator?.cancel()
        objectAnimator = null
        launchWhenCreatedPolling?.cancel()
        launchWhenCreatedPolling = null
        setAppScreenBrightness(-1F)
    }

    private fun startAnimation() {
        objectAnimator?.cancel()
        objectAnimator = ObjectAnimator.ofFloat(mRadarView, "mRotate", 360F, 0F)
        objectAnimator?.duration = DURATION
        objectAnimator?.repeatCount = ObjectAnimator.INFINITE
        objectAnimator?.interpolator = LinearInterpolator()
        objectAnimator?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                Log.d("12345", ": 开始启动");
                mRadarView.pts.clear()
                for (index in 1..5) {
                    mRadarView.pts.add(
                        Random.nextInt(mRadarView.width).toFloat()
                    )
                    mRadarView.pts.add(
                        Random.nextInt(mRadarView.height).toFloat()
                    )
                }

            }

            override fun onAnimationEnd(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
                Log.d("12345", ": 开始启动");
                mRadarView.pts.clear()
                for (index in 1..5) {
                    mRadarView.pts.add(
                        Random.nextInt(mRadarView.width).toFloat()
                    )
                    mRadarView.pts.add(
                        Random.nextInt(mRadarView.height).toFloat()
                    )
                }
            }

        })
        objectAnimator?.start()

    }

    private fun startTime() {
        if (millisecondValue <= 0) {
            Toast.makeText(this, "您还没有选择时间", Toast.LENGTH_SHORT).show()
            mToggleButton.isChecked = false
            return
        }
        setAppScreenBrightness(0.02F)
        startAnimation()
        startDefend()
        launchWhenCreated = lifecycleScope.launchWhenCreated {
            while (true) {
                delay(DURATION)
                val currentTimeMillis = System.currentTimeMillis()

                mTextViewContent.text =
                    "上次查询时间是：${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTimeMillis)}"


                if (millisecondValue <= currentTimeMillis) {
                    val trim = edittextMailbox.text.toString().trim()
                    var send: String? = edittextSend.text.toString().trim()
                    if (TextUtils.isEmpty(send)) {
                        send = null
                    }
                    var sendPass: String? = edittextSendPass.text.toString().trim()
                    if (TextUtils.isEmpty(sendPass)) {
                        sendPass = null
                    }
                    val startActivityForPackName =
                        startActivityForPackName("com.alibaba.android.rimet")
                    Log.d("12345", "打开: $startActivityForPackName");
                    mTextViewContent.append("\n打开钉钉：$startActivityForPackName")


                    Log.d("12345", "trim:$trim ");
                    if (!TextUtils.isEmpty(trim)) {
                        if (startDingding) {
                            return@launchWhenCreated
                        }
                        if (startActivityForPackName) {
                            startDingding = startActivityForPackName
                        }
                        var msg = if (startActivityForPackName) "成功" else "失败"
                        SendMailboxManager.sendMailbox(
                            send, sendPass,
                            arrayListOf(trim),
                            "计时器计时时间到，并尝试打开滴滴，打开结果为：" +
                                    "$msg " +
                                    "\n 如果此次打开滴滴失败，" +
                                    "后续会每尝试打开一次滴滴就会发送一次邮件，直到打开成功或者您回到公司手动关闭计时"
                        )
                    }
                }
            }
        }
    }

    /**
     * 跳转到指定app界面
     *
     * @param name
     * @return
     */
    fun startActivityForPackName(name: String?): Boolean {
        try {
            if (!TextUtils.isEmpty(name)) {
                val packageManager: PackageManager =
                    packageManager
                val launchIntentForPackage = packageManager.getLaunchIntentForPackage(name!!)
                launchIntentForPackage!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(launchIntentForPackage)
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }


    override fun onDestroy() {
        closeTime()
        super.onDestroy()
    }


    fun getShow(): Boolean {
        try {
            val cacheDir = File(cacheDir, "show")

            if (cacheDir.exists()) {
                cacheDir.createNewFile()
            }
            if (cacheDir.length() > 0) {
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    fun setShow() {
        try {
            val cacheDir = File(cacheDir, "show")

            if (cacheDir.exists()) {
                cacheDir.createNewFile()
            }
            val bufferedWriter = BufferedWriter(FileWriter(cacheDir))
            bufferedWriter.write("show")
            bufferedWriter.flush()
            bufferedWriter.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    fun requestForAccess() {
        val permissions = arrayOf(
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.RECORD_AUDIO //音频
        )
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.MODIFY_AUDIO_SETTINGS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS)
        }
    }

    /**
     * 开始录屏
     */
    private fun requestRecording() {
        mMediaProjectionManage =
            getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        var captureIntent: Intent? = null
        if (mMediaProjectionManage != null) {
            captureIntent = mMediaProjectionManage?.createScreenCaptureIntent()
        }
        startActivityForResult(captureIntent, RECORD_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RECORD_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                mMediaProjection = mMediaProjectionManage?.getMediaProjection(resultCode, data!!);
            } else {
                Log.d("12345", "requestRecordFail: 用戶拒绝录制屏幕: ")
            }
        }
    }
}