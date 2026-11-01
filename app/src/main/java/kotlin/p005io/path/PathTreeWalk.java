package kotlin.p005io.path;

import java.nio.file.FileSystemLoopException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.ArrayDeque;
import kotlin.collections.ArraysKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequenceScope;
import kotlin.sequences.SequencesKt;

/* JADX INFO: compiled from: PathTreeWalk.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010(\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u001d\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u000e\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00060\u0005¢\u0006\u0002\u0010\u0007J\u000e\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00020\u0015H\u0002J\u000e\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00020\u0015H\u0002J\u000f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00020\u0015H\u0096\u0002JB\u0010\u0018\u001a\u00020\u0019*\b\u0012\u0004\u0012\u00020\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001e2\u0018\u0010\u001f\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001c0!\u0012\u0004\u0012\u00020\u00190 H\u0082H¢\u0006\u0002\u0010\"R\u0014\u0010\b\u001a\u00020\t8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0014\u0010\f\u001a\u00020\t8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\u000bR\u0014\u0010\u000e\u001a\u00020\t8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000bR\u001a\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\u00058BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0012R\u0018\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00060\u0005X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0013R\u000e\u0010\u0003\u001a\u00020\u0002X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006#"}, m421d2 = {"Lkotlin/io/path/PathTreeWalk;", "Lkotlin/sequences/Sequence;", "Ljava/nio/file/Path;", "start", "options", "", "Lkotlin/io/path/PathWalkOption;", "(Ljava/nio/file/Path;[Lkotlin/io/path/PathWalkOption;)V", "followLinks", "", "getFollowLinks", "()Z", "includeDirectories", "getIncludeDirectories", "isBFS", "linkOptions", "Ljava/nio/file/LinkOption;", "getLinkOptions", "()[Ljava/nio/file/LinkOption;", "[Lkotlin/io/path/PathWalkOption;", "bfsIterator", "", "dfsIterator", "iterator", "yieldIfNeeded", "", "Lkotlin/sequences/SequenceScope;", "node", "Lkotlin/io/path/PathNode;", "entriesReader", "Lkotlin/io/path/DirectoryEntriesReader;", "entriesAction", "Lkotlin/Function1;", "", "(Lkotlin/sequences/SequenceScope;Lkotlin/io/path/PathNode;Lkotlin/io/path/DirectoryEntriesReader;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlin-stdlib-jdk7"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class PathTreeWalk implements Sequence<Path> {
    private final PathWalkOption[] options;
    private final Path start;

    /* JADX INFO: renamed from: kotlin.io.path.PathTreeWalk$bfsIterator$1 */
    /* JADX INFO: compiled from: PathTreeWalk.kt */
    @Metadata(m420d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u008a@"}, m421d2 = {"<anonymous>", "", "Lkotlin/sequences/SequenceScope;", "Ljava/nio/file/Path;"}, m422k = 3, m423mv = {1, 9, 0}, m425xi = 48)
    @DebugMetadata(m436c = "kotlin.io.path.PathTreeWalk$bfsIterator$1", m437f = "PathTreeWalk.kt", m438i = {0, 0, 0, 0, 0, 0, 1, 1, 1}, m439l = {184, 190}, m440m = "invokeSuspend", m441n = {"$this$iterator", "queue", "entriesReader", "pathNode", "this_$iv", "path$iv", "$this$iterator", "queue", "entriesReader"}, m442s = {"L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$0", "L$1", "L$2"})
    static final class C10991 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super Path>, Continuation<? super Unit>, Object> {
        private /* synthetic */ Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        int label;
        final /* synthetic */ PathTreeWalk this$0;

        C10991(PathTreeWalk r2, Continuation<? super C10991> r3) {
            this.this$0 = r2;
            super(2, r3);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object r3, Continuation<?> r4) {
            C10991 r0 = new C10991(this.this$0, r4);
            r0.L$0 = r3;
            return r0;
        }

        @Override // kotlin.jvm.functions.Function2
        public /* bridge */ /* synthetic */ Object invoke(SequenceScope<? super Path> r2, Continuation<? super Unit> r3) {
            return invoke2(r2, r3);
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final Object invoke2(SequenceScope<? super Path> r3, Continuation<? super Unit> r4) {
            return ((C10991) create(r3, r4)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:23:0x00f8 -> B:9:0x007e). Please report as a decompilation issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:24:0x00fa -> B:9:0x007e). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object r14) {
            Object r0 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch(this.label) {
                case 0: goto L8;
                case 1: goto L7;
                case 2: goto L6;
                default: goto L5;
            };
        L6:
            C10991 r1 = this;
            DirectoryEntriesReader r4 = (DirectoryEntriesReader) r1.L$2;
            ArrayDeque r5 = (ArrayDeque) r1.L$1;
            SequenceScope r6 = (SequenceScope) r1.L$0;
            ResultKt.throwOnFailure(r14);
        L10:
            if (r5.isEmpty() == true) goto L35;
            PathNode r3 = (PathNode) r5.removeFirst();
            PathTreeWalk r7 = r1.this$0;
            SequenceScope r8 = r6;
            Path r10 = r3.getPath();
            LinkOption[] r11 = PathTreeWalk.access$getLinkOptions(r7);
            LinkOption[] r12 = (LinkOption[]) Arrays.copyOf(r11, r11.length);
            if (Files.isDirectory(r10, (LinkOption[]) Arrays.copyOf(r12, r12.length)) == true) goto L14;
            if (Files.exists(r10, (LinkOption[]) Arrays.copyOf(new LinkOption[]{LinkOption.NOFOLLOW_LINKS}, 1)) == false) goto L10;
            r1.L$0 = r6;
            r1.L$1 = r5;
            r1.L$2 = r4;
            r1.L$3 = null;
            r1.L$4 = null;
            r1.L$5 = null;
            r1.label = 2;
            if (r8.yield(r10, r1) != r0) goto L10;
            return r0;
        L14:
            if (PathTreeWalkKt.access$createsCycle(r3) == true) goto L26;
            if (PathTreeWalk.access$getIncludeDirectories(r7) == false) goto L22;
            r1.L$0 = r6;
            r1.L$1 = r5;
            r1.L$2 = r4;
            r1.L$3 = r3;
            r1.L$4 = r7;
            r1.L$5 = r10;
            r1.label = 1;
            if (r8.yield(r10, r1) != r0) goto L20;
            return r0;
        L20:
            SequenceScope r9 = r6;
            PathNode r13 = r3;
            boolean r15 = false;
            SequenceScope r16 = r9;
            ArrayDeque r17 = r5;
            PathTreeWalk r18 = r7;
            DirectoryEntriesReader r19 = r4;
            Path r20 = r10;
        L21:
            r3 = r13;
            r6 = r16;
            r10 = r20;
            r4 = r19;
            r7 = r18;
            r5 = r17;
        L22:
            LinkOption[] r21 = PathTreeWalk.access$getLinkOptions(r7);
            LinkOption[] r22 = (LinkOption[]) Arrays.copyOf(r21, r21.length);
            if (Files.isDirectory(r10, (LinkOption[]) Arrays.copyOf(r22, r22.length)) == false) goto L10;
            r5.addAll(r4.readEntries(r3));
            goto L10
        L26:
            throw new FileSystemLoopException(r10.toString());
        L35:
            return Unit.INSTANCE;
        L7:
            r1 = this;
            r15 = false;
            r20 = (Path) r1.L$5;
            r18 = (PathTreeWalk) r1.L$4;
            r13 = (PathNode) r1.L$3;
            r19 = (DirectoryEntriesReader) r1.L$2;
            r17 = (ArrayDeque) r1.L$1;
            r16 = (SequenceScope) r1.L$0;
            ResultKt.throwOnFailure(r14);
            goto L21
        L8:
            ResultKt.throwOnFailure(r14);
            r1 = this;
            SequenceScope r23 = (SequenceScope) r1.L$0;
            ArrayDeque r24 = new ArrayDeque();
            DirectoryEntriesReader r25 = new DirectoryEntriesReader(PathTreeWalk.access$getFollowLinks(r1.this$0));
            r24.addLast(new PathNode(PathTreeWalk.access$getStart$p(r1.this$0), PathTreeWalkKt.access$keyOf(PathTreeWalk.access$getStart$p(r1.this$0), PathTreeWalk.access$getLinkOptions(r1.this$0)), null));
            r5 = r24;
            r4 = r25;
            r6 = r23;
            goto L10
        L5:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: renamed from: kotlin.io.path.PathTreeWalk$dfsIterator$1 */
    /* JADX INFO: compiled from: PathTreeWalk.kt */
    @Metadata(m420d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u008a@"}, m421d2 = {"<anonymous>", "", "Lkotlin/sequences/SequenceScope;", "Ljava/nio/file/Path;"}, m422k = 3, m423mv = {1, 9, 0}, m425xi = 48)
    @DebugMetadata(m436c = "kotlin.io.path.PathTreeWalk$dfsIterator$1", m437f = "PathTreeWalk.kt", m438i = {0, 0, 0, 0, 0, 0, 1, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3}, m439l = {184, 190, 199, 205}, m440m = "invokeSuspend", m441n = {"$this$iterator", "stack", "entriesReader", "startNode", "this_$iv", "path$iv", "$this$iterator", "stack", "entriesReader", "$this$iterator", "stack", "entriesReader", "pathNode", "this_$iv", "path$iv", "$this$iterator", "stack", "entriesReader"}, m442s = {"L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$0", "L$1", "L$2", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$0", "L$1", "L$2"})
    static final class C11001 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super Path>, Continuation<? super Unit>, Object> {
        private /* synthetic */ Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        int label;
        final /* synthetic */ PathTreeWalk this$0;

        C11001(PathTreeWalk r2, Continuation<? super C11001> r3) {
            this.this$0 = r2;
            super(2, r3);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object r3, Continuation<?> r4) {
            C11001 r0 = new C11001(this.this$0, r4);
            r0.L$0 = r3;
            return r0;
        }

        @Override // kotlin.jvm.functions.Function2
        public /* bridge */ /* synthetic */ Object invoke(SequenceScope<? super Path> r2, Continuation<? super Unit> r3) {
            return invoke2(r2, r3);
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final Object invoke2(SequenceScope<? super Path> r3, Continuation<? super Unit> r4) {
            return ((C11001) create(r3, r4)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:49:0x0203 -> B:33:0x0176). Please report as a decompilation issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:50:0x0205 -> B:33:0x0176). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object r18) {
            Object r0 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch(this.label) {
                case 0: goto L10;
                case 1: goto L9;
                case 2: goto L8;
                case 3: goto L7;
                case 4: goto L6;
                default: goto L5;
            };
        L6:
            C11001 r2 = this;
            DirectoryEntriesReader r8 = (DirectoryEntriesReader) r2.L$2;
            ArrayDeque r9 = (ArrayDeque) r2.L$1;
            SequenceScope r10 = (SequenceScope) r2.L$0;
            ResultKt.throwOnFailure(r18);
            char r16 = 0;
        L34:
            if (r9.isEmpty() == true) goto L62;
            Iterator<PathNode> r7 = ((PathNode) r9.last()).getContentIterator();
            Intrinsics.checkNotNull(r7);
            if (r7.hasNext() == true) goto L37;
            r9.removeLast();
            goto L34
        L37:
            PathNode r3 = r7.next();
            PathTreeWalk r11 = r2.this$0;
            SequenceScope r12 = r10;
            Path r13 = r3.getPath();
            LinkOption[] r14 = PathTreeWalk.access$getLinkOptions(r11);
            LinkOption[] r15 = (LinkOption[]) Arrays.copyOf(r14, r14.length);
            if (Files.isDirectory(r13, (LinkOption[]) Arrays.copyOf(r15, r15.length)) == true) goto L40;
            LinkOption[] r4 = new LinkOption[1];
            r4[r16] = LinkOption.NOFOLLOW_LINKS;
            if (Files.exists(r13, (LinkOption[]) Arrays.copyOf(r4, 1)) == false) goto L34;
            r2.L$0 = r10;
            r2.L$1 = r9;
            r2.L$2 = r8;
            r2.L$3 = null;
            r2.L$4 = null;
            r2.L$5 = null;
            r2.label = 4;
            if (r12.yield(r13, r2) != r0) goto L34;
            return r0;
        L40:
            if (PathTreeWalkKt.access$createsCycle(r3) == true) goto L52;
            if (PathTreeWalk.access$getIncludeDirectories(r11) == false) goto L48;
            r2.L$0 = r10;
            r2.L$1 = r9;
            r2.L$2 = r8;
            r2.L$3 = r3;
            r2.L$4 = r11;
            r2.L$5 = r13;
            r2.label = 3;
            if (r12.yield(r13, r2) != r0) goto L46;
            return r0;
        L46:
            ArrayDeque r17 = r9;
            PathTreeWalk r19 = r11;
            boolean r20 = false;
            ArrayDeque r110 = r17;
            DirectoryEntriesReader r111 = r8;
            Path r21 = r13;
            SequenceScope r112 = r10;
            PathNode r113 = r3;
        L47:
            r11 = r19;
            r9 = r110;
            r3 = r113;
            r10 = r112;
            r13 = r21;
            r8 = r111;
        L48:
            LinkOption[] r114 = PathTreeWalk.access$getLinkOptions(r11);
            LinkOption[] r22 = (LinkOption[]) Arrays.copyOf(r114, r114.length);
            if (Files.isDirectory(r13, (LinkOption[]) Arrays.copyOf(r22, r22.length)) == false) goto L34;
            r3.setContentIterator(r8.readEntries(r3).iterator());
            r9.addLast(r3);
            goto L34
        L52:
            throw new FileSystemLoopException(r13.toString());
        L62:
            return Unit.INSTANCE;
        L7:
            r2 = this;
            r20 = false;
            r21 = (Path) r2.L$5;
            r19 = (PathTreeWalk) r2.L$4;
            r113 = (PathNode) r2.L$3;
            r111 = (DirectoryEntriesReader) r2.L$2;
            r110 = (ArrayDeque) r2.L$1;
            r112 = (SequenceScope) r2.L$0;
            ResultKt.throwOnFailure(r18);
            r16 = 0;
            goto L47
        L8:
            r2 = this;
            r8 = (DirectoryEntriesReader) r2.L$2;
            r9 = (ArrayDeque) r2.L$1;
            r10 = (SequenceScope) r2.L$0;
            ResultKt.throwOnFailure(r18);
            r16 = 0;
            goto L34
        L9:
            r2 = this;
            boolean r23 = false;
            Path r24 = (Path) r2.L$5;
            PathTreeWalk r25 = (PathTreeWalk) r2.L$4;
            PathNode r115 = (PathNode) r2.L$3;
            DirectoryEntriesReader r116 = (DirectoryEntriesReader) r2.L$2;
            ArrayDeque r117 = (ArrayDeque) r2.L$1;
            SequenceScope r118 = (SequenceScope) r2.L$0;
            ResultKt.throwOnFailure(r18);
            r16 = 0;
        L20:
            PathNode r26 = r115;
            r10 = r118;
            Path r119 = r24;
            r8 = r116;
            PathTreeWalk r120 = r25;
            r9 = r117;
        L21:
            LinkOption[] r5 = PathTreeWalk.access$getLinkOptions(r120);
            LinkOption[] r6 = (LinkOption[]) Arrays.copyOf(r5, r5.length);
            if (Files.isDirectory(r119, (LinkOption[]) Arrays.copyOf(r6, r6.length)) == false) goto L34;
            r26.setContentIterator(r8.readEntries(r26).iterator());
            r9.addLast(r26);
            goto L34
        L10:
            ResultKt.throwOnFailure(r18);
            r2 = this;
            r10 = (SequenceScope) r2.L$0;
            r9 = new ArrayDeque();
            r8 = new DirectoryEntriesReader(PathTreeWalk.access$getFollowLinks(r2.this$0));
            r26 = new PathNode(PathTreeWalk.access$getStart$p(r2.this$0), PathTreeWalkKt.access$keyOf(PathTreeWalk.access$getStart$p(r2.this$0), PathTreeWalk.access$getLinkOptions(r2.this$0)), null);
            r120 = r2.this$0;
            r119 = r26.getPath();
            LinkOption[] r121 = PathTreeWalk.access$getLinkOptions(r120);
            r16 = 0;
            LinkOption[] r27 = (LinkOption[]) Arrays.copyOf(r121, r121.length);
            if (Files.isDirectory(r119, (LinkOption[]) Arrays.copyOf(r27, r27.length)) == false) goto L27;
            if (PathTreeWalkKt.access$createsCycle(r26) == true) goto L25;
            if (PathTreeWalk.access$getIncludeDirectories(r120) == false) goto L21;
            r2.L$0 = r10;
            r2.L$1 = r9;
            r2.L$2 = r8;
            r2.L$3 = r26;
            r2.L$4 = r120;
            r2.L$5 = r119;
            r2.label = 1;
            if (r10.yield(r119, r2) != r0) goto L19;
            return r0;
        L19:
            r115 = r26;
            r23 = false;
            r118 = r10;
            r117 = r9;
            r25 = r120;
            r116 = r8;
            r24 = r119;
            goto L20
        L25:
            throw new FileSystemLoopException(r119.toString());
        L27:
            if (Files.exists(r119, (LinkOption[]) Arrays.copyOf(new LinkOption[]{LinkOption.NOFOLLOW_LINKS}, 1)) == false) goto L34;
            r2.L$0 = r10;
            r2.L$1 = r9;
            r2.L$2 = r8;
            r2.label = 2;
            if (r10.yield(r119, r2) != r0) goto L34;
            return r0;
        L5:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    public PathTreeWalk(Path r2, PathWalkOption[] r3) {
        Intrinsics.checkNotNullParameter(r2, "start");
        Intrinsics.checkNotNullParameter(r3, "options");
        this.start = r2;
        this.options = r3;
    }

    public static final /* synthetic */ boolean access$getFollowLinks(PathTreeWalk r1) {
        return r1.getFollowLinks();
    }

    public static final /* synthetic */ boolean access$getIncludeDirectories(PathTreeWalk r1) {
        return r1.getIncludeDirectories();
    }

    public static final /* synthetic */ LinkOption[] access$getLinkOptions(PathTreeWalk r1) {
        return r1.getLinkOptions();
    }

    public static final /* synthetic */ Path access$getStart$p(PathTreeWalk r1) {
        return r1.start;
    }

    private final Iterator<Path> bfsIterator() {
        return SequencesKt.iterator(new C10991(this, null));
    }

    private final Iterator<Path> dfsIterator() {
        return SequencesKt.iterator(new C11001(this, null));
    }

    private final boolean getFollowLinks() {
        return ArraysKt.contains(this.options, PathWalkOption.FOLLOW_LINKS);
    }

    private final boolean getIncludeDirectories() {
        return ArraysKt.contains(this.options, PathWalkOption.INCLUDE_DIRECTORIES);
    }

    private final LinkOption[] getLinkOptions() {
        return LinkFollowing.INSTANCE.toLinkOptions(getFollowLinks());
    }

    private final boolean isBFS() {
        return ArraysKt.contains(this.options, PathWalkOption.BREADTH_FIRST);
    }

    private final Object yieldIfNeeded(SequenceScope<? super Path> r7, PathNode r8, DirectoryEntriesReader r9, Function1<? super List<PathNode>, Unit> r10, Continuation<? super Unit> r11) {
        Path r1 = r8.getPath();
        LinkOption[] r2 = access$getLinkOptions(this);
        LinkOption[] r3 = (LinkOption[]) Arrays.copyOf(r2, r2.length);
        if (Files.isDirectory(r1, (LinkOption[]) Arrays.copyOf(r3, r3.length)) == false) goto L16;
        if (PathTreeWalkKt.access$createsCycle(r8) == true) goto L14;
        if (access$getIncludeDirectories(this) == false) goto L9;
        r7.yield(r1, r11);
    L9:
        LinkOption[] r4 = access$getLinkOptions(this);
        LinkOption[] r5 = (LinkOption[]) Arrays.copyOf(r4, r4.length);
        if (Files.isDirectory(r1, (LinkOption[]) Arrays.copyOf(r5, r5.length)) == false) goto L20;
        r10.invoke(r9.readEntries(r8));
    L20:
        return Unit.INSTANCE;
    L14:
        throw new FileSystemLoopException(r1.toString());
    L16:
        if (Files.exists(r1, (LinkOption[]) Arrays.copyOf(new LinkOption[]{LinkOption.NOFOLLOW_LINKS}, 1)) == false) goto L20;
        r7.yield(r1, r11);
        return Unit.INSTANCE;
    }

    @Override // kotlin.sequences.Sequence
    public Iterator<Path> iterator() {
        if (isBFS() == false) goto L6;
        return bfsIterator();
    L6:
        return dfsIterator();
    }
}
