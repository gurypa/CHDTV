package kotlin.comparisons;

import androidx.exifinterface.media.ExifInterface;
import androidx.media3.exoplayer.upstream.CmcdData;
import java.util.Comparator;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Comparisons.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000<\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a>\u0010\u0000\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u00022\u001a\b\u0004\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005H\u0087\bø\u0001\u0000\u001aY\u0010\u0000\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u000226\u0010\u0007\u001a\u001c\u0012\u0018\b\u0001\u0012\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u00050\b\"\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005¢\u0006\u0002\u0010\t\u001aZ\u0010\u0000\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\n2\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\n0\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\n`\u00032\u0014\b\u0004\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\n0\u0005H\u0087\bø\u0001\u0000\u001a>\u0010\f\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u00022\u001a\b\u0004\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005H\u0087\bø\u0001\u0000\u001aZ\u0010\f\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\n2\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\n0\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\n`\u00032\u0014\b\u0004\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\n0\u0005H\u0087\bø\u0001\u0000\u001a-\u0010\r\u001a\u00020\u000e\"\f\b\u0000\u0010\u0002*\u0006\u0012\u0002\b\u00030\u00062\b\u0010\u000f\u001a\u0004\u0018\u0001H\u00022\b\u0010\u0010\u001a\u0004\u0018\u0001H\u0002¢\u0006\u0002\u0010\u0011\u001aA\u0010\u0012\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u000f\u001a\u0002H\u00022\u0006\u0010\u0010\u001a\u0002H\u00022\u0018\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u0013\u001aY\u0010\u0012\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u000f\u001a\u0002H\u00022\u0006\u0010\u0010\u001a\u0002H\u000226\u0010\u0007\u001a\u001c\u0012\u0018\b\u0001\u0012\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u00050\b\"\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005¢\u0006\u0002\u0010\u0014\u001a]\u0010\u0012\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\n2\u0006\u0010\u000f\u001a\u0002H\u00022\u0006\u0010\u0010\u001a\u0002H\u00022\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\n0\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\n`\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\n0\u0005H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u0015\u001aG\u0010\u0016\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u000f\u001a\u0002H\u00022\u0006\u0010\u0010\u001a\u0002H\u00022 \u0010\u0007\u001a\u001c\u0012\u0018\b\u0001\u0012\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u00050\bH\u0002¢\u0006\u0004\b\u0017\u0010\u0014\u001a&\u0010\u0018\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0006\u001a-\u0010\u0019\u001a\u0016\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0001j\n\u0012\u0006\u0012\u0004\u0018\u0001H\u0002`\u0003\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0006H\u0087\b\u001a@\u0010\u0019\u001a\u0016\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0001j\n\u0012\u0006\u0012\u0004\u0018\u0001H\u0002`\u0003\"\b\b\u0000\u0010\u0002*\u00020\u001a2\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u0003\u001a-\u0010\u001b\u001a\u0016\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0001j\n\u0012\u0006\u0012\u0004\u0018\u0001H\u0002`\u0003\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0006H\u0087\b\u001a@\u0010\u001b\u001a\u0016\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0001j\n\u0012\u0006\u0012\u0004\u0018\u0001H\u0002`\u0003\"\b\b\u0000\u0010\u0002*\u00020\u001a2\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u0003\u001a&\u0010\u001c\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u000e\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0006\u001a0\u0010\u001d\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\u001aO\u0010\u001e\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u00032\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u0003H\u0086\u0004\u001aR\u0010\u001f\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u00032\u001a\b\u0004\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005H\u0087\bø\u0001\u0000\u001an\u0010\u001f\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\n*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u00032\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\n0\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\n`\u00032\u0014\b\u0004\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\n0\u0005H\u0087\bø\u0001\u0000\u001aR\u0010 \u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u00032\u001a\b\u0004\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00060\u0005H\u0087\bø\u0001\u0000\u001an\u0010 \u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\n*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u00032\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\n0\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\n`\u00032\u0014\b\u0004\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\n0\u0005H\u0087\bø\u0001\u0000\u001ap\u0010!\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u000328\b\u0004\u0010\"\u001a2\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b$\u0012\b\b%\u0012\u0004\b\b(\u000f\u0012\u0013\u0012\u0011H\u0002¢\u0006\f\b$\u0012\b\b%\u0012\u0004\b\b(\u0010\u0012\u0004\u0012\u00020\u000e0#H\u0087\bø\u0001\u0000\u001aO\u0010&\u001a\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u0003\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u0004\u0012\u0002H\u00020\u0001j\b\u0012\u0004\u0012\u0002H\u0002`\u00032\u001a\u0010\u000b\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0001j\n\u0012\u0006\b\u0000\u0012\u0002H\u0002`\u0003H\u0086\u0004\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006'"}, m421d2 = {"compareBy", "Ljava/util/Comparator;", ExifInterface.GPS_DIRECTION_TRUE, "Lkotlin/Comparator;", "selector", "Lkotlin/Function1;", "", "selectors", "", "([Lkotlin/jvm/functions/Function1;)Ljava/util/Comparator;", "K", "comparator", "compareByDescending", "compareValues", "", CmcdData.Factory.OBJECT_TYPE_AUDIO_ONLY, "b", "(Ljava/lang/Comparable;Ljava/lang/Comparable;)I", "compareValuesBy", "(Ljava/lang/Object;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)I", "(Ljava/lang/Object;Ljava/lang/Object;[Lkotlin/jvm/functions/Function1;)I", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/util/Comparator;Lkotlin/jvm/functions/Function1;)I", "compareValuesByImpl", "compareValuesByImpl$ComparisonsKt__ComparisonsKt", "naturalOrder", "nullsFirst", "", "nullsLast", "reverseOrder", "reversed", "then", "thenBy", "thenByDescending", "thenComparator", "comparison", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "thenDescending", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/comparisons/ComparisonsKt")
class ComparisonsKt__ComparisonsKt {

