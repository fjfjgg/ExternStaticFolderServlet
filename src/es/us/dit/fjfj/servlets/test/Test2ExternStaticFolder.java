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

package es.us.dit.fjfj.servlets.test;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.us.dit.fjfj.servlets.ExternStaticFolderServlet;

/**
 * Example of subclass of ExternStaticFolderServlet
 * 
 * @author Francisco José Fernández Jiménez
 *
 */
@WebServlet("/test2/*")
public class Test2ExternStaticFolder extends ExternStaticFolderServlet {

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		setExternFolder("/home/dit/web");
	}

	private static final long serialVersionUID = 1L;

	/**
	 * Example. Don't allow path starting with "/WEB-INF"
	 * 
	 * @param request
	 * @param response
	 * @return True if authorized.
	 */
	@Override
	protected boolean isAuthorized(HttpServletRequest request, HttpServletResponse response) {
		boolean authorized = true;
		String path = request.getPathInfo();
		if (path!=null && path.startsWith("/WEB-INF") ) {
			authorized = false;
		}
		return authorized;
	}
}
