package com.example.week4.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by robertsmith on 6/25/14.
 */
public class DialogFrag extends DialogFragment {

    public static EditText input;

    public static DialogFrag newInstance(int title){
        DialogFrag frag = new DialogFrag();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        int title = getArguments().getInt("title");
        input = new EditText(getActivity());

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setView(input)
                .setPositiveButton("Search",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int button) {
                                ((MainActivity) getActivity()).dialogOKClick();
                            }
                        }
                )
                .setNegativeButton("Reset",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int button) {
                                ((MainActivity) getActivity()).dialogResetClick();
                            }
                        }
                )
                .create();
    }
}
