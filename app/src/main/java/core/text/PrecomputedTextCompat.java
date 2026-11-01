package androidx.core.text;

import android.os.Build;
import android.text.PrecomputedText;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.MetricAffectingSpan;
import androidx.core.os.TraceCompat;
import androidx.core.util.ObjectsCompat;
import androidx.core.util.Preconditions;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/* JADX INFO: loaded from: classes.dex */
public class PrecomputedTextCompat implements Spannable {
    private static final char LINE_FEED = '\n';
    private static Executor sExecutor;
    private static final Object sLock = null;
    private final int[] mParagraphEnds;
    private final Params mParams;
    private final Spannable mText;
    private final PrecomputedText mWrapped;

    public static final class Params {
        private final int mBreakStrategy;
        private final int mHyphenationFrequency;
        private final TextPaint mPaint;
        private final TextDirectionHeuristic mTextDir;
        final PrecomputedText.Params mWrapped;

        public static class Builder {
            private int mBreakStrategy;
            private int mHyphenationFrequency;
            private final TextPaint mPaint;
            private TextDirectionHeuristic mTextDir;

            public Builder(TextPaint r2) {
                this.mPaint = r2;
                this.mBreakStrategy = 1;
                this.mHyphenationFrequency = 1;
                this.mTextDir = TextDirectionHeuristics.FIRSTSTRONG_LTR;
            }

            public Params build() {
                return new Params(this.mPaint, this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency);
            }

            public Builder setBreakStrategy(int r1) {
                this.mBreakStrategy = r1;
                return this;
            }

            public Builder setHyphenationFrequency(int r1) {
                this.mHyphenationFrequency = r1;
                return this;
            }

            public Builder setTextDirection(TextDirectionHeuristic r1) {
                this.mTextDir = r1;
                return this;
            }
        }

        public Params(PrecomputedText.Params r3) {
            this.mPaint = r3.getTextPaint();
            this.mTextDir = r3.getTextDirection();
            this.mBreakStrategy = r3.getBreakStrategy();
            this.mHyphenationFrequency = r3.getHyphenationFrequency();
            if (Build.VERSION.SDK_INT < 29) goto L5;
            PrecomputedText.Params r0 = r3;
        L6:
            this.mWrapped = r0;
            return;
        L5:
            r0 = null;
            goto L6
        }

        Params(TextPaint r3, TextDirectionHeuristic r4, int r5, int r6) {
            if (Build.VERSION.SDK_INT < 29) goto L5;
            this.mWrapped = new PrecomputedText.Params.Builder(r3).setBreakStrategy(r5).setHyphenationFrequency(r6).setTextDirection(r4).build();
        L6:
            this.mPaint = r3;
            this.mTextDir = r4;
            this.mBreakStrategy = r5;
            this.mHyphenationFrequency = r6;
            return;
        L5:
            this.mWrapped = null;
            goto L6
        }

        public boolean equals(Object r6) {
            if (r6 != this) goto L6;
            return true;
        L6:
            if ((r6 instanceof Params) == true) goto L8;
            return false;
        L8:
            Params r1 = (Params) r6;
            if (equalsWithoutTextDirection(r1) == true) goto L12;
            return false;
        L12:
            if (this.mTextDir == r1.getTextDirection()) goto L14;
            return false;
        L14:
            return true;
        }

