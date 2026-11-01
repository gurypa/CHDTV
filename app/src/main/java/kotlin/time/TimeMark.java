package kotlin.time;

import com.arthenica.ffmpegkit.MediaInformation;
import kotlin.Metadata;

/* JADX INFO: compiled from: TimeSource.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\b\bg\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u0003H&ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005J\b\u0010\u0006\u001a\u00020\u0007H\u0016J\b\u0010\b\u001a\u00020\u0007H\u0016J\u0018\u0010\t\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u0003H\u0096\u0002¢\u0006\u0004\b\u000b\u0010\fJ\u0018\u0010\r\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u0003H\u0096\u0002¢\u0006\u0004\b\u000e\u0010\f\u0082\u0002\u0004\n\u0002\b!¨\u0006\u000f"}, m421d2 = {"Lkotlin/time/TimeMark;", "", "elapsedNow", "Lkotlin/time/Duration;", "elapsedNow-UwyO8pc", "()J", "hasNotPassedNow", "", "hasPassedNow", "minus", MediaInformation.KEY_DURATION, "minus-LRDsOJo", "(J)Lkotlin/time/TimeMark;", "plus", "plus-LRDsOJo", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public interface TimeMark {

    /* JADX INFO: compiled from: TimeSource.kt */
    @Metadata(m422k = 3, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class DefaultImpls {
        public static boolean hasNotPassedNow(TimeMark r2) {
            return Duration.m2178isNegativeimpl(r2.mo2135elapsedNowUwyO8pc());
        }

        public static boolean hasPassedNow(TimeMark r2) {
            return !Duration.m2178isNegativeimpl(r2.mo2135elapsedNowUwyO8pc());
        }

        /* JADX INFO: renamed from: minus-LRDsOJo, reason: not valid java name */
        public static TimeMark m2282minusLRDsOJo(TimeMark r2, long r3) {
            return r2.mo2138plusLRDsOJo(Duration.m2198unaryMinusUwyO8pc(r3));
        }

        /* JADX INFO: renamed from: plus-LRDsOJo, reason: not valid java name */
        public static TimeMark m2283plusLRDsOJo(TimeMark r2, long r3) {
            return new AdjustedTimeMark(r2, r3, null);
        }
    }

    /* JADX INFO: renamed from: elapsedNow-UwyO8pc */
    long mo2135elapsedNowUwyO8pc();

    boolean hasNotPassedNow();

    boolean hasPassedNow();

    /* JADX INFO: renamed from: minus-LRDsOJo */
    TimeMark mo2136minusLRDsOJo(long r1);

    /* JADX INFO: renamed from: plus-LRDsOJo */
    TimeMark mo2138plusLRDsOJo(long r1);
}
