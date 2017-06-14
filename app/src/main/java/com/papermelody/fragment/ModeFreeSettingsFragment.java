package com.papermelody.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.papermelody.R;
import com.papermelody.activity.CalibrationActivity;
import com.papermelody.activity.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HgS_1217_ on 2017/4/10.
 */

public class ModeFreeSettingsFragment extends BaseFragment {
    /**
     * 用例：演奏乐器（流程三）
     * 自由模式演奏前设置页面
     */

    @BindView(R.id.spinner_free_instrument)
    Spinner spinnerInstrument;
    @BindView(R.id.spinner_free_category)
    Spinner spinnerCategory;
    @BindView(R.id.btn_free_cfm)
    Button btnFreeConfirm;

    private int instrument, category;
    private ArrayAdapter<CharSequence> arrayAdapterInstrument, arrayAdapterCategory;

    public static ModeFreeSettingsFragment newInstance() {
        ModeFreeSettingsFragment fragment = new ModeFreeSettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mode_free_settings, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        arrayAdapterInstrument = ArrayAdapter.createFromResource(getContext(), R.array.spinner_instrument, android.R.layout.simple_spinner_item);
        arrayAdapterInstrument.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInstrument.setAdapter(arrayAdapterInstrument);
        spinnerInstrument.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    arrayAdapterCategory = ArrayAdapter.createFromResource(getContext(), R.array.spinner_category_piano, android.R.layout.simple_spinner_item);
                } else {
                    arrayAdapterCategory = ArrayAdapter.createFromResource(getContext(), R.array.spinner_category_flute, android.R.layout.simple_spinner_item);
                }
                instrument = position;
                arrayAdapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCategory.setAdapter(arrayAdapterCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    category = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnFreeConfirm.setOnClickListener((View v) -> {
            Intent intent = new Intent(getContext(), CalibrationActivity.class);
            intent.putExtra("mode", 0);
            intent.putExtra("instrument", instrument);
            intent.putExtra("category", category);
            startActivity(intent);
            MainActivity activity = (MainActivity) getActivity();
            activity.updateFragment(MainActivity.MAIN_HOME);
        });
    }
}
