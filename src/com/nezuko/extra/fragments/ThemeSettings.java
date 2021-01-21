package com.nezuko.extra.fragments;

import com.android.internal.logging.nano.MetricsProto;
import static android.os.UserHandle.USER_SYSTEM;
import android.graphics.Color;
import android.os.Bundle;

import android.content.om.IOverlayManager;
import android.content.om.OverlayInfo;
import android.content.pm.PackageManager;
import android.os.ServiceManager;
import android.app.UiModeManager;
import android.os.RemoteException;

import android.content.pm.PackageManager.NameNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.UserHandle;
import android.os.SystemProperties;
import android.content.ContentResolver;
import android.content.res.Resources;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.PreferenceManager;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;
import com.nezuko.extra.preferences.CustomSeekBarPreference;
import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import java.util.Locale;
import android.text.TextUtils;
import android.view.View;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

import com.android.internal.util.nezuko.ThemesUtils;
import com.android.internal.util.nezuko.NezukoThemeUtils;
import net.margaritov.preference.colorpicker.ColorPickerPreference;

public class ThemeSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {
    private static final String PREF_THEME_SWITCH = "theme_switch";

    private static final String PREF_ROUNDED_CORNER = "rounded_ui";
    private static final String PREF_SB_HEIGHT = "statusbar_height";
    private static final String PREF_NB_COLOR = "navbar_color";

    private UiModeManager mUiModeManager;
    private ListPreference mThemeSwitch;

