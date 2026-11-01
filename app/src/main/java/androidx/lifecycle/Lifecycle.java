package androidx.lifecycle;

import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: loaded from: classes.dex */
public abstract class Lifecycle {
    AtomicReference<Object> mInternalScopeRef;

    /* JADX INFO: renamed from: androidx.lifecycle.Lifecycle$1 */
    static /* synthetic */ class C01491 {
        static final /* synthetic */ int[] $SwitchMap$androidx$lifecycle$Lifecycle$Event = null;
        static final /* synthetic */ int[] $SwitchMap$androidx$lifecycle$Lifecycle$State = null;

        static {
            $SwitchMap$androidx$lifecycle$Lifecycle$Event = new int[Event.values().length];
            $SwitchMap$androidx$lifecycle$Lifecycle$Event[Event.ON_CREATE.ordinal()] = 1;     // Catch: NoSuchFieldError -> L5
        L65:
            $SwitchMap$androidx$lifecycle$Lifecycle$Event[Event.ON_STOP.ordinal()] = 2;     // Catch: NoSuchFieldError -> L9
        L49:
            $SwitchMap$androidx$lifecycle$Lifecycle$Event[Event.ON_START.ordinal()] = 3;     // Catch: NoSuchFieldError -> L13
        L47:
            $SwitchMap$androidx$lifecycle$Lifecycle$Event[Event.ON_PAUSE.ordinal()] = 4;     // Catch: NoSuchFieldError -> L17
        L55:
            $SwitchMap$androidx$lifecycle$Lifecycle$Event[Event.ON_RESUME.ordinal()] = 5;     // Catch: NoSuchFieldError -> L21
        L61:
            $SwitchMap$androidx$lifecycle$Lifecycle$Event[Event.ON_DESTROY.ordinal()] = 6;     // Catch: NoSuchFieldError -> L24
        L67:
            $SwitchMap$androidx$lifecycle$Lifecycle$Event[Event.ON_ANY.ordinal()] = 7;     // Catch: NoSuchFieldError -> L27
        L28:
            $SwitchMap$androidx$lifecycle$Lifecycle$State = new int[State.values().length];
            $SwitchMap$androidx$lifecycle$Lifecycle$State[State.CREATED.ordinal()] = 1;     // Catch: NoSuchFieldError -> L31
        L57:
            $SwitchMap$androidx$lifecycle$Lifecycle$State[State.STARTED.ordinal()] = 2;     // Catch: NoSuchFieldError -> L34
        L63:
            $SwitchMap$androidx$lifecycle$Lifecycle$State[State.RESUMED.ordinal()] = 3;     // Catch: NoSuchFieldError -> L37
        L45:
            $SwitchMap$androidx$lifecycle$Lifecycle$State[State.DESTROYED.ordinal()] = 4;     // Catch: NoSuchFieldError -> L40
        L51:
            $SwitchMap$androidx$lifecycle$Lifecycle$State[State.INITIALIZED.ordinal()] = 5;     // Catch: NoSuchFieldError -> L43
            return;
        }
    }

    public static final enum Event extends Enum<Event> {
        private static final /* synthetic */ Event[] $VALUES = null;
        public static final Event ON_ANY = null;
        public static final Event ON_CREATE = null;
        public static final Event ON_DESTROY = null;
        public static final Event ON_PAUSE = null;
        public static final Event ON_RESUME = null;
        public static final Event ON_START = null;
        public static final Event ON_STOP = null;

        static {
            ON_CREATE = new Event("ON_CREATE", 0);
            ON_START = new Event("ON_START", 1);
            ON_RESUME = new Event("ON_RESUME", 2);
            ON_PAUSE = new Event("ON_PAUSE", 3);
            ON_STOP = new Event("ON_STOP", 4);
            ON_DESTROY = new Event("ON_DESTROY", 5);
            ON_ANY = new Event("ON_ANY", 6);
            $VALUES = new Event[]{ON_CREATE, ON_START, ON_RESUME, ON_PAUSE, ON_STOP, ON_DESTROY, ON_ANY};
        }

        private Event(String r1, int r2) {
            super(r1, r2);
        }

        public static Event downFrom(State r2) {
            switch(C01491.$SwitchMap$androidx$lifecycle$Lifecycle$State[r2.ordinal()]) {
                case 1: goto L11;
                case 2: goto L9;
                case 3: goto L7;
                default: goto L4;
            };
        L4:
            return null;
        L7:
            return ON_PAUSE;
        L9:
            return ON_STOP;
        L11:
            return ON_DESTROY;
        }

        public static Event downTo(State r2) {
            switch(C01491.$SwitchMap$androidx$lifecycle$Lifecycle$State[r2.ordinal()]) {
                case 1: goto L11;
                case 2: goto L9;
                case 3: goto L4;
                case 4: goto L7;
                default: goto L4;
            };
        L4:
            return null;
        L7:
            return ON_DESTROY;
        L9:
            return ON_PAUSE;
        L11:
            return ON_STOP;
        }

        public static Event upFrom(State r2) {
            switch(C01491.$SwitchMap$androidx$lifecycle$Lifecycle$State[r2.ordinal()]) {
                case 1: goto L11;
                case 2: goto L9;
                case 3: goto L4;
                case 4: goto L4;
                case 5: goto L7;
                default: goto L4;
            };
        L4:
            return null;
        L7:
            return ON_CREATE;
        L9:
            return ON_RESUME;
        L11:
            return ON_START;
        }

        public static Event upTo(State r2) {
            switch(C01491.$SwitchMap$androidx$lifecycle$Lifecycle$State[r2.ordinal()]) {
                case 1: goto L11;
                case 2: goto L9;
                case 3: goto L7;
                default: goto L4;
            };
        L4:
            return null;
        L7:
            return ON_RESUME;
        L9:
            return ON_START;
        L11:
            return ON_CREATE;
        }

        public static Event valueOf(String r1) {
            return (Event) Enum.valueOf(Event.class, r1);
        }

        public static Event[] values() {
            return (Event[]) $VALUES.clone();
        }

        public State getTargetState() {
            switch(C01491.$SwitchMap$androidx$lifecycle$Lifecycle$Event[ordinal()]) {
                case 1: goto L13;
                case 2: goto L13;
                case 3: goto L11;
                case 4: goto L11;
                case 5: goto L9;
                case 6: goto L7;
                default: goto L5;
            };
        L5:
            throw new IllegalArgumentException(this + " has no target state");
        L7:
            return State.DESTROYED;
        L9:
            return State.RESUMED;
        L11:
            return State.STARTED;
        L13:
            return State.CREATED;
        }
    }

    public static final enum State extends Enum<State> {
        private static final /* synthetic */ State[] $VALUES = null;
        public static final State CREATED = null;
        public static final State DESTROYED = null;
        public static final State INITIALIZED = null;
        public static final State RESUMED = null;
        public static final State STARTED = null;

        static {
            DESTROYED = new State("DESTROYED", 0);
            INITIALIZED = new State("INITIALIZED", 1);
            CREATED = new State("CREATED", 2);
            STARTED = new State("STARTED", 3);
            RESUMED = new State("RESUMED", 4);
            $VALUES = new State[]{DESTROYED, INITIALIZED, CREATED, STARTED, RESUMED};
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

        public boolean isAtLeast(State r2) {
            if (compareTo(r2) < 0) goto L5;
            return true;
        L5:
            return false;
        }
    }

    public Lifecycle() {
        this.mInternalScopeRef = new AtomicReference();
    }

    public abstract void addObserver(LifecycleObserver r1);

    public abstract State getCurrentState();

    public abstract void removeObserver(LifecycleObserver r1);
}