    /* JADX INFO: renamed from: kotlin.comparisons.ComparisonsKt__ComparisonsKt$compareBy$2 */
    /* JADX INFO: compiled from: Comparisons.kt */
    @Metadata(m420d1 = {"\u0000\n\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u000e\u0010\u0003\u001a\n \u0004*\u0004\u0018\u0001H\u0002H\u00022\u000e\u0010\u0005\u001a\n \u0004*\u0004\u0018\u0001H\u0002H\u0002H\n¢\u0006\u0004\b\u0006\u0010\u0007"}, m421d2 = {"<anonymous>", "", ExifInterface.GPS_DIRECTION_TRUE, CmcdData.Factory.OBJECT_TYPE_AUDIO_ONLY, "kotlin.jvm.PlatformType", "b", "compare", "(Ljava/lang/Object;Ljava/lang/Object;)I"}, m422k = 3, m423mv = {1, 9, 0}, m425xi = 176)
    public static final class C10712<T> implements Comparator {
        final /* synthetic */ Function1<T, Comparable<?>> $selector;

        /* JADX WARN: Multi-variable type inference failed */
        public C10712(Function1<? super T, ? extends Comparable<?>> r1) {
            this.$selector = r1;
        }

        @Override // java.util.Comparator
        public final int compare(T r3, T r4) {
            Function1<T, Comparable<?>> r0 = this.$selector;
            return ComparisonsKt.compareValues(r0.invoke(r3), r0.invoke(r4));
        }
    }

    /* JADX INFO: renamed from: kotlin.comparisons.ComparisonsKt__ComparisonsKt$compareByDescending$1 */
    /* JADX INFO: compiled from: Comparisons.kt */
    @Metadata(m420d1 = {"\u0000\n\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u000e\u0010\u0003\u001a\n \u0004*\u0004\u0018\u0001H\u0002H\u00022\u000e\u0010\u0005\u001a\n \u0004*\u0004\u0018\u0001H\u0002H\u0002H\n¢\u0006\u0004\b\u0006\u0010\u0007"}, m421d2 = {"<anonymous>", "", ExifInterface.GPS_DIRECTION_TRUE, CmcdData.Factory.OBJECT_TYPE_AUDIO_ONLY, "kotlin.jvm.PlatformType", "b", "compare", "(Ljava/lang/Object;Ljava/lang/Object;)I"}, m422k = 3, m423mv = {1, 9, 0}, m425xi = 176)
    public static final class C10731<T> implements Comparator {
        final /* synthetic */ Function1<T, Comparable<?>> $selector;

