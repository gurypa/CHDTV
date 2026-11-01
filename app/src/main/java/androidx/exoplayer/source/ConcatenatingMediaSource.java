package androidx.media3.exoplayer.source;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Timeline;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.Util;
import androidx.media3.datasource.TransferListener;
import androidx.media3.exoplayer.AbstractConcatenatedTimeline;
import androidx.media3.exoplayer.upstream.Allocator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
public final class ConcatenatingMediaSource extends CompositeMediaSource<MediaSourceHolder> {
    private static final int MSG_ADD = 1;
    private static final int MSG_MOVE = 3;
    private static final int MSG_ON_COMPLETION = 6;
    private static final int MSG_REMOVE = 2;
    private static final int MSG_SET_SHUFFLE_ORDER = 4;
    private static final int MSG_UPDATE_TIMELINE = 5;
    private static final MediaItem PLACEHOLDER_MEDIA_ITEM = null;
    private final Set<MediaSourceHolder> enabledMediaSourceHolders;
    private final boolean isAtomic;
    private final IdentityHashMap<MediaPeriod, MediaSourceHolder> mediaSourceByMediaPeriod;
    private final Map<Object, MediaSourceHolder> mediaSourceByUid;
    private final List<MediaSourceHolder> mediaSourceHolders;
    private final List<MediaSourceHolder> mediaSourcesPublic;
    private Set<HandlerAndRunnable> nextTimelineUpdateOnCompletionActions;
    private final Set<HandlerAndRunnable> pendingOnCompletionActions;
    private Handler playbackThreadHandler;
    private ShuffleOrder shuffleOrder;
    private boolean timelineUpdateScheduled;
    private final boolean useLazyPreparation;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.source.ConcatenatingMediaSource$1 */
    static /* synthetic */ class C03261 {
    }

    private static final class ConcatenatedTimeline extends AbstractConcatenatedTimeline {
        private final HashMap<Object, Integer> childIndexByUid;
        private final int[] firstPeriodInChildIndices;
        private final int[] firstWindowInChildIndices;
        private final int periodCount;
        private final Timeline[] timelines;
        private final Object[] uids;
        private final int windowCount;

        public ConcatenatedTimeline(Collection<MediaSourceHolder> r10, ShuffleOrder r11, boolean r12) {
            super(r12, r11);
            int r0 = r10.size();
            this.firstPeriodInChildIndices = new int[r0];
            this.firstWindowInChildIndices = new int[r0];
            this.timelines = new Timeline[r0];
            this.uids = new Object[r0];
            this.childIndexByUid = new HashMap();
            int r1 = 0;
            int r2 = 0;
            int r3 = 0;
            Iterator<MediaSourceHolder> r4 = r10.iterator();
        L4:
            if (r4.hasNext() == false) goto L6;
            MediaSourceHolder r5 = r4.next();
            this.timelines[r1] = r5.mediaSource.getTimeline();
            this.firstWindowInChildIndices[r1] = r2;
            this.firstPeriodInChildIndices[r1] = r3;
            r2 = r2 + this.timelines[r1].getWindowCount();
            r3 = r3 + this.timelines[r1].getPeriodCount();
            this.uids[r1] = r5.uid;
            int r8 = r1 + 1;
            this.childIndexByUid.put(this.uids[r1], Integer.valueOf(r1));
            r1 = r8;
            goto L4
        L6:
            this.windowCount = r2;
            this.periodCount = r3;
        }

        @Override // androidx.media3.exoplayer.AbstractConcatenatedTimeline
        protected int getChildIndexByChildUid(Object r3) {
            Integer r0 = this.childIndexByUid.get(r3);
            if (r0 != null) goto L6;
            return -1;
        L6:
            return r0.intValue();
        }

        @Override // androidx.media3.exoplayer.AbstractConcatenatedTimeline
        protected int getChildIndexByPeriodIndex(int r4) {
            return Util.binarySearchFloor(this.firstPeriodInChildIndices, r4 + 1, false, false);
        }

        @Override // androidx.media3.exoplayer.AbstractConcatenatedTimeline
        protected int getChildIndexByWindowIndex(int r4) {
            return Util.binarySearchFloor(this.firstWindowInChildIndices, r4 + 1, false, false);
        }

        @Override // androidx.media3.exoplayer.AbstractConcatenatedTimeline
        protected Object getChildUidByChildIndex(int r2) {
            return this.uids[r2];
        }

