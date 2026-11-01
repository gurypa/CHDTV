package kotlin.p005io.path;

import androidx.media3.extractor.text.ttml.TtmlNode;
import java.nio.file.Path;
import java.nio.file.Paths;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: PathUtils.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\bÂ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0016\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\n"}, m421d2 = {"Lkotlin/io/path/PathRelativizer;", "", "()V", "emptyPath", "Ljava/nio/file/Path;", "kotlin.jvm.PlatformType", "parentPath", "tryRelativeTo", "path", TtmlNode.RUBY_BASE, "kotlin-stdlib-jdk7"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
final class PathRelativizer {
    public static final PathRelativizer INSTANCE = null;
    private static final Path emptyPath = null;
    private static final Path parentPath = null;

    static {
        INSTANCE = new PathRelativizer();
        emptyPath = Paths.get("", new String[0]);
        parentPath = Paths.get("..", new String[0]);
    }

    private PathRelativizer() {
    }

    public final Path tryRelativeTo(Path r9, Path r10) {
        Intrinsics.checkNotNullParameter(r9, "path");
        Intrinsics.checkNotNullParameter(r10, TtmlNode.RUBY_BASE);
        Path r0 = r10.normalize();
        Path r1 = r9.normalize();
        Path r2 = r0.relativize(r1);
        int r3 = 0;
        int r4 = Math.min(r0.getNameCount(), r1.getNameCount());
    L3:
        if (r3 >= r4) goto L12;
        if (Intrinsics.areEqual(r0.getName(r3), parentPath) == false) goto L12;
        if (Intrinsics.areEqual(r1.getName(r3), parentPath) == false) goto L10;
        r3 = r3 + 1;
        goto L3
    L10:
        throw new IllegalArgumentException("Unable to compute relative path");
    L12:
        if (Intrinsics.areEqual(r1, r0) == false) goto L14;
    L16:
        String r5 = r2.toString();
        String r6 = r2.getFileSystem().getSeparator();
        Intrinsics.checkNotNullExpressionValue(r6, "getSeparator(...)");
        if (StringsKt.endsWith$default(r5, r6, false, 2, null) == false) goto L19;
        Path r7 = r2.getFileSystem().getPath(StringsKt.dropLast(r5, r2.getFileSystem().getSeparator().length()), new String[0]);
    L20:
        Intrinsics.checkNotNull(r7);
        return r7;
    L19:
        r7 = r2;
        goto L20
    L14:
        if (Intrinsics.areEqual(r0, emptyPath) == false) goto L16;
        r7 = r1;
        goto L20
    }
}
