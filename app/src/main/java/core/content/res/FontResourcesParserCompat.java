package androidx.core.content.res;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.Base64;
import android.util.Xml;
import androidx.core.C0039R;
import androidx.core.provider.FontRequest;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes.dex */
public class FontResourcesParserCompat {
    private static final int DEFAULT_TIMEOUT_MILLIS = 500;
    public static final int FETCH_STRATEGY_ASYNC = 1;
    public static final int FETCH_STRATEGY_BLOCKING = 0;
    public static final int INFINITE_TIMEOUT_VALUE = -1;
    private static final int ITALIC = 1;
    private static final int NORMAL_WEIGHT = 400;

    static class Api21Impl {
        private Api21Impl() {
        }

        static int getType(TypedArray r1, int r2) {
            return r1.getType(r2);
        }
    }

    public interface FamilyResourceEntry {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FetchStrategy {
    }

    public static final class FontFamilyFilesResourceEntry implements FamilyResourceEntry {
        private final FontFileResourceEntry[] mEntries;

        public FontFamilyFilesResourceEntry(FontFileResourceEntry[] r1) {
            this.mEntries = r1;
        }

        public FontFileResourceEntry[] getEntries() {
            return this.mEntries;
        }
    }

    public static final class FontFileResourceEntry {
        private final String mFileName;
        private final boolean mItalic;
        private final int mResourceId;
        private final int mTtcIndex;
        private final String mVariationSettings;
        private final int mWeight;

        public FontFileResourceEntry(String r1, int r2, boolean r3, String r4, int r5, int r6) {
            this.mFileName = r1;
            this.mWeight = r2;
            this.mItalic = r3;
            this.mVariationSettings = r4;
            this.mTtcIndex = r5;
            this.mResourceId = r6;
        }

        public String getFileName() {
            return this.mFileName;
        }

        public int getResourceId() {
            return this.mResourceId;
        }

        public int getTtcIndex() {
            return this.mTtcIndex;
        }

        public String getVariationSettings() {
            return this.mVariationSettings;
        }

        public int getWeight() {
            return this.mWeight;
        }

        public boolean isItalic() {
            return this.mItalic;
        }
    }

    public static final class ProviderResourceEntry implements FamilyResourceEntry {
        private final FontRequest mRequest;
        private final int mStrategy;
        private final String mSystemFontFamilyName;
        private final int mTimeoutMs;

        public ProviderResourceEntry(FontRequest r2, int r3, int r4) {
            this(r2, r3, r4, null);
        }

        public ProviderResourceEntry(FontRequest r1, int r2, int r3, String r4) {
            this.mRequest = r1;
            this.mStrategy = r2;
            this.mTimeoutMs = r3;
            this.mSystemFontFamilyName = r4;
        }

        public int getFetchStrategy() {
            return this.mStrategy;
        }

        public FontRequest getRequest() {
            return this.mRequest;
        }

        public String getSystemFontFamilyName() {
            return this.mSystemFontFamilyName;
        }

        public int getTimeout() {
            return this.mTimeoutMs;
        }
    }

    private FontResourcesParserCompat() {
    }

    private static int getType(TypedArray r1, int r2) {
        return Api21Impl.getType(r1, r2);
    }

    public static FamilyResourceEntry parse(XmlPullParser r3, Resources r4) throws XmlPullParserException, IOException {
    L2:
        int r0 = r3.next();
        if (r0 == 2) goto L7;
        if (r0 != 1) goto L2;
    L7:
        if (r0 != 2) goto L11;
        return readFamilies(r3, r4);
    L11:
        throw new XmlPullParserException("No start tag found");
    }

    public static List<List<byte[]>> readCerts(Resources r6, int r7) {
        if (r7 == 0) goto L4;
        TypedArray r0 = r6.obtainTypedArray(r7);
    L24:
        th = move-exception;
        r0.recycle();
        throw th;
    L7:
        if (r0.length() != 0) goto L11;
        List<List<byte[]>> r1 = Collections.emptyList();     // Catch: Throwable -> L24
        r0.recycle();
        return r1;
    L11:
        ArrayList r2 = new ArrayList();     // Catch: Throwable -> L24
        if (getType(r0, 0) != 1) goto L21;
        int r3 = 0;
    L15:
        if (r3 >= r0.length()) goto L22;
        int r4 = r0.getResourceId(r3, 0);     // Catch: Throwable -> L24
        if (r4 == 0) goto L19;
        r2.add(toByteArrayList(r6.getStringArray(r4)));     // Catch: Throwable -> L24
    L19:
        r3 = r3 + 1;     // Catch: Throwable -> L24
    L22:
        r0.recycle();
        return r2;
    L21:
        r2.add(toByteArrayList(r6.getStringArray(r7)));     // Catch: Throwable -> L24
        goto L22
    L4:
        return Collections.emptyList();
    }

    private static FamilyResourceEntry readFamilies(XmlPullParser r3, Resources r4) throws XmlPullParserException, IOException {
        r3.require(2, null, "font-family");
        if (r3.getName().equals("font-family") == true) goto L5;
        skip(r3);
        return null;
    L5:
        return readFamily(r3, r4);
    }

