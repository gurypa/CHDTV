package android.support.v4.media.session;

import android.media.session.PlaybackState;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.text.TextUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class PlaybackStateCompat implements Parcelable {
    public static final long ACTION_FAST_FORWARD = 64;
    public static final long ACTION_PAUSE = 2;
    public static final long ACTION_PLAY = 4;
    public static final long ACTION_PLAY_FROM_MEDIA_ID = 1024;
    public static final long ACTION_PLAY_FROM_SEARCH = 2048;
    public static final long ACTION_PLAY_FROM_URI = 8192;
    public static final long ACTION_PLAY_PAUSE = 512;
    public static final long ACTION_PREPARE = 16384;
    public static final long ACTION_PREPARE_FROM_MEDIA_ID = 32768;
    public static final long ACTION_PREPARE_FROM_SEARCH = 65536;
    public static final long ACTION_PREPARE_FROM_URI = 131072;
    public static final long ACTION_REWIND = 8;
    public static final long ACTION_SEEK_TO = 256;
    public static final long ACTION_SET_CAPTIONING_ENABLED = 1048576;
    public static final long ACTION_SET_PLAYBACK_SPEED = 4194304;
    public static final long ACTION_SET_RATING = 128;
    public static final long ACTION_SET_REPEAT_MODE = 262144;
    public static final long ACTION_SET_SHUFFLE_MODE = 2097152;

    @Deprecated
    public static final long ACTION_SET_SHUFFLE_MODE_ENABLED = 524288;
    public static final long ACTION_SKIP_TO_NEXT = 32;
    public static final long ACTION_SKIP_TO_PREVIOUS = 16;
    public static final long ACTION_SKIP_TO_QUEUE_ITEM = 4096;
    public static final long ACTION_STOP = 1;
    public static final Parcelable.Creator<PlaybackStateCompat> CREATOR = null;
    public static final int ERROR_CODE_ACTION_ABORTED = 10;
    public static final int ERROR_CODE_APP_ERROR = 1;
    public static final int ERROR_CODE_AUTHENTICATION_EXPIRED = 3;
    public static final int ERROR_CODE_CONCURRENT_STREAM_LIMIT = 5;
    public static final int ERROR_CODE_CONTENT_ALREADY_PLAYING = 8;
    public static final int ERROR_CODE_END_OF_QUEUE = 11;
    public static final int ERROR_CODE_NOT_AVAILABLE_IN_REGION = 7;
    public static final int ERROR_CODE_NOT_SUPPORTED = 2;
    public static final int ERROR_CODE_PARENTAL_CONTROL_RESTRICTED = 6;
    public static final int ERROR_CODE_PREMIUM_ACCOUNT_REQUIRED = 4;
    public static final int ERROR_CODE_SKIP_LIMIT_REACHED = 9;
    public static final int ERROR_CODE_UNKNOWN_ERROR = 0;
    private static final int KEYCODE_MEDIA_PAUSE = 127;
    private static final int KEYCODE_MEDIA_PLAY = 126;
    public static final long PLAYBACK_POSITION_UNKNOWN = -1;
    public static final int REPEAT_MODE_ALL = 2;
    public static final int REPEAT_MODE_GROUP = 3;
    public static final int REPEAT_MODE_INVALID = -1;
    public static final int REPEAT_MODE_NONE = 0;
    public static final int REPEAT_MODE_ONE = 1;
    public static final int SHUFFLE_MODE_ALL = 1;
    public static final int SHUFFLE_MODE_GROUP = 2;
    public static final int SHUFFLE_MODE_INVALID = -1;
    public static final int SHUFFLE_MODE_NONE = 0;
    public static final int STATE_BUFFERING = 6;
    public static final int STATE_CONNECTING = 8;
    public static final int STATE_ERROR = 7;
    public static final int STATE_FAST_FORWARDING = 4;
    public static final int STATE_NONE = 0;
    public static final int STATE_PAUSED = 2;
    public static final int STATE_PLAYING = 3;
    public static final int STATE_REWINDING = 5;
    public static final int STATE_SKIPPING_TO_NEXT = 10;
    public static final int STATE_SKIPPING_TO_PREVIOUS = 9;
    public static final int STATE_SKIPPING_TO_QUEUE_ITEM = 11;
    public static final int STATE_STOPPED = 1;
    final long mActions;
    final long mActiveItemId;
    final long mBufferedPosition;
    List<CustomAction> mCustomActions;
    final int mErrorCode;
    final CharSequence mErrorMessage;
    final Bundle mExtras;
    final long mPosition;
    final float mSpeed;
    final int mState;
    private PlaybackState mStateFwk;
    final long mUpdateTime;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Actions {
    }

    private static class Api21Impl {
        private Api21Impl() {
        }

        static void addCustomAction(PlaybackState.Builder r0, PlaybackState.CustomAction r1) {
            r0.addCustomAction(r1);
        }

        static PlaybackState.CustomAction build(PlaybackState.CustomAction.Builder r1) {
            return r1.build();
        }

        static PlaybackState build(PlaybackState.Builder r1) {
            return r1.build();
        }

        static PlaybackState.Builder createBuilder() {
            return new PlaybackState.Builder();
        }

        static PlaybackState.CustomAction.Builder createCustomActionBuilder(String r1, CharSequence r2, int r3) {
            return new PlaybackState.CustomAction.Builder(r1, r2, r3);
        }

        static String getAction(PlaybackState.CustomAction r1) {
            return r1.getAction();
        }

        static long getActions(PlaybackState r2) {
            return r2.getActions();
        }

        static long getActiveQueueItemId(PlaybackState r2) {
            return r2.getActiveQueueItemId();
        }

        static long getBufferedPosition(PlaybackState r2) {
            return r2.getBufferedPosition();
        }

        static List<PlaybackState.CustomAction> getCustomActions(PlaybackState r1) {
            return r1.getCustomActions();
        }

        static CharSequence getErrorMessage(PlaybackState r1) {
            return r1.getErrorMessage();
        }

        static Bundle getExtras(PlaybackState.CustomAction r1) {
            return r1.getExtras();
        }

        static int getIcon(PlaybackState.CustomAction r1) {
            return r1.getIcon();
        }

        static long getLastPositionUpdateTime(PlaybackState r2) {
            return r2.getLastPositionUpdateTime();
        }

        static CharSequence getName(PlaybackState.CustomAction r1) {
            return r1.getName();
        }

        static float getPlaybackSpeed(PlaybackState r1) {
            return r1.getPlaybackSpeed();
        }

        static long getPosition(PlaybackState r2) {
            return r2.getPosition();
        }

        static int getState(PlaybackState r1) {
            return r1.getState();
        }

        static void setActions(PlaybackState.Builder r0, long r1) {
            r0.setActions(r1);
        }

        static void setActiveQueueItemId(PlaybackState.Builder r0, long r1) {
            r0.setActiveQueueItemId(r1);
        }

        static void setBufferedPosition(PlaybackState.Builder r0, long r1) {
            r0.setBufferedPosition(r1);
        }

        static void setErrorMessage(PlaybackState.Builder r0, CharSequence r1) {
            r0.setErrorMessage(r1);
        }

        static void setExtras(PlaybackState.CustomAction.Builder r0, Bundle r1) {
            r0.setExtras(r1);
        }

        static void setState(PlaybackState.Builder r0, int r1, long r2, float r4, long r5) {
            r0.setState(r1, r2, r4, r5);
        }
    }

    private static class Api22Impl {
        private Api22Impl() {
        }

        static Bundle getExtras(PlaybackState r1) {
            return r1.getExtras();
        }

        static void setExtras(PlaybackState.Builder r0, Bundle r1) {
            r0.setExtras(r1);
        }
    }

    public static final class Builder {
        private long mActions;
        private long mActiveItemId;
        private long mBufferedPosition;
        private final List<CustomAction> mCustomActions;
        private int mErrorCode;
        private CharSequence mErrorMessage;
        private Bundle mExtras;
        private long mPosition;
        private float mRate;
        private int mState;
        private long mUpdateTime;

        public Builder() {
            this.mCustomActions = new ArrayList();
            this.mActiveItemId = -1;
        }

        public Builder(PlaybackStateCompat r3) {
            this.mCustomActions = new ArrayList();
            this.mActiveItemId = -1;
            this.mState = r3.mState;
            this.mPosition = r3.mPosition;
            this.mRate = r3.mSpeed;
            this.mUpdateTime = r3.mUpdateTime;
            this.mBufferedPosition = r3.mBufferedPosition;
            this.mActions = r3.mActions;
            this.mErrorCode = r3.mErrorCode;
            this.mErrorMessage = r3.mErrorMessage;
            if (r3.mCustomActions == null) goto L5;
            this.mCustomActions.addAll(r3.mCustomActions);
        L5:
            this.mActiveItemId = r3.mActiveItemId;
            this.mExtras = r3.mExtras;
        }

        public Builder addCustomAction(CustomAction r3) {
            if (r3 == null) goto L6;
            this.mCustomActions.add(r3);
            return this;
        L6:
            throw new IllegalArgumentException("You may not add a null CustomAction to PlaybackStateCompat");
        }

        public Builder addCustomAction(String r3, String r4, int r5) {
            return addCustomAction(new CustomAction(r3, r4, r5, null));
        }

        public PlaybackStateCompat build() {
            return new PlaybackStateCompat(this.mState, this.mPosition, this.mBufferedPosition, this.mRate, this.mActions, this.mErrorCode, this.mErrorMessage, this.mUpdateTime, this.mCustomActions, this.mActiveItemId, this.mExtras);
        }

        public Builder setActions(long r1) {
            this.mActions = r1;
            return this;
        }

        public Builder setActiveQueueItemId(long r1) {
            this.mActiveItemId = r1;
            return this;
        }

        public Builder setBufferedPosition(long r1) {
            this.mBufferedPosition = r1;
            return this;
        }

        public Builder setErrorMessage(int r1, CharSequence r2) {
            this.mErrorCode = r1;
            this.mErrorMessage = r2;
            return this;
        }

        @Deprecated
        public Builder setErrorMessage(CharSequence r1) {
            this.mErrorMessage = r1;
            return this;
        }

        public Builder setExtras(Bundle r1) {
            this.mExtras = r1;
            return this;
        }

        public Builder setState(int r8, long r9, float r11) {
            return setState(r8, r9, r11, SystemClock.elapsedRealtime());
        }

        public Builder setState(int r1, long r2, float r4, long r5) {
            this.mState = r1;
            this.mPosition = r2;
            this.mUpdateTime = r5;
            this.mRate = r4;
            return this;
        }
    }

    public static final class CustomAction implements Parcelable {
        public static final Parcelable.Creator<CustomAction> CREATOR = null;
        private final String mAction;
        private PlaybackState.CustomAction mCustomActionFwk;
        private final Bundle mExtras;
        private final int mIcon;
        private final CharSequence mName;

        public static final class Builder {
            private final String mAction;
            private Bundle mExtras;
            private final int mIcon;
            private final CharSequence mName;

            public Builder(String r3, CharSequence r4, int r5) {
                if (TextUtils.isEmpty(r3) == true) goto L14;
                if (TextUtils.isEmpty(r4) == true) goto L12;
                if (r5 == 0) goto L10;
                this.mAction = r3;
                this.mName = r4;
                this.mIcon = r5;
                return;
            L10:
                throw new IllegalArgumentException("You must specify an icon resource id to build a CustomAction");
            L12:
                throw new IllegalArgumentException("You must specify a name to build a CustomAction");
            L14:
                throw new IllegalArgumentException("You must specify an action to build a CustomAction");
            }

            public CustomAction build() {
                return new CustomAction(this.mAction, this.mName, this.mIcon, this.mExtras);
            }

            public Builder setExtras(Bundle r1) {
                this.mExtras = r1;
                return this;
            }
        }

        static {
            CREATOR = new C00281();
        }

        CustomAction(Parcel r2) {
            this.mAction = r2.readString();
            this.mName = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(r2);
            this.mIcon = r2.readInt();
            this.mExtras = r2.readBundle(MediaSessionCompat.class.getClassLoader());
        }

        CustomAction(String r1, CharSequence r2, int r3, Bundle r4) {
            this.mAction = r1;
            this.mName = r2;
            this.mIcon = r3;
            this.mExtras = r4;
        }

        public static CustomAction fromCustomAction(Object r6) {
            if (r6 == null) goto L5;
            PlaybackState.CustomAction r0 = (PlaybackState.CustomAction) r6;
            Bundle r1 = Api21Impl.getExtras(r0);
            MediaSessionCompat.ensureClassLoader(r1);
            CustomAction r2 = new CustomAction(Api21Impl.getAction(r0), Api21Impl.getName(r0), Api21Impl.getIcon(r0), r1);
            r2.mCustomActionFwk = r0;
            return r2;
        L5:
            return null;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public String getAction() {
            return this.mAction;
        }

        public Object getCustomAction() {
            if (this.mCustomActionFwk != null) goto L7;
            PlaybackState.CustomAction.Builder r0 = Api21Impl.createCustomActionBuilder(this.mAction, this.mName, this.mIcon);
            Api21Impl.setExtras(r0, this.mExtras);
            return Api21Impl.build(r0);
        L7:
            return this.mCustomActionFwk;
        }

        public Bundle getExtras() {
            return this.mExtras;
        }

        public int getIcon() {
            return this.mIcon;
        }

        public CharSequence getName() {
            return this.mName;
        }

        public String toString() {
            return "Action:mName='" + this.mName + ", mIcon=" + this.mIcon + ", mExtras=" + this.mExtras;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel r2, int r3) {
            r2.writeString(this.mAction);
            TextUtils.writeToParcel(this.mName, r2, r3);
            r2.writeInt(this.mIcon);
            r2.writeBundle(this.mExtras);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface MediaKeyAction {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RepeatMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ShuffleMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

    static {
        CREATOR = new C00271();
    }

    PlaybackStateCompat(int r15, long r16, long r18, float r20, long r21, int r23, CharSequence r24, long r25, List<CustomAction> r27, long r28, Bundle r30) {
        this.mState = r15;
        this.mPosition = r16;
        this.mBufferedPosition = r18;
        this.mSpeed = r20;
        this.mActions = r21;
        this.mErrorCode = r23;
        this.mErrorMessage = r24;
        this.mUpdateTime = r25;
        this.mCustomActions = new ArrayList(r27);
        this.mActiveItemId = r28;
        this.mExtras = r30;
    }

    PlaybackStateCompat(Parcel r3) {
        this.mState = r3.readInt();
        this.mPosition = r3.readLong();
        this.mSpeed = r3.readFloat();
        this.mUpdateTime = r3.readLong();
        this.mBufferedPosition = r3.readLong();
        this.mActions = r3.readLong();
        this.mErrorMessage = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(r3);
        this.mCustomActions = r3.createTypedArrayList(CustomAction.CREATOR);
        this.mActiveItemId = r3.readLong();
        this.mExtras = r3.readBundle(MediaSessionCompat.class.getClassLoader());
        this.mErrorCode = r3.readInt();
    }

    public static PlaybackStateCompat fromPlaybackState(Object r23) {
        if (r23 == null) goto L13;
        PlaybackState r0 = (PlaybackState) r23;
        List<PlaybackState.CustomAction> r1 = Api21Impl.getCustomActions(r0);
        if (r1 == null) goto L10;
        ArrayList r3 = new ArrayList(r1.size());
        Iterator<PlaybackState.CustomAction> r4 = r1.iterator();
    L7:
        if (r4.hasNext() == false) goto L9;
        r3.add(CustomAction.fromCustomAction(r4.next()));
        goto L7
    L9:
        ArrayList r19 = r3;
    L11:
        Bundle r22 = Api22Impl.getExtras(r0);
        MediaSessionCompat.ensureClassLoader(r22);
        PlaybackStateCompat r6 = new PlaybackStateCompat(Api21Impl.getState(r0), Api21Impl.getPosition(r0), Api21Impl.getBufferedPosition(r0), Api21Impl.getPlaybackSpeed(r0), Api21Impl.getActions(r0), 0, Api21Impl.getErrorMessage(r0), Api21Impl.getLastPositionUpdateTime(r0), r19, Api21Impl.getActiveQueueItemId(r0), r22);
        r6.mStateFwk = r0;
        return r6;
    L10:
        r19 = null;
        goto L11
    L13:
        return null;
    }

    public static int toKeyCode(long r2) {
        if (r2 != 4) goto L7;
        return KEYCODE_MEDIA_PLAY;
    L7:
        if (r2 != 2) goto L11;
        return KEYCODE_MEDIA_PAUSE;
    L11:
        if (r2 != 32) goto L15;
        return 87;
    L15:
        if (r2 != 16) goto L19;
        return 88;
    L19:
        if (r2 != 1) goto L23;
        return 86;
    L23:
        if (r2 != 64) goto L27;
        return 90;
    L27:
        if (r2 != 8) goto L31;
        return 89;
    L31:
        if (r2 != 512) goto L34;
        return 85;
    L34:
        return 0;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public long getActions() {
        return this.mActions;
    }

    public long getActiveQueueItemId() {
        return this.mActiveItemId;
    }

    public long getBufferedPosition() {
        return this.mBufferedPosition;
    }

    public long getCurrentPosition(Long r8) {
        long r0 = this.mPosition;
        float r2 = this.mSpeed;
        if (r8 == null) goto L5;
        long r3 = r8.longValue();
    L7:
        return Math.max(0, r0 + ((long) (r2 * r3)));
    L5:
        r3 = SystemClock.elapsedRealtime() - this.mUpdateTime;
        goto L7
    }

    public List<CustomAction> getCustomActions() {
        return this.mCustomActions;
    }

    public int getErrorCode() {
        return this.mErrorCode;
    }

    public CharSequence getErrorMessage() {
        return this.mErrorMessage;
    }

    public Bundle getExtras() {
        return this.mExtras;
    }

    public long getLastPositionUpdateTime() {
        return this.mUpdateTime;
    }

    public float getPlaybackSpeed() {
        return this.mSpeed;
    }

    public Object getPlaybackState() {
        if (this.mStateFwk != null) goto L10;
        PlaybackState.Builder r1 = Api21Impl.createBuilder();
        Api21Impl.setState(r1, this.mState, this.mPosition, this.mSpeed, this.mUpdateTime);
        Api21Impl.setBufferedPosition(r1, this.mBufferedPosition);
        Api21Impl.setActions(r1, this.mActions);
        Api21Impl.setErrorMessage(r1, this.mErrorMessage);
        Iterator<CustomAction> r0 = this.mCustomActions.iterator();
    L6:
        if (r0.hasNext() == false) goto L8;
        Api21Impl.addCustomAction(r1, (PlaybackState.CustomAction) r0.next().getCustomAction());
        goto L6
    L8:
        Api21Impl.setActiveQueueItemId(r1, this.mActiveItemId);
        Api22Impl.setExtras(r1, this.mExtras);
        this.mStateFwk = Api21Impl.build(r1);
    L10:
        return this.mStateFwk;
    }

    public long getPosition() {
        return this.mPosition;
    }

    public int getState() {
        return this.mState;
    }

    public String toString() {
        StringBuilder r0 = new StringBuilder("PlaybackState {");
        r0.append("state=").append(this.mState);
        r0.append(", position=").append(this.mPosition);
        r0.append(", buffered position=").append(this.mBufferedPosition);
        r0.append(", speed=").append(this.mSpeed);
        r0.append(", updated=").append(this.mUpdateTime);
        r0.append(", actions=").append(this.mActions);
        r0.append(", error code=").append(this.mErrorCode);
        r0.append(", error message=").append(this.mErrorMessage);
        r0.append(", custom actions=").append(this.mCustomActions);
        r0.append(", active item id=").append(this.mActiveItemId);
        r0.append("}");
        return r0.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel r3, int r4) {
        r3.writeInt(this.mState);
        r3.writeLong(this.mPosition);
        r3.writeFloat(this.mSpeed);
        r3.writeLong(this.mUpdateTime);
        r3.writeLong(this.mBufferedPosition);
        r3.writeLong(this.mActions);
        TextUtils.writeToParcel(this.mErrorMessage, r3, r4);
        r3.writeTypedList(this.mCustomActions);
        r3.writeLong(this.mActiveItemId);
        r3.writeBundle(this.mExtras);
        r3.writeInt(this.mErrorCode);
    }
}