        @Override // androidx.media3.exoplayer.AbstractConcatenatedTimeline
        protected int getFirstPeriodIndexByChildIndex(int r2) {
            return this.firstPeriodInChildIndices[r2];
        }

        @Override // androidx.media3.exoplayer.AbstractConcatenatedTimeline
        protected int getFirstWindowIndexByChildIndex(int r2) {
            return this.firstWindowInChildIndices[r2];
        }

        @Override // androidx.media3.common.Timeline
        public int getPeriodCount() {
            return this.periodCount;
        }

        @Override // androidx.media3.exoplayer.AbstractConcatenatedTimeline
        protected Timeline getTimelineByChildIndex(int r2) {
            return this.timelines[r2];
        }

        @Override // androidx.media3.common.Timeline
        public int getWindowCount() {
            return this.windowCount;
        }
    }

    private static final class FakeMediaSource extends BaseMediaSource {
        private FakeMediaSource() {
        }

        /* synthetic */ FakeMediaSource(C03261 r1) {
            this();
        }

        @Override // androidx.media3.exoplayer.source.MediaSource
        public MediaPeriod createPeriod(MediaSource.MediaPeriodId r2, Allocator r3, long r4) {
            throw new UnsupportedOperationException();
        }

        @Override // androidx.media3.exoplayer.source.MediaSource
        public MediaItem getMediaItem() {
            return ConcatenatingMediaSource.access$100();
        }

        @Override // androidx.media3.exoplayer.source.MediaSource
        public void maybeThrowSourceInfoRefreshError() {
        }

        @Override // androidx.media3.exoplayer.source.BaseMediaSource
        protected void prepareSourceInternal(TransferListener r1) {
        }

        @Override // androidx.media3.exoplayer.source.MediaSource
        public void releasePeriod(MediaPeriod r1) {
        }

        @Override // androidx.media3.exoplayer.source.BaseMediaSource
        protected void releaseSourceInternal() {
        }
    }

    private static final class HandlerAndRunnable {
        private final Handler handler;
        private final Runnable runnable;

        public HandlerAndRunnable(Handler r1, Runnable r2) {
            this.handler = r1;
            this.runnable = r2;
        }

        public void dispatch() {
            this.handler.post(this.runnable);
        }
    }

    static final class MediaSourceHolder {
        public final List<MediaSource.MediaPeriodId> activeMediaPeriodIds;
        public int childIndex;
        public int firstWindowIndexInChild;
        public boolean isRemoved;
        public final MaskingMediaSource mediaSource;
        public final Object uid;

        public MediaSourceHolder(MediaSource r2, boolean r3) {
            this.mediaSource = new MaskingMediaSource(r2, r3);
            this.activeMediaPeriodIds = new ArrayList();
            this.uid = new Object();
        }

        public void reset(int r2, int r3) {
            this.childIndex = r2;
            this.firstWindowIndexInChild = r3;
            this.isRemoved = false;
            this.activeMediaPeriodIds.clear();
        }
    }

    private static final class MessageData<T> {
        public final T customData;
        public final int index;
        public final HandlerAndRunnable onCompletionAction;

        public MessageData(int r1, T r2, HandlerAndRunnable r3) {
            this.index = r1;
            this.customData = r2;
            this.onCompletionAction = r3;
        }
    }

    public static /* synthetic */ boolean $r8$lambda$kn6RbvW74OB13KKGATjYUxIN99A(ConcatenatingMediaSource r0, Message r1) {
        return r0.handleMessage(r1);
    }

    static {
        PLACEHOLDER_MEDIA_ITEM = new MediaItem.Builder().setUri(Uri.EMPTY).build();
    }

    public ConcatenatingMediaSource(boolean r2, ShuffleOrder r3, MediaSource... r4) {
        this(r2, false, r3, r4);
    }

    public ConcatenatingMediaSource(boolean r4, boolean r5, ShuffleOrder r6, MediaSource... r7) {
        int r0 = r7.length;
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L6;
        Assertions.checkNotNull(r7[r1]);
        r1 = r1 + 1;
        goto L3
    L6:
        if (r6.getLength() <= 0) goto L8;
        ShuffleOrder r2 = r6.cloneAndClear();
    L9:
        this.shuffleOrder = r2;
        this.mediaSourceByMediaPeriod = new IdentityHashMap();
        this.mediaSourceByUid = new HashMap();
        this.mediaSourcesPublic = new ArrayList();
        this.mediaSourceHolders = new ArrayList();
        this.nextTimelineUpdateOnCompletionActions = new HashSet();
        this.pendingOnCompletionActions = new HashSet();
        this.enabledMediaSourceHolders = new HashSet();
        this.isAtomic = r4;
        this.useLazyPreparation = r5;
        addMediaSources(Arrays.asList(r7));
        return;
    L8:
        r2 = r6;
        goto L9
    }

