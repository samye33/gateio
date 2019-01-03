package com.utils;

import java.io.*;
import java.util.zip.*;

public class CustomGetMethod extends org.apache.commons.httpclient.methods.GetMethod {

	public CustomGetMethod(String uri) {

		super(uri);

	}

	/**
	 * Get response as string whether response is GZipped or not
	 * 
	 * 
	 * @return
	 * 
	 * @throws IOException
	 */
	@Override
	public String getResponseBodyAsString() throws IOException {

		GZIPInputStream gzin;
		//System.out.println("###############" + getResponseHeader("Content-Encoding") + "###############");
		if (getResponseBody() != null || getResponseStream() != null) {

			if ((getResponseHeader("Content-Encoding") != null
					&& getResponseHeader("Content-Encoding").getValue().toLowerCase().indexOf("gzip") > -1)) {

				// For GZip response

				InputStream is = getResponseBodyAsStream();

				gzin = new GZIPInputStream(is);

				InputStreamReader isr = new InputStreamReader(gzin, getResponseCharSet());

				java.io.BufferedReader br = new java.io.BufferedReader(isr);

				StringBuffer sb = new StringBuffer();

				String tempbf;

				while ((tempbf = br.readLine()) != null) {
					sb.append(tempbf);
					sb.append("\r\n");
				}
				isr.close();
				gzin.close();
				return sb.toString();
			}
			if ((getResponseHeader("Content-Encoding") != null
					&& getResponseHeader("Content-Encoding").getValue().toLowerCase().indexOf("deflate") > -1)) {
				String data = null;

				try {
					data = this.toDeflate(this.getResponseBody(), (int) this.getResponseContentLength());
				} catch (DataFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return data;
			} else {
				// For deflate response
				return super.getResponseBodyAsString();
			}
		} else {
			return super.getResponseBodyAsString();

		}
	}

	private String toDeflate(byte[] is, int contentLength) throws IOException, DataFormatException {
		// Decompress the bytes
		InputStream inflInstream = new InflaterInputStream(new ByteArrayInputStream(is), new Inflater(true));

		byte bytes[] = new byte[1024];
		StringBuffer buf = new StringBuffer();
		while (true) {
			int length = inflInstream.read(bytes, 0, 1024);
			if (length == -1)
				break;
			String row = new String(bytes, 0, length);
			buf.append(row);
		}
		return buf.toString();
	}

	private static final int BUFFER_SIZE = 4 * 1024;

	/**
	 * compress data by {@linkplain Level}
	 * 
	 * @author lichengwu
	 * @created 2013-02-07
	 * 
	 * @param data
	 * @param level
	 *            see {@link Level}
	 * @return
	 * @throws IOException
	 */
	public byte[] compress(byte[] data, Level level) throws IOException {

		Deflater deflater = new Deflater();
		// set compression level
		deflater.setLevel(level.getLevel());
		deflater.setInput(data);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);

		deflater.finish();
		byte[] buffer = new byte[BUFFER_SIZE];
		while (!deflater.finished()) {
			int count = deflater.deflate(buffer); // returns the generated
													// code... index
			outputStream.write(buffer, 0, count);
		}
		byte[] output = outputStream.toByteArray();
		outputStream.close();
		return output;
	}

	/**
	 * decompress data
	 * 
	 * @author lichengwu
	 * @created 2013-02-07
	 * 
	 * @param data
	 * @return
	 * @throws IOException
	 * @throws DataFormatException
	 */
	public byte[] decompress(byte[] data) throws IOException, DataFormatException {

		Inflater inflater = new Inflater();
		inflater.setInput(data);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[BUFFER_SIZE];
		while (!inflater.finished()) {
			int count = inflater.inflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		byte[] output = outputStream.toByteArray();
		outputStream.close();
		return output;
	}

	/**
	 * Compression level
	 */
	public static enum Level {

		/**
		 * Compression level for no compression.
		 */
		NO_COMPRESSION(0),

		/**
		 * Compression level for fastest compression.
		 */
		BEST_SPEED(1),

		/**
		 * Compression level for best compression.
		 */
		BEST_COMPRESSION(9),

		/**
		 * Default compression level.
		 */
		DEFAULT_COMPRESSION(-1);

		private int level;

		Level(

				int level) {
			this.level = level;
		}

		public int getLevel() {
			return level;
		}
	}

}
