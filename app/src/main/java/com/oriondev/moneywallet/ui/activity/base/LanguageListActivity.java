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

package com.oriondev.moneywallet.ui.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;

import com.oriondev.moneywallet.R;
import com.oriondev.moneywallet.model.CurrencyUnit;
import com.oriondev.moneywallet.storage.database.Contract;
import com.oriondev.moneywallet.storage.database.DataContentProvider;
import com.oriondev.moneywallet.ui.activity.NewEditCurrencyActivity;
import com.oriondev.moneywallet.ui.activity.NewEditLanguageActivity;
import com.oriondev.moneywallet.ui.adapter.recycler.AbstractCursorAdapter;
import com.oriondev.moneywallet.ui.adapter.recycler.CurrencyCursorAdapter;
import com.oriondev.moneywallet.ui.adapter.recycler.LanguageCursorAdapter;
import com.oriondev.moneywallet.ui.adapter.recycler.LanguageSelectorCursorAdapter;
import com.oriondev.moneywallet.ui.view.AdvancedRecyclerView;
import com.oriondev.moneywallet.utils.CurrencyManager;

/**
 * Created by andrea on 03/02/18.
 */
public class LanguageListActivity extends SinglePanelSimpleListActivity implements LanguageCursorAdapter.LanguageActionListener {

    public static final String ACTIVITY_MODE = "LanguageListActivity::ActivityMode";
    public static final String RESULT_CURRENCY = "LanguageListActivity::Result::SelectedLanguage";

    public static final int LANGUAGE_MANAGER = 0;
    public static final int LANGUAGE_PICKER = 1;

    private int mActivityMode;

    @Override
    protected void onPrepareRecyclerView(AdvancedRecyclerView recyclerView) {
        Intent intent = getIntent();
        if (intent != null) {
            mActivityMode = intent.getIntExtra(ACTIVITY_MODE, LANGUAGE_MANAGER);
        } else {
            mActivityMode = LANGUAGE_MANAGER;
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setEmptyText(R.string.message_no_currency_found);      //ALLAGH
    }

    @Override
    protected AbstractCursorAdapter onCreateAdapter() {
        return new LanguageCursorAdapter(this);         //What?
    }

    @Override
    @StringRes
    protected int getActivityTitleRes() {
        return R.string.title_activity_currency_list;
    }       //ALLAGH

    @Override
    protected boolean isFloatingActionButtonEnabled() {
        return mActivityMode == LANGUAGE_MANAGER;
    }

    @Override
    protected void onFloatingActionButtonClick() {
        Intent intent = new Intent(this, NewEditCurrencyActivity.class);
        intent.putExtra(NewEditCurrencyActivity.MODE, NewEditCurrencyActivity.Mode.NEW_ITEM);       //ALLAGH H DIAGRAFH
        startActivity(intent);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = DataContentProvider.CONTENT_LANGUAGES;
        String[] projection = new String[] {
                Contract.Language.NAME,
        };
        String sortBy = Contract.Language.NAME;
        return new CursorLoader(this, uri, projection, null, null, sortBy);
    }

    @Override
    public void onPlaceClick(long id) {
        if (mActivityMode == LANGUAGE_MANAGER) {
            Intent intent = new Intent(this, NewEditLanguageActivity.class);
            intent.putExtra(NewEditLanguageActivity.MODE, NewEditLanguageActivity.Mode.EDIT_ITEM);
            startActivity(intent);
        } else if (mActivityMode == LANGUAGE_PICKER) {
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

}