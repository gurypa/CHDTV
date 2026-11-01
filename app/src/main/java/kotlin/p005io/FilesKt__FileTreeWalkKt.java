package kotlin.p005io;

import java.io.File;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: FileTreeWalk.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u0014\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0014\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004\u001a\n\u0010\u0005\u001a\u00020\u0001*\u00020\u0002\u001a\n\u0010\u0006\u001a\u00020\u0001*\u00020\u0002¨\u0006\u0007"}, m421d2 = {"walk", "Lkotlin/io/FileTreeWalk;", "Ljava/io/File;", "direction", "Lkotlin/io/FileWalkDirection;", "walkBottomUp", "walkTopDown", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/io/FilesKt")
class FilesKt__FileTreeWalkKt extends FilesKt__FileReadWriteKt {
    public FilesKt__FileTreeWalkKt() {
    }

    public static final FileTreeWalk walk(File r1, FileWalkDirection r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "direction");
        return new FileTreeWalk(r1, r2);
    }

    public static /* synthetic */ FileTreeWalk walk$default(File r0, FileWalkDirection r1, int r2, Object r3) {
        if ((r2 & 1) == 0) goto L6;
        r1 = FileWalkDirection.TOP_DOWN;
    L6:
        return FilesKt.walk(r0, r1);
    }

    public static final FileTreeWalk walkBottomUp(File r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return FilesKt.walk(r1, FileWalkDirection.BOTTOM_UP);
    }

    public static final FileTreeWalk walkTopDown(File r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return FilesKt.walk(r1, FileWalkDirection.TOP_DOWN);
    }
}
