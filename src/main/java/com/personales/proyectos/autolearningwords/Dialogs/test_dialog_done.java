package com.personales.proyectos.autolearningwords.Dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.personales.proyectos.autolearningwords.R;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class test_dialog_done extends dialog_parent {

    @BindView(R.id.text_correct) TextView text_correct;
    @BindView(R.id.text_failed) TextView text_failed;
    @BindView(R.id.bt_close_test) Button bt_close_test;

    private int correct = 0;
    private int failed = 0;

    public test_dialog_done(){
        super();
        layout = R.layout.dialog_test_done;
    }

    public static test_dialog_done newInstance(int correct, int failed) {
        test_dialog_done frag = new test_dialog_done();
        frag.correct = correct;
        frag.failed = failed;
        return frag;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        text_correct.setText(((Integer)correct).toString());
        text_failed.setText(((Integer)failed).toString());

        bt_close_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }
}

