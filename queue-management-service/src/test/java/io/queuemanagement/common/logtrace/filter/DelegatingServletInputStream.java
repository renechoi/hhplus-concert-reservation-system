package io.queuemanagement.common.logtrace.filter;

import java.io.IOException;
import java.io.InputStream;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;

public class DelegatingServletInputStream extends ServletInputStream {
	private final InputStream sourceStream;

	public DelegatingServletInputStream(InputStream sourceStream) {
		this.sourceStream = sourceStream;
	}

	@Override
	public int read() throws IOException {
		return sourceStream.read();
	}

	@Override
	public int available() throws IOException {
		return sourceStream.available();
	}

	@Override
	public void close() throws IOException {
		super.close();
		sourceStream.close();
	}

	@Override
	public void setReadListener(ReadListener readListener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isReady() {
		return true;
	}

	@Override
	public boolean isFinished() {
		try {
			return sourceStream.available() == 0;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}