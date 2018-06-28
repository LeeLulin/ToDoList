package com.example.lulin.todolist.fragment;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.util.Log;

import com.example.lulin.todolist.R;
import com.example.lulin.todolist.utils.SPUtils;

public class SettingsFragment extends PreferenceFragment {

    private RingtonePreference mRingtone;
    private PreferenceScreen preferenceScreen;
    private SwitchPreference mVibrate;
    private static final String KEY_RINGTONE = "ring_tone";
    private static final String KEY_VIBRATE = "vibrator";
    private static final String TAG = "setting";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.addPreferencesFromResource(R.xml.preferences);
        intView();
        setChangeListener();

    }

    public void intView(){
        preferenceScreen = getPreferenceScreen();
        mRingtone = (RingtonePreference) preferenceScreen.findPreference(KEY_RINGTONE);
//        mVibrate = (SwitchPreference) preferenceScreen.findPreference(KEY_VIBRATE);
        Uri uri = Uri.parse(SPUtils.get(getActivity(), KEY_RINGTONE, "").toString());
        Log.i(TAG, "铃声" + getRingtonName(uri));
        if (getRingtonName(uri).equals("未知铃声")){
            mRingtone.setSummary("默认铃声");
        } else {
            mRingtone.setSummary(getRingtonName(uri));
        }

    }

    public void setChangeListener(){
        mRingtone.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object value) {
                if (preference == mRingtone){
                    SPUtils.put(getActivity(), KEY_RINGTONE, value.toString());
                    Log.i(TAG, value.toString());
                    mRingtone.setSummary(getRingtonName(Uri.parse(value.toString())));
                }
                return false;
            }
        });

//        mVibrate.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object object) {
//                if (preference == mVibrate){
//                    if (mVibrate.isChecked() != (Boolean)object) {
//                        boolean value = (Boolean)(object);
//                        mVibrate.setChecked(value);
//                        SPUtils.put(getActivity(), KEY_VIBRATE, value);
//                    }
//                }
//                return true;
//            }
//        });

    }

    /**
     * 获取铃声名
     * @param uri
     * @return
     */
    public String getRingtonName(Uri uri) {
        Ringtone r = RingtoneManager.getRingtone(getActivity(), uri);
        return r.getTitle(getActivity());
    }

    public boolean getIsVibrate(Context context){

        Boolean isVibrate = (Boolean) SPUtils.get(context, KEY_VIBRATE, true);

        return isVibrate;

    }

}

