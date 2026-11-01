package androidx.core.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseArray;
import androidx.core.graphics.drawable.IconCompat;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
class NotificationCompatJellybean {
    static final String EXTRA_ALLOW_GENERATED_REPLIES = "android.support.allowGeneratedReplies";
    static final String EXTRA_DATA_ONLY_REMOTE_INPUTS = "android.support.dataRemoteInputs";
    private static final String KEY_ACTION_INTENT = "actionIntent";
    private static final String KEY_ALLOWED_DATA_TYPES = "allowedDataTypes";
    private static final String KEY_ALLOW_FREE_FORM_INPUT = "allowFreeFormInput";
    private static final String KEY_CHOICES = "choices";
    private static final String KEY_DATA_ONLY_REMOTE_INPUTS = "dataOnlyRemoteInputs";
    private static final String KEY_EXTRAS = "extras";
    private static final String KEY_ICON = "icon";
    private static final String KEY_LABEL = "label";
    private static final String KEY_REMOTE_INPUTS = "remoteInputs";
    private static final String KEY_RESULT_KEY = "resultKey";
    private static final String KEY_SEMANTIC_ACTION = "semanticAction";
    private static final String KEY_SHOWS_USER_INTERFACE = "showsUserInterface";
    private static final String KEY_TITLE = "title";
    public static final String TAG = "NotificationCompat";
    private static Field sActionIconField;
    private static Field sActionIntentField;
    private static Field sActionTitleField;
    private static boolean sActionsAccessFailed;
    private static Field sActionsField;
    private static final Object sActionsLock = null;
    private static Field sExtrasField;
    private static boolean sExtrasFieldAccessFailed;
    private static final Object sExtrasLock = null;

    static {
        sExtrasLock = new Object();
        sActionsLock = new Object();
    }

    private NotificationCompatJellybean() {
    }

    public static SparseArray<Bundle> buildActionExtrasMap(List<Bundle> r5) {
        SparseArray<Bundle> r0 = null;
        int r1 = 0;
        int r2 = r5.size();
    L3:
        if (r1 >= r2) goto L10;
        Bundle r3 = r5.get(r1);
        if (r3 == null) goto L9;
        if (r0 != null) goto L8;
        r0 = new SparseArray();
    L8:
        r0.put(r1, r3);
    L9:
        r1 = r1 + 1;
        goto L3
    L10:
        return r0;
    }

    private static boolean ensureActionReflectionReadyLocked() {
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
            java.lang.String r0 = "Unable to access notification actions"
            java.lang.String r1 = "NotificationCompat"
            boolean r2 = androidx.core.app.NotificationCompatJellybean.sActionsAccessFailed
            if (r2 == 0) goto La
            r0 = 0
            return r0
        La:
            r2 = 1
            java.lang.reflect.Field r3 = androidx.core.app.NotificationCompatJellybean.sActionsField     // Catch: java.lang.NoSuchFieldException -> L3e java.lang.ClassNotFoundException -> L45
            if (r3 != 0) goto L4b
            java.lang.String r3 = "android.app.Notification$Action"
            java.lang.Class r3 = java.lang.Class.forName(r3)     // Catch: java.lang.NoSuchFieldException -> L3e java.lang.ClassNotFoundException -> L45
            java.lang.String r4 = "icon"
            java.lang.reflect.Field r4 = r3.getDeclaredField(r4)     // Catch: java.lang.NoSuchFieldException -> L3e java.lang.ClassNotFoundException -> L45
            androidx.core.app.NotificationCompatJellybean.sActionIconField = r4     // Catch: java.lang.NoSuchFieldException -> L3e java.lang.ClassNotFoundException -> L45
            java.lang.String r4 = "title"
            java.lang.reflect.Field r4 = r3.getDeclaredField(r4)     // Catch: java.lang.NoSuchFieldException -> L3e java.lang.ClassNotFoundException -> L45
            androidx.core.app.NotificationCompatJellybean.sActionTitleField = r4     // Catch: java.lang.NoSuchFieldException -> L3e java.lang.ClassNotFoundException -> L45
            java.lang.String r4 = "actionIntent"
            java.lang.reflect.Field r4 = r3.getDeclaredField(r4)     // Catch: java.lang.NoSuchFieldException -> L3e java.lang.ClassNotFoundException -> L45
            androidx.core.app.NotificationCompatJellybean.sActionIntentField = r4     // Catch: java.lang.NoSuchFieldException -> L3e java.lang.ClassNotFoundException -> L45
            java.lang.Class<android.app.Notification> r4 = android.app.Notification.class
            java.lang.String r5 = "actions"
            java.lang.reflect.Field r4 = r4.getDeclaredField(r5)     // Catch: java.lang.NoSuchFieldException -> L3e java.lang.ClassNotFoundException -> L45
            androidx.core.app.NotificationCompatJellybean.sActionsField = r4     // Catch: java.lang.NoSuchFieldException -> L3e java.lang.ClassNotFoundException -> L45
            java.lang.reflect.Field r4 = androidx.core.app.NotificationCompatJellybean.sActionsField     // Catch: java.lang.NoSuchFieldException -> L3e java.lang.ClassNotFoundException -> L45
            r4.setAccessible(r2)     // Catch: java.lang.NoSuchFieldException -> L3e java.lang.ClassNotFoundException -> L45
            goto L4b
        L3e:
            r3 = move-exception
            android.util.Log.e(r1, r0, r3)
            androidx.core.app.NotificationCompatJellybean.sActionsAccessFailed = r2
            goto L4c
        L45:
            r3 = move-exception
            android.util.Log.e(r1, r0, r3)
            androidx.core.app.NotificationCompatJellybean.sActionsAccessFailed = r2
        L4b:
        L4c:
            boolean r0 = androidx.core.app.NotificationCompatJellybean.sActionsAccessFailed
            r0 = r0 ^ r2
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.app.NotificationCompatJellybean.ensureActionReflectionReadyLocked():boolean");
    }

