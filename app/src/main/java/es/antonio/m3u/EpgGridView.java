package es.antonio.m3u;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.LruCache;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes7.dex */
public final class EpgGridView extends View {
    private static final ExecutorService artworkWorker = null;
    private Activation activation;
    private Map<Entry, List<String>> artwork;
    private final List<Entry> channels;
    private final SimpleDateFormat clock;
    private final SimpleDateFormat day;
    private final float density;
    private float downX;
    private float downY;
    private boolean dragging;
    private final Date drawDate;
    private final float headerHeight;
    private float lastX;
    private float lastY;
    private Selection listener;
    private volatile int logoTicket;
    private final LruCache<String, ImageView> logoViews;
    private LogoLoader logos;
    private float offsetY;
    private long origin;
    private boolean overlay;
    private final Paint paint;
    private float pixelsPerMinute;
    private final float rowHeight;
    private final List<List<EpgData.Programme>> schedules;
    private EpgData.Programme selected;
    private long selectedInstant;
    private int selectedRow;
    private long tappedAt;
    private int tappedRow;

    interface Activation {
        void play(Entry r1);
    }

    interface Selection {
        void changed(Entry r1, EpgData.Programme r2);
    }

    static {
        artworkWorker = Executors.newSingleThreadExecutor(new EpgGridView$$ExternalSyntheticLambda2());
    }

    public EpgGridView(Context r4) {
        super(r4);
        this.tappedRow = -1;
        this.paint = new Paint(1);
        this.clock = new SimpleDateFormat("HH:mm", Locale.getDefault());
        this.day = new SimpleDateFormat("EEE d MMM", Locale.getDefault());
        this.drawDate = new Date();
        this.channels = new ArrayList();
        this.schedules = new ArrayList();
        this.artwork = Collections.emptyMap();
        this.logoViews = new LruCache(40);
        this.origin = roundedNow();
        this.selectedInstant = System.currentTimeMillis();
        this.density = getResources().getDisplayMetrics().density;
        this.rowHeight = this.density * 48.0f;
        this.headerHeight = this.density * 30.0f;
        this.pixelsPerMinute = this.density * 3.0f;
        setFocusable(true);
        setFocusableInTouchMode(true);
        setContentDescription("Parrilla EPG. Flechas para navegar; usa Ver canal para reproducir.");
    }

    private float channelWidth() {
        return Math.min(this.density * 180.0f, getWidth() * 0.26f);
    }

    private void clampScroll() {
        this.offsetY = Math.max(0.0f, Math.min(this.offsetY, Math.max(0.0f, (this.channels.size() * this.rowHeight) - (getHeight() - this.headerHeight))));
    }

    private void ensureVisible() {
        float r0 = Math.max(0.0f, getHeight() - this.headerHeight);
        if (r0 > 0.0f) goto L6;
        this.offsetY = 0.0f;
        return;
    L6:
        float r1 = this.selectedRow * this.rowHeight;
        if (r1 >= this.offsetY) goto L10;
    L8:
        this.offsetY = r1;
    L12:
        clampScroll();
        return;
    L10:
        if ((this.rowHeight + r1) <= (this.offsetY + r0)) goto L12;
        r1 = (r1 + this.rowHeight) - r0;
        goto L8
    }

    static int firstEndingAfter(List<EpgData.Programme> r6, long r7) {
        int r0 = r6.size();
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L4;
        int r3 = (r2 + r0) >>> 1;
        if (r6.get(r3).start <= r7) goto L14;
        r0 = r3;
        goto L3
    L14:
        r2 = r3 + 1;
        goto L3
    L4:
        int r1 = Math.max(0, r2 - 1);
    L6:
        if (r1 >= r6.size()) goto L11;
        if (r6.get(r1).stop > r7) goto L11;
        r1 = r1 + 1;
    L11:
        return r1;
    }

    private String format(SimpleDateFormat r2, long r3) {
        this.drawDate.setTime(r3);
        return r2.format(this.drawDate);
    }

