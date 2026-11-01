package kotlin.time;

import androidx.exifinterface.media.ExifInterface;
import androidx.media3.exoplayer.upstream.CmcdData;
import androidx.media3.extractor.text.ttml.TtmlNode;
import kotlin.Deprecated;
import kotlin.DeprecatedSinceKotlin;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.ULong$$ExternalSyntheticBackport0;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.JvmInline;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.functions.Function5;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;
import kotlin.ranges.LongRange;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: Duration.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\b-\n\u0002\u0018\u0002\n\u0002\b\u0017\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\u001b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000e\n\u0002\b\u0012\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\b\u0087@\u0018\u0000 ¦\u00012\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0002¦\u0001B\u0011\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\"\u0010D\u001a\u00020\u00002\u0006\u0010E\u001a\u00020\u00032\u0006\u0010F\u001a\u00020\u0003H\u0002ø\u0001\u0000¢\u0006\u0004\bG\u0010HJ\u0018\u0010I\u001a\u00020\t2\u0006\u0010J\u001a\u00020\u0000H\u0096\u0002¢\u0006\u0004\bK\u0010LJ\u001b\u0010M\u001a\u00020\u00002\u0006\u0010N\u001a\u00020\u000fH\u0086\u0002ø\u0001\u0000¢\u0006\u0004\bO\u0010PJ\u001b\u0010M\u001a\u00020\u00002\u0006\u0010N\u001a\u00020\tH\u0086\u0002ø\u0001\u0000¢\u0006\u0004\bO\u0010QJ\u0018\u0010M\u001a\u00020\u000f2\u0006\u0010J\u001a\u00020\u0000H\u0086\u0002¢\u0006\u0004\bR\u0010SJ\u001a\u0010T\u001a\u00020U2\b\u0010J\u001a\u0004\u0018\u00010VHÖ\u0003¢\u0006\u0004\bW\u0010XJ\u0010\u0010Y\u001a\u00020\tHÖ\u0001¢\u0006\u0004\bZ\u0010\rJ\r\u0010[\u001a\u00020U¢\u0006\u0004\b\\\u0010]J\u000f\u0010^\u001a\u00020UH\u0002¢\u0006\u0004\b_\u0010]J\u000f\u0010`\u001a\u00020UH\u0002¢\u0006\u0004\ba\u0010]J\r\u0010b\u001a\u00020U¢\u0006\u0004\bc\u0010]J\r\u0010d\u001a\u00020U¢\u0006\u0004\be\u0010]J\r\u0010f\u001a\u00020U¢\u0006\u0004\bg\u0010]J\u0018\u0010h\u001a\u00020\u00002\u0006\u0010J\u001a\u00020\u0000H\u0086\u0002¢\u0006\u0004\bi\u0010jJ\u0018\u0010k\u001a\u00020\u00002\u0006\u0010J\u001a\u00020\u0000H\u0086\u0002¢\u0006\u0004\bl\u0010jJ\u001b\u0010m\u001a\u00020\u00002\u0006\u0010N\u001a\u00020\u000fH\u0086\u0002ø\u0001\u0000¢\u0006\u0004\bn\u0010PJ\u001b\u0010m\u001a\u00020\u00002\u0006\u0010N\u001a\u00020\tH\u0086\u0002ø\u0001\u0000¢\u0006\u0004\bn\u0010QJ\u009d\u0001\u0010o\u001a\u0002Hp\"\u0004\b\u0000\u0010p2u\u0010q\u001aq\u0012\u0013\u0012\u00110\u0003¢\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(u\u0012\u0013\u0012\u00110\t¢\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(v\u0012\u0013\u0012\u00110\t¢\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(w\u0012\u0013\u0012\u00110\t¢\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(x\u0012\u0013\u0012\u00110\t¢\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(y\u0012\u0004\u0012\u0002Hp0rH\u0086\bø\u0001\u0001\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0004\bz\u0010{J\u0088\u0001\u0010o\u001a\u0002Hp\"\u0004\b\u0000\u0010p2`\u0010q\u001a\\\u0012\u0013\u0012\u00110\u0003¢\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(v\u0012\u0013\u0012\u00110\t¢\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(w\u0012\u0013\u0012\u00110\t¢\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(x\u0012\u0013\u0012\u00110\t¢\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(y\u0012\u0004\u0012\u0002Hp0|H\u0086\bø\u0001\u0001\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0004\bz\u0010}Js\u0010o\u001a\u0002Hp\"\u0004\b\u0000\u0010p2K\u0010q\u001aG\u0012\u0013\u0012\u00110\u0003¢\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(w\u0012\u0013\u0012\u00110\t¢\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(x\u0012\u0013\u0012\u00110\t¢\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(y\u0012\u0004\u0012\u0002Hp0~H\u0086\bø\u0001\u0001\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0004\bz\u0010\u007fJ`\u0010o\u001a\u0002Hp\"\u0004\b\u0000\u0010p27\u0010q\u001a3\u0012\u0013\u0012\u00110\u0003¢\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(x\u0012\u0013\u0012\u00110\t¢\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(y\u0012\u0004\u0012\u0002Hp0\u0080\u0001H\u0086\bø\u0001\u0001\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0005\bz\u0010\u0081\u0001J\u0019\u0010\u0082\u0001\u001a\u00020\u000f2\u0007\u0010\u0083\u0001\u001a\u00020=¢\u0006\u0006\b\u0084\u0001\u0010\u0085\u0001J\u0019\u0010\u0086\u0001\u001a\u00020\t2\u0007\u0010\u0083\u0001\u001a\u00020=¢\u0006\u0006\b\u0087\u0001\u0010\u0088\u0001J\u0011\u0010\u0089\u0001\u001a\u00030\u008a\u0001¢\u0006\u0006\b\u008b\u0001\u0010\u008c\u0001J\u0019\u0010\u008d\u0001\u001a\u00020\u00032\u0007\u0010\u0083\u0001\u001a\u00020=¢\u0006\u0006\b\u008e\u0001\u0010\u008f\u0001J\u0011\u0010\u0090\u0001\u001a\u00020\u0003H\u0007¢\u0006\u0005\b\u0091\u0001\u0010\u0005J\u0011\u0010\u0092\u0001\u001a\u00020\u0003H\u0007¢\u0006\u0005\b\u0093\u0001\u0010\u0005J\u0013\u0010\u0094\u0001\u001a\u00030\u008a\u0001H\u0016¢\u0006\u0006\b\u0095\u0001\u0010\u008c\u0001J%\u0010\u0094\u0001\u001a\u00030\u008a\u00012\u0007\u0010\u0083\u0001\u001a\u00020=2\t\b\u0002\u0010\u0096\u0001\u001a\u00020\t¢\u0006\u0006\b\u0095\u0001\u0010\u0097\u0001J\u001e\u0010\u0098\u0001\u001a\u00020\u00002\u0007\u0010\u0083\u0001\u001a\u00020=H\u0000ø\u0001\u0000¢\u0006\u0006\b\u0099\u0001\u0010\u008f\u0001J\u0015\u0010\u009a\u0001\u001a\u00020\u0000H\u0086\u0002ø\u0001\u0000¢\u0006\u0005\b\u009b\u0001\u0010\u0005JK\u0010\u009c\u0001\u001a\u00030\u009d\u0001*\b0\u009e\u0001j\u0003`\u009f\u00012\u0007\u0010 \u0001\u001a\u00020\t2\u0007\u0010¡\u0001\u001a\u00020\t2\u0007\u0010¢\u0001\u001a\u00020\t2\b\u0010\u0083\u0001\u001a\u00030\u008a\u00012\u0007\u0010£\u0001\u001a\u00020UH\u0002¢\u0006\u0006\b¤\u0001\u0010¥\u0001R\u0014\u0010\u0006\u001a\u00020\u00008Fø\u0001\u0000¢\u0006\u0006\u001a\u0004\b\u0007\u0010\u0005R\u001a\u0010\b\u001a\u00020\t8@X\u0081\u0004¢\u0006\f\u0012\u0004\b\n\u0010\u000b\u001a\u0004\b\f\u0010\rR\u001a\u0010\u000e\u001a\u00020\u000f8FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u0010\u0010\u000b\u001a\u0004\b\u0011\u0010\u0012R\u001a\u0010\u0013\u001a\u00020\u000f8FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u0014\u0010\u000b\u001a\u0004\b\u0015\u0010\u0012R\u001a\u0010\u0016\u001a\u00020\u000f8FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u0017\u0010\u000b\u001a\u0004\b\u0018\u0010\u0012R\u001a\u0010\u0019\u001a\u00020\u000f8FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u001a\u0010\u000b\u001a\u0004\b\u001b\u0010\u0012R\u001a\u0010\u001c\u001a\u00020\u000f8FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u001d\u0010\u000b\u001a\u0004\b\u001e\u0010\u0012R\u001a\u0010\u001f\u001a\u00020\u000f8FX\u0087\u0004¢\u0006\f\u0012\u0004\b \u0010\u000b\u001a\u0004\b!\u0010\u0012R\u001a\u0010\"\u001a\u00020\u000f8FX\u0087\u0004¢\u0006\f\u0012\u0004\b#\u0010\u000b\u001a\u0004\b$\u0010\u0012R\u0011\u0010%\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b&\u0010\u0005R\u0011\u0010'\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b(\u0010\u0005R\u0011\u0010)\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b*\u0010\u0005R\u0011\u0010+\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b,\u0010\u0005R\u0011\u0010-\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b.\u0010\u0005R\u0011\u0010/\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b0\u0010\u0005R\u0011\u00101\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b2\u0010\u0005R\u001a\u00103\u001a\u00020\t8@X\u0081\u0004¢\u0006\f\u0012\u0004\b4\u0010\u000b\u001a\u0004\b5\u0010\rR\u001a\u00106\u001a\u00020\t8@X\u0081\u0004¢\u0006\f\u0012\u0004\b7\u0010\u000b\u001a\u0004\b8\u0010\rR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u00109\u001a\u00020\t8@X\u0081\u0004¢\u0006\f\u0012\u0004\b:\u0010\u000b\u001a\u0004\b;\u0010\rR\u0014\u0010<\u001a\u00020=8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b>\u0010?R\u0015\u0010@\u001a\u00020\t8Â\u0002X\u0082\u0004¢\u0006\u0006\u001a\u0004\bA\u0010\rR\u0014\u0010B\u001a\u00020\u00038BX\u0082\u0004¢\u0006\u0006\u001a\u0004\bC\u0010\u0005\u0088\u0001\u0002\u0092\u0001\u00020\u0003\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u009920\u0001¨\u0006§\u0001"}, m421d2 = {"Lkotlin/time/Duration;", "", "rawValue", "", "constructor-impl", "(J)J", "absoluteValue", "getAbsoluteValue-UwyO8pc", "hoursComponent", "", "getHoursComponent$annotations", "()V", "getHoursComponent-impl", "(J)I", "inDays", "", "getInDays$annotations", "getInDays-impl", "(J)D", "inHours", "getInHours$annotations", "getInHours-impl", "inMicroseconds", "getInMicroseconds$annotations", "getInMicroseconds-impl", "inMilliseconds", "getInMilliseconds$annotations", "getInMilliseconds-impl", "inMinutes", "getInMinutes$annotations", "getInMinutes-impl", "inNanoseconds", "getInNanoseconds$annotations", "getInNanoseconds-impl", "inSeconds", "getInSeconds$annotations", "getInSeconds-impl", "inWholeDays", "getInWholeDays-impl", "inWholeHours", "getInWholeHours-impl", "inWholeMicroseconds", "getInWholeMicroseconds-impl", "inWholeMilliseconds", "getInWholeMilliseconds-impl", "inWholeMinutes", "getInWholeMinutes-impl", "inWholeNanoseconds", "getInWholeNanoseconds-impl", "inWholeSeconds", "getInWholeSeconds-impl", "minutesComponent", "getMinutesComponent$annotations", "getMinutesComponent-impl", "nanosecondsComponent", "getNanosecondsComponent$annotations", "getNanosecondsComponent-impl", "secondsComponent", "getSecondsComponent$annotations", "getSecondsComponent-impl", "storageUnit", "Lkotlin/time/DurationUnit;", "getStorageUnit-impl", "(J)Lkotlin/time/DurationUnit;", "unitDiscriminator", "getUnitDiscriminator-impl", "value", "getValue-impl", "addValuesMixedRanges", "thisMillis", "otherNanos", "addValuesMixedRanges-UwyO8pc", "(JJJ)J", "compareTo", "other", "compareTo-LRDsOJo", "(JJ)I", TtmlNode.TAG_DIV, "scale", "div-UwyO8pc", "(JD)J", "(JI)J", "div-LRDsOJo", "(JJ)D", "equals", "", "", "equals-impl", "(JLjava/lang/Object;)Z", "hashCode", "hashCode-impl", "isFinite", "isFinite-impl", "(J)Z", "isInMillis", "isInMillis-impl", "isInNanos", "isInNanos-impl", "isInfinite", "isInfinite-impl", "isNegative", "isNegative-impl", "isPositive", "isPositive-impl", "minus", "minus-LRDsOJo", "(JJ)J", "plus", "plus-LRDsOJo", "times", "times-UwyO8pc", "toComponents", ExifInterface.GPS_DIRECTION_TRUE, "action", "Lkotlin/Function5;", "Lkotlin/ParameterName;", "name", "days", "hours", "minutes", "seconds", "nanoseconds", "toComponents-impl", "(JLkotlin/jvm/functions/Function5;)Ljava/lang/Object;", "Lkotlin/Function4;", "(JLkotlin/jvm/functions/Function4;)Ljava/lang/Object;", "Lkotlin/Function3;", "(JLkotlin/jvm/functions/Function3;)Ljava/lang/Object;", "Lkotlin/Function2;", "(JLkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "toDouble", "unit", "toDouble-impl", "(JLkotlin/time/DurationUnit;)D", "toInt", "toInt-impl", "(JLkotlin/time/DurationUnit;)I", "toIsoString", "", "toIsoString-impl", "(J)Ljava/lang/String;", "toLong", "toLong-impl", "(JLkotlin/time/DurationUnit;)J", "toLongMilliseconds", "toLongMilliseconds-impl", "toLongNanoseconds", "toLongNanoseconds-impl", "toString", "toString-impl", "decimals", "(JLkotlin/time/DurationUnit;I)Ljava/lang/String;", "truncateTo", "truncateTo-UwyO8pc$kotlin_stdlib", "unaryMinus", "unaryMinus-UwyO8pc", "appendFractional", "", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "whole", "fractional", "fractionalSize", "isoZeroes", "appendFractional-impl", "(JLjava/lang/StringBuilder;IIILjava/lang/String;Z)V", "Companion", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
@JvmInline
public final class Duration implements Comparable<Duration> {
    public static final Companion Companion = null;
    private static final long INFINITE = 0;
    private static final long NEG_INFINITE = 0;
    private static final long ZERO = 0;
    private final long rawValue;

    /* JADX INFO: compiled from: Duration.kt */
    @Metadata(m420d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0017\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000e\n\u0002\b\n\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J \u0010*\u001a\u00020\r2\u0006\u0010+\u001a\u00020\r2\u0006\u0010,\u001a\u00020-2\u0006\u0010.\u001a\u00020-H\u0007J\u001a\u0010\f\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\rH\u0007ø\u0001\u0000¢\u0006\u0004\b/\u0010\u0011J\u001a\u0010\f\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0012H\u0007ø\u0001\u0000¢\u0006\u0004\b/\u0010\u0014J\u001a\u0010\f\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0015H\u0007ø\u0001\u0000¢\u0006\u0004\b/\u0010\u0017J\u001a\u0010\u0018\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\rH\u0007ø\u0001\u0000¢\u0006\u0004\b0\u0010\u0011J\u001a\u0010\u0018\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0012H\u0007ø\u0001\u0000¢\u0006\u0004\b0\u0010\u0014J\u001a\u0010\u0018\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0015H\u0007ø\u0001\u0000¢\u0006\u0004\b0\u0010\u0017J\u001a\u0010\u001b\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\rH\u0007ø\u0001\u0000¢\u0006\u0004\b1\u0010\u0011J\u001a\u0010\u001b\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0012H\u0007ø\u0001\u0000¢\u0006\u0004\b1\u0010\u0014J\u001a\u0010\u001b\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0015H\u0007ø\u0001\u0000¢\u0006\u0004\b1\u0010\u0017J\u001a\u0010\u001e\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\rH\u0007ø\u0001\u0000¢\u0006\u0004\b2\u0010\u0011J\u001a\u0010\u001e\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0012H\u0007ø\u0001\u0000¢\u0006\u0004\b2\u0010\u0014J\u001a\u0010\u001e\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0015H\u0007ø\u0001\u0000¢\u0006\u0004\b2\u0010\u0017J\u001a\u0010!\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\rH\u0007ø\u0001\u0000¢\u0006\u0004\b3\u0010\u0011J\u001a\u0010!\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0012H\u0007ø\u0001\u0000¢\u0006\u0004\b3\u0010\u0014J\u001a\u0010!\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0015H\u0007ø\u0001\u0000¢\u0006\u0004\b3\u0010\u0017J\u001a\u0010$\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\rH\u0007ø\u0001\u0000¢\u0006\u0004\b4\u0010\u0011J\u001a\u0010$\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0012H\u0007ø\u0001\u0000¢\u0006\u0004\b4\u0010\u0014J\u001a\u0010$\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0015H\u0007ø\u0001\u0000¢\u0006\u0004\b4\u0010\u0017J\u0018\u00105\u001a\u00020\u00042\u0006\u0010+\u001a\u000206ø\u0001\u0000¢\u0006\u0004\b7\u00108J\u0018\u00109\u001a\u00020\u00042\u0006\u0010+\u001a\u000206ø\u0001\u0000¢\u0006\u0004\b:\u00108J\u0018\u0010;\u001a\u0004\u0018\u00010\u00042\u0006\u0010+\u001a\u000206ø\u0001\u0000¢\u0006\u0002\b<J\u0018\u0010=\u001a\u0004\u0018\u00010\u00042\u0006\u0010+\u001a\u000206ø\u0001\u0000¢\u0006\u0002\b>J\u001a\u0010'\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\rH\u0007ø\u0001\u0000¢\u0006\u0004\b?\u0010\u0011J\u001a\u0010'\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0012H\u0007ø\u0001\u0000¢\u0006\u0004\b?\u0010\u0014J\u001a\u0010'\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0015H\u0007ø\u0001\u0000¢\u0006\u0004\b?\u0010\u0017R\u0016\u0010\u0003\u001a\u00020\u0004ø\u0001\u0000¢\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b\u0005\u0010\u0006R\u0019\u0010\b\u001a\u00020\u0004X\u0080\u0004ø\u0001\u0000¢\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b\t\u0010\u0006R\u0016\u0010\n\u001a\u00020\u0004ø\u0001\u0000¢\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b\u000b\u0010\u0006R\"\u0010\f\u001a\u00020\u0004*\u00020\r8Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\u000e\u0010\u000f\u001a\u0004\b\u0010\u0010\u0011R\"\u0010\f\u001a\u00020\u0004*\u00020\u00128Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\u000e\u0010\u0013\u001a\u0004\b\u0010\u0010\u0014R\"\u0010\f\u001a\u00020\u0004*\u00020\u00158Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\u000e\u0010\u0016\u001a\u0004\b\u0010\u0010\u0017R\"\u0010\u0018\u001a\u00020\u0004*\u00020\r8Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\u0019\u0010\u000f\u001a\u0004\b\u001a\u0010\u0011R\"\u0010\u0018\u001a\u00020\u0004*\u00020\u00128Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\u0019\u0010\u0013\u001a\u0004\b\u001a\u0010\u0014R\"\u0010\u0018\u001a\u00020\u0004*\u00020\u00158Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\u0019\u0010\u0016\u001a\u0004\b\u001a\u0010\u0017R\"\u0010\u001b\u001a\u00020\u0004*\u00020\r8Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\u001c\u0010\u000f\u001a\u0004\b\u001d\u0010\u0011R\"\u0010\u001b\u001a\u00020\u0004*\u00020\u00128Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\u001c\u0010\u0013\u001a\u0004\b\u001d\u0010\u0014R\"\u0010\u001b\u001a\u00020\u0004*\u00020\u00158Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\u001c\u0010\u0016\u001a\u0004\b\u001d\u0010\u0017R\"\u0010\u001e\u001a\u00020\u0004*\u00020\r8Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\u001f\u0010\u000f\u001a\u0004\b \u0010\u0011R\"\u0010\u001e\u001a\u00020\u0004*\u00020\u00128Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\u001f\u0010\u0013\u001a\u0004\b \u0010\u0014R\"\u0010\u001e\u001a\u00020\u0004*\u00020\u00158Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\u001f\u0010\u0016\u001a\u0004\b \u0010\u0017R\"\u0010!\u001a\u00020\u0004*\u00020\r8Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\"\u0010\u000f\u001a\u0004\b#\u0010\u0011R\"\u0010!\u001a\u00020\u0004*\u00020\u00128Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\"\u0010\u0013\u001a\u0004\b#\u0010\u0014R\"\u0010!\u001a\u00020\u0004*\u00020\u00158Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\"\u0010\u0016\u001a\u0004\b#\u0010\u0017R\"\u0010$\u001a\u00020\u0004*\u00020\r8Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b%\u0010\u000f\u001a\u0004\b&\u0010\u0011R\"\u0010$\u001a\u00020\u0004*\u00020\u00128Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b%\u0010\u0013\u001a\u0004\b&\u0010\u0014R\"\u0010$\u001a\u00020\u0004*\u00020\u00158Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b%\u0010\u0016\u001a\u0004\b&\u0010\u0017R\"\u0010'\u001a\u00020\u0004*\u00020\r8Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b(\u0010\u000f\u001a\u0004\b)\u0010\u0011R\"\u0010'\u001a\u00020\u0004*\u00020\u00128Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b(\u0010\u0013\u001a\u0004\b)\u0010\u0014R\"\u0010'\u001a\u00020\u0004*\u00020\u00158Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b(\u0010\u0016\u001a\u0004\b)\u0010\u0017\u0082\u0002\u0004\n\u0002\b!¨\u0006@"}, m421d2 = {"Lkotlin/time/Duration$Companion;", "", "()V", "INFINITE", "Lkotlin/time/Duration;", "getINFINITE-UwyO8pc", "()J", "J", "NEG_INFINITE", "getNEG_INFINITE-UwyO8pc$kotlin_stdlib", "ZERO", "getZERO-UwyO8pc", "days", "", "getDays-UwyO8pc$annotations", "(D)V", "getDays-UwyO8pc", "(D)J", "", "(I)V", "(I)J", "", "(J)V", "(J)J", "hours", "getHours-UwyO8pc$annotations", "getHours-UwyO8pc", "microseconds", "getMicroseconds-UwyO8pc$annotations", "getMicroseconds-UwyO8pc", "milliseconds", "getMilliseconds-UwyO8pc$annotations", "getMilliseconds-UwyO8pc", "minutes", "getMinutes-UwyO8pc$annotations", "getMinutes-UwyO8pc", "nanoseconds", "getNanoseconds-UwyO8pc$annotations", "getNanoseconds-UwyO8pc", "seconds", "getSeconds-UwyO8pc$annotations", "getSeconds-UwyO8pc", "convert", "value", "sourceUnit", "Lkotlin/time/DurationUnit;", "targetUnit", "days-UwyO8pc", "hours-UwyO8pc", "microseconds-UwyO8pc", "milliseconds-UwyO8pc", "minutes-UwyO8pc", "nanoseconds-UwyO8pc", "parse", "", "parse-UwyO8pc", "(Ljava/lang/String;)J", "parseIsoString", "parseIsoString-UwyO8pc", "parseIsoStringOrNull", "parseIsoStringOrNull-FghU774", "parseOrNull", "parseOrNull-FghU774", "seconds-UwyO8pc", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker r1) {
            this();
        }

        /* JADX INFO: renamed from: getDays-UwyO8pc, reason: not valid java name */
        private final long m2201getDaysUwyO8pc(double r3) {
            return DurationKt.toDuration(r3, DurationUnit.DAYS);
        }

        /* JADX INFO: renamed from: getDays-UwyO8pc, reason: not valid java name */
        private final long m2202getDaysUwyO8pc(int r3) {
            return DurationKt.toDuration(r3, DurationUnit.DAYS);
        }

        /* JADX INFO: renamed from: getDays-UwyO8pc, reason: not valid java name */
        private final long m2203getDaysUwyO8pc(long r3) {
            return DurationKt.toDuration(r3, DurationUnit.DAYS);
        }

        /* JADX INFO: renamed from: getDays-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m2204getDaysUwyO8pc$annotations(double r0) {
        }

        /* JADX INFO: renamed from: getDays-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m2205getDaysUwyO8pc$annotations(int r0) {
        }

        /* JADX INFO: renamed from: getDays-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m2206getDaysUwyO8pc$annotations(long r0) {
        }

        /* JADX INFO: renamed from: getHours-UwyO8pc, reason: not valid java name */
        private final long m2207getHoursUwyO8pc(double r3) {
            return DurationKt.toDuration(r3, DurationUnit.HOURS);
        }

        /* JADX INFO: renamed from: getHours-UwyO8pc, reason: not valid java name */
        private final long m2208getHoursUwyO8pc(int r3) {
            return DurationKt.toDuration(r3, DurationUnit.HOURS);
        }

        /* JADX INFO: renamed from: getHours-UwyO8pc, reason: not valid java name */
        private final long m2209getHoursUwyO8pc(long r3) {
            return DurationKt.toDuration(r3, DurationUnit.HOURS);
        }

        /* JADX INFO: renamed from: getHours-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m2210getHoursUwyO8pc$annotations(double r0) {
        }

        /* JADX INFO: renamed from: getHours-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m2211getHoursUwyO8pc$annotations(int r0) {
        }

        /* JADX INFO: renamed from: getHours-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m2212getHoursUwyO8pc$annotations(long r0) {
        }

        /* JADX INFO: renamed from: getMicroseconds-UwyO8pc, reason: not valid java name */
        private final long m2213getMicrosecondsUwyO8pc(double r3) {
            return DurationKt.toDuration(r3, DurationUnit.MICROSECONDS);
        }

        /* JADX INFO: renamed from: getMicroseconds-UwyO8pc, reason: not valid java name */
        private final long m2214getMicrosecondsUwyO8pc(int r3) {
            return DurationKt.toDuration(r3, DurationUnit.MICROSECONDS);
        }

        /* JADX INFO: renamed from: getMicroseconds-UwyO8pc, reason: not valid java name */
        private final long m2215getMicrosecondsUwyO8pc(long r3) {
            return DurationKt.toDuration(r3, DurationUnit.MICROSECONDS);
        }

        /* JADX INFO: renamed from: getMicroseconds-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m2216getMicrosecondsUwyO8pc$annotations(double r0) {
        }

        /* JADX INFO: renamed from: getMicroseconds-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m2217getMicrosecondsUwyO8pc$annotations(int r0) {
        }

        /* JADX INFO: renamed from: getMicroseconds-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m2218getMicrosecondsUwyO8pc$annotations(long r0) {
        }

        /* JADX INFO: renamed from: getMilliseconds-UwyO8pc, reason: not valid java name */
        private final long m2219getMillisecondsUwyO8pc(double r3) {
            return DurationKt.toDuration(r3, DurationUnit.MILLISECONDS);
        }

        /* JADX INFO: renamed from: getMilliseconds-UwyO8pc, reason: not valid java name */
        private final long m2220getMillisecondsUwyO8pc(int r3) {
            return DurationKt.toDuration(r3, DurationUnit.MILLISECONDS);
        }

        /* JADX INFO: renamed from: getMilliseconds-UwyO8pc, reason: not valid java name */
        private final long m2221getMillisecondsUwyO8pc(long r3) {
            return DurationKt.toDuration(r3, DurationUnit.MILLISECONDS);
        }

        /* JADX INFO: renamed from: getMilliseconds-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m2222getMillisecondsUwyO8pc$annotations(double r0) {
        }

        /* JADX INFO: renamed from: getMilliseconds-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m2223getMillisecondsUwyO8pc$annotations(int r0) {
        }

        /* JADX INFO: renamed from: getMilliseconds-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m2224getMillisecondsUwyO8pc$annotations(long r0) {
        }

        /* JADX INFO: renamed from: getMinutes-UwyO8pc, reason: not valid java name */
        private final long m2225getMinutesUwyO8pc(double r3) {
            return DurationKt.toDuration(r3, DurationUnit.MINUTES);
        }

        /* JADX INFO: renamed from: getMinutes-UwyO8pc, reason: not valid java name */
        private final long m2226getMinutesUwyO8pc(int r3) {
            return DurationKt.toDuration(r3, DurationUnit.MINUTES);
        }

        /* JADX INFO: renamed from: getMinutes-UwyO8pc, reason: not valid java name */
        private final long m2227getMinutesUwyO8pc(long r3) {
            return DurationKt.toDuration(r3, DurationUnit.MINUTES);
        }

        /* JADX INFO: renamed from: getMinutes-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m2228getMinutesUwyO8pc$annotations(double r0) {
        }

        /* JADX INFO: renamed from: getMinutes-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m2229getMinutesUwyO8pc$annotations(int r0) {
        }

        /* JADX INFO: renamed from: getMinutes-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m2230getMinutesUwyO8pc$annotations(long r0) {
        }

        /* JADX INFO: renamed from: getNanoseconds-UwyO8pc, reason: not valid java name */
        private final long m2231getNanosecondsUwyO8pc(double r3) {
            return DurationKt.toDuration(r3, DurationUnit.NANOSECONDS);
        }

        /* JADX INFO: renamed from: getNanoseconds-UwyO8pc, reason: not valid java name */
        private final long m2232getNanosecondsUwyO8pc(int r3) {
            return DurationKt.toDuration(r3, DurationUnit.NANOSECONDS);
        }

        /* JADX INFO: renamed from: getNanoseconds-UwyO8pc, reason: not valid java name */
        private final long m2233getNanosecondsUwyO8pc(long r3) {
            return DurationKt.toDuration(r3, DurationUnit.NANOSECONDS);
        }

        /* JADX INFO: renamed from: getNanoseconds-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m2234getNanosecondsUwyO8pc$annotations(double r0) {
        }

        /* JADX INFO: renamed from: getNanoseconds-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m2235getNanosecondsUwyO8pc$annotations(int r0) {
        }

        /* JADX INFO: renamed from: getNanoseconds-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m2236getNanosecondsUwyO8pc$annotations(long r0) {
        }

        /* JADX INFO: renamed from: getSeconds-UwyO8pc, reason: not valid java name */
        private final long m2237getSecondsUwyO8pc(double r3) {
            return DurationKt.toDuration(r3, DurationUnit.SECONDS);
        }

        /* JADX INFO: renamed from: getSeconds-UwyO8pc, reason: not valid java name */
        private final long m2238getSecondsUwyO8pc(int r3) {
            return DurationKt.toDuration(r3, DurationUnit.SECONDS);
        }

        /* JADX INFO: renamed from: getSeconds-UwyO8pc, reason: not valid java name */
        private final long m2239getSecondsUwyO8pc(long r3) {
            return DurationKt.toDuration(r3, DurationUnit.SECONDS);
        }

        /* JADX INFO: renamed from: getSeconds-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m2240getSecondsUwyO8pc$annotations(double r0) {
        }

        /* JADX INFO: renamed from: getSeconds-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m2241getSecondsUwyO8pc$annotations(int r0) {
        }

        /* JADX INFO: renamed from: getSeconds-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m2242getSecondsUwyO8pc$annotations(long r0) {
        }

        public final double convert(double r3, DurationUnit r5, DurationUnit r6) {
            Intrinsics.checkNotNullParameter(r5, "sourceUnit");
            Intrinsics.checkNotNullParameter(r6, "targetUnit");
            return DurationUnitKt.convertDurationUnit(r3, r5, r6);
        }

        @Deprecated(message = "Use 'Double.days' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "value.days", imports = {"kotlin.time.Duration.Companion.days"}))
        @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: days-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m2243daysUwyO8pc(double r3) {
            return DurationKt.toDuration(r3, DurationUnit.DAYS);
        }

        @Deprecated(message = "Use 'Int.days' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "value.days", imports = {"kotlin.time.Duration.Companion.days"}))
        @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: days-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m2244daysUwyO8pc(int r3) {
            return DurationKt.toDuration(r3, DurationUnit.DAYS);
        }

        @Deprecated(message = "Use 'Long.days' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "value.days", imports = {"kotlin.time.Duration.Companion.days"}))
        @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: days-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m2245daysUwyO8pc(long r3) {
            return DurationKt.toDuration(r3, DurationUnit.DAYS);
        }

        /* JADX INFO: renamed from: getINFINITE-UwyO8pc, reason: not valid java name */
        public final long m2246getINFINITEUwyO8pc() {
            return Duration.access$getINFINITE$cp();
        }

        /* JADX INFO: renamed from: getNEG_INFINITE-UwyO8pc$kotlin_stdlib, reason: not valid java name */
        public final long m2247getNEG_INFINITEUwyO8pc$kotlin_stdlib() {
            return Duration.access$getNEG_INFINITE$cp();
        }

        /* JADX INFO: renamed from: getZERO-UwyO8pc, reason: not valid java name */
        public final long m2248getZEROUwyO8pc() {
            return Duration.access$getZERO$cp();
        }

        @Deprecated(message = "Use 'Double.hours' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "value.hours", imports = {"kotlin.time.Duration.Companion.hours"}))
        @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: hours-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m2249hoursUwyO8pc(double r3) {
            return DurationKt.toDuration(r3, DurationUnit.HOURS);
        }

        @Deprecated(message = "Use 'Int.hours' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "value.hours", imports = {"kotlin.time.Duration.Companion.hours"}))
        @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: hours-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m2250hoursUwyO8pc(int r3) {
            return DurationKt.toDuration(r3, DurationUnit.HOURS);
        }

        @Deprecated(message = "Use 'Long.hours' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "value.hours", imports = {"kotlin.time.Duration.Companion.hours"}))
        @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: hours-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m2251hoursUwyO8pc(long r3) {
            return DurationKt.toDuration(r3, DurationUnit.HOURS);
        }

        @Deprecated(message = "Use 'Double.microseconds' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "value.microseconds", imports = {"kotlin.time.Duration.Companion.microseconds"}))
        @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: microseconds-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m2252microsecondsUwyO8pc(double r3) {
            return DurationKt.toDuration(r3, DurationUnit.MICROSECONDS);
        }

        @Deprecated(message = "Use 'Int.microseconds' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "value.microseconds", imports = {"kotlin.time.Duration.Companion.microseconds"}))
        @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: microseconds-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m2253microsecondsUwyO8pc(int r3) {
            return DurationKt.toDuration(r3, DurationUnit.MICROSECONDS);
        }

        @Deprecated(message = "Use 'Long.microseconds' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "value.microseconds", imports = {"kotlin.time.Duration.Companion.microseconds"}))
        @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: microseconds-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m2254microsecondsUwyO8pc(long r3) {
            return DurationKt.toDuration(r3, DurationUnit.MICROSECONDS);
        }

        @Deprecated(message = "Use 'Double.milliseconds' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "value.milliseconds", imports = {"kotlin.time.Duration.Companion.milliseconds"}))
        @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: milliseconds-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m2255millisecondsUwyO8pc(double r3) {
            return DurationKt.toDuration(r3, DurationUnit.MILLISECONDS);
        }

        @Deprecated(message = "Use 'Int.milliseconds' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "value.milliseconds", imports = {"kotlin.time.Duration.Companion.milliseconds"}))
        @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: milliseconds-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m2256millisecondsUwyO8pc(int r3) {
            return DurationKt.toDuration(r3, DurationUnit.MILLISECONDS);
        }

        @Deprecated(message = "Use 'Long.milliseconds' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "value.milliseconds", imports = {"kotlin.time.Duration.Companion.milliseconds"}))
        @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: milliseconds-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m2257millisecondsUwyO8pc(long r3) {
            return DurationKt.toDuration(r3, DurationUnit.MILLISECONDS);
        }

        @Deprecated(message = "Use 'Double.minutes' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "value.minutes", imports = {"kotlin.time.Duration.Companion.minutes"}))
        @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: minutes-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m2258minutesUwyO8pc(double r3) {
            return DurationKt.toDuration(r3, DurationUnit.MINUTES);
        }

        @Deprecated(message = "Use 'Int.minutes' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "value.minutes", imports = {"kotlin.time.Duration.Companion.minutes"}))
        @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: minutes-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m2259minutesUwyO8pc(int r3) {
            return DurationKt.toDuration(r3, DurationUnit.MINUTES);
        }

        @Deprecated(message = "Use 'Long.minutes' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "value.minutes", imports = {"kotlin.time.Duration.Companion.minutes"}))
        @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: minutes-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m2260minutesUwyO8pc(long r3) {
            return DurationKt.toDuration(r3, DurationUnit.MINUTES);
        }

        @Deprecated(message = "Use 'Double.nanoseconds' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "value.nanoseconds", imports = {"kotlin.time.Duration.Companion.nanoseconds"}))
        @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: nanoseconds-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m2261nanosecondsUwyO8pc(double r3) {
            return DurationKt.toDuration(r3, DurationUnit.NANOSECONDS);
        }

        @Deprecated(message = "Use 'Int.nanoseconds' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "value.nanoseconds", imports = {"kotlin.time.Duration.Companion.nanoseconds"}))
        @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: nanoseconds-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m2262nanosecondsUwyO8pc(int r3) {
            return DurationKt.toDuration(r3, DurationUnit.NANOSECONDS);
        }

        @Deprecated(message = "Use 'Long.nanoseconds' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "value.nanoseconds", imports = {"kotlin.time.Duration.Companion.nanoseconds"}))
        @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: nanoseconds-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m2263nanosecondsUwyO8pc(long r3) {
            return DurationKt.toDuration(r3, DurationUnit.NANOSECONDS);
        }

        /* JADX INFO: renamed from: parse-UwyO8pc, reason: not valid java name */
        public final long m2264parseUwyO8pc(String r5) {
            Intrinsics.checkNotNullParameter(r5, "value");
            return DurationKt.access$parseDuration(r5, false);
        L5:
            e = move-exception;
            throw new IllegalArgumentException("Invalid duration string format: '" + r5 + "'.", e);
        }

        /* JADX INFO: renamed from: parseIsoString-UwyO8pc, reason: not valid java name */
        public final long m2265parseIsoStringUwyO8pc(String r5) {
            Intrinsics.checkNotNullParameter(r5, "value");
            return DurationKt.access$parseDuration(r5, true);
        L5:
            e = move-exception;
            throw new IllegalArgumentException("Invalid ISO duration string format: '" + r5 + "'.", e);
        }

        /* JADX INFO: renamed from: parseIsoStringOrNull-FghU774, reason: not valid java name */
        public final Duration m2266parseIsoStringOrNullFghU774(String r3) {
            Intrinsics.checkNotNullParameter(r3, "value");
            return Duration.m2143boximpl(DurationKt.access$parseDuration(r3, true));
        L7:
            return null;
        }

        /* JADX INFO: renamed from: parseOrNull-FghU774, reason: not valid java name */
        public final Duration m2267parseOrNullFghU774(String r3) {
            Intrinsics.checkNotNullParameter(r3, "value");
            return Duration.m2143boximpl(DurationKt.access$parseDuration(r3, false));
        L7:
            return null;
        }

        @Deprecated(message = "Use 'Double.seconds' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "value.seconds", imports = {"kotlin.time.Duration.Companion.seconds"}))
        @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: seconds-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m2268secondsUwyO8pc(double r3) {
            return DurationKt.toDuration(r3, DurationUnit.SECONDS);
        }

        @Deprecated(message = "Use 'Int.seconds' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "value.seconds", imports = {"kotlin.time.Duration.Companion.seconds"}))
        @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: seconds-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m2269secondsUwyO8pc(int r3) {
            return DurationKt.toDuration(r3, DurationUnit.SECONDS);
        }

        @Deprecated(message = "Use 'Long.seconds' extension property from Duration.Companion instead.", replaceWith = @ReplaceWith(expression = "value.seconds", imports = {"kotlin.time.Duration.Companion.seconds"}))
        @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: seconds-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m2270secondsUwyO8pc(long r3) {
            return DurationKt.toDuration(r3, DurationUnit.SECONDS);
        }
    }

    static {
        Companion = new Companion(null);
        ZERO = m2145constructorimpl(0);
        INFINITE = DurationKt.access$durationOfMillis(DurationKt.MAX_MILLIS);
        NEG_INFINITE = DurationKt.access$durationOfMillis(-4611686018427387903L);
    }

    private /* synthetic */ Duration(long r1) {
        this.rawValue = r1;
    }

    public static final /* synthetic */ long access$getINFINITE$cp() {
        return INFINITE;
    }

    public static final /* synthetic */ long access$getNEG_INFINITE$cp() {
        return NEG_INFINITE;
    }

    public static final /* synthetic */ long access$getZERO$cp() {
        return ZERO;
    }

    /* JADX INFO: renamed from: addValuesMixedRanges-UwyO8pc, reason: not valid java name */
    private static final long m2141addValuesMixedRangesUwyO8pc(long r9, long r11, long r13) {
        long r0 = DurationKt.access$nanosToMillis(r13);
        long r2 = r11 + r0;
        if (new LongRange(-4611686018426L, 4611686018426L).contains(r2) == false) goto L6;
        long r4 = r13 - DurationKt.access$millisToNanos(r0);
        return DurationKt.access$durationOfNanos(DurationKt.access$millisToNanos(r2) + r4);
    L6:
        return DurationKt.access$durationOfMillis(RangesKt.coerceIn(r2, -4611686018427387903L, DurationKt.MAX_MILLIS));
    }

    /* JADX INFO: renamed from: appendFractional-impl, reason: not valid java name */
    private static final void m2142appendFractionalimpl(long r12, StringBuilder r14, int r15, int r16, int r17, String r18, boolean r19) {
        r14.append(r15);
        if (r16 == 0) goto L20;
        r14.append('.');
        String r0 = StringsKt.padStart(String.valueOf(r16), r17, '0');
        String r3 = r0;
        int r6 = -1;
        int r5 = r3.length() - 1;
        if (r5 < 0) goto L14;
    L6:
        int r9 = r5;
        r5 = r5 - 1;
        if (r3.charAt(r9) == '0') goto L9;
        boolean r10 = true;
    L10:
        if (r10 == true) goto L11;
        if (r5 >= 0) goto L6;
    L11:
        r6 = r9;
        goto L14
    L9:
        r10 = false;
    L14:
        int r7 = r6 + 1;
        if (r19 == true) goto L18;
        if (r7 >= 3) goto L18;
        Intrinsics.checkNotNullExpressionValue(r14.append(r0, 0, r7), "append(...)");
    L18:
        Intrinsics.checkNotNullExpressionValue(r14.append(r0, 0, ((r7 + 2) / 3) * 3), "append(...)");
    L20:
        r14.append(r18);
    }

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ Duration m2143boximpl(long r1) {
        return new Duration(r1);
    }

    /* JADX INFO: renamed from: compareTo-LRDsOJo, reason: not valid java name */
    public static int m2144compareToLRDsOJo(long r5, long r7) {
        long r0 = r5 ^ r7;
        if (r0 < 0) goto L13;
        if ((((int) r0) & 1) == 0) goto L13;
        int r2 = (((int) r5) & 1) - (((int) r7) & 1);
        if (m2178isNegativeimpl(r5) == false) goto L11;
        return -r2;
    L11:
        return r2;
    L13:
        return Intrinsics.compare(r5, r7);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static long m2145constructorimpl(long r5) {
        if (DurationJvmKt.getDurationAssertionsEnabled() == true) goto L5;
    L20:
        return r5;
    L5:
        if (m2176isInNanosimpl(r5) == false) goto L12;
        if (new LongRange(-4611686018426999999L, DurationKt.MAX_NANOS).contains(m2172getValueimpl(r5)) == true) goto L20;
        throw new AssertionError(m2172getValueimpl(r5) + " ns is out of nanoseconds range");
    L12:
        if (new LongRange(-4611686018427387903L, DurationKt.MAX_MILLIS).contains(m2172getValueimpl(r5)) == false) goto L19;
        if (new LongRange(-4611686018426L, 4611686018426L).contains(m2172getValueimpl(r5)) == false) goto L20;
        throw new AssertionError(m2172getValueimpl(r5) + " ms is denormalized");
    L19:
        throw new AssertionError(m2172getValueimpl(r5) + " ms is out of milliseconds range");
    }

    /* JADX INFO: renamed from: div-LRDsOJo, reason: not valid java name */
    public static final double m2146divLRDsOJo(long r5, long r7) {
        DurationUnit r0 = (DurationUnit) ComparisonsKt.maxOf(m2170getStorageUnitimpl(r5), m2170getStorageUnitimpl(r7));
        return m2188toDoubleimpl(r5, r0) / m2188toDoubleimpl(r7, r0);
    }

    /* JADX INFO: renamed from: div-UwyO8pc, reason: not valid java name */
    public static final long m2147divUwyO8pc(long r6, double r8) {
        int r0 = MathKt.roundToInt(r8);
        if (r0 != r8) goto L5;
        boolean r1 = true;
    L6:
        if (r1 == false) goto L10;
        if (r0 == 0) goto L10;
        return m2148divUwyO8pc(r6, r0);
    L10:
        DurationUnit r2 = m2170getStorageUnitimpl(r6);
        return DurationKt.toDuration(m2188toDoubleimpl(r6, r2) / r8, r2);
    L5:
        r1 = false;
        goto L6
    }

    /* JADX INFO: renamed from: div-UwyO8pc, reason: not valid java name */
    public static final long m2148divUwyO8pc(long r7, int r9) {
        if (r9 != 0) goto L13;
        if (m2179isPositiveimpl(r7) == false) goto L7;
        return INFINITE;
    L7:
        if (m2178isNegativeimpl(r7) == false) goto L11;
        return NEG_INFINITE;
    L11:
        throw new IllegalArgumentException("Dividing zero duration by zero yields an undefined result.");
    L13:
        if (m2176isInNanosimpl(r7) == false) goto L17;
        return DurationKt.access$durationOfNanos(m2172getValueimpl(r7) / ((long) r9));
    L17:
        if (m2177isInfiniteimpl(r7) == true) goto L19;
        long r0 = m2172getValueimpl(r7) / ((long) r9);
        if (new LongRange(-4611686018426L, 4611686018426L).contains(r0) == false) goto L25;
        long r2 = DurationKt.access$millisToNanos(m2172getValueimpl(r7) - (((long) r9) * r0)) / ((long) r9);
        return DurationKt.access$durationOfNanos(DurationKt.access$millisToNanos(r0) + r2);
    L25:
        return DurationKt.access$durationOfMillis(r0);
    L19:
        return m2183timesUwyO8pc(r7, MathKt.getSign(r9));
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m2149equalsimpl(long r4, Object r6) {
        if ((r6 instanceof Duration) == true) goto L6;
        return false;
    L6:
        if (r4 == ((Duration) r6).m2200unboximpl()) goto L8;
        return false;
    L8:
        return true;
    }

    /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m2150equalsimpl0(long r1, long r3) {
        if (r1 != r3) goto L5;
        return true;
    L5:
        return false;
    }

    /* JADX INFO: renamed from: getAbsoluteValue-UwyO8pc, reason: not valid java name */
    public static final long m2151getAbsoluteValueUwyO8pc(long r2) {
        if (m2178isNegativeimpl(r2) == false) goto L6;
        return m2198unaryMinusUwyO8pc(r2);
    L6:
        return r2;
    }

    public static /* synthetic */ void getHoursComponent$annotations() {
    }

    /* JADX INFO: renamed from: getHoursComponent-impl, reason: not valid java name */
    public static final int m2152getHoursComponentimpl(long r4) {
        if (m2177isInfiniteimpl(r4) == false) goto L6;
        return 0;
    L6:
        return (int) (m2161getInWholeHoursimpl(r4) % ((long) 24));
    }

    @Deprecated(message = "Use inWholeDays property instead or convert toDouble(DAYS) if a double value is required.", replaceWith = @ReplaceWith(expression = "toDouble(DurationUnit.DAYS)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getInDays$annotations() {
    }

    /* JADX INFO: renamed from: getInDays-impl, reason: not valid java name */
    public static final /* synthetic */ double m2153getInDaysimpl(long r2) {
        return m2188toDoubleimpl(r2, DurationUnit.DAYS);
    }

    @Deprecated(message = "Use inWholeHours property instead or convert toDouble(HOURS) if a double value is required.", replaceWith = @ReplaceWith(expression = "toDouble(DurationUnit.HOURS)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getInHours$annotations() {
    }

    /* JADX INFO: renamed from: getInHours-impl, reason: not valid java name */
    public static final /* synthetic */ double m2154getInHoursimpl(long r2) {
        return m2188toDoubleimpl(r2, DurationUnit.HOURS);
    }

    @Deprecated(message = "Use inWholeMicroseconds property instead or convert toDouble(MICROSECONDS) if a double value is required.", replaceWith = @ReplaceWith(expression = "toDouble(DurationUnit.MICROSECONDS)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getInMicroseconds$annotations() {
    }

    /* JADX INFO: renamed from: getInMicroseconds-impl, reason: not valid java name */
    public static final /* synthetic */ double m2155getInMicrosecondsimpl(long r2) {
        return m2188toDoubleimpl(r2, DurationUnit.MICROSECONDS);
    }

    @Deprecated(message = "Use inWholeMilliseconds property instead or convert toDouble(MILLISECONDS) if a double value is required.", replaceWith = @ReplaceWith(expression = "toDouble(DurationUnit.MILLISECONDS)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getInMilliseconds$annotations() {
    }

    /* JADX INFO: renamed from: getInMilliseconds-impl, reason: not valid java name */
    public static final /* synthetic */ double m2156getInMillisecondsimpl(long r2) {
        return m2188toDoubleimpl(r2, DurationUnit.MILLISECONDS);
    }

    @Deprecated(message = "Use inWholeMinutes property instead or convert toDouble(MINUTES) if a double value is required.", replaceWith = @ReplaceWith(expression = "toDouble(DurationUnit.MINUTES)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getInMinutes$annotations() {
    }

    /* JADX INFO: renamed from: getInMinutes-impl, reason: not valid java name */
    public static final /* synthetic */ double m2157getInMinutesimpl(long r2) {
        return m2188toDoubleimpl(r2, DurationUnit.MINUTES);
    }

    @Deprecated(message = "Use inWholeNanoseconds property instead or convert toDouble(NANOSECONDS) if a double value is required.", replaceWith = @ReplaceWith(expression = "toDouble(DurationUnit.NANOSECONDS)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getInNanoseconds$annotations() {
    }

    /* JADX INFO: renamed from: getInNanoseconds-impl, reason: not valid java name */
    public static final /* synthetic */ double m2158getInNanosecondsimpl(long r2) {
        return m2188toDoubleimpl(r2, DurationUnit.NANOSECONDS);
    }

    @Deprecated(message = "Use inWholeSeconds property instead or convert toDouble(SECONDS) if a double value is required.", replaceWith = @ReplaceWith(expression = "toDouble(DurationUnit.SECONDS)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getInSeconds$annotations() {
    }

    /* JADX INFO: renamed from: getInSeconds-impl, reason: not valid java name */
    public static final /* synthetic */ double m2159getInSecondsimpl(long r2) {
        return m2188toDoubleimpl(r2, DurationUnit.SECONDS);
    }

    /* JADX INFO: renamed from: getInWholeDays-impl, reason: not valid java name */
    public static final long m2160getInWholeDaysimpl(long r2) {
        return m2191toLongimpl(r2, DurationUnit.DAYS);
    }

    /* JADX INFO: renamed from: getInWholeHours-impl, reason: not valid java name */
    public static final long m2161getInWholeHoursimpl(long r2) {
        return m2191toLongimpl(r2, DurationUnit.HOURS);
    }

    /* JADX INFO: renamed from: getInWholeMicroseconds-impl, reason: not valid java name */
    public static final long m2162getInWholeMicrosecondsimpl(long r2) {
        return m2191toLongimpl(r2, DurationUnit.MICROSECONDS);
    }

    /* JADX INFO: renamed from: getInWholeMilliseconds-impl, reason: not valid java name */
    public static final long m2163getInWholeMillisecondsimpl(long r2) {
        if (m2175isInMillisimpl(r2) == false) goto L8;
        if (m2174isFiniteimpl(r2) == false) goto L8;
        return m2172getValueimpl(r2);
    L8:
        return m2191toLongimpl(r2, DurationUnit.MILLISECONDS);
    }

    /* JADX INFO: renamed from: getInWholeMinutes-impl, reason: not valid java name */
    public static final long m2164getInWholeMinutesimpl(long r2) {
        return m2191toLongimpl(r2, DurationUnit.MINUTES);
    }

    /* JADX INFO: renamed from: getInWholeNanoseconds-impl, reason: not valid java name */
    public static final long m2165getInWholeNanosecondsimpl(long r4) {
        long r0 = m2172getValueimpl(r4);
        if (m2176isInNanosimpl(r4) == false) goto L6;
        return r0;
    L6:
        if (r0 <= 9223372036854L) goto L9;
        return Long.MAX_VALUE;
    L9:
        if (r0 >= (-9223372036854L)) goto L12;
        return Long.MIN_VALUE;
    L12:
        return DurationKt.access$millisToNanos(r0);
    }

    /* JADX INFO: renamed from: getInWholeSeconds-impl, reason: not valid java name */
    public static final long m2166getInWholeSecondsimpl(long r2) {
        return m2191toLongimpl(r2, DurationUnit.SECONDS);
    }

    public static /* synthetic */ void getMinutesComponent$annotations() {
    }

    /* JADX INFO: renamed from: getMinutesComponent-impl, reason: not valid java name */
    public static final int m2167getMinutesComponentimpl(long r4) {
        if (m2177isInfiniteimpl(r4) == false) goto L6;
        return 0;
    L6:
        return (int) (m2164getInWholeMinutesimpl(r4) % ((long) 60));
    }

    public static /* synthetic */ void getNanosecondsComponent$annotations() {
    }

    /* JADX INFO: renamed from: getNanosecondsComponent-impl, reason: not valid java name */
    public static final int m2168getNanosecondsComponentimpl(long r4) {
        if (m2177isInfiniteimpl(r4) == false) goto L6;
        return 0;
    L6:
        if (m2175isInMillisimpl(r4) == false) goto L9;
        return (int) DurationKt.access$millisToNanos(m2172getValueimpl(r4) % ((long) 1000));
    L9:
        return (int) (m2172getValueimpl(r4) % ((long) 1000000000));
    }

    public static /* synthetic */ void getSecondsComponent$annotations() {
    }

    /* JADX INFO: renamed from: getSecondsComponent-impl, reason: not valid java name */
    public static final int m2169getSecondsComponentimpl(long r4) {
        if (m2177isInfiniteimpl(r4) == false) goto L6;
        return 0;
    L6:
        return (int) (m2166getInWholeSecondsimpl(r4) % ((long) 60));
    }

    /* JADX INFO: renamed from: getStorageUnit-impl, reason: not valid java name */
    private static final DurationUnit m2170getStorageUnitimpl(long r1) {
        if (m2176isInNanosimpl(r1) == false) goto L6;
        return DurationUnit.NANOSECONDS;
    L6:
        return DurationUnit.MILLISECONDS;
    }

    /* JADX INFO: renamed from: getUnitDiscriminator-impl, reason: not valid java name */
    private static final int m2171getUnitDiscriminatorimpl(long r2) {
        return ((int) r2) & 1;
    }

    /* JADX INFO: renamed from: getValue-impl, reason: not valid java name */
    private static final long m2172getValueimpl(long r2) {
        return r2 >> 1;
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m2173hashCodeimpl(long r1) {
        return ULong$$ExternalSyntheticBackport0.m434m(r1);
    }

    /* JADX INFO: renamed from: isFinite-impl, reason: not valid java name */
    public static final boolean m2174isFiniteimpl(long r1) {
        return !m2177isInfiniteimpl(r1);
    }

    /* JADX INFO: renamed from: isInMillis-impl, reason: not valid java name */
    private static final boolean m2175isInMillisimpl(long r3) {
        if ((((int) r3) & 1) == 1) goto L7;
        return false;
    L7:
        return true;
    }

    /* JADX INFO: renamed from: isInNanos-impl, reason: not valid java name */
    private static final boolean m2176isInNanosimpl(long r3) {
        if ((((int) r3) & 1) == 0) goto L7;
        return false;
    L7:
        return true;
    }

    /* JADX INFO: renamed from: isInfinite-impl, reason: not valid java name */
    public static final boolean m2177isInfiniteimpl(long r2) {
        if (r2 != INFINITE) goto L5;
    L8:
        return true;
    L5:
        if (r2 == NEG_INFINITE) goto L8;
        return false;
    }

    /* JADX INFO: renamed from: isNegative-impl, reason: not valid java name */
    public static final boolean m2178isNegativeimpl(long r2) {
        if (r2 >= 0) goto L5;
        return true;
    L5:
        return false;
    }

    /* JADX INFO: renamed from: isPositive-impl, reason: not valid java name */
    public static final boolean m2179isPositiveimpl(long r2) {
        if (r2 <= 0) goto L5;
        return true;
    L5:
        return false;
    }

    /* JADX INFO: renamed from: minus-LRDsOJo, reason: not valid java name */
    public static final long m2180minusLRDsOJo(long r2, long r4) {
        return m2181plusLRDsOJo(r2, m2198unaryMinusUwyO8pc(r4));
    }

    /* JADX INFO: renamed from: plus-LRDsOJo, reason: not valid java name */
    public static final long m2181plusLRDsOJo(long r7, long r9) {
        if (m2177isInfiniteimpl(r7) == false) goto L13;
        if (m2174isFiniteimpl(r9) == false) goto L7;
    L11:
        return r7;
    L7:
        if ((r7 ^ r9) >= 0) goto L11;
        throw new IllegalArgumentException("Summing infinite durations of different signs yields an undefined result.");
    L13:
        if (m2177isInfiniteimpl(r9) == false) goto L16;
        return r9;
    L16:
        if ((((int) r7) & 1) != (((int) r9) & 1)) goto L22;
        long r0 = m2172getValueimpl(r7) + m2172getValueimpl(r9);
        if (m2176isInNanosimpl(r7) == false) goto L27;
        return DurationKt.access$durationOfNanosNormalized(r0);
    L27:
        return DurationKt.access$durationOfMillisNormalized(r0);
    L22:
        if (m2175isInMillisimpl(r7) == false) goto L25;
        return m2141addValuesMixedRangesUwyO8pc(r7, m2172getValueimpl(r7), m2172getValueimpl(r9));
    L25:
        return m2141addValuesMixedRangesUwyO8pc(r7, m2172getValueimpl(r9), m2172getValueimpl(r7));
    }

    /* JADX INFO: renamed from: times-UwyO8pc, reason: not valid java name */
    public static final long m2182timesUwyO8pc(long r6, double r8) {
        int r0 = MathKt.roundToInt(r8);
        if (r0 != r8) goto L5;
        boolean r1 = true;
    L6:
        if (r1 == true) goto L8;
        DurationUnit r2 = m2170getStorageUnitimpl(r6);
        return DurationKt.toDuration(m2188toDoubleimpl(r6, r2) * r8, r2);
    L8:
        return m2183timesUwyO8pc(r6, r0);
    L5:
        r1 = false;
        goto L6
    }

    /* JADX INFO: renamed from: times-UwyO8pc, reason: not valid java name */
    public static final long m2183timesUwyO8pc(long r18, int r20) {
        if (m2177isInfiniteimpl(r18) == false) goto L11;
        if (r20 == 0) goto L10;
        if (r20 <= 0) goto L8;
        return r18;
    L8:
        return m2198unaryMinusUwyO8pc(r18);
    L10:
        throw new IllegalArgumentException("Multiplying infinite duration by zero yields an undefined result.");
    L11:
        if (r20 == 0) goto L13;
        long r1 = m2172getValueimpl(r18);
        long r3 = ((long) r20) * r1;
        if (m2176isInNanosimpl(r18) == false) goto L32;
        if (new LongRange(-2147483647L, 2147483647L).contains(r1) == false) goto L20;
        return DurationKt.access$durationOfNanos(r3);
    L20:
        if ((r3 / ((long) r20)) == r1) goto L41;
        long r10 = DurationKt.access$nanosToMillis(r1);
        long r14 = ((long) r20) * r10;
        long r5 = DurationKt.access$nanosToMillis(r20 * (r1 - DurationKt.access$millisToNanos(r10))) + r14;
        if ((r14 / ((long) r20)) != r10) goto L28;
        if ((r5 ^ r14) < 0) goto L28;
        return DurationKt.access$durationOfMillis(RangesKt.coerceIn(r5, new LongRange(-4611686018427387903L, DurationKt.MAX_MILLIS)));
    L28:
        if ((MathKt.getSign(r1) * MathKt.getSign(r20)) <= 0) goto L44;
        return INFINITE;
    L44:
        return NEG_INFINITE;
    L41:
        return DurationKt.access$durationOfNanosNormalized(r3);
    L32:
        if ((r3 / ((long) r20)) != r1) goto L35;
        return DurationKt.access$durationOfMillis(RangesKt.coerceIn(r3, new LongRange(-4611686018427387903L, DurationKt.MAX_MILLIS)));
    L35:
        if ((MathKt.getSign(r1) * MathKt.getSign(r20)) <= 0) goto L38;
        return INFINITE;
    L38:
        return NEG_INFINITE;
    L13:
        return ZERO;
    }

    /* JADX INFO: renamed from: toComponents-impl, reason: not valid java name */
    public static final <T> T m2184toComponentsimpl(long r3, Function2<? super Long, ? super Integer, ? extends T> r5) {
        Intrinsics.checkNotNullParameter(r5, "action");
        return r5.invoke(Long.valueOf(m2166getInWholeSecondsimpl(r3)), Integer.valueOf(m2168getNanosecondsComponentimpl(r3)));
    }

    /* JADX INFO: renamed from: toComponents-impl, reason: not valid java name */
    public static final <T> T m2185toComponentsimpl(long r4, Function3<? super Long, ? super Integer, ? super Integer, ? extends T> r6) {
        Intrinsics.checkNotNullParameter(r6, "action");
        return r6.invoke(Long.valueOf(m2164getInWholeMinutesimpl(r4)), Integer.valueOf(m2169getSecondsComponentimpl(r4)), Integer.valueOf(m2168getNanosecondsComponentimpl(r4)));
    }

    /* JADX INFO: renamed from: toComponents-impl, reason: not valid java name */
    public static final <T> T m2186toComponentsimpl(long r5, Function4<? super Long, ? super Integer, ? super Integer, ? super Integer, ? extends T> r7) {
        Intrinsics.checkNotNullParameter(r7, "action");
        return r7.invoke(Long.valueOf(m2161getInWholeHoursimpl(r5)), Integer.valueOf(m2167getMinutesComponentimpl(r5)), Integer.valueOf(m2169getSecondsComponentimpl(r5)), Integer.valueOf(m2168getNanosecondsComponentimpl(r5)));
    }

    /* JADX INFO: renamed from: toComponents-impl, reason: not valid java name */
    public static final <T> T m2187toComponentsimpl(long r9, Function5<? super Long, ? super Integer, ? super Integer, ? super Integer, ? super Integer, ? extends T> r11) {
        Intrinsics.checkNotNullParameter(r11, "action");
        return r11.invoke(Long.valueOf(m2160getInWholeDaysimpl(r9)), Integer.valueOf(m2152getHoursComponentimpl(r9)), Integer.valueOf(m2167getMinutesComponentimpl(r9)), Integer.valueOf(m2169getSecondsComponentimpl(r9)), Integer.valueOf(m2168getNanosecondsComponentimpl(r9)));
    }

    /* JADX INFO: renamed from: toDouble-impl, reason: not valid java name */
    public static final double m2188toDoubleimpl(long r3, DurationUnit r5) {
        Intrinsics.checkNotNullParameter(r5, "unit");
        if (r3 != INFINITE) goto L6;
        return Double.POSITIVE_INFINITY;
    L6:
        if (r3 != NEG_INFINITE) goto L9;
        return Double.NEGATIVE_INFINITY;
    L9:
        return DurationUnitKt.convertDurationUnit(m2172getValueimpl(r3), m2170getStorageUnitimpl(r3), r5);
    }

    /* JADX INFO: renamed from: toInt-impl, reason: not valid java name */
    public static final int m2189toIntimpl(long r7, DurationUnit r9) {
        Intrinsics.checkNotNullParameter(r9, "unit");
        return (int) RangesKt.coerceIn(m2191toLongimpl(r7, r9), -2147483648L, 2147483647L);
    }

    /* JADX INFO: renamed from: toIsoString-impl, reason: not valid java name */
    public static final String m2190toIsoStringimpl(long r22) {
        StringBuilder r0 = new StringBuilder();
        if (m2178isNegativeimpl(r22) == false) goto L5;
        r0.append('-');
    L5:
        r0.append("PT");
        long r10 = m2151getAbsoluteValueUwyO8pc(r22);
        long r1 = m2161getInWholeHoursimpl(r10);
        int r13 = m2167getMinutesComponentimpl(r10);
        int r4 = m2169getSecondsComponentimpl(r10);
        int r5 = m2168getNanosecondsComponentimpl(r10);
        if (m2177isInfiniteimpl(r22) == false) goto L8;
        r1 = 9999999999999L;
    L8:
        boolean r7 = true;
        if (r1 == 0) goto L11;
        boolean r6 = true;
    L12:
        boolean r17 = r6;
        if (r4 != 0) goto L17;
        if (r5 != 0) goto L17;
        boolean r8 = false;
    L18:
        boolean r18 = r8;
        if (r13 != 0) goto L24;
        if (r18 == false) goto L23;
        if (r17 == true) goto L24;
    L23:
        r7 = false;
    L24:
        boolean r19 = r7;
        if (r17 == false) goto L27;
        r0.append(r1).append('H');
    L27:
        if (r19 == false) goto L29;
        r0.append(r13).append('M');
    L29:
        if (r18 == true) goto L34;
        if (r17 == true) goto L35;
        if (r19 == false) goto L34;
    L35:
        String r2 = r0.toString();
        Intrinsics.checkNotNullExpressionValue(r2, "toString(...)");
        return r2;
    L34:
        m2142appendFractionalimpl(r22, r0, r4, r5, 9, ExifInterface.LATITUDE_SOUTH, true);
    L17:
        r8 = true;
        goto L18
    L11:
        r6 = false;
        goto L12
    }

    /* JADX INFO: renamed from: toLong-impl, reason: not valid java name */
    public static final long m2191toLongimpl(long r3, DurationUnit r5) {
        Intrinsics.checkNotNullParameter(r5, "unit");
        if (r3 != INFINITE) goto L6;
        return Long.MAX_VALUE;
    L6:
        if (r3 != NEG_INFINITE) goto L9;
        return Long.MIN_VALUE;
    L9:
        return DurationUnitKt.convertDurationUnit(m2172getValueimpl(r3), m2170getStorageUnitimpl(r3), r5);
    }

    @Deprecated(message = "Use inWholeMilliseconds property instead.", replaceWith = @ReplaceWith(expression = "this.inWholeMilliseconds", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    /* JADX INFO: renamed from: toLongMilliseconds-impl, reason: not valid java name */
    public static final /* synthetic */ long m2192toLongMillisecondsimpl(long r2) {
        return m2163getInWholeMillisecondsimpl(r2);
    }

    @Deprecated(message = "Use inWholeNanoseconds property instead.", replaceWith = @ReplaceWith(expression = "this.inWholeNanoseconds", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    /* JADX INFO: renamed from: toLongNanoseconds-impl, reason: not valid java name */
    public static final /* synthetic */ long m2193toLongNanosecondsimpl(long r2) {
        return m2165getInWholeNanosecondsimpl(r2);
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static String m2194toStringimpl(long r27) {
        if (r27 != 0) goto L6;
        return "0s";
    L6:
        if (r27 != INFINITE) goto L9;
        return "Infinity";
    L9:
        if (r27 != NEG_INFINITE) goto L11;
        return "-Infinity";
    L11:
        boolean r2 = m2178isNegativeimpl(r27);
        StringBuilder r3 = new StringBuilder();
        if (r2 == false) goto L14;
        r3.append('-');
    L14:
        long r13 = m2151getAbsoluteValueUwyO8pc(r27);
        long r4 = m2160getInWholeDaysimpl(r13);
        int r7 = m2152getHoursComponentimpl(r13);
        int r8 = m2167getMinutesComponentimpl(r13);
        int r16 = m2169getSecondsComponentimpl(r13);
        int r9 = m2168getNanosecondsComponentimpl(r13);
        boolean r1 = false;
        if (r4 == 0) goto L17;
        boolean r0 = true;
    L18:
        if (r7 == 0) goto L20;
        boolean r11 = true;
    L21:
        boolean r18 = r11;
        if (r8 == 0) goto L24;
        boolean r12 = true;
    L25:
        boolean r19 = r12;
        if (r16 != 0) goto L28;
        if (r9 != 0) goto L28;
    L29:
        int r14 = 0;
        if (r0 == false) goto L34;
        r3.append(r4).append('d');
        r14 = 0 + 1;
    L34:
        if (r18 == true) goto L38;
        if (r0 == false) goto L42;
        if (r19 == true) goto L38;
        if (r1 == true) goto L38;
    L42:
        if (r19 == true) goto L46;
        if (r1 == false) goto L50;
        if (r18 == true) goto L46;
        if (r0 != 0) goto L46;
    L50:
        if (r1 == false) goto L70;
        int r5 = r14 + 1;
        if (r14 <= 0) goto L55;
        r3.append(' ');
    L55:
        if (r16 != 0) goto L67;
        if (r0 != 0) goto L67;
        if (r18 == true) goto L67;
        if (r19 == false) goto L61;
        int r6 = 1;
    L68:
        m2142appendFractionalimpl(r27, r3, r16, r9, 9, CmcdData.Factory.STREAMING_FORMAT_SS, false);
    L69:
        r14 = r5;
    L71:
        if (r2 == false) goto L74;
        if (r14 <= r6) goto L74;
        r3.insert(r6, '(').append(')');
    L74:
        String r10 = r3.toString();
        Intrinsics.checkNotNullExpressionValue(r10, "toString(...)");
        return r10;
    L61:
        if (r9 < 1000000) goto L63;
        r5 = r5;
        r6 = 1;
        m2142appendFractionalimpl(r27, r3, r9 / 1000000, r9 % 1000000, 6, "ms", false);
        goto L69
    L63:
        r5 = r5;
        r6 = 1;
        if (r9 < 1000) goto L66;
        m2142appendFractionalimpl(r27, r3, r9 / 1000, r9 % 1000, 3, "us", false);
        goto L69
    L66:
        r3.append(r9).append("ns");
    L67:
        r6 = 1;
        goto L68
    L70:
        r6 = 1;
    L46:
        int r15 = r14 + 1;
        if (r14 <= 0) goto L49;
        r3.append(' ');
    L49:
        r3.append(r8).append('m');
        r14 = r15;
    L38:
        int r17 = r14 + 1;
        if (r14 <= 0) goto L41;
        r3.append(' ');
    L41:
        r3.append(r7).append('h');
        r14 = r17;
    L28:
        r1 = true;
        goto L29
    L24:
        r12 = false;
        goto L25
    L20:
        r11 = false;
        goto L21
    L17:
        r0 = false;
        goto L18
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static final String m2195toStringimpl(long r4, DurationUnit r6, int r7) {
        Intrinsics.checkNotNullParameter(r6, "unit");
        if (r7 < 0) goto L5;
        boolean r0 = true;
    L6:
        if (r0 == false) goto L14;
        double r1 = m2188toDoubleimpl(r4, r6);
        if (Double.isInfinite(r1) == false) goto L12;
        return String.valueOf(r1);
    L12:
        return DurationJvmKt.formatToExactDecimals(r1, RangesKt.coerceAtMost(r7, 12)) + DurationUnitKt.shortName(r6);
    L14:
        throw new IllegalArgumentException(("decimals must be not negative, but was " + r7).toString());
    L5:
        r0 = false;
        goto L6
    }

    /* JADX INFO: renamed from: toString-impl$default, reason: not valid java name */
    public static /* synthetic */ String m2196toStringimpl$default(long r0, DurationUnit r2, int r3, int r4, Object r5) {
        if ((r4 & 2) == 0) goto L6;
        r3 = 0;
    L6:
        return m2195toStringimpl(r0, r2, r3);
    }

    /* JADX INFO: renamed from: truncateTo-UwyO8pc$kotlin_stdlib, reason: not valid java name */
    public static final long m2197truncateToUwyO8pc$kotlin_stdlib(long r7, DurationUnit r9) {
        Intrinsics.checkNotNullParameter(r9, "unit");
        DurationUnit r0 = m2170getStorageUnitimpl(r7);
        if (r9.compareTo(r0) > 0) goto L5;
    L9:
        return r7;
    L5:
        if (m2177isInfiniteimpl(r7) == true) goto L9;
        long r1 = DurationUnitKt.convertDurationUnit(1, r9, r0);
        return DurationKt.toDuration(m2172getValueimpl(r7) - (m2172getValueimpl(r7) % r1), r0);
    }

    /* JADX INFO: renamed from: unaryMinus-UwyO8pc, reason: not valid java name */
    public static final long m2198unaryMinusUwyO8pc(long r4) {
        return DurationKt.access$durationOf(-m2172getValueimpl(r4), ((int) r4) & 1);
    }

    @Override // java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(Duration r3) {
        return m2199compareToLRDsOJo(r3.m2200unboximpl());
    }

    /* JADX INFO: renamed from: compareTo-LRDsOJo, reason: not valid java name */
    public int m2199compareToLRDsOJo(long r3) {
        return m2144compareToLRDsOJo(this.rawValue, r3);
    }

    public boolean equals(Object r3) {
        return m2149equalsimpl(this.rawValue, r3);
    }

    public int hashCode() {
        return m2173hashCodeimpl(this.rawValue);
    }

    public String toString() {
        return m2194toStringimpl(this.rawValue);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name */
    public final /* synthetic */ long m2200unboximpl() {
        return this.rawValue;
    }
}
