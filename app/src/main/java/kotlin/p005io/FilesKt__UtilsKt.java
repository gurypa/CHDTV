package kotlin.p005io;

import androidx.media3.extractor.text.ttml.TtmlNode;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: Utils.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000<\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\u001a*\u0010\t\u001a\u00020\u00022\b\b\u0002\u0010\n\u001a\u00020\u00012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0002H\u0007\u001a*\u0010\r\u001a\u00020\u00022\b\b\u0002\u0010\n\u001a\u00020\u00012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0002H\u0007\u001a8\u0010\u000e\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00022\b\b\u0002\u0010\u0011\u001a\u00020\u000f2\u001a\b\u0002\u0010\u0012\u001a\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00150\u0013\u001a&\u0010\u0016\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00022\b\b\u0002\u0010\u0011\u001a\u00020\u000f2\b\b\u0002\u0010\u0017\u001a\u00020\u0018\u001a\n\u0010\u0019\u001a\u00020\u000f*\u00020\u0002\u001a\u0012\u0010\u001a\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0002\u001a\u0012\u0010\u001a\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0001\u001a\n\u0010\u001c\u001a\u00020\u0002*\u00020\u0002\u001a\u001d\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00020\u001d*\b\u0012\u0004\u0012\u00020\u00020\u001dH\u0002¢\u0006\u0002\b\u001e\u001a\u0011\u0010\u001c\u001a\u00020\u001f*\u00020\u001fH\u0002¢\u0006\u0002\b\u001e\u001a\u0012\u0010 \u001a\u00020\u0002*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u0014\u0010\"\u001a\u0004\u0018\u00010\u0002*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u0012\u0010#\u001a\u00020\u0002*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u0012\u0010$\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0002\u001a\u0012\u0010$\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0001\u001a\u0012\u0010&\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0002\u001a\u0012\u0010&\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0001\u001a\u0012\u0010'\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0002\u001a\u0012\u0010'\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0001\u001a\u0012\u0010(\u001a\u00020\u0001*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u001b\u0010)\u001a\u0004\u0018\u00010\u0001*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002H\u0002¢\u0006\u0002\b*\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u0015\u0010\u0005\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0004\"\u0015\u0010\u0007\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\b\u0010\u0004¨\u0006+"}, m421d2 = {"extension", "", "Ljava/io/File;", "getExtension", "(Ljava/io/File;)Ljava/lang/String;", "invariantSeparatorsPath", "getInvariantSeparatorsPath", "nameWithoutExtension", "getNameWithoutExtension", "createTempDir", "prefix", "suffix", "directory", "createTempFile", "copyRecursively", "", "target", "overwrite", "onError", "Lkotlin/Function2;", "Ljava/io/IOException;", "Lkotlin/io/OnErrorAction;", "copyTo", "bufferSize", "", "deleteRecursively", "endsWith", "other", "normalize", "", "normalize$FilesKt__UtilsKt", "Lkotlin/io/FilePathComponents;", "relativeTo", TtmlNode.RUBY_BASE, "relativeToOrNull", "relativeToOrSelf", "resolve", "relative", "resolveSibling", "startsWith", "toRelativeString", "toRelativeStringOrNull", "toRelativeStringOrNull$FilesKt__UtilsKt", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/io/FilesKt")
class FilesKt__UtilsKt extends FilesKt__FileTreeWalkKt {
    public FilesKt__UtilsKt() {
    }

    public static final boolean copyRecursively(File r12, File r13, boolean r14, final Function2<? super File, ? super IOException, ? extends OnErrorAction> r15) {
        Intrinsics.checkNotNullParameter(r12, "<this>");
        Intrinsics.checkNotNullParameter(r13, "target");
        Intrinsics.checkNotNullParameter(r15, "onError");
        if (r12.exists() == true) goto L66;
        if (r15.invoke(r12, new NoSuchFileException(r12, null, "The source file doesn't exist.", 2, null)) == OnErrorAction.TERMINATE) goto L8;
        return true;
    L8:
        return false;
    L66:
        Iterator<File> r16 = FilesKt.walkTopDown(r12).onFail(new C10962(r15)).iterator();     // Catch: TerminateException -> L61
    L12:
        if (r16.hasNext() == false) goto L60;
        File r0 = r16.next();     // Catch: TerminateException -> L61
        if (r0.exists() == false) goto L64;
        File r3 = new File(r13, FilesKt.toRelativeString(r0, r12));     // Catch: TerminateException -> L61
        if (r3.exists() == false) goto L46;
        if (r0.isDirectory() == true) goto L27;
    L28:
        if (r14 == true) goto L31;
        boolean r5 = true;
    L41:
        if (r5 == false) goto L46;
        if (r15.invoke(r3, new FileAlreadyExistsException(r0, r3, "The destination file already exists.")) != OnErrorAction.TERMINATE) goto L12;
        return false;
    L31:
        if (r3.isDirectory() == false) goto L37;
        if (FilesKt.deleteRecursively(r3) == true) goto L35;
        r5 = true;
        goto L41
    L35:
        r5 = false;
        goto L41
    L37:
        if (r3.delete() == true) goto L39;
        r5 = true;
        goto L41
    L39:
        r5 = false;
        goto L41
    L27:
        if (r3.isDirectory() == false) goto L28;
    L46:
        if (r0.isDirectory() == true) goto L48;
        boolean r7 = r14;
        if (FilesKt.copyTo$default(r0, r3, r7, 0, 4, null).length() != r0.length()) goto L53;
        r14 = r7;
        goto L12
    L53:
        if (r15.invoke(r0, new IOException("Source file wasn't copied completely, length of destination file differs.")) == OnErrorAction.TERMINATE) goto L55;
        r14 = r7;
        goto L12
    L55:
        return false;
    L48:
        r3.mkdirs();     // Catch: TerminateException -> L19
        goto L12
    L64:
        if (r15.invoke(r0, new NoSuchFileException(r0, null, "The source file doesn't exist.", 2, null)) != OnErrorAction.TERMINATE) goto L12;
        return false;
    L60:
        return true;
    L63:
        return false;
    }

    public static /* synthetic */ boolean copyRecursively$default(File r0, File r1, boolean r2, C10951 r3, int r4, Object r5) {
        if ((r4 & 2) == 0) goto L6;
        r2 = false;
    L6:
        if ((r4 & 4) == 0) goto L9;
        r3 = C10951.INSTANCE;
    L9:
        return FilesKt.copyRecursively(r0, r1, r2, r3);
    }

    public static final File copyTo(File r9, File r10, boolean r11, int r12) {
        Intrinsics.checkNotNullParameter(r9, "<this>");
        Intrinsics.checkNotNullParameter(r10, "target");
        if (r9.exists() == false) goto L44;
        if (r10.exists() == false) goto L15;
        if (r11 == false) goto L13;
        if (r10.delete() == true) goto L15;
        throw new FileAlreadyExistsException(r9, r10, "Tried to overwrite the destination, but failed to delete it.");
    L13:
        throw new FileAlreadyExistsException(r9, r10, "The destination file already exists.");
    L15:
        if (r9.isDirectory() == true) goto L17;
        File r0 = r10.getParentFile();
        if (r0 == null) goto L24;
        r0.mkdirs();
    L24:
        FileInputStream r1 = new FileInputStream(r9);
        FileInputStream r2 = r1;     // Catch: Throwable -> L37
        FileOutputStream r4 = new FileOutputStream(r10);     // Catch: Throwable -> L37
        ByteStreamsKt.copyTo(r2, r4, r12);     // Catch: Throwable -> L31
    L28:
        CloseableKt.closeFinally(r4, null);     // Catch: Throwable -> L37
        CloseableKt.closeFinally(r1, null);
    L30:
        return r10;
    L31:
        th = move-exception;
        throw th;     // Catch: Throwable -> L34
    L34:
        th = move-exception;
        CloseableKt.closeFinally(r4, th);     // Catch: Throwable -> L37
        throw th;     // Catch: Throwable -> L37
    L37:
        th = move-exception;
        throw th;     // Catch: Throwable -> L40
    L40:
        th = move-exception;
        CloseableKt.closeFinally(r1, th);
        throw th;
    L17:
        if (r10.mkdirs() == true) goto L30;
        throw new FileSystemException(r9, r10, "Failed to create target directory.");
    L44:
        throw new NoSuchFileException(r9, null, "The source file doesn't exist.", 2, null);
    }

    public static /* synthetic */ File copyTo$default(File r0, File r1, boolean r2, int r3, int r4, Object r5) {
        if ((r4 & 2) == 0) goto L6;
        r2 = false;
    L6:
        if ((r4 & 4) == 0) goto L9;
        r3 = 8192;
    L9:
        return FilesKt.copyTo(r0, r1, r2, r3);
    }

    @Deprecated(message = "Avoid creating temporary directories in the default temp location with this function due to too wide permissions on the newly created directory. Use kotlin.io.path.createTempDirectory instead.")
    public static final File createTempDir(String r4, String r5, File r6) {
        Intrinsics.checkNotNullParameter(r4, "prefix");
        File r0 = File.createTempFile(r4, r5, r6);
        r0.delete();
        if (r0.mkdir() == false) goto L7;
        Intrinsics.checkNotNull(r0);
        return r0;
    L7:
        throw new IOException("Unable to create temporary directory " + r0 + '.');
    }

    public static /* synthetic */ File createTempDir$default(String r1, String r2, File r3, int r4, Object r5) {
        if ((r4 & 1) == 0) goto L6;
        r1 = "tmp";
    L6:
        if ((r4 & 2) == 0) goto L9;
        r2 = null;
    L9:
        if ((r4 & 4) == 0) goto L12;
        r3 = null;
    L12:
        return FilesKt.createTempDir(r1, r2, r3);
    }

    @Deprecated(message = "Avoid creating temporary files in the default temp location with this function due to too wide permissions on the newly created file. Use kotlin.io.path.createTempFile instead or resort to java.io.File.createTempFile.")
    public static final File createTempFile(String r2, String r3, File r4) {
        Intrinsics.checkNotNullParameter(r2, "prefix");
        File r0 = File.createTempFile(r2, r3, r4);
        Intrinsics.checkNotNullExpressionValue(r0, "createTempFile(...)");
        return r0;
    }

    public static /* synthetic */ File createTempFile$default(String r1, String r2, File r3, int r4, Object r5) {
        if ((r4 & 1) == 0) goto L6;
        r1 = "tmp";
    L6:
        if ((r4 & 2) == 0) goto L9;
        r2 = null;
    L9:
        if ((r4 & 4) == 0) goto L12;
        r3 = null;
    L12:
        return FilesKt.createTempFile(r1, r2, r3);
    }

    public static final boolean deleteRecursively(File r10) {
        Intrinsics.checkNotNullParameter(r10, "<this>");
        boolean r3 = true;
        Iterator<File> r4 = FilesKt.walkBottomUp(r10).iterator();
    L4:
        if (r4.hasNext() == false) goto L14;
        File r6 = r4.next();
        boolean r7 = r3;
        if (r6.delete() == false) goto L8;
    L9:
        if (r7 == false) goto L11;
        boolean r9 = true;
    L12:
        r3 = r9;
    L11:
        r9 = false;
        goto L12
    L8:
        if (r6.exists() == true) goto L11;
    L14:
        return r3;
    }

    public static final boolean endsWith(File r5, File r6) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Intrinsics.checkNotNullParameter(r6, "other");
        FilePathComponents r0 = FilesKt.toComponents(r5);
        FilePathComponents r1 = FilesKt.toComponents(r6);
        if (r1.isRooted() == true) goto L5;
        int r2 = r0.getSize() - r1.getSize();
        if (r2 >= 0) goto L10;
        return false;
    L10:
        return r0.getSegments().subList(r2, r0.getSize()).equals(r1.getSegments());
    L5:
        return Intrinsics.areEqual(r5, r6);
    }

    public static final boolean endsWith(File r1, String r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "other");
        return FilesKt.endsWith(r1, new File(r2));
    }

    public static final String getExtension(File r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        String r0 = r3.getName();
        Intrinsics.checkNotNullExpressionValue(r0, "getName(...)");
        return StringsKt.substringAfterLast(r0, '.', "");
    }

    public static final String getInvariantSeparatorsPath(File r9) {
        Intrinsics.checkNotNullParameter(r9, "<this>");
        if (File.separatorChar == '/') goto L5;
        String r3 = r9.getPath();
        Intrinsics.checkNotNullExpressionValue(r3, "getPath(...)");
        return StringsKt.replace$default(r3, File.separatorChar, '/', false, 4, null);
    L5:
        String r0 = r9.getPath();
        Intrinsics.checkNotNullExpressionValue(r0, "getPath(...)");
        return r0;
    }

    public static final String getNameWithoutExtension(File r4) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        String r0 = r4.getName();
        Intrinsics.checkNotNullExpressionValue(r0, "getName(...)");
        return StringsKt.substringBeforeLast$default(r0, ".", null, 2, null);
    }

    public static final File normalize(File r13) {
        Intrinsics.checkNotNullParameter(r13, "<this>");
        FilePathComponents r0 = FilesKt.toComponents(r13);
        File r2 = r0.getRoot();
        List<File> r4 = normalize$FilesKt__UtilsKt(r0.getSegments());
        String r3 = File.separator;
        Intrinsics.checkNotNullExpressionValue(r3, "separator");
        return FilesKt.resolve(r2, CollectionsKt.joinToString$default(r4, r3, null, null, 0, null, null, 62, null));
    }

    private static final List<File> normalize$FilesKt__UtilsKt(List<? extends File> r5) {
        ArrayList r0 = new ArrayList(r5.size());
        Iterator<? extends File> r1 = r5.iterator();
    L4:
        if (r1.hasNext() == false) goto L16;
        File r2 = r1.next();
        String r3 = r2.getName();
        if (Intrinsics.areEqual(r3, ".") == true) goto L4;
        if (Intrinsics.areEqual(r3, "..") == true) goto L10;
        r0.add(r2);
        goto L4
    L10:
        if (r0.isEmpty() == true) goto L14;
        if (Intrinsics.areEqual(((File) CollectionsKt.last(r0)).getName(), "..") == true) goto L14;
        r0.remove(r0.size() - 1);
    L14:
        r0.add(r2);
        goto L4
    L16:
        return r0;
    }

    private static final FilePathComponents normalize$FilesKt__UtilsKt(FilePathComponents r3) {
        return new FilePathComponents(r3.getRoot(), normalize$FilesKt__UtilsKt(r3.getSegments()));
    }

    public static final File relativeTo(File r2, File r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, TtmlNode.RUBY_BASE);
        return new File(FilesKt.toRelativeString(r2, r3));
    }

    public static final File relativeToOrNull(File r3, File r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, TtmlNode.RUBY_BASE);
        String r0 = toRelativeStringOrNull$FilesKt__UtilsKt(r3, r4);
        if (r0 != null) goto L7;
        return null;
    L7:
        return new File(r0);
    }

    public static final File relativeToOrSelf(File r3, File r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, TtmlNode.RUBY_BASE);
        String r0 = toRelativeStringOrNull$FilesKt__UtilsKt(r3, r4);
        if (r0 == null) goto L6;
        return new File(r0);
    L6:
        return r3;
    }

    public static final File resolve(File r6, File r7) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        Intrinsics.checkNotNullParameter(r7, "relative");
        if (FilesKt.isRooted(r7) == false) goto L5;
        return r7;
    L5:
        String r0 = r6.toString();
        Intrinsics.checkNotNullExpressionValue(r0, "toString(...)");
        if (r0.length() != 0) goto L8;
        boolean r1 = true;
    L9:
        if (r1 == true) goto L15;
        if (StringsKt.endsWith$default(r0, File.separatorChar, false, 2, null) == true) goto L15;
        return new File(r0 + File.separatorChar + r7);
    L15:
        return new File(r0 + r7);
    L8:
        r1 = false;
        goto L9
    }

    public static final File resolve(File r1, String r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "relative");
        return FilesKt.resolve(r1, new File(r2));
    }

    public static final File resolveSibling(File r3, File r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "relative");
        FilePathComponents r0 = FilesKt.toComponents(r3);
        if (r0.getSize() != 0) goto L5;
        File r1 = new File("..");
    L7:
        return FilesKt.resolve(FilesKt.resolve(r0.getRoot(), r1), r4);
    L5:
        r1 = r0.subPath(0, r0.getSize() - 1);
        goto L7
    }

    public static final File resolveSibling(File r1, String r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "relative");
        return FilesKt.resolveSibling(r1, new File(r2));
    }

    public static final boolean startsWith(File r5, File r6) {
        Intrinsics.checkNotNullParameter(r5, "<this>");
        Intrinsics.checkNotNullParameter(r6, "other");
        FilePathComponents r0 = FilesKt.toComponents(r5);
        FilePathComponents r1 = FilesKt.toComponents(r6);
        if (Intrinsics.areEqual(r0.getRoot(), r1.getRoot()) == true) goto L6;
        return false;
    L6:
        if (r0.getSize() >= r1.getSize()) goto L9;
        return false;
    L9:
        return r0.getSegments().subList(0, r1.getSize()).equals(r1.getSegments());
    }

    public static final boolean startsWith(File r1, String r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "other");
        return FilesKt.startsWith(r1, new File(r2));
    }

    public static final String toRelativeString(File r3, File r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, TtmlNode.RUBY_BASE);
        String r0 = toRelativeStringOrNull$FilesKt__UtilsKt(r3, r4);
        if (r0 == null) goto L6;
        return r0;
    L6:
        throw new IllegalArgumentException("this and base files have different roots: " + r3 + " and " + r4 + '.');
    }

    private static final String toRelativeStringOrNull$FilesKt__UtilsKt(File r18, File r19) {
        FilePathComponents r0 = normalize$FilesKt__UtilsKt(FilesKt.toComponents(r18));
        FilePathComponents r1 = normalize$FilesKt__UtilsKt(FilesKt.toComponents(r19));
        if (Intrinsics.areEqual(r0.getRoot(), r1.getRoot()) == true) goto L5;
        return null;
    L5:
        int r2 = r1.getSize();
        int r4 = r0.getSize();
        int r7 = 0;
        int r8 = Math.min(r4, r2);
    L6:
        if (r7 >= r8) goto L10;
        if (Intrinsics.areEqual(r0.getSegments().get(r7), r1.getSegments().get(r7)) == false) goto L10;
        r7 = r7 + 1;
    L10:
        StringBuilder r5 = new StringBuilder();
        int r6 = r2 - 1;
        if (r7 <= r6) goto L13;
    L20:
        if (r7 >= r4) goto L25;
        if (r7 >= r2) goto L23;
        r5.append(File.separatorChar);
    L23:
        String r3 = File.separator;
        Intrinsics.checkNotNullExpressionValue(r3, "separator");
        CollectionsKt.joinTo$default(CollectionsKt.drop(r0.getSegments(), r7), r5, r3, null, null, 0, null, null, 124, null);
    L25:
        return r5.toString();
    L13:
        if (Intrinsics.areEqual(r1.getSegments().get(r6).getName(), "..") == true) goto L14;
        r5.append("..");
        if (r6 == r7) goto L18;
        r5.append(File.separatorChar);
    L18:
        if (r6 == r7) goto L20;
        r6 = r6 - 1;
        goto L13
    L14:
        return null;
    }
}
