package kotlin.text;

import androidx.exifinterface.media.ExifInterface;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: Regex.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000>\n\u0000\n\u0002\u0010\"\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\r\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001c\n\u0000\u001a-\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0014\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003*\b\u0012\u0004\u0012\u0002H\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0082\b\u001a\u001e\u0010\u0007\u001a\u0004\u0018\u00010\b*\u00020\t2\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\fH\u0002\u001a\u0016\u0010\r\u001a\u0004\u0018\u00010\b*\u00020\t2\u0006\u0010\u000b\u001a\u00020\fH\u0002\u001a\f\u0010\u000e\u001a\u00020\u000f*\u00020\u0010H\u0002\u001a\u0014\u0010\u000e\u001a\u00020\u000f*\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0006H\u0002\u001a\u0012\u0010\u0012\u001a\u00020\u0006*\b\u0012\u0004\u0012\u00020\u00030\u0013H\u0002¨\u0006\u0014"}, m421d2 = {"fromInt", "", ExifInterface.GPS_DIRECTION_TRUE, "Lkotlin/text/FlagEnum;", "", "value", "", "findNext", "Lkotlin/text/MatchResult;", "Ljava/util/regex/Matcher;", "from", "input", "", "matchEntire", "range", "Lkotlin/ranges/IntRange;", "Ljava/util/regex/MatchResult;", "groupIndex", "toInt", "", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class RegexKt {
    public static final /* synthetic */ MatchResult access$findNext(Matcher r1, int r2, CharSequence r3) {
        return findNext(r1, r2, r3);
    }

    public static final /* synthetic */ MatchResult access$matchEntire(Matcher r1, CharSequence r2) {
        return matchEntire(r1, r2);
    }

    public static final /* synthetic */ IntRange access$range(java.util.regex.MatchResult r1) {
        return range(r1);
    }

    public static final /* synthetic */ IntRange access$range(java.util.regex.MatchResult r1, int r2) {
        return range(r1, r2);
    }

    public static final /* synthetic */ int access$toInt(Iterable r1) {
        return toInt(r1);
    }

    private static final MatchResult findNext(Matcher r1, int r2, CharSequence r3) {
        if (r1.find(r2) == true) goto L6;
        return null;
    L6:
        return new MatcherMatchResult(r1, r3);
    }

    private static final /* synthetic */ <T extends Enum<T> & FlagEnum> Set<T> fromInt(final int r6) {
        Intrinsics.reifiedOperationMarker(4, ExifInterface.GPS_DIRECTION_TRUE);
        EnumSet r1 = EnumSet.allOf(Enum.class);
        EnumSet r2 = r1;
        Intrinsics.checkNotNull(r2);
        Intrinsics.needClassReification();
        CollectionsKt.retainAll(r2, new RegexKt$fromInt$1$1(r6));
        Set<T> r3 = Collections.unmodifiableSet(r1);
        Intrinsics.checkNotNullExpressionValue(r3, "unmodifiableSet(...)");
        return r3;
    }

    private static final MatchResult matchEntire(Matcher r1, CharSequence r2) {
        if (r1.matches() == true) goto L6;
        return null;
    L6:
        return new MatcherMatchResult(r1, r2);
    }

    private static final IntRange range(java.util.regex.MatchResult r2) {
        return RangesKt.until(r2.start(), r2.end());
    }

    private static final IntRange range(java.util.regex.MatchResult r2, int r3) {
        return RangesKt.until(r2.start(r3), r2.end(r3));
    }

    private static final int toInt(Iterable<? extends FlagEnum> r10) {
        int r3 = 0;
        Iterator<? extends FlagEnum> r4 = r10.iterator();
    L4:
        if (r4.hasNext() == false) goto L7;
        r3 = r3 | r4.next().getValue();
        goto L4
    L7:
        return r3;
    }
}
