package kotlin.time;

import androidx.exifinterface.media.ExifInterface;
import com.arthenica.ffmpegkit.MediaInformation;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: measureTime.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002B\u0015\u0012\u0006\u0010\u0003\u001a\u00028\u0000\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u000e\u0010\r\u001a\u00028\u0000HÆ\u0003¢\u0006\u0002\u0010\u000bJ\u0013\u0010\u000e\u001a\u00020\u0005HÆ\u0003ø\u0001\u0000¢\u0006\u0004\b\u000f\u0010\bJ*\u0010\u0010\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\b\b\u0002\u0010\u0003\u001a\u00028\u00002\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001¢\u0006\u0004\b\u0011\u0010\u0012J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0002HÖ\u0003J\t\u0010\u0016\u001a\u00020\u0017HÖ\u0001J\t\u0010\u0018\u001a\u00020\u0019HÖ\u0001R\u0016\u0010\u0004\u001a\u00020\u0005ø\u0001\u0000¢\u0006\n\n\u0002\u0010\t\u001a\u0004\b\u0007\u0010\bR\u0013\u0010\u0003\u001a\u00028\u0000¢\u0006\n\n\u0002\u0010\f\u001a\u0004\b\n\u0010\u000b\u0082\u0002\u0004\n\u0002\b!¨\u0006\u001a"}, m421d2 = {"Lkotlin/time/TimedValue;", ExifInterface.GPS_DIRECTION_TRUE, "", "value", MediaInformation.KEY_DURATION, "Lkotlin/time/Duration;", "(Ljava/lang/Object;JLkotlin/jvm/internal/DefaultConstructorMarker;)V", "getDuration-UwyO8pc", "()J", "J", "getValue", "()Ljava/lang/Object;", "Ljava/lang/Object;", "component1", "component2", "component2-UwyO8pc", "copy", "copy-RFiDyg4", "(Ljava/lang/Object;J)Lkotlin/time/TimedValue;", "equals", "", "other", "hashCode", "", "toString", "", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class TimedValue<T> {
    private final long duration;
    private final T value;

    private TimedValue(T r1, long r2) {
        this.value = r1;
        this.duration = r2;
    }

    public /* synthetic */ TimedValue(Object r1, long r2, DefaultConstructorMarker r4) {
        this(r1, r2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX INFO: renamed from: copy-RFiDyg4$default, reason: not valid java name */
    public static /* synthetic */ TimedValue m2303copyRFiDyg4$default(TimedValue r0, Object r1, long r2, int r4, Object r5) {
        if ((r4 & 1) == 0) goto L6;
        r1 = r0.value;
    L6:
        if ((r4 & 2) == 0) goto L9;
        r2 = r0.duration;
    L9:
        return r0.m2305copyRFiDyg4(r1, r2);
    }

    public final T component1() {
        return this.value;
    }

    /* JADX INFO: renamed from: component2-UwyO8pc, reason: not valid java name */
    public final long m2304component2UwyO8pc() {
        return this.duration;
    }

    /* JADX INFO: renamed from: copy-RFiDyg4, reason: not valid java name */
    public final TimedValue<T> m2305copyRFiDyg4(T r3, long r4) {
        return new TimedValue(r3, r4, null);
    }

    public boolean equals(Object r8) {
        if (this != r8) goto L6;
        return true;
    L6:
        if ((r8 instanceof TimedValue) == true) goto L8;
        return false;
    L8:
        TimedValue r1 = (TimedValue) r8;
        if (Intrinsics.areEqual(this.value, r1.value) == true) goto L12;
        return false;
    L12:
        if (Duration.m2150equalsimpl0(this.duration, r1.duration) == true) goto L14;
        return false;
    L14:
        return true;
    }

    /* JADX INFO: renamed from: getDuration-UwyO8pc, reason: not valid java name */
    public final long m2306getDurationUwyO8pc() {
        return this.duration;
    }

    public final T getValue() {
        return this.value;
    }

    public int hashCode() {
        if (this.value != null) goto L5;
        int r0 = 0;
    L7:
        return (r0 * 31) + Duration.m2173hashCodeimpl(this.duration);
    L5:
        r0 = this.value.hashCode();
        goto L7
    }

    public String toString() {
        return "TimedValue(value=" + this.value + ", duration=" + Duration.m2194toStringimpl(this.duration) + ')';
    }
}