    private static FamilyResourceEntry readFamily(XmlPullParser r14, Resources r15) throws XmlPullParserException, IOException {
        TypedArray r1 = r15.obtainAttributes(Xml.asAttributeSet(r14), C0039R.styleable.FontFamily);
        String r2 = r1.getString(C0039R.styleable.FontFamily_fontProviderAuthority);
        String r3 = r1.getString(C0039R.styleable.FontFamily_fontProviderPackage);
        String r4 = r1.getString(C0039R.styleable.FontFamily_fontProviderQuery);
        int r5 = r1.getResourceId(C0039R.styleable.FontFamily_fontProviderCerts, 0);
        int r7 = r1.getInteger(C0039R.styleable.FontFamily_fontProviderFetchStrategy, 1);
        int r8 = r1.getInteger(C0039R.styleable.FontFamily_fontProviderFetchTimeout, DEFAULT_TIMEOUT_MILLIS);
        String r9 = r1.getString(C0039R.styleable.FontFamily_fontProviderSystemFontFamily);
        r1.recycle();
        if (r2 == null) goto L11;
        if (r3 == null) goto L11;
        if (r4 == null) goto L11;
    L7:
        if (r14.next() == 3) goto L10;
        skip(r14);
        goto L7
    L10:
        return new ProviderResourceEntry(new FontRequest(r2, r3, r4, readCerts(r15, r5)), r7, r8, r9);
    L11:
        ArrayList r11 = new ArrayList();
    L13:
        if (r14.next() == 3) goto L23;
        if (r14.getEventType() != 2) goto L13;
        if (r14.getName().equals("font") == true) goto L19;
        skip(r14);
        goto L13
    L19:
        r11.add(readFont(r14, r15));
        goto L13
    L23:
        if (r11.isEmpty() == false) goto L27;
        return null;
    L27:
        return new FontFamilyFilesResourceEntry((FontFileResourceEntry[]) r11.toArray(new FontFileResourceEntry[0]));
    }

    private static FontFileResourceEntry readFont(XmlPullParser r16, Resources r17) throws XmlPullParserException, IOException {
        TypedArray r1 = r17.obtainAttributes(Xml.asAttributeSet(r16), C0039R.styleable.FontFamilyFont);
        if (r1.hasValue(C0039R.styleable.FontFamilyFont_fontWeight) == false) goto L5;
        int r3 = C0039R.styleable.FontFamilyFont_fontWeight;
    L6:
        int r7 = r1.getInt(r3, NORMAL_WEIGHT);
        if (r1.hasValue(C0039R.styleable.FontFamilyFont_fontStyle) == false) goto L9;
        int r4 = C0039R.styleable.FontFamilyFont_fontStyle;
    L10:
        boolean r8 = true;
        if (1 == r1.getInt(r4, 0)) goto L15;
        r8 = false;
    L15:
        if (r1.hasValue(C0039R.styleable.FontFamilyFont_ttcIndex) == false) goto L17;
        int r6 = C0039R.styleable.FontFamilyFont_ttcIndex;
    L18:
        int r12 = r6;
        if (r1.hasValue(C0039R.styleable.FontFamilyFont_fontVariationSettings) == false) goto L21;
        int r9 = C0039R.styleable.FontFamilyFont_fontVariationSettings;
    L22:
        String r10 = r1.getString(r9);
        int r11 = r1.getInt(r12, 0);
        if (r1.hasValue(C0039R.styleable.FontFamilyFont_font) == false) goto L25;
        int r13 = C0039R.styleable.FontFamilyFont_font;
    L26:
        int r14 = r13;
        int r15 = r1.getResourceId(r14, 0);
        String r18 = r1.getString(r14);
        r1.recycle();
    L28:
        if (r16.next() == 3) goto L31;
        skip(r16);
        goto L28
    L31:
        return new FontFileResourceEntry(r18, r7, r8, r10, r11, r15);
    L25:
        r13 = C0039R.styleable.FontFamilyFont_android_font;
        goto L26
    L21:
        r9 = C0039R.styleable.FontFamilyFont_android_fontVariationSettings;
        goto L22
    L17:
        r6 = C0039R.styleable.FontFamilyFont_android_ttcIndex;
        goto L18
    L9:
        r4 = C0039R.styleable.FontFamilyFont_android_fontStyle;
        goto L10
    L5:
        r3 = C0039R.styleable.FontFamilyFont_android_fontWeight;
        goto L6
    }

    private static void skip(XmlPullParser r2) throws XmlPullParserException, IOException {
        int r0 = 1;
    L3:
        if (r0 <= 0) goto L10;
        switch(r2.next()) {
            case 2: goto L8;
            case 3: goto L7;
            default: goto L3;
        };
    L7:
        r0 = r0 - 1;
        goto L3
    L8:
        r0 = r0 + 1;
        goto L3
    }

    private static List<byte[]> toByteArrayList(String[] r6) {
        ArrayList r0 = new ArrayList();
        int r1 = r6.length;
        int r3 = 0;
    L3:
        if (r3 >= r1) goto L5;
        r0.add(Base64.decode(r6[r3], 0));
        r3 = r3 + 1;
        goto L3
    L5:
        return r0;
    }
}
