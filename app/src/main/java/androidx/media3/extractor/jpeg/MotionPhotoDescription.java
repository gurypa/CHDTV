package androidx.media3.extractor.jpeg;

import androidx.media3.common.MimeTypes;
import androidx.media3.extractor.metadata.mp4.MotionPhotoMetadata;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class MotionPhotoDescription {
    public final List<ContainerItem> items;
    public final long photoPresentationTimestampUs;

    public static final class ContainerItem {
        public final long length;
        public final String mime;
        public final long padding;
        public final String semantic;

        public ContainerItem(String r1, String r2, long r3, long r5) {
            this.mime = r1;
            this.semantic = r2;
            this.length = r3;
            this.padding = r5;
        }
    }

    public MotionPhotoDescription(long r1, List<ContainerItem> r3) {
        this.photoPresentationTimestampUs = r1;
        this.items = r3;
    }

    public MotionPhotoMetadata getMotionPhotoMetadata(long r28) {
        if (this.items.size() >= 2) goto L5;
        return null;
    L5:
        boolean r10 = false;
        long r11 = r28;
        int r15 = this.items.size() - 1;
        long r17 = -1;
        long r19 = -1;
        long r23 = -1;
        long r25 = -1;
    L6:
        if (r15 < 0) goto L20;
        ContainerItem r1 = this.items.get(r15);
        boolean r2 = MimeTypes.VIDEO_MP4.equals(r1.mime) | r10;
        long r4 = r11;
        if (r15 != 0) goto L10;
        r11 = 0;
        long r13 = r4 - r1.padding;
    L11:
        if (r2 == true) goto L13;
    L15:
        r10 = r2;
    L16:
        if (r15 != 0) goto L18;
        r17 = r11;
        r19 = r13;
    L18:
        r15 = r15 - 1;
        goto L6
    L13:
        if (r11 == r13) goto L15;
        r23 = r11;
        r25 = r13 - r11;
        r10 = false;
        goto L16
    L10:
        r11 = r11 - r1.length;
        r13 = r4;
        goto L11
    L20:
        if (r23 != (-1)) goto L22;
    L30:
        return null;
    L22:
        if (r25 == (-1)) goto L30;
        if (r17 == (-1)) goto L30;
        if (r19 == (-1)) goto L30;
        return new MotionPhotoMetadata(r17, r19, this.photoPresentationTimestampUs, r23, r25);
    }
}