    public ConcatenatingMediaSource(boolean r3, MediaSource... r4) {
        this(r3, new ShuffleOrder.DefaultShuffleOrder(0), r4);
    }

    public ConcatenatingMediaSource(MediaSource... r2) {
        this(false, r2);
    }

    static /* synthetic */ MediaItem access$100() {
        return PLACEHOLDER_MEDIA_ITEM;
    }

    private void addMediaSourceInternal(int r5, MediaSourceHolder r6) {
        if (r5 <= 0) goto L4;
        MediaSourceHolder r0 = this.mediaSourceHolders.get(r5 - 1);
        r6.reset(r5, r0.firstWindowIndexInChild + r0.mediaSource.getTimeline().getWindowCount());
    L5:
        correctOffsets(r5, 1, r6.mediaSource.getTimeline().getWindowCount());
        this.mediaSourceHolders.add(r5, r6);
        this.mediaSourceByUid.put(r6.uid, r6);
        prepareChildSource(r6, r6.mediaSource);
        if (isEnabled() == true) goto L8;
    L10:
        disableChildSource(r6);
        return;
    L8:
        if (this.mediaSourceByMediaPeriod.isEmpty() == false) goto L10;
        this.enabledMediaSourceHolders.add(r6);
        return;
    L4:
        r6.reset(r5, 0);
        goto L5
    }

    private void addMediaSourcesInternal(int r4, Collection<MediaSourceHolder> r5) {
        Iterator<MediaSourceHolder> r0 = r5.iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        addMediaSourceInternal(r4, r0.next());
        r4 = r4 + 1;
        goto L4
    }

    private void addPublicMediaSources(int r8, Collection<MediaSource> r9, Handler r10, Runnable r11) {
        boolean r0 = false;
        if (r10 != null) goto L5;
        boolean r2 = true;
    L6:
        if (r11 != null) goto L8;
        boolean r3 = true;
    L9:
        if (r2 != r3) goto L11;
        r0 = true;
    L11:
        Assertions.checkArgument(r0);
        Handler r1 = this.playbackThreadHandler;
        Iterator<MediaSource> r4 = r9.iterator();
    L13:
        if (r4.hasNext() == false) goto L15;
        Assertions.checkNotNull(r4.next());
        goto L13
    L15:
        ArrayList r5 = new ArrayList(r9.size());
        Iterator<MediaSource> r6 = r9.iterator();
    L17:
        if (r6.hasNext() == false) goto L19;
        r5.add(new MediaSourceHolder(r6.next(), this.useLazyPreparation));
        goto L17
    L19:
        this.mediaSourcesPublic.addAll(r8, r5);
        if (r1 != null) goto L22;
    L24:
        if (r11 == null) goto L32;
        if (r10 == null) goto L33;
        r10.post(r11);
        return;
    L33:
        return;
    L32:
        return;
    L22:
        if (r9.isEmpty() == true) goto L24;
        r1.obtainMessage(1, new MessageData(r8, r5, createOnCompletionAction(r10, r11))).sendToTarget();
        return;
    L8:
        r3 = false;
        goto L9
    L5:
        r2 = false;
        goto L6
    }

    private void correctOffsets(int r4, int r5, int r6) {
        int r0 = r4;
    L4:
        if (r0 >= this.mediaSourceHolders.size()) goto L6;
        MediaSourceHolder r1 = this.mediaSourceHolders.get(r0);
        r1.childIndex += r5;
        r1.firstWindowIndexInChild += r6;
        r0 = r0 + 1;
        goto L4
    }

    private HandlerAndRunnable createOnCompletionAction(Handler r3, Runnable r4) {
        if (r3 == null) goto L7;
        if (r4 == null) goto L9;
        HandlerAndRunnable r0 = new HandlerAndRunnable(r3, r4);
        this.pendingOnCompletionActions.add(r0);
        return r0;
    L9:
        return null;
    L7:
        return null;
    }

