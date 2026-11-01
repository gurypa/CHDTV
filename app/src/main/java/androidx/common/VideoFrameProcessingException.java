package androidx.media3.common;

/* JADX INFO: loaded from: classes.dex */
public final class VideoFrameProcessingException extends Exception {
    public final long presentationTimeUs;

    public VideoFrameProcessingException(String r3) {
        this(r3, C0180C.TIME_UNSET);
    }

    public VideoFrameProcessingException(String r1, long r2) {
        super(r1);
        this.presentationTimeUs = r2;
    }

    public VideoFrameProcessingException(String r3, Throwable r4) {
        this(r3, r4, C0180C.TIME_UNSET);
    }

    public VideoFrameProcessingException(String r1, Throwable r2, long r3) {
        super(r1, r2);
        this.presentationTimeUs = r3;
    }

    public VideoFrameProcessingException(Throwable r3) {
        this(r3, C0180C.TIME_UNSET);
    }

    public VideoFrameProcessingException(Throwable r1, long r2) {
        super(r1);
        this.presentationTimeUs = r2;
    }

    public static VideoFrameProcessingException from(Exception r2) {
        return from(r2, C0180C.TIME_UNSET);
    }

    public static VideoFrameProcessingException from(Exception r1, long r2) {
        if ((r1 instanceof VideoFrameProcessingException) == false) goto L7;
        return (VideoFrameProcessingException) r1;
    L7:
        return new VideoFrameProcessingException(r1, r2);
    }
}
