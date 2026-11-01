package androidx.core.text;

import android.icu.util.ULocale;
import android.os.Build;
import android.util.Log;
import java.lang.reflect.Method;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public final class ICUCompat {
    private static final String TAG = "ICUCompat";
    private static Method sAddLikelySubtagsMethod;
    private static Method sGetScriptMethod;

    static class Api21Impl {
        private Api21Impl() {
        }

        static String getScript(Locale r1) {
            return r1.getScript();
        }
    }

    static class Api24Impl {
        private Api24Impl() {
        }

        static ULocale addLikelySubtags(Object r1) {
            return ULocale.addLikelySubtags((ULocale) r1);
        }

        static ULocale forLocale(Locale r1) {
            return ULocale.forLocale(r1);
        }

        static String getScript(Object r1) {
            return ((ULocale) r1).getScript();
        }
    }

    static {
        if (Build.VERSION.SDK_INT < 24) goto L10;
        return;
    L10:
        sAddLikelySubtagsMethod = Class.forName("libcore.icu.ICU").getMethod("addLikelySubtags", new Class[]{Locale.class});     // Catch: Exception -> L6
        return;
    L6:
        e = move-exception;
        throw new IllegalStateException(e);
    }

    private ICUCompat() {
    }

    private static String addLikelySubtagsBelowApi21(Locale r5) {
        /*  JADX ERROR: Simple mode code generation failed
            java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0
            	at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
            	at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
            	at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:266)
            	at java.base/java.util.Objects.checkIndex(Objects.java:361)
            	at java.base/java.util.ArrayList.get(ArrayList.java:427)
            	at jadx.core.codegen.MethodGen.generateSimpleCode(MethodGen.java:354)
            	at jadx.core.codegen.MethodGen.addSimpleMethodCode(MethodGen.java:316)
            	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:279)
            	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:420)
            	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:345)
            	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$3(ClassGen.java:299)
            	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
            	at java.base/java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
            	at java.base/java.util.stream.Sink$ChainedReference.end(Sink.java:258)
            */
        /*
            java.lang.String r0 = "ICUCompat"
            java.lang.String r1 = r5.toString()
            java.lang.reflect.Method r2 = androidx.core.text.ICUCompat.sAddLikelySubtagsMethod     // Catch: java.lang.reflect.InvocationTargetException -> L1a java.lang.IllegalAccessException -> L1f
            if (r2 == 0) goto L23
            r2 = 1
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch: java.lang.reflect.InvocationTargetException -> L1a java.lang.IllegalAccessException -> L1f
            r3 = 0
            r2[r3] = r1     // Catch: java.lang.reflect.InvocationTargetException -> L1a java.lang.IllegalAccessException -> L1f
            java.lang.reflect.Method r3 = androidx.core.text.ICUCompat.sAddLikelySubtagsMethod     // Catch: java.lang.reflect.InvocationTargetException -> L1a java.lang.IllegalAccessException -> L1f
            r4 = 0
            java.lang.Object r3 = r3.invoke(r4, r2)     // Catch: java.lang.reflect.InvocationTargetException -> L1a java.lang.IllegalAccessException -> L1f
            java.lang.String r3 = (java.lang.String) r3     // Catch: java.lang.reflect.InvocationTargetException -> L1a java.lang.IllegalAccessException -> L1f
            return r3
        L1a:
            r2 = move-exception
            android.util.Log.w(r0, r2)
            goto L24
        L1f:
            r2 = move-exception
            android.util.Log.w(r0, r2)
        L23:
        L24:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.text.ICUCompat.addLikelySubtagsBelowApi21(java.util.Locale):java.lang.String");
    }

    private static String getScriptBelowApi21(String r4) {
        /*  JADX ERROR: Simple mode code generation failed
            java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0
            	at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
            	at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
            	at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:266)
            	at java.base/java.util.Objects.checkIndex(Objects.java:361)
            	at java.base/java.util.ArrayList.get(ArrayList.java:427)
            	at jadx.core.codegen.MethodGen.generateSimpleCode(MethodGen.java:354)
            	at jadx.core.codegen.MethodGen.addSimpleMethodCode(MethodGen.java:316)
            	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:279)
            	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:420)
            	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:345)
            	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$3(ClassGen.java:299)
            	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
            	at java.base/java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
            	at java.base/java.util.stream.Sink$ChainedReference.end(Sink.java:258)
            */
        /*
            java.lang.String r0 = "ICUCompat"
            r1 = 0
            java.lang.reflect.Method r2 = androidx.core.text.ICUCompat.sGetScriptMethod     // Catch: java.lang.reflect.InvocationTargetException -> L16 java.lang.IllegalAccessException -> L1b
            if (r2 == 0) goto L1f
            r2 = 1
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch: java.lang.reflect.InvocationTargetException -> L16 java.lang.IllegalAccessException -> L1b
            r3 = 0
            r2[r3] = r4     // Catch: java.lang.reflect.InvocationTargetException -> L16 java.lang.IllegalAccessException -> L1b
            java.lang.reflect.Method r3 = androidx.core.text.ICUCompat.sGetScriptMethod     // Catch: java.lang.reflect.InvocationTargetException -> L16 java.lang.IllegalAccessException -> L1b
            java.lang.Object r3 = r3.invoke(r1, r2)     // Catch: java.lang.reflect.InvocationTargetException -> L16 java.lang.IllegalAccessException -> L1b
            java.lang.String r3 = (java.lang.String) r3     // Catch: java.lang.reflect.InvocationTargetException -> L16 java.lang.IllegalAccessException -> L1b
            return r3
        L16:
            r2 = move-exception
            android.util.Log.w(r0, r2)
            goto L20
        L1b:
            r2 = move-exception
            android.util.Log.w(r0, r2)
        L1f:
        L20:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.text.ICUCompat.getScriptBelowApi21(java.lang.String):java.lang.String");
    }

    public static String maximizeAndGetScript(Locale r4) {
        if (Build.VERSION.SDK_INT < 24) goto L17;
        return Api24Impl.getScript(Api24Impl.addLikelySubtags(Api24Impl.forLocale(r4)));
    L17:
        return Api21Impl.getScript((Locale) sAddLikelySubtagsMethod.invoke(null, new Object[]{r4}));
    L9:
        e = move-exception;
        Log.w(TAG, e);
    L16:
        return Api21Impl.getScript(r4);
    L11:
        e = move-exception;
        Log.w(TAG, e);
        goto L16
    }
}
