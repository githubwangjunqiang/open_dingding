package com.app.xq.mvpbase.entiy

/**
 * @author Android-小强
 * @email: 15075818555@163.com
 * @data:  on 2020/11/10 13:58
 */
class ViewStats {

    companion object {
        val contentView = 0
        val errorView = 1
        val emptyView = 2
        val networkErrorView = 3
        val loadDialogView = 4
        val loadView = 5
    }

    var stats = 0
    var msg: String? = null
    var drawableId = 0

    constructor() : this(contentView)
    constructor(stats: Int) : this(stats, null)
    constructor(stats: Int, msg: String?) : this(stats, msg, 0)
    constructor(msg: String?, drawableId: Int) : this(contentView, msg, drawableId)

    constructor(stats: Int, msg: String?, drawableId: Int) {
        this.stats = stats
        this.msg = msg
        this.drawableId = drawableId
    }


}