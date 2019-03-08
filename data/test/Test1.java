import org.apache.poi.ss.usermodel.*;

public class Test1 {

  public Test1() {}

  class InnerClass {
    /** @deprecated **/
    void innerFoo(short a) {}
  }

  void fun(Workbook wb) {
    CellStyle style = wb.createCellStyle();
    Sheet sheet = wb.createSheet("sheet");
    Row row = sheet.createRow(0);
    Cell cell = row.createCell(0);
    style.setFillForegroundColor(IndexedColors.RED.getIndex());
    style.setFillPattern(FillPattern.SOLID);
    cell.setCellStyle(style);
  }

  void bar() {
    A.a();
    B.b();
  }

  void foo() {
    Workbook wb = new HSSFWorkbook();
    A.a();
    try {
      OutputStream fileOut = new FileOutputStream("workbook.xls");
      wb.write(fileOut);
    } catch (IOException e) {
      e.printStackTrace();
    }
    B.b();
  }

  CellStyle createCellStyle(int a, Cell c) {}

  CellStyle createCellStyle(int a) {}

  CellStyle createCellStyle(int a, Cell c) {}

  void createCellStyle(int a, Cell c) {}

  class InnerClass2 {
    void innerBar(short b) {}
  }
}