    private static RemoteInput fromBundle(Bundle r10) {
        ArrayList<String> r0 = r10.getStringArrayList(KEY_ALLOWED_DATA_TYPES);
        HashSet r1 = new HashSet();
        if (r0 == null) goto L9;
        Iterator<String> r2 = r0.iterator();
    L6:
        if (r2.hasNext() == false) goto L9;
        r1.add(r2.next());
    L9:
        return new RemoteInput(r10.getString(KEY_RESULT_KEY), r10.getCharSequence(KEY_LABEL), r10.getCharSequenceArray(KEY_CHOICES), r10.getBoolean(KEY_ALLOW_FREE_FORM_INPUT), 0, r10.getBundle(KEY_EXTRAS), r1);
    }

    private static RemoteInput[] fromBundleArray(Bundle[] r3) {
        if (r3 != null) goto L5;
        return null;
    L5:
        RemoteInput[] r0 = new RemoteInput[r3.length];
        int r1 = 0;
    L7:
        if (r1 >= r3.length) goto L9;
        r0[r1] = fromBundle(r3[r1]);
        r1 = r1 + 1;
        goto L7
    L9:
        return r0;
    }

    public static NotificationCompat.Action getAction(Notification r8, int r9) {
        Object r0 = sActionsLock;
        monitor-enter(r0);
        Object[] r1 = getActionObjectsLocked(r8);     // Catch: Throwable -> L15 IllegalAccessException -> L17
        if (r1 == null) goto L19;
        Object r2 = r1[r9];     // Catch: Throwable -> L15 IllegalAccessException -> L17
        Bundle r3 = null;
        Bundle r4 = getExtras(r8);     // Catch: Throwable -> L15 IllegalAccessException -> L17
        if (r4 == null) goto L11;
        SparseArray r5 = r4.getSparseParcelableArray(NotificationCompatExtras.EXTRA_ACTION_EXTRAS);     // Catch: Throwable -> L15 IllegalAccessException -> L17
        if (r5 == null) goto L11;
        r3 = (Bundle) r5.get(r9);     // Catch: Throwable -> L15 IllegalAccessException -> L17
    L11:
        NotificationCompat.Action r6 = readAction(sActionIconField.getInt(r2), (CharSequence) sActionTitleField.get(r2), (PendingIntent) sActionIntentField.get(r2), r3);     // Catch: Throwable -> L15 IllegalAccessException -> L17
        monitor-exit(r0);     // Catch: Throwable -> L15
        return r6;
    L19:
        monitor-exit(r0);     // Catch: Throwable -> L15
        return null;
    L17:
        e = move-exception;
        Log.e(TAG, "Unable to access notification actions", e);     // Catch: Throwable -> L15
        sActionsAccessFailed = true;     // Catch: Throwable -> L15
    L15:
        th = move-exception;
        throw th;
    }

    public static int getActionCount(Notification r3) {
        Object r0 = sActionsLock;
        monitor-enter(r0);
        Object[] r1 = getActionObjectsLocked(r3);     // Catch: Throwable -> L10
        if (r1 == null) goto L7;
        int r2 = r1.length;     // Catch: Throwable -> L10
    L8:
        monitor-exit(r0);     // Catch: Throwable -> L10
        return r2;
    L7:
        r2 = 0;
    L10:
        th = move-exception;
        throw th;
    }

    static NotificationCompat.Action getActionFromBundle(Bundle r15) {
        Bundle r1 = r15.getBundle(KEY_EXTRAS);
        if (r1 == null) goto L5;
        boolean r10 = r1.getBoolean(EXTRA_ALLOW_GENERATED_REPLIES, false);
    L7:
        return new NotificationCompat.Action(r15.getInt(KEY_ICON), r15.getCharSequence(KEY_TITLE), (PendingIntent) r15.getParcelable(KEY_ACTION_INTENT), r15.getBundle(KEY_EXTRAS), fromBundleArray(getBundleArrayFromBundle(r15, KEY_REMOTE_INPUTS)), fromBundleArray(getBundleArrayFromBundle(r15, KEY_DATA_ONLY_REMOTE_INPUTS)), r10, r15.getInt(KEY_SEMANTIC_ACTION), r15.getBoolean(KEY_SHOWS_USER_INTERFACE), false, false);
    L5:
        r10 = false;
        goto L7
    }

    private static Object[] getActionObjectsLocked(Notification r5) {
        Object r0 = sActionsLock;
        monitor-enter(r0);
    L15:
        th = move-exception;
        throw th;
    L5:
        if (ensureActionReflectionReadyLocked() == true) goto L18;
        monitor-exit(r0);     // Catch: Throwable -> L15
        return null;
    L18:
        Object[] r1 = (Object[]) sActionsField.get(r5);     // Catch: IllegalAccessException -> L11 Throwable -> L15
        monitor-exit(r0);     // Catch: Throwable -> L15
        return r1;
    L11:
        e = move-exception;
        Log.e(TAG, "Unable to access notification actions", e);     // Catch: Throwable -> L15
        sActionsAccessFailed = true;     // Catch: Throwable -> L15
        return null;
    }

    private static Bundle[] getBundleArrayFromBundle(Bundle r3, String r4) {
        Parcelable[] r0 = r3.getParcelableArray(r4);
        if ((r0 instanceof Bundle[]) == true) goto L9;
        if (r0 == null) goto L9;
        Bundle[] r1 = (Bundle[]) Arrays.copyOf(r0, r0.length, Bundle[].class);
        r3.putParcelableArray(r4, r1);
        return r1;
    L9:
        return (Bundle[]) r0;
    }

    static Bundle getBundleForAction(NotificationCompat.Action r5) {
        Bundle r0 = new Bundle();
        IconCompat r1 = r5.getIconCompat();
        if (r1 == null) goto L5;
        int r2 = r1.getResId();
    L6:
        r0.putInt(KEY_ICON, r2);
        r0.putCharSequence(KEY_TITLE, r5.getTitle());
        r0.putParcelable(KEY_ACTION_INTENT, r5.getActionIntent());
        if (r5.getExtras() == null) goto L9;
        Bundle r3 = new Bundle(r5.getExtras());
    L10:
        r3.putBoolean(EXTRA_ALLOW_GENERATED_REPLIES, r5.getAllowGeneratedReplies());
        r0.putBundle(KEY_EXTRAS, r3);
        r0.putParcelableArray(KEY_REMOTE_INPUTS, toBundleArray(r5.getRemoteInputs()));
        r0.putBoolean(KEY_SHOWS_USER_INTERFACE, r5.getShowsUserInterface());
        r0.putInt(KEY_SEMANTIC_ACTION, r5.getSemanticAction());
        return r0;
    L9:
        r3 = new Bundle();
        goto L10
    L5:
        r2 = 0;
        goto L6
    }

