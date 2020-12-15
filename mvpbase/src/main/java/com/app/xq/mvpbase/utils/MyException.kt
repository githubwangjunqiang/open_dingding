package com.app.xq.mvpbase.utils

import java.io.IOException

/**
 * @author Android-小强
 * @email: 15075818555@163.com
 * @data:  on 2020/11/23 15:21
 */
class MyException : IOException {
    var code = -1
    var msg = ""

    constructor(code: Int, msg: String) : super() {
        this.code = code
        this.msg = msg
    }

}