        /* JADX WARN: Multi-variable type inference failed */
        public C10731(Function1<? super T, ? extends Comparable<?>> r1) {
            this.$selector = r1;
        }

        @Override // java.util.Comparator
        public final int compare(T r3, T r4) {
            Function1<T, Comparable<?>> r0 = this.$selector;
            return ComparisonsKt.compareValues(r0.invoke(r4), r0.invoke(r3));
        }
    }

    public static /* synthetic */ int $r8$lambda$75RqBHjEE2iU7zbZ3cBiUB4M5DE(Comparator r0, Object r1, Object r2) {
        return nullsLast$lambda$4$ComparisonsKt__ComparisonsKt(r0, r1, r2);
    }

    /* JADX INFO: renamed from: $r8$lambda$OB0mUMQVuAc-U0wu9PhfvNNbqqs, reason: not valid java name */
    public static /* synthetic */ int m1999$r8$lambda$OB0mUMQVuAcU0wu9PhfvNNbqqs(Comparator r0, Comparator r1, Object r2, Object r3) {
        return thenDescending$lambda$2$ComparisonsKt__ComparisonsKt(r0, r1, r2, r3);
    }

    public static /* synthetic */ int $r8$lambda$W8AWSBCuy65sMGWfMo6pJ3RDjRw(Function1[] r0, Object r1, Object r2) {
        return compareBy$lambda$0$ComparisonsKt__ComparisonsKt(r0, r1, r2);
    }

    public static /* synthetic */ int $r8$lambda$fFv4wzBXuBoGFl05zSxqQb7pKRU(Comparator r0, Comparator r1, Object r2, Object r3) {
        return then$lambda$1$ComparisonsKt__ComparisonsKt(r0, r1, r2, r3);
    }

    public static /* synthetic */ int $r8$lambda$tgO5p6pL1ym8xxzpZUrxHlIPkYM(Comparator r0, Object r1, Object r2) {
        return nullsFirst$lambda$3$ComparisonsKt__ComparisonsKt(r0, r1, r2);
    }

    public ComparisonsKt__ComparisonsKt() {
    }

    private static final <T, K> Comparator<T> compareBy(final Comparator<? super K> r1, final Function1<? super T, ? extends K> r2) {
        Intrinsics.checkNotNullParameter(r1, "comparator");
        Intrinsics.checkNotNullParameter(r2, "selector");
        return new C10723(r1, r2);
    }

    private static final <T> Comparator<T> compareBy(Function1<? super T, ? extends Comparable<?>> r1) {
        Intrinsics.checkNotNullParameter(r1, "selector");
        return new C10712(r1);
    }

    public static final <T> Comparator<T> compareBy(final Function1<? super T, ? extends Comparable<?>>... r2) {
        Intrinsics.checkNotNullParameter(r2, "selectors");
        if (r2.length <= 0) goto L5;
        boolean r0 = true;
    L6:
        if (r0 == false) goto L10;
        return new ComparisonsKt__ComparisonsKt$$ExternalSyntheticLambda3(r2);
    L10:
        throw new IllegalArgumentException("Failed requirement.".toString());
    L5:
        r0 = false;
        goto L6
    }

    private static final int compareBy$lambda$0$ComparisonsKt__ComparisonsKt(Function1[] r1, Object r2, Object r3) {
        Intrinsics.checkNotNullParameter(r1, "$selectors");
        return compareValuesByImpl$ComparisonsKt__ComparisonsKt(r2, r3, r1);
    }

    private static final <T, K> Comparator<T> compareByDescending(final Comparator<? super K> r1, final Function1<? super T, ? extends K> r2) {
        Intrinsics.checkNotNullParameter(r1, "comparator");
        Intrinsics.checkNotNullParameter(r2, "selector");
        return new C10742(r1, r2);
    }

    private static final <T> Comparator<T> compareByDescending(Function1<? super T, ? extends Comparable<?>> r1) {
        Intrinsics.checkNotNullParameter(r1, "selector");
        return new C10731(r1);
    }

    public static final <T extends Comparable<?>> int compareValues(T r1, T r2) {
        if (r1 != r2) goto L5;
        return 0;
    L5:
        if (r1 != null) goto L8;
        return -1;
    L8:
        if (r2 != null) goto L12;
        return 1;
    L12:
        return r1.compareTo(r2);
    }