    private void label(Canvas r8, String r9, float r10, float r11, float r12, float r13, int r14) {
        if (r12 > (this.density * 4.0f)) goto L5;
        return;
    L5:
        this.paint.setTextSize(this.density * r13);
        this.paint.setColor(r14);
        if (r9 != null) goto L8;
        String r15 = "";
    L10:
        if (this.paint.breakText(r15, true, r12 - (this.density * 8.0f), null) >= r15.length()) goto L12;
        r15 = String.valueOf(r15.substring(0, this.paint.breakText(r15, true, Math.max(0.0f, (r12 - (this.density * 8.0f)) - this.paint.measureText("…")), null))) + "…";
    L12:
        int r16 = r8.save();
        r8.clipRect(r10, r11, r12 + r10, this.rowHeight + r11);
        r8.drawText(r15, r10 + (this.density * 4.0f), (r11 + (r13 * this.density)) + (this.density * 8.0f), this.paint);
        r8.restoreToCount(r16);
        return;
    L8:
        r15 = r9.replace('\n', ' ');
        goto L10
    }

    static /* synthetic */ Thread lambda$0(Runnable r2) {
        Thread r0 = new Thread(r2, "CHDTVEpgLogos");
        r0.setDaemon(true);
        return r0;
    }

    private void loadLogos() {
        final int r0 = this.logoTicket + 1;
        this.logoTicket = r0;
        final ArrayList r1 = new ArrayList(this.channels);
        final Context r2 = getContext().getApplicationContext();
        artworkWorker.execute(new EpgGridView$$ExternalSyntheticLambda0(this, r0, r2, r1));
    }

    private void logo(Canvas r8, Entry r9, float r10) {
        if (this.logos != null) goto L5;
        this.logos = new LogoLoader(getContext());
    L5:
        List<String> r0 = logoSources(r9);
        String r11 = String.valueOf(r9.identity()) + "\n" + r0;
        ImageView r1 = this.logoViews.get(r11);
        if (r1 != null) goto L8;
        final Context r2 = getContext();
        r1 = new C09941(this, r2);
        this.logoViews.put(r11, r1);
    L8:
        this.logos.show(r1, r0);
        Drawable r12 = r1.getDrawable();
        if (r12 == null) goto L23;
        int r3 = r8.save();
        r8.translate(this.density * 4.0f, r10 + (this.density * 8.0f));
        float r13 = this.density * 32.0f;
        float r4 = this.density * 32.0f;
        int r5 = r12.getIntrinsicWidth();
        int r6 = r12.getIntrinsicHeight();
        if (r5 <= 0) goto L14;
        if (r6 <= 0) goto L14;
        float r7 = Math.min(r13 / r5, r4 / r6);
    L15:
        if (r5 <= 0) goto L17;
        int r14 = (int) (r5 * r7);
    L18:
        if (r6 <= 0) goto L20;
        int r15 = (int) (r6 * r7);
    L21:
        float r16 = r14;
        float r17 = r15;
        r12.setBounds((int) ((r13 - r16) / 2.0f), (int) ((r4 - r17) / 2.0f), (int) ((r13 + r16) / 2.0f), (int) ((r4 + r17) / 2.0f));
        r12.draw(r8);
        r8.restoreToCount(r3);
        return;
    L20:
        r15 = (int) r4;
        goto L21
    L17:
        r14 = (int) r13;
    L14:
        r7 = 1.0f;
        goto L15
    }

    private List<String> logoSources(Entry r2) {
        List<String> r0 = this.artwork.get(r2);
        if (r0 == null) goto L5;
        return r0;
    L5:
        if (r2.logo != null) goto L7;
        String r3 = "";
    L9:
        return Collections.singletonList(r3);
    L7:
        r3 = r2.logo;
        goto L9
    }

    private void notifySelection() {
        if (this.listener == null) goto L5;
        this.listener.changed(selectedChannel(), this.selected);
    L5:
        Entry r0 = selectedChannel();
        if (r0 != null) goto L8;
        String r1 = "No hay canales";
    L17:
        setContentDescription(r1);
        return;
    L8:
        StringBuilder r2 = new StringBuilder(String.valueOf(r0.title)).append(". ");
        if (this.selected != null) goto L11;
        String r3 = "Sin programación";
    L12:
        StringBuilder r4 = r2.append(r3);
        if (this.activation != null) goto L15;
        String r5 = "";
    L16:
        r1 = r4.append(r5).toString();
        goto L17
    L15:
        r5 = ". Pulsa dos veces para ver el canal.";
        goto L16
    L11:
        r3 = String.valueOf(this.selected.title) + ", " + this.clock.format(new Date(this.selected.start));
        goto L12
    }