        public boolean equalsWithoutTextDirection(Params r4) {
            if (this.mBreakStrategy == r4.getBreakStrategy()) goto L6;
            return false;
        L6:
            if (this.mHyphenationFrequency == r4.getHyphenationFrequency()) goto L9;
            return false;
        L9:
            if (this.mPaint.getTextSize() == r4.getTextPaint().getTextSize()) goto L12;
            return false;
        L12:
            if (this.mPaint.getTextScaleX() == r4.getTextPaint().getTextScaleX()) goto L15;
            return false;
        L15:
            if (this.mPaint.getTextSkewX() == r4.getTextPaint().getTextSkewX()) goto L18;
            return false;
        L18:
            if (this.mPaint.getLetterSpacing() == r4.getTextPaint().getLetterSpacing()) goto L21;
            return false;
        L21:
            if (TextUtils.equals(this.mPaint.getFontFeatureSettings(), r4.getTextPaint().getFontFeatureSettings()) == true) goto L24;
            return false;
        L24:
            if (this.mPaint.getFlags() == r4.getTextPaint().getFlags()) goto L27;
            return false;
        L27:
            if (Build.VERSION.SDK_INT < 24) goto L32;
            if (this.mPaint.getTextLocales().equals(r4.getTextPaint().getTextLocales()) == true) goto L35;
            return false;
        L35:
            if (this.mPaint.getTypeface() != null) goto L40;
            if (r4.getTextPaint().getTypeface() == null) goto L42;
            return false;
        L42:
            return true;
        L40:
            if (this.mPaint.getTypeface().equals(r4.getTextPaint().getTypeface()) == true) goto L44;
            return false;
        L44:
            return true;
        L32:
            if (this.mPaint.getTextLocale().equals(r4.getTextPaint().getTextLocale()) == true) goto L35;
            return false;
        }

        public int getBreakStrategy() {
            return this.mBreakStrategy;
        }

        public int getHyphenationFrequency() {
            return this.mHyphenationFrequency;
        }

        public TextDirectionHeuristic getTextDirection() {
            return this.mTextDir;
        }

        public TextPaint getTextPaint() {
            return this.mPaint;
        }

