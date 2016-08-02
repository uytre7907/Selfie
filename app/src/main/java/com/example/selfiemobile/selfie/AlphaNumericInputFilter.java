package com.example.selfiemobile.selfie;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by uytre_000 on 8/2/2016.
 */
public class AlphaNumericInputFilter implements InputFilter {
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {

        // Only keep characters that are alphanumeric
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < end; i++) {
            char c = source.charAt(i);
            if (Character.isLetterOrDigit(c)||c=='_'||c=='.') {
                builder.append(c);
            }
        }

        // If all characters are valid, return null, otherwise only return the filtered characters
        boolean allCharactersValid = (builder.length() == end - start);
        return allCharactersValid ? null : builder.toString();
    }
}
