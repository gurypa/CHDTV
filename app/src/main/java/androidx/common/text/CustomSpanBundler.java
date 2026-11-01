package androidx.media3.common.text;

import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
final class CustomSpanBundler {
    private static final String FIELD_END_INDEX = null;
    private static final String FIELD_FLAGS = null;
    private static final String FIELD_PARAMS = null;
    private static final String FIELD_START_INDEX = null;
    private static final String FIELD_TYPE = null;
    private static final int HORIZONTAL_TEXT_IN_VERTICAL_CONTEXT = 3;
    private static final int RUBY = 1;
    private static final int TEXT_EMPHASIS = 2;
    private static final int UNKNOWN = -1;
    private static final int VOICE = 4;

    static {
        FIELD_START_INDEX = Util.intToStringMaxRadix(0);
        FIELD_END_INDEX = Util.intToStringMaxRadix(1);
        FIELD_FLAGS = Util.intToStringMaxRadix(2);
        FIELD_TYPE = Util.intToStringMaxRadix(3);
        FIELD_PARAMS = Util.intToStringMaxRadix(4);
    }

    private CustomSpanBundler() {
    }

    public static ArrayList<Bundle> bundleCustomSpans(Spanned r8) {
        ArrayList<Bundle> r0 = new ArrayList();
        int r3 = 0;
        RubySpan[] r1 = (RubySpan[]) r8.getSpans(0, r8.length(), RubySpan.class);
        int r2 = r1.length;
        int r4 = 0;
    L3:
        if (r4 >= r2) goto L5;
        RubySpan r5 = r1[r4];
        r0.add(spanToBundle(r8, r5, 1, r5.toBundle()));
        r4 = r4 + 1;
        goto L3
    L5:
        TextEmphasisSpan[] r6 = (TextEmphasisSpan[]) r8.getSpans(0, r8.length(), TextEmphasisSpan.class);
        int r7 = r6.length;
        int r9 = 0;
    L6:
        if (r9 >= r7) goto L8;
        TextEmphasisSpan r10 = r6[r9];
        r0.add(spanToBundle(r8, r10, 2, r10.toBundle()));
        r9 = r9 + 1;
        goto L6
    L8:
        HorizontalTextInVerticalContextSpan[] r11 = (HorizontalTextInVerticalContextSpan[]) r8.getSpans(0, r8.length(), HorizontalTextInVerticalContextSpan.class);
        int r12 = r11.length;
        int r13 = 0;
    L9:
        if (r13 >= r12) goto L11;
        r0.add(spanToBundle(r8, r11[r13], 3, null));
        r13 = r13 + 1;
        goto L9
    L11:
        VoiceSpan[] r14 = (VoiceSpan[]) r8.getSpans(0, r8.length(), VoiceSpan.class);
        int r15 = r14.length;
    L12:
        if (r3 >= r15) goto L14;
        VoiceSpan r16 = r14[r3];
        r0.add(spanToBundle(r8, r16, 4, r16.toBundle()));
        r3 = r3 + 1;
        goto L12
    L14:
        return r0;
    }

    private static Bundle spanToBundle(Spanned r3, Object r4, int r5, Bundle r6) {
        Bundle r0 = new Bundle();
        r0.putInt(FIELD_START_INDEX, r3.getSpanStart(r4));
        r0.putInt(FIELD_END_INDEX, r3.getSpanEnd(r4));
        r0.putInt(FIELD_FLAGS, r3.getSpanFlags(r4));
        r0.putInt(FIELD_TYPE, r5);
        if (r6 == null) goto L5;
        r0.putBundle(FIELD_PARAMS, r6);
    L5:
        return r0;
    }

    public static void unbundleAndApplyCustomSpan(Bundle r6, Spannable r7) {
        int r0 = r6.getInt(FIELD_START_INDEX);
        int r1 = r6.getInt(FIELD_END_INDEX);
        int r2 = r6.getInt(FIELD_FLAGS);
        int r3 = r6.getInt(FIELD_TYPE, -1);
        Bundle r4 = r6.getBundle(FIELD_PARAMS);
        switch(r3) {
            case 1: goto L8;
            case 2: goto L7;
            case 3: goto L6;
            case 4: goto L5;
            default: goto L13;
        };
    L5:
        r7.setSpan(VoiceSpan.fromBundle((Bundle) Assertions.checkNotNull(r4)), r0, r1, r2);
        return;
    L6:
        r7.setSpan(new HorizontalTextInVerticalContextSpan(), r0, r1, r2);
        return;
    L7:
        r7.setSpan(TextEmphasisSpan.fromBundle((Bundle) Assertions.checkNotNull(r4)), r0, r1, r2);
        return;
    L8:
        r7.setSpan(RubySpan.fromBundle((Bundle) Assertions.checkNotNull(r4)), r0, r1, r2);
        return;
    }
}
