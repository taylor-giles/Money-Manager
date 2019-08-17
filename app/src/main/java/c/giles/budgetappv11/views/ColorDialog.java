package c.giles.budgetappv11.views;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Space;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import c.giles.budgetappv11.R;

public class ColorDialog extends AppCompatDialogFragment {

    private ColorDialogListener listener;
    List<Integer> colors = new ArrayList<>();
    final List<Boolean> selectedList = new ArrayList<>();
    List<ImageButton> colorButtons = new ArrayList<>();
    List<LinearLayout> subLayouts = new ArrayList<>();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.color_dialog, null);


        LinearLayout colorLayout = view.findViewById(R.id.color_layout);

        //Set up color buttons selection
        colors.add(ContextCompat.getColor(getActivity(), R.color.myRed));
        colors.add(ContextCompat.getColor(getActivity(), R.color.myRedOrange));
        colors.add(ContextCompat.getColor(getActivity(), R.color.myOrange));
        colors.add(ContextCompat.getColor(getActivity(), R.color.myYellow));
        colors.add(ContextCompat.getColor(getActivity(), R.color.myYellowGreen));
        colors.add(ContextCompat.getColor(getActivity(), R.color.myGreen));
        colors.add(ContextCompat.getColor(getActivity(), R.color.myAqua));
        colors.add(ContextCompat.getColor(getActivity(), R.color.myLightBlue));
        colors.add(ContextCompat.getColor(getActivity(), R.color.myDarkBlue));
        colors.add(ContextCompat.getColor(getActivity(), R.color.myViolet));
        colors.add(ContextCompat.getColor(getActivity(), R.color.myPink));
        colors.add(ContextCompat.getColor(getActivity(), R.color.myMagenta));

        for(int i = 0; i < colors.size(); i++){
            selectedList.add(false);
            colorButtons.add(new ImageButton(getActivity()));
        }

        selectedList.set(0, true);


        for(int i = 0; i < colorButtons.size(); i++) {
            if(i % 2 == 0){
                LinearLayout subLayout = new LinearLayout(getActivity());
                subLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                subLayout.setOrientation(LinearLayout.VERTICAL);
                subLayouts.add(subLayout);
            }
            final int j = i;
            final ImageButton currentButton = colorButtons.get(i);

            final GradientDrawable unselected = new GradientDrawable();
            unselected.setShape(GradientDrawable.OVAL);
            unselected.setColor(Color.parseColor(String.format("#%06X", (0xFFFFFF & colors.get(i)))));
            unselected.setSize(144, 144);

            final GradientDrawable selected = new GradientDrawable();
            selected.setShape(GradientDrawable.OVAL);
            selected.setColor(Color.parseColor(String.format("#%06X", (0xFFFFFF & colors.get(i)))));
            selected.setSize(159, 159);
            selected.setStroke(15, Color.parseColor("#D2D1D2"));


            currentButton.setBackground(unselected);
            currentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentButton.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(), android.R.anim.fade_in));
                    if (selectedList.get(j)) {
                        selectedList.set(j, false);
                        currentButton.setBackground(unselected);
                    } else {
                        selectedList.set(j, true);
                        currentButton.setBackground(selected);
                    }
                }
            });
        }

        for(int i = 0; i < colorButtons.size(); i = i + 2){
            LinearLayout subLayout = new LinearLayout(getActivity());
            subLayout.setLayoutParams(new LinearLayout.LayoutParams(170, LinearLayout.LayoutParams.WRAP_CONTENT));
            subLayout.setOrientation(LinearLayout.VERTICAL);

            Space space = new Space(getActivity());
            space.setLayoutParams(new LinearLayout.LayoutParams(0, 20));
            subLayout.addView(colorButtons.get(i));
            subLayout.addView(space);
            subLayout.addView(colorButtons.get(i+1));
            subLayouts.add(subLayout);
        }

        for(int i = 0; i < subLayouts.size(); i++){
            Space space = new Space(getActivity());
            space.setLayoutParams(new LinearLayout.LayoutParams(20,0));
            colorLayout.addView(subLayouts.get(i));
            colorLayout.addView(space);
        }


        builder.setView(view)
                .setTitle("Choose a Color")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }

                })
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                });


        return builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ColorDialogListener) context;
        } catch(ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement ColorDialogListener");
        }
    }


    public interface ColorDialogListener {
        void applyColor(String color);
    }
}