    private void disableUnusedMediaSources() {
        Iterator<MediaSourceHolder> r0 = this.enabledMediaSourceHolders.iterator();
    L4:
        if (r0.hasNext() == false) goto L9;
        MediaSourceHolder r1 = r0.next();
        if (r1.activeMediaPeriodIds.isEmpty() == false) goto L4;
        disableChildSource(r1);
        r0.remove();
        goto L4
    }

    private synchronized void dispatchOnCompletionActions(Set<HandlerAndRunnable> r3) {
        monitor-enter(this);
        Iterator<HandlerAndRunnable> r0 = r3.iterator();     // Catch: Throwable -> L10
    L5:
        if (r0.hasNext() == false) goto L7;
        r0.next().dispatch();     // Catch: Throwable -> L10
        goto L5
    L7:
        this.pendingOnCompletionActions.removeAll(r3);     // Catch: Throwable -> L10
        monitor-exit(this);
        return;
    L10:
        th = move-exception;
        throw th;
    }

    private void enableMediaSource(MediaSourceHolder r2) {
        this.enabledMediaSourceHolders.add(r2);
        enableChildSource(r2);
    }

    private static Object getChildPeriodUid(Object r1) {
        return ConcatenatedTimeline.getChildPeriodUidFromConcatenatedUid(r1);
    }

    private static Object getMediaSourceHolderUid(Object r1) {
        return ConcatenatedTimeline.getChildTimelineUidFromConcatenatedUid(r1);
    }

    private static Object getPeriodUid(MediaSourceHolder r1, Object r2) {
        return ConcatenatedTimeline.getConcatenatedUid(r1.uid, r2);
    }

