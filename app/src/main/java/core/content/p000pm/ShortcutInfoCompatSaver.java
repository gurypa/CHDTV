package androidx.core.content.p000pm;

import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class ShortcutInfoCompatSaver<T> {

    public static class NoopImpl extends ShortcutInfoCompatSaver<Void> {
        public NoopImpl() {
        }

        @Override // androidx.core.content.p000pm.ShortcutInfoCompatSaver
        public /* bridge */ /* synthetic */ Void addShortcuts(List r1) {
            return addShortcuts2(r1);
        }

        @Override // androidx.core.content.p000pm.ShortcutInfoCompatSaver
        /* JADX INFO: renamed from: addShortcuts, reason: avoid collision after fix types in other method */
        public Void addShortcuts2(List<ShortcutInfoCompat> r2) {
            return null;
        }

        @Override // androidx.core.content.p000pm.ShortcutInfoCompatSaver
        public /* bridge */ /* synthetic */ Void removeAllShortcuts() {
            return removeAllShortcuts2();
        }

        @Override // androidx.core.content.p000pm.ShortcutInfoCompatSaver
        /* JADX INFO: renamed from: removeAllShortcuts, reason: avoid collision after fix types in other method */
        public Void removeAllShortcuts2() {
            return null;
        }

        @Override // androidx.core.content.p000pm.ShortcutInfoCompatSaver
        public /* bridge */ /* synthetic */ Void removeShortcuts(List r1) {
            return removeShortcuts2(r1);
        }

        @Override // androidx.core.content.p000pm.ShortcutInfoCompatSaver
        /* JADX INFO: renamed from: removeShortcuts, reason: avoid collision after fix types in other method */
        public Void removeShortcuts2(List<String> r2) {
            return null;
        }
    }

    public ShortcutInfoCompatSaver() {
    }

    public abstract T addShortcuts(List<ShortcutInfoCompat> r1);

    public List<ShortcutInfoCompat> getShortcuts() throws Exception {
        return new ArrayList();
    }

    public abstract T removeAllShortcuts();

    public abstract T removeShortcuts(List<String> r1);
}
