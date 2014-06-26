package com.example.week4.app.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

import com.example.week4.app.Activites.MainActivity;

//Robert Smith
//Java 2 Project 4 Term 1406
//June 26 2014
//Dialog Frag - This Displays an Dialog Fragment that allows the user to search the movie list

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
        input.setHint("Movie Name");

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
