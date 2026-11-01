package kotlin.time;

import com.arthenica.ffmpegkit.MediaInformation;
import kotlin.Metadata;

/* JADX INFO: compiled from: TimeSources.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0017\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002¢\u0006\u0004\b\t\u0010\nJ\u0018\u0010\u000b\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086\u0002¢\u0006\u0004\b\f\u0010\nJ\b\u0010\r\u001a\u00020\u0004H\u0014R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u000e"}, m421d2 = {"Lkotlin/time/TestTimeSource;", "Lkotlin/time/AbstractLongTimeSource;", "()V", "reading", "", "overflow", "", MediaInformation.KEY_DURATION, "Lkotlin/time/Duration;", "overflow-LRDsOJo", "(J)V", "plusAssign", "plusAssign-LRDsOJo", "read", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class TestTimeSource extends AbstractLongTimeSource {
    private long reading;

    public TestTimeSource() {
        super(DurationUnit.NANOSECONDS);
        markNow();
    }

    /* JADX INFO: renamed from: overflow-LRDsOJo, reason: not valid java name */
    private final void m2280overflowLRDsOJo(long r5) {
        throw new IllegalStateException("TestTimeSource will overflow if its reading " + this.reading + DurationUnitKt.shortName(getUnit()) + " is advanced by " + Duration.m2194toStringimpl(r5) + '.');
    }

    /* JADX INFO: renamed from: plusAssign-LRDsOJo, reason: not valid java name */
    public final void m2281plusAssignLRDsOJo(long r20) {
        long r4 = Duration.m2191toLongimpl(r20, getUnit());
        boolean r11 = true;
        if (((r4 - 1) | 1) != Long.MAX_VALUE) goto L5;
        boolean r0 = true;
    L6:
        if (r0 == true) goto L13;
        long r6 = this.reading + r4;
        if ((this.reading ^ r4) >= 0) goto L10;
    L12:
        this.reading = r6;
        return;
    L10:
        if ((this.reading ^ r6) >= 0) goto L12;
        m2280overflowLRDsOJo(r20);
        goto L12
    L13:
        long r7 = Duration.m2148divUwyO8pc(r20, 2);
        if (((Duration.m2191toLongimpl(r7, getUnit()) - 1) | 1) == Long.MAX_VALUE) goto L17;
        r11 = false;
    L17:
        if (r11 == true) goto L24;
        long r8 = this.reading;
        m2281plusAssignLRDsOJo(r7);     // Catch: IllegalStateException -> L21
        m2281plusAssignLRDsOJo(Duration.m2180minusLRDsOJo(r20, r7));     // Catch: IllegalStateException -> L21
        return;
    L21:
        e = move-exception;
        this.reading = r8;
        throw e;
    L24:
        m2280overflowLRDsOJo(r20);
        return;
    L5:
        r0 = false;
        goto L6
    }

    @Override // kotlin.time.AbstractLongTimeSource
    protected long read() {
        return this.reading;
    }
}
