package com.google.common.util.concurrent;

import com.google.errorprone.annotations.DoNotMock;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* JADX INFO: loaded from: classes.dex */
@DoNotMock("Create an AbstractIdleService")
@ElementTypesAreNonnullByDefault
public interface Service {

    public static abstract class Listener {
        public Listener() {
        }

        public void failed(State r1, Throwable r2) {
        }

        public void running() {
        }

        public void starting() {
        }

        public void stopping(State r1) {
        }

        public void terminated(State r1) {
        }
    }

    public static final enum State extends Enum<State> {
        private static final /* synthetic */ State[] $VALUES = null;
        public static final State FAILED = null;
        public static final State NEW = null;
        public static final State RUNNING = null;
        public static final State STARTING = null;
        public static final State STOPPING = null;
        public static final State TERMINATED = null;

        private static /* synthetic */ State[] $values() {
            return new State[]{NEW, STARTING, RUNNING, STOPPING, TERMINATED, FAILED};
        }

        static {
            NEW = new State("NEW", 0);
            STARTING = new State("STARTING", 1);
            RUNNING = new State("RUNNING", 2);
            STOPPING = new State("STOPPING", 3);
            TERMINATED = new State("TERMINATED", 4);
            FAILED = new State("FAILED", 5);
            $VALUES = $values();
        }

        private State(String r1, int r2) {
            super(r1, r2);
        }

        public static State valueOf(String r1) {
            return (State) Enum.valueOf(State.class, r1);
        }

        public static State[] values() {
            return (State[]) $VALUES.clone();
        }
    }

    void addListener(Listener r1, Executor r2);

    void awaitRunning();

    void awaitRunning(long r1, TimeUnit r3) throws TimeoutException;

    void awaitTerminated();

    void awaitTerminated(long r1, TimeUnit r3) throws TimeoutException;

    Throwable failureCause();

    boolean isRunning();

    Service startAsync();

    State state();

    Service stopAsync();
}
