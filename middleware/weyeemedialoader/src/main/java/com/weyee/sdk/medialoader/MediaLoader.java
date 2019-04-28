/*
 *
 *  Copyright 2017 liu-feng
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  imitations under the License.
 *
 */

package com.weyee.sdk.medialoader;

import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.content.Loader;
import com.weyee.sdk.medialoader.callback.*;
import com.weyee.sdk.medialoader.loader.AbsLoaderCallBack;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Created by Taurus on 2017/5/23.
 */

public class MediaLoader {

    private static final int DEFAULT_START_ID = 1000;

    private final String TAG = "MediaLoader";
    private static MediaLoader loader = new MediaLoader();

    private Map<String,Queue<LoaderTask>> mTaskGroup = new HashMap<>();

    private Map<String,Integer> mIds = new HashMap<>();

    private final int MSG_CODE_LOAD_FINISH = 101;
    private final int MSG_CODE_LOAD_START = 102;

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_CODE_LOAD_FINISH:
                    Message message = Message.obtain();
                    message.what = MSG_CODE_LOAD_START;
                    message.obj = msg.obj;
                    sendMessage(message);
                    break;
                case MSG_CODE_LOAD_START:
                    String group = (String) msg.obj;
                    if(!TextUtils.isEmpty(group)){
                        Queue<LoaderTask> queue = mTaskGroup.get(group);
                        LoaderTask task = queue.poll();
                        if(task!=null){
                            queueLoader(task.activity.get(),task.onLoaderCallBack);
                        }
                        Log.d(TAG,"after poll current group = " + group + " queue length = " + queue.size());
                    }
                    break;
            }
        }
    };

    private MediaLoader(){
    }

    public static MediaLoader getLoader(){
        return loader;
    }

    private int checkIds(FragmentActivity activity){
        String name = activity.getClass().getName();
        int id;
        if(!mIds.containsKey(name)){
            id = DEFAULT_START_ID;
            mIds.put(name, id);
        }else{
            int preId = mIds.get(name);
            preId++;
            mIds.put(name, preId);
            id = preId;
        }
        return id;
    }

    private void loadMedia(FragmentActivity activity, AbsLoaderCallBack absLoaderCallBack){
        activity.getSupportLoaderManager().restartLoader(checkIds(activity),null,absLoaderCallBack);
    }

    private synchronized void load(FragmentActivity activity, OnLoaderCallBack onLoaderCallBack){

        String name = activity.getClass().getSimpleName();
        Queue<LoaderTask> queue = mTaskGroup.get(name);
        LoaderTask task = new LoaderTask(new WeakReference<>(activity),onLoaderCallBack);
        if(queue==null){
            queue = new LinkedList<>();
            mTaskGroup.put(name,queue);
        }
        queue.offer(task);
        Log.d(TAG,"after offer current queue group = " + name + " queue length = " + queue.size());
        if(queue.size()==1){
            Message message = Message.obtain();
            message.what = MSG_CODE_LOAD_START;
            message.obj = name;
            mHandler.sendMessage(message);
        }
    }

    private void queueLoader(final FragmentActivity activity, OnLoaderCallBack onLoaderCallBack){
        loadMedia(activity, new AbsLoaderCallBack(activity,onLoaderCallBack){
            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                super.onLoaderReset(loader);
                Queue<LoaderTask> queue = mTaskGroup.get(activity.getClass().getSimpleName());
                if(queue!=null){
                    queue.clear();
                }
                Log.d(TAG,"***onLoaderReset***");
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                super.onLoadFinished(loader, data);
                Message message = Message.obtain();
                message.what = MSG_CODE_LOAD_FINISH;
                message.obj = activity.getClass().getSimpleName();
                mHandler.sendMessage(message);
                Log.d(TAG,"***onLoaderFinished***");
            }
        });
    }

    public void loadPhotos(FragmentActivity activity, OnPhotoLoaderCallBack onPhotoLoaderCallBack){
        load(activity,onPhotoLoaderCallBack);
    }

    public void loadVideos(FragmentActivity activity, OnVideoLoaderCallBack onVideoLoaderCallBack){
        load(activity,onVideoLoaderCallBack);
    }

    public void loadAudios(FragmentActivity activity, OnAudioLoaderCallBack onAudioLoaderCallBack){
        load(activity,onAudioLoaderCallBack);
    }

    public void loadFiles(FragmentActivity activity, OnFileLoaderCallBack onFileLoaderCallBack){
        load(activity,onFileLoaderCallBack);
    }

    public static class LoaderTask{
        public WeakReference<FragmentActivity> activity;
        public OnLoaderCallBack onLoaderCallBack;

        public LoaderTask(WeakReference<FragmentActivity> activity, OnLoaderCallBack onLoaderCallBack) {
            this.activity = activity;
            this.onLoaderCallBack = onLoaderCallBack;
        }
    }

}
