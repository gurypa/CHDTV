package androidx.core.app;

import android.app.Activity;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.ActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import androidx.core.content.IntentCompat;
import androidx.core.util.Preconditions;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class ShareCompat {
    public static final String EXTRA_CALLING_ACTIVITY = "androidx.core.app.EXTRA_CALLING_ACTIVITY";
    public static final String EXTRA_CALLING_ACTIVITY_INTEROP = "android.support.v4.app.EXTRA_CALLING_ACTIVITY";
    public static final String EXTRA_CALLING_PACKAGE = "androidx.core.app.EXTRA_CALLING_PACKAGE";
    public static final String EXTRA_CALLING_PACKAGE_INTEROP = "android.support.v4.app.EXTRA_CALLING_PACKAGE";
    private static final String HISTORY_FILENAME_PREFIX = ".sharecompat_";

    private static class Api16Impl {
        private Api16Impl() {
        }

        static String escapeHtml(CharSequence r1) {
            return Html.escapeHtml(r1);
        }

        static void migrateExtraStreamToClipData(Intent r7, ArrayList<Uri> r8) {
            CharSequence r0 = r7.getCharSequenceExtra("android.intent.extra.TEXT");
            String r1 = r7.getStringExtra(IntentCompat.EXTRA_HTML_TEXT);
            ClipData r2 = new ClipData(null, new String[]{r7.getType()}, new ClipData.Item(r0, r1, null, r8.get(0)));
            int r3 = 1;
            int r4 = r8.size();
        L3:
            if (r3 >= r4) goto L5;
            r2.addItem(new ClipData.Item(r8.get(r3)));
            r3 = r3 + 1;
            goto L3
        L5:
            r7.setClipData(r2);
            r7.addFlags(1);
        }

        static void removeClipData(Intent r1) {
            r1.setClipData(null);
            r1.setFlags(r1.getFlags() & (-2));
        }
    }

    public static class IntentBuilder {
        private ArrayList<String> mBccAddresses;
        private ArrayList<String> mCcAddresses;
        private CharSequence mChooserTitle;
        private final Context mContext;
        private final Intent mIntent;
        private ArrayList<Uri> mStreams;
        private ArrayList<String> mToAddresses;

        public IntentBuilder(Context r6) {
            this.mContext = (Context) Preconditions.checkNotNull(r6);
            this.mIntent = new Intent().setAction("android.intent.action.SEND");
            this.mIntent.putExtra(ShareCompat.EXTRA_CALLING_PACKAGE, r6.getPackageName());
            this.mIntent.putExtra(ShareCompat.EXTRA_CALLING_PACKAGE_INTEROP, r6.getPackageName());
            this.mIntent.addFlags(524288);
            Activity r0 = null;
            Context r1 = r6;
        L4:
            if ((r1 instanceof ContextWrapper) == false) goto L9;
            if ((r1 instanceof Activity) == true) goto L7;
            r1 = ((ContextWrapper) r1).getBaseContext();
            goto L4
        L7:
            r0 = (Activity) r1;
        L9:
            if (r0 == null) goto L14;
            ComponentName r2 = r0.getComponentName();
            this.mIntent.putExtra(ShareCompat.EXTRA_CALLING_ACTIVITY, r2);
            this.mIntent.putExtra(ShareCompat.EXTRA_CALLING_ACTIVITY_INTEROP, r2);
            return;
        }

        private void combineArrayExtra(String r6, ArrayList<String> r7) {
            String[] r0 = this.mIntent.getStringArrayExtra(r6);
            if (r0 == null) goto L5;
            int r2 = r0.length;
        L6:
            String[] r3 = new String[r7.size() + r2];
            r7.toArray(r3);
            if (r0 == null) goto L9;
            System.arraycopy(r0, 0, r3, r7.size(), r2);
        L9:
            this.mIntent.putExtra(r6, r3);
            return;
        L5:
            r2 = 0;
            goto L6
        }

        private void combineArrayExtra(String r7, String[] r8) {
            Intent r0 = getIntent();
            String[] r1 = r0.getStringArrayExtra(r7);
            if (r1 == null) goto L5;
            int r3 = r1.length;
        L6:
            String[] r4 = new String[r8.length + r3];
            if (r1 == null) goto L9;
            System.arraycopy(r1, 0, r4, 0, r3);
        L9:
            System.arraycopy(r8, 0, r4, r3, r8.length);
            r0.putExtra(r7, r4);
            return;
        L5:
            r3 = 0;
            goto L6
        }

        @Deprecated
        public static IntentBuilder from(Activity r1) {
            return new IntentBuilder(r1);
        }

        public IntentBuilder addEmailBcc(String r2) {
            if (this.mBccAddresses != null) goto L5;
            this.mBccAddresses = new ArrayList();
        L5:
            this.mBccAddresses.add(r2);
            return this;
        }

        public IntentBuilder addEmailBcc(String[] r2) {
            combineArrayExtra("android.intent.extra.BCC", r2);
            return this;
        }

        public IntentBuilder addEmailCc(String r2) {
            if (this.mCcAddresses != null) goto L5;
            this.mCcAddresses = new ArrayList();
        L5:
            this.mCcAddresses.add(r2);
            return this;
        }

        public IntentBuilder addEmailCc(String[] r2) {
            combineArrayExtra("android.intent.extra.CC", r2);
            return this;
        }

        public IntentBuilder addEmailTo(String r2) {
            if (this.mToAddresses != null) goto L5;
            this.mToAddresses = new ArrayList();
        L5:
            this.mToAddresses.add(r2);
            return this;
        }

        public IntentBuilder addEmailTo(String[] r2) {
            combineArrayExtra("android.intent.extra.EMAIL", r2);
            return this;
        }

        public IntentBuilder addStream(Uri r2) {
            if (this.mStreams != null) goto L5;
            this.mStreams = new ArrayList();
        L5:
            this.mStreams.add(r2);
            return this;
        }

        public Intent createChooserIntent() {
            return Intent.createChooser(getIntent(), this.mChooserTitle);
        }

        Context getContext() {
            return this.mContext;
        }

        public Intent getIntent() {
            if (this.mToAddresses == null) goto L6;
            combineArrayExtra("android.intent.extra.EMAIL", this.mToAddresses);
            this.mToAddresses = null;
        L6:
            if (this.mCcAddresses == null) goto L9;
            combineArrayExtra("android.intent.extra.CC", this.mCcAddresses);
            this.mCcAddresses = null;
        L9:
            if (this.mBccAddresses == null) goto L12;
            combineArrayExtra("android.intent.extra.BCC", this.mBccAddresses);
            this.mBccAddresses = null;
        L12:
            if (this.mStreams == null) goto L16;
            boolean r2 = true;
            if (this.mStreams.size() <= 1) goto L16;
        L18:
            if (r2 == true) goto L25;
            this.mIntent.setAction("android.intent.action.SEND");
            if (this.mStreams != null) goto L22;
        L24:
            this.mIntent.removeExtra("android.intent.extra.STREAM");
            Api16Impl.removeClipData(this.mIntent);
        L27:
            return this.mIntent;
        L22:
            if (this.mStreams.isEmpty() == true) goto L24;
            this.mIntent.putExtra("android.intent.extra.STREAM", this.mStreams.get(0));
            Api16Impl.migrateExtraStreamToClipData(this.mIntent, this.mStreams);
            goto L27
        L25:
            this.mIntent.setAction("android.intent.action.SEND_MULTIPLE");
            this.mIntent.putParcelableArrayListExtra("android.intent.extra.STREAM", this.mStreams);
            Api16Impl.migrateExtraStreamToClipData(this.mIntent, this.mStreams);
        L16:
            r2 = false;
            goto L18
        }

        public IntentBuilder setChooserTitle(int r2) {
            return setChooserTitle(this.mContext.getText(r2));
        }

        public IntentBuilder setChooserTitle(CharSequence r1) {
            this.mChooserTitle = r1;
            return this;
        }

        public IntentBuilder setEmailBcc(String[] r3) {
            this.mIntent.putExtra("android.intent.extra.BCC", r3);
            return this;
        }

        public IntentBuilder setEmailCc(String[] r3) {
            this.mIntent.putExtra("android.intent.extra.CC", r3);
            return this;
        }

        public IntentBuilder setEmailTo(String[] r3) {
            if (this.mToAddresses == null) goto L5;
            this.mToAddresses = null;
        L5:
            this.mIntent.putExtra("android.intent.extra.EMAIL", r3);
            return this;
        }

        public IntentBuilder setHtmlText(String r3) {
            this.mIntent.putExtra(IntentCompat.EXTRA_HTML_TEXT, r3);
            if (this.mIntent.hasExtra("android.intent.extra.TEXT") == true) goto L5;
            setText(Html.fromHtml(r3));
        L5:
            return this;
        }

        public IntentBuilder setStream(Uri r2) {
            this.mStreams = null;
            if (r2 == null) goto L5;
            addStream(r2);
        L5:
            return this;
        }

        public IntentBuilder setSubject(String r3) {
            this.mIntent.putExtra("android.intent.extra.SUBJECT", r3);
            return this;
        }

        public IntentBuilder setText(CharSequence r3) {
            this.mIntent.putExtra("android.intent.extra.TEXT", r3);
            return this;
        }

        public IntentBuilder setType(String r2) {
            this.mIntent.setType(r2);
            return this;
        }

        public void startChooser() {
            this.mContext.startActivity(createChooserIntent());
        }
    }

    public static class IntentReader {
        private static final String TAG = "IntentReader";
        private final ComponentName mCallingActivity;
        private final String mCallingPackage;
        private final Context mContext;
        private final Intent mIntent;
        private ArrayList<Uri> mStreams;

        public IntentReader(Activity r3) {
            this((Context) Preconditions.checkNotNull(r3), r3.getIntent());
        }

        public IntentReader(Context r2, Intent r3) {
            this.mContext = (Context) Preconditions.checkNotNull(r2);
            this.mIntent = (Intent) Preconditions.checkNotNull(r3);
            this.mCallingPackage = ShareCompat.getCallingPackage(r3);
            this.mCallingActivity = ShareCompat.getCallingActivity(r3);
        }

        @Deprecated
        public static IntentReader from(Activity r1) {
            return new IntentReader(r1);
        }

        private static void withinStyle(StringBuilder r4, CharSequence r5, int r6, int r7) {
            int r0 = r6;
        L3:
            if (r0 >= r7) goto L28;
            char r1 = r5.charAt(r0);
            if (r1 != '<') goto L8;
            r4.append("&lt;");
        L27:
            r0 = r0 + 1;
            goto L3
        L8:
            if (r1 != '>') goto L11;
            r4.append("&gt;");
            goto L27
        L11:
            if (r1 != '&') goto L14;
            r4.append("&amp;");
            goto L27
        L14:
            if (r1 <= '~') goto L16;
        L26:
            r4.append("&#").append(r1).append(";");
            goto L27
        L16:
            if (r1 < ' ') goto L26;
            if (r1 == ' ') goto L20;
            r4.append(r1);
        L20:
            if ((r0 + 1) >= r7) goto L24;
            if (r5.charAt(r0 + 1) != ' ') goto L24;
            r4.append("&nbsp;");
            r0 = r0 + 1;
        L24:
            r4.append(' ');
            goto L27
        }

        public ComponentName getCallingActivity() {
            return this.mCallingActivity;
        }

        public Drawable getCallingActivityIcon() {
            if (this.mCallingActivity != null) goto L5;
            return null;
        L5:
            PackageManager r0 = this.mContext.getPackageManager();
            return r0.getActivityIcon(this.mCallingActivity);
        L8:
            e = move-exception;
            Log.e(TAG, "Could not retrieve icon for calling activity", e);
            return null;
        }

        public Drawable getCallingApplicationIcon() {
            if (this.mCallingPackage != null) goto L5;
            return null;
        L5:
            PackageManager r0 = this.mContext.getPackageManager();
            return r0.getApplicationIcon(this.mCallingPackage);
        L8:
            e = move-exception;
            Log.e(TAG, "Could not retrieve icon for calling application", e);
            return null;
        }

        public CharSequence getCallingApplicationLabel() {
            if (this.mCallingPackage != null) goto L5;
            return null;
        L5:
            PackageManager r0 = this.mContext.getPackageManager();
            return r0.getApplicationLabel(r0.getApplicationInfo(this.mCallingPackage, 0));
        L8:
            e = move-exception;
            Log.e(TAG, "Could not retrieve label for calling application", e);
            return null;
        }

        public String getCallingPackage() {
            return this.mCallingPackage;
        }

        public String[] getEmailBcc() {
            return this.mIntent.getStringArrayExtra("android.intent.extra.BCC");
        }

        public String[] getEmailCc() {
            return this.mIntent.getStringArrayExtra("android.intent.extra.CC");
        }

        public String[] getEmailTo() {
            return this.mIntent.getStringArrayExtra("android.intent.extra.EMAIL");
        }

        public String getHtmlText() {
            String r0 = this.mIntent.getStringExtra(IntentCompat.EXTRA_HTML_TEXT);
            if (r0 != null) goto L10;
            CharSequence r1 = getText();
            if ((r1 instanceof Spanned) == true) goto L11;
            if (r1 != null) goto L9;
            return r0;
        L9:
            return Api16Impl.escapeHtml(r1);
        L11:
            return Html.toHtml((Spanned) r1);
        L10:
            return r0;
        }

        public Uri getStream() {
            return (Uri) this.mIntent.getParcelableExtra("android.intent.extra.STREAM");
        }

        public Uri getStream(int r4) {
            if (this.mStreams != null) goto L8;
            if (isMultipleShare() == false) goto L8;
            this.mStreams = this.mIntent.getParcelableArrayListExtra("android.intent.extra.STREAM");
        L8:
            if (this.mStreams != null) goto L10;
            if (r4 != 0) goto L15;
            return (Uri) this.mIntent.getParcelableExtra("android.intent.extra.STREAM");
        L15:
            throw new IndexOutOfBoundsException("Stream items available: " + getStreamCount() + " index requested: " + r4);
        L10:
            return this.mStreams.get(r4);
        }

        public int getStreamCount() {
            if (this.mStreams != null) goto L8;
            if (isMultipleShare() == false) goto L8;
            this.mStreams = this.mIntent.getParcelableArrayListExtra("android.intent.extra.STREAM");
        L8:
            if (this.mStreams == null) goto L12;
            return this.mStreams.size();
        L12:
            return this.mIntent.hasExtra("android.intent.extra.STREAM") ? 1 : 0;
        }

        public String getSubject() {
            return this.mIntent.getStringExtra("android.intent.extra.SUBJECT");
        }

        public CharSequence getText() {
            return this.mIntent.getCharSequenceExtra("android.intent.extra.TEXT");
        }

        public String getType() {
            return this.mIntent.getType();
        }

        public boolean isMultipleShare() {
            return "android.intent.action.SEND_MULTIPLE".equals(this.mIntent.getAction());
        }

        public boolean isShareIntent() {
            String r0 = this.mIntent.getAction();
            if ("android.intent.action.SEND".equals(r0) == false) goto L5;
        L8:
            return true;
        L5:
            if ("android.intent.action.SEND_MULTIPLE".equals(r0) == true) goto L8;
            return false;
        }

        public boolean isSingleShare() {
            return "android.intent.action.SEND".equals(this.mIntent.getAction());
        }
    }

    private ShareCompat() {
    }

    @Deprecated
    public static void configureMenuItem(Menu r4, int r5, IntentBuilder r6) {
        MenuItem r0 = r4.findItem(r5);
        if (r0 == null) goto L7;
        configureMenuItem(r0, r6);
        return;
    L7:
        throw new IllegalArgumentException("Could not find menu item with id " + r5 + " in the supplied menu");
    }

    @Deprecated
    public static void configureMenuItem(MenuItem r4, IntentBuilder r5) {
        ActionProvider r0 = r4.getActionProvider();
        if ((r0 instanceof ShareActionProvider) == true) goto L5;
        ShareActionProvider r1 = new ShareActionProvider(r5.getContext());
    L6:
        r1.setShareHistoryFileName(HISTORY_FILENAME_PREFIX + r5.getContext().getClass().getName());
        r1.setShareIntent(r5.getIntent());
        r4.setActionProvider(r1);
        return;
    L5:
        r1 = (ShareActionProvider) r0;
        goto L6
    }

    public static ComponentName getCallingActivity(Activity r2) {
        Intent r0 = r2.getIntent();
        ComponentName r1 = r2.getCallingActivity();
        if (r1 == null) goto L5;
        return r1;
    L5:
        return getCallingActivity(r0);
    }

    static ComponentName getCallingActivity(Intent r2) {
        ComponentName r0 = (ComponentName) r2.getParcelableExtra(EXTRA_CALLING_ACTIVITY);
        if (r0 == null) goto L5;
        return r0;
    L5:
        return (ComponentName) r2.getParcelableExtra(EXTRA_CALLING_ACTIVITY_INTEROP);
    }

    public static String getCallingPackage(Activity r2) {
        Intent r0 = r2.getIntent();
        String r1 = r2.getCallingPackage();
        if (r1 != null) goto L7;
        if (r0 != null) goto L6;
        return r1;
    L6:
        return getCallingPackage(r0);
    L7:
        return r1;
    }

    static String getCallingPackage(Intent r2) {
        String r0 = r2.getStringExtra(EXTRA_CALLING_PACKAGE);
        if (r0 == null) goto L5;
        return r0;
    L5:
        return r2.getStringExtra(EXTRA_CALLING_PACKAGE_INTEROP);
    }
}
