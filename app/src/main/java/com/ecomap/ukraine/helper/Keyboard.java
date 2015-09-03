package com.ecomap.ukraine.helper;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Performs keyboard hiding, when view lose the focus.
 */
public final class Keyboard {

    private Keyboard(){}

    /**
     * Sets onFocusChangeListener to view.
     *
     * @param view view which interact with keyboard.
     */
    public static void setOnFocusChangeListener(View view) {
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            /**
             * Performs when view lose the focus.
             *
             * @param view target view.
             * @param hasFocus indicates whether a view has focus.
             */
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                }
            }
        });
    }

    /**
     * Hides keyboard from screen.
     *
     * @param view view which interact with keyboard.
     */
    private static void hideKeyboard(View view) {
        InputMethodManager inputMethodManager
                = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
