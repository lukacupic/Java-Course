package hr.fer.zemris.java.servlets;

import hr.fer.zemris.java.model.PollOption;
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
 * This class represents a servlet which dynamically creates
 * an XML file whose data set represents the results of the
 * poll.
 * The servlet obtains a list of {@link PollOption} from the
 * servlet context and uses this data for storing it into the
 * file.
 *
 * @author Luka Čupić
 */
@WebServlet("/servleti/glasanje-xls")
public class VotingXLSServlet extends HttpServlet {

	@Override
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/vnd.ms-excel; charset=utf-8");
		resp.setStatus(HttpServletResponse.SC_OK);

		List<PollOption> options = (List<PollOption>) req.getServletContext().getAttribute("pollOptions");

		OutputStream out = resp.getOutputStream();
		createHWB(options).write(out);

		out.flush();
		out.close();
	}

	/**
	 * Creates and returns a new excel document.
	 *
	 * @return a new excel workbook
	 */
	private HSSFWorkbook createHWB(List<PollOption> options) {
		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet(String.valueOf("Voting results"));

		for (int i = 0; i < options.size(); i++) {
			HSSFRow row = sheet.createRow(i);
			row.createCell(0).setCellValue(options.get(i).getOptionTitle());
			row.createCell(1).setCellValue(options.get(i).getVotesCount());
		}
		return hwb;
	}
}
