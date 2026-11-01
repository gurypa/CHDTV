package kotlin.jvm.internal;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import kotlin.jvm.KotlinReflectionNotSupportedError;
import kotlin.reflect.KCallable;
import kotlin.reflect.KDeclarationContainer;
import kotlin.reflect.KParameter;
import kotlin.reflect.KType;
import kotlin.reflect.KTypeParameter;
import kotlin.reflect.KVisibility;

/* JADX INFO: loaded from: classes.dex */
public abstract class CallableReference implements KCallable, Serializable {
    public static final Object NO_RECEIVER = null;
    private final boolean isTopLevel;
    private final String name;
    private final Class owner;
    protected final Object receiver;
    private transient KCallable reflected;
    private final String signature;

    private static class NoReceiver implements Serializable {
        private static final NoReceiver INSTANCE = null;

        static {
            INSTANCE = new NoReceiver();
        }

        private NoReceiver() {
        }

        static /* synthetic */ NoReceiver access$000() {
            return INSTANCE;
        }

        private Object readResolve() throws ObjectStreamException {
            return INSTANCE;
        }
    }

    static {
        NO_RECEIVER = NoReceiver.access$000();
    }

    public CallableReference() {
        this(NO_RECEIVER);
    }

    protected CallableReference(Object r7) {
        this(r7, null, null, null, false);
    }

    protected CallableReference(Object r1, Class r2, String r3, String r4, boolean r5) {
        this.receiver = r1;
        this.owner = r2;
        this.name = r3;
        this.signature = r4;
        this.isTopLevel = r5;
    }

    @Override // kotlin.reflect.KCallable
    public Object call(Object... r2) {
        return getReflected().call(r2);
    }

    @Override // kotlin.reflect.KCallable
    public Object callBy(Map r2) {
        return getReflected().callBy(r2);
    }

    public KCallable compute() {
        KCallable r0 = this.reflected;
        if (r0 != null) goto L6;
        KCallable r1 = computeReflected();
        this.reflected = r1;
        return r1;
    L6:
        return r0;
    }

    protected abstract KCallable computeReflected();

    @Override // kotlin.reflect.KAnnotatedElement
    public List<Annotation> getAnnotations() {
        return getReflected().getAnnotations();
    }

    public Object getBoundReceiver() {
        return this.receiver;
    }

    @Override // kotlin.reflect.KCallable
    public String getName() {
        return this.name;
    }

    public KDeclarationContainer getOwner() {
        if (this.owner != null) goto L6;
        return null;
    L6:
        if (this.isTopLevel == false) goto L9;
        return Reflection.getOrCreateKotlinPackage(this.owner);
    L9:
        return Reflection.getOrCreateKotlinClass(this.owner);
    }

    @Override // kotlin.reflect.KCallable
    public List<KParameter> getParameters() {
        return getReflected().getParameters();
    }

    protected KCallable getReflected() {
        KCallable r0 = compute();
        if (r0 == this) goto L6;
        return r0;
    L6:
        throw new KotlinReflectionNotSupportedError();
    }

    @Override // kotlin.reflect.KCallable
    public KType getReturnType() {
        return getReflected().getReturnType();
    }

    public String getSignature() {
        return this.signature;
    }

    @Override // kotlin.reflect.KCallable
    public List<KTypeParameter> getTypeParameters() {
        return getReflected().getTypeParameters();
    }

    @Override // kotlin.reflect.KCallable
    public KVisibility getVisibility() {
        return getReflected().getVisibility();
    }

    @Override // kotlin.reflect.KCallable
    public boolean isAbstract() {
        return getReflected().isAbstract();
    }

    @Override // kotlin.reflect.KCallable
    public boolean isFinal() {
        return getReflected().isFinal();
    }

    @Override // kotlin.reflect.KCallable
    public boolean isOpen() {
        return getReflected().isOpen();
    }

    @Override // kotlin.reflect.KCallable
    public boolean isSuspend() {
        return getReflected().isSuspend();
    }
}
