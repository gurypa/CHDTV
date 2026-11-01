package es.antonio.m3u;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes3.dex */
public class Entry implements Serializable {
    private static final long serialVersionUID = 1;
    public int episode;
    public String group;
    public Map<String, String> headers;
    public String kind;
    public String license;
    public Map<String, String> licenseHeaders;
    public String licenseType;
    public String logo;
    public String manifest;
    transient int playbackLicense;
    transient CatalogCache.TextTable playbackText;
    transient int playbackUrl;
    public String plexBase;
    public String plexKey;
    public int season;
    public String series;
    public String title;
    public String tvgId;
    public String tvgName;
    public String url;

    public Entry() {
        this(true);
    }

    Entry(boolean r3) {
        this.title = "";
        this.group = "Sin sección";
        this.logo = "";
        this.url = "";
        this.kind = "Canales";
        this.series = "";
        this.manifest = "";
        this.licenseType = "";
        this.license = "";
        this.plexBase = "";
        this.plexKey = "";
        this.tvgId = "";
        this.tvgName = "";
        this.season = -1;
        this.episode = -1;
        if (r3 == false) goto L6;
        this.headers = new LinkedHashMap();
        this.licenseHeaders = new LinkedHashMap();
        return;
    }

    public static Entry details(Entry r0) {
        if (r0 == null) goto L4;
        r0.loadPlaybackFields();
    L4:
        return r0;
    }

    private void writeObject(ObjectOutputStream r1) throws IOException {
        loadPlaybackFields();
        r1.defaultWriteObject();
    }

    public String identity() {
        return this.kind + "\n" + this.group + "\n" + this.title;
    }

    public Map<String, String> licenseRequestHeaders() {
        if (this.licenseHeaders != null) goto L6;
        return Collections.emptyMap();
    L6:
        return Collections.unmodifiableMap(this.licenseHeaders);
    }

    synchronized void loadPlaybackFields() {
        monitor-enter(this);
    L8:
        th = move-exception;
        throw th;
    L4:
        if (this.playbackText == null) goto L6;
        this.url = this.playbackText.get(this.playbackUrl);     // Catch: Throwable -> L8
        this.license = this.playbackText.get(this.playbackLicense);     // Catch: Throwable -> L8
        this.playbackText = null;     // Catch: Throwable -> L8
    L6:
        monitor-exit(this);
    }
}
