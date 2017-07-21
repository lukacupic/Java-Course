package hr.fer.zemris.java.servlets;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This class represents a servlet which dynamically generates
 * an excel document from the given parameters. The document
 * consists of "n" pages, where each page display two columns
 * where the first column contains values from "a" to "b" and
 * the second column contains the n-th power of the value from
 * the first column.
 *
 * @author Luka Čupić
 */
@WebServlet("/powers")
public class XLSCreatorServlet extends HttpServlet {

    private boolean error = false;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/vnd.ms-excel; charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);

        HSSFWorkbook hwb = null;
        try {
            int a, b, n;
            a = Integer.parseInt(req.getParameter("a"));
            b = Integer.parseInt(req.getParameter("b"));
            n = Integer.parseInt(req.getParameter("n"));

            if (a < -100 || a > 100) error = true;
            if (b < -100 || b > 100) error = true;
            if (n < 1 || n > 5) error = true;
            if (b < a) error = true;

            hwb = createHWB(a, b, n);
        } catch (Exception ex) {
            error = true;
        }

        if (error || hwb == null) {
            error = false;
            req.getRequestDispatcher("/WEB-INF/pages/denied.jsp").forward(req, resp);
        } else {
            OutputStream out = resp.getOutputStream();
            hwb.write(out);
            out.flush();
            out.close();
        }
    }

    /**
     * Creates and returns a new excel document.
     *
     * @param a the beginning of the interval
     * @param b the end of the interval
     * @param n the number of pages
     * @return a new excel workbook
     */
    private HSSFWorkbook createHWB(int a, int b, int n) {
        HSSFWorkbook hwb = new HSSFWorkbook();
        HSSFSheet[] sheets = new HSSFSheet[n];
        for (int i = 0; i < n; i++) {
            sheets[i] = hwb.createSheet(String.valueOf("Sheet " + i));

            for (int j = 0; j <= b - a; j++) {
                HSSFRow row = sheets[i].createRow(j);
                row.createCell(0).setCellValue(String.valueOf(a + j));
                row.createCell(1).setCellValue(String.valueOf(Math.pow(a + j, i + 1)));
            }
        }
        return hwb;
    }
}