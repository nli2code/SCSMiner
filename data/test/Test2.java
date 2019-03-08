import org.apache.poi.ss.usermodel.*;

public class Test1 {

  void fun(Workbook wb) {
    CellStyle style = wb.createCellStyle();
    Sheet sheet = wb.createSheet("sheet");
    Row row = sheet.createRow(0);
    Cell cell = row.createCell(0);
    style.setFillForegroundColor(IndexedColors.RED.getIndex());
    style.setFillPattern(FillPattern.SOLID);
    cell.setCellStyle(style);
  }

}