    private static final <T, K> int compareValuesBy(T r2, T r3, Comparator<? super K> r4, Function1<? super T, ? extends K> r5) {
        Intrinsics.checkNotNullParameter(r4, "comparator");
        Intrinsics.checkNotNullParameter(r5, "selector");
        return r4.compare(r5.invoke(r2), r5.invoke(r3));
    }

    private static final <T> int compareValuesBy(T r2, T r3, Function1<? super T, ? extends Comparable<?>> r4) {
        Intrinsics.checkNotNullParameter(r4, "selector");
        return ComparisonsKt.compareValues(r4.invoke(r2), r4.invoke(r3));
    }

    public static final <T> int compareValuesBy(T r2, T r3, Function1<? super T, ? extends Comparable<?>>... r4) {
        Intrinsics.checkNotNullParameter(r4, "selectors");
        if (r4.length <= 0) goto L5;
        boolean r0 = true;
    L6:
        if (r0 == false) goto L10;
        return compareValuesByImpl$ComparisonsKt__ComparisonsKt(r2, r3, r4);
    L10:
        throw new IllegalArgumentException("Failed requirement.".toString());
    L5:
        r0 = false;
        goto L6
    }

    private static final <T> int compareValuesByImpl$ComparisonsKt__ComparisonsKt(T r7, T r8, Function1<? super T, ? extends Comparable<?>>[] r9) {
        int r0 = r9.length;
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L8;
        Function1<? super T, ? extends Comparable<?>> r3 = r9[r2];
        int r6 = ComparisonsKt.compareValues(r3.invoke(r7), r3.invoke(r8));
        if (r6 != 0) goto L6;
        r2 = r2 + 1;
        goto L3
    L6:
        return r6;
    L8:
        return 0;
    }

    public static final <T extends Comparable<? super T>> Comparator<T> naturalOrder() {
        NaturalOrderComparator r0 = NaturalOrderComparator.INSTANCE;
        Intrinsics.checkNotNull(r0, "null cannot be cast to non-null type java.util.Comparator<T of kotlin.comparisons.ComparisonsKt__ComparisonsKt.naturalOrder>{ kotlin.TypeAliasesKt.Comparator<T of kotlin.comparisons.ComparisonsKt__ComparisonsKt.naturalOrder> }");
        return r0;
    }

    private static final <T extends Comparable<? super T>> Comparator<T> nullsFirst() {
        return ComparisonsKt.nullsFirst(ComparisonsKt.naturalOrder());
    }

    public static final <T> Comparator<T> nullsFirst(final Comparator<? super T> r1) {
        Intrinsics.checkNotNullParameter(r1, "comparator");
        return new ComparisonsKt__ComparisonsKt$$ExternalSyntheticLambda4(r1);
    }

    private static final int nullsFirst$lambda$3$ComparisonsKt__ComparisonsKt(Comparator r1, Object r2, Object r3) {
        Intrinsics.checkNotNullParameter(r1, "$comparator");
        if (r2 != r3) goto L5;
        return 0;
    L5:
        if (r2 != null) goto L7;
        return -1;
    L7:
        if (r3 != null) goto L10;
        return 1;
    L10:
        return r1.compare(r2, r3);
    }

    private static final <T extends Comparable<? super T>> Comparator<T> nullsLast() {
        return ComparisonsKt.nullsLast(ComparisonsKt.naturalOrder());
    }

    public static final <T> Comparator<T> nullsLast(final Comparator<? super T> r1) {
        Intrinsics.checkNotNullParameter(r1, "comparator");
        return new ComparisonsKt__ComparisonsKt$$ExternalSyntheticLambda0(r1);
    }

    private static final int nullsLast$lambda$4$ComparisonsKt__ComparisonsKt(Comparator r1, Object r2, Object r3) {
        Intrinsics.checkNotNullParameter(r1, "$comparator");
        if (r2 != r3) goto L5;
        return 0;
    L5:
        if (r2 != null) goto L7;
        return 1;
    L7:
        if (r3 != null) goto L10;
        return -1;
    L10:
        return r1.compare(r2, r3);
    }

