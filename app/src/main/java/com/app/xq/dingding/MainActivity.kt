package com.app.xq.dingding

import android.animation.ObjectAnimator
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Rect
import android.graphics.RectF
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.app.xq.dingding.guidingmask.GuidingMaskView
import com.app.xq.dingding.guidingmask.MaskView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


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
         * 选中的时间
         */
        var millisecondValue = 0L
    }


    private var launchWhenCreatedPolling: Job? = null
    private var isChecked: Boolean = false
    private var objectAnimator: ObjectAnimator? = null
    private var launchWhenCreated: Job? = null
    val mTextViewTimeStart by lazy {
        findViewById<Button>(R.id.starttime)
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        setListener()
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
            if (getShow()) {
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
            val top: Float =
                20F.dp() + rctf.bottom

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

    }

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
        mToggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            this.isChecked = isChecked
            if (isChecked) {
                startTime()
            } else {
                closeTime()
            }
        }

    }

    private fun closeTime() {
        launchWhenCreated?.cancel()
        launchWhenCreated = null
        objectAnimator?.cancel()
        objectAnimator = null
        launchWhenCreatedPolling?.cancel()
        launchWhenCreatedPolling = null
    }

    private fun startAnimation() {
        objectAnimator?.cancel()
        objectAnimator = ObjectAnimator.ofFloat(mRadarView, "mRotate", 360F, 0F)
        objectAnimator?.duration = DURATION
        objectAnimator?.repeatCount = ObjectAnimator.INFINITE
        objectAnimator?.interpolator = LinearInterpolator()
        objectAnimator?.start()

    }

    private fun startTime() {
        if (millisecondValue <= 0) {
            Toast.makeText(this, "您还没有选择时间", Toast.LENGTH_SHORT).show()
            mToggleButton.isChecked = false
            return
        }
        startAnimation()
        startDefend()
        launchWhenCreated = lifecycleScope.launchWhenCreated {
            while (true) {
                delay(DURATION)
                if (!isChecked) {
                    continue
                }
                val currentTimeMillis = System.currentTimeMillis()

                mTextViewContent.text =
                    "上次查询时间是：${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTimeMillis)}"


                if (millisecondValue <= currentTimeMillis) {
                    val startActivityForPackName =
                        startActivityForPackName("com.alibaba.android.rimet")
                    Log.d("12345", "打开: $startActivityForPackName");
                    mTextViewContent.append("\n打开钉钉：$startActivityForPackName")
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
        objectAnimator?.cancel()
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
}