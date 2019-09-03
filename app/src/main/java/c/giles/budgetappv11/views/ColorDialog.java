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
import java.util.Objects;

import c.giles.budgetappv11.R;

public class ColorDialog extends AppCompatDialogFragment {

    private ColorDialogListener listener;
    List<Integer> colors = new ArrayList<>();
//    List<String> colorStrings = new ArrayList<>();
    final List<Boolean> selectedList = new ArrayList<>();
    List<ImageButton> colorButtons = new ArrayList<>();
    List<LinearLayout> subLayouts = new ArrayList<>();
    LinearLayout colorLayout;
    Integer selectedColor = -1;
    private final int BUTTONS_IN_COLUMN = 3;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();

        View view = inflater.inflate(R.layout.color_dialog, null);


        colorLayout = view.findViewById(R.id.color_layout);

        //Set up color buttons selection
        colors.add(ContextCompat.getColor(getActivity(), R.color.myDefault));
        colors.add(ContextCompat.getColor(getActivity(), R.color.myScarlet));
        colors.add(ContextCompat.getColor(getActivity(), R.color.myRed));
        colors.add(ContextCompat.getColor(getActivity(), R.color.myRedOrange));
        colors.add(ContextCompat.getColor(getActivity(), R.color.myOrange));
        //colors.add(ContextCompat.getColor(getActivity(), R.color.myOrangeYellow));
        colors.add(ContextCompat.getColor(getActivity(), R.color.myYellow));
        colors.add(ContextCompat.getColor(getActivity(), R.color.myYellowGreen));
        colors.add(ContextCompat.getColor(getActivity(), R.color.myGreen));
        colors.add(ContextCompat.getColor(getActivity(), R.color.myAqua));
        colors.add(ContextCompat.getColor(getActivity(), R.color.myLightBlue));
        colors.add(ContextCompat.getColor(getActivity(), R.color.myDarkBlue));
        colors.add(ContextCompat.getColor(getActivity(), R.color.myViolet));
        colors.add(ContextCompat.getColor(getActivity(), R.color.myPink));
        colors.add(ContextCompat.getColor(getActivity(), R.color.myLavender));
        colors.add(ContextCompat.getColor(getActivity(), R.color.myMagenta));


        //Initialize lists
        for(int i = 0; i < colors.size(); i++){
            selectedList.add(false);
            colorButtons.add(new ImageButton(getActivity()));
        }
        for(ImageButton button : colorButtons){
            button.setLayoutParams(new LinearLayout.LayoutParams(75, 75));
        }

        //Select the color that the parent class defined as the "selected" color
        if(colors.indexOf(selectedColor) < 0) {
            selectedList.set(0, true);
        } else {
            selectedList.set(colors.indexOf(selectedColor), true);
        }

        makeButtons();

        builder.setView(view)
                .setTitle("Choose a Color")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Do nothing - close dialog
                    }
                })
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.applyColor(colors.get(selectedList.indexOf(true)));
                    }
                });
        return builder.create();
    }

    private void makeButtons(){
        //Reset everything before redrawing buttons
        for(int i = 0; i < subLayouts.size(); i++){
            subLayouts.get(i).removeAllViews();
        }
        subLayouts = new ArrayList<>();
        colorLayout.removeAllViews();

        //Add click behavior
        for(int i = 0; i < colorButtons.size(); i++) {
            if(i % 3 == 0){
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
            unselected.setSize(100, 100);

            final GradientDrawable selected = new GradientDrawable();
            selected.setShape(GradientDrawable.OVAL);
            selected.setColor(Color.parseColor(String.format("#%06X", (0xFFFFFF & colors.get(i)))));
            selected.setSize(100, 100);
            selected.setStroke(5, Color.parseColor("#D2D1D2"));


            if(selectedList.get(colorButtons.indexOf(currentButton))){
                currentButton.setBackground(selected);
            } else {
                currentButton.setBackground(unselected);
            }
            currentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentButton.startAnimation(AnimationUtils.loadAnimation(Objects.requireNonNull(getActivity()).getBaseContext(), android.R.anim.fade_in));
                    for(int k = 0; k < selectedList.size(); k++){
                        selectedList.set(k, false);
                    }
                    selectedList.set(j, true);
                    makeButtons();
//                    if (selectedList.get(j)) {
//                        selectedList.set(j, false);
//                        currentButton.setBackground(unselected);
//                    } else {
//                        selectedList.set(j, true);
//                        currentButton.setBackground(selected);
//                    }
                }
            });
        }

        //Add buttons to sublayouts
        for(int i = 0; i < colorButtons.size(); i = i + BUTTONS_IN_COLUMN){
            LinearLayout subLayout = new LinearLayout(getActivity());
            subLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            subLayout.setOrientation(LinearLayout.VERTICAL);

            for(int j = 0; j < BUTTONS_IN_COLUMN; j++) {
                Space space = new Space(getActivity());
                space.setLayoutParams(new LinearLayout.LayoutParams(0, 20));
                subLayout.addView(colorButtons.get(i + j));
                subLayout.addView(space);
            }

            subLayouts.add(subLayout);
        }

        //Add sublayouts to dialog
        for(int i = 0; i < subLayouts.size(); i++){
            Space space = new Space(getActivity());
            space.setLayoutParams(new LinearLayout.LayoutParams(20,0));
            colorLayout.addView(subLayouts.get(i));
            if(i!=subLayouts.size()-1) {
                colorLayout.addView(space);
            }
        }

//        //Add a space at the end
//        Space endSpace = new Space(getActivity());
//        endSpace.setLayoutParams(new LinearLayout.LayoutParams(30, 0));
//        colorLayout.addView(endSpace);
    }

    public void setSelectedColor(Integer color){
        selectedColor = color;
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
        void applyColor(Integer color);
    }
}
