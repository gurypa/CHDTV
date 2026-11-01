package kotlin.text;

import com.arthenica.ffmpegkit.StreamInformation;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequenceScope;
import kotlin.sequences.SequencesKt;

/* JADX INFO: compiled from: Regex.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 02\u00060\u0001j\u0002`\u0002:\u000201B\u000f\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005B\u0017\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bB\u001d\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00070\n¢\u0006\u0002\u0010\u000bB\u000f\b\u0001\u0012\u0006\u0010\f\u001a\u00020\r¢\u0006\u0002\u0010\u000eJ\u000e\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017J\u001a\u0010\u0018\u001a\u0004\u0018\u00010\u00192\u0006\u0010\u0016\u001a\u00020\u00172\b\b\u0002\u0010\u001a\u001a\u00020\u001bJ\u001e\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00190\u001d2\u0006\u0010\u0016\u001a\u00020\u00172\b\b\u0002\u0010\u001a\u001a\u00020\u001bJ\u001a\u0010\u001e\u001a\u0004\u0018\u00010\u00192\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u001f\u001a\u00020\u001bH\u0007J\u0010\u0010 \u001a\u0004\u0018\u00010\u00192\u0006\u0010\u0016\u001a\u00020\u0017J\u0011\u0010!\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0086\u0004J\u0018\u0010\"\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u001f\u001a\u00020\u001bH\u0007J\"\u0010#\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u00172\u0012\u0010$\u001a\u000e\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u00170%J\u0016\u0010#\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010&\u001a\u00020\u0004J\u0016\u0010'\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010&\u001a\u00020\u0004J\u001e\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00040)2\u0006\u0010\u0016\u001a\u00020\u00172\b\b\u0002\u0010*\u001a\u00020\u001bJ \u0010+\u001a\b\u0012\u0004\u0012\u00020\u00040\u001d2\u0006\u0010\u0016\u001a\u00020\u00172\b\b\u0002\u0010*\u001a\u00020\u001bH\u0007J\u0006\u0010,\u001a\u00020\rJ\b\u0010-\u001a\u00020\u0004H\u0016J\b\u0010.\u001a\u00020/H\u0002R\u0016\u0010\u000f\u001a\n\u0012\u0004\u0012\u00020\u0007\u0018\u00010\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00070\n8F¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0003\u001a\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013¨\u00062"}, m421d2 = {"Lkotlin/text/Regex;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "pattern", "", "(Ljava/lang/String;)V", "option", "Lkotlin/text/RegexOption;", "(Ljava/lang/String;Lkotlin/text/RegexOption;)V", "options", "", "(Ljava/lang/String;Ljava/util/Set;)V", "nativePattern", "Ljava/util/regex/Pattern;", "(Ljava/util/regex/Pattern;)V", "_options", "getOptions", "()Ljava/util/Set;", "getPattern", "()Ljava/lang/String;", "containsMatchIn", "", "input", "", "find", "Lkotlin/text/MatchResult;", "startIndex", "", "findAll", "Lkotlin/sequences/Sequence;", "matchAt", StreamInformation.KEY_INDEX, "matchEntire", "matches", "matchesAt", "replace", "transform", "Lkotlin/Function1;", "replacement", "replaceFirst", "split", "", "limit", "splitToSequence", "toPattern", "toString", "writeReplace", "", "Companion", "Serialized", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class Regex implements Serializable {
    public static final Companion Companion = null;
    private Set<? extends RegexOption> _options;
    private final Pattern nativePattern;

    /* JADX INFO: compiled from: Regex.kt */
    @Metadata(m420d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0002J\u000e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007J\u000e\u0010\t\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007J\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\b\u001a\u00020\u0007¨\u0006\f"}, m421d2 = {"Lkotlin/text/Regex$Companion;", "", "()V", "ensureUnicodeCase", "", "flags", "escape", "", "literal", "escapeReplacement", "fromLiteral", "Lkotlin/text/Regex;", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker r1) {
            this();
        }

        public static final /* synthetic */ int access$ensureUnicodeCase(Companion r1, int r2) {
            return r1.ensureUnicodeCase(r2);
        }

        private final int ensureUnicodeCase(int r2) {
            if ((r2 & 2) == 0) goto L6;
            return r2 | 64;
        L6:
            return r2;
        }

        public final String escape(String r3) {
            Intrinsics.checkNotNullParameter(r3, "literal");
            String r0 = Pattern.quote(r3);
            Intrinsics.checkNotNullExpressionValue(r0, "quote(...)");
            return r0;
        }

        public final String escapeReplacement(String r3) {
            Intrinsics.checkNotNullParameter(r3, "literal");
            String r0 = Matcher.quoteReplacement(r3);
            Intrinsics.checkNotNullExpressionValue(r0, "quoteReplacement(...)");
            return r0;
        }

        public final Regex fromLiteral(String r3) {
            Intrinsics.checkNotNullParameter(r3, "literal");
            return new Regex(r3, RegexOption.LITERAL);
        }
    }

    /* JADX INFO: compiled from: Regex.kt */
    @Metadata(m420d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0002\u0018\u0000 \u000e2\u00060\u0001j\u0002`\u0002:\u0001\u000eB\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\b\u0010\f\u001a\u00020\rH\u0002R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u000f"}, m421d2 = {"Lkotlin/text/Regex$Serialized;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "pattern", "", "flags", "", "(Ljava/lang/String;I)V", "getFlags", "()I", "getPattern", "()Ljava/lang/String;", "readResolve", "", "Companion", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    private static final class Serialized implements Serializable {
        public static final Companion Companion = null;
        private static final long serialVersionUID = 0;
        private final int flags;
        private final String pattern;

        /* JADX INFO: compiled from: Regex.kt */
        @Metadata(m420d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0005"}, m421d2 = {"Lkotlin/text/Regex$Serialized$Companion;", "", "()V", "serialVersionUID", "", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
        public static final class Companion {
            private Companion() {
            }

            public /* synthetic */ Companion(DefaultConstructorMarker r1) {
                this();
            }
        }

        static {
            Companion = new Companion(null);
        }

        public Serialized(String r2, int r3) {
            Intrinsics.checkNotNullParameter(r2, "pattern");
            this.pattern = r2;
            this.flags = r3;
        }

        private final Object readResolve() {
            Pattern r1 = Pattern.compile(this.pattern, this.flags);
            Intrinsics.checkNotNullExpressionValue(r1, "compile(...)");
            return new Regex(r1);
        }

        public final int getFlags() {
            return this.flags;
        }

        public final String getPattern() {
            return this.pattern;
        }
    }

    /* JADX INFO: renamed from: kotlin.text.Regex$findAll$2 */
    /* JADX INFO: compiled from: Regex.kt */
    @Metadata(m422k = 3, m423mv = {1, 9, 0}, m425xi = 48)
    /* synthetic */ class C11572 extends FunctionReferenceImpl implements Function1<MatchResult, MatchResult> {
        public static final C11572 INSTANCE = null;

        static {
            INSTANCE = new C11572();
        }

        C11572() {
            super(1, MatchResult.class, "next", "next()Lkotlin/text/MatchResult;", 0);
        }

        @Override // kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ MatchResult invoke(MatchResult r2) {
            return invoke2(r2);
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final MatchResult invoke2(MatchResult r2) {
            Intrinsics.checkNotNullParameter(r2, "p0");
            return r2.next();
        }
    }

    /* JADX INFO: renamed from: kotlin.text.Regex$splitToSequence$1 */
    /* JADX INFO: compiled from: Regex.kt */
    @Metadata(m420d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u008a@"}, m421d2 = {"<anonymous>", "", "Lkotlin/sequences/SequenceScope;", ""}, m422k = 3, m423mv = {1, 9, 0}, m425xi = 48)
    @DebugMetadata(m436c = "kotlin.text.Regex$splitToSequence$1", m437f = "Regex.kt", m438i = {1, 1, 1}, m439l = {275, 283, 287}, m440m = "invokeSuspend", m441n = {"$this$sequence", "matcher", "splitCount"}, m442s = {"L$0", "L$1", "I$0"})
    static final class C11581 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super String>, Continuation<? super Unit>, Object> {
        final /* synthetic */ CharSequence $input;
        final /* synthetic */ int $limit;
        int I$0;
        private /* synthetic */ Object L$0;
        Object L$1;
        int label;
        final /* synthetic */ Regex this$0;

        C11581(Regex r2, CharSequence r3, int r4, Continuation<? super C11581> r5) {
            this.this$0 = r2;
            this.$input = r3;
            this.$limit = r4;
            super(2, r5);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object r5, Continuation<?> r6) {
            C11581 r0 = new C11581(this.this$0, this.$input, this.$limit, r6);
            r0.L$0 = r5;
            return r0;
        }

        @Override // kotlin.jvm.functions.Function2
        public /* bridge */ /* synthetic */ Object invoke(SequenceScope<? super String> r2, Continuation<? super Unit> r3) {
            return invoke2(r2, r3);
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final Object invoke2(SequenceScope<? super String> r3, Continuation<? super Unit> r4) {
            return ((C11581) create(r3, r4)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:16:0x0070 -> B:18:0x0073). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object r11) {
            Object r0 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch(this.label) {
                case 0: goto L9;
                case 1: goto L8;
                case 2: goto L7;
                case 3: goto L6;
                default: goto L5;
            };
        L6:
            ResultKt.throwOnFailure(r11);
        L28:
            return Unit.INSTANCE;
        L7:
            C11581 r1 = this;
            int r3 = r1.I$0;
            Matcher r4 = (Matcher) r1.L$1;
            SequenceScope r5 = (SequenceScope) r1.L$0;
            ResultKt.throwOnFailure(r11);
        L18:
            int r6 = r4.end();
            r3 = r3 + 1;
            if (r3 != (r1.$limit - 1)) goto L21;
        L23:
            r1.L$0 = null;
            r1.L$1 = null;
            r1.label = 3;
            if (r5.yield(r1.$input.subSequence(r6, r1.$input.length()).toString(), r1) != r0) goto L28;
            return r0;
        L21:
            if (r4.find() == false) goto L23;
        L15:
            r1.L$0 = r5;
            r1.L$1 = r4;
            r1.I$0 = r3;
            r1.label = 2;
            if (r5.yield(r1.$input.subSequence(r6, r4.start()).toString(), r1) != r0) goto L18;
            return r0;
        L8:
            ResultKt.throwOnFailure(r11);
        L34:
            return Unit.INSTANCE;
        L9:
            ResultKt.throwOnFailure(r11);
            r1 = this;
            SequenceScope r7 = (SequenceScope) r1.L$0;
            r4 = Regex.access$getNativePattern$p(r1.this$0).matcher(r1.$input);
            if (r1.$limit != 1) goto L12;
        L29:
            r1.label = 1;
            if (r7.yield(r1.$input.toString(), r1) != r0) goto L34;
            return r0;
        L12:
            if (r4.find() == false) goto L29;
            r5 = r7;
            r3 = 0;
            r6 = 0;
            goto L15
        L5:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    static {
        Companion = new Companion(null);
    }

    public Regex(String r3) {
        Intrinsics.checkNotNullParameter(r3, "pattern");
        Pattern r0 = Pattern.compile(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "compile(...)");
        this(r0);
    }

    public Regex(String r3, Set<? extends RegexOption> r4) {
        Intrinsics.checkNotNullParameter(r3, "pattern");
        Intrinsics.checkNotNullParameter(r4, "options");
        Pattern r0 = Pattern.compile(r3, Companion.access$ensureUnicodeCase(Companion, RegexKt.access$toInt(r4)));
        Intrinsics.checkNotNullExpressionValue(r0, "compile(...)");
        this(r0);
    }

    public Regex(String r3, RegexOption r4) {
        Intrinsics.checkNotNullParameter(r3, "pattern");
        Intrinsics.checkNotNullParameter(r4, "option");
        Pattern r0 = Pattern.compile(r3, Companion.access$ensureUnicodeCase(Companion, r4.getValue()));
        Intrinsics.checkNotNullExpressionValue(r0, "compile(...)");
        this(r0);
    }

    public Regex(Pattern r2) {
        Intrinsics.checkNotNullParameter(r2, "nativePattern");
        this.nativePattern = r2;
    }

    public static final /* synthetic */ Pattern access$getNativePattern$p(Regex r1) {
        return r1.nativePattern;
    }

    public static /* synthetic */ MatchResult find$default(Regex r0, CharSequence r1, int r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L6;
        r2 = 0;
    L6:
        return r0.find(r1, r2);
    }

    public static /* synthetic */ Sequence findAll$default(Regex r0, CharSequence r1, int r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L6;
        r2 = 0;
    L6:
        return r0.findAll(r1, r2);
    }

    public static /* synthetic */ List split$default(Regex r0, CharSequence r1, int r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L6;
        r2 = 0;
    L6:
        return r0.split(r1, r2);
    }

    public static /* synthetic */ Sequence splitToSequence$default(Regex r0, CharSequence r1, int r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L6;
        r2 = 0;
    L6:
        return r0.splitToSequence(r1, r2);
    }

    private final Object writeReplace() {
        String r1 = this.nativePattern.pattern();
        Intrinsics.checkNotNullExpressionValue(r1, "pattern(...)");
        return new Serialized(r1, this.nativePattern.flags());
    }

    public final boolean containsMatchIn(CharSequence r2) {
        Intrinsics.checkNotNullParameter(r2, "input");
        return this.nativePattern.matcher(r2).find();
    }

    public final MatchResult find(CharSequence r3, int r4) {
        Intrinsics.checkNotNullParameter(r3, "input");
        Matcher r0 = this.nativePattern.matcher(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "matcher(...)");
        return RegexKt.access$findNext(r0, r4, r3);
    }

    public final Sequence<MatchResult> findAll(final CharSequence r4, final int r5) {
        Intrinsics.checkNotNullParameter(r4, "input");
        if (r5 < 0) goto L9;
        if (r5 > r4.length()) goto L9;
        return SequencesKt.generateSequence(new C11561(this, r4, r5), C11572.INSTANCE);
    L9:
        throw new IndexOutOfBoundsException("Start index out of bounds: " + r5 + ", input length: " + r4.length());
    }

    public final Set<RegexOption> getOptions() {
        Set r0 = this._options;
        if (r0 != null) goto L6;
        final int r1 = this.nativePattern.flags();
        EnumSet r2 = EnumSet.allOf(RegexOption.class);
        Intrinsics.checkNotNull(r2);
        CollectionsKt.retainAll(r2, new Regex$special$$inlined$fromInt$1(r1));
        Set<RegexOption> r3 = Collections.unmodifiableSet(r2);
        Intrinsics.checkNotNullExpressionValue(r3, "unmodifiableSet(...)");
        this._options = r3;
        return r3;
    L6:
        return r0;
    }

    public final String getPattern() {
        String r0 = this.nativePattern.pattern();
        Intrinsics.checkNotNullExpressionValue(r0, "pattern(...)");
        return r0;
    }

    public final MatchResult matchAt(CharSequence r4, int r5) {
        Intrinsics.checkNotNullParameter(r4, "input");
        Matcher r0 = this.nativePattern.matcher(r4).useAnchoringBounds(false).useTransparentBounds(true).region(r5, r4.length());
        if (r0.lookingAt() == false) goto L5;
        Intrinsics.checkNotNull(r0);
        MatcherMatchResult r2 = new MatcherMatchResult(r0, r4);
    L7:
        return r2;
    L5:
        r2 = null;
        goto L7
    }

    public final MatchResult matchEntire(CharSequence r3) {
        Intrinsics.checkNotNullParameter(r3, "input");
        Matcher r0 = this.nativePattern.matcher(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "matcher(...)");
        return RegexKt.access$matchEntire(r0, r3);
    }

    public final boolean matches(CharSequence r2) {
        Intrinsics.checkNotNullParameter(r2, "input");
        return this.nativePattern.matcher(r2).matches();
    }

    public final boolean matchesAt(CharSequence r3, int r4) {
        Intrinsics.checkNotNullParameter(r3, "input");
        return this.nativePattern.matcher(r3).useAnchoringBounds(false).useTransparentBounds(true).region(r4, r3.length()).lookingAt();
    }

    public final String replace(CharSequence r3, String r4) {
        Intrinsics.checkNotNullParameter(r3, "input");
        Intrinsics.checkNotNullParameter(r4, "replacement");
        String r0 = this.nativePattern.matcher(r3).replaceAll(r4);
        Intrinsics.checkNotNullExpressionValue(r0, "replaceAll(...)");
        return r0;
    }

    public final String replace(CharSequence r7, Function1<? super MatchResult, ? extends CharSequence> r8) {
        Intrinsics.checkNotNullParameter(r7, "input");
        Intrinsics.checkNotNullParameter(r8, "transform");
        MatchResult r0 = find$default(this, r7, 0, 2, null);
        if (r0 == null) goto L5;
        int r1 = 0;
        int r2 = r7.length();
        StringBuilder r3 = new StringBuilder(r2);
    L7:
        MatchResult r4 = r0;
        r3.append(r7, r1, r4.getRange().getStart().intValue());
        r3.append(r8.invoke(r4));
        r1 = r4.getRange().getEndInclusive().intValue() + 1;
        r0 = r4.next();
        if (r1 >= r2) goto L10;
        if (r0 != null) goto L7;
    L10:
        if (r1 >= r2) goto L12;
        r3.append(r7, r1, r2);
    L12:
        String r5 = r3.toString();
        Intrinsics.checkNotNullExpressionValue(r5, "toString(...)");
        return r5;
    L5:
        return r7.toString();
    }

    public final String replaceFirst(CharSequence r3, String r4) {
        Intrinsics.checkNotNullParameter(r3, "input");
        Intrinsics.checkNotNullParameter(r4, "replacement");
        String r0 = this.nativePattern.matcher(r3).replaceFirst(r4);
        Intrinsics.checkNotNullExpressionValue(r0, "replaceFirst(...)");
        return r0;
    }

    public final List<String> split(CharSequence r6, int r7) {
        Intrinsics.checkNotNullParameter(r6, "input");
        StringsKt.requireNonNegativeLimit(r7);
        Matcher r0 = this.nativePattern.matcher(r6);
        if (r7 == 1) goto L20;
        if (r0.find() == false) goto L20;
        int r2 = 10;
        if (r7 <= 0) goto L10;
        r2 = RangesKt.coerceAtMost(r7, 10);
    L10:
        ArrayList r1 = new ArrayList(r2);
        int r3 = 0;
        int r4 = r7 - 1;
    L11:
        r1.add(r6.subSequence(r3, r0.start()).toString());
        r3 = r0.end();
        if (r4 < 0) goto L16;
        if (r1.size() != r4) goto L16;
    L17:
        r1.add(r6.subSequence(r3, r6.length()).toString());
        return r1;
    L16:
        if (r0.find() == true) goto L11;
    L20:
        return CollectionsKt.listOf(r6.toString());
    }

    public final Sequence<String> splitToSequence(CharSequence r3, int r4) {
        Intrinsics.checkNotNullParameter(r3, "input");
        StringsKt.requireNonNegativeLimit(r4);
        return SequencesKt.sequence(new C11581(this, r3, r4, null));
    }

    public final Pattern toPattern() {
        return this.nativePattern;
    }

    public String toString() {
        String r0 = this.nativePattern.toString();
        Intrinsics.checkNotNullExpressionValue(r0, "toString(...)");
        return r0;
    }
}
