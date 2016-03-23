package com.example.listenandrepeat.musicandmatch.Setting;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.listenandrepeat.musicandmatch.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DialogPositionFragment extends DialogFragment {

    private static final int BASE_POSITION = 10;
    private static final int GUITAR_POSITION= 11;
    private static final int DRUM_POSITION= 12;
    private static final int KEYBOARD_POSITION =13;
    private static final int VOCAL_POSITION =14;
    private static final int RAP_POSITION =15;
    private static final int COMPOSE_POSITION =16;
    private static final int _POSITION =17;

    private static final int BALLAD_GENRE = 0;
    private static final int RB_GENRE = 1;
    private static final int HIPHOP_GENRE = 2;
    private static final int ROCK_GENRE = 3;
    private static final int DANCE_GENRE = 4;
    private static final int INDI_GENRE = 5;
    private static final int ELEC_GENRE = 6;
    private static final int TROT_GENRE = 7;

    public interface OnPositionSelectionListener {
        public void onPositionSelected(int position);
    }

    OnPositionSelectionListener mSelectedListener;
    public void setOnPositionSelectionListener(OnPositionSelectionListener listener) {
        mSelectedListener = listener;
    }

    public DialogPositionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.MyDialog);
    }

    ImageView imageView;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;
    ImageView imageView6;
    ImageView imageView7;
    ImageView imageView8;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dialog_position, container, false);

        imageView = (ImageView) view.findViewById(R.id.image_exit);
        imageView = (ImageView) view.findViewById(R.id.image_exit);
        imageView1 = (ImageView) view.findViewById(R.id.imageView1);
        imageView2 = (ImageView) view.findViewById(R.id.imageView2);
        imageView3 = (ImageView) view.findViewById(R.id.image_genre);
        imageView4 = (ImageView) view.findViewById(R.id.imageView4);
        imageView5 = (ImageView) view.findViewById(R.id.image_position);
        imageView6 = (ImageView) view.findViewById(R.id.imageView6);
        imageView7 = (ImageView) view.findViewById(R.id.imageView7);
        imageView8 = (ImageView) view.findViewById(R.id.imageView8);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedListener != null) {
                    mSelectedListener.onPositionSelected(10);
                }
                dismiss();
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedListener != null) {
                    mSelectedListener.onPositionSelected(11);
                }
                dismiss();
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedListener != null) {
                    mSelectedListener.onPositionSelected(12);
                }
                dismiss();
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedListener != null) {
                    mSelectedListener.onPositionSelected(13);
                }
                dismiss();
            }
        });

        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedListener != null) {
                    mSelectedListener.onPositionSelected(14);
                }
                dismiss();
            }
        });

        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedListener != null) {
                    mSelectedListener.onPositionSelected(15);
                }
                dismiss();
            }
        });

        imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedListener != null) {
                    mSelectedListener.onPositionSelected(16);
                }
                dismiss();
            }
        });

        imageView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedListener != null) {
                    mSelectedListener.onPositionSelected(17);
                }
                dismiss();
            }
        });

        return view;
    }

}
