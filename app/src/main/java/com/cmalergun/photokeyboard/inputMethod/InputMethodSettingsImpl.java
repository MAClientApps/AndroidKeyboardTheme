/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * This is a part of the inputmethod-common static Java library.
 * The original source code can be found at frameworks/opt/inputmethodcommon of Android Open Source
 * Project.
 */

package com.cmalergun.photokeyboard.inputMethod;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;

import java.util.List;

/* package private */
class InputMethodSettingsImpl implements InputMethodSettingsInterface {

    private Context pContext;
    private Preference pSubtypeEnablerPreference;
    private int mInputMethodSettingsCategoryTitleRes;
    private CharSequence mInputMethodSettingsCategoryTitle;
    private CharSequence pSubtypeEnablerTitle;
    private int pSubtypeEnablerTitleRes;
    private int pSubtypeEnablerIconRes;
    private Drawable pSubtypeEnablerIcon;
    private InputMethodInfo pImi;
    private InputMethodManager pImm;


    /**
     * Initialize internal states of this object.
     *
     * @param context    the context for this application.
     * @param prefScreen a PreferenceScreen of PreferenceActivity or PreferenceFragment.
     * @return true if this application is an IME and has two or more subtypes, false otherwise.
     */
    public boolean init(final Context context, final PreferenceScreen prefScreen) {
        pContext = context;
        pImm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        pImi = getMyImi(context, pImm);
        if (pImi == null || pImi.getSubtypeCount() <= 1) {
            return false;
        }
        pSubtypeEnablerPreference = new Preference(context);
        pSubtypeEnablerPreference
                .setOnPreferenceClickListener(new OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        final CharSequence title = getSubtypeEnablerTitle(context);
                        final Intent intent =
                                new Intent(Settings.ACTION_INPUT_METHOD_SUBTYPE_SETTINGS);
                        intent.putExtra(Settings.EXTRA_INPUT_METHOD_ID, pImi.getId());
                        if (!TextUtils.isEmpty(title)) {
                            intent.putExtra(Intent.EXTRA_TITLE, title);
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        return true;
                    }
                });
        prefScreen.addPreference(pSubtypeEnablerPreference);
        updateSubtypeEnabler();
        return true;
    }

    private static InputMethodInfo getMyImi(Context context, InputMethodManager imm) {
        final List<InputMethodInfo> imis = imm.getInputMethodList();
        for (int i = 0; i < imis.size(); ++i) {
            final InputMethodInfo imi = imis.get(i);
            if (imis.get(i).getPackageName().equals(context.getPackageName())) {
                return imi;
            }
        }
        return null;
    }

    private static String getEnabledSubtypesLabel(
            Context context, InputMethodManager imm, InputMethodInfo imi) {
        if (context == null || imm == null || imi == null) return null;
        final List<InputMethodSubtype> subtypes = imm.getEnabledInputMethodSubtypeList(imi, true);
        final StringBuilder sb = new StringBuilder();
        final int N = subtypes.size();
        for (int i = 0; i < N; ++i) {
            final InputMethodSubtype subtype = subtypes.get(i);
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(subtype.getDisplayName(context, imi.getPackageName(),
                    imi.getServiceInfo().applicationInfo));
        }
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInputMethodSettingsCategoryTitle(int resId) {
        mInputMethodSettingsCategoryTitleRes = resId;
        updateSubtypeEnabler();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInputMethodSettingsCategoryTitle(CharSequence title) {
        mInputMethodSettingsCategoryTitleRes = 0;
        mInputMethodSettingsCategoryTitle = title;
        updateSubtypeEnabler();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSubtypeEnablerTitle(int resId) {
        pSubtypeEnablerTitleRes = resId;
        updateSubtypeEnabler();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSubtypeEnablerTitle(CharSequence title) {
        pSubtypeEnablerTitleRes = 0;
        pSubtypeEnablerTitle = title;
        updateSubtypeEnabler();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSubtypeEnablerIcon(int resId) {
        pSubtypeEnablerIconRes = resId;
        updateSubtypeEnabler();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSubtypeEnablerIcon(Drawable drawable) {
        pSubtypeEnablerIconRes = 0;
        pSubtypeEnablerIcon = drawable;
        updateSubtypeEnabler();
    }

    private CharSequence getSubtypeEnablerTitle(Context context) {
        if (pSubtypeEnablerTitleRes != 0) {
            return context.getString(pSubtypeEnablerTitleRes);
        } else {
            return pSubtypeEnablerTitle;
        }
    }

    public void updateSubtypeEnabler() {
        if (pSubtypeEnablerPreference != null) {
            if (pSubtypeEnablerTitleRes != 0) {
                pSubtypeEnablerPreference.setTitle(pSubtypeEnablerTitleRes);
            } else if (!TextUtils.isEmpty(pSubtypeEnablerTitle)) {
                pSubtypeEnablerPreference.setTitle(pSubtypeEnablerTitle);
            }
            final String summary = getEnabledSubtypesLabel(pContext, pImm, pImi);
            if (!TextUtils.isEmpty(summary)) {
                pSubtypeEnablerPreference.setSummary(summary);
            }
            if (pSubtypeEnablerIconRes != 0) {
                pSubtypeEnablerPreference.setIcon(pSubtypeEnablerIconRes);
            } else if (pSubtypeEnablerIcon != null) {
                pSubtypeEnablerPreference.setIcon(pSubtypeEnablerIcon);
            }
        }
    }
}
