package kotlin.sequences;

import androidx.exifinterface.media.ExifInterface;
import androidx.media3.extractor.p001ts.TsExtractor;
import com.arthenica.ffmpegkit.StreamInformation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;

/* JADX INFO: compiled from: Sequences.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000L\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0010\u0011\n\u0002\b\u0005\n\u0002\u0010\u001c\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\u001a.\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0014\b\u0004\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00050\u0004H\u0087\bø\u0001\u0000\u001a\u0012\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002\u001ab\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\b0\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\t\"\u0004\b\u0002\u0010\b2\f\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0018\u0010\u000b\u001a\u0014\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\t0\f2\u0018\u0010\u0003\u001a\u0014\u0012\u0004\u0012\u0002H\t\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u00050\u000eH\u0000\u001a&\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\u00102\u000e\u0010\u0011\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u0004\u001a<\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\u00102\u000e\u0010\u0012\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u00042\u0014\u0010\u0011\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u000e\u001a=\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\u00102\b\u0010\u0013\u001a\u0004\u0018\u0001H\u00022\u0014\u0010\u0011\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u000eH\u0007¢\u0006\u0002\u0010\u0014\u001a+\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0012\u0010\u0016\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0017\"\u0002H\u0002¢\u0006\u0002\u0010\u0018\u001a\u001c\u0010\u0019\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0005\u001a\u001c\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001\u001aC\u0010\u001b\u001a\b\u0012\u0004\u0012\u0002H\b0\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\b*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0018\u0010\u0003\u001a\u0014\u0012\u0004\u0012\u0002H\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u00050\u000eH\u0002¢\u0006\u0002\b\u001c\u001a)\u0010\u001b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u001d0\u0001H\u0007¢\u0006\u0002\b\u001e\u001a\"\u0010\u001b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00010\u0001\u001a2\u0010\u001f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0012\u0010 \u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00010\u0004H\u0007\u001a!\u0010!\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0004\u0012\u0002H\u0002\u0018\u00010\u0001H\u0087\b\u001a\u001e\u0010\"\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0001H\u0007\u001a&\u0010\"\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010#\u001a\u00020$H\u0007\u001a@\u0010%\u001a\u001a\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020'\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0'0&\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\b*\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\b0&0\u0001\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006("}, m421d2 = {"Sequence", "Lkotlin/sequences/Sequence;", ExifInterface.GPS_DIRECTION_TRUE, "iterator", "Lkotlin/Function0;", "", "emptySequence", "flatMapIndexed", "R", "C", "source", "transform", "Lkotlin/Function2;", "", "Lkotlin/Function1;", "generateSequence", "", "nextFunction", "seedFunction", "seed", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)Lkotlin/sequences/Sequence;", "sequenceOf", "elements", "", "([Ljava/lang/Object;)Lkotlin/sequences/Sequence;", "asSequence", "constrainOnce", "flatten", "flatten$SequencesKt__SequencesKt", "", "flattenSequenceOfIterable", "ifEmpty", "defaultValue", "orEmpty", "shuffled", "random", "Lkotlin/random/Random;", "unzip", "Lkotlin/Pair;", "", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/sequences/SequencesKt")
class SequencesKt__SequencesKt extends SequencesKt__SequencesJVMKt {

    /* JADX INFO: Add missing generic type declarations: [R] */
    /* JADX INFO: renamed from: kotlin.sequences.SequencesKt__SequencesKt$flatMapIndexed$1 */
    /* JADX INFO: compiled from: Sequences.kt */
    @Metadata(m420d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u0005H\u008a@"}, m421d2 = {"<anonymous>", "", ExifInterface.GPS_DIRECTION_TRUE, "C", "R", "Lkotlin/sequences/SequenceScope;"}, m422k = 3, m423mv = {1, 9, 0}, m425xi = 48)
    @DebugMetadata(m436c = "kotlin.sequences.SequencesKt__SequencesKt$flatMapIndexed$1", m437f = "Sequences.kt", m438i = {0, 0}, m439l = {332}, m440m = "invokeSuspend", m441n = {"$this$sequence", StreamInformation.KEY_INDEX}, m442s = {"L$0", "I$0"})
    static final class C11161<R> extends RestrictedSuspendLambda implements Function2<SequenceScope<? super R>, Continuation<? super Unit>, Object> {
        final /* synthetic */ Function1<C, Iterator<R>> $iterator;
        final /* synthetic */ Sequence<T> $source;
        final /* synthetic */ Function2<Integer, T, C> $transform;
        int I$0;
        private /* synthetic */ Object L$0;
        Object L$1;
        int label;

        /* JADX WARN: Multi-variable type inference failed */
        C11161(Sequence<? extends T> r2, Function2<? super Integer, ? super T, ? extends C> r3, Function1<? super C, ? extends Iterator<? extends R>> r4, Continuation<? super C11161> r5) {
            this.$source = r2;
            this.$transform = r3;
            this.$iterator = r4;
            super(2, r5);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object r5, Continuation<?> r6) {
            C11161 r0 = new C11161(this.$source, this.$transform, this.$iterator, r6);
            r0.L$0 = r5;
            return r0;
        }

        @Override // kotlin.jvm.functions.Function2
        public /* bridge */ /* synthetic */ Object invoke(Object r2, Continuation<? super Unit> r3) {
            return invoke((SequenceScope) r2, r3);
        }

        public final Object invoke(SequenceScope<? super R> r3, Continuation<? super Unit> r4) {
            return ((C11161) create(r3, r4)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Type inference incomplete: some casts might be missing */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object r11) {
            Object r0 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch(this.label) {
                case 0: goto L7;
                case 1: goto L6;
                default: goto L5;
            };
        L6:
            C11161<R> r1 = this;
            int r2 = r1.I$0;
            Iterator r3 = (Iterator) r1.L$1;
            SequenceScope r4 = (SequenceScope) r1.L$0;
            ResultKt.throwOnFailure(r11);
        L9:
            if (r3.hasNext() == false) goto L19;
            Object r5 = r3.next();
            Function2<Integer, T, C> r6 = r1.$transform;
            int r7 = r2 + 1;
            if (r2 >= 0) goto L13;
            CollectionsKt.throwIndexOverflow();
        L13:
            Object r8 = r6.invoke(Boxing.boxInt(r2), (T) r5);
            r1.L$0 = r4;
            r1.L$1 = r3;
            r1.I$0 = r7;
            r1.label = 1;
            if (r4.yieldAll(r1.$iterator.invoke((C) r8), r1) == r0) goto L15;
            r2 = r7;
            goto L9
        L15:
            return r0;
        L19:
            return Unit.INSTANCE;
        L7:
            ResultKt.throwOnFailure(r11);
            r1 = this;
            SequenceScope r9 = (SequenceScope) r1.L$0;
            r4 = r9;
            r2 = 0;
            r3 = r1.$source.iterator();
            goto L9
        L5:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* JADX INFO: renamed from: kotlin.sequences.SequencesKt__SequencesKt$ifEmpty$1 */
    /* JADX INFO: compiled from: Sequences.kt */
    @Metadata(m420d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u008a@"}, m421d2 = {"<anonymous>", "", ExifInterface.GPS_DIRECTION_TRUE, "Lkotlin/sequences/SequenceScope;"}, m422k = 3, m423mv = {1, 9, 0}, m425xi = 48)
    @DebugMetadata(m436c = "kotlin.sequences.SequencesKt__SequencesKt$ifEmpty$1", m437f = "Sequences.kt", m438i = {}, m439l = {69, TsExtractor.TS_SYNC_BYTE}, m440m = "invokeSuspend", m441n = {}, m442s = {})
    static final class C11221<T> extends RestrictedSuspendLambda implements Function2<SequenceScope<? super T>, Continuation<? super Unit>, Object> {
        final /* synthetic */ Function0<Sequence<T>> $defaultValue;
        final /* synthetic */ Sequence<T> $this_ifEmpty;
        private /* synthetic */ Object L$0;
        int label;

        /* JADX WARN: Multi-variable type inference failed */
        C11221(Sequence<? extends T> r2, Function0<? extends Sequence<? extends T>> r3, Continuation<? super C11221> r4) {
            this.$this_ifEmpty = r2;
            this.$defaultValue = r3;
            super(2, r4);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object r4, Continuation<?> r5) {
            C11221 r0 = new C11221(this.$this_ifEmpty, this.$defaultValue, r5);
            r0.L$0 = r4;
            return r0;
        }

        @Override // kotlin.jvm.functions.Function2
        public /* bridge */ /* synthetic */ Object invoke(Object r2, Continuation<? super Unit> r3) {
            return invoke((SequenceScope) r2, r3);
        }

        public final Object invoke(SequenceScope<? super T> r3, Continuation<? super Unit> r4) {
            return ((C11221) create(r3, r4)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object r7) {
            Object r0 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch(this.label) {
                case 0: goto L8;
                case 1: goto L7;
                case 2: goto L6;
                default: goto L5;
            };
        L6:
            ResultKt.throwOnFailure(r7);
        L21:
            return Unit.INSTANCE;
        L7:
            ResultKt.throwOnFailure(r7);
            goto L21
        L8:
            ResultKt.throwOnFailure(r7);
            SequenceScope r2 = (SequenceScope) this.L$0;
            Iterator<? extends T> r3 = this.$this_ifEmpty.iterator();
            if (r3.hasNext() == false) goto L15;
            this.label = 1;
            if (r2.yieldAll(r3, this) != r0) goto L21;
            return r0;
        L15:
            this.label = 2;
            if (r2.yieldAll(this.$defaultValue.invoke(), this) != r0) goto L21;
            return r0;
        L5:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* JADX INFO: renamed from: kotlin.sequences.SequencesKt__SequencesKt$shuffled$1 */
    /* JADX INFO: compiled from: Sequences.kt */
    @Metadata(m420d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u008a@"}, m421d2 = {"<anonymous>", "", ExifInterface.GPS_DIRECTION_TRUE, "Lkotlin/sequences/SequenceScope;"}, m422k = 3, m423mv = {1, 9, 0}, m425xi = 48)
    @DebugMetadata(m436c = "kotlin.sequences.SequencesKt__SequencesKt$shuffled$1", m437f = "Sequences.kt", m438i = {0, 0}, m439l = {145}, m440m = "invokeSuspend", m441n = {"$this$sequence", "buffer"}, m442s = {"L$0", "L$1"})
    static final class C11231<T> extends RestrictedSuspendLambda implements Function2<SequenceScope<? super T>, Continuation<? super Unit>, Object> {
        final /* synthetic */ Random $random;
        final /* synthetic */ Sequence<T> $this_shuffled;
        private /* synthetic */ Object L$0;
        Object L$1;
        int label;

        /* JADX WARN: Multi-variable type inference failed */
        C11231(Sequence<? extends T> r2, Random r3, Continuation<? super C11231> r4) {
            this.$this_shuffled = r2;
            this.$random = r3;
            super(2, r4);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object r4, Continuation<?> r5) {
            C11231 r0 = new C11231(this.$this_shuffled, this.$random, r5);
            r0.L$0 = r4;
            return r0;
        }

        @Override // kotlin.jvm.functions.Function2
        public /* bridge */ /* synthetic */ Object invoke(Object r2, Continuation<? super Unit> r3) {
            return invoke((SequenceScope) r2, r3);
        }

        public final Object invoke(SequenceScope<? super T> r3, Continuation<? super Unit> r4) {
            return ((C11231) create(r3, r4)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object r9) {
            Object r0 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch(this.label) {
                case 0: goto L7;
                case 1: goto L6;
                default: goto L5;
            };
        L6:
            C11231<T> r1 = this;
            List r2 = (List) r1.L$1;
            SequenceScope r3 = (SequenceScope) r1.L$0;
            ResultKt.throwOnFailure(r9);
            SequenceScope r4 = r3;
        L9:
            if (r2.isEmpty() == true) goto L18;
            int r5 = r1.$random.nextInt(r2.size());
            Object r6 = CollectionsKt.removeLast(r2);
            if (r5 >= r2.size()) goto L13;
            r6 = r2.set(r5, r6);
        L13:
            r1.L$0 = r4;
            r1.L$1 = r2;
            r1.label = 1;
            if (r4.yield(r6, r1) != r0) goto L9;
            return r0;
        L18:
            return Unit.INSTANCE;
        L7:
            ResultKt.throwOnFailure(r9);
            r1 = this;
            SequenceScope r7 = (SequenceScope) r1.L$0;
            List r8 = SequencesKt.toMutableList(r1.$this_shuffled);
            r4 = r7;
            r2 = r8;
            goto L9
        L5:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    public SequencesKt__SequencesKt() {
    }

    private static final <T> Sequence<T> Sequence(final Function0<? extends Iterator<? extends T>> r1) {
        Intrinsics.checkNotNullParameter(r1, "iterator");
        return new C11151(r1);
    }

    public static final <T> Sequence<T> asSequence(final Iterator<? extends T> r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return SequencesKt.constrainOnce(new SequencesKt__SequencesKt$asSequence$$inlined$Sequence$1(r1));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> Sequence<T> constrainOnce(Sequence<? extends T> r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        if ((r1 instanceof ConstrainedOnceSequence) == false) goto L6;
        return r1;
    L6:
        return new ConstrainedOnceSequence(r1);
    }

    public static final <T> Sequence<T> emptySequence() {
        return EmptySequence.INSTANCE;
    }

    public static final <T, C, R> Sequence<R> flatMapIndexed(Sequence<? extends T> r2, Function2<? super Integer, ? super T, ? extends C> r3, Function1<? super C, ? extends Iterator<? extends R>> r4) {
        Intrinsics.checkNotNullParameter(r2, "source");
        Intrinsics.checkNotNullParameter(r3, "transform");
        Intrinsics.checkNotNullParameter(r4, "iterator");
        return SequencesKt.sequence(new C11161(r2, r3, r4, null));
    }

    public static final <T> Sequence<T> flatten(Sequence<? extends Sequence<? extends T>> r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return flatten$SequencesKt__SequencesKt(r1, C11171.INSTANCE);
    }

    private static final <T, R> Sequence<R> flatten$SequencesKt__SequencesKt(Sequence<? extends T> r2, Function1<? super T, ? extends Iterator<? extends R>> r3) {
        if ((r2 instanceof TransformingSequence) == false) goto L7;
        return ((TransformingSequence) r2).flatten$kotlin_stdlib(r3);
    L7:
        return new FlatteningSequence(r2, C11193.INSTANCE, r3);
    }

    public static final <T> Sequence<T> flattenSequenceOfIterable(Sequence<? extends Iterable<? extends T>> r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return flatten$SequencesKt__SequencesKt(r1, C11182.INSTANCE);
    }

    public static final <T> Sequence<T> generateSequence(final T r2, Function1<? super T, ? extends T> r3) {
        Intrinsics.checkNotNullParameter(r3, "nextFunction");
        if (r2 != null) goto L6;
        return EmptySequence.INSTANCE;
    L6:
        return new GeneratorSequence(new C11212(r2), r3);
    }

    public static final <T> Sequence<T> generateSequence(final Function0<? extends T> r2) {
        Intrinsics.checkNotNullParameter(r2, "nextFunction");
        return SequencesKt.constrainOnce(new GeneratorSequence(r2, new C11201(r2)));
    }

    public static final <T> Sequence<T> generateSequence(Function0<? extends T> r1, Function1<? super T, ? extends T> r2) {
        Intrinsics.checkNotNullParameter(r1, "seedFunction");
        Intrinsics.checkNotNullParameter(r2, "nextFunction");
        return new GeneratorSequence(r1, r2);
    }

    public static final <T> Sequence<T> ifEmpty(Sequence<? extends T> r2, Function0<? extends Sequence<? extends T>> r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "defaultValue");
        return SequencesKt.sequence(new C11221(r2, r3, null));
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final <T> Sequence<T> orEmpty(Sequence<? extends T> r1) {
        if (r1 != 0) goto L5;
        return SequencesKt.emptySequence();
    L5:
        return r1;
    }

    public static final <T> Sequence<T> sequenceOf(T... r1) {
        Intrinsics.checkNotNullParameter(r1, "elements");
        if (r1.length != 0) goto L5;
        boolean r0 = true;
    L6:
        if (r0 == false) goto L9;
        return SequencesKt.emptySequence();
    L9:
        return ArraysKt.asSequence(r1);
    L5:
        r0 = false;
        goto L6
    }

    public static final <T> Sequence<T> shuffled(Sequence<? extends T> r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return SequencesKt.shuffled(r1, Random.Default);
    }

    public static final <T> Sequence<T> shuffled(Sequence<? extends T> r2, Random r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "random");
        return SequencesKt.sequence(new C11231(r2, r3, null));
    }

    public static final <T, R> Pair<List<T>, List<R>> unzip(Sequence<? extends Pair<? extends T, ? extends R>> r5) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        ArrayList r0 = new ArrayList();
        ArrayList r1 = new ArrayList();
        Iterator<? extends Pair<? extends T, ? extends R>> r2 = r5.iterator();
    L4:
        if (r2.hasNext() == false) goto L7;
        Pair<? extends T, ? extends R> r3 = r2.next();
        r0.add(r3.getFirst());
        r1.add(r3.getSecond());
        goto L4
    L7:
        return TuplesKt.m428to(r0, r1);
    }
}
