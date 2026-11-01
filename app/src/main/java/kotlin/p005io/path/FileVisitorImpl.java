package kotlin.p005io.path;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import kotlin.Metadata;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: FileVisitorBuilder.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001Bw\u0012\u001a\u0010\u0003\u001a\u0016\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0004\u0012\u001a\u0010\u0007\u001a\u0016\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0004\u0012\u001a\u0010\b\u001a\u0016\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0004\u0012\u001c\u0010\n\u001a\u0018\u0012\u0004\u0012\u00020\u0002\u0012\u0006\u0012\u0004\u0018\u00010\t\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0004¢\u0006\u0002\u0010\u000bJ\u001a\u0010\f\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u00022\b\u0010\u000e\u001a\u0004\u0018\u00010\tH\u0016J\u0018\u0010\u000f\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u0005H\u0016J\u0018\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u0005H\u0016J\u0018\u0010\u0013\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u00022\u0006\u0010\u000e\u001a\u00020\tH\u0016R$\u0010\n\u001a\u0018\u0012\u0004\u0012\u00020\u0002\u0012\u0006\u0012\u0004\u0018\u00010\t\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\"\u0010\u0003\u001a\u0016\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\"\u0010\u0007\u001a\u0016\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\"\u0010\b\u001a\u0016\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0014"}, m421d2 = {"Lkotlin/io/path/FileVisitorImpl;", "Ljava/nio/file/SimpleFileVisitor;", "Ljava/nio/file/Path;", "onPreVisitDirectory", "Lkotlin/Function2;", "Ljava/nio/file/attribute/BasicFileAttributes;", "Ljava/nio/file/FileVisitResult;", "onVisitFile", "onVisitFileFailed", "Ljava/io/IOException;", "onPostVisitDirectory", "(Lkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function2;)V", "postVisitDirectory", "dir", "exc", "preVisitDirectory", "attrs", "visitFile", "file", "visitFileFailed", "kotlin-stdlib-jdk7"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
final class FileVisitorImpl extends SimpleFileVisitor<Path> {
    private final Function2<Path, IOException, FileVisitResult> onPostVisitDirectory;
    private final Function2<Path, BasicFileAttributes, FileVisitResult> onPreVisitDirectory;
    private final Function2<Path, BasicFileAttributes, FileVisitResult> onVisitFile;
    private final Function2<Path, IOException, FileVisitResult> onVisitFileFailed;

    /* JADX WARN: Multi-variable type inference failed */
    public FileVisitorImpl(Function2<? super Path, ? super BasicFileAttributes, ? extends FileVisitResult> r1, Function2<? super Path, ? super BasicFileAttributes, ? extends FileVisitResult> r2, Function2<? super Path, ? super IOException, ? extends FileVisitResult> r3, Function2<? super Path, ? super IOException, ? extends FileVisitResult> r4) {
        this.onPreVisitDirectory = r1;
        this.onVisitFile = r2;
        this.onVisitFileFailed = r3;
        this.onPostVisitDirectory = r4;
    }

    @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
    public /* bridge */ /* synthetic */ FileVisitResult postVisitDirectory(Object r2, IOException r3) {
        return postVisitDirectory((Path) r2, r3);
    }

    public FileVisitResult postVisitDirectory(Path r3, IOException r4) {
        Intrinsics.checkNotNullParameter(r3, "dir");
        Function2<Path, IOException, FileVisitResult> r0 = this.onPostVisitDirectory;
        if (r0 == null) goto L6;
        FileVisitResult r1 = r0.invoke(r3, r4);
        if (r1 == null) goto L6;
        return r1;
    L6:
        FileVisitResult r2 = super.postVisitDirectory(r3, r4);
        Intrinsics.checkNotNullExpressionValue(r2, "postVisitDirectory(...)");
        return r2;
    }

    @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
    public /* bridge */ /* synthetic */ FileVisitResult preVisitDirectory(Object r2, BasicFileAttributes r3) {
        return preVisitDirectory((Path) r2, r3);
    }

    public FileVisitResult preVisitDirectory(Path r3, BasicFileAttributes r4) {
        Intrinsics.checkNotNullParameter(r3, "dir");
        Intrinsics.checkNotNullParameter(r4, "attrs");
        Function2<Path, BasicFileAttributes, FileVisitResult> r0 = this.onPreVisitDirectory;
        if (r0 == null) goto L6;
        FileVisitResult r1 = r0.invoke(r3, r4);
        if (r1 == null) goto L6;
        return r1;
    L6:
        FileVisitResult r2 = super.preVisitDirectory(r3, r4);
        Intrinsics.checkNotNullExpressionValue(r2, "preVisitDirectory(...)");
        return r2;
    }

    @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
    public /* bridge */ /* synthetic */ FileVisitResult visitFile(Object r2, BasicFileAttributes r3) {
        return visitFile((Path) r2, r3);
    }

    public FileVisitResult visitFile(Path r3, BasicFileAttributes r4) {
        Intrinsics.checkNotNullParameter(r3, "file");
        Intrinsics.checkNotNullParameter(r4, "attrs");
        Function2<Path, BasicFileAttributes, FileVisitResult> r0 = this.onVisitFile;
        if (r0 == null) goto L6;
        FileVisitResult r1 = r0.invoke(r3, r4);
        if (r1 == null) goto L6;
        return r1;
    L6:
        FileVisitResult r2 = super.visitFile(r3, r4);
        Intrinsics.checkNotNullExpressionValue(r2, "visitFile(...)");
        return r2;
    }

    @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
    public /* bridge */ /* synthetic */ FileVisitResult visitFileFailed(Object r2, IOException r3) {
        return visitFileFailed((Path) r2, r3);
    }

    public FileVisitResult visitFileFailed(Path r3, IOException r4) {
        Intrinsics.checkNotNullParameter(r3, "file");
        Intrinsics.checkNotNullParameter(r4, "exc");
        Function2<Path, IOException, FileVisitResult> r0 = this.onVisitFileFailed;
        if (r0 == null) goto L6;
        FileVisitResult r1 = r0.invoke(r3, r4);
        if (r1 == null) goto L6;
        return r1;
    L6:
        FileVisitResult r2 = super.visitFileFailed(r3, r4);
        Intrinsics.checkNotNullExpressionValue(r2, "visitFileFailed(...)");
        return r2;
    }
}