    private void rect(Canvas r19, float r20, float r21, float r22, float r23, int r24) {
        if (r22 > r20) goto L5;
        return;
    L5:
        if (r23 > r21) goto L8;
        return;
    L8:
        if (this.overlay == false) goto L10;
        int r1 = 110;
    L11:
        int r2 = r1 << 24;
        this.paint.setColor(r2 | (r24 & ViewCompat.MEASURED_SIZE_MASK));
        r19.drawRoundRect(r20, r21, r22, r23, this.density * 4.0f, this.density * 4.0f, this.paint);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(this.density * 0.5f);
        this.paint.setColor(AppTheme.color(getContext(), 609145582));
        r19.drawRoundRect(r20 + (this.density * 0.25f), r21 + (this.density * 0.25f), r22 - (this.density * 0.25f), r23 - (this.density * 0.25f), this.density * 4.0f, this.density * 4.0f, this.paint);
        this.paint.setStyle(Paint.Style.FILL);
        return;
    L10:
        r1 = 150;
        goto L11
    }

    private void resetActivation() {
        this.tappedRow = -1;
    }

    private static long roundedNow() {
        long r0 = System.currentTimeMillis();
        return r0 - (r0 % 1800000);
    }

    private void selectAt(long r4) {
        this.selectedInstant = r4;
        this.selected = null;
        if (this.channels.isEmpty() == true) goto L9;
        List<EpgData.Programme> r0 = this.schedules.get(this.selectedRow);
        int r1 = firstEndingAfter(r0, r4);
        if (r1 >= r0.size()) goto L9;
        EpgData.Programme r2 = r0.get(r1);
        if (r2.start > r4) goto L9;
        this.selected = r2;
    L9:
        notifySelection();
    }

    private long time(float r5) {
        return this.origin + ((long) (((r5 - channelWidth()) / this.pixelsPerMinute) * 60000.0f));
    }

    /* JADX INFO: renamed from: x */
    private float m400x(long r4) {
        return channelWidth() + (((r4 - this.origin) / 60000.0f) * this.pixelsPerMinute);
    }

    /* JADX INFO: renamed from: lambda$1$es-antonio-m3u-EpgGridView, reason: not valid java name */
    /* synthetic */ void m579lambda$1$esantoniom3uEpgGridView(final int r4, Context r5, List r6) {
        if (r4 == this.logoTicket) goto L17;
        return;
    L17:
        ChannelLogos r7 = AppStorage.indexFor(new AppStorage(r5).catalog()).logos;     // Catch: Exception -> L14
        final IdentityHashMap r0 = new IdentityHashMap();     // Catch: Exception -> L14
        Iterator r8 = r6.iterator();     // Catch: Exception -> L14
    L7:
        if (r8.hasNext() == false) goto L8;
        Entry r1 = (Entry) r8.next();     // Catch: Exception -> L14
        if (r4 != this.logoTicket) goto L11;
        r0.put(r1, r7.candidates(r1));     // Catch: Exception -> L14
        goto L7
    L11:
        return;
    L8:
        post(new EpgGridView$$ExternalSyntheticLambda3(this, r4, r0));     // Catch: Exception -> L14
        return;
    L14:
        e = move-exception;
        BlackBox.failure("EPG_LOGOS", e);
    }

    /* JADX INFO: renamed from: lambda$2$es-antonio-m3u-EpgGridView, reason: not valid java name */
    /* synthetic */ void m580lambda$2$esantoniom3uEpgGridView(int r2, Map r3) {
        if (r2 == this.logoTicket) goto L5;
        return;
    L5:
        this.artwork = r3;
        this.logoViews.evictAll();
        invalidate();
    }

    /* JADX INFO: renamed from: lambda$3$es-antonio-m3u-EpgGridView, reason: not valid java name */
    /* synthetic */ void m581lambda$3$esantoniom3uEpgGridView() {
        ensureVisible();
        invalidate();
    }

    void now() {
        resetActivation();
        this.origin = roundedNow();
        selectAt(System.currentTimeMillis());
        invalidate();
    }

