package com.app.xq.dingding

import android.util.TypedValue
import com.teprinciple.mailsender.Mail
import com.teprinciple.mailsender.MailSender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


/**
 * @author Android-小强
 * @email: 15075818555@163.com
 * @data:  on 2020/11/9 11:02
 */

/**
 * px 转换 dp
 */
fun Float.dp(): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        App.context.resources.displayMetrics
    )
}

/**
 * px 转换 dp
 */
fun Float.dpInt(): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        App.context.resources.displayMetrics
    ).toInt()
}

/**
 * 发送邮件
 * @param toEmailAddress 接收邮箱地址的数组集合  例如 arrayListOf("488394778@qq.com")
 */
fun sendMailbox(toEmailAddress: ArrayList<String>, msg: String) {
    // 创建邮箱
    val mail = Mail().apply {
        mailServerHost = "smtp.qq.com"
        mailServerPort = "587"
        fromAddress = "980766134@qq.com"
        password = "oswiknzxjodobbec"
        toAddress = toEmailAddress
        subject = "钉钉"
        content = msg
//        attachFiles = arrayListOf(file)
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