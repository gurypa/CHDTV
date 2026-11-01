package androidx.media3.datasource;

import android.net.Uri;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.util.Assertions;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;

/* JADX INFO: loaded from: classes.dex */
public final class UdpDataSource extends BaseDataSource {
    public static final int DEFAULT_MAX_PACKET_SIZE = 2000;
    public static final int DEFAULT_SOCKET_TIMEOUT_MILLIS = 8000;
    public static final int UDP_PORT_UNSET = -1;
    private InetAddress address;
    private MulticastSocket multicastSocket;
    private boolean opened;
    private final DatagramPacket packet;
    private final byte[] packetBuffer;
    private int packetRemaining;
    private DatagramSocket socket;
    private final int socketTimeoutMillis;
    private Uri uri;

    public static final class UdpDataSourceException extends DataSourceException {
        public UdpDataSourceException(Throwable r1, int r2) {
            super(r1, r2);
        }
    }

    public UdpDataSource() {
        this(2000);
    }

    public UdpDataSource(int r2) {
        this(r2, 8000);
    }

    public UdpDataSource(int r4, int r5) {
        super(true);
        this.socketTimeoutMillis = r5;
        this.packetBuffer = new byte[r4];
        this.packet = new DatagramPacket(this.packetBuffer, 0, r4);
    }

    @Override // androidx.media3.datasource.DataSource
    public void close() {
        this.uri = null;
        if (this.multicastSocket == null) goto L9;
        this.multicastSocket.leaveGroup((InetAddress) Assertions.checkNotNull(this.address));     // Catch: IOException -> L6
    L7:
        this.multicastSocket = null;
    L9:
        if (this.socket == null) goto L11;
        this.socket.close();
        this.socket = null;
    L11:
        this.address = null;
        this.packetRemaining = 0;
        if (this.opened == false) goto L17;
        this.opened = false;
        transferEnded();
        return;
    }

    public int getLocalPort() {
        if (this.socket != null) goto L7;
        return -1;
    L7:
        return this.socket.getLocalPort();
    }

    @Override // androidx.media3.datasource.DataSource
    public Uri getUri() {
        return this.uri;
    }

    @Override // androidx.media3.datasource.DataSource
    public long open(DataSpec r6) throws UdpDataSourceException {
        this.uri = r6.uri;
        String r0 = (String) Assertions.checkNotNull(this.uri.getHost());
        int r1 = this.uri.getPort();
        transferInitializing(r6);
        this.address = InetAddress.getByName(r0);     // Catch: IOException -> L10 SecurityException -> L13
        InetSocketAddress r2 = new InetSocketAddress(this.address, r1);     // Catch: IOException -> L10 SecurityException -> L13
        if (this.address.isMulticastAddress() == false) goto L6;
        this.multicastSocket = new MulticastSocket(r2);     // Catch: IOException -> L10 SecurityException -> L13
        this.multicastSocket.joinGroup(this.address);     // Catch: IOException -> L10 SecurityException -> L13
        this.socket = this.multicastSocket;     // Catch: IOException -> L10 SecurityException -> L13
    L7:
        this.socket.setSoTimeout(this.socketTimeoutMillis);     // Catch: IOException -> L10 SecurityException -> L13
        this.opened = true;
        transferStarted(r6);
        return -1;
    L6:
        this.socket = new DatagramSocket(r2);     // Catch: IOException -> L10 SecurityException -> L13
    L10:
        e = move-exception;
        throw new UdpDataSourceException(e, PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED);
    L13:
        e = move-exception;
        throw new UdpDataSourceException(e, PlaybackException.ERROR_CODE_IO_NO_PERMISSION);
    }

    @Override // androidx.media3.common.DataReader
    public int read(byte[] r4, int r5, int r6) throws UdpDataSourceException {
        if (r6 != 0) goto L6;
        return 0;
    L6:
        if (this.packetRemaining == 0) goto L17;
    L15:
        int r0 = this.packet.getLength() - this.packetRemaining;
        int r1 = Math.min(this.packetRemaining, r6);
        System.arraycopy(this.packetBuffer, r0, r4, r5, r1);
        this.packetRemaining -= r1;
        return r1;
    L17:
        ((DatagramSocket) Assertions.checkNotNull(this.socket)).receive(this.packet);     // Catch: IOException -> L9 SocketTimeoutException -> L12
        this.packetRemaining = this.packet.getLength();
        bytesTransferred(this.packetRemaining);
        goto L15
    L12:
        e = move-exception;
        throw new UdpDataSourceException(e, PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT);
    L9:
        e = move-exception;
        throw new UdpDataSourceException(e, PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED);
    }
}
