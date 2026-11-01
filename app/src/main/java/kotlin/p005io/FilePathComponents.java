package kotlin.p005io;

import com.arthenica.ffmpegkit.MediaInformation;
import java.io.File;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: FilePathComponents.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\r\b\u0080\b\u0018\u00002\u00020\u0001B\u001d\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\u0016\u001a\u00020\u0003HÆ\u0003J\u000f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005HÆ\u0003J#\u0010\u0018\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005HÆ\u0001J\u0013\u0010\u0019\u001a\u00020\b2\b\u0010\u001a\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001b\u001a\u00020\u0013HÖ\u0001J\u0016\u0010\u001c\u001a\u00020\u00032\u0006\u0010\u001d\u001a\u00020\u00132\u0006\u0010\u001e\u001a\u00020\u0013J\t\u0010\u001f\u001a\u00020\rHÖ\u0001R\u0011\u0010\u0007\u001a\u00020\b8F¢\u0006\u0006\u001a\u0004\b\u0007\u0010\tR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\f\u001a\u00020\r8F¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0012\u001a\u00020\u00138F¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015¨\u0006 "}, m421d2 = {"Lkotlin/io/FilePathComponents;", "", "root", "Ljava/io/File;", "segments", "", "(Ljava/io/File;Ljava/util/List;)V", "isRooted", "", "()Z", "getRoot", "()Ljava/io/File;", "rootName", "", "getRootName", "()Ljava/lang/String;", "getSegments", "()Ljava/util/List;", MediaInformation.KEY_SIZE, "", "getSize", "()I", "component1", "component2", "copy", "equals", "other", "hashCode", "subPath", "beginIndex", "endIndex", "toString", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class FilePathComponents {
    private final File root;
    private final List<File> segments;

    /* JADX WARN: Multi-variable type inference failed */
    public FilePathComponents(File r2, List<? extends File> r3) {
        Intrinsics.checkNotNullParameter(r2, "root");
        Intrinsics.checkNotNullParameter(r3, "segments");
        this.root = r2;
        this.segments = r3;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ FilePathComponents copy$default(FilePathComponents r0, File r1, List r2, int r3, Object r4) {
        if ((r3 & 1) == 0) goto L6;
        r1 = r0.root;
    L6:
        if ((r3 & 2) == 0) goto L9;
        r2 = r0.segments;
    L9:
        return r0.copy(r1, r2);
    }

    public final File component1() {
        return this.root;
    }

    public final List<File> component2() {
        return this.segments;
    }

    public final FilePathComponents copy(File r2, List<? extends File> r3) {
        Intrinsics.checkNotNullParameter(r2, "root");
        Intrinsics.checkNotNullParameter(r3, "segments");
        return new FilePathComponents(r2, r3);
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if ((r6 instanceof FilePathComponents) == true) goto L8;
        return false;
    L8:
        FilePathComponents r1 = (FilePathComponents) r6;
        if (Intrinsics.areEqual(this.root, r1.root) == true) goto L12;
        return false;
    L12:
        if (Intrinsics.areEqual(this.segments, r1.segments) == true) goto L14;
        return false;
    L14:
        return true;
    }

    public final File getRoot() {
        return this.root;
    }

    public final String getRootName() {
        String r0 = this.root.getPath();
        Intrinsics.checkNotNullExpressionValue(r0, "getPath(...)");
        return r0;
    }

    public final List<File> getSegments() {
        return this.segments;
    }

    public final int getSize() {
        return this.segments.size();
    }

    public int hashCode() {
        return (this.root.hashCode() * 31) + this.segments.hashCode();
    }

    public final boolean isRooted() {
        String r0 = this.root.getPath();
        Intrinsics.checkNotNullExpressionValue(r0, "getPath(...)");
        if (r0.length() <= 0) goto L5;
        return true;
    L5:
        return false;
    }

    public final File subPath(int r12, int r13) {
        if (r12 < 0) goto L9;
        if (r12 > r13) goto L9;
        if (r13 > getSize()) goto L9;
        List<File> r2 = this.segments.subList(r12, r13);
        String r1 = File.separator;
        Intrinsics.checkNotNullExpressionValue(r1, "separator");
        return new File(CollectionsKt.joinToString$default(r2, r1, null, null, 0, null, null, 62, null));
    L9:
        throw new IllegalArgumentException();
    }

    public String toString() {
        return "FilePathComponents(root=" + this.root + ", segments=" + this.segments + ')';
    }
}