    @Override // android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.channels.isEmpty() == true) goto L6;
        loadLogos();
        return;
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        this.logoTicket++;
        this.artwork = Collections.emptyMap();
        if (this.logos == null) goto L5;
        this.logos.close();
        this.logos = null;
    L5:
        this.logoViews.evictAll();
        super.onDetachedFromWindow();
    }

    @Override // android.view.View
    protected void onDraw(Canvas r36) {
        super.onDraw(r36);
        if (this.overlay == false) goto L5;
        Context r0 = getContext();
        int r1 = 805837098;
    L6:
        r36.drawColor(AppTheme.color(r0, r1));
        float r10 = channelWidth();
        rect(r36, 0.0f, 0.0f, getWidth(), this.headerHeight, AppTheme.color(getContext(), -15583409));
        label(r36, format(this.day, this.origin), this.density * 4.0f, 0.0f, r10 - (this.density * 4.0f), 12.0f, AppTheme.color(getContext(), -7681572));
        long r12 = 1800000;
        long r14 = this.origin - (this.origin % 1800000);
    L8:
        if (m400x(r14) >= getWidth()) goto L10;
        long r18 = r12;
        float r3 = m400x(r14);
        if (r3 < r10) goto L75;
        label(r36, format(this.clock, r14), r3, 0.0f, this.density * 90.0f, 12.0f, AppTheme.color(getContext(), -6432793));
    L75:
        r14 = r14 + r18;
        r12 = r18;
        goto L8
    L10:
        if (this.channels.isEmpty() == false) goto L13;
        label(r36, "No hay canales en este filtro", r10, this.headerHeight, getWidth() - r10, 14.0f, -1);
        return;
    L13:
        int r2 = Math.max(0, (int) (this.offsetY / this.rowHeight));
        int r13 = Math.min(this.channels.size(), (int) Math.ceil(((this.offsetY + getHeight()) - this.headerHeight) / this.rowHeight));
        long r16 = time(getWidth());
        int r15 = r36.save();
        r36.clipRect(0.0f, this.headerHeight, getWidth(), getHeight());
        int r17 = r2;
    L14:
        if (r17 >= r13) goto L15;
        float r7 = (this.headerHeight + (r17 * this.rowHeight)) - this.offsetY;
        Entry r6 = this.channels.get(r17);
        float r4 = r10 - (this.density * 2.0f);
        float r5 = (this.rowHeight + r7) - (this.density * 2.0f);
        if (r17 != this.selectedRow) goto L25;
        Context r8 = getContext();
        int r9 = -15445390;
    L26:
        rect(r36, 0.0f, r7, r4, r5, AppTheme.color(r8, r9));
        logo(r36, r6, r7);
        if (RecordingIndicator.recording(r6) == false) goto L29;
        this.paint.setColor(SupportMenu.CATEGORY_MASK);
        r36.drawCircle(this.density * 29.0f, (this.density * 12.0f) + r7, this.density * 5.0f, this.paint);
    L29:
        int r27 = r17 + 1;
        String r11 = String.valueOf(r27) + "  " + r6.title;
        float r19 = this.density * 40.0f;
        float r20 = r10 - (this.density * 42.0f);
        if (r17 != this.selectedRow) goto L32;
        int r21 = AppTheme.color(getContext(), -11148545);
    L33:
        int r28 = r13;
        label(r36, r11, r19, r7, r20, 14.0f, r21);
        rect(r36, r10, r7, getWidth(), (r7 + this.rowHeight) - (this.density * 2.0f), AppTheme.color(getContext(), -15718340));
        List<EpgData.Programme> r110 = this.schedules.get(r17);
        int r22 = r36.save();
        r36.clipRect(r10, r7, getWidth(), (this.rowHeight + r7) - (this.density * 2.0f));
        int r23 = firstEndingAfter(r110, this.origin);
        boolean r24 = false;
    L35:
        if (r23 >= r110.size()) goto L40;
        EpgData.Programme r25 = r110.get(r23);
        if (r25.start >= r16) goto L40;
        int r29 = r22;
        if (r25.stop <= this.origin) goto L71;
        float r30 = Math.max(r10, m400x(r25.start));
        float r31 = Math.min(getWidth(), m400x(r25.stop));
        if (r17 == this.selectedRow) goto L50;
    L52:
        boolean r111 = false;
    L54:
        if (r25.start <= System.currentTimeMillis()) goto L56;
    L58:
        boolean r26 = false;
    L59:
        float r32 = r31 - (this.density * 2.0f);
        float r33 = (r7 + this.rowHeight) - (this.density * 2.0f);
        if (r111 == false) goto L63;
        Context r34 = getContext();
        int r35 = -16220500;
    L66:
        rect(r36, r30, r7, r32, r33, AppTheme.color(r34, r35));
        float r37 = r31 - r30;
        label(r36, r25.title, r30, r7, r37 - (this.density * 2.0f), 14.0f, -1);
        if (r111 == false) goto L70;
        this.paint.setShader(new LinearGradient(r30, r7, r31, r7 + this.rowHeight, AppTheme.color(getContext(), -2144683816), AppTheme.color(getContext(), 268992172), Shader.TileMode.CLAMP));
        r36.drawRoundRect(r30 + this.density, r7 + this.density, r31 - (this.density * 3.0f), (r7 + this.rowHeight) - (this.density * 3.0f), this.density * 4.0f, this.density * 4.0f, this.paint);
        this.paint.setShader(null);
        label(r36, r25.title, r30, r7, r37 - (this.density * 2.0f), 14.0f, -1);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(this.density * 2.0f);
        this.paint.setColor(AppTheme.color(getContext(), -11281409));
        r36.drawRoundRect(r30 + this.density, r7 + this.density, r31 - (this.density * 3.0f), (r7 + this.rowHeight) - (this.density * 3.0f), this.density * 4.0f, this.density * 4.0f, this.paint);
        this.paint.setStyle(Paint.Style.FILL);
    L70:
        r24 = true;
        goto L71
    L63:
        if (r26 == false) goto L65;
        r34 = getContext();
        r35 = -14596002;
        goto L66
    L65:
        r34 = getContext();
        r35 = -14337716;
        goto L66
    L56:
        if (r25.stop <= System.currentTimeMillis()) goto L58;
        r26 = true;
        goto L59
    L50:
        if (r25 != this.selected) goto L52;
        r111 = true;
    L71:
        r23 = r23 + 1;
        r22 = r29;
    L40:
        if (r24 == true) goto L43;
        label(r36, "Sin programación disponible", r10, r7, getWidth() - r10, 13.0f, AppTheme.color(getContext(), -7559494));
    L43:
        r36.restoreToCount(r22);
        r17 = r27;
        r13 = r28;
        goto L14
    L32:
        r21 = -1;
        goto L33
    L25:
        r8 = getContext();
        r9 = -15717824;
        goto L26
    L15:
        float r38 = m400x(System.currentTimeMillis());
        if (r38 >= r10) goto L18;
    L20:
        r36.restoreToCount(r15);
        return;
    L18:
        if (r38 >= getWidth()) goto L20;
        this.paint.setColor(AppTheme.color(getContext(), -12460303));
        this.paint.setStrokeWidth(this.density);
        r36.drawLine(r38, this.headerHeight, r38, getHeight(), this.paint);
        goto L20
    L5:
        r0 = getContext();
        r1 = 268966186;
        goto L6
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int r7, KeyEvent r8) {
        if (this.channels.isEmpty() == false) goto L7;
        return super.onKeyDown(r7, r8);
    L7:
        if (this.selected != null) goto L9;
        long r0 = this.selectedInstant;
    L10:
        int r4 = -1;
        if (r7 == 19) goto L54;
        if (r7 == 20) goto L54;
        if (r7 == 21) goto L33;
        if (r7 == 22) goto L33;
        if (r7 == 23) goto L30;
        if (r7 == 66) goto L30;
        if (r7 == 160) goto L30;
        if (r7 == 96) goto L30;
        return super.onKeyDown(r7, r8);
    L30:
        if (r8.getRepeatCount() != 0) goto L32;
        performClick();
    L32:
        return true;
    L33:
        resetActivation();
        List<EpgData.Programme> r9 = this.schedules.get(this.selectedRow);
        if (this.selected != null) goto L40;
        int r1 = firstEndingAfter(r9, this.selectedInstant);
        if (r7 == 21) goto L39;
        r4 = 0;
    L39:
        int r2 = r1 + r4;
    L44:
        if (r2 >= 0) goto L46;
    L53:
        return true;
    L46:
        if (r2 >= r9.size()) goto L53;
        this.selected = r9.get(r2);
        this.selectedInstant = this.selected.start;
        if (m400x(this.selected.start) >= channelWidth()) goto L50;
    L51:
        this.origin = this.selected.start;
    L52:
        notifySelection();
        invalidate();
        goto L53
    L50:
        if (m400x(this.selected.start) <= (getWidth() - (this.density * 60.0f))) goto L52;
    L40:
        int r3 = r9.indexOf(this.selected);
        if (r7 != 22) goto L43;
        r4 = 1;
    L43:
        r2 = r3 + r4;
    L54:
        resetActivation();
        int r5 = this.selectedRow;
        if (r7 != 20) goto L57;
        r4 = 1;
    L57:
        int r6 = r5 + r4;
        if (r6 < 0) goto L65;
        if (r6 >= this.channels.size()) goto L65;
        this.selectedRow = r6;
        selectAt(r0);
        ensureVisible();
        invalidate();
        return true;
    L65:
        return super.onKeyDown(r7, r8);
    L9:
        r0 = this.selected.start;
        goto L10
    }

    @Override // android.view.View
    protected void onSizeChanged(int r1, int r2, int r3, int r4) {
        super.onSizeChanged(r1, r2, r3, r4);
        this.pixelsPerMinute = Math.max(this.density * 3.0f, (r1 - channelWidth()) / 180.0f);
        ensureVisible();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent r8) {
        switch(r8.getActionMasked()) {
            case 0: goto L35;
            case 1: goto L22;
            case 2: goto L8;
            case 3: goto L6;
            default: goto L5;
        };
    L6:
        resetActivation();
        return true;
    L8:
        float r0 = r8.getX() - this.lastX;
        float r2 = r8.getY() - this.lastY;
        if (this.dragging == true) goto L14;
        if (Math.hypot(r8.getX() - this.downX, r8.getY() - this.downY) <= ViewConfiguration.get(getContext()).getScaledTouchSlop()) goto L14;
        this.dragging = true;
    L14:
        if (this.dragging == false) goto L19;
        resetActivation();
        if (this.downX < channelWidth()) goto L18;
        this.origin -= (long) ((r0 / this.pixelsPerMinute) * 60000.0f);
    L18:
        this.offsetY -= r2;
        clampScroll();
        invalidate();
    L19:
        this.lastX = r8.getX();
        this.lastY = r8.getY();
        return true;
    L35:
        float r1 = r8.getX();
        this.lastX = r1;
        this.downX = r1;
        float r9 = r8.getY();
        this.lastY = r9;
        this.downY = r9;
        this.dragging = false;
        requestFocus();
        return true;
    L5:
        return super.onTouchEvent(r8);
    L22:
        if (this.dragging == true) goto L34;
        int r3 = (int) (((r8.getY() - this.headerHeight) + this.offsetY) / this.rowHeight);
        if (r8.getY() < this.headerHeight) goto L33;
        if (r3 < 0) goto L33;
        if (r3 >= this.channels.size()) goto L33;
        this.selectedRow = r3;
        if (r8.getX() >= channelWidth()) goto L31;
        long r4 = System.currentTimeMillis();
    L32:
        selectAt(r4);
        invalidate();
        performClick();
        goto L34
    L31:
        r4 = time(r8.getX());
    L33:
        resetActivation();
    L34:
        return true;
    }

    @Override // android.view.View
    public boolean performClick() {
        super.performClick();
        if (this.activation != null) goto L5;
        return true;
    L5:
        if (selectedChannel() == null) goto L15;
        long r0 = SystemClock.uptimeMillis();
        if (this.tappedRow == this.selectedRow) goto L9;
    L11:
        this.tappedRow = this.selectedRow;
        this.tappedAt = r0;
        return true;
    L9:
        if ((r0 - this.tappedAt) > ViewConfiguration.getDoubleTapTimeout()) goto L11;
        Entry r1 = selectedChannel();
        resetActivation();
        this.activation.play(r1);
        return true;
    L15:
        return true;
    }

    Entry selectedChannel() {
        if (this.channels.isEmpty() == false) goto L6;
        return null;
    L6:
        return this.channels.get(this.selectedRow);
    }

    EpgData.Programme selectedProgramme() {
        return this.selected;
    }

    void setActivationListener(Activation r1) {
        this.activation = r1;
        resetActivation();
        notifySelection();
    }

    void setChannels(List<Entry> r4, EpgData r5, String r6) {
        resetActivation();
        this.channels.clear();
        this.schedules.clear();
        this.channels.addAll(r4);
        int r7 = 0;
        this.selectedRow = 0;
        this.offsetY = 0.0f;
        loadLogos();
    L4:
        if (r7 >= this.channels.size()) goto L5;
        Entry r0 = this.channels.get(r7);
        this.schedules.add(r5.schedule(r0));
        if (r0.identity().equals(r6) == false) goto L10;
        this.selectedRow = r7;
    L10:
        r7 = r7 + 1;
        goto L4
    L5:
        selectAt(System.currentTimeMillis());
        ensureVisible();
        post(new EpgGridView$$ExternalSyntheticLambda1(this));
        invalidate();
    }

    void setOverlay(boolean r1) {
        this.overlay = r1;
        invalidate();
    }

    void setSelectionListener(Selection r1) {
        this.listener = r1;
    }

    void shift(int r7) {
        resetActivation();
        this.origin += ((long) (r7 * 60)) * 60000;
        selectAt(this.origin);
        invalidate();
    }

    long startTime() {
        return this.origin;
    }
}
