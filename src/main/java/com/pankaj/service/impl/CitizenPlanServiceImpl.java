package com.pankaj.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.xmlbeans.impl.tool.FactorImports;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.pankaj.binding.SearchCriteria;
import com.pankaj.entity.CitizenPlan;
import com.pankaj.repository.CitizenPlanRepository;
import com.pankaj.service.CitizenPlanService;
import com.pankaj.utils.EmailUtils;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class CitizenPlanServiceImpl implements CitizenPlanService {

	@Autowired
	private CitizenPlanRepository citizenPlanRepository;

	@Autowired
	private EmailUtils emailUtils;

	@Override
	public List<String> getPlanNames() {
		List<String> planNames = citizenPlanRepository.getPlanNames();
		return planNames;
	}

	@Override
	public List<String> getPlanStatus() {
		List<String> planStatus = citizenPlanRepository.getPlanStatus();
		return planStatus;
	}

	@Override
	public List<CitizenPlan> searchCitizens(SearchCriteria criteria) {
		// TODO implementation of search filter remaining
		CitizenPlan entity = new CitizenPlan();

		if (StringUtils.isNotBlank(criteria.getPlanName())) {
			entity.setPlanName(criteria.getPlanName());

		}
		if (StringUtils.isNotBlank(criteria.getPlanStatus())) {
			entity.setPlanStatus(criteria.getPlanStatus());

		}
		if (StringUtils.isNotBlank(criteria.getGender())) {
			entity.setGender(criteria.getGender());

		}
		if (null != criteria.getPlanStartDate()) {
			entity.setPlanStartDate(criteria.getPlanStartDate());
		}

		if (null != criteria.getPlanEndDate()) {
			entity.setPlanEndDate(criteria.getPlanEndDate());
		}

		// JPA Used here Query by Example:-(QBE used to filter the data based on entity
		// objects)
		Example<CitizenPlan> of = Example.of(entity);

		return citizenPlanRepository.findAll(of);
	}

	@Override
	public void generateExcel(HttpServletResponse response) throws Exception {

		List<CitizenPlan> records = citizenPlanRepository.findAll();

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("DataRecord");
		HSSFRow headerRow = sheet.createRow(0);

		// Set Data for header row cells

		headerRow.createCell(0).setCellValue("Name");
		headerRow.createCell(1).setCellValue("Email");
		headerRow.createCell(2).setCellValue("Gender");
		headerRow.createCell(3).setCellValue("SSN");
		headerRow.createCell(4).setCellValue("Plan Name");
		headerRow.createCell(0).setCellValue("Plan Status");

		int rowIndex = 1;
		for (CitizenPlan record : records) {
			HSSFRow dataRow = sheet.createRow(rowIndex);

			dataRow.createCell(0).setCellValue(record.getName());
			dataRow.createCell(1).setCellValue(record.getEmail());
			dataRow.createCell(2).setCellValue(record.getGender());
			dataRow.createCell(3).setCellValue(record.getSsn());
			dataRow.createCell(0).setCellValue(record.getPlanName());
			dataRow.createCell(0).setCellValue(record.getPlanStatus());

			rowIndex++;

		}
		// To send file in mail
		File f = new File("data.xls");
		FileOutputStream fos = new FileOutputStream(f);
		workbook.write(fos);
		emailUtils.sendEmail(f);

		// To Download file in Browser
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();

	}

	@Override
	public void generatePdf(HttpServletResponse response) throws Exception {

		// List<CitizenPlan> records= citizenPlanRepository.findAll();

		Document pdfDoc1 = new Document(PageSize.A4); // For Browser Download

		ServletOutputStream outputStrem = response.getOutputStream();
		PdfWriter.getInstance(pdfDoc1, outputStrem);
		pdfDoc1.open();

		Document pdfDoc2 = new Document(PageSize.A4); // For Email-Attachment Download

		File f = new File("data.pdf");
		FileOutputStream fos = new FileOutputStream(f);
		PdfWriter.getInstance(pdfDoc2, fos);
		pdfDoc2.open();

		Font fonTitle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
		fonTitle.setSize(20);
		Paragraph p = new Paragraph("Citizen Plans Info", fonTitle);
		p.setAlignment(Paragraph.ALIGN_CENTER);

		pdfDoc1.add(p);
		pdfDoc2.add(p);

		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100);
		table.setWidths(new int[] { 3, 3, 3, 3, 3, 3 });
		table.setSpacingBefore(5);

		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(CMYKColor.CYAN);
		cell.setPadding(5);

		Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
		font.setColor(CMYKColor.WHITE);

		cell.setPhrase(new Phrase("Name", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Email", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Gender", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("SSN", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Plan Name", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Plan Status", font));
		table.addCell(cell);

		List<CitizenPlan> records = citizenPlanRepository.findAll();
		for (CitizenPlan record : records) {
			table.addCell(record.getName());
			table.addCell(record.getEmail());
			table.addCell(record.getGender());
			table.addCell(String.valueOf(record.getSsn()));
			table.addCell(record.getPlanName());
			table.addCell(record.getPlanStatus());
		}
		pdfDoc1.add(table);
		pdfDoc2.add(table);

		pdfDoc1.close();
		outputStrem.close();

		pdfDoc2.close();
		fos.close();

		emailUtils.sendEmail(f);

	}

}
