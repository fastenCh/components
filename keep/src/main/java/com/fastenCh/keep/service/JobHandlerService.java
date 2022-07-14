package com.fastenCh.keep.service;

import android.app.Notification;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.app.job.JobInfo.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;

import androidx.annotation.RequiresApi;

import com.fastenCh.keep.KeepLive;
import com.fastenCh.keep.config.NotificationUtils;
import com.fastenCh.keep.receiver.NotificationClickReceiver;
import com.fastenCh.keep.utils.ServiceUtils;

@RequiresApi(
        api = 21
)
public final class JobHandlerService extends JobService {
    private JobScheduler mJobScheduler;
    private int jobId = 100;

    public JobHandlerService() {
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        this.startService(this);
        if (VERSION.SDK_INT >= 21) {
            this.mJobScheduler = (JobScheduler) this.getSystemService("jobscheduler");
            this.mJobScheduler.cancel(this.jobId);
            Builder builder = new Builder(this.jobId, new ComponentName(this.getPackageName(), JobHandlerService.class.getName()));
            if (VERSION.SDK_INT >= 24) {
                builder.setMinimumLatency(30000L);
                builder.setOverrideDeadline(30000L);
                builder.setMinimumLatency(30000L);
                builder.setBackoffCriteria(30000L, 0);
            } else {
                builder.setPeriodic(30000L);
            }

            builder.setRequiredNetworkType(1);
            builder.setPersisted(true);
            this.mJobScheduler.schedule(builder.build());
        }

        return 1;
    }

    private void startService(Context context) {
        Intent localIntent;
        if (VERSION.SDK_INT >= 26 && KeepLive.foregroundNotification != null) {
            localIntent = new Intent(this.getApplicationContext(), NotificationClickReceiver.class);
            localIntent.setAction("CLICK_NOTIFICATION");
            Notification notification = NotificationUtils.createNotification(this, KeepLive.foregroundNotification.getTitle(), KeepLive.foregroundNotification.getDescription(), KeepLive.foregroundNotification.getIconRes(), localIntent);
            this.startForeground(13691, notification);
        }

        localIntent = new Intent(context, LocalService.class);
        Intent guardIntent = new Intent(context, RemoteService.class);
        this.startService(localIntent);
        this.startService(guardIntent);
    }

    public boolean onStartJob(JobParameters jobParameters) {
        if (!ServiceUtils.isServiceRunning(this.getApplicationContext(), "com.fanjun.keeplive.service.LocalService") || !ServiceUtils.isRunningTaskExist(this.getApplicationContext(), this.getPackageName() + ":remote")) {
            this.startService(this);
        }

        return false;
    }

    public boolean onStopJob(JobParameters jobParameters) {
        if (!ServiceUtils.isServiceRunning(this.getApplicationContext(), "com.fanjun.keeplive.service.LocalService") || !ServiceUtils.isRunningTaskExist(this.getApplicationContext(), this.getPackageName() + ":remote")) {
            this.startService(this);
        }

        return false;
    }
}
