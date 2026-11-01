package kotlin.p005io.path;

import java.nio.file.FileVisitOption;
import java.nio.file.LinkOption;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.SetsKt;

/* JADX INFO: compiled from: PathTreeWalk.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\bÀ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0019\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\r\u001a\u00020\u000e¢\u0006\u0002\u0010\u000fJ\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\r\u001a\u00020\u000eR\u0016\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0006R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0006R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0011"}, m421d2 = {"Lkotlin/io/path/LinkFollowing;", "", "()V", "followLinkOption", "", "Ljava/nio/file/LinkOption;", "[Ljava/nio/file/LinkOption;", "followVisitOption", "", "Ljava/nio/file/FileVisitOption;", "nofollowLinkOption", "nofollowVisitOption", "toLinkOptions", "followLinks", "", "(Z)[Ljava/nio/file/LinkOption;", "toVisitOptions", "kotlin-stdlib-jdk7"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class LinkFollowing {
    public static final LinkFollowing INSTANCE = null;
    private static final LinkOption[] followLinkOption = null;
    private static final Set<FileVisitOption> followVisitOption = null;
    private static final LinkOption[] nofollowLinkOption = null;
    private static final Set<FileVisitOption> nofollowVisitOption = null;

    static {
        INSTANCE = new LinkFollowing();
        nofollowLinkOption = new LinkOption[]{LinkOption.NOFOLLOW_LINKS};
        followLinkOption = new LinkOption[0];
        nofollowVisitOption = SetsKt.emptySet();
        followVisitOption = SetsKt.setOf(FileVisitOption.FOLLOW_LINKS);
    }

    private LinkFollowing() {
    }

    public final LinkOption[] toLinkOptions(boolean r2) {
        if (r2 == false) goto L5;
        return followLinkOption;
    L5:
        return nofollowLinkOption;
    }

    public final Set<FileVisitOption> toVisitOptions(boolean r2) {
        if (r2 == false) goto L5;
        return followVisitOption;
    L5:
        return nofollowVisitOption;
    }
}
