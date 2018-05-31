/**
 Copyright (c) 2018 Francisco José Fernández Jiménez

 Permission is hereby granted, free of charge, to any person obtaining a
 copy of this software and associated documentation files (the "Software"),
 to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense,
 and/or sell copies of the Software, and to permit persons to whom the Software
 is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in 
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
**/

package es.us.dit.fjfj.servlets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that serves static content from an extern folder.
 * 
 * @author Francisco José Fernández Jiménez
 * @since 2018-05-30
 * @version 1.0
 * 
 */
public class ExternStaticFolderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Path to extern folder.
	 */
	private String externFolder;
	
	private static final int DEFAULT_BUFFER_SIZE = 10240;
	
	protected String getExternFolder() {
		return externFolder;
	}

	protected void setExternFolder(String externFolder) {
		this.externFolder = externFolder;
	}

	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		// Recover the path to the static extern folder. In this case, we use
		// an initParam
		externFolder = config.getInitParameter("ExternFolder");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String externPath = externFolder + request.getPathInfo();
		// Check if user is authorized here or with a filter
		if (isAuthorized(request, response)) {

			File file = new File(externPath);
			if (!file.isFile() || !file.canRead()) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			} else {

				// Start
				response.reset();
				response.setBufferSize(DEFAULT_BUFFER_SIZE);

				// Send content type and length
				String mimeType = getServletContext().getMimeType(externPath);
				if (mimeType == null)
					mimeType = "application/octet-stream";
				response.setContentType(mimeType);
				response.setHeader("Content-Length", String.valueOf(file.length()));

				// Send file
				BufferedInputStream input = null;
				BufferedOutputStream output = null;
				try {
					input = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);
					output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);
					byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
					int length;
					while ((length = input.read(buffer)) > 0) {
						output.write(buffer, 0, length);
					}
				} finally {
					if (input != null)
						input.close();
					if (output != null)
						output.close();
				}
			}
		} else {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
		}
	}

	/**
	 * Check if this path is authorized. Override in subclasses.
	 * 
	 * @param request
	 * @param response
	 * @return True if authorized.
	 */
	protected boolean isAuthorized(HttpServletRequest request, HttpServletResponse response) {
		return true;
	}

}
