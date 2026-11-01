package kotlin.p005io;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: FilePathComponents.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000$\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\u001a\u0011\u0010\u000b\u001a\u00020\f*\u00020\bH\u0002¢\u0006\u0002\b\r\u001a\u001c\u0010\u000e\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u000f\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\fH\u0000\u001a\f\u0010\u0011\u001a\u00020\u0012*\u00020\u0002H\u0000\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0000\u0010\u0003\"\u0018\u0010\u0004\u001a\u00020\u0002*\u00020\u00028@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006\"\u0018\u0010\u0007\u001a\u00020\b*\u00020\u00028@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\n¨\u0006\u0013"}, m421d2 = {"isRooted", "", "Ljava/io/File;", "(Ljava/io/File;)Z", "root", "getRoot", "(Ljava/io/File;)Ljava/io/File;", "rootName", "", "getRootName", "(Ljava/io/File;)Ljava/lang/String;", "getRootLength", "", "getRootLength$FilesKt__FilePathComponentsKt", "subPath", "beginIndex", "endIndex", "toComponents", "Lkotlin/io/FilePathComponents;", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/io/FilesKt")
class FilesKt__FilePathComponentsKt {
    public FilesKt__FilePathComponentsKt() {
    }

    public static final File getRoot(File r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return new File(FilesKt.getRootName(r2));
    }

    private static final int getRootLength$FilesKt__FilePathComponentsKt(String r10) {
        int r0 = StringsKt.indexOf$default(r10, File.separatorChar, 0, false, 4, null);
        if (r0 != 0) goto L18;
        if (r10.length() > 1) goto L7;
    L16:
        return 1;
    L7:
        if (r10.charAt(1) != File.separatorChar) goto L16;
        int r1 = StringsKt.indexOf$default(r10, File.separatorChar, 2, false, 4, null);
        if (r1 < 0) goto L16;
        int r2 = StringsKt.indexOf$default(r10, File.separatorChar, r1 + 1, false, 4, null);
        if (r2 < 0) goto L15;
        return r2 + 1;
    L15:
        return r10.length();
    L18:
        if (r0 <= 0) goto L24;
        if (r10.charAt(r0 - 1) != ':') goto L24;
        return r0 + 1;
    L24:
        if (r0 == (-1)) goto L26;
    L29:
        return 0;
    L26:
        if (StringsKt.endsWith$default(r10, ':', false, 2, null) == false) goto L29;
        return r10.length();
    }

    public static final String getRootName(File r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        String r0 = r3.getPath();
        Intrinsics.checkNotNullExpressionValue(r0, "getPath(...)");
        String r2 = r3.getPath();
        Intrinsics.checkNotNullExpressionValue(r2, "getPath(...)");
        String r1 = r0.substring(0, getRootLength$FilesKt__FilePathComponentsKt(r2));
        Intrinsics.checkNotNullExpressionValue(r1, "substring(...)");
        return r1;
    }

    public static final boolean isRooted(File r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        String r0 = r2.getPath();
        Intrinsics.checkNotNullExpressionValue(r0, "getPath(...)");
        if (getRootLength$FilesKt__FilePathComponentsKt(r0) <= 0) goto L5;
        return true;
    L5:
        return false;
    }

    public static final File subPath(File r1, int r2, int r3) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return FilesKt.toComponents(r1).subPath(r2, r3);
    }

    public static final FilePathComponents toComponents(File r14) {
        Intrinsics.checkNotNullParameter(r14, "<this>");
        String r0 = r14.getPath();
        Intrinsics.checkNotNull(r0);
        int r1 = getRootLength$FilesKt__FilePathComponentsKt(r0);
        String r3 = r0.substring(0, r1);
        Intrinsics.checkNotNullExpressionValue(r3, "substring(...)");
        String r5 = r0.substring(r1);
        Intrinsics.checkNotNullExpressionValue(r5, "substring(...)");
        if (r5.length() != 0) goto L5;
        boolean r4 = true;
    L6:
        if (r4 == false) goto L8;
        List r2 = CollectionsKt.emptyList();
    L14:
        return new FilePathComponents(new File(r3), r2);
    L8:
        List r6 = StringsKt.split$default(r5, new char[]{File.separatorChar}, false, 0, 6, null);
        ArrayList r7 = new ArrayList(CollectionsKt.collectionSizeOrDefault(r6, 10));
        Iterator r9 = r6.iterator();
    L10:
        if (r9.hasNext() == false) goto L12;
        r7.add(new File((String) r9.next()));
        goto L10
    L12:
        r2 = r7;
        goto L14
    L5:
        r4 = false;
        goto L6
    }
}
