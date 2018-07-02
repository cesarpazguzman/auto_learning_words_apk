package com.personales.proyectos.autolearningwords.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.personales.proyectos.autolearningwords.DataBase.databaseManager;
import com.personales.proyectos.autolearningwords.Interfaces.itemVisitable;
import com.personales.proyectos.autolearningwords.Models.item_model;
import com.personales.proyectos.autolearningwords.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class test_dialog extends dialog_parent {

    @BindView(R.id.counter_words) TextView counter_words;
    @BindView(R.id.test_original) TextView test_original;
    @BindView(R.id.fail_button) ImageButton bt_fail;
    @BindView(R.id.correct_button) ImageButton bt_correct;
    @BindView(R.id.test_traduccion) TextView test_traduccion;
    @BindView(R.id.test_examples) TextView test_examples;

    private ArrayList<itemVisitable> failed_words;
    private ArrayList<itemVisitable> all_words;
    private int current_word = 0;

    public test_dialog(){
        super();
        failed_words = new ArrayList<>();
        layout = R.layout.dialog_test;
    }

    public static test_dialog newInstance(ArrayList<itemVisitable> all_words) {
        test_dialog frag = new test_dialog();
        frag.all_words = all_words;
        return frag;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        databaseManager db_manager = databaseManager.getInstance(getContext());

        next_word();

        test_traduccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                see_answer();
            }
        });

        bt_correct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                failed_words.add(all_words.get(current_word-1));
                next_word();
            }
        });

        bt_fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next_word();
            }
        });

        setCancelable(false);

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()){
            @Override
            public void onBackPressed() {
                //do your stuff
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.test_cancel_warning)
                        .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                                test_dialog.this.dismiss();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                // Create the AlertDialog object and return it
                builder.create();
                builder.show();
            }
        };
    }



    private void see_answer(){
        test_traduccion.setText(((item_model)(all_words.get(current_word-1))).getTranslation());
        test_examples.setVisibility(View.VISIBLE);
        test_examples.setText(text_examples());
        bt_fail.setVisibility(View.VISIBLE);
        bt_correct.setVisibility(View.VISIBLE);
    }

    private void next_word(){

        if(current_word == all_words.size()){
            FragmentManager fm = getActivity().getSupportFragmentManager();
            test_dialog_done folders_dialog_frag = test_dialog_done.newInstance(all_words.size()-failed_words.size(),
                    failed_words.size(), failed_words);

            folders_dialog_frag.show(fm, "new_test_dialog");
            dismiss();
        }else{
            ++current_word;
            counter_words.setText(current_word+"/"+all_words.size());
            test_original.setText(all_words.get(current_word-1).getName());
            test_traduccion.setText(R.string.see_answer);
            test_examples.setVisibility(View.INVISIBLE);
            test_examples.setText("");
            bt_fail.setVisibility(View.GONE);
            bt_correct.setVisibility(View.GONE);
        }
    }

    private SpannableStringBuilder text_examples(){
        String string_final = "";
        item_model viewModel = (item_model) all_words.get(current_word-1);
        if(!viewModel.getExample1().isEmpty()){
            string_final += R.string.example1+": "+viewModel.getExample1();
            if(!viewModel.getExample2().isEmpty() || !viewModel.getComment().isEmpty())
                string_final += "\n\n";
        }
        if(!viewModel.getExample2().isEmpty()){
            string_final += R.string.example2+": "+viewModel.getExample2();
            if(!viewModel.getComment().isEmpty())
                string_final += "\n\n";
        }
        if(!viewModel.getComment().isEmpty()){
            string_final += R.string.comment_min+": "+viewModel.getComment();
        }

        SpannableStringBuilder str = new SpannableStringBuilder(string_final);
        str = set_text_bold(str, R.string.example1+":", string_final);
        str = set_text_bold(str, R.string.example2+":", string_final);
        str = set_text_bold(str, R.string.comment_min+":", string_final);
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
}

