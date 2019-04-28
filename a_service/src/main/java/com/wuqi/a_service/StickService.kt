package com.wuqi.a_service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.weyee.sdk.router.WorkerNavigation

/**
 *
 * @author wuqi by 2019/4/16.
 */
class StickService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        WorkerNavigation(baseContext).toWorkerActivity()
        return super.onStartCommand(intent, START_FLAG_RETRY, startId)
    }
}