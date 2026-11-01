package kotlin.p005io.path;

import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: PathRecursiveFunctions.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\bÃ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001c\u0010\u0003\u001a\u00020\u0004*\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\bH\u0016¨\u0006\t"}, m421d2 = {"Lkotlin/io/path/DefaultCopyActionContext;", "Lkotlin/io/path/CopyActionContext;", "()V", "copyToIgnoringExistingDirectory", "Lkotlin/io/path/CopyActionResult;", "Ljava/nio/file/Path;", "target", "followLinks", "", "kotlin-stdlib-jdk7"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
final class DefaultCopyActionContext implements CopyActionContext {
    public static final DefaultCopyActionContext INSTANCE = null;

    static {
        INSTANCE = new DefaultCopyActionContext();
    }

    private DefaultCopyActionContext() {
    }

    @Override // kotlin.p005io.path.CopyActionContext
    public CopyActionResult copyToIgnoringExistingDirectory(Path r6, Path r7, boolean r8) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        Intrinsics.checkNotNullParameter(r7, "target");
        LinkOption[] r0 = LinkFollowing.INSTANCE.toLinkOptions(r8);
        LinkOption[] r1 = (LinkOption[]) Arrays.copyOf(r0, r0.length);
        if (Files.isDirectory(r6, (LinkOption[]) Arrays.copyOf(r1, r1.length)) == true) goto L5;
    L6:
        CopyOption[] r2 = (CopyOption[]) Arrays.copyOf(r0, r0.length);
        Intrinsics.checkNotNullExpressionValue(Files.copy(r6, r7, (CopyOption[]) Arrays.copyOf(r2, r2.length)), "copy(...)");
    L8:
        return CopyActionResult.CONTINUE;
    L5:
        if (Files.isDirectory(r7, (LinkOption[]) Arrays.copyOf(new LinkOption[]{LinkOption.NOFOLLOW_LINKS}, 1)) == true) goto L8;
        goto L6
    }
}
