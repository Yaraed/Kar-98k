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

package com.weyee.sdk.util.sp;

/**
 * SharedPreferences 默认工具类
 *
 * @author wuqi by 2019/3/12.
 */
public class SpUtils {

    private SpUtils() {

    }

    /**
     * 默认的日志记录为Logcat
     */
    public static ISharedPreferences getDefault() {
        return SpImpl.Holder.BUS;
        //return SpImpl.Holder.BUS;
    }
}
