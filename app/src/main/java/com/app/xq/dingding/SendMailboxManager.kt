package com.app.xq.dingding

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.SharedPreferences
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.teprinciple.mailsender.Mail
import com.teprinciple.mailsender.MailSender
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File


/**
 * @author Android-小强
 * @email: 15075818555@163.com
 * @data:  on 2021/1/8 16:40
 */
object SendMailboxManager {

    /**
     * 接收邮箱
     */
    fun saveMailbox(mailbox: String, context: Context) {
        val sp: SharedPreferences =
            context.getSharedPreferences("mailbox", AppCompatActivity.MODE_PRIVATE)
        sp.edit().putString("mailbox", mailbox).commit()
    }

    fun getMailbox(context: Context): String {
        val sp: SharedPreferences =
            context.getSharedPreferences("mailbox", AppCompatActivity.MODE_PRIVATE)
        return sp.getString("mailbox", "") ?: ""
    }

    /**
     * 发送邮箱
     */
    fun saveSendMailbox(mailbox: String, context: Context) {
        val sp: SharedPreferences =
            context.getSharedPreferences("mailbox", AppCompatActivity.MODE_PRIVATE)
        sp.edit().putString("send_mailbox", mailbox).commit()
    }

    fun getSendMailbox(context: Context): String {
        val sp: SharedPreferences =
            context.getSharedPreferences("mailbox", AppCompatActivity.MODE_PRIVATE)
        return sp.getString("send_mailbox", "") ?: ""
    }

    /**
     * 发送邮箱密码
     */
    fun saveSendPass(mailbox: String, context: Context) {
        val sp: SharedPreferences =
            context.getSharedPreferences("mailbox", AppCompatActivity.MODE_PRIVATE)
        sp.edit().putString("send_pass", mailbox).commit()
    }

    fun getSendPass(context: Context): String {
        val sp: SharedPreferences =
            context.getSharedPreferences("mailbox", AppCompatActivity.MODE_PRIVATE)
        return sp.getString("send_pass", "") ?: ""
    }


    /**
     * 更新文案
     */
    fun saveVersionMsg(context: Context) {
        val sp: SharedPreferences =
            context.getSharedPreferences("mailbox", AppCompatActivity.MODE_PRIVATE)
        sp.edit().putBoolean("show_brightness${BuildConfig.VERSION_CODE}", true).commit()
    }

    fun getVersionMsg(context: Context): Boolean {
        val sp: SharedPreferences =
            context.getSharedPreferences("mailbox", AppCompatActivity.MODE_PRIVATE)
        return sp.getBoolean("show_brightness${BuildConfig.VERSION_CODE}", false)
    }

    private var countMailbox: CountMailbox? = null

    init {

        GlobalScope.launch {
            while (true) {
                delay(1000 * 10)
                countMailbox?.run {
                    sendMailbox(
                        fromAddress, fromAddressPass,
                        emailAddress ?: arrayListOf<String>(),
                        content
                    )
                }
            }
        }

    }


    /**
     * 发送邮件  带附件的
     * @param toEmailAddress 接收邮箱地址的数组集合  例如 arrayListOf("488394778@qq.com")
     */
    fun sendMailboxImg(toEmailAddress: ArrayList<String>, msg: String, imgs: ArrayList<File>) {
        // 创建邮箱
        val mail = Mail().apply {
            mailServerHost = "smtp.qq.com"
            mailServerPort = "587"
            fromAddress = "980766134@qq.com"
            password = "oswiknzxjodobbec"
            toAddress = toEmailAddress
            subject = "钉钉"
            content = msg
            attachFiles = imgs
        }
        // 发送邮箱
        MailSender.getInstance().sendMail(mail, object : MailSender.OnMailSendListener {
            override fun onError(e: Throwable) {

            }

            override fun onSuccess() {
            }
        })
    }

    /**
     * 发送邮件
     * @param toEmailAddress 接收邮箱地址的数组集合  例如 arrayListOf("488394778@qq.com")
     */
    fun sendMailbox(
        froAddress: String?,
        passwd: String?,
        toEmailAddress: ArrayList<String>,
        msg: String
    ) {
        // 创建邮箱
        countMailbox = null
        val mail = Mail().apply {
            mailServerHost = "smtp.qq.com"
            mailServerPort = "587"
            fromAddress = froAddress ?: "980766134@qq.com"
            password = passwd ?: "oswiknzxjodobbec"
            toAddress = toEmailAddress
            subject = "钉钉"
            content = msg
//        attachFiles = arrayListOf(file)
        }
        // 发送邮箱
        MailSender.getInstance().sendMail(mail, object : MailSender.OnMailSendListener {
            override fun onError(e: Throwable) {
                countMailbox = CountMailbox().apply {
                    content = msg
                    emailAddress = toEmailAddress
                    fromAddress = froAddress
                    fromAddressPass = passwd
                }
            }

            override fun onSuccess() {

            }
        })

    }


    /**
     * 隐藏键盘
     */
    fun hideInput(context: Activity) {
        val imm: InputMethodManager? =
            context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
        val v: View = context.window.peekDecorView()
        v?.run {
            imm?.hideSoftInputFromWindow(windowToken, 0)
        }

    }

}