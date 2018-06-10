package com.personales.proyectos.autolearningwords.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopeer.itemtouchhelperextension.Extension;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;
import com.personales.proyectos.autolearningwords.Base.baseViewHolder;
import com.personales.proyectos.autolearningwords.Interfaces.itemVisitable;
import com.personales.proyectos.autolearningwords.R;

import java.util.ArrayList;
import java.util.List;

public class MainRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int ITEM_TYPE_ACTION_WIDTH_NO_SPRING = 1002;
    private List<TestModel> mDatas;
    private Context mContext;
    private ItemTouchHelperExtension mItemTouchHelperExtension;
    private ArrayList<TestModel> selected_items;

    public void add_selected_item(TestModel item){
        selected_items.add(item);
        baseViewHolder.MULTISELECT_ACTIVED = true;
    }

    public void remove_selected_item(TestModel item){
        selected_items.remove(item);
    }

    public void clear_selected_item(){
        selected_items = new ArrayList<>();
        baseViewHolder.MULTISELECT_ACTIVED = false;
    }

    public ArrayList<TestModel> get_selected_items(){
        return selected_items;
    }

    public MainRecyclerAdapter(Context context) {
        mDatas = new ArrayList<>();
        mContext = context;
        selected_items=new ArrayList<>();
    }

    public void setDatas(List<TestModel> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
    }

    public void updateData(List<TestModel> datas) {
        setDatas(datas);
        notifyDataSetChanged();
    }

    public void setItemTouchHelperExtension(ItemTouchHelperExtension itemTouchHelperExtension) {
        mItemTouchHelperExtension = itemTouchHelperExtension;
    }

    private LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.list_item_main, parent, false);
        return new ItemSwipeWithActionWidthNoSpringViewHolder(view);
    }

    public List<TestModel> get_viewModels(){
        return mDatas;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ItemBaseViewHolder baseViewHolder = (ItemBaseViewHolder) holder;
        baseViewHolder.bind(mDatas.get(position));
        if(selected_items.contains(mDatas.get(position)))
            holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_selected_state));
        else
            holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_normal_state));

        baseViewHolder.mViewContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Item Content click: #" + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void move(int from, int to) {
        TestModel prev = mDatas.remove(from);
        mDatas.add(to > from ? to - 1 : to, prev);
        notifyItemMoved(from, to);
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE_ACTION_WIDTH_NO_SPRING;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class ItemBaseViewHolder extends RecyclerView.ViewHolder {
        TextView mTextTitle;
        TextView mTextIndex;
        public View mViewContent;
        public View mActionContainer;

        public ItemBaseViewHolder(View itemView) {
            super(itemView);
            mTextTitle = (TextView) itemView.findViewById(R.id.text_list_main_title);
            mTextIndex = (TextView) itemView.findViewById(R.id.text_list_main_index);
            mViewContent = itemView.findViewById(R.id.view_list_main_content);
            mActionContainer = itemView.findViewById(R.id.view_list_repo_action_container);
        }

        public void bind(TestModel testModel) {
            mTextTitle.setText(testModel.title);
            mTextIndex.setText("#" + testModel.position);
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        mItemTouchHelperExtension.startDrag(ItemBaseViewHolder.this);
                    }
                    return true;
                }
            });
        }
    }

    public class ItemSwipeWithActionWidthNoSpringViewHolder extends ItemBaseViewHolder implements Extension {
        View mActionViewRefresh;

        public ItemSwipeWithActionWidthNoSpringViewHolder(View itemView) {
            super(itemView);
            mActionViewRefresh = itemView.findViewById(R.id.view_list_repo_action_update);
        }

        @Override
        public float getActionWidth() {
            return mActionContainer.getWidth();
        }
    }

}