    private ListPreference mRoundedUi;
    private ListPreference mSbHeight;
    private ListPreference mnbSwitch;
    private IOverlayManager mOverlayService;
    private IOverlayManager mOverlayManager;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.systemthemes);

        PreferenceScreen prefScreen = getPreferenceScreen();
        ContentResolver resolver = getActivity().getContentResolver();

        mUiModeManager = getContext().getSystemService(UiModeManager.class);

        mOverlayService = IOverlayManager.Stub
                .asInterface(ServiceManager.getService(Context.OVERLAY_SERVICE));

        setupThemeSwitchPref();
        setupNavbarSwitchPref();

        mRoundedUi = (ListPreference) findPreference(PREF_ROUNDED_CORNER);
        int roundedValue = getOverlayPosition(ThemesUtils.UI_RADIUS);
        if (roundedValue != -1) {
            mRoundedUi.setValue(String.valueOf(roundedValue + 2));
        } else {
            mRoundedUi.setValue("1");
        }
        mRoundedUi.setSummary(mRoundedUi.getEntry());
        mRoundedUi.setOnPreferenceChangeListener(this);

        mSbHeight = (ListPreference) findPreference(PREF_SB_HEIGHT);
        int sbHeightValue = getOverlayPosition(ThemesUtils.STATUSBAR_HEIGHT);
        if (sbHeightValue != -1) {
            mSbHeight.setValue(String.valueOf(sbHeightValue + 2));
        } else {
            mSbHeight.setValue("1");
        }
        mSbHeight.setSummary(mSbHeight.getEntry());
        mSbHeight.setOnPreferenceChangeListener(this);

        }

    public void handleOverlays(String packagename, Boolean state, IOverlayManager mOverlayManager) {
        try {
            mOverlayService.setEnabled(packagename, state, USER_SYSTEM);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
         if (preference == mThemeSwitch) {
            String theme_switch = (String) newValue;
            final Context context = getContext();
            switch (theme_switch) {
                case "1":
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_NO, ThemesUtils.SOLARIZED_DARK);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_NO, ThemesUtils.BAKED_GREEN);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_NO, ThemesUtils.CHOCO_X);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_NO, ThemesUtils.PITCH_BLACK);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_NO, ThemesUtils.DARK_GREY);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_NO, ThemesUtils.MATERIAL_OCEAN);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_NO, ThemesUtils.CLEAR_SPRING);
                    break;
                case "2":
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.SOLARIZED_DARK);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.BAKED_GREEN);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.CHOCO_X);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.PITCH_BLACK);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.DARK_GREY);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.MATERIAL_OCEAN);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.CLEAR_SPRING);
                    break;
                case "3":
                    handleBackgrounds(true, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.SOLARIZED_DARK);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.BAKED_GREEN);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.CHOCO_X);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.PITCH_BLACK);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.DARK_GREY);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.MATERIAL_OCEAN);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.CLEAR_SPRING);
                    break;
                case "4":
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.SOLARIZED_DARK);
                    handleBackgrounds(true, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.BAKED_GREEN);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.CHOCO_X);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.PITCH_BLACK);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.DARK_GREY);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.MATERIAL_OCEAN);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.CLEAR_SPRING);
                    break;
                case "5":
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.SOLARIZED_DARK);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.BAKED_GREEN);
                    handleBackgrounds(true, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.CHOCO_X);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.PITCH_BLACK);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.DARK_GREY);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.MATERIAL_OCEAN);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.CLEAR_SPRING);
                    break;
                case "6":
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.SOLARIZED_DARK);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.BAKED_GREEN);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.CHOCO_X);
                    handleBackgrounds(true, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.PITCH_BLACK);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.DARK_GREY);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.MATERIAL_OCEAN);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.CLEAR_SPRING);
                    break;
                case "7":
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.SOLARIZED_DARK);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.BAKED_GREEN);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.CHOCO_X);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.PITCH_BLACK);
                    handleBackgrounds(true, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.DARK_GREY);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.MATERIAL_OCEAN);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.CLEAR_SPRING);
                    break;
                case "8":
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.SOLARIZED_DARK);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.BAKED_GREEN);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.CHOCO_X);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.PITCH_BLACK);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.DARK_GREY);
                    handleBackgrounds(true, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.MATERIAL_OCEAN);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.CLEAR_SPRING);
                    break;
                case "9":
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.SOLARIZED_DARK);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.BAKED_GREEN);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.CHOCO_X);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.PITCH_BLACK);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.DARK_GREY);
                    handleBackgrounds(false, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.MATERIAL_OCEAN);
                    handleBackgrounds(true, context, UiModeManager.MODE_NIGHT_YES, ThemesUtils.CLEAR_SPRING);
                    break;
            }
            try {
                 mOverlayService.reloadAndroidAssets(UserHandle.USER_CURRENT);
                 mOverlayService.reloadAssets("com.android.settings", UserHandle.USER_CURRENT);
                 mOverlayService.reloadAssets("com.android.systemui", UserHandle.USER_CURRENT);
             } catch (RemoteException ignored) {
             }
            return true;
            } 

            else if (preference == mnbSwitch){
            String nbSwitch = (String) newValue;
            final Context context = getContext();
            switch (nbSwitch) {
                case "1":
                handleOverlays(ThemesUtils.NAVBAR_COLOR_ORCD, false, mOverlayManager);
                handleOverlays(ThemesUtils.NAVBAR_COLOR_OPRD, false, mOverlayManager);
                handleOverlays(ThemesUtils.NAVBAR_COLOR_PURP, false, mOverlayManager);
                break;
                case "2":
                handleOverlays(ThemesUtils.NAVBAR_COLOR_ORCD, true, mOverlayManager);
                handleOverlays(ThemesUtils.NAVBAR_COLOR_OPRD, false, mOverlayManager);
                handleOverlays(ThemesUtils.NAVBAR_COLOR_PURP, false, mOverlayManager);
                break;
                case "3":
                handleOverlays(ThemesUtils.NAVBAR_COLOR_ORCD, false, mOverlayManager);
                handleOverlays(ThemesUtils.NAVBAR_COLOR_OPRD, true, mOverlayManager);
                handleOverlays(ThemesUtils.NAVBAR_COLOR_PURP, false, mOverlayManager);
                break;
                case "4":
                handleOverlays(ThemesUtils.NAVBAR_COLOR_ORCD, false, mOverlayManager);
                handleOverlays(ThemesUtils.NAVBAR_COLOR_OPRD, false, mOverlayManager);
                handleOverlays(ThemesUtils.NAVBAR_COLOR_PURP, true, mOverlayManager);
                break;
            }
            try {
                 mOverlayService.reloadAndroidAssets(UserHandle.USER_CURRENT);
                 mOverlayService.reloadAssets("com.android.settings", UserHandle.USER_CURRENT);
                 mOverlayService.reloadAssets("com.android.systemui", UserHandle.USER_CURRENT);
             } catch (RemoteException ignored) {
             }
            return true;
            }

            else if (preference == mRoundedUi) {
            String rounded = (String) newValue;
            int roundedValue = Integer.parseInt(rounded);
            mRoundedUi.setValue(String.valueOf(roundedValue));
            String overlayName = getOverlayName(ThemesUtils.UI_RADIUS);
                if (overlayName != null) {
                    handleOverlays(overlayName, false, mOverlayManager);
                }
                if (roundedValue > 1) {
                    handleOverlays(ThemesUtils.UI_RADIUS[roundedValue -2],
                            true, mOverlayManager);
            }
            mRoundedUi.setSummary(mRoundedUi.getEntry());
            return true;

            } else if (preference == mSbHeight) {
            String sbheight = (String) newValue;
            int sbheightValue = Integer.parseInt(sbheight);
            mSbHeight.setValue(String.valueOf(sbheightValue));
            String overlayName = getOverlayName(ThemesUtils.STATUSBAR_HEIGHT);
                if (overlayName != null) {
                    handleOverlays(overlayName, false, mOverlayManager);
                }
                if (sbheightValue > 1) {
                    handleOverlays(ThemesUtils.STATUSBAR_HEIGHT[sbheightValue -2],
                            true, mOverlayManager);
            }
            mSbHeight.setSummary(mSbHeight.getEntry());
            return true;
            } 
            return false;
       }


    private int getOverlayPosition(String[] overlays) {
        int position = -1;
        for (int i = 0; i < overlays.length; i++) {
            String overlay = overlays[i];
            if (NezukoThemeUtils.isThemeEnabled(overlay)) {
                position = i;
            }
        }
        return position;
    }

    private String getOverlayName(String[] overlays) {
        String overlayName = null;
        for (int i = 0; i < overlays.length; i++) {
            String overlay = overlays[i];
            if (NezukoThemeUtils.isThemeEnabled(overlay)) {
                overlayName = overlay;
            }
        }
        return overlayName;
    }

    private void setupThemeSwitchPref() {
        mThemeSwitch = (ListPreference) findPreference(PREF_THEME_SWITCH);
        mThemeSwitch.setOnPreferenceChangeListener(this);
         if (NezukoThemeUtils.isThemeEnabled("com.android.theme.clearspring.system")) {
            mThemeSwitch.setValue("9");
        } else if (NezukoThemeUtils.isThemeEnabled("com.android.theme.darkgrey.system")) {
            mThemeSwitch.setValue("7");
        } else if (NezukoThemeUtils.isThemeEnabled("com.android.theme.pitchblack.system")) {
            mThemeSwitch.setValue("6");
        } else if (NezukoThemeUtils.isThemeEnabled("com.android.theme.materialocean.system")) {
            mThemeSwitch.setValue("8");
        } else if (NezukoThemeUtils.isThemeEnabled("com.android.theme.chocox.system")) {
            mThemeSwitch.setValue("5");
        } else if (NezukoThemeUtils.isThemeEnabled("com.android.theme.bakedgreen.system")) {
            mThemeSwitch.setValue("4");
        } else if (NezukoThemeUtils.isThemeEnabled("com.android.theme.solarizeddark.system")) {
            mThemeSwitch.setValue("3");
        } else if (mUiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES) {
            mThemeSwitch.setValue("2");
        } else {
            mThemeSwitch.setValue("1");
        }
    }

    private void setupNavbarSwitchPref() {
        mnbSwitch = (ListPreference) findPreference(PREF_NB_COLOR);
        mnbSwitch.setOnPreferenceChangeListener(this);
        if (NezukoThemeUtils.isNavbarColor("com.gnonymous.gvisualmod.pgm_purp")){
            mnbSwitch.setValue("4");
        } else if (NezukoThemeUtils.isNavbarColor("com.gnonymous.gvisualmod.pgm_oprd")){
            mnbSwitch.setValue("3");
        } else if (NezukoThemeUtils.isNavbarColor("com.gnonymous.gvisualmod.pgm_orcd")){
            mnbSwitch.setValue("2");
        }
        else{
            mnbSwitch.setValue("1");
        }
    }

    private void handleBackgrounds(Boolean state, Context context, int mode, String[] overlays) {
        if (context != null) {
            Objects.requireNonNull(context.getSystemService(UiModeManager.class))
                    .setNightMode(mode);
        }
        for (int i = 0; i < overlays.length; i++) {
            String background = overlays[i];
            try {
                mOverlayService.setEnabled(background, state, USER_SYSTEM);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.NEZUKO;
    }
}