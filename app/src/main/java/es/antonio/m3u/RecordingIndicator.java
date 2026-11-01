package es.antonio.m3u;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.internal.view.SupportMenu;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes7.dex */
final class RecordingIndicator {
    private static final Map<Activity, WeakReference<Badge>> badges = null;
    private static volatile Set<String> identities;

    /* JADX INFO: renamed from: io */
    private static final ExecutorService f187io = null;
    private static long lastRead;
    private static volatile boolean reading;
    private static volatile String title;

    private static final class Badge extends LinearLayout implements Runnable {
        final WeakReference<Activity> activity;
        final TextView name;

        Badge(Activity r4) {
            super(r4);
            this.activity = new WeakReference(r4);
            setOrientation(0);
            setPadding(10, 5, 10, 5);
            setBackgroundColor(-585755114);
            TextView r0 = new TextView(r4);
            r0.setTag("keepColor");
            r0.setText("● ");
            r0.setTextColor(SupportMenu.CATEGORY_MASK);
            r0.setTextSize(17.0f);
            addView(r0);
            this.name = new TextView(r4);
            this.name.setTag("keepColor");
            this.name.setTextColor(-1);
            this.name.setTextSize(13.0f);
            this.name.setMaxLines(1);
            this.name.setMaxWidth((int) (getResources().getDisplayMetrics().density * 240.0f));
            addView(this.name);
            setVisibility(8);
            setOnClickListener(new RecordingIndicator$Badge$$ExternalSyntheticLambda0(this));
        }

        /* JADX INFO: renamed from: lambda$0$es-antonio-m3u-RecordingIndicator$Badge, reason: not valid java name */
        /* synthetic */ void m736lambda$0$esantoniom3uRecordingIndicator$Badge(View r3) {
            Activity r4 = this.activity.get();
            if (r4 == null) goto L6;
            r4.startActivity(new Intent(r4, RecordingsActivity.class));
            return;
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            removeCallbacks(this);
            super.onDetachedFromWindow();
        }

        void resume() {
            removeCallbacks(this);
            post(this);
        }

        @Override // java.lang.Runnable
        public void run() {
            Activity r0 = this.activity.get();
            if (r0 != null) goto L5;
            return;
        L5:
            if (r0.isFinishing() == false) goto L7;
            return;
        L7:
            if (r0.isDestroyed() == false) goto L9;
            return;
        L9:
            if (isAttachedToWindow() == true) goto L12;
            return;
        L12:
            if (r0.hasWindowFocus() == true) goto L15;
            postDelayed(this, 1000);
            return;
        L15:
            RecordingIndicator.access$0(r0.getApplicationContext());
            if ((r0 instanceof EpgActivity) == false) goto L18;
            ((EpgActivity) r0).refreshRecordingIndicator();
        L18:
            this.name.setText("Grabando · " + RecordingIndicator.access$1());
            setContentDescription("Grabación en curso: " + RecordingIndicator.access$1());
            if (RecordingIndicator.access$1().isEmpty() == false) goto L21;
            int r1 = 8;
        L22:
            setVisibility(r1);
            postDelayed(this, 1000);
            return;
        L21:
            r1 = 0;
            goto L22
        }
    }

    static {
        f187io = Executors.newSingleThreadExecutor(new RecordingIndicator$$ExternalSyntheticLambda2());
        badges = new WeakHashMap();
        identities = Collections.emptySet();
        title = "";
    }

    RecordingIndicator() {
    }

    static /* synthetic */ void access$0(Context r0) {
        refresh(r0);
    }

    static /* synthetic */ String access$1() {
        return title;
    }

    static void attach(Activity r4) {
        if (r4.isFinishing() == false) goto L5;
        return;
    L5:
        if (r4.isDestroyed() == true) goto L22;
        WeakReference<Badge> r0 = badges.get(r4);
        if (r0 != null) goto L10;
        Badge r1 = null;
    L11:
        if (r1 != null) goto L13;
    L16:
        View r2 = r4.findViewById(android.R.id.content);
        if ((r2 instanceof FrameLayout) == true) goto L19;
        return;
    L19:
        Badge r3 = new Badge(r4);
        badges.put(r4, new WeakReference(r3));
        FrameLayout.LayoutParams r5 = new FrameLayout.LayoutParams(-2, -2, 8388661);
        r5.topMargin = 8;
        r5.rightMargin = 8;
        ((FrameLayout) r2).addView(r3, r5);
        r3.resume();
        return;
    L13:
        if (r1.isAttachedToWindow() == false) goto L16;
        r1.resume();
        return;
    L10:
        r1 = r0.get();
        goto L11
    }

    private static String key(Entry r4) {
        String r0 = Entry.details(r4).url;
        URI r1 = new URI(r0);     // Catch: Exception -> L5
        r0 = String.valueOf(r1.getScheme()) + "://" + r1.getRawAuthority() + r1.getRawPath();     // Catch: Exception -> L5
    L7:
        return String.valueOf(r4.identity()) + "\n" + r0;
    }

    static /* synthetic */ Thread lambda$0(final Runnable r2) {
        Thread r0 = new Thread(new RecordingIndicator$$ExternalSyntheticLambda1(r2), "CHDTVRecordingBadge");
        r0.setDaemon(true);
        return r0;
    }

    static /* synthetic */ void lambda$1(Runnable r0) {
        BackgroundWork.lowPriority();
        r0.run();
    }

    static /* synthetic */ void lambda$2(Context r5) {
        HashSet r1 = new HashSet();     // Catch: Throwable -> L17 Exception -> L20
        StringBuilder r2 = new StringBuilder();     // Catch: Throwable -> L17 Exception -> L20
        Iterator<RecordingTask> r6 = RecordingStore.all(r5).iterator();     // Catch: Throwable -> L17 Exception -> L20
    L5:
        if (r6.hasNext() == false) goto L6;
        RecordingTask r3 = r6.next();     // Catch: Throwable -> L17 Exception -> L20
        if (r3.active() == false) goto L5;
        if (r3.entry == null) goto L13;
        r1.add(key(r3.entry));     // Catch: Throwable -> L17 Exception -> L20
    L13:
        if (r2.length() <= 0) goto L15;
        r2.append(" / ");     // Catch: Throwable -> L17 Exception -> L20
    L15:
        r2.append(r3.title);     // Catch: Throwable -> L17 Exception -> L20
        goto L5
    L6:
        identities = Collections.unmodifiableSet(r1);     // Catch: Throwable -> L17 Exception -> L20
        title = r2.toString();     // Catch: Throwable -> L17 Exception -> L20
    L17:
        th = move-exception;
        lastRead = SystemClock.elapsedRealtime();
        reading = false;
        throw th;
    L21:
        lastRead = SystemClock.elapsedRealtime();
        reading = false;
    }

    static boolean recording(Entry r1) {
        if (identities.isEmpty() == true) goto L9;
        if (r1 != null) goto L6;
        return false;
    L6:
        if (identities.contains(key(r1)) == false) goto L12;
        return true;
    L12:
        return false;
    L9:
        return false;
    }

    private static void refresh(final Context r4) {
        if (reading == false) goto L5;
        return;
    L5:
        if ((SystemClock.elapsedRealtime() - lastRead) < 900) goto L10;
        reading = true;
        f187io.execute(new RecordingIndicator$$ExternalSyntheticLambda0(r4));
        return;
    }
}
