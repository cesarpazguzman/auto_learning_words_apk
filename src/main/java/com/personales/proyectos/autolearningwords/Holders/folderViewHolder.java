package com.personales.proyectos.autolearningwords.Holders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.personales.proyectos.autolearningwords.Base.SwipeHelper;
import com.personales.proyectos.autolearningwords.Base.baseViewHolder;
import com.personales.proyectos.autolearningwords.MainActivity;
import com.personales.proyectos.autolearningwords.Models.folder_model;
import com.personales.proyectos.autolearningwords.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class folderViewHolder extends baseViewHolder<folder_model> {
    public static final int LAYOUT = R.layout.item_folders_main;
    public static final String EXTRA_PARENTID = "parent_id";
    private folder_model viewModel;
    private Context context;

    @BindView(R.id.icon_folder)ImageView viewImageFolder;
    @BindView(R.id.folder_name)TextView folder_name;
    @BindView(R.id.card_item)CardView card_item;

    public folderViewHolder(View itemView){
        super(itemView);
        context = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(folder_model element) {
        this.viewModel = element;
        folder_name.setText(viewModel.getName());

        card_item.setOnClickListener(click_listener);
    }

    private View.OnClickListener click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            if(!baseViewHolder.MULTISELECT_ACTIVED && SwipeHelper.getSwipedPos()==-1 ){
                SwipeHelper.setISTOUCH(false);
                //Cambio de elementos en la lista
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra(EXTRA_PARENTID, viewModel.getId());
                context.startActivity(intent);
            }
        }
    };
}
