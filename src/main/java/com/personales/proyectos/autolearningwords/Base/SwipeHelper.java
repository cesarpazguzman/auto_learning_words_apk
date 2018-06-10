package com.personales.proyectos.autolearningwords.Base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.personales.proyectos.autolearningwords.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public abstract class SwipeHelper extends ItemTouchHelper.SimpleCallback {

    public static final int BUTTON_WIDTH = 150;
    private RecyclerView recyclerView;
    private GestureDetector gestureDetector;
    private static int swipedPos = -1;
    private static int oldPos = -1;
    private Queue<Integer> recoverQueue;
    private static boolean IS_TOUCH = false;
    private Map<Integer, List<UnderlayButton>> buttonsBuffer;
    private List<UnderlayButton> buttons;

    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            for (UnderlayButton button : buttons){
                if(button.onClick(e.getX(), e.getY()))
                    break;
            }

            return false;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }
    };

    public static void setSwipedPos(int value){
        swipedPos = value;
    }

    public static void setISTOUCH(boolean value){
        IS_TOUCH = value;
    }

    public static int getSwipedPos(){ return swipedPos;}
    public static boolean getISTOUCH(){return IS_TOUCH;}


    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent e) {
            //recyclerView.getAdapter().notifyItemChanged(-1);
            if (swipedPos < 0) return false;
            Point point = new Point((int) e.getRawX(), (int) e.getRawY());

            RecyclerView.ViewHolder swipedViewHolder = recyclerView.findViewHolderForAdapterPosition(swipedPos);
            if(swipedViewHolder!=null){
                View swipedItem = swipedViewHolder.itemView;
                Rect rect = new Rect();
                swipedItem.getGlobalVisibleRect(rect);
                if (e.getAction() == MotionEvent.ACTION_DOWN || e.getAction() == MotionEvent.ACTION_UP ||e.getAction() == MotionEvent.ACTION_MOVE) {
                    if ( swipedPos==oldPos){
                        reset_swipe(-1);
                    }
                    else if (rect.top < point.y && rect.bottom > point.y){
                        gestureDetector.onTouchEvent(e);

                    }
                    else {
                        reset_swipe(swipedPos);
                    }
                }
            }
            return false;
        }
    };

    public void reset_swipe(int swipedPos2){
        recoverQueue.add(swipedPos2);
        setSwipedPos(-1);
        oldPos = -1;
        recoverSwipedItem();
    }

    public SwipeHelper(Context context, RecyclerView recyclerView) {
        super(0, ItemTouchHelper.LEFT);
        this.recyclerView = recyclerView;
        this.gestureDetector = new GestureDetector(context, gestureListener);
        this.recyclerView.setOnTouchListener(onTouchListener);
        recyclerView.getItemAnimator().setChangeDuration(350);
        buttonsBuffer = new HashMap<>();
        this.buttons = new ArrayList<>();
        recoverQueue = new LinkedList<Integer>(){
            @Override
            public boolean add(Integer o) {
                if (contains(o))
                    return false;
                else
                    return super.add(o);
            }
        };

        attachSwipe();
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        oldPos = swipedPos;
        setSwipedPos(viewHolder.getAdapterPosition());

        recoverQueue.add(oldPos);

        if (buttonsBuffer.containsKey(swipedPos))
            buttons = buttonsBuffer.get(swipedPos);
        else
            buttons.clear();

        buttonsBuffer.clear();

        recoverSwipedItem();



    }

    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return 5f * defaultValue;
    }

    @Override
    public float getSwipeVelocityThreshold(float defaultValue) {
        return 30.0f * defaultValue;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (dY != 0 && dX == 0){

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            setISTOUCH(false);
            return;
        }
        int pos = viewHolder.getAdapterPosition();
        float translationX = dX;
        View itemView = viewHolder.itemView;

        if(pos == oldPos) {
            SwipeHelper.setISTOUCH(false);
            return;
        }
        if (pos < 0){
            setSwipedPos(pos);
            SwipeHelper.setISTOUCH(false);
            return;
        }

        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            if(dX < 0) {
                List<UnderlayButton> buffer = new ArrayList<>();

                if (!buttonsBuffer.containsKey(pos)){
                    instantiateUnderlayButton(viewHolder, buffer);
                    buttonsBuffer.put(pos, buffer);
                }
                else {
                    buffer = buttonsBuffer.get(pos);
                }
                translationX = dX * 1 * BUTTON_WIDTH / itemView.getWidth();
                SwipeHelper.setISTOUCH(true);
                drawButtons(c, itemView, buffer, pos, translationX);
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive);
    }


    private void drawButtons(Canvas c, View itemView, List<UnderlayButton> buffer, int pos, float dX){
        float right = itemView.getRight();
        float dButtonWidth = (-1) * dX / buffer.size();

        for (UnderlayButton button : buffer) {
            float left = right - dButtonWidth;
            button.onDraw(
                    c,
                    new RectF(
                            left,
                            itemView.getTop(),
                            right,
                            itemView.getBottom()
                    ),
                    pos
            );

            right = left;
        }
    }

    public abstract void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons);


    public static class UnderlayButton {
        private String text;
        private Drawable imageResId;
        private int color;
        private int pos;
        private RectF clickRegion;
        private UnderlayButtonClickListener clickListener;

        public UnderlayButton(String text, Drawable imageResId, int color, UnderlayButtonClickListener clickListener) {
            this.text = text;
            this.imageResId = imageResId;
            this.color = color;
            this.clickListener = clickListener;
        }

        public boolean onClick(float x, float y){
            if (clickRegion != null && clickRegion.contains(x, y)){
                clickListener.onClick(pos);
                return true;
            }

            return false;
        }

        public void onDraw(Canvas c, RectF rect, int pos){
            Paint p = new Paint();

            // Draw background
            p.setColor(color);
            c.drawRect(new Rect((int)(rect.left*0.5f),(int)(rect.top*0.5f),(int)(rect.right*0.5f),(int)(rect.bottom*0.5f)), p);

            imageResId.setBounds((int)rect.left, (int)rect.top, (int)rect.right, (int)rect.bottom);
            imageResId.draw(c);

            clickRegion = rect;
            this.pos = pos;
        }
    }

    /*public static void setSwipedPos(int value){
        swipedPos = value;
    }*/
    private synchronized void recoverSwipedItem(){
        while (!recoverQueue.isEmpty()){
            int pos = recoverQueue.poll();
            if (pos > -1) {
                recyclerView.getAdapter().notifyItemChanged(pos);
            }
        }

        if(SwipeHelper.IS_TOUCH){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SwipeHelper.setISTOUCH(false);
                }
            }, 350);
        }


    }

    public void attachSwipe(){
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public interface UnderlayButtonClickListener {
        void onClick(int pos);
    }
}