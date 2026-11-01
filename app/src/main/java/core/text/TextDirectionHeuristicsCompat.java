package androidx.core.text;

import java.nio.CharBuffer;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public final class TextDirectionHeuristicsCompat {
    public static final TextDirectionHeuristicCompat ANYRTL_LTR = null;
    public static final TextDirectionHeuristicCompat FIRSTSTRONG_LTR = null;
    public static final TextDirectionHeuristicCompat FIRSTSTRONG_RTL = null;
    public static final TextDirectionHeuristicCompat LOCALE = null;
    public static final TextDirectionHeuristicCompat LTR = null;
    public static final TextDirectionHeuristicCompat RTL = null;
    private static final int STATE_FALSE = 1;
    private static final int STATE_TRUE = 0;
    private static final int STATE_UNKNOWN = 2;

    private static class AnyStrong implements TextDirectionAlgorithm {
        static final AnyStrong INSTANCE_RTL = null;
        private final boolean mLookForRtl;

        static {
            INSTANCE_RTL = new AnyStrong(true);
        }

        private AnyStrong(boolean r1) {
            this.mLookForRtl = r1;
        }

        @Override // androidx.core.text.TextDirectionHeuristicsCompat.TextDirectionAlgorithm
        public int checkRtl(CharSequence r5, int r6, int r7) {
            boolean r0 = false;
            int r1 = r6;
            int r2 = r6 + r7;
        L3:
            if (r1 >= r2) goto L18;
            switch(TextDirectionHeuristicsCompat.isRtlText(Character.getDirectionality(r5.charAt(r1)))) {
                case 0: goto L13;
                case 1: goto L8;
                default: goto L17;
            };
        L8:
            if (this.mLookForRtl == false) goto L9;
            r0 = true;
            goto L17
        L9:
            return 1;
        L13:
            if (this.mLookForRtl == true) goto L14;
            r0 = true;
            goto L17
        L14:
            return 0;
        L17:
            r1 = r1 + 1;
            goto L3
        L18:
            if (r0 == true) goto L20;
            return 2;
        L20:
            return this.mLookForRtl ? 1 : 0;
        }
    }

    private static class FirstStrong implements TextDirectionAlgorithm {
        static final FirstStrong INSTANCE = null;

        static {
            INSTANCE = new FirstStrong();
        }

        private FirstStrong() {
        }

        @Override // androidx.core.text.TextDirectionHeuristicsCompat.TextDirectionAlgorithm
        public int checkRtl(CharSequence r5, int r6, int r7) {
            int r0 = 2;
            int r1 = r6;
            int r2 = r6 + r7;
        L3:
            if (r1 >= r2) goto L7;
            if (r0 != 2) goto L7;
            r0 = TextDirectionHeuristicsCompat.isRtlTextOrFormat(Character.getDirectionality(r5.charAt(r1)));
            r1 = r1 + 1;
        L7:
            return r0;
        }
    }

    private interface TextDirectionAlgorithm {
        int checkRtl(CharSequence r1, int r2, int r3);
    }

    private static abstract class TextDirectionHeuristicImpl implements TextDirectionHeuristicCompat {
        private final TextDirectionAlgorithm mAlgorithm;

        TextDirectionHeuristicImpl(TextDirectionAlgorithm r1) {
            this.mAlgorithm = r1;
        }

        private boolean doCheck(CharSequence r2, int r3, int r4) {
            switch(this.mAlgorithm.checkRtl(r2, r3, r4)) {
                case 0: goto L8;
                case 1: goto L6;
                default: goto L5;
            };
        L6:
            return false;
        L8:
            return true;
        L5:
            return defaultIsRtl();
        }

        protected abstract boolean defaultIsRtl();

        @Override // androidx.core.text.TextDirectionHeuristicCompat
        public boolean isRtl(CharSequence r2, int r3, int r4) {
            if (r2 == null) goto L14;
            if (r3 < 0) goto L14;
            if (r4 < 0) goto L14;
            if ((r2.length() - r4) < r3) goto L14;
            if (this.mAlgorithm != null) goto L12;
            return defaultIsRtl();
        L12:
            return doCheck(r2, r3, r4);
        L14:
            throw new IllegalArgumentException();
        }

        @Override // androidx.core.text.TextDirectionHeuristicCompat
        public boolean isRtl(char[] r2, int r3, int r4) {
            return isRtl(CharBuffer.wrap(r2), r3, r4);
        }
    }

    private static class TextDirectionHeuristicInternal extends TextDirectionHeuristicImpl {
        private final boolean mDefaultIsRtl;

        TextDirectionHeuristicInternal(TextDirectionAlgorithm r1, boolean r2) {
            super(r1);
            this.mDefaultIsRtl = r2;
        }

        @Override // androidx.core.text.TextDirectionHeuristicsCompat.TextDirectionHeuristicImpl
        protected boolean defaultIsRtl() {
            return this.mDefaultIsRtl;
        }
    }

    private static class TextDirectionHeuristicLocale extends TextDirectionHeuristicImpl {
        static final TextDirectionHeuristicLocale INSTANCE = null;

        static {
            INSTANCE = new TextDirectionHeuristicLocale();
        }

        TextDirectionHeuristicLocale() {
            super(null);
        }

        @Override // androidx.core.text.TextDirectionHeuristicsCompat.TextDirectionHeuristicImpl
        protected boolean defaultIsRtl() {
            if (TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == 1) goto L7;
            return false;
        L7:
            return true;
        }
    }

    static {
        LTR = new TextDirectionHeuristicInternal(null, false);
        RTL = new TextDirectionHeuristicInternal(null, true);
        FIRSTSTRONG_LTR = new TextDirectionHeuristicInternal(FirstStrong.INSTANCE, false);
        FIRSTSTRONG_RTL = new TextDirectionHeuristicInternal(FirstStrong.INSTANCE, true);
        ANYRTL_LTR = new TextDirectionHeuristicInternal(AnyStrong.INSTANCE_RTL, false);
        LOCALE = TextDirectionHeuristicLocale.INSTANCE;
    }

    private TextDirectionHeuristicsCompat() {
    }

    static int isRtlText(int r1) {
        switch(r1) {
            case 0: goto L7;
            case 1: goto L5;
            case 2: goto L5;
            default: goto L3;
        };
    L3:
        return 2;
    L5:
        return 0;
    L7:
        return 1;
    }

    static int isRtlTextOrFormat(int r1) {
        switch(r1) {
            case 0: goto L7;
            case 1: goto L5;
            case 2: goto L5;
            case 14: goto L7;
            case 15: goto L7;
            case 16: goto L5;
            case 17: goto L5;
            default: goto L3;
        };
    L3:
        return 2;
    L5:
        return 0;
    L7:
        return 1;
    }
}
