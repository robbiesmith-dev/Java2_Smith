package com.example.week4.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

/**
 * Created by robertsmith on 6/25/14.
 */
public class PrefDialogFrag extends DialogFragment {

    public static EditText input;

    public static PrefDialogFrag newInstance(int title){
        PrefDialogFrag frag = new PrefDialogFrag();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        int title = getArguments().getInt("title");
        input = new EditText(getActivity());
        input.setHint("Username");

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setView(input)
                .setPositiveButton("Log In",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface prefDialog, int button) {
                                ((MainActivity) getActivity()).prefDialogOKClick();
                            }
                        }
                )
                .setNegativeButton("Log Out",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface prefDialog, int button) {
                                ((MainActivity) getActivity()).prefDialogCancelClick();
                            }
                        }
                )
                .create();
    }
}
