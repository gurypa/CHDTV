package com.google.common.p003io;

import com.google.common.base.Preconditions;
import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class PatternFilenameFilter implements FilenameFilter {
    private final Pattern pattern;

    public PatternFilenameFilter(String r2) {
        this(Pattern.compile(r2));
    }

    public PatternFilenameFilter(Pattern r2) {
        this.pattern = (Pattern) Preconditions.checkNotNull(r2);
    }

    @Override // java.io.FilenameFilter
    public boolean accept(File r2, String r3) {
        return this.pattern.matcher(r3).matches();
    }
}
