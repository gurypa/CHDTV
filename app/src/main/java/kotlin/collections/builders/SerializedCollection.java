package kotlin.collections.builders;

import java.io.Externalizable;
import java.io.InvalidObjectException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: ListBuilder.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0000\u0018\u0000 \u00112\u00020\u0001:\u0001\u0011B\u0007\b\u0016¢\u0006\u0002\u0010\u0002B\u0019\u0012\n\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016J\b\u0010\f\u001a\u00020\rH\u0002J\u0010\u0010\u000e\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016R\u0012\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0012"}, m421d2 = {"Lkotlin/collections/builders/SerializedCollection;", "Ljava/io/Externalizable;", "()V", "collection", "", "tag", "", "(Ljava/util/Collection;I)V", "readExternal", "", "input", "Ljava/io/ObjectInput;", "readResolve", "", "writeExternal", "output", "Ljava/io/ObjectOutput;", "Companion", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class SerializedCollection implements Externalizable {
    public static final Companion Companion = null;
    private static final long serialVersionUID = 0;
    public static final int tagList = 0;
    public static final int tagSet = 1;
    private Collection<?> collection;
    private final int tag;

    /* JADX INFO: compiled from: ListBuilder.kt */
    @Metadata(m420d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0086T¢\u0006\u0002\n\u0000¨\u0006\b"}, m421d2 = {"Lkotlin/collections/builders/SerializedCollection$Companion;", "", "()V", "serialVersionUID", "", "tagList", "", "tagSet", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker r1) {
            this();
        }
    }

    static {
        Companion = new Companion(null);
    }

    public SerializedCollection() {
        this(CollectionsKt.emptyList(), 0);
    }

    public SerializedCollection(Collection<?> r2, int r3) {
        Intrinsics.checkNotNullParameter(r2, "collection");
        this.collection = r2;
        this.tag = r3;
    }

    private final Object readResolve() {
        return this.collection;
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput r12) {
        Intrinsics.checkNotNullParameter(r12, "input");
        byte r0 = r12.readByte();
        int r1 = r0 & 1;
        if ((r0 & (-2)) != 0) goto L23;
        int r4 = r12.readInt();
        if (r4 < 0) goto L21;
        int r5 = 0;
        switch(r1) {
            case 0: goto L14;
            case 1: goto L10;
            default: goto L9;
        };
    L10:
        Set r3 = SetsKt.createSetBuilder(r4);
    L11:
        if (r5 >= r4) goto L13;
        r3.add(r12.readObject());
        r5 = r5 + 1;
        goto L11
    L13:
        List r6 = SetsKt.build(r3);
    L18:
        this.collection = r6;
        return;
    L14:
        List r7 = CollectionsKt.createListBuilder(r4);
    L15:
        if (r5 >= r4) goto L17;
        r7.add(r12.readObject());
        r5 = r5 + 1;
        goto L15
    L17:
        r6 = CollectionsKt.build(r7);
        goto L18
    L9:
        throw new InvalidObjectException("Unsupported collection type tag: " + r1 + '.');
    L21:
        throw new InvalidObjectException("Illegal size value: " + r4 + '.');
    L23:
        throw new InvalidObjectException("Unsupported flags value: " + r0 + '.');
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput r3) {
        Intrinsics.checkNotNullParameter(r3, "output");
        r3.writeByte(this.tag);
        r3.writeInt(this.collection.size());
        Iterator<?> r0 = this.collection.iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        r3.writeObject(r0.next());
        goto L4
    }
}
