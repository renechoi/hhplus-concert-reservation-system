package io.queuemanagement.common.logtrace.filter;

import java.io.IOException;
import java.io.OutputStream;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;

public class DelegatingServletOutputStream extends ServletOutputStream {
	private final OutputStream targetStream;

	public DelegatingServletOutputStream(OutputStream targetStream) {
		this.targetStream = targetStream;
	}

	@Override
	public void write(int b) throws IOException {
		targetStream.write(b);
	}

	@Override
	public void setWriteListener(WriteListener writeListener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isReady() {
		return true;
	}
}