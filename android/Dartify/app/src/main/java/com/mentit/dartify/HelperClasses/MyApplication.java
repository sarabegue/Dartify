package com.mentit.dartify.HelperClasses;

import android.app.Application;
import androidx.emoji.bundled.BundledEmojiCompatConfig;
import androidx.emoji.text.EmojiCompat;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        EmojiCompat.Config config = new BundledEmojiCompatConfig(this);
        config.setReplaceAll(true);
        EmojiCompat.init(config);
    }
}