package com.personales.proyectos.autolearningwords.Holders;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.personales.proyectos.autolearningwords.Adapter.multi_select_adapter;
import com.personales.proyectos.autolearningwords.Base.SwipeHelper;
import com.personales.proyectos.autolearningwords.Base.baseViewHolder;
import com.personales.proyectos.autolearningwords.Models.item_model;
import com.personales.proyectos.autolearningwords.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class itemViewHolder extends baseViewHolder<item_model> {
    public static final int LAYOUT = R.layout.item_main;
    private item_model viewModel;
    private Context context;

    @BindView(R.id.item_original)TextView item_name;
    @BindView(R.id.item_traduccion)TextView item_traduccion;
    @BindView(R.id.txt_examples_item)TextView txt_examples_item;
    @BindView(R.id.bt_collapse_item)ImageButton bt_collapse_item;
    @BindView(R.id.bt_expand_item)ImageButton bt_expand_item;
    @BindView(R.id.cl_button_expand)FrameLayout cl_button_expand;
    @BindView(R.id.card_item)CardView card_item;
    @BindView(R.id.cl_examples_item)ConstraintLayout cl_examples_item;

    public itemViewHolder(View itemView){
        super(itemView);
        context = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(item_model element) {
        this.viewModel = element;
        viewModel.expanded = false;
        item_name.setText(viewModel.getName());
        item_traduccion.setText(viewModel.getTranslation());
        txt_examples_item.setText(text_examples());

        cl_button_expand.setVisibility(
                txt_examples_item.getText().toString().isEmpty() ?
                        View.INVISIBLE :
                        View.VISIBLE);

        card_item.setOnClickListener(
                !txt_examples_item.getText().toString().isEmpty() ?
                        collapse_expand_listener :
                        null);

    }

    private SpannableStringBuilder text_examples(){
        String string_final = "";
        if(!viewModel.getExample1().isEmpty()){
            string_final += context.getResources().getString(R.string.example1)+": "+viewModel.getExample1();
            if(!viewModel.getExample2().isEmpty() || !viewModel.getComment().isEmpty())
                string_final += "\n\n";
        }
        if(!viewModel.getExample2().isEmpty()){
            string_final += context.getResources().getString(R.string.example2)+": "+viewModel.getExample2();
            if(!viewModel.getComment().isEmpty())
                string_final += "\n\n";
        }
        if(!viewModel.getComment().isEmpty()){
            string_final += context.getResources().getString(R.string.comment_min)+": "+viewModel.getComment();
        }

        SpannableStringBuilder str = new SpannableStringBuilder(string_final);
        str = set_text_bold(str, context.getResources().getString(R.string.example1)+":", string_final);
        str = set_text_bold(str, context.getResources().getString(R.string.example2)+":", string_final);
        str = set_text_bold(str, context.getResources().getString(R.string.comment_min)+":", string_final);
        return str;
    }

    //Devuelve un texto con una cadena en negrita
    private SpannableStringBuilder set_text_bold(SpannableStringBuilder str, String text, String string_final){
        if(string_final.contains(text)){
            str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), string_final.indexOf(text),
                    string_final.indexOf(text) + text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return str;
    }

    private View.OnClickListener collapse_expand_listener = new View.OnClickListener() {
        public void onClick(View v) {
            if(!multi_select_adapter.MULTISELECT_ACTIVED && SwipeHelper.getSwipedPos()==-1 ){
                SwipeHelper.setISTOUCH(false);
                bt_expand_item.setVisibility(
                        viewModel.expanded ?
                                View.VISIBLE :
                                View.GONE);

                bt_collapse_item.setVisibility(
                        viewModel.expanded ?
                                View.GONE :
                                View.VISIBLE);

                cl_examples_item.setVisibility(
                        viewModel.expanded ?
                                View.GONE :
                                View.VISIBLE);

                viewModel.expanded = !viewModel.expanded;
            }
        }
    };
}
