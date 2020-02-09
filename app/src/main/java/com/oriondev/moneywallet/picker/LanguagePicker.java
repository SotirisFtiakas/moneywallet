/*
 * Copyright (c) 2018.
 *
 * This file is part of MoneyWallet.
 *
 * MoneyWallet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MoneyWallet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MoneyWallet.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.oriondev.moneywallet.picker;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.oriondev.moneywallet.model.Language;
import com.oriondev.moneywallet.ui.fragment.dialog.LanguagePickerDialog;

/**
 * Created by andrea on 10/03/18.
 */

public class LanguagePicker extends Fragment implements LanguagePickerDialog.Callback {

    private static final String SS_CURRENT_LANGUAGE = "LanguagePicker::SavedState::CurrentLanguage";

    private static final String ARG_DEFAULT_LANGUAGE = "LanguagePicker::Arguments::DefaultLanguage";

    private Controller mController;

    private Language mCurrentLanguage;

    private LanguagePickerDialog mLanguagePickerDialog;                //NA TO DW EDW

    public static LanguagePicker createPicker(FragmentManager fragmentManager, String tag, Language defaultLanguage) {
        LanguagePicker languagePicker = (LanguagePicker) fragmentManager.findFragmentByTag(tag);
        if (languagePicker == null) {
            languagePicker = new LanguagePicker();
            Bundle arguments = new Bundle();
            arguments.putParcelable(ARG_DEFAULT_LANGUAGE, defaultLanguage);
            languagePicker.setArguments(arguments);
            fragmentManager.beginTransaction().add(languagePicker, tag).commit();
        }
        return languagePicker;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Controller) {
            mController = (Controller) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mCurrentLanguage = savedInstanceState.getParcelable(SS_CURRENT_LANGUAGE);
        } else {
            Bundle arguments = getArguments();
            if (arguments != null) {
                mCurrentLanguage = arguments.getParcelable(ARG_DEFAULT_LANGUAGE);
            }
        }
        mLanguagePickerDialog = (LanguagePickerDialog) getChildFragmentManager().findFragmentByTag(getDialogTag());
        if (mLanguagePickerDialog == null) {
            mLanguagePickerDialog = LanguagePickerDialog.newInstance();
        }
        mLanguagePickerDialog.setCallback(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fireCallbackSafely();
    }

    private void fireCallbackSafely() {
        if (mController != null) {
            mController.onPlaceChanged(getTag(), mCurrentLanguage);
        }
    }

    private String getDialogTag() {
        return getTag() + "::DialogFragment";
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SS_CURRENT_LANGUAGE, mCurrentLanguage);
    }

    public boolean isSelected() {
        return mCurrentLanguage != null;
    }

    public void setCurrentPlace(Language language) {
        mCurrentLanguage = language;
        fireCallbackSafely();
    }

    public Language getCurrentPlace() {
        return mCurrentLanguage;
    }

    public void showPicker() {
        mLanguagePickerDialog.showPicker(getChildFragmentManager(), getDialogTag(), mCurrentLanguage);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mController = null;
    }

    @Override
    public void onLanguageSelected(Language language) {
        mCurrentLanguage = language;
        fireCallbackSafely();
    }

    public interface Controller {

        void onPlaceChanged(String tag, Language language);
    }
}