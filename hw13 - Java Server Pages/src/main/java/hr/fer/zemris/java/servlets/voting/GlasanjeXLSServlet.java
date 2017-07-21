package hr.fer.zemris.java.servlets.voting;

import hr.fer.zemris.java.servlets.voting.GlasanjeRezultatiServlet.BandScore;
import hr.fer.zemris.java.servlets.voting.GlasanjeServlet.Band;
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
import java.util.List;

/**
 * This class represents a servlet which generates
 * an XML document from the voting results.
 *
 * @author Luka Čupić
 */
@WebServlet("/glasanje-xls")
public class GlasanjeXLSServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/vnd.ms-excel; charset=utf-8");
		resp.setStatus(HttpServletResponse.SC_OK);

		List<BandScore> scores = (List<BandScore>) req.getSession().getAttribute("scores");
		List<Band> bands = (List<Band>) req.getSession().getAttribute("bands");

		OutputStream out = resp.getOutputStream();
		createHWB(scores, bands).write(out);
		out.flush();
		out.close();
	}

	/**
	 * Creates and returns a new excel document.
	 *
	 * @return a new excel workbook
	 */
	private HSSFWorkbook createHWB(List<BandScore> scores, List<Band> bands) {
		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet(String.valueOf("Scores"));

		for (int i = 0; i < bands.size(); i++) {
			HSSFRow row = sheet.createRow(i);
			row.createCell(0).setCellValue(bands.get(i).getName());
			row.createCell(1).setCellValue(scores.get(i).getVotes());
		}
		return hwb;
	}
}