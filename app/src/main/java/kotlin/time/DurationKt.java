package kotlin.time;

import com.arthenica.ffmpegkit.MediaInformation;
import java.util.Collection;
import java.util.Iterator;
import kotlin.Deprecated;
import kotlin.DeprecatedSinceKotlin;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.collections.IntIterator;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;
import kotlin.ranges.CharRange;
import kotlin.ranges.IntRange;
import kotlin.ranges.LongRange;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: Duration.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000>\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0006\n\u0002\b*\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\f\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a\u001d\u0010#\u001a\u00020\u00072\u0006\u0010$\u001a\u00020\u00012\u0006\u0010%\u001a\u00020\u0005H\u0002¢\u0006\u0002\u0010&\u001a\u0015\u0010'\u001a\u00020\u00072\u0006\u0010(\u001a\u00020\u0001H\u0002¢\u0006\u0002\u0010\u0010\u001a\u0015\u0010)\u001a\u00020\u00072\u0006\u0010*\u001a\u00020\u0001H\u0002¢\u0006\u0002\u0010\u0010\u001a\u0015\u0010+\u001a\u00020\u00072\u0006\u0010,\u001a\u00020\u0001H\u0002¢\u0006\u0002\u0010\u0010\u001a\u0015\u0010-\u001a\u00020\u00072\u0006\u0010.\u001a\u00020\u0001H\u0002¢\u0006\u0002\u0010\u0010\u001a\u0010\u0010/\u001a\u00020\u00012\u0006\u0010*\u001a\u00020\u0001H\u0002\u001a\u0010\u00100\u001a\u00020\u00012\u0006\u0010.\u001a\u00020\u0001H\u0002\u001a\u001d\u00101\u001a\u00020\u00072\u0006\u00102\u001a\u0002032\u0006\u00104\u001a\u000205H\u0002¢\u0006\u0002\u00106\u001a\u0010\u00107\u001a\u00020\u00012\u0006\u00102\u001a\u000203H\u0002\u001a)\u00108\u001a\u00020\u0005*\u0002032\u0006\u00109\u001a\u00020\u00052\u0012\u0010:\u001a\u000e\u0012\u0004\u0012\u00020<\u0012\u0004\u0012\u0002050;H\u0082\b\u001a)\u0010=\u001a\u000203*\u0002032\u0006\u00109\u001a\u00020\u00052\u0012\u0010:\u001a\u000e\u0012\u0004\u0012\u00020<\u0012\u0004\u0012\u0002050;H\u0082\b\u001a\u001c\u0010>\u001a\u00020\u0007*\u00020\b2\u0006\u0010?\u001a\u00020\u0007H\u0087\n¢\u0006\u0004\b@\u0010A\u001a\u001c\u0010>\u001a\u00020\u0007*\u00020\u00052\u0006\u0010?\u001a\u00020\u0007H\u0087\n¢\u0006\u0004\bB\u0010C\u001a\u0019\u0010D\u001a\u00020\u0007*\u00020\b2\u0006\u0010E\u001a\u00020FH\u0007¢\u0006\u0002\u0010G\u001a\u0019\u0010D\u001a\u00020\u0007*\u00020\u00052\u0006\u0010E\u001a\u00020FH\u0007¢\u0006\u0002\u0010H\u001a\u0019\u0010D\u001a\u00020\u0007*\u00020\u00012\u0006\u0010E\u001a\u00020FH\u0007¢\u0006\u0002\u0010I\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0005X\u0080T¢\u0006\u0002\n\u0000\"\u001e\u0010\u0006\u001a\u00020\u0007*\u00020\b8FX\u0087\u0004¢\u0006\f\u0012\u0004\b\t\u0010\n\u001a\u0004\b\u000b\u0010\f\"\u001e\u0010\u0006\u001a\u00020\u0007*\u00020\u00058FX\u0087\u0004¢\u0006\f\u0012\u0004\b\t\u0010\r\u001a\u0004\b\u000b\u0010\u000e\"\u001e\u0010\u0006\u001a\u00020\u0007*\u00020\u00018FX\u0087\u0004¢\u0006\f\u0012\u0004\b\t\u0010\u000f\u001a\u0004\b\u000b\u0010\u0010\"\u001e\u0010\u0011\u001a\u00020\u0007*\u00020\b8FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u0012\u0010\n\u001a\u0004\b\u0013\u0010\f\"\u001e\u0010\u0011\u001a\u00020\u0007*\u00020\u00058FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u0012\u0010\r\u001a\u0004\b\u0013\u0010\u000e\"\u001e\u0010\u0011\u001a\u00020\u0007*\u00020\u00018FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u0012\u0010\u000f\u001a\u0004\b\u0013\u0010\u0010\"\u001e\u0010\u0014\u001a\u00020\u0007*\u00020\b8FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u0015\u0010\n\u001a\u0004\b\u0016\u0010\f\"\u001e\u0010\u0014\u001a\u00020\u0007*\u00020\u00058FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u0015\u0010\r\u001a\u0004\b\u0016\u0010\u000e\"\u001e\u0010\u0014\u001a\u00020\u0007*\u00020\u00018FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u0015\u0010\u000f\u001a\u0004\b\u0016\u0010\u0010\"\u001e\u0010\u0017\u001a\u00020\u0007*\u00020\b8FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u0018\u0010\n\u001a\u0004\b\u0019\u0010\f\"\u001e\u0010\u0017\u001a\u00020\u0007*\u00020\u00058FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u0018\u0010\r\u001a\u0004\b\u0019\u0010\u000e\"\u001e\u0010\u0017\u001a\u00020\u0007*\u00020\u00018FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u0018\u0010\u000f\u001a\u0004\b\u0019\u0010\u0010\"\u001e\u0010\u001a\u001a\u00020\u0007*\u00020\b8FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u001b\u0010\n\u001a\u0004\b\u001c\u0010\f\"\u001e\u0010\u001a\u001a\u00020\u0007*\u00020\u00058FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u001b\u0010\r\u001a\u0004\b\u001c\u0010\u000e\"\u001e\u0010\u001a\u001a\u00020\u0007*\u00020\u00018FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u001b\u0010\u000f\u001a\u0004\b\u001c\u0010\u0010\"\u001e\u0010\u001d\u001a\u00020\u0007*\u00020\b8FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u001e\u0010\n\u001a\u0004\b\u001f\u0010\f\"\u001e\u0010\u001d\u001a\u00020\u0007*\u00020\u00058FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u001e\u0010\r\u001a\u0004\b\u001f\u0010\u000e\"\u001e\u0010\u001d\u001a\u00020\u0007*\u00020\u00018FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u001e\u0010\u000f\u001a\u0004\b\u001f\u0010\u0010\"\u001e\u0010 \u001a\u00020\u0007*\u00020\b8FX\u0087\u0004¢\u0006\f\u0012\u0004\b!\u0010\n\u001a\u0004\b\"\u0010\f\"\u001e\u0010 \u001a\u00020\u0007*\u00020\u00058FX\u0087\u0004¢\u0006\f\u0012\u0004\b!\u0010\r\u001a\u0004\b\"\u0010\u000e\"\u001e\u0010 \u001a\u00020\u0007*\u00020\u00018FX\u0087\u0004¢\u0006\f\u0012\u0004\b!\u0010\u000f\u001a\u0004\b\"\u0010\u0010¨\u0006J"}, m421d2 = {"MAX_MILLIS", "", "MAX_NANOS", "MAX_NANOS_IN_MILLIS", "NANOS_IN_MILLIS", "", "days", "Lkotlin/time/Duration;", "", "getDays$annotations", "(D)V", "getDays", "(D)J", "(I)V", "(I)J", "(J)V", "(J)J", "hours", "getHours$annotations", "getHours", "microseconds", "getMicroseconds$annotations", "getMicroseconds", "milliseconds", "getMilliseconds$annotations", "getMilliseconds", "minutes", "getMinutes$annotations", "getMinutes", "nanoseconds", "getNanoseconds$annotations", "getNanoseconds", "seconds", "getSeconds$annotations", "getSeconds", "durationOf", "normalValue", "unitDiscriminator", "(JI)J", "durationOfMillis", "normalMillis", "durationOfMillisNormalized", "millis", "durationOfNanos", "normalNanos", "durationOfNanosNormalized", "nanos", "millisToNanos", "nanosToMillis", "parseDuration", "value", "", "strictIso", "", "(Ljava/lang/String;Z)J", "parseOverLongIsoComponent", "skipWhile", "startIndex", "predicate", "Lkotlin/Function1;", "", "substringWhile", "times", MediaInformation.KEY_DURATION, "times-kIfJnKk", "(DJ)J", "times-mvk6XK0", "(IJ)J", "toDuration", "unit", "Lkotlin/time/DurationUnit;", "(DLkotlin/time/DurationUnit;)J", "(ILkotlin/time/DurationUnit;)J", "(JLkotlin/time/DurationUnit;)J", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class DurationKt {
    public static final long MAX_MILLIS = 4611686018427387903L;
    public static final long MAX_NANOS = 4611686018426999999L;
    private static final long MAX_NANOS_IN_MILLIS = 4611686018426L;
    public static final int NANOS_IN_MILLIS = 1000000;

    public static final /* synthetic */ long access$durationOf(long r2, int r4) {
        return durationOf(r2, r4);
    }

    public static final /* synthetic */ long access$durationOfMillis(long r2) {
        return durationOfMillis(r2);
    }

    public static final /* synthetic */ long access$durationOfMillisNormalized(long r2) {
        return durationOfMillisNormalized(r2);
    }

    public static final /* synthetic */ long access$durationOfNanos(long r2) {
        return durationOfNanos(r2);
    }

    public static final /* synthetic */ long access$durationOfNanosNormalized(long r2) {
        return durationOfNanosNormalized(r2);
    }

    public static final /* synthetic */ long access$millisToNanos(long r2) {
        return millisToNanos(r2);
    }

    public static final /* synthetic */ long access$nanosToMillis(long r2) {
        return nanosToMillis(r2);
    }

    public static final /* synthetic */ long access$parseDuration(String r2, boolean r3) {
        return parseDuration(r2, r3);
    }

    private static final long durationOf(long r4, int r6) {
        return Duration.m2145constructorimpl((r4 << 1) + ((long) r6));
    }

    private static final long durationOfMillis(long r4) {
        return Duration.m2145constructorimpl((r4 << 1) + 1);
    }

    private static final long durationOfMillisNormalized(long r8) {
        if (new LongRange(-4611686018426L, MAX_NANOS_IN_MILLIS).contains(r8) == false) goto L6;
        return durationOfNanos(millisToNanos(r8));
    L6:
        return durationOfMillis(RangesKt.coerceIn(r8, -4611686018427387903L, MAX_MILLIS));
    }

    private static final long durationOfNanos(long r2) {
        return Duration.m2145constructorimpl(r2 << 1);
    }

    private static final long durationOfNanosNormalized(long r5) {
        if (new LongRange(-4611686018426999999L, MAX_NANOS).contains(r5) == false) goto L6;
        return durationOfNanos(r5);
    L6:
        return durationOfMillis(nanosToMillis(r5));
    }

    public static final /* synthetic */ long getDays(double r2) {
        return toDuration(r2, DurationUnit.DAYS);
    }

    public static final /* synthetic */ long getDays(int r2) {
        return toDuration(r2, DurationUnit.DAYS);
    }

    public static final /* synthetic */ long getDays(long r2) {
        return toDuration(r2, DurationUnit.DAYS);
    }

    @Deprecated(message = "Use 'Double.days' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "this.days", imports = {"kotlin.time.Duration.Companion.days"}))
    @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getDays$annotations(double r0) {
    }

    @Deprecated(message = "Use 'Int.days' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "this.days", imports = {"kotlin.time.Duration.Companion.days"}))
    @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getDays$annotations(int r0) {
    }

    @Deprecated(message = "Use 'Long.days' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "this.days", imports = {"kotlin.time.Duration.Companion.days"}))
    @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getDays$annotations(long r0) {
    }

    public static final /* synthetic */ long getHours(double r2) {
        return toDuration(r2, DurationUnit.HOURS);
    }

    public static final /* synthetic */ long getHours(int r2) {
        return toDuration(r2, DurationUnit.HOURS);
    }

    public static final /* synthetic */ long getHours(long r2) {
        return toDuration(r2, DurationUnit.HOURS);
    }

    @Deprecated(message = "Use 'Double.hours' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "this.hours", imports = {"kotlin.time.Duration.Companion.hours"}))
    @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getHours$annotations(double r0) {
    }

    @Deprecated(message = "Use 'Int.hours' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "this.hours", imports = {"kotlin.time.Duration.Companion.hours"}))
    @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getHours$annotations(int r0) {
    }

    @Deprecated(message = "Use 'Long.hours' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "this.hours", imports = {"kotlin.time.Duration.Companion.hours"}))
    @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getHours$annotations(long r0) {
    }

    public static final /* synthetic */ long getMicroseconds(double r2) {
        return toDuration(r2, DurationUnit.MICROSECONDS);
    }

    public static final /* synthetic */ long getMicroseconds(int r2) {
        return toDuration(r2, DurationUnit.MICROSECONDS);
    }

    public static final /* synthetic */ long getMicroseconds(long r2) {
        return toDuration(r2, DurationUnit.MICROSECONDS);
    }

    @Deprecated(message = "Use 'Double.microseconds' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "this.microseconds", imports = {"kotlin.time.Duration.Companion.microseconds"}))
    @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getMicroseconds$annotations(double r0) {
    }

    @Deprecated(message = "Use 'Int.microseconds' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "this.microseconds", imports = {"kotlin.time.Duration.Companion.microseconds"}))
    @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getMicroseconds$annotations(int r0) {
    }

    @Deprecated(message = "Use 'Long.microseconds' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "this.microseconds", imports = {"kotlin.time.Duration.Companion.microseconds"}))
    @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getMicroseconds$annotations(long r0) {
    }

    public static final /* synthetic */ long getMilliseconds(double r2) {
        return toDuration(r2, DurationUnit.MILLISECONDS);
    }

    public static final /* synthetic */ long getMilliseconds(int r2) {
        return toDuration(r2, DurationUnit.MILLISECONDS);
    }

    public static final /* synthetic */ long getMilliseconds(long r2) {
        return toDuration(r2, DurationUnit.MILLISECONDS);
    }

    @Deprecated(message = "Use 'Double.milliseconds' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "this.milliseconds", imports = {"kotlin.time.Duration.Companion.milliseconds"}))
    @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getMilliseconds$annotations(double r0) {
    }

    @Deprecated(message = "Use 'Int.milliseconds' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "this.milliseconds", imports = {"kotlin.time.Duration.Companion.milliseconds"}))
    @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getMilliseconds$annotations(int r0) {
    }

    @Deprecated(message = "Use 'Long.milliseconds' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "this.milliseconds", imports = {"kotlin.time.Duration.Companion.milliseconds"}))
    @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getMilliseconds$annotations(long r0) {
    }

    public static final /* synthetic */ long getMinutes(double r2) {
        return toDuration(r2, DurationUnit.MINUTES);
    }

    public static final /* synthetic */ long getMinutes(int r2) {
        return toDuration(r2, DurationUnit.MINUTES);
    }

    public static final /* synthetic */ long getMinutes(long r2) {
        return toDuration(r2, DurationUnit.MINUTES);
    }

    @Deprecated(message = "Use 'Double.minutes' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "this.minutes", imports = {"kotlin.time.Duration.Companion.minutes"}))
    @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getMinutes$annotations(double r0) {
    }

    @Deprecated(message = "Use 'Int.minutes' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "this.minutes", imports = {"kotlin.time.Duration.Companion.minutes"}))
    @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getMinutes$annotations(int r0) {
    }

    @Deprecated(message = "Use 'Long.minutes' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "this.minutes", imports = {"kotlin.time.Duration.Companion.minutes"}))
    @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getMinutes$annotations(long r0) {
    }

    public static final /* synthetic */ long getNanoseconds(double r2) {
        return toDuration(r2, DurationUnit.NANOSECONDS);
    }

    public static final /* synthetic */ long getNanoseconds(int r2) {
        return toDuration(r2, DurationUnit.NANOSECONDS);
    }

    public static final /* synthetic */ long getNanoseconds(long r2) {
        return toDuration(r2, DurationUnit.NANOSECONDS);
    }

    @Deprecated(message = "Use 'Double.nanoseconds' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "this.nanoseconds", imports = {"kotlin.time.Duration.Companion.nanoseconds"}))
    @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getNanoseconds$annotations(double r0) {
    }

    @Deprecated(message = "Use 'Int.nanoseconds' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "this.nanoseconds", imports = {"kotlin.time.Duration.Companion.nanoseconds"}))
    @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getNanoseconds$annotations(int r0) {
    }

    @Deprecated(message = "Use 'Long.nanoseconds' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "this.nanoseconds", imports = {"kotlin.time.Duration.Companion.nanoseconds"}))
    @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getNanoseconds$annotations(long r0) {
    }

    public static final /* synthetic */ long getSeconds(double r2) {
        return toDuration(r2, DurationUnit.SECONDS);
    }

    public static final /* synthetic */ long getSeconds(int r2) {
        return toDuration(r2, DurationUnit.SECONDS);
    }

    public static final /* synthetic */ long getSeconds(long r2) {
        return toDuration(r2, DurationUnit.SECONDS);
    }

    @Deprecated(message = "Use 'Double.seconds' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "this.seconds", imports = {"kotlin.time.Duration.Companion.seconds"}))
    @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getSeconds$annotations(double r0) {
    }

    @Deprecated(message = "Use 'Int.seconds' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "this.seconds", imports = {"kotlin.time.Duration.Companion.seconds"}))
    @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getSeconds$annotations(int r0) {
    }

    @Deprecated(message = "Use 'Long.seconds' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "this.seconds", imports = {"kotlin.time.Duration.Companion.seconds"}))
    @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getSeconds$annotations(long r0) {
    }

    private static final long millisToNanos(long r2) {
        return ((long) 1000000) * r2;
    }

    private static final long nanosToMillis(long r2) {
        return r2 / ((long) 1000000);
    }

    private static final long parseDuration(String r31, boolean r32) {
        String r0 = r31;
        int r6 = r0.length();
        if (r6 == 0) goto L166;
        int r1 = 0;
        long r7 = Duration.Companion.m2248getZEROUwyO8pc();
        String r2 = "Infinity";
        char r3 = r0.charAt(0);
        if (r3 != '+') goto L7;
    L6:
        boolean r4 = true;
    L10:
        if (r4 == false) goto L12;
        r1 = 0 + 1;
    L12:
        if (r1 <= 0) goto L14;
        boolean r5 = true;
    L15:
        boolean r11 = r5;
        if (r11 == true) goto L18;
    L20:
        boolean r8 = false;
    L21:
        boolean r12 = r8;
        if (r6 <= r1) goto L164;
        if (r0.charAt(r1) != 'P') goto L80;
        int r9 = r1 + 1;
        if (r9 == r6) goto L78;
        String r10 = "+-.";
        boolean r13 = false;
        DurationUnit r14 = null;
    L28:
        if (r9 >= r6) goto L76;
        if (r0.charAt(r9) == 'T') goto L31;
        boolean r15 = false;
        String r19 = r31;
        int r16 = r9;
    L38:
        String r21 = r2;
        if (r16 >= r19.length()) goto L50;
        String r17 = r19;
        String r110 = r10;
        char r18 = r17.charAt(r16);
        int r24 = r6;
        boolean r111 = r15;
        if (new CharRange('0', '9').contains(r18) == true) goto L46;
        boolean r112 = r11;
        if (StringsKt.contains$default(r110, r18, false, 2, null) == true) goto L47;
        boolean r20 = false;
    L48:
        if (r20 == false) goto L51;
        r16 = r16 + 1;
        r11 = r112;
        r15 = r111;
        r10 = r110;
        r2 = r21;
        r19 = r17;
        r6 = r24;
    L51:
        Intrinsics.checkNotNull(r31, "null cannot be cast to non-null type java.lang.String");
        String r22 = r31.substring(r9, r16);
        Intrinsics.checkNotNullExpressionValue(r22, "substring(...)");
        if (r22.length() != 0) goto L54;
        boolean r23 = true;
    L55:
        if (r23 == true) goto L75;
        int r25 = r9 + r22.length();
        String r26 = r31;
        if (r25 < 0) goto L73;
        if (r25 > StringsKt.getLastIndex(r26)) goto L73;
        char r27 = r26.charAt(r25);
        r9 = r25 + 1;
        DurationUnit r28 = DurationUnitKt.durationUnitByIsoChar(r27, r13);
        if (r14 != null) goto L63;
    L67:
        r14 = r28;
        int r29 = StringsKt.indexOf$default(r22, '.', 0, false, 6, null);
        if (r28 != DurationUnit.SECONDS) goto L71;
        if (r29 <= 0) goto L71;
        Intrinsics.checkNotNull(r22, "null cannot be cast to non-null type java.lang.String");
        String r30 = r22.substring(0, r29);
        Intrinsics.checkNotNullExpressionValue(r30, "substring(...)");
        long r33 = Duration.m2181plusLRDsOJo(r7, toDuration(parseOverLongIsoComponent(r30), r28));
        Intrinsics.checkNotNull(r22, "null cannot be cast to non-null type java.lang.String");
        String r113 = r22.substring(r29);
        Intrinsics.checkNotNullExpressionValue(r113, "substring(...)");
        r7 = Duration.m2181plusLRDsOJo(r33, toDuration(Double.parseDouble(r113), r28));
        r0 = r31;
        r11 = r112;
        r10 = r110;
        r2 = r21;
        r6 = r24;
    L71:
        r7 = Duration.m2181plusLRDsOJo(r7, toDuration(parseOverLongIsoComponent(r22), r28));
        r0 = r31;
        r11 = r112;
        r10 = r110;
        r2 = r21;
        r6 = r24;
        goto L28
    L63:
        if (r14.compareTo(r28) > 0) goto L67;
        throw new IllegalArgumentException("Unexpected order of duration components");
    L73:
        throw new IllegalArgumentException("Missing unit for value " + r22);
    L75:
        throw new IllegalArgumentException();
    L54:
        r23 = false;
    L47:
        r20 = true;
        goto L48
    L46:
        r112 = r11;
        goto L47
    L50:
        r24 = r6;
        r112 = r11;
        r110 = r10;
        goto L51
    L31:
        if (r13 == true) goto L36;
        r9 = r9 + 1;
        if (r9 == r6) goto L36;
        r13 = true;
    L36:
        throw new IllegalArgumentException();
    L76:
        boolean r114 = r12;
    L157:
        if (r114 == false) goto L160;
        return Duration.m2198unaryMinusUwyO8pc(r7);
    L160:
        return r7;
    L78:
        throw new IllegalArgumentException();
    L80:
        if (r32 == true) goto L162;
        String r34 = "Infinity";
        if (StringsKt.regionMatches(r31, r1, r34, 0, Math.max(r6 - r1, "Infinity".length()), true) == false) goto L84;
        r7 = Duration.Companion.m2246getINFINITEUwyO8pc();
        r114 = r12;
        goto L157
    L84:
        DurationUnit r35 = null;
        boolean r36 = false;
        if (r11 == true) goto L87;
        boolean r115 = true;
    L88:
        if (r11 == true) goto L90;
    L98:
        int r37 = r6;
    L99:
        if (r1 >= r37) goto L156;
        if (r36 == false) goto L113;
        if (r115 == false) goto L113;
        int r116 = r1;
    L104:
        if (r116 >= r31.length()) goto L112;
        int r117 = r1;
        if (r31.charAt(r116) != ' ') goto L108;
        boolean r38 = true;
    L109:
        if (r38 == false) goto L112;
        r116 = r116 + 1;
        r1 = r117;
        goto L104
    L108:
        r38 = false;
    L112:
        int r39 = r116;
    L114:
        boolean r40 = true;
        boolean r118 = false;
        r34 = r34;
        int r41 = r39;
    L115:
        r36 = r40;
        if (r41 >= r31.length()) goto L126;
        char r42 = r31.charAt(r41);
        boolean r210 = r118;
        r115 = r115;
        r12 = r12;
        if (new CharRange('0', '9').contains(r42) == false) goto L120;
    L123:
        boolean r43 = true;
    L124:
        if (r43 == false) goto L127;
        r41 = r41 + 1;
        r40 = r36;
        r12 = r12;
        r118 = r210;
        r115 = r115;
    L127:
        Intrinsics.checkNotNull(r31, "null cannot be cast to non-null type java.lang.String");
        String r44 = r31.substring(r39, r41);
        Intrinsics.checkNotNullExpressionValue(r44, "substring(...)");
        if (r44.length() != 0) goto L130;
        boolean r45 = true;
    L131:
        if (r45 == true) goto L155;
        int r46 = r39 + r44.length();
        boolean r47 = false;
        String r119 = r31;
        int r120 = r46;
    L134:
        if (r120 >= r119.length()) goto L139;
        boolean r211 = r47;
        String r212 = r119;
        if (new CharRange('a', 'z').contains(r119.charAt(r120)) == false) goto L139;
        r120 = r120 + 1;
        r47 = r211;
        r119 = r212;
    L139:
        Intrinsics.checkNotNull(r31, "null cannot be cast to non-null type java.lang.String");
        String r48 = r31.substring(r46, r120);
        Intrinsics.checkNotNullExpressionValue(r48, "substring(...)");
        r1 = r46 + r48.length();
        DurationUnit r49 = DurationUnitKt.durationUnitByShortName(r48);
        if (r35 != null) goto L142;
    L146:
        r35 = r49;
        int r121 = StringsKt.indexOf$default(r44, '.', 0, false, 6, null);
        if (r121 > 0) goto L148;
        r7 = Duration.m2181plusLRDsOJo(r7, toDuration(Long.parseLong(r44), r49));
        goto L99
    L148:
        Intrinsics.checkNotNull(r44, "null cannot be cast to non-null type java.lang.String");
        String r122 = r44.substring(0, r121);
        Intrinsics.checkNotNullExpressionValue(r122, "substring(...)");
        long r50 = Duration.m2181plusLRDsOJo(r7, toDuration(Long.parseLong(r122), r49));
        Intrinsics.checkNotNull(r44, "null cannot be cast to non-null type java.lang.String");
        String r123 = r44.substring(r121);
        Intrinsics.checkNotNullExpressionValue(r123, "substring(...)");
        r7 = Duration.m2181plusLRDsOJo(r50, toDuration(Double.parseDouble(r123), r49));
        if (r1 >= r37) goto L99;
        throw new IllegalArgumentException("Fractional component must be last");
    L142:
        if (r35.compareTo(r49) > 0) goto L146;
        throw new IllegalArgumentException("Unexpected order of duration components");
    L155:
        throw new IllegalArgumentException();
    L130:
        r45 = false;
        goto L131
    L120:
        if (r42 == '.') goto L123;
        r43 = false;
        goto L124
    L126:
        r115 = r115;
        r12 = r12;
    L113:
        r39 = r1;
        goto L114
    L156:
        r114 = r12;
        goto L157
    L90:
        if (r31.charAt(r1) != '(') goto L98;
        if (StringsKt.last(r31) != ')') goto L98;
        r115 = true;
        r1 = r1 + 1;
        r37 = r6 - 1;
        if (r1 != r37) goto L99;
        throw new IllegalArgumentException("No components");
    L87:
        r115 = false;
        goto L88
    L162:
        throw new IllegalArgumentException();
    L164:
        throw new IllegalArgumentException("No components");
    L18:
        if (StringsKt.startsWith$default(r0, '-', false, 2, null) == false) goto L20;
        r8 = true;
        goto L21
    L14:
        r5 = false;
        goto L15
    L7:
        if (r3 == '-') goto L6;
        r4 = false;
        goto L10
    L166:
        throw new IllegalArgumentException("The string is empty");
    }

    private static final long parseOverLongIsoComponent(String r15) {
        int r0 = r15.length();
        int r1 = 0;
        if (r0 <= 0) goto L8;
        if (StringsKt.contains$default("+-", r15.charAt(0), false, 2, null) == false) goto L8;
        r1 = 0 + 1;
    L8:
        if ((r0 - r1) <= 16) goto L28;
        Iterable r5 = new IntRange(r1, StringsKt.getLastIndex(r15));
        if ((r5 instanceof Collection) == true) goto L12;
    L14:
        Iterator r8 = r5.iterator();
    L16:
        if (r8.hasNext() == false) goto L20;
        if (new CharRange('0', '9').contains(r15.charAt(((IntIterator) r8).nextInt())) == true) goto L16;
        boolean r6 = false;
    L21:
        if (r6 == false) goto L28;
        if (r15.charAt(0) != '-') goto L25;
        return Long.MIN_VALUE;
    L25:
        return Long.MAX_VALUE;
    L20:
        r6 = true;
        goto L21
    L12:
        if (((Collection) r5).isEmpty() == false) goto L14;
        r6 = true;
    L28:
        if (StringsKt.startsWith$default(r15, "+", false, 2, null) == false) goto L31;
        return Long.parseLong(StringsKt.drop(r15, 1));
    L31:
        return Long.parseLong(r15);
    }

    private static final int skipWhile(String r3, int r4, Function1<? super Character, Boolean> r5) {
        int r1 = r4;
    L4:
        if (r1 >= r3.length()) goto L8;
        if (r5.invoke(Character.valueOf(r3.charAt(r1))).booleanValue() == false) goto L8;
        r1 = r1 + 1;
    L8:
        return r1;
    }

    private static final String substringWhile(String r5, int r6, Function1<? super Character, Boolean> r7) {
        int r3 = r6;
    L4:
        if (r3 >= r5.length()) goto L8;
        if (r7.invoke(Character.valueOf(r5.charAt(r3))).booleanValue() == false) goto L8;
        r3 = r3 + 1;
    L8:
        Intrinsics.checkNotNull(r5, "null cannot be cast to non-null type java.lang.String");
        String r1 = r5.substring(r6, r3);
        Intrinsics.checkNotNullExpressionValue(r1, "substring(...)");
        return r1;
    }

    /* JADX INFO: renamed from: times-kIfJnKk, reason: not valid java name */
    private static final long m2271timeskIfJnKk(double r2, long r4) {
        return Duration.m2182timesUwyO8pc(r4, r2);
    }

    /* JADX INFO: renamed from: times-mvk6XK0, reason: not valid java name */
    private static final long m2272timesmvk6XK0(int r2, long r3) {
        return Duration.m2183timesUwyO8pc(r3, r2);
    }

    public static final long toDuration(double r9, DurationUnit r11) {
        Intrinsics.checkNotNullParameter(r11, "unit");
        double r0 = DurationUnitKt.convertDurationUnit(r9, r11, DurationUnit.NANOSECONDS);
        if (Double.isNaN(r0) == true) goto L10;
        long r2 = MathKt.roundToLong(r0);
        if (new LongRange(-4611686018426999999L, MAX_NANOS).contains(r2) == false) goto L8;
        return durationOfNanos(r2);
    L8:
        return durationOfMillisNormalized(MathKt.roundToLong(DurationUnitKt.convertDurationUnit(r9, r11, DurationUnit.MILLISECONDS)));
    L10:
        throw new IllegalArgumentException("Duration value cannot be NaN.".toString());
    }

    public static final long toDuration(int r3, DurationUnit r4) {
        Intrinsics.checkNotNullParameter(r4, "unit");
        if (r4.compareTo(DurationUnit.SECONDS) > 0) goto L6;
        return durationOfNanos(DurationUnitKt.convertDurationUnitOverflow(r3, r4, DurationUnit.NANOSECONDS));
    L6:
        return toDuration(r3, r4);
    }

    public static final long toDuration(long r9, DurationUnit r11) {
        Intrinsics.checkNotNullParameter(r11, "unit");
        long r0 = DurationUnitKt.convertDurationUnitOverflow(MAX_NANOS, DurationUnit.NANOSECONDS, r11);
        if (new LongRange(-r0, r0).contains(r9) == false) goto L7;
        return durationOfNanos(DurationUnitKt.convertDurationUnitOverflow(r9, r11, DurationUnit.NANOSECONDS));
    L7:
        return durationOfMillis(RangesKt.coerceIn(DurationUnitKt.convertDurationUnit(r9, r11, DurationUnit.MILLISECONDS), -4611686018427387903L, MAX_MILLIS));
    }
}
