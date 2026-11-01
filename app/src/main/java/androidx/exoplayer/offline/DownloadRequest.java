package androidx.media3.exoplayer.offline;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.StreamKey;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class DownloadRequest implements Parcelable {
    public static final Parcelable.Creator<DownloadRequest> CREATOR = null;
    public final String customCacheKey;
    public final byte[] data;

    /* JADX INFO: renamed from: id */
    public final String f41id;
    public final byte[] keySetId;
    public final String mimeType;
    public final List<StreamKey> streamKeys;
    public final Uri uri;

    public static class Builder {
        private String customCacheKey;
        private byte[] data;

        /* JADX INFO: renamed from: id */
        private final String f42id;
        private byte[] keySetId;
        private String mimeType;
        private List<StreamKey> streamKeys;
        private final Uri uri;

        public Builder(String r1, Uri r2) {
            this.f42id = r1;
            this.uri = r2;
        }

        public DownloadRequest build() {
            String r1 = this.f42id;
            Uri r2 = this.uri;
            String r3 = this.mimeType;
            if (this.streamKeys == null) goto L5;
            List r4 = this.streamKeys;
        L7:
            return new DownloadRequest(r1, r2, r3, r4, this.keySetId, this.customCacheKey, this.data, null);
        L5:
            r4 = ImmutableList.m212of();
            goto L7
        }

        public Builder setCustomCacheKey(String r1) {
            this.customCacheKey = r1;
            return this;
        }

        public Builder setData(byte[] r1) {
            this.data = r1;
            return this;
        }

        public Builder setKeySetId(byte[] r1) {
            this.keySetId = r1;
            return this;
        }

        public Builder setMimeType(String r2) {
            this.mimeType = MimeTypes.normalizeMimeType(r2);
            return this;
        }

        public Builder setStreamKeys(List<StreamKey> r1) {
            this.streamKeys = r1;
            return this;
        }
    }

    public static class UnsupportedRequestException extends IOException {
        public UnsupportedRequestException() {
        }
    }

    static {
        CREATOR = new C03171();
    }

    DownloadRequest(Parcel r5) {
        this.f41id = (String) Util.castNonNull(r5.readString());
        this.uri = Uri.parse((String) Util.castNonNull(r5.readString()));
        this.mimeType = r5.readString();
        int r0 = r5.readInt();
        ArrayList r1 = new ArrayList(r0);
        int r2 = 0;
    L3:
        if (r2 >= r0) goto L5;
        r1.add((StreamKey) r5.readParcelable(StreamKey.class.getClassLoader()));
        r2 = r2 + 1;
        goto L3
    L5:
        this.streamKeys = Collections.unmodifiableList(r1);
        this.keySetId = r5.createByteArray();
        this.customCacheKey = r5.readString();
        this.data = (byte[]) Util.castNonNull(r5.createByteArray());
    }

    private DownloadRequest(String r5, Uri r6, String r7, List<StreamKey> r8, byte[] r9, String r10, byte[] r11) {
        int r0 = Util.inferContentTypeForUriAndMimeType(r6, r7);
        boolean r1 = true;
        if (r0 != 0) goto L5;
    L7:
        if (r10 == null) goto L10;
        r1 = false;
    L10:
        Assertions.checkArgument(r1, "customCacheKey must be null for type: " + r0);
    L11:
        this.f41id = r5;
        this.uri = r6;
        this.mimeType = r7;
        ArrayList r2 = new ArrayList(r8);
        Collections.sort(r2);
        this.streamKeys = Collections.unmodifiableList(r2);
        if (r9 == null) goto L14;
        byte[] r3 = Arrays.copyOf(r9, r9.length);
    L15:
        this.keySetId = r3;
        this.customCacheKey = r10;
        if (r11 == null) goto L18;
        byte[] r4 = Arrays.copyOf(r11, r11.length);
    L19:
        this.data = r4;
        return;
    L18:
        r4 = Util.EMPTY_BYTE_ARRAY;
        goto L19
    L14:
        r3 = null;
        goto L15
    L5:
        if (r0 == 2) goto L7;
        if (r0 != 1) goto L11;
        goto L7
    }

    /* synthetic */ DownloadRequest(String r1, Uri r2, String r3, List r4, byte[] r5, String r6, byte[] r7, C03171 r8) {
        this(r1, r2, r3, r4, r5, r6, r7);
    }

    public DownloadRequest copyWithId(String r9) {
        return new DownloadRequest(r9, this.uri, this.mimeType, this.streamKeys, this.keySetId, this.customCacheKey, this.data);
    }

    public DownloadRequest copyWithKeySetId(byte[] r9) {
        return new DownloadRequest(this.f41id, this.uri, this.mimeType, this.streamKeys, r9, this.customCacheKey, this.data);
    }

    public DownloadRequest copyWithMergedRequest(DownloadRequest r10) {
        Assertions.checkArgument(this.f41id.equals(r10.f41id));
        if (this.streamKeys.isEmpty() == false) goto L5;
    L15:
        List r5 = Collections.emptyList();
    L17:
        return new DownloadRequest(this.f41id, r10.uri, r10.mimeType, r5, r10.keySetId, r10.customCacheKey, r10.data);
    L5:
        if (r10.streamKeys.isEmpty() == true) goto L15;
        ArrayList r0 = new ArrayList(this.streamKeys);
        int r1 = 0;
    L9:
        if (r1 >= r10.streamKeys.size()) goto L14;
        StreamKey r2 = r10.streamKeys.get(r1);
        if (r0.contains(r2) == true) goto L13;
        r0.add(r2);
    L13:
        r1 = r1 + 1;
        goto L9
    L14:
        r5 = r0;
        goto L17
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object r5) {
        if ((r5 instanceof DownloadRequest) == true) goto L5;
        return false;
    L5:
        DownloadRequest r0 = (DownloadRequest) r5;
        if (this.f41id.equals(r0.f41id) == true) goto L8;
        return false;
    L8:
        if (this.uri.equals(r0.uri) == true) goto L10;
        return false;
    L10:
        if (Util.areEqual(this.mimeType, r0.mimeType) == true) goto L12;
        return false;
    L12:
        if (this.streamKeys.equals(r0.streamKeys) == true) goto L14;
        return false;
    L14:
        if (Arrays.equals(this.keySetId, r0.keySetId) == true) goto L16;
        return false;
    L16:
        if (Util.areEqual(this.customCacheKey, r0.customCacheKey) == true) goto L18;
        return false;
    L18:
        if (Arrays.equals(this.data, r0.data) == false) goto L28;
        return true;
    L28:
        return false;
    }

    public final int hashCode() {
        int r0 = (((this.f41id.hashCode() * 31) * 31) + this.uri.hashCode()) * 31;
        int r3 = 0;
        if (this.mimeType == null) goto L5;
        int r2 = this.mimeType.hashCode();
    L6:
        int r1 = (((((r0 + r2) * 31) + this.streamKeys.hashCode()) * 31) + Arrays.hashCode(this.keySetId)) * 31;
        if (this.customCacheKey == null) goto L10;
        r3 = this.customCacheKey.hashCode();
    L10:
        return ((r1 + r3) * 31) + Arrays.hashCode(this.data);
    L5:
        r2 = 0;
        goto L6
    }

    public MediaItem toMediaItem() {
        return new MediaItem.Builder().setMediaId(this.f41id).setUri(this.uri).setCustomCacheKey(this.customCacheKey).setMimeType(this.mimeType).setStreamKeys(this.streamKeys).build();
    }

    public String toString() {
        return this.mimeType + ":" + this.f41id;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r4, int r5) {
        r4.writeString(this.f41id);
        r4.writeString(this.uri.toString());
        r4.writeString(this.mimeType);
        r4.writeInt(this.streamKeys.size());
        int r0 = 0;
    L4:
        if (r0 >= this.streamKeys.size()) goto L6;
        r4.writeParcelable(this.streamKeys.get(r0), 0);
        r0 = r0 + 1;
        goto L4
    L6:
        r4.writeByteArray(this.keySetId);
        r4.writeString(this.customCacheKey);
        r4.writeByteArray(this.data);
    }
}
