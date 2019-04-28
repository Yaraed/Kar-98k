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

package com.weyee.sdk.multitype;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 只处理简单的Adapter，如需比较复杂的视图类型，请使用优秀的第三方框架，example：vLayout/MultiType
 *
 * @author wuqi by 2019/2/22.
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseHolder<T>> implements AdapterHelper<T> {
    private List<T> mList;
    private OnRecyclerViewItemClickListener<T> mOnItemClickListener = null;

    public BaseAdapter(@Nullable List<T> infos) {
        super();
        this.mList = infos == null ? new ArrayList<>() : infos;
    }

    public BaseAdapter(@Nullable List<T> infos, @Nullable OnRecyclerViewItemClickListener<T> listener) {
        super();
        this.mList = infos == null ? new ArrayList<>() : infos;
        this.mOnItemClickListener = listener;
    }

    /**
     * 让子类实现用以提供 {@link BaseHolder}
     *
     * @param v        用于展示的 {@link View}
     * @param viewType 布局类型
     * @return {@link BaseHolder}
     */
    @NonNull
    public abstract BaseHolder<T> getHolder(@NonNull View v, int viewType);

    /**
     * 提供用于 item 布局的 {@code layoutId}
     *
     * @param viewType 布局类型
     * @return 布局 id
     */
    public abstract int getLayoutId(int viewType);

    /**
     * 创建 {@link BaseHolder}
     *
     * @param parent   父容器
     * @param viewType 布局类型
     * @return {@link BaseHolder}
     */
    @NonNull
    @Override
    public BaseHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(viewType), parent, false);
        final BaseHolder<T> mHolder = getHolder(view, viewType);
        //设置Item点击事件
        mHolder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null && mList.size() > 0) {
                int position = mHolder.getAdapterPosition();
                mOnItemClickListener.onItemClick(view, viewType, getItem(position), position);
            }
        });
        return mHolder;
    }

    /**
     * 绑定数据
     *
     * @param holder   {@link BaseHolder}
     * @param position 在 RecyclerView 中的位置
     */
    @Override
    public void onBindViewHolder(@NonNull BaseHolder<T> holder, int position) {
        holder.setData(mList.get(position), position);
    }

    /**
     * 返回数据总个数
     *
     * @return 数据总个数
     */
    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    /**
     * 获得 RecyclerView 中某个 position 上的 item 数据
     *
     * @param position 在 RecyclerView 中的位置
     * @return 数据
     */
    public T getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    /**
     * 遍历所有 {@link BaseHolder}, 释放他们需要释放的资源
     *
     * @param recyclerView {@link RecyclerView}
     */
    public static void releaseAllHolder(RecyclerView recyclerView) {
        if (recyclerView == null) return;
        for (int i = recyclerView.getChildCount() - 1; i >= 0; i--) {
            final View view = recyclerView.getChildAt(i);
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
            if (viewHolder instanceof BaseHolder) {
                ((BaseHolder) viewHolder).onRelease();
            }
        }
    }

    @Override
    public boolean addAll(@Nullable List<T> list) {
        return addAll(list, false);
    }

    @Override
    public boolean addAll(@Nullable List<T> list, boolean clear) {
        if (list == null) return false;
        if (clear)
            mList.clear();
        boolean result = mList.addAll(list);
        notifyDataSetChanged();
        return result;
    }

    @Override
    public boolean addAll(int position, @Nullable List<T> list) {
        if (list == null) return false;
        if (position < 0 || position >= mList.size()) return false;
        boolean result = mList.addAll(position, list);
        notifyDataSetChanged();
        return result;
    }

    @Override
    public void add(@Nullable T data) {
        if (data == null) return;
        mList.add(data);
        notifyDataSetChanged();
    }

    @Override
    public void add(int position, @Nullable T data) {
        if (data == null) return;
        if (position < 0 || position >= mList.size()) return;
        mList.add(position, data);
        notifyDataSetChanged();
    }

    @Override
    public void clear() {
        mList.clear();
    }

    @Override
    public void clearAll() {
        mList.clear();
        notifyDataSetChanged();
    }

    @Override
    public boolean contains(T data) {
        return mList.contains(data);
    }

    @Override
    public T getData(int index) {
        if (index < 0 || index >= mList.size()) return null;
        return mList.get(index);
    }

    /**
     * 返回数据集合
     *
     * @return 数据集合
     */
    @Override
    public List<T> getAll() {
        return mList;
    }

    @Override
    public void modify(@Nullable T oldData, @Nullable T newData) {
        modify(mList.indexOf(oldData), newData);
    }

    @Override
    public void modify(int index, @Nullable T newData) {
        if (index < 0 || index >= mList.size()) return;
        mList.set(index, newData);
        notifyDataSetChanged();
    }

    @Override
    public boolean remove(@Nullable T data) {
        if (data == null) return false;
        boolean result = mList.remove(data);
        notifyDataSetChanged();
        return result;
    }

    @Override
    public void remove(int index) {
        if (index < 0 || index >= mList.size()) return;
        mList.remove(index);
        notifyDataSetChanged();
    }
}
