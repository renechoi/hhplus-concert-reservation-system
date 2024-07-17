package io.apiorchestrationservice.common.logtrace.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

/**
 * @author : Rene Choi
 * @since : 2024/07/17
 */
public class CachedBodyHttpServletResponse extends HttpServletResponseWrapper {
	private final ByteArrayOutputStream cachedBody;
	private final ServletOutputStream outputStream;
	private final PrintWriter writer;

	public CachedBodyHttpServletResponse(HttpServletResponse response) throws IOException {
		super(response);
		this.cachedBody = new ByteArrayOutputStream();
		this.outputStream = new CachedBodyServletOutputStream(this.cachedBody);
		this.writer = new PrintWriter(this.cachedBody);
	}

	@Override
	public ServletOutputStream getOutputStream() {
		return this.outputStream;
	}

	@Override
	public PrintWriter getWriter() {
		return this.writer;
	}

	public byte[] getCachedBody() {
		return this.cachedBody.toByteArray();
	}

	public String getCachedBodyAsString() {
		return new String(this.getCachedBody());
	}

	public void copyBodyToResponse() throws IOException {
		ServletOutputStream responseOutputStream = super.getOutputStream();
		responseOutputStream.write(getCachedBody());
		responseOutputStream.flush();
	}

	private static class CachedBodyServletOutputStream extends ServletOutputStream {
		private final ByteArrayOutputStream outputStream;

		public CachedBodyServletOutputStream(ByteArrayOutputStream outputStream) {
			this.outputStream = outputStream;
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setWriteListener(WriteListener writeListener) {
		}

		@Override
		public void write(int b) throws IOException {
			this.outputStream.write(b);
		}
	}
}



