package link.webarata3.dro.copypictonas.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.util.Objects;

import link.webarata3.dro.copypictonas.util.FileUtil;

public class FileSizeIntentService extends IntentService {
    private static final String TAG = FileSizeIntentService.class.getSimpleName();

    public FileSizeIntentService() {
        super(FileSizeIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Service Started!");
        Objects.requireNonNull(intent);

        ResultReceiver receiver = intent.getParcelableExtra("receiver");
        String dirName = intent.getStringExtra("dirName");

        FileUtil.TotalInDirectory tid = FileUtil.fileCountAndSize(new File(dirName));

        Bundle bundle = new Bundle();
        bundle.putInt("fileCount", tid.getCount());
        bundle.putLong("fileSize", tid.getSize());
        receiver.send(1, bundle);

        stopSelf();

        Log.d(TAG, "Service Stopping!");
    }
}