    public static final <T extends Comparable<? super T>> Comparator<T> reverseOrder() {
        ReverseOrderComparator r0 = ReverseOrderComparator.INSTANCE;
        Intrinsics.checkNotNull(r0, "null cannot be cast to non-null type java.util.Comparator<T of kotlin.comparisons.ComparisonsKt__ComparisonsKt.reverseOrder>{ kotlin.TypeAliasesKt.Comparator<T of kotlin.comparisons.ComparisonsKt__ComparisonsKt.reverseOrder> }");
        return r0;
    }

    public static final <T> Comparator<T> reversed(Comparator<T> r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if ((r2 instanceof ReversedComparator) == false) goto L6;
        return ((ReversedComparator) r2).getComparator();
    L6:
        if (Intrinsics.areEqual(r2, NaturalOrderComparator.INSTANCE) == false) goto L9;
        ReverseOrderComparator r0 = ReverseOrderComparator.INSTANCE;
        Intrinsics.checkNotNull(r0, "null cannot be cast to non-null type java.util.Comparator<T of kotlin.comparisons.ComparisonsKt__ComparisonsKt.reversed>{ kotlin.TypeAliasesKt.Comparator<T of kotlin.comparisons.ComparisonsKt__ComparisonsKt.reversed> }");
        return r0;
    L9:
        if (Intrinsics.areEqual(r2, ReverseOrderComparator.INSTANCE) == false) goto L12;
        NaturalOrderComparator r1 = NaturalOrderComparator.INSTANCE;
        Intrinsics.checkNotNull(r1, "null cannot be cast to non-null type java.util.Comparator<T of kotlin.comparisons.ComparisonsKt__ComparisonsKt.reversed>{ kotlin.TypeAliasesKt.Comparator<T of kotlin.comparisons.ComparisonsKt__ComparisonsKt.reversed> }");
        return r1;
    L12:
        return new ReversedComparator(r2);
    }

    public static final <T> Comparator<T> then(final Comparator<T> r1, final Comparator<? super T> r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "comparator");
        return new ComparisonsKt__ComparisonsKt$$ExternalSyntheticLambda2(r1, r2);
    }

    private static final int then$lambda$1$ComparisonsKt__ComparisonsKt(Comparator r2, Comparator r3, Object r4, Object r5) {
        Intrinsics.checkNotNullParameter(r2, "$this_then");
        Intrinsics.checkNotNullParameter(r3, "$comparator");
        int r0 = r2.compare(r4, r5);
        if (r0 == 0) goto L6;
        return r0;
    L6:
        return r3.compare(r4, r5);
    }

    private static final <T, K> Comparator<T> thenBy(final Comparator<T> r1, final Comparator<? super K> r2, final Function1<? super T, ? extends K> r3) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "comparator");
        Intrinsics.checkNotNullParameter(r3, "selector");
        return new C10762(r1, r2, r3);
    }

    private static final <T> Comparator<T> thenBy(final Comparator<T> r1, final Function1<? super T, ? extends Comparable<?>> r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "selector");
        return new C10751(r1, r2);
    }

    private static final <T, K> Comparator<T> thenByDescending(final Comparator<T> r1, final Comparator<? super K> r2, final Function1<? super T, ? extends K> r3) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "comparator");
        Intrinsics.checkNotNullParameter(r3, "selector");
        return new C10782(r1, r2, r3);
    }

    private static final <T> Comparator<T> thenByDescending(final Comparator<T> r1, final Function1<? super T, ? extends Comparable<?>> r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "selector");
        return new C10771(r1, r2);
    }

    private static final <T> Comparator<T> thenComparator(final Comparator<T> r1, final Function2<? super T, ? super T, Integer> r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "comparison");
        return new C10791(r1, r2);
    }

    public static final <T> Comparator<T> thenDescending(final Comparator<T> r1, final Comparator<? super T> r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "comparator");
        return new ComparisonsKt__ComparisonsKt$$ExternalSyntheticLambda1(r1, r2);
    }

    private static final int thenDescending$lambda$2$ComparisonsKt__ComparisonsKt(Comparator r2, Comparator r3, Object r4, Object r5) {
        Intrinsics.checkNotNullParameter(r2, "$this_thenDescending");
        Intrinsics.checkNotNullParameter(r3, "$comparator");
        int r0 = r2.compare(r4, r5);
        if (r0 == 0) goto L6;
        return r0;
    L6:
        return r3.compare(r5, r4);
    }
}