    private Handler getPlaybackThreadHandlerOnPlaybackThread() {
        return (Handler) Assertions.checkNotNull(this.playbackThreadHandler);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean handleMessage(Message r6) {
        switch(r6.what) {
            case 1: goto L20;
            case 2: goto L10;
            case 3: goto L9;
            case 4: goto L8;
            case 5: goto L7;
            case 6: goto L6;
            default: goto L5;
        };
    L6:
        dispatchOnCompletionActions((Set) Util.castNonNull(r6.obj));
    L21:
        return true;
    L7:
        updateTimelineAndScheduleOnCompletionActions();
        goto L21
    L8:
        MessageData r0 = (MessageData) Util.castNonNull(r6.obj);
        this.shuffleOrder = (ShuffleOrder) r0.customData;
        scheduleTimelineUpdate(r0.onCompletionAction);
        goto L21
    L9:
        MessageData r1 = (MessageData) Util.castNonNull(r6.obj);
        this.shuffleOrder = this.shuffleOrder.cloneAndRemove(r1.index, r1.index + 1);
        this.shuffleOrder = this.shuffleOrder.cloneAndInsert(((Integer) r1.customData).intValue(), 1);
        moveMediaSourceInternal(r1.index, ((Integer) r1.customData).intValue());
        scheduleTimelineUpdate(r1.onCompletionAction);
        goto L21
    L10:
        MessageData r2 = (MessageData) Util.castNonNull(r6.obj);
        int r3 = r2.index;
        int r4 = ((Integer) r2.customData).intValue();
        if (r3 == 0) goto L13;
    L15:
        this.shuffleOrder = this.shuffleOrder.cloneAndRemove(r3, r4);
    L16:
        int r5 = r4 - 1;
    L17:
        if (r5 < r3) goto L19;
        removeMediaSourceInternal(r5);
        r5 = r5 - 1;
        goto L17
    L19:
        scheduleTimelineUpdate(r2.onCompletionAction);
        goto L21
    L13:
        if (r4 != this.shuffleOrder.getLength()) goto L15;
        this.shuffleOrder = this.shuffleOrder.cloneAndClear();
        goto L16
    L20:
        MessageData r7 = (MessageData) Util.castNonNull(r6.obj);
        this.shuffleOrder = this.shuffleOrder.cloneAndInsert(r7.index, ((Collection) r7.customData).size());
        addMediaSourcesInternal(r7.index, (Collection) r7.customData);
        scheduleTimelineUpdate(r7.onCompletionAction);
        goto L21
    L5:
        throw new IllegalStateException();
    }

    private void maybeReleaseChildSource(MediaSourceHolder r2) {
        if (r2.isRemoved == true) goto L5;
        return;
    L5:
        if (r2.activeMediaPeriodIds.isEmpty() == false) goto L9;
        this.enabledMediaSourceHolders.remove(r2);
        releaseChildSource(r2);
        return;
    }

    private void moveMediaSourceInternal(int r7, int r8) {
        int r0 = Math.min(r7, r8);
        int r1 = Math.max(r7, r8);
        int r2 = this.mediaSourceHolders.get(r0).firstWindowIndexInChild;
        this.mediaSourceHolders.add(r8, this.mediaSourceHolders.remove(r7));
        int r3 = r0;
    L3:
        if (r3 > r1) goto L5;
        MediaSourceHolder r4 = this.mediaSourceHolders.get(r3);
        r4.childIndex = r3;
        r4.firstWindowIndexInChild = r2;
        r2 = r2 + r4.mediaSource.getTimeline().getWindowCount();
        r3 = r3 + 1;
        goto L3
    }

    private void movePublicMediaSource(int r5, int r6, Handler r7, Runnable r8) {
        boolean r0 = true;
        if (r7 != null) goto L5;
        boolean r2 = true;
    L6:
        if (r8 != null) goto L8;
        boolean r3 = true;
    L9:
        if (r2 == r3) goto L12;
        r0 = false;
    L12:
        Assertions.checkArgument(r0);
        Handler r1 = this.playbackThreadHandler;
        this.mediaSourcesPublic.add(r6, this.mediaSourcesPublic.remove(r5));
        if (r1 == null) goto L15;
        r1.obtainMessage(3, new MessageData(r5, Integer.valueOf(r6), createOnCompletionAction(r7, r8))).sendToTarget();
        return;
    L15:
        if (r8 == null) goto L21;
        if (r7 == null) goto L22;
        r7.post(r8);
        return;
    L22:
        return;
    L21:
        return;
    L8:
        r3 = false;
        goto L9
    L5:
        r2 = false;
        goto L6
    }

    private void removeMediaSourceInternal(int r5) {
        MediaSourceHolder r0 = this.mediaSourceHolders.remove(r5);
        this.mediaSourceByUid.remove(r0.uid);
        correctOffsets(r5, -1, -r0.mediaSource.getTimeline().getWindowCount());
        r0.isRemoved = true;
        maybeReleaseChildSource(r0);
    }

    private void removePublicMediaSources(int r5, int r6, Handler r7, Runnable r8) {
        boolean r0 = true;
        if (r7 != null) goto L5;
        boolean r2 = true;
    L6:
        if (r8 != null) goto L8;
        boolean r3 = true;
    L9:
        if (r2 == r3) goto L12;
        r0 = false;
    L12:
        Assertions.checkArgument(r0);
        Handler r1 = this.playbackThreadHandler;
        Util.removeRange(this.mediaSourcesPublic, r5, r6);
        if (r1 == null) goto L15;
        r1.obtainMessage(2, new MessageData(r5, Integer.valueOf(r6), createOnCompletionAction(r7, r8))).sendToTarget();
        return;
    L15:
        if (r8 == null) goto L21;
        if (r7 == null) goto L22;
        r7.post(r8);
        return;
    L22:
        return;
    L21:
        return;
    L8:
        r3 = false;
        goto L9
    L5:
        r2 = false;
        goto L6
    }

    private void scheduleTimelineUpdate() {
        scheduleTimelineUpdate(null);
    }

    private void scheduleTimelineUpdate(HandlerAndRunnable r3) {
        if (this.timelineUpdateScheduled == true) goto L5;
        getPlaybackThreadHandlerOnPlaybackThread().obtainMessage(5).sendToTarget();
        this.timelineUpdateScheduled = true;
    L5:
        if (r3 == null) goto L8;
        this.nextTimelineUpdateOnCompletionActions.add(r3);
        return;
    }

    private void setPublicShuffleOrder(ShuffleOrder r6, Handler r7, Runnable r8) {
        boolean r0 = true;
        if (r7 != null) goto L5;
        boolean r2 = true;
    L6:
        if (r8 != null) goto L8;
        boolean r3 = true;
    L9:
        if (r2 == r3) goto L12;
        r0 = false;
    L12:
        Assertions.checkArgument(r0);
        Handler r1 = this.playbackThreadHandler;
        if (r1 == null) goto L19;
        int r4 = getSize();
        if (r6.getLength() == r4) goto L17;
        r6 = r6.cloneAndClear().cloneAndInsert(0, r4);
    L17:
        r1.obtainMessage(4, new MessageData(0, r6, createOnCompletionAction(r7, r8))).sendToTarget();
        return;
    L19:
        if (r6.getLength() <= 0) goto L21;
        ShuffleOrder r5 = r6.cloneAndClear();
    L22:
        this.shuffleOrder = r5;
        if (r8 == null) goto L28;
        if (r7 == null) goto L29;
        r7.post(r8);
        return;
    L29:
        return;
    L28:
        return;
    L21:
        r5 = r6;
        goto L22
    L8:
        r3 = false;
        goto L9
    L5:
        r2 = false;
        goto L6
    }

    private void updateMediaSourceInternal(MediaSourceHolder r5, Timeline r6) {
        if ((r5.childIndex + 1) >= this.mediaSourceHolders.size()) goto L7;
        MediaSourceHolder r0 = this.mediaSourceHolders.get(r5.childIndex + 1);
        int r1 = r6.getWindowCount() - (r0.firstWindowIndexInChild - r5.firstWindowIndexInChild);
        if (r1 == 0) goto L7;
        correctOffsets(r5.childIndex + 1, 0, r1);
    L7:
        scheduleTimelineUpdate();
    }

    private void updateTimelineAndScheduleOnCompletionActions() {
        this.timelineUpdateScheduled = false;
        Set<HandlerAndRunnable> r0 = this.nextTimelineUpdateOnCompletionActions;
        this.nextTimelineUpdateOnCompletionActions = new HashSet();
        refreshSourceInfo(new ConcatenatedTimeline(this.mediaSourceHolders, this.shuffleOrder, this.isAtomic));
        getPlaybackThreadHandlerOnPlaybackThread().obtainMessage(6, r0).sendToTarget();
    }

    public synchronized void addMediaSource(int r3, MediaSource r4) {
        monitor-enter(this);
        addPublicMediaSources(r3, Collections.singletonList(r4), null, null);     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized void addMediaSource(int r2, MediaSource r3, Handler r4, Runnable r5) {
        monitor-enter(this);
        addPublicMediaSources(r2, Collections.singletonList(r3), r4, r5);     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized void addMediaSource(MediaSource r2) {
        monitor-enter(this);
        addMediaSource(this.mediaSourcesPublic.size(), r2);     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized void addMediaSource(MediaSource r2, Handler r3, Runnable r4) {
        monitor-enter(this);
        addMediaSource(this.mediaSourcesPublic.size(), r2, r3, r4);     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized void addMediaSources(int r2, Collection<MediaSource> r3) {
        monitor-enter(this);
        addPublicMediaSources(r2, r3, null, null);     // Catch: Throwable -> L7
        monitor-exit(this);
        return;
    L7:
        th = move-exception;
        throw th;
    }

    public synchronized void addMediaSources(int r1, Collection<MediaSource> r2, Handler r3, Runnable r4) {
        monitor-enter(this);
        addPublicMediaSources(r1, r2, r3, r4);     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized void addMediaSources(Collection<MediaSource> r3) {
        monitor-enter(this);
        addPublicMediaSources(this.mediaSourcesPublic.size(), r3, null, null);     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized void addMediaSources(Collection<MediaSource> r2, Handler r3, Runnable r4) {
        monitor-enter(this);
        addPublicMediaSources(this.mediaSourcesPublic.size(), r2, r3, r4);     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized void clear() {
        monitor-enter(this);
        removeMediaSourceRange(0, getSize());     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized void clear(Handler r3, Runnable r4) {
        monitor-enter(this);
        removeMediaSourceRange(0, getSize(), r3, r4);     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public MediaPeriod createPeriod(MediaSource.MediaPeriodId r7, Allocator r8, long r9) {
        Object r0 = getMediaSourceHolderUid(r7.periodUid);
        MediaSource.MediaPeriodId r1 = r7.copyWithPeriodUid(getChildPeriodUid(r7.periodUid));
        MediaSourceHolder r2 = this.mediaSourceByUid.get(r0);
        if (r2 != null) goto L5;
        r2 = new MediaSourceHolder(new FakeMediaSource(null), this.useLazyPreparation);
        r2.isRemoved = true;
        prepareChildSource(r2, r2.mediaSource);
    L5:
        enableMediaSource(r2);
        r2.activeMediaPeriodIds.add(r1);
        MaskingMediaPeriod r3 = r2.mediaSource.createPeriod(r1, r8, r9);
        this.mediaSourceByMediaPeriod.put(r3, r2);
        disableUnusedMediaSources();
        return r3;
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource, androidx.media3.exoplayer.source.BaseMediaSource
    protected void disableInternal() {
        super.disableInternal();
        this.enabledMediaSourceHolders.clear();
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource, androidx.media3.exoplayer.source.BaseMediaSource
    protected void enableInternal() {
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource, androidx.media3.exoplayer.source.MediaSource
    public synchronized Timeline getInitialTimeline() {
        monitor-enter(this);
    L10:
        th = move-exception;
        throw th;
    L4:
        if (this.shuffleOrder.getLength() == this.mediaSourcesPublic.size()) goto L6;
        ShuffleOrder r0 = this.shuffleOrder.cloneAndClear().cloneAndInsert(0, this.mediaSourcesPublic.size());     // Catch: Throwable -> L10
    L7:
        ConcatenatedTimeline r1 = new ConcatenatedTimeline(this.mediaSourcesPublic, r0, this.isAtomic);     // Catch: Throwable -> L10
        monitor-exit(this);
        return r1;
    L6:
        r0 = this.shuffleOrder;     // Catch: Throwable -> L10
        goto L7
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public MediaItem getMediaItem() {
        return PLACEHOLDER_MEDIA_ITEM;
    }

    /* JADX INFO: renamed from: getMediaPeriodIdForChildMediaPeriodId, reason: avoid collision after fix types in other method */
    protected MediaSource.MediaPeriodId getMediaPeriodIdForChildMediaPeriodId2(MediaSourceHolder r6, MediaSource.MediaPeriodId r7) {
        int r0 = 0;
    L4:
        if (r0 >= r6.activeMediaPeriodIds.size()) goto L10;
        if (r6.activeMediaPeriodIds.get(r0).windowSequenceNumber == r7.windowSequenceNumber) goto L8;
        r0 = r0 + 1;
        goto L4
    L8:
        return r7.copyWithPeriodUid(getPeriodUid(r6, r7.periodUid));
    L10:
        return null;
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource
    protected /* bridge */ /* synthetic */ MediaSource.MediaPeriodId getMediaPeriodIdForChildMediaPeriodId(MediaSourceHolder r1, MediaSource.MediaPeriodId r2) {
        return getMediaPeriodIdForChildMediaPeriodId2(r1, r2);
    }

    public synchronized MediaSource getMediaSource(int r2) {
        monitor-enter(this);
        MaskingMediaSource r0 = this.mediaSourcesPublic.get(r2).mediaSource;     // Catch: Throwable -> L6
        monitor-exit(this);
        return r0;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized int getSize() {
        monitor-enter(this);
        int r0 = this.mediaSourcesPublic.size();     // Catch: Throwable -> L6
        monitor-exit(this);
        return r0;
    L6:
        th = move-exception;
        throw th;
    }

    /* JADX INFO: renamed from: getWindowIndexForChildWindowIndex, reason: avoid collision after fix types in other method */
    protected int getWindowIndexForChildWindowIndex2(MediaSourceHolder r2, int r3) {
        return r2.firstWindowIndexInChild + r3;
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource
    protected /* bridge */ /* synthetic */ int getWindowIndexForChildWindowIndex(MediaSourceHolder r1, int r2) {
        return getWindowIndexForChildWindowIndex2(r1, r2);
    }

    @Override // androidx.media3.exoplayer.source.BaseMediaSource, androidx.media3.exoplayer.source.MediaSource
    public boolean isSingleWindow() {
        return false;
    }

    public synchronized void moveMediaSource(int r2, int r3) {
        monitor-enter(this);
        movePublicMediaSource(r2, r3, null, null);     // Catch: Throwable -> L7
        monitor-exit(this);
        return;
    L7:
        th = move-exception;
        throw th;
    }

    public synchronized void moveMediaSource(int r1, int r2, Handler r3, Runnable r4) {
        monitor-enter(this);
        movePublicMediaSource(r1, r2, r3, r4);     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }

    /* JADX INFO: renamed from: onChildSourceInfoRefreshed, reason: avoid collision after fix types in other method */
    protected void onChildSourceInfoRefreshed2(MediaSourceHolder r1, MediaSource r2, Timeline r3) {
        updateMediaSourceInternal(r1, r3);
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource
    protected /* bridge */ /* synthetic */ void onChildSourceInfoRefreshed(MediaSourceHolder r1, MediaSource r2, Timeline r3) {
        onChildSourceInfoRefreshed2(r1, r2, r3);
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource, androidx.media3.exoplayer.source.BaseMediaSource
    protected synchronized void prepareSourceInternal(TransferListener r4) {
        monitor-enter(this);
        super.prepareSourceInternal(r4);     // Catch: Throwable -> L9
        this.playbackThreadHandler = new Handler(new ConcatenatingMediaSource$$ExternalSyntheticLambda0(this));     // Catch: Throwable -> L9
        if (this.mediaSourcesPublic.isEmpty() == false) goto L6;
        updateTimelineAndScheduleOnCompletionActions();     // Catch: Throwable -> L9
    L7:
        monitor-exit(this);
        return;
    L6:
        this.shuffleOrder = this.shuffleOrder.cloneAndInsert(0, this.mediaSourcesPublic.size());     // Catch: Throwable -> L9
        addMediaSourcesInternal(0, this.mediaSourcesPublic);     // Catch: Throwable -> L9
        scheduleTimelineUpdate();     // Catch: Throwable -> L9
    L9:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.exoplayer.source.MediaSource
    public void releasePeriod(MediaPeriod r4) {
        MediaSourceHolder r0 = (MediaSourceHolder) Assertions.checkNotNull(this.mediaSourceByMediaPeriod.remove(r4));
        r0.mediaSource.releasePeriod(r4);
        r0.activeMediaPeriodIds.remove(((MaskingMediaPeriod) r4).f44id);
        if (this.mediaSourceByMediaPeriod.isEmpty() == true) goto L5;
        disableUnusedMediaSources();
    L5:
        maybeReleaseChildSource(r0);
    }

    @Override // androidx.media3.exoplayer.source.CompositeMediaSource, androidx.media3.exoplayer.source.BaseMediaSource
    protected synchronized void releaseSourceInternal() {
        monitor-enter(this);
        super.releaseSourceInternal();     // Catch: Throwable -> L9
        this.mediaSourceHolders.clear();     // Catch: Throwable -> L9
        this.enabledMediaSourceHolders.clear();     // Catch: Throwable -> L9
        this.mediaSourceByUid.clear();     // Catch: Throwable -> L9
        this.shuffleOrder = this.shuffleOrder.cloneAndClear();     // Catch: Throwable -> L9
        if (this.playbackThreadHandler == null) goto L6;
        this.playbackThreadHandler.removeCallbacksAndMessages(null);     // Catch: Throwable -> L9
        this.playbackThreadHandler = null;     // Catch: Throwable -> L9
    L6:
        this.timelineUpdateScheduled = false;     // Catch: Throwable -> L9
        this.nextTimelineUpdateOnCompletionActions.clear();     // Catch: Throwable -> L9
        dispatchOnCompletionActions(this.pendingOnCompletionActions);     // Catch: Throwable -> L9
        monitor-exit(this);
        return;
    L9:
        th = move-exception;
        throw th;
    }

    public synchronized MediaSource removeMediaSource(int r4) {
        monitor-enter(this);
        MediaSource r0 = getMediaSource(r4);     // Catch: Throwable -> L6
        removePublicMediaSources(r4, r4 + 1, null, null);     // Catch: Throwable -> L6
        monitor-exit(this);
        return r0;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized MediaSource removeMediaSource(int r3, Handler r4, Runnable r5) {
        monitor-enter(this);
        MediaSource r0 = getMediaSource(r3);     // Catch: Throwable -> L6
        removePublicMediaSources(r3, r3 + 1, r4, r5);     // Catch: Throwable -> L6
        monitor-exit(this);
        return r0;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized void removeMediaSourceRange(int r2, int r3) {
        monitor-enter(this);
        removePublicMediaSources(r2, r3, null, null);     // Catch: Throwable -> L7
        monitor-exit(this);
        return;
    L7:
        th = move-exception;
        throw th;
    }

    public synchronized void removeMediaSourceRange(int r1, int r2, Handler r3, Runnable r4) {
        monitor-enter(this);
        removePublicMediaSources(r1, r2, r3, r4);     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized void setShuffleOrder(ShuffleOrder r2) {
        monitor-enter(this);
        setPublicShuffleOrder(r2, null, null);     // Catch: Throwable -> L7
        monitor-exit(this);
        return;
    L7:
        th = move-exception;
        throw th;
    }

    public synchronized void setShuffleOrder(ShuffleOrder r1, Handler r2, Runnable r3) {
        monitor-enter(this);
        setPublicShuffleOrder(r1, r2, r3);     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }
}
