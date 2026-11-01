package androidx.core.app;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public final class RemoteInput {
    public static final int EDIT_CHOICES_BEFORE_SENDING_AUTO = 0;
    public static final int EDIT_CHOICES_BEFORE_SENDING_DISABLED = 1;
    public static final int EDIT_CHOICES_BEFORE_SENDING_ENABLED = 2;
    private static final String EXTRA_DATA_TYPE_RESULTS_DATA = "android.remoteinput.dataTypeResultsData";
    public static final String EXTRA_RESULTS_DATA = "android.remoteinput.resultsData";
    private static final String EXTRA_RESULTS_SOURCE = "android.remoteinput.resultsSource";
    public static final String RESULTS_CLIP_LABEL = "android.remoteinput.results";
    public static final int SOURCE_CHOICE = 1;
    public static final int SOURCE_FREE_FORM_INPUT = 0;
    private final boolean mAllowFreeFormTextInput;
    private final Set<String> mAllowedDataTypes;
    private final CharSequence[] mChoices;
    private final int mEditChoicesBeforeSending;
    private final Bundle mExtras;
    private final CharSequence mLabel;
    private final String mResultKey;

    static class Api16Impl {
        private Api16Impl() {
        }

        static ClipData getClipData(Intent r1) {
            return r1.getClipData();
        }

        static void setClipData(Intent r0, ClipData r1) {
            r0.setClipData(r1);
        }
    }

    static class Api20Impl {
        private Api20Impl() {
        }

        static void addResultsToIntent(Object r1, Intent r2, Bundle r3) {
            android.app.RemoteInput.addResultsToIntent((android.app.RemoteInput[]) r1, r2, r3);
        }

        public static android.app.RemoteInput fromCompat(RemoteInput r5) {
            android.app.RemoteInput.Builder r0 = new android.app.RemoteInput.Builder(r5.getResultKey()).setLabel(r5.getLabel()).setChoices(r5.getChoices()).setAllowFreeFormInput(r5.getAllowFreeFormInput()).addExtras(r5.getExtras());
            if (Build.VERSION.SDK_INT < 26) goto L11;
            Set<String> r1 = r5.getAllowedDataTypes();
            if (r1 == null) goto L11;
            Iterator<String> r2 = r1.iterator();
        L8:
            if (r2.hasNext() == false) goto L11;
            Api26Impl.setAllowDataType(r0, r2.next(), true);
        L11:
            if (Build.VERSION.SDK_INT < 29) goto L14;
            Api29Impl.setEditChoicesBeforeSending(r0, r5.getEditChoicesBeforeSending());
        L14:
            return r0.build();
        }

        static RemoteInput fromPlatform(Object r6) {
            android.app.RemoteInput r0 = (android.app.RemoteInput) r6;
            Builder r1 = new Builder(r0.getResultKey()).setLabel(r0.getLabel()).setChoices(r0.getChoices()).setAllowFreeFormInput(r0.getAllowFreeFormInput()).addExtras(r0.getExtras());
            if (Build.VERSION.SDK_INT < 26) goto L11;
            Set<String> r2 = Api26Impl.getAllowedDataTypes(r0);
            if (r2 == null) goto L11;
            Iterator<String> r3 = r2.iterator();
        L8:
            if (r3.hasNext() == false) goto L11;
            r1.setAllowDataType(r3.next(), true);
        L11:
            if (Build.VERSION.SDK_INT < 29) goto L14;
            r1.setEditChoicesBeforeSending(Api29Impl.getEditChoicesBeforeSending(r0));
        L14:
            return r1.build();
        }

        static Bundle getResultsFromIntent(Intent r1) {
            return android.app.RemoteInput.getResultsFromIntent(r1);
        }
    }

    static class Api26Impl {
        private Api26Impl() {
        }

        static void addDataResultToIntent(RemoteInput r1, Intent r2, Map<String, Uri> r3) {
            android.app.RemoteInput.addDataResultToIntent(RemoteInput.fromCompat(r1), r2, r3);
        }

        static Set<String> getAllowedDataTypes(Object r1) {
            return ((android.app.RemoteInput) r1).getAllowedDataTypes();
        }

        static Map<String, Uri> getDataResultsFromIntent(Intent r1, String r2) {
            return android.app.RemoteInput.getDataResultsFromIntent(r1, r2);
        }

        static android.app.RemoteInput.Builder setAllowDataType(android.app.RemoteInput.Builder r1, String r2, boolean r3) {
            return r1.setAllowDataType(r2, r3);
        }
    }

    static class Api28Impl {
        private Api28Impl() {
        }

        static int getResultsSource(Intent r1) {
            return android.app.RemoteInput.getResultsSource(r1);
        }

        static void setResultsSource(Intent r0, int r1) {
            android.app.RemoteInput.setResultsSource(r0, r1);
        }
    }

    static class Api29Impl {
        private Api29Impl() {
        }

        static int getEditChoicesBeforeSending(Object r1) {
            return ((android.app.RemoteInput) r1).getEditChoicesBeforeSending();
        }

        static android.app.RemoteInput.Builder setEditChoicesBeforeSending(android.app.RemoteInput.Builder r1, int r2) {
            return r1.setEditChoicesBeforeSending(r2);
        }
    }

    public static final class Builder {
        private boolean mAllowFreeFormTextInput;
        private final Set<String> mAllowedDataTypes;
        private CharSequence[] mChoices;
        private int mEditChoicesBeforeSending;
        private final Bundle mExtras;
        private CharSequence mLabel;
        private final String mResultKey;

        public Builder(String r3) {
            this.mAllowedDataTypes = new HashSet();
            this.mExtras = new Bundle();
            this.mAllowFreeFormTextInput = true;
            this.mEditChoicesBeforeSending = 0;
            if (r3 == null) goto L7;
            this.mResultKey = r3;
            return;
        L7:
            throw new IllegalArgumentException("Result key can't be null");
        }

        public Builder addExtras(Bundle r2) {
            if (r2 == null) goto L4;
            this.mExtras.putAll(r2);
        L4:
            return this;
        }

        public RemoteInput build() {
            return new RemoteInput(this.mResultKey, this.mLabel, this.mChoices, this.mAllowFreeFormTextInput, this.mEditChoicesBeforeSending, this.mExtras, this.mAllowedDataTypes);
        }

        public Bundle getExtras() {
            return this.mExtras;
        }

        public Builder setAllowDataType(String r2, boolean r3) {
            if (r3 == false) goto L4;
            this.mAllowedDataTypes.add(r2);
        L5:
            return this;
        L4:
            this.mAllowedDataTypes.remove(r2);
            goto L5
        }

        public Builder setAllowFreeFormInput(boolean r1) {
            this.mAllowFreeFormTextInput = r1;
            return this;
        }

        public Builder setChoices(CharSequence[] r1) {
            this.mChoices = r1;
            return this;
        }

        public Builder setEditChoicesBeforeSending(int r1) {
            this.mEditChoicesBeforeSending = r1;
            return this;
        }

        public Builder setLabel(CharSequence r1) {
            this.mLabel = r1;
            return this;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface EditChoicesBeforeSending {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Source {
    }

    RemoteInput(String r3, CharSequence r4, CharSequence[] r5, boolean r6, int r7, Bundle r8, Set<String> r9) {
        this.mResultKey = r3;
        this.mLabel = r4;
        this.mChoices = r5;
        this.mAllowFreeFormTextInput = r6;
        this.mEditChoicesBeforeSending = r7;
        this.mExtras = r8;
        this.mAllowedDataTypes = r9;
        if (getEditChoicesBeforeSending() == 2) goto L5;
        return;
    L5:
        if (getAllowFreeFormInput() == false) goto L8;
        return;
    L8:
        throw new IllegalArgumentException("setEditChoicesBeforeSending requires setAllowFreeFormInput");
    }

    public static void addDataResultToIntent(RemoteInput r8, Intent r9, Map<String, Uri> r10) {
        if (Build.VERSION.SDK_INT < 26) goto L5;
        Api26Impl.addDataResultToIntent(r8, r9, r10);
        return;
    L5:
        Intent r0 = getClipDataIntentFromIntent(r9);
        if (r0 != null) goto L8;
        r0 = new Intent();
    L8:
        Iterator<Map.Entry<String, Uri>> r1 = r10.entrySet().iterator();
    L10:
        if (r1.hasNext() == false) goto L18;
        Map.Entry<String, Uri> r2 = r1.next();
        String r3 = r2.getKey();
        Uri r4 = r2.getValue();
        if (r3 == null) goto L10;
        Bundle r5 = r0.getBundleExtra(getExtraResultsKeyForData(r3));
        if (r5 != null) goto L17;
        r5 = new Bundle();
    L17:
        r5.putString(r8.getResultKey(), r4.toString());
        r0.putExtra(getExtraResultsKeyForData(r3), r5);
        goto L10
    L18:
        Api16Impl.setClipData(r9, ClipData.newIntent(RESULTS_CLIP_LABEL, r0));
    }

    public static void addResultsToIntent(RemoteInput[] r9, Intent r10, Bundle r11) {
        if (Build.VERSION.SDK_INT < 26) goto L5;
        Api20Impl.addResultsToIntent(fromCompat(r9), r10, r11);
        return;
    L5:
        Bundle r0 = getResultsFromIntent(r10);
        int r1 = getResultsSource(r10);
        if (r0 != null) goto L8;
        r0 = r11;
    L9:
        int r2 = r9.length;
        int r4 = 0;
    L10:
        if (r4 >= r2) goto L15;
        RemoteInput r5 = r9[r4];
        Map<String, Uri> r6 = getDataResultsFromIntent(r10, r5.getResultKey());
        Api20Impl.addResultsToIntent(fromCompat(new RemoteInput[]{r5}), r10, r0);
        if (r6 == null) goto L14;
        addDataResultToIntent(r5, r10, r6);
    L14:
        r4 = r4 + 1;
        goto L10
    L15:
        setResultsSource(r10, r1);
        return;
    L8:
        r0.putAll(r11);
        goto L9
    }

    static android.app.RemoteInput fromCompat(RemoteInput r1) {
        return Api20Impl.fromCompat(r1);
    }

    static android.app.RemoteInput[] fromCompat(RemoteInput[] r3) {
        if (r3 != null) goto L5;
        return null;
    L5:
        android.app.RemoteInput[] r0 = new android.app.RemoteInput[r3.length];
        int r1 = 0;
    L7:
        if (r1 >= r3.length) goto L9;
        r0[r1] = fromCompat(r3[r1]);
        r1 = r1 + 1;
        goto L7
    L9:
        return r0;
    }

    static RemoteInput fromPlatform(android.app.RemoteInput r1) {
        return Api20Impl.fromPlatform(r1);
    }

    private static Intent getClipDataIntentFromIntent(Intent r5) {
        ClipData r0 = Api16Impl.getClipData(r5);
        if (r0 != null) goto L5;
        return null;
    L5:
        ClipDescription r2 = r0.getDescription();
        if (r2.hasMimeType("text/vnd.android.intent") == true) goto L9;
        return null;
    L9:
        if (r2.getLabel().toString().contentEquals(RESULTS_CLIP_LABEL) == true) goto L12;
        return null;
    L12:
        return r0.getItemAt(0).getIntent();
    }

    public static Map<String, Uri> getDataResultsFromIntent(Intent r10, String r11) {
        if (Build.VERSION.SDK_INT >= 26) goto L5;
        Intent r0 = getClipDataIntentFromIntent(r10);
        if (r0 != null) goto L9;
        return null;
    L9:
        HashMap r2 = new HashMap();
        Iterator<String> r4 = r0.getExtras().keySet().iterator();
    L11:
        if (r4.hasNext() == false) goto L25;
        String r5 = r4.next();
        if (r5.startsWith(EXTRA_DATA_TYPE_RESULTS_DATA) == false) goto L11;
        String r6 = r5.substring(EXTRA_DATA_TYPE_RESULTS_DATA.length());
        if (r6.isEmpty() == true) goto L11;
        String r8 = r0.getBundleExtra(r5).getString(r11);
        if (r8 == null) goto L11;
        if (r8.isEmpty() == true) goto L11;
        r2.put(r6, Uri.parse(r8));
        goto L11
    L25:
        if (r2.isEmpty() == false) goto L28;
        return null;
    L28:
        return r2;
    L5:
        return Api26Impl.getDataResultsFromIntent(r10, r11);
    }

    private static String getExtraResultsKeyForData(String r2) {
        return EXTRA_DATA_TYPE_RESULTS_DATA + r2;
    }

    public static Bundle getResultsFromIntent(Intent r1) {
        return Api20Impl.getResultsFromIntent(r1);
    }

    public static int getResultsSource(Intent r4) {
        if (Build.VERSION.SDK_INT >= 28) goto L5;
        Intent r0 = getClipDataIntentFromIntent(r4);
        if (r0 != null) goto L10;
        return 0;
    L10:
        return r0.getExtras().getInt(EXTRA_RESULTS_SOURCE, 0);
    L5:
        return Api28Impl.getResultsSource(r4);
    }

    public static void setResultsSource(Intent r2, int r3) {
        if (Build.VERSION.SDK_INT < 28) goto L5;
        Api28Impl.setResultsSource(r2, r3);
        return;
    L5:
        Intent r0 = getClipDataIntentFromIntent(r2);
        if (r0 != null) goto L8;
        r0 = new Intent();
    L8:
        r0.putExtra(EXTRA_RESULTS_SOURCE, r3);
        Api16Impl.setClipData(r2, ClipData.newIntent(RESULTS_CLIP_LABEL, r0));
    }

    public boolean getAllowFreeFormInput() {
        return this.mAllowFreeFormTextInput;
    }

    public Set<String> getAllowedDataTypes() {
        return this.mAllowedDataTypes;
    }

    public CharSequence[] getChoices() {
        return this.mChoices;
    }

    public int getEditChoicesBeforeSending() {
        return this.mEditChoicesBeforeSending;
    }

    public Bundle getExtras() {
        return this.mExtras;
    }

    public CharSequence getLabel() {
        return this.mLabel;
    }

    public String getResultKey() {
        return this.mResultKey;
    }

    public boolean isDataOnly() {
        if (getAllowFreeFormInput() == false) goto L5;
    L13:
        return false;
    L5:
        if (getChoices() == null) goto L9;
        if (getChoices().length != 0) goto L13;
    L9:
        if (getAllowedDataTypes() == null) goto L13;
        if (getAllowedDataTypes().isEmpty() == true) goto L13;
        return true;
    }
}
