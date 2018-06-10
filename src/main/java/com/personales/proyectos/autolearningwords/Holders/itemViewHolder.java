package com.personales.proyectos.autolearningwords.Holders;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

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
    }

    @Override
    public void bind(item_model element) {
        this.viewModel = element;

        item_name.setText(viewModel.getName());
        item_traduccion.setText(viewModel.getTranslation());
        txt_examples_item.setText(text_examples());

        cl_button_expand.setVisibility(
                txt_examples_item.getText().toString().isEmpty() ?
                        View.GONE :
                        View.VISIBLE);

        card_item.setOnClickListener(
                !txt_examples_item.getText().toString().isEmpty() ?
                        collapse_expand_listener :
                        null);

    }

    private SpannableStringBuilder text_examples(){
        String string_final = "";
        if(!viewModel.getExample1().isEmpty()){
            string_final += "Ejemplo 1: "+viewModel.getExample1();
            if(!viewModel.getExample2().isEmpty() || !viewModel.getComment().isEmpty())
                string_final += "\n\n";
        }
        if(!viewModel.getExample2().isEmpty()){
            string_final += "Ejemplo 2: "+viewModel.getExample2();
            if(!viewModel.getComment().isEmpty())
                string_final += "\n\n";
        }
        if(!viewModel.getComment().isEmpty()){
            string_final += "Comentario: "+viewModel.getComment();
        }

        SpannableStringBuilder str = new SpannableStringBuilder(string_final);
        str = set_text_bold(str, "Ejemplo 1:", string_final);
        str = set_text_bold(str, "Ejemplo 2:", string_final);
        str = set_text_bold(str, "Comentario:", string_final);
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
            //Si no está en eliminado conjunto:
            if(!baseViewHolder.MULTISELECT_ACTIVED && SwipeHelper.getSwipedPos()==-1){
                bt_expand_item.setVisibility(
                        expanded ?
                                View.VISIBLE :
                                View.GONE);

                bt_collapse_item.setVisibility(
                        expanded ?
                                View.GONE :
                                View.VISIBLE);

                txt_examples_item.setVisibility(
                        expanded ?
                                View.GONE :
                                View.VISIBLE);

                expanded = !expanded;
            }
        }
    };

}
