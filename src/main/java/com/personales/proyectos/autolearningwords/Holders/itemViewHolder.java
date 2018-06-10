package com.personales.proyectos.autolearningwords.Holders;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.personales.proyectos.autolearningwords.Base.SwipeHelper;
import com.personales.proyectos.autolearningwords.Base.baseViewHolder;
import com.personales.proyectos.autolearningwords.Models.folder_model;
import com.personales.proyectos.autolearningwords.Models.item_model;
import com.personales.proyectos.autolearningwords.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class itemViewHolder extends baseViewHolder<item_model> {
    public static final int LAYOUT = R.layout.item_main;
    private item_model viewModel;
    private Context context;
    private Boolean expanded = false;

    @BindView(R.id.item_original)TextView item_name;
    @BindView(R.id.item_traduccion)TextView item_traduccion;
    @BindView(R.id.txt_examples_item)TextView txt_examples_item;
    @BindView(R.id.bt_collapse_item)ImageButton bt_collapse_item;
    @BindView(R.id.bt_expand_item)ImageButton bt_expand_item;
    @BindView(R.id.cl_button_expand)FrameLayout cl_button_expand;
    @BindView(R.id.card_item)CardView card_item;

    public itemViewHolder(View itemView){
        super(itemView);
        context = itemView.getContext();
        ButterKnife.bind(this, itemView);

        card_item.setOnClickListener(collapse_expand_listener);
    }

    @Override
    public void bind(item_model element) {
        this.viewModel = element;
        item_name.setText(viewModel.getName());
        item_traduccion.setText(viewModel.getTranslation());

        String string_final = "";
        SpannableStringBuilder str1, str2, str3;
        if(viewModel.getExample1()!=null && !viewModel.getExample1().isEmpty()){
            string_final += "Ejemplo 1: "+viewModel.getExample1();
            if(viewModel.getExample2()!=null && !viewModel.getExample2().isEmpty() ||
                    viewModel.getComment()!=null && !viewModel.getComment().isEmpty())
                string_final += "\n\n";
        }
        if(viewModel.getExample2()!=null && !viewModel.getExample2().isEmpty()){
            string_final += "Ejemplo 2: "+viewModel.getExample2();
            if(viewModel.getComment()!=null && !viewModel.getComment().isEmpty())
                string_final += "\n\n";
        }
        if(viewModel.getComment()!=null && !viewModel.getComment().isEmpty()){
            string_final += "Comentario: "+viewModel.getComment();
        }

        SpannableStringBuilder str = new SpannableStringBuilder(string_final);
        if(string_final.contains("Ejemplo 1:")){
            str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), string_final.indexOf("Ejemplo 1:"),
                    string_final.indexOf("Ejemplo 1:") + "Ejemplo 1:".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if(string_final.contains("Ejemplo 2:")){
            str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), string_final.indexOf("Ejemplo 2:"),
                    string_final.indexOf("Ejemplo 2:") + "Ejemplo 2:".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if(string_final.contains("Comentario:")){
            str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), string_final.indexOf("Comentario:"),
                    string_final.indexOf("Comentario:") + "Comentario:".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }


        txt_examples_item.setText(str);

        if(txt_examples_item.getText().toString().isEmpty()){
            cl_button_expand.setVisibility(View.GONE);
        }
        else{
            cl_button_expand.setVisibility(View.VISIBLE);
        }
    }

    private View.OnClickListener collapse_expand_listener = new View.OnClickListener() {
        public void onClick(View v) {
            if(!baseViewHolder.MULTISELECT_ACTIVED && SwipeHelper.getSwipedPos()==-1&& !txt_examples_item.getText().toString().isEmpty()){
                if(expanded){
                    bt_expand_item.setVisibility(View.VISIBLE);
                    bt_collapse_item.setVisibility(View.GONE);
                    txt_examples_item.setVisibility(View.GONE);
                }else{
                    bt_expand_item.setVisibility(View.GONE);
                    bt_collapse_item.setVisibility(View.VISIBLE);
                    txt_examples_item.setVisibility(View.VISIBLE);
                }
                expanded = !expanded;
            }
        }
    };

}
