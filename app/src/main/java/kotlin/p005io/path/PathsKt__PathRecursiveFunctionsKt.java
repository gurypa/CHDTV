package kotlin.p005io.path;

import java.nio.file.DirectoryStream;
import java.nio.file.FileSystemException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.SecureDirectoryStream;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import kotlin.ExceptionsKt;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.p005io.CloseableKt;

/* JADX INFO: compiled from: PathRecursiveFunctions.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000v\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a$\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0082\b¢\u0006\u0002\b\u0006\u001a\u001d\u0010\u0007\u001a\u00020\u00012\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u0002\u001a\u00020\u0003H\u0002¢\u0006\u0002\b\n\u001a\u001d\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\t2\u0006\u0010\u0002\u001a\u00020\u0003H\u0002¢\u0006\u0002\b\r\u001a&\u0010\u000e\u001a\u0004\u0018\u0001H\u000f\"\u0004\b\u0000\u0010\u000f2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u000f0\u0005H\u0082\b¢\u0006\u0004\b\u0010\u0010\u0011\u001aw\u0010\u0012\u001a\u00020\t*\u00020\t2\u0006\u0010\u0013\u001a\u00020\t2Q\b\u0002\u0010\u0014\u001aK\u0012\u0013\u0012\u00110\t¢\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u0018\u0012\u0013\u0012\u00110\t¢\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u0013\u0012\u0017\u0012\u00150\u0019j\u0002`\u001a¢\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u001b\u0012\u0004\u0012\u00020\u001c0\u00152\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001eH\u0007\u001a´\u0001\u0010\u0012\u001a\u00020\t*\u00020\t2\u0006\u0010\u0013\u001a\u00020\t2Q\b\u0002\u0010\u0014\u001aK\u0012\u0013\u0012\u00110\t¢\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u0018\u0012\u0013\u0012\u00110\t¢\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u0013\u0012\u0017\u0012\u00150\u0019j\u0002`\u001a¢\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u001b\u0012\u0004\u0012\u00020\u001c0\u00152\u0006\u0010\u001d\u001a\u00020\u001e2C\b\u0002\u0010 \u001a=\u0012\u0004\u0012\u00020!\u0012\u0013\u0012\u00110\t¢\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u0018\u0012\u0013\u0012\u00110\t¢\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u0013\u0012\u0004\u0012\u00020\"0\u0015¢\u0006\u0002\b#H\u0007\u001a\f\u0010$\u001a\u00020\u0001*\u00020\tH\u0007\u001a\u001b\u0010%\u001a\f\u0012\b\u0012\u00060\u0019j\u0002`\u001a0&*\u00020\tH\u0002¢\u0006\u0002\b'\u001a'\u0010(\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\t0)2\u0006\u0010\u0017\u001a\u00020\t2\u0006\u0010\u0002\u001a\u00020\u0003H\u0002¢\u0006\u0002\b*\u001a'\u0010+\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\t0)2\u0006\u0010\u0017\u001a\u00020\t2\u0006\u0010\u0002\u001a\u00020\u0003H\u0002¢\u0006\u0002\b,\u001a5\u0010-\u001a\u00020\u001e*\b\u0012\u0004\u0012\u00020\t0)2\u0006\u0010.\u001a\u00020\t2\u0012\u0010/\u001a\n\u0012\u0006\b\u0001\u0012\u00020100\"\u000201H\u0002¢\u0006\u0004\b2\u00103\u001a\u0011\u00104\u001a\u000205*\u00020\"H\u0003¢\u0006\u0002\b6\u001a\u0011\u00104\u001a\u000205*\u00020\u001cH\u0003¢\u0006\u0002\b6¨\u00067"}, m421d2 = {"collectIfThrows", "", "collector", "Lkotlin/io/path/ExceptionsCollector;", "function", "Lkotlin/Function0;", "collectIfThrows$PathsKt__PathRecursiveFunctionsKt", "insecureEnterDirectory", "path", "Ljava/nio/file/Path;", "insecureEnterDirectory$PathsKt__PathRecursiveFunctionsKt", "insecureHandleEntry", "entry", "insecureHandleEntry$PathsKt__PathRecursiveFunctionsKt", "tryIgnoreNoSuchFileException", "R", "tryIgnoreNoSuchFileException$PathsKt__PathRecursiveFunctionsKt", "(Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "copyToRecursively", "target", "onError", "Lkotlin/Function3;", "Lkotlin/ParameterName;", "name", "source", "Ljava/lang/Exception;", "Lkotlin/Exception;", "exception", "Lkotlin/io/path/OnErrorResult;", "followLinks", "", "overwrite", "copyAction", "Lkotlin/io/path/CopyActionContext;", "Lkotlin/io/path/CopyActionResult;", "Lkotlin/ExtensionFunctionType;", "deleteRecursively", "deleteRecursivelyImpl", "", "deleteRecursivelyImpl$PathsKt__PathRecursiveFunctionsKt", "enterDirectory", "Ljava/nio/file/SecureDirectoryStream;", "enterDirectory$PathsKt__PathRecursiveFunctionsKt", "handleEntry", "handleEntry$PathsKt__PathRecursiveFunctionsKt", "isDirectory", "entryName", "options", "", "Ljava/nio/file/LinkOption;", "isDirectory$PathsKt__PathRecursiveFunctionsKt", "(Ljava/nio/file/SecureDirectoryStream;Ljava/nio/file/Path;[Ljava/nio/file/LinkOption;)Z", "toFileVisitResult", "Ljava/nio/file/FileVisitResult;", "toFileVisitResult$PathsKt__PathRecursiveFunctionsKt", "kotlin-stdlib-jdk7"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/io/path/PathsKt")
class PathsKt__PathRecursiveFunctionsKt extends PathsKt__PathReadWriteKt {

    /* JADX INFO: compiled from: PathRecursiveFunctions.kt */
    @Metadata(m422k = 3, m423mv = {1, 9, 0}, m425xi = 48)
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0 = null;
        public static final /* synthetic */ int[] $EnumSwitchMapping$1 = null;

        static {
            int[] r0 = new int[CopyActionResult.values().length];
            r0[CopyActionResult.CONTINUE.ordinal()] = 1;     // Catch: NoSuchFieldError -> L5
        L22:
            r0[CopyActionResult.TERMINATE.ordinal()] = 2;     // Catch: NoSuchFieldError -> L9
        L28:
            r0[CopyActionResult.SKIP_SUBTREE.ordinal()] = 3;     // Catch: NoSuchFieldError -> L12
        L13:
            $EnumSwitchMapping$0 = r0;
            int[] r1 = new int[OnErrorResult.values().length];
            r1[OnErrorResult.TERMINATE.ordinal()] = 1;     // Catch: NoSuchFieldError -> L16
        L26:
            r1[OnErrorResult.SKIP_SUBTREE.ordinal()] = 2;     // Catch: NoSuchFieldError -> L19
        L20:
            $EnumSwitchMapping$1 = r1;
        }
    }

    public PathsKt__PathRecursiveFunctionsKt() {
    }

    public static final /* synthetic */ FileVisitResult access$copyToRecursively$copy(Function3 r1, Path r2, Path r3, Function3 r4, Path r5, BasicFileAttributes r6) {
        return copyToRecursively$copy$PathsKt__PathRecursiveFunctionsKt(r1, r2, r3, r4, r5, r6);
    }

    public static final /* synthetic */ FileVisitResult access$copyToRecursively$error(Function3 r1, Path r2, Path r3, Path r4, Exception r5) {
        return copyToRecursively$error$PathsKt__PathRecursiveFunctionsKt(r1, r2, r3, r4, r5);
    }

    private static final void collectIfThrows$PathsKt__PathRecursiveFunctionsKt(ExceptionsCollector r2, Function0<Unit> r3) {
        r3.invoke();     // Catch: Exception -> L5
        return;
    L5:
        e = move-exception;
        r2.collect(e);
    }

    public static final Path copyToRecursively(final Path r7, final Path r8, final Function3<? super Path, ? super Path, ? super Exception, ? extends OnErrorResult> r9, boolean r10, final Function3<? super CopyActionContext, ? super Path, ? super Path, ? extends CopyActionResult> r11) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, "target");
        Intrinsics.checkNotNullParameter(r9, "onError");
        Intrinsics.checkNotNullParameter(r11, "copyAction");
        LinkOption[] r0 = LinkFollowing.INSTANCE.toLinkOptions(r10);
        LinkOption[] r1 = (LinkOption[]) Arrays.copyOf(r0, r0.length);
        if (Files.exists(r7, (LinkOption[]) Arrays.copyOf(r1, r1.length)) == false) goto L39;
        boolean r2 = false;
        if (Files.exists(r7, (LinkOption[]) Arrays.copyOf(new LinkOption[0], 0)) == false) goto L36;
        if (r10 == true) goto L10;
        if (Files.isSymbolicLink(r7) == true) goto L36;
    L10:
        if (Files.exists(r8, (LinkOption[]) Arrays.copyOf(new LinkOption[0], 0)) == true) goto L12;
    L14:
        boolean r3 = false;
    L15:
        if (r3 == false) goto L20;
        if (Files.isSameFile(r7, r8) == true) goto L36;
    L20:
        if (Intrinsics.areEqual(r7.getFileSystem(), r8.getFileSystem()) == false) goto L32;
        if (r3 == false) goto L24;
        r2 = r8.toRealPath(new LinkOption[0]).startsWith(r7.toRealPath(new LinkOption[0]));
        goto L32
    L24:
        Path r4 = r8.getParent();
        if (r4 == null) goto L32;
        if (Files.exists(r4, (LinkOption[]) Arrays.copyOf(new LinkOption[0], 0)) == false) goto L32;
        if (r4.toRealPath(new LinkOption[0]).startsWith(r7.toRealPath(new LinkOption[0])) == false) goto L32;
        r2 = true;
    L32:
        if (r2 == false) goto L36;
        throw new FileSystemException(r7.toString(), r8.toString(), "Recursively copying a directory into its subdirectory is prohibited.");
    L12:
        if (Files.isSymbolicLink(r8) == true) goto L14;
        r3 = true;
    L36:
        PathsKt.visitFileTree$default(r7, 0, r10, new C11055(r11, r7, r8, r9), 1, null);
        return r8;
    L39:
        throw new NoSuchFileException(r7.toString(), r8.toString(), "The source file doesn't exist.");
    }

    public static final Path copyToRecursively(Path r8, Path r9, Function3<? super Path, ? super Path, ? super Exception, ? extends OnErrorResult> r10, final boolean r11, boolean r12) {
        Intrinsics.checkNotNullParameter(r8, "<this>");
        Intrinsics.checkNotNullParameter(r9, "target");
        Intrinsics.checkNotNullParameter(r10, "onError");
        if (r12 == false) goto L6;
        return PathsKt.copyToRecursively(r8, r9, r10, r11, new C11022(r11));
    L6:
        return PathsKt.copyToRecursively$default(r8, r9, r10, r11, null, 8, null);
    }

    private static final FileVisitResult copyToRecursively$copy$PathsKt__PathRecursiveFunctionsKt(Function3<? super CopyActionContext, ? super Path, ? super Path, ? extends CopyActionResult> r2, Path r3, Path r4, Function3<? super Path, ? super Path, ? super Exception, ? extends OnErrorResult> r5, Path r6, BasicFileAttributes r7) {
        return toFileVisitResult$PathsKt__PathRecursiveFunctionsKt(r2.invoke(DefaultCopyActionContext.INSTANCE, r6, copyToRecursively$destination$PathsKt__PathRecursiveFunctionsKt(r3, r4, r6)));
    L4:
        e = move-exception;
        return copyToRecursively$error$PathsKt__PathRecursiveFunctionsKt(r5, r3, r4, r6, e);
    }

    public static /* synthetic */ Path copyToRecursively$default(Path r0, Path r1, C11033 r2, final boolean r3, Function3 r4, int r5, Object r6) {
        if ((r5 & 2) == 0) goto L6;
        r2 = C11033.INSTANCE;
    L6:
        if ((r5 & 8) == 0) goto L9;
        r4 = new C11044(r3);
    L9:
        return PathsKt.copyToRecursively(r0, r1, r2, r3, r4);
    }

    public static /* synthetic */ Path copyToRecursively$default(Path r0, Path r1, C11011 r2, boolean r3, boolean r4, int r5, Object r6) {
        if ((r5 & 2) == 0) goto L6;
        r2 = C11011.INSTANCE;
    L6:
        return PathsKt.copyToRecursively(r0, r1, r2, r3, r4);
    }

    private static final Path copyToRecursively$destination$PathsKt__PathRecursiveFunctionsKt(Path r3, Path r4, Path r5) {
        Path r1 = r4.resolve(PathsKt.relativeTo(r5, r3).toString());
        Intrinsics.checkNotNullExpressionValue(r1, "resolve(...)");
        return r1;
    }

    private static final FileVisitResult copyToRecursively$error$PathsKt__PathRecursiveFunctionsKt(Function3<? super Path, ? super Path, ? super Exception, ? extends OnErrorResult> r1, Path r2, Path r3, Path r4, Exception r5) {
        return toFileVisitResult$PathsKt__PathRecursiveFunctionsKt(r1.invoke(r4, copyToRecursively$destination$PathsKt__PathRecursiveFunctionsKt(r2, r3, r4), r5));
    }

    public static final void deleteRecursively(Path r12) {
        Intrinsics.checkNotNullParameter(r12, "<this>");
        List<Exception> r0 = deleteRecursivelyImpl$PathsKt__PathRecursiveFunctionsKt(r12);
        if (r0.isEmpty() == true) goto L10;
        FileSystemException r1 = new FileSystemException("Failed to delete one or more files. See suppressed exceptions for details.");
        Iterator<T> r6 = r0.iterator();
    L6:
        if (r6.hasNext() == false) goto L9;
        ExceptionsKt.addSuppressed(r1, (Exception) r6.next());
        goto L6
    L9:
        throw r1;
    }

    private static final List<Exception> deleteRecursivelyImpl$PathsKt__PathRecursiveFunctionsKt(Path r12) {
        ExceptionsCollector r0 = new ExceptionsCollector(0, 1, null);
        boolean r1 = true;
        Path r2 = r12.getParent();
        if (r2 != null) goto L28;
    L22:
        if (r1 == false) goto L25;
        insecureHandleEntry$PathsKt__PathRecursiveFunctionsKt(r12, r0);
    L25:
        return r0.getCollectedExceptions();
    L28:
        DirectoryStream<Path> r5 = Files.newDirectoryStream(r2);     // Catch: Throwable -> L7
    L9:
        if (r5 == null) goto L22;
        DirectoryStream<Path> r6 = r5;
        DirectoryStream<Path> r7 = r6;     // Catch: Throwable -> L16
        if ((r7 instanceof SecureDirectoryStream) == false) goto L14;
        r1 = false;
        r0.setPath(r2);     // Catch: Throwable -> L16
        Path r10 = r12.getFileName();     // Catch: Throwable -> L16
        Intrinsics.checkNotNullExpressionValue(r10, "getFileName(...)");     // Catch: Throwable -> L16
        handleEntry$PathsKt__PathRecursiveFunctionsKt((SecureDirectoryStream) r7, r10, r0);     // Catch: Throwable -> L16
    L14:
        Unit r8 = Unit.INSTANCE;     // Catch: Throwable -> L16
        CloseableKt.closeFinally(r6, null);
    L16:
        th = move-exception;
        throw th;     // Catch: Throwable -> L18
    L18:
        th = move-exception;
        CloseableKt.closeFinally(r6, th);
        throw th;
    L8:
        r5 = null;
        goto L9
    }

    private static final void enterDirectory$PathsKt__PathRecursiveFunctionsKt(SecureDirectoryStream<Path> r10, Path r11, ExceptionsCollector r12) {
        SecureDirectoryStream<Path> r4 = r10.newDirectoryStream(r11, new LinkOption[]{LinkOption.NOFOLLOW_LINKS});     // Catch: Exception -> L5 NoSuchFileException -> L7
    L10:
        if (r4 == null) goto L35;
        SecureDirectoryStream<Path> r5 = r4;     // Catch: Exception -> L5
        SecureDirectoryStream<Path> r2 = r5;     // Catch: Throwable -> L19
        Iterator<Path> r6 = r2.iterator();     // Catch: Throwable -> L19
    L14:
        if (r6.hasNext() == false) goto L16;
        Path r8 = r6.next().getFileName();     // Catch: Throwable -> L19
        Intrinsics.checkNotNullExpressionValue(r8, "getFileName(...)");     // Catch: Throwable -> L19
        handleEntry$PathsKt__PathRecursiveFunctionsKt(r2, r8, r12);     // Catch: Throwable -> L19
        goto L14
    L16:
        Unit r3 = Unit.INSTANCE;     // Catch: Throwable -> L19
        CloseableKt.closeFinally(r5, null);     // Catch: Exception -> L5
        return;
    L19:
        th = move-exception;
        throw th;     // Catch: Throwable -> L21
    L21:
        th = move-exception;
        CloseableKt.closeFinally(r5, th);     // Catch: Exception -> L5
        throw th;     // Catch: Exception -> L5
    L35:
        return;
    L5:
        e = move-exception;
        r12.collect(e);
        return;
    L8:
        r4 = null;
        goto L10
    }

    private static final void handleEntry$PathsKt__PathRecursiveFunctionsKt(SecureDirectoryStream<Path> r5, Path r6, ExceptionsCollector r7) {
        r7.enterEntry(r6);
    L17:
        e = move-exception;
        r7.collect(e);
    L19:
        r7.exitEntry(r6);
        return;
    L4:
        if (isDirectory$PathsKt__PathRecursiveFunctionsKt(r5, r6, new LinkOption[]{LinkOption.NOFOLLOW_LINKS}) == false) goto L21;
        int r2 = r7.getTotalExceptions();     // Catch: Exception -> L17
        enterDirectory$PathsKt__PathRecursiveFunctionsKt(r5, r6, r7);     // Catch: Exception -> L17
        if (r2 == r7.getTotalExceptions()) goto L23;
    L25:
        goto L19
    L23:
        r5.deleteDirectory(r6);     // Catch: NoSuchFileException -> L10 Exception -> L17
        Unit r4 = Unit.INSTANCE;     // Catch: NoSuchFileException -> L10 Exception -> L17
        goto L25
    L21:
        r5.deleteFile(r6);     // Catch: NoSuchFileException -> L15 Exception -> L17
        Unit r3 = Unit.INSTANCE;     // Catch: NoSuchFileException -> L15 Exception -> L17
        goto L25
    }

    private static final void insecureEnterDirectory$PathsKt__PathRecursiveFunctionsKt(Path r8, ExceptionsCollector r9) {
        DirectoryStream<Path> r5 = Files.newDirectoryStream(r8);     // Catch: Exception -> L5 NoSuchFileException -> L7
    L10:
        if (r5 == null) goto L35;
        DirectoryStream<Path> r6 = r5;     // Catch: Exception -> L5
        Iterator<Path> r7 = r6.iterator();     // Catch: Throwable -> L19
    L14:
        if (r7.hasNext() == false) goto L16;
        Path r10 = r7.next();     // Catch: Throwable -> L19
        Intrinsics.checkNotNull(r10);     // Catch: Throwable -> L19
        insecureHandleEntry$PathsKt__PathRecursiveFunctionsKt(r10, r9);     // Catch: Throwable -> L19
        goto L14
    L16:
        Unit r2 = Unit.INSTANCE;     // Catch: Throwable -> L19
        CloseableKt.closeFinally(r6, null);     // Catch: Exception -> L5
        return;
    L19:
        th = move-exception;
        throw th;     // Catch: Throwable -> L21
    L21:
        th = move-exception;
        CloseableKt.closeFinally(r6, th);     // Catch: Exception -> L5
        throw th;     // Catch: Exception -> L5
    L35:
        return;
    L5:
        e = move-exception;
        r9.collect(e);
        return;
    L8:
        r5 = null;
        goto L10
    }

    private static final void insecureHandleEntry$PathsKt__PathRecursiveFunctionsKt(Path r6, ExceptionsCollector r7) {
    L10:
        e = move-exception;
        r7.collect(e);
        return;
    L4:
        if (Files.isDirectory(r6, (LinkOption[]) Arrays.copyOf(new LinkOption[]{LinkOption.NOFOLLOW_LINKS}, 1)) == false) goto L8;
        int r2 = r7.getTotalExceptions();     // Catch: Exception -> L10
        insecureEnterDirectory$PathsKt__PathRecursiveFunctionsKt(r6, r7);     // Catch: Exception -> L10
        if (r2 != r7.getTotalExceptions()) goto L14;
        Files.deleteIfExists(r6);     // Catch: Exception -> L10
    L14:
        return;
    L8:
        Files.deleteIfExists(r6);     // Catch: Exception -> L10
        goto L14
    }

    private static final boolean isDirectory$PathsKt__PathRecursiveFunctionsKt(SecureDirectoryStream<Path> r4, Path r5, LinkOption... r6) {
        Boolean r1 = Boolean.valueOf(((BasicFileAttributeView) r4.getFileAttributeView(r5, BasicFileAttributeView.class, (LinkOption[]) Arrays.copyOf(r6, r6.length))).readAttributes().isDirectory());     // Catch: NoSuchFileException -> L5
    L7:
        if (r1 != null) goto L13;
        return false;
    L13:
        return r1.booleanValue();
    L6:
        r1 = null;
        goto L7
    }

    private static final FileVisitResult toFileVisitResult$PathsKt__PathRecursiveFunctionsKt(CopyActionResult r2) {
        switch(WhenMappings.$EnumSwitchMapping$0[r2.ordinal()]) {
            case 1: goto L9;
            case 2: goto L11;
            case 3: goto L10;
            default: goto L5;
        };
    L5:
        throw new NoWhenBranchMatchedException();
    L10:
        return FileVisitResult.SKIP_SUBTREE;
    L11:
        return FileVisitResult.TERMINATE;
    L9:
        return FileVisitResult.CONTINUE;
    }

    private static final FileVisitResult toFileVisitResult$PathsKt__PathRecursiveFunctionsKt(OnErrorResult r2) {
        switch(WhenMappings.$EnumSwitchMapping$1[r2.ordinal()]) {
            case 1: goto L8;
            case 2: goto L9;
            default: goto L5;
        };
    L5:
        throw new NoWhenBranchMatchedException();
    L9:
        return FileVisitResult.SKIP_SUBTREE;
    L8:
        return FileVisitResult.TERMINATE;
    }

    private static final <R> R tryIgnoreNoSuchFileException$PathsKt__PathRecursiveFunctionsKt(Function0<? extends R> r3) {
        return r3.invoke();
    L7:
        return null;
    }
}
