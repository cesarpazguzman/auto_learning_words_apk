package com.personales.proyectos.autolearningwords.Dialogs;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.personales.proyectos.autolearningwords.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class dialog_contact extends dialog_parent {

    @BindView(R.id.bt_gmail)
    ImageButton bt_gmail;
    @BindView(R.id.bt_linkedin) ImageButton bt_linkedin;

    public dialog_contact(){
        super();
        layout = R.layout.dialog_contact;
    }

    public static dialog_contact newInstance() {
        dialog_contact frag = new dialog_contact();
        return frag;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        bt_gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("plain/text");
                Uri data = Uri.parse("mailto:"+"cesar.pazguzman@gmail.com");
                intent.setData(data);
                startActivity(intent);
                dismiss();
            }
        });

        bt_linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://add/%@" + "cesarpazguzman"));
                final PackageManager packageManager = getContext().getPackageManager();
                final List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                if (list.isEmpty()) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.linkedin.com/profile/view?id=" + "cesarpazguzman"));
                }
                startActivity(intent);
                dismiss();
            }
        });

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }
}
