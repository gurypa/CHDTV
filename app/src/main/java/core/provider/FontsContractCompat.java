package androidx.core.provider;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.Handler;
import android.provider.BaseColumns;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.TypefaceCompat;
import androidx.core.graphics.TypefaceCompatUtil;
import androidx.core.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public class FontsContractCompat {

    @Deprecated
    public static final String PARCEL_FONT_RESULTS = "font_results";

    @Deprecated
    static final int RESULT_CODE_PROVIDER_NOT_FOUND = -1;

    @Deprecated
    static final int RESULT_CODE_WRONG_CERTIFICATES = -2;

    public static final class Columns implements BaseColumns {
        public static final String FILE_ID = "file_id";
        public static final String ITALIC = "font_italic";
        public static final String RESULT_CODE = "result_code";
        public static final int RESULT_CODE_FONT_NOT_FOUND = 1;
        public static final int RESULT_CODE_FONT_UNAVAILABLE = 2;
        public static final int RESULT_CODE_MALFORMED_QUERY = 3;
        public static final int RESULT_CODE_OK = 0;
        public static final String TTC_INDEX = "font_ttc_index";
        public static final String VARIATION_SETTINGS = "font_variation_settings";
        public static final String WEIGHT = "font_weight";

        public Columns() {
        }
    }

    public static class FontFamilyResult {
        public static final int STATUS_OK = 0;
        public static final int STATUS_UNEXPECTED_DATA_PROVIDED = 2;
        public static final int STATUS_WRONG_CERTIFICATES = 1;
        private final FontInfo[] mFonts;
        private final int mStatusCode;

        @Deprecated
        public FontFamilyResult(int r1, FontInfo[] r2) {
            this.mStatusCode = r1;
            this.mFonts = r2;
        }

        static FontFamilyResult create(int r1, FontInfo[] r2) {
            return new FontFamilyResult(r1, r2);
        }

        public FontInfo[] getFonts() {
            return this.mFonts;
        }

        public int getStatusCode() {
            return this.mStatusCode;
        }
    }

    public static class FontInfo {
        private final boolean mItalic;
        private final int mResultCode;
        private final int mTtcIndex;
        private final Uri mUri;
        private final int mWeight;

        @Deprecated
        public FontInfo(Uri r2, int r3, int r4, boolean r5, int r6) {
            this.mUri = (Uri) Preconditions.checkNotNull(r2);
            this.mTtcIndex = r3;
            this.mWeight = r4;
            this.mItalic = r5;
            this.mResultCode = r6;
        }

        static FontInfo create(Uri r6, int r7, int r8, boolean r9, int r10) {
            return new FontInfo(r6, r7, r8, r9, r10);
        }

        public int getResultCode() {
            return this.mResultCode;
        }

        public int getTtcIndex() {
            return this.mTtcIndex;
        }

        public Uri getUri() {
            return this.mUri;
        }

        public int getWeight() {
            return this.mWeight;
        }

        public boolean isItalic() {
            return this.mItalic;
        }
    }

    public static class FontRequestCallback {
        public static final int FAIL_REASON_FONT_LOAD_ERROR = -3;
        public static final int FAIL_REASON_FONT_NOT_FOUND = 1;
        public static final int FAIL_REASON_FONT_UNAVAILABLE = 2;
        public static final int FAIL_REASON_MALFORMED_QUERY = 3;
        public static final int FAIL_REASON_PROVIDER_NOT_FOUND = -1;
        public static final int FAIL_REASON_SECURITY_VIOLATION = -4;
        public static final int FAIL_REASON_WRONG_CERTIFICATES = -2;

        @Deprecated
        public static final int RESULT_OK = 0;
        static final int RESULT_SUCCESS = 0;

        @Retention(RetentionPolicy.SOURCE)
        public @interface FontRequestFailReason {
        }

        public FontRequestCallback() {
        }

        public void onTypefaceRequestFailed(int r1) {
        }

        public void onTypefaceRetrieved(Typeface r1) {
        }
    }

    private FontsContractCompat() {
    }

    public static Typeface buildTypeface(Context r1, CancellationSignal r2, FontInfo[] r3) {
        return TypefaceCompat.createFromFontInfo(r1, r2, r3, 0);
    }

    public static FontFamilyResult fetchFonts(Context r1, CancellationSignal r2, FontRequest r3) throws PackageManager.NameNotFoundException {
        return FontProvider.getFontFamilyResult(r1, r3, r2);
    }

    @Deprecated
    public static Typeface getFontSync(Context r8, FontRequest r9, ResourcesCompat.FontCallback r10, Handler r11, boolean r12, int r13, int r14) {
        TypefaceCompat.ResourcesCallbackAdapter r0 = new TypefaceCompat.ResourcesCallbackAdapter(r10);
        return requestFont(r8, r9, r14, r12, r13, ResourcesCompat.FontCallback.getHandler(r11), r0);
    }

    @Deprecated
    public static ProviderInfo getProvider(PackageManager r1, FontRequest r2, Resources r3) throws PackageManager.NameNotFoundException {
        return FontProvider.getProvider(r1, r2, r3);
    }

    @Deprecated
    public static Map<Uri, ByteBuffer> prepareFontData(Context r1, FontInfo[] r2, CancellationSignal r3) {
        return TypefaceCompatUtil.readFontInfoIntoByteBuffer(r1, r2, r3);
    }

    public static Typeface requestFont(Context r2, FontRequest r3, int r4, boolean r5, int r6, Handler r7, FontRequestCallback r8) {
        CallbackWithHandler r0 = new CallbackWithHandler(r8, r7);
        if (r5 == false) goto L7;
        return FontRequestWorker.requestFontSync(r2, r3, r0, r4, r6);
    L7:
        return FontRequestWorker.requestFontAsync(r2, r3, r4, null, r0);
    }

    public static void requestFont(Context r4, FontRequest r5, FontRequestCallback r6, Handler r7) {
        CallbackWithHandler r0 = new CallbackWithHandler(r6);
        Executor r1 = RequestExecutor.createHandlerExecutor(r7);
        FontRequestWorker.requestFontAsync(r4.getApplicationContext(), r5, 0, r1, r0);
    }

    @Deprecated
    public static void resetCache() {
        FontRequestWorker.resetTypefaceCache();
    }

    public static void resetTypefaceCache() {
        FontRequestWorker.resetTypefaceCache();
    }
}
