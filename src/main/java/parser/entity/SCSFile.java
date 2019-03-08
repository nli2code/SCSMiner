package parser.entity;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;

public class SCSFile {
  private String path;
  private List<String> importedLibraries;
  private List<SCSUnit> units;

  public SCSFile() {
    importedLibraries = new ArrayList<>();
    units = new ArrayList<>();
  }

  public void setPath(String path) {
    this.path = path;
  }

  public List<String> getImportedLibraries() {
    return importedLibraries;
  }

  public List<SCSUnit> getUnits() {
    return units;
  }

  public void addLibrary(String library) {
    if (!importedLibraries.contains(library)) {
      importedLibraries.add(library);
    }
  }

  public void addUnit(SCSUnit unit) {
    units.add(unit);
  }

  public String toString() {
    String ret = path + "\n";
    ret += Strings.repeat("=", path.length());
    ret += "\nImported libraries:\n";
    for (String library: importedLibraries) {
      ret += "  " + library + "\n";
    }
    ret += "--------\nSCSUnits:\n";
    for (SCSUnit unit: units) {
      ret += unit.toString();
    }
    return ret;
  }
}
