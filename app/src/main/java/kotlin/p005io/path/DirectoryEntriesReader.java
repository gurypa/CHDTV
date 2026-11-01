package kotlin.p005io.path;

import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.ArrayDeque;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: PathTreeWalk.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0003\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u00022\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u0006\u001a\u00020\u0007J\u0018\u0010\u0013\u001a\u00020\r2\u0006\u0010\u0014\u001a\u00020\u00022\u0006\u0010\u000f\u001a\u00020\u0010H\u0016R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u0015"}, m421d2 = {"Lkotlin/io/path/DirectoryEntriesReader;", "Ljava/nio/file/SimpleFileVisitor;", "Ljava/nio/file/Path;", "followLinks", "", "(Z)V", "directoryNode", "Lkotlin/io/path/PathNode;", "entries", "Lkotlin/collections/ArrayDeque;", "getFollowLinks", "()Z", "preVisitDirectory", "Ljava/nio/file/FileVisitResult;", "dir", "attrs", "Ljava/nio/file/attribute/BasicFileAttributes;", "readEntries", "", "visitFile", "file", "kotlin-stdlib-jdk7"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
final class DirectoryEntriesReader extends SimpleFileVisitor<Path> {
    private PathNode directoryNode;
    private ArrayDeque<PathNode> entries;
    private final boolean followLinks;

    public DirectoryEntriesReader(boolean r2) {
        this.followLinks = r2;
        this.entries = new ArrayDeque();
    }

    public final boolean getFollowLinks() {
        return this.followLinks;
    }

    @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
    public /* bridge */ /* synthetic */ FileVisitResult preVisitDirectory(Object r2, BasicFileAttributes r3) {
        return preVisitDirectory((Path) r2, r3);
    }

    public FileVisitResult preVisitDirectory(Path r4, BasicFileAttributes r5) {
        Intrinsics.checkNotNullParameter(r4, "dir");
        Intrinsics.checkNotNullParameter(r5, "attrs");
        PathNode r0 = new PathNode(r4, r5.fileKey(), this.directoryNode);
        this.entries.add(r0);
        FileVisitResult r1 = super.preVisitDirectory(r4, r5);
        Intrinsics.checkNotNullExpressionValue(r1, "preVisitDirectory(...)");
        return r1;
    }

    public final List<PathNode> readEntries(PathNode r5) {
        Intrinsics.checkNotNullParameter(r5, "directoryNode");
        this.directoryNode = r5;
        Files.walkFileTree(r5.getPath(), LinkFollowing.INSTANCE.toVisitOptions(this.followLinks), 1, this);
        this.entries.removeFirst();
        ArrayDeque<PathNode> r0 = this.entries;
        this.entries = new ArrayDeque();
        return r0;
    }

    @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
    public /* bridge */ /* synthetic */ FileVisitResult visitFile(Object r2, BasicFileAttributes r3) {
        return visitFile((Path) r2, r3);
    }

    public FileVisitResult visitFile(Path r4, BasicFileAttributes r5) {
        Intrinsics.checkNotNullParameter(r4, "file");
        Intrinsics.checkNotNullParameter(r5, "attrs");
        PathNode r0 = new PathNode(r4, null, this.directoryNode);
        this.entries.add(r0);
        FileVisitResult r1 = super.visitFile(r4, r5);
        Intrinsics.checkNotNullExpressionValue(r1, "visitFile(...)");
        return r1;
    }
}
