package com.google.common.base;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class CommonMatcher {
    CommonMatcher() {
    }

    public abstract int end();

    public abstract boolean find();

    public abstract boolean find(int r1);

    public abstract boolean matches();

    public abstract String replaceAll(String r1);

    public abstract int start();
}
