package androidx.media3.common.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class SystemHandlerWrapper implements HandlerWrapper {
    private static final int MAX_POOL_SIZE = 50;
    private static final List<SystemMessage> messagePool = null;
    private final Handler handler;

    /* JADX INFO: renamed from: androidx.media3.common.util.SystemHandlerWrapper$1 */
    static /* synthetic */ class C02011 {
    }

    private static final class SystemMessage implements HandlerWrapper.Message {
        private SystemHandlerWrapper handler;
        private Message message;

        private SystemMessage() {
        }

        /* synthetic */ SystemMessage(C02011 r1) {
            this();
        }

        private void recycle() {
            this.message = null;
            this.handler = null;
            SystemHandlerWrapper.access$100(this);
        }

        @Override // androidx.media3.common.util.HandlerWrapper.Message
        public HandlerWrapper getTarget() {
            return (HandlerWrapper) Assertions.checkNotNull(this.handler);
        }

        public boolean sendAtFrontOfQueue(Handler r2) {
            boolean r0 = r2.sendMessageAtFrontOfQueue((Message) Assertions.checkNotNull(this.message));
            recycle();
            return r0;
        }

        @Override // androidx.media3.common.util.HandlerWrapper.Message
        public void sendToTarget() {
            ((Message) Assertions.checkNotNull(this.message)).sendToTarget();
            recycle();
        }

        public SystemMessage setMessage(Message r1, SystemHandlerWrapper r2) {
            this.message = r1;
            this.handler = r2;
            return this;
        }
    }

    static {
        messagePool = new ArrayList(50);
    }

    public SystemHandlerWrapper(Handler r1) {
        this.handler = r1;
    }

    static /* synthetic */ void access$100(SystemMessage r0) {
        recycleMessage(r0);
    }

    private static SystemMessage obtainSystemMessage() {
        List<SystemMessage> r0 = messagePool;
        monitor-enter(r0);
    L10:
        th = move-exception;
        throw th;
    L5:
        if (messagePool.isEmpty() == false) goto L7;
        SystemMessage r1 = new SystemMessage(null);     // Catch: Throwable -> L10
    L8:
        monitor-exit(r0);     // Catch: Throwable -> L10
        return r1;
    L7:
        r1 = messagePool.remove(messagePool.size() - 1);     // Catch: Throwable -> L10
        goto L8
    }

    private static void recycleMessage(SystemMessage r3) {
        List<SystemMessage> r0 = messagePool;
        monitor-enter(r0);
    L9:
        th = move-exception;
        throw th;
    L5:
        if (messagePool.size() >= 50) goto L7;
        messagePool.add(r3);     // Catch: Throwable -> L9
    L7:
        monitor-exit(r0);     // Catch: Throwable -> L9
    }

    @Override // androidx.media3.common.util.HandlerWrapper
    public Looper getLooper() {
        return this.handler.getLooper();
    }

    @Override // androidx.media3.common.util.HandlerWrapper
    public boolean hasMessages(int r2) {
        if (r2 == 0) goto L4;
        boolean r0 = true;
    L5:
        Assertions.checkArgument(r0);
        return this.handler.hasMessages(r2);
    L4:
        r0 = false;
        goto L5
    }

    @Override // androidx.media3.common.util.HandlerWrapper
    public HandlerWrapper.Message obtainMessage(int r3) {
        return obtainSystemMessage().setMessage(this.handler.obtainMessage(r3), this);
    }

    @Override // androidx.media3.common.util.HandlerWrapper
    public HandlerWrapper.Message obtainMessage(int r3, int r4, int r5) {
        return obtainSystemMessage().setMessage(this.handler.obtainMessage(r3, r4, r5), this);
    }

    @Override // androidx.media3.common.util.HandlerWrapper
    public HandlerWrapper.Message obtainMessage(int r3, int r4, int r5, Object r6) {
        return obtainSystemMessage().setMessage(this.handler.obtainMessage(r3, r4, r5, r6), this);
    }

    @Override // androidx.media3.common.util.HandlerWrapper
    public HandlerWrapper.Message obtainMessage(int r3, Object r4) {
        return obtainSystemMessage().setMessage(this.handler.obtainMessage(r3, r4), this);
    }

    @Override // androidx.media3.common.util.HandlerWrapper
    public boolean post(Runnable r2) {
        return this.handler.post(r2);
    }

    @Override // androidx.media3.common.util.HandlerWrapper
    public boolean postAtFrontOfQueue(Runnable r2) {
        return this.handler.postAtFrontOfQueue(r2);
    }

    @Override // androidx.media3.common.util.HandlerWrapper
    public boolean postDelayed(Runnable r2, long r3) {
        return this.handler.postDelayed(r2, r3);
    }

    @Override // androidx.media3.common.util.HandlerWrapper
    public void removeCallbacksAndMessages(Object r2) {
        this.handler.removeCallbacksAndMessages(r2);
    }

    @Override // androidx.media3.common.util.HandlerWrapper
    public void removeMessages(int r2) {
        if (r2 == 0) goto L4;
        boolean r0 = true;
    L5:
        Assertions.checkArgument(r0);
        this.handler.removeMessages(r2);
        return;
    L4:
        r0 = false;
        goto L5
    }

    @Override // androidx.media3.common.util.HandlerWrapper
    public boolean sendEmptyMessage(int r2) {
        return this.handler.sendEmptyMessage(r2);
    }

    @Override // androidx.media3.common.util.HandlerWrapper
    public boolean sendEmptyMessageAtTime(int r2, long r3) {
        return this.handler.sendEmptyMessageAtTime(r2, r3);
    }

    @Override // androidx.media3.common.util.HandlerWrapper
    public boolean sendEmptyMessageDelayed(int r4, int r5) {
        return this.handler.sendEmptyMessageDelayed(r4, r5);
    }

    @Override // androidx.media3.common.util.HandlerWrapper
    public boolean sendMessageAtFrontOfQueue(HandlerWrapper.Message r3) {
        return ((SystemMessage) r3).sendAtFrontOfQueue(this.handler);
    }
}