        public int hashCode() {
            if (Build.VERSION.SDK_INT < 24) goto L7;
            return ObjectsCompat.hash(new Object[]{Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), Float.valueOf(this.mPaint.getLetterSpacing()), Integer.valueOf(this.mPaint.getFlags()), this.mPaint.getTextLocales(), this.mPaint.getTypeface(), Boolean.valueOf(this.mPaint.isElegantTextHeight()), this.mTextDir, Integer.valueOf(this.mBreakStrategy), Integer.valueOf(this.mHyphenationFrequency)});
        L7:
            return ObjectsCompat.hash(new Object[]{Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), Float.valueOf(this.mPaint.getLetterSpacing()), Integer.valueOf(this.mPaint.getFlags()), this.mPaint.getTextLocale(), this.mPaint.getTypeface(), Boolean.valueOf(this.mPaint.isElegantTextHeight()), this.mTextDir, Integer.valueOf(this.mBreakStrategy), Integer.valueOf(this.mHyphenationFrequency)});
        }

        public String toString() {
            StringBuilder r0 = new StringBuilder("{");
            r0.append("textSize=" + this.mPaint.getTextSize());
            r0.append(", textScaleX=" + this.mPaint.getTextScaleX());
            r0.append(", textSkewX=" + this.mPaint.getTextSkewX());
            r0.append(", letterSpacing=" + this.mPaint.getLetterSpacing());
            r0.append(", elegantTextHeight=" + this.mPaint.isElegantTextHeight());
            if (Build.VERSION.SDK_INT < 24) goto L5;
            r0.append(", textLocale=" + this.mPaint.getTextLocales());
        L6:
            r0.append(", typeface=" + this.mPaint.getTypeface());
            if (Build.VERSION.SDK_INT < 26) goto L9;
            r0.append(", variationSettings=" + this.mPaint.getFontVariationSettings());
        L9:
            r0.append(", textDir=" + this.mTextDir);
            r0.append(", breakStrategy=" + this.mBreakStrategy);
            r0.append(", hyphenationFrequency=" + this.mHyphenationFrequency);
            r0.append("}");
            return r0.toString();
        L5:
            r0.append(", textLocale=" + this.mPaint.getTextLocale());
            goto L6
        }
    }

    private static class PrecomputedTextFutureTask extends FutureTask<PrecomputedTextCompat> {

        private static class PrecomputedTextCallback implements Callable<PrecomputedTextCompat> {
            private Params mParams;
            private CharSequence mText;

            PrecomputedTextCallback(Params r1, CharSequence r2) {
                this.mParams = r1;
                this.mText = r2;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public PrecomputedTextCompat call() throws Exception {
                return PrecomputedTextCompat.create(this.mText, this.mParams);
            }

            @Override // java.util.concurrent.Callable
            public /* bridge */ /* synthetic */ PrecomputedTextCompat call() throws Exception {
                return call();
            }
        }

        PrecomputedTextFutureTask(Params r2, CharSequence r3) {
            super(new PrecomputedTextCallback(r2, r3));
        }
    }

    static {
        sLock = new Object();
        sExecutor = null;
    }

    private PrecomputedTextCompat(PrecomputedText r4, Params r5) {
        this.mText = r4;
        this.mParams = r5;
        PrecomputedText r0 = null;
        this.mParagraphEnds = null;
        if (Build.VERSION.SDK_INT < 29) goto L5;
        r0 = r4;
    L5:
        this.mWrapped = r0;
    }

    private PrecomputedTextCompat(CharSequence r2, Params r3, int[] r4) {
        this.mText = new SpannableString(r2);
        this.mParams = r3;
        this.mParagraphEnds = r4;
        this.mWrapped = null;
    }

    public static PrecomputedTextCompat create(CharSequence r8, Params r9) {
        Preconditions.checkNotNull(r8);
        Preconditions.checkNotNull(r9);
        TraceCompat.beginSection("PrecomputedText");     // Catch: Throwable -> L24
        if (Build.VERSION.SDK_INT >= 29) goto L6;
    L10:
        ArrayList r0 = new ArrayList();     // Catch: Throwable -> L24
        int r2 = r8.length();     // Catch: Throwable -> L24
        int r3 = 0;
    L11:
        if (r3 >= r2) goto L17;
        int r4 = TextUtils.indexOf(r8, LINE_FEED, r3, r2);     // Catch: Throwable -> L24
        if (r4 >= 0) goto L15;
        int r1 = r2;
    L16:
        r0.add(Integer.valueOf(r1));     // Catch: Throwable -> L24
        r3 = r1;
        goto L11
    L15:
        r1 = r4 + 1;     // Catch: Throwable -> L24
        goto L16
    L17:
        int[] r5 = new int[r0.size()];     // Catch: Throwable -> L24
        int r6 = 0;
    L19:
        if (r6 >= r0.size()) goto L21;
        r5[r6] = ((Integer) r0.get(r6)).intValue();     // Catch: Throwable -> L24
        r6 = r6 + 1;     // Catch: Throwable -> L24
        goto L19
    L21:
        StaticLayout.Builder.obtain(r8, 0, r8.length(), r9.getTextPaint(), Integer.MAX_VALUE).setBreakStrategy(r9.getBreakStrategy()).setHyphenationFrequency(r9.getHyphenationFrequency()).setTextDirection(r9.getTextDirection()).build();     // Catch: Throwable -> L24
        PrecomputedTextCompat r7 = new PrecomputedTextCompat(r8, r9, r5);     // Catch: Throwable -> L24
        TraceCompat.endSection();
        return r7;
    L6:
        if (r9.mWrapped == null) goto L10;
        PrecomputedTextCompat r10 = new PrecomputedTextCompat(PrecomputedText.create(r8, r9.mWrapped), r9);     // Catch: Throwable -> L24
        TraceCompat.endSection();
        return r10;
    L24:
        th = move-exception;
        TraceCompat.endSection();
        throw th;
    }

    public static Future<PrecomputedTextCompat> getTextFuture(CharSequence r3, Params r4, Executor r5) {
        PrecomputedTextFutureTask r0 = new PrecomputedTextFutureTask(r4, r3);
        if (r5 != null) goto L15;
        Object r1 = sLock;
        monitor-enter(r1);
    L12:
        th = move-exception;
        throw th;
    L7:
        if (sExecutor != null) goto L9;
        sExecutor = Executors.newFixedThreadPool(1);     // Catch: Throwable -> L12
    L9:
        r5 = sExecutor;     // Catch: Throwable -> L12
        monitor-exit(r1);     // Catch: Throwable -> L12
    L15:
        r5.execute(r0);
        return r0;
    }

    @Override // java.lang.CharSequence
    public char charAt(int r2) {
        return this.mText.charAt(r2);
    }

    public int getParagraphCount() {
        if (Build.VERSION.SDK_INT < 29) goto L7;
        return this.mWrapped.getParagraphCount();
    L7:
        return this.mParagraphEnds.length;
    }

    public int getParagraphEnd(int r4) {
        Preconditions.checkArgumentInRange(r4, 0, getParagraphCount(), "paraIndex");
        if (Build.VERSION.SDK_INT < 29) goto L7;
        return this.mWrapped.getParagraphEnd(r4);
    L7:
        return this.mParagraphEnds[r4];
    }

    public int getParagraphStart(int r4) {
        Preconditions.checkArgumentInRange(r4, 0, getParagraphCount(), "paraIndex");
        if (Build.VERSION.SDK_INT >= 29) goto L5;
        if (r4 != 0) goto L9;
        return 0;
    L9:
        return this.mParagraphEnds[r4 - 1];
    L5:
        return this.mWrapped.getParagraphStart(r4);
    }

    public Params getParams() {
        return this.mParams;
    }

    public PrecomputedText getPrecomputedText() {
        if ((this.mText instanceof PrecomputedText) == true) goto L5;
        return null;
    L5:
        return (PrecomputedText) this.mText;
    }

    @Override // android.text.Spanned
    public int getSpanEnd(Object r2) {
        return this.mText.getSpanEnd(r2);
    }

    @Override // android.text.Spanned
    public int getSpanFlags(Object r2) {
        return this.mText.getSpanFlags(r2);
    }

    @Override // android.text.Spanned
    public int getSpanStart(Object r2) {
        return this.mText.getSpanStart(r2);
    }

    @Override // android.text.Spanned
    public <T> T[] getSpans(int r3, int r4, Class<T> r5) {
        if (Build.VERSION.SDK_INT < 29) goto L7;
        return (T[]) this.mWrapped.getSpans(r3, r4, r5);
    L7:
        return (T[]) this.mText.getSpans(r3, r4, r5);
    }

    @Override // java.lang.CharSequence
    public int length() {
        return this.mText.length();
    }

    @Override // android.text.Spanned
    public int nextSpanTransition(int r2, int r3, Class r4) {
        return this.mText.nextSpanTransition(r2, r3, r4);
    }

    @Override // android.text.Spannable
    public void removeSpan(Object r3) {
        if ((r3 instanceof MetricAffectingSpan) == true) goto L10;
        if (Build.VERSION.SDK_INT < 29) goto L7;
        this.mWrapped.removeSpan(r3);
        return;
    L7:
        this.mText.removeSpan(r3);
        return;
    L10:
        throw new IllegalArgumentException("MetricAffectingSpan can not be removed from PrecomputedText.");
    }

    @Override // android.text.Spannable
    public void setSpan(Object r3, int r4, int r5, int r6) {
        if ((r3 instanceof MetricAffectingSpan) == true) goto L10;
        if (Build.VERSION.SDK_INT < 29) goto L7;
        this.mWrapped.setSpan(r3, r4, r5, r6);
        return;
    L7:
        this.mText.setSpan(r3, r4, r5, r6);
        return;
    L10:
        throw new IllegalArgumentException("MetricAffectingSpan can not be set to PrecomputedText.");
    }

    @Override // java.lang.CharSequence
    public CharSequence subSequence(int r2, int r3) {
        return this.mText.subSequence(r2, r3);
    }

    @Override // java.lang.CharSequence
    public String toString() {
        return this.mText.toString();
    }
}
