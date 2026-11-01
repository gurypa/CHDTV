package kotlin.collections;

import androidx.exifinterface.media.ExifInterface;
import com.arthenica.ffmpegkit.MediaInformation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequenceScope;
import kotlin.sequences.SequencesKt;

/* JADX INFO: compiled from: SlidingWindow.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000*\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010(\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0000\u001aH\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u00070\u0006\"\u0004\b\u0000\u0010\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\b0\u00062\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0000\u001aD\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u00070\u000e\"\u0004\b\u0000\u0010\b*\b\u0012\u0004\u0012\u0002H\b0\u000e2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0000¨\u0006\u000f"}, m421d2 = {"checkWindowSizeStep", "", MediaInformation.KEY_SIZE, "", "step", "windowedIterator", "", "", ExifInterface.GPS_DIRECTION_TRUE, "iterator", "partialWindows", "", "reuseBuffer", "windowedSequence", "Lkotlin/sequences/Sequence;", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class SlidingWindowKt {

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* JADX INFO: renamed from: kotlin.collections.SlidingWindowKt$windowedIterator$1 */
    /* JADX INFO: compiled from: SlidingWindow.kt */
    @Metadata(m420d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00040\u0003H\u008a@"}, m421d2 = {"<anonymous>", "", ExifInterface.GPS_DIRECTION_TRUE, "Lkotlin/sequences/SequenceScope;", ""}, m422k = 3, m423mv = {1, 9, 0}, m425xi = 48)
    @DebugMetadata(m436c = "kotlin.collections.SlidingWindowKt$windowedIterator$1", m437f = "SlidingWindow.kt", m438i = {0, 0, 0, 2, 2, 3, 3}, m439l = {34, 40, 49, 55, 58}, m440m = "invokeSuspend", m441n = {"$this$iterator", "buffer", "gap", "$this$iterator", "buffer", "$this$iterator", "buffer"}, m442s = {"L$0", "L$1", "I$0", "L$0", "L$1", "L$0", "L$1"})
    static final class C10701<T> extends RestrictedSuspendLambda implements Function2<SequenceScope<? super List<? extends T>>, Continuation<? super Unit>, Object> {
        final /* synthetic */ Iterator<T> $iterator;
        final /* synthetic */ boolean $partialWindows;
        final /* synthetic */ boolean $reuseBuffer;
        final /* synthetic */ int $size;
        final /* synthetic */ int $step;
        int I$0;
        private /* synthetic */ Object L$0;
        Object L$1;
        Object L$2;
        int label;

        /* JADX WARN: Multi-variable type inference failed */
        C10701(int r2, int r3, Iterator<? extends T> r4, boolean r5, boolean r6, Continuation<? super C10701> r7) {
            this.$size = r2;
            this.$step = r3;
            this.$iterator = r4;
            this.$reuseBuffer = r5;
            this.$partialWindows = r6;
            super(2, r7);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object r8, Continuation<?> r9) {
            C10701 r0 = new C10701(this.$size, this.$step, this.$iterator, this.$reuseBuffer, this.$partialWindows, r9);
            r0.L$0 = r8;
            return r0;
        }

        @Override // kotlin.jvm.functions.Function2
        public /* bridge */ /* synthetic */ Object invoke(Object r2, Continuation<? super Unit> r3) {
            return invoke((SequenceScope) r2, r3);
        }

        public final Object invoke(SequenceScope<? super List<? extends T>> r3, Continuation<? super Unit> r4) {
            return ((C10701) create(r3, r4)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:24:0x00ae -> B:25:0x00af). Please report as a decompilation issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:53:0x013b -> B:55:0x013e). Please report as a decompilation issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:66:0x0177 -> B:68:0x017a). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object r12) {
            Object r0 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch(this.label) {
                case 0: goto L11;
                case 1: goto L10;
                case 2: goto L9;
                case 3: goto L8;
                case 4: goto L7;
                case 5: goto L6;
                default: goto L5;
            };
        L6:
            ResultKt.throwOnFailure(r12);
        L77:
            return Unit.INSTANCE;
        L7:
            RingBuffer r3 = (RingBuffer) this.L$1;
            SequenceScope r4 = (SequenceScope) this.L$0;
            ResultKt.throwOnFailure(r12);
            Object r1 = r0;
            C10701<T> r2 = this;
        L68:
            r3.removeFirst(r2.$step);
        L60:
            if (r3.size() <= r2.$step) goto L70;
            if (r2.$reuseBuffer == false) goto L64;
            List r5 = r3;
        L65:
            r2.L$0 = r4;
            r2.L$1 = r3;
            r2.L$2 = null;
            r2.label = 4;
            if (r4.yield(r5, r2) != r1) goto L68;
            return r1;
        L64:
            r5 = new ArrayList(r3);
            goto L65
        L70:
            if (r3.isEmpty() == true) goto L77;
            r2.L$0 = null;
            r2.L$1 = null;
            r2.L$2 = null;
            r2.label = 5;
            if (r4.yield(r3, r2) != r1) goto L77;
            return r1;
        L8:
            C10701<T> r6 = this;
            Iterator<T> r7 = (Iterator) r6.L$2;
            RingBuffer r8 = (RingBuffer) r6.L$1;
            SequenceScope r9 = (SequenceScope) r6.L$0;
            ResultKt.throwOnFailure(r12);
        L55:
            r8.removeFirst(r6.$step);
        L42:
            if (r7.hasNext() == false) goto L57;
            r8.add(r7.next());
            if (r8.isFull() == false) goto L42;
            if (r8.size() >= r6.$size) goto L49;
            r8 = r8.expanded(r6.$size);
            goto L42
        L49:
            if (r6.$reuseBuffer == false) goto L51;
            List r10 = r8;
        L52:
            r6.L$0 = r9;
            r6.L$1 = r8;
            r6.L$2 = r7;
            r6.label = 3;
            if (r9.yield(r10, r6) != r0) goto L55;
            return r0;
        L51:
            r10 = new ArrayList(r8);
            goto L52
        L57:
            if (r6.$partialWindows == false) goto L77;
            C10701<T> r11 = r6;
            r1 = r0;
            r2 = r11;
            r3 = r8;
            r4 = r9;
            goto L60
        L9:
            ResultKt.throwOnFailure(r12);
            goto L77
        L10:
            int r13 = this.I$0;
            Iterator<T> r14 = (Iterator) this.L$2;
            ArrayList r15 = (ArrayList) this.L$1;
            SequenceScope r16 = (SequenceScope) this.L$0;
            ResultKt.throwOnFailure(r12);
            Object r17 = r0;
            C10701<T> r18 = this;
            SequenceScope r19 = r16;
        L26:
            if (r18.$reuseBuffer == false) goto L28;
            r15.clear();
            ArrayList r20 = r15;
        L29:
            int r21 = r13;
        L15:
            if (r14.hasNext() == false) goto L31;
            T r22 = r14.next();
            if (r21 > 0) goto L18;
            r20.add(r22);
            if (r20.size() != r18.$size) goto L15;
            r18.L$0 = r19;
            r18.L$1 = r20;
            r18.L$2 = r14;
            r18.I$0 = r13;
            r18.label = 1;
            if (r19.yield(r20, r18) != r17) goto L24;
            return r17;
        L24:
            r15 = r20;
            goto L26
        L18:
            r21 = r21 - 1;
            goto L15
        L31:
            if (r20.isEmpty() == true) goto L77;
            if (r18.$partialWindows == false) goto L35;
        L36:
            r18.L$0 = null;
            r18.L$1 = null;
            r18.L$2 = null;
            r18.label = 2;
            if (r19.yield(r20, r18) != r17) goto L77;
            return r17;
        L35:
            if (r20.size() != r18.$size) goto L77;
        L28:
            r20 = new ArrayList(r18.$size);
            goto L29
        L11:
            ResultKt.throwOnFailure(r12);
            r6 = this;
            SequenceScope r23 = (SequenceScope) r6.L$0;
            int r24 = RangesKt.coerceAtMost(r6.$size, 1024);
            int r25 = r6.$step - r6.$size;
            if (r25 < 0) goto L40;
            r20 = new ArrayList(r24);
            Iterator<T> r26 = r6.$iterator;
            r17 = r0;
            r18 = r6;
            r19 = r23;
            r13 = r25;
            r21 = 0;
            r14 = r26;
            goto L15
        L40:
            RingBuffer r27 = new RingBuffer(r24);
            r9 = r23;
            r7 = r6.$iterator;
            r8 = r27;
            goto L42
        L5:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    public static final void checkWindowSizeStep(int r4, int r5) {
        if (r4 <= 0) goto L5;
        if (r5 <= 0) goto L5;
        boolean r0 = true;
    L6:
        if (r0 == false) goto L8;
        return;
    L8:
        if (r4 == r5) goto L10;
        String r1 = "Both size " + r4 + " and step " + r5 + " must be greater than zero.";
    L12:
        throw new IllegalArgumentException(r1.toString());
    L10:
        r1 = "size " + r4 + " must be greater than zero.";
    L5:
        r0 = false;
        goto L6
    }

    public static final <T> Iterator<List<T>> windowedIterator(Iterator<? extends T> r8, int r9, int r10, boolean r11, boolean r12) {
        Intrinsics.checkNotNullParameter(r8, "iterator");
        if (r8.hasNext() == true) goto L7;
        return EmptyIterator.INSTANCE;
    L7:
        return SequencesKt.iterator(new C10701(r9, r10, r8, r12, r11, null));
    }

    public static final <T> Sequence<List<T>> windowedSequence(final Sequence<? extends T> r7, final int r8, final int r9, final boolean r10, final boolean r11) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        checkWindowSizeStep(r8, r9);
        return new SlidingWindowKt$windowedSequence$$inlined$Sequence$1(r7, r8, r9, r10, r11);
    }
}
