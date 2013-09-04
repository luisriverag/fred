package freenet.support.io;

import java.io.IOException;

public class ByteArrayRandomAccessThing implements LockableRandomAccessThing {

	private final byte[] data;
	private boolean readOnly;
	
	public ByteArrayRandomAccessThing(byte[] padded) {
		this.data = padded;
	}

	@Override
	public void close() {
		// Do nothing
	}

	@Override
	public void pread(long fileOffset, byte[] buf, int bufOffset, int length)
			throws IOException {
		if(fileOffset < 0) throw new IOException("Cannot read before zero");
		if(fileOffset + length > data.length) throw new IOException("Cannot read after end: trying to read from "+fileOffset+" to "+(fileOffset+length)+" on block length "+data.length);
		System.arraycopy(data, (int)fileOffset, buf, bufOffset, length);
	}

	@Override
	public void pwrite(long fileOffset, byte[] buf, int bufOffset, int length)
			throws IOException {
		if(fileOffset < 0) throw new IOException("Cannot write before zero");
		if(fileOffset + length > data.length) throw new IOException("Cannot write after end: trying to write from "+fileOffset+" to "+(fileOffset+length)+" on block length "+data.length);
		if(readOnly) throw new IOException("Read-only");
		System.arraycopy(buf, bufOffset, data, (int)fileOffset, length);
	}

	@Override
	public long size() throws IOException {
		return data.length;
	}

	public void setReadOnly() {
		readOnly = true;
	}
	
    @Override
    public RAFLock lock() {
        return new RAFLock() {

            @Override
            protected void innerUnlock() {
                // Do nothing. Always open.
            }
            
        };
    }

    @Override
    public void free() {
        // Do nothing.
    }

}
