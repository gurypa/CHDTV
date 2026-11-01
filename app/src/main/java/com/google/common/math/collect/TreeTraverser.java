package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Queue;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
@Deprecated
public abstract class TreeTraverser<T> {

    private final class BreadthFirstIterator extends UnmodifiableIterator<T> implements PeekingIterator<T> {
        private final Queue<T> queue;
        final /* synthetic */ TreeTraverser this$0;

        BreadthFirstIterator(TreeTraverser r1, T r2) {
            this.this$0 = r1;
            this.queue = new ArrayDeque();
            this.queue.add(r2);
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return !this.queue.isEmpty();
        }

        @Override // java.util.Iterator, com.google.common.collect.PeekingIterator
        public T next() {
            T r0 = this.queue.remove();
            Iterables.addAll(this.queue, this.this$0.children(r0));
            return r0;
        }

        @Override // com.google.common.collect.PeekingIterator
        public T peek() {
            return this.queue.element();
        }
    }

    private final class PostOrderIterator extends AbstractIterator<T> {
        private final ArrayDeque<PostOrderNode<T>> stack;
        final /* synthetic */ TreeTraverser this$0;

        PostOrderIterator(TreeTraverser r2, T r3) {
            this.this$0 = r2;
            this.stack = new ArrayDeque();
            this.stack.addLast(expand(r3));
        }

        private PostOrderNode<T> expand(T r3) {
            return new PostOrderNode(r3, this.this$0.children(r3).iterator());
        }

        @Override // com.google.common.collect.AbstractIterator
        @CheckForNull
        protected T computeNext() {
        L3:
            if (this.stack.isEmpty() == true) goto L10;
            PostOrderNode<T> r0 = this.stack.getLast();
            if (r0.childIterator.hasNext() == false) goto L7;
            T r1 = r0.childIterator.next();
            this.stack.addLast(expand(r1));
            goto L3
        L7:
            this.stack.removeLast();
            return r0.root;
        L10:
            return endOfData();
        }
    }

    private static final class PostOrderNode<T> {
        final Iterator<T> childIterator;
        final T root;

        PostOrderNode(T r2, Iterator<T> r3) {
            this.root = (T) Preconditions.checkNotNull(r2);
            this.childIterator = (Iterator) Preconditions.checkNotNull(r3);
        }
    }

    private final class PreOrderIterator extends UnmodifiableIterator<T> {
        private final Deque<Iterator<T>> stack;
        final /* synthetic */ TreeTraverser this$0;

        PreOrderIterator(TreeTraverser r2, T r3) {
            this.this$0 = r2;
            this.stack = new ArrayDeque();
            this.stack.addLast(Iterators.singletonIterator(Preconditions.checkNotNull(r3)));
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return !this.stack.isEmpty();
        }

        /* JADX WARN: Type inference fix 'apply assigned field type' failed
        java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
        	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
        	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
        	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
         */
        @Override // java.util.Iterator
        public T next() {
            Iterator<T> r0 = this.stack.getLast();
            T r1 = (T) Preconditions.checkNotNull(r0.next());
            if (r0.hasNext() == true) goto L5;
            this.stack.removeLast();
        L5:
            Iterator<T> r2 = this.this$0.children(r1).iterator();
            if (r2.hasNext() == false) goto L8;
            this.stack.addLast(r2);
        L8:
            return r1;
        }
    }

    public TreeTraverser() {
    }

    @Deprecated
    public static <T> TreeTraverser<T> using(final Function<T, ? extends Iterable<T>> r1) {
        Preconditions.checkNotNull(r1);
        return new C07771(r1);
    }

    @Deprecated
    public final FluentIterable<T> breadthFirstTraversal(final T r2) {
        Preconditions.checkNotNull(r2);
        return new C07804(this, r2);
    }

    public abstract Iterable<T> children(T r1);

    UnmodifiableIterator<T> postOrderIterator(T r2) {
        return new PostOrderIterator(this, r2);
    }

    @Deprecated
    public final FluentIterable<T> postOrderTraversal(final T r2) {
        Preconditions.checkNotNull(r2);
        return new C07793(this, r2);
    }

    UnmodifiableIterator<T> preOrderIterator(T r2) {
        return new PreOrderIterator(this, r2);
    }

    @Deprecated
    public final FluentIterable<T> preOrderTraversal(final T r2) {
        Preconditions.checkNotNull(r2);
        return new C07782(this, r2);
    }
}