    public static Bundle getExtras(Notification r6) {
        Object r0 = sExtrasLock;
        monitor-enter(r0);
    L29:
        th = move-exception;
        throw th;
    L5:
        if (sExtrasFieldAccessFailed == false) goto L33;
        monitor-exit(r0);     // Catch: Throwable -> L29
        return null;
    L33:
    L24:
        e = move-exception;
        Log.e(TAG, "Unable to access notification extras", e);     // Catch: Throwable -> L29
    L26:
        sExtrasFieldAccessFailed = true;     // Catch: Throwable -> L29
        monitor-exit(r0);     // Catch: Throwable -> L29
        return null;
    L22:
        e = move-exception;
        Log.e(TAG, "Unable to access notification extras", e);     // Catch: Throwable -> L29
        goto L26
    L10:
        if (sExtrasField != null) goto L17;
        Field r3 = Notification.class.getDeclaredField(KEY_EXTRAS);     // Catch: NoSuchFieldException -> L22 IllegalAccessException -> L24 Throwable -> L29
        if (Bundle.class.isAssignableFrom(r3.getType()) == true) goto L16;
        Log.e(TAG, "Notification.extras field is not of type Bundle");     // Catch: NoSuchFieldException -> L22 IllegalAccessException -> L24 Throwable -> L29
        sExtrasFieldAccessFailed = true;     // Catch: NoSuchFieldException -> L22 IllegalAccessException -> L24 Throwable -> L29
        monitor-exit(r0);     // Catch: Throwable -> L29
        return null;
    L16:
        r3.setAccessible(true);     // Catch: NoSuchFieldException -> L22 IllegalAccessException -> L24 Throwable -> L29
        sExtrasField = r3;     // Catch: NoSuchFieldException -> L22 IllegalAccessException -> L24 Throwable -> L29
    L17:
        Bundle r4 = (Bundle) sExtrasField.get(r6);     // Catch: NoSuchFieldException -> L22 IllegalAccessException -> L24 Throwable -> L29
        if (r4 != null) goto L20;
        r4 = new Bundle();     // Catch: NoSuchFieldException -> L22 IllegalAccessException -> L24 Throwable -> L29
        sExtrasField.set(r6, r4);     // Catch: NoSuchFieldException -> L22 IllegalAccessException -> L24 Throwable -> L29
    L20:
        monitor-exit(r0);     // Catch: Throwable -> L29
        return r4;
    }

    public static NotificationCompat.Action readAction(int r12, CharSequence r13, PendingIntent r14, Bundle r15) {
        if (r15 == null) goto L5;
        RemoteInput[] r0 = fromBundleArray(getBundleArrayFromBundle(r15, NotificationCompatExtras.EXTRA_REMOTE_INPUTS));
        RemoteInput[] r5 = r0;
        RemoteInput[] r6 = fromBundleArray(getBundleArrayFromBundle(r15, EXTRA_DATA_ONLY_REMOTE_INPUTS));
        boolean r7 = r15.getBoolean(EXTRA_ALLOW_GENERATED_REPLIES);
    L7:
        return new NotificationCompat.Action(r12, r13, r14, r15, r5, r6, r7, 0, true, false, false);
    L5:
        r5 = null;
        r6 = null;
        r7 = false;
        goto L7
    }

    private static Bundle toBundle(RemoteInput r5) {
        Bundle r0 = new Bundle();
        r0.putString(KEY_RESULT_KEY, r5.getResultKey());
        r0.putCharSequence(KEY_LABEL, r5.getLabel());
        r0.putCharSequenceArray(KEY_CHOICES, r5.getChoices());
        r0.putBoolean(KEY_ALLOW_FREE_FORM_INPUT, r5.getAllowFreeFormInput());
        r0.putBundle(KEY_EXTRAS, r5.getExtras());
        Set<String> r1 = r5.getAllowedDataTypes();
        if (r1 != null) goto L5;
    L11:
        return r0;
    L5:
        if (r1.isEmpty() == true) goto L11;
        ArrayList<String> r2 = new ArrayList(r1.size());
        Iterator<String> r3 = r1.iterator();
    L8:
        if (r3.hasNext() == false) goto L10;
        r2.add(r3.next());
        goto L8
    L10:
        r0.putStringArrayList(KEY_ALLOWED_DATA_TYPES, r2);
        goto L11
    }

    private static Bundle[] toBundleArray(RemoteInput[] r3) {
        if (r3 != null) goto L5;
        return null;
    L5:
        Bundle[] r0 = new Bundle[r3.length];
        int r1 = 0;
    L7:
        if (r1 >= r3.length) goto L9;
        r0[r1] = toBundle(r3[r1]);
        r1 = r1 + 1;
        goto L7
    L9:
        return r0;
    }

    public static Bundle writeActionAndGetExtras(Notification.Builder r4, NotificationCompat.Action r5) {
        IconCompat r0 = r5.getIconCompat();
        if (r0 == null) goto L5;
        int r1 = r0.getResId();
    L6:
        r4.addAction(r1, r5.getTitle(), r5.getActionIntent());
        Bundle r2 = new Bundle(r5.getExtras());
        if (r5.getRemoteInputs() == null) goto L10;
        r2.putParcelableArray(NotificationCompatExtras.EXTRA_REMOTE_INPUTS, toBundleArray(r5.getRemoteInputs()));
    L10:
        if (r5.getDataOnlyRemoteInputs() == null) goto L12;
        r2.putParcelableArray(EXTRA_DATA_ONLY_REMOTE_INPUTS, toBundleArray(r5.getDataOnlyRemoteInputs()));
    L12:
        r2.putBoolean(EXTRA_ALLOW_GENERATED_REPLIES, r5.getAllowGeneratedReplies());
        return r2;
    L5:
        r1 = 0;
        goto L6
    }
}
