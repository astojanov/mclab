package mclint;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import junit.framework.TestCase;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;

public class ProjectTest extends TestCase {
  private Path projectRoot;

  @Override
  protected void setUp() throws IOException {
    projectRoot = Files.createTempDirectory(null);
  }

  private void create(String path) throws IOException {
    Path file = projectRoot.resolve(path);
    Files.createDirectories(file.getParent());
    Files.createFile(file);
  }

  public void testSingleFile() throws IOException {
    create("f.m");

    Project project = Project.at(projectRoot);
    MatlabProgram f = Iterables.getOnlyElement(project.getMatlabPrograms());
    assertEquals("f.m", f.getPath());
    assertFalse(f.isPrivate());
    assertEquals("", f.getPackage());
  }

  public void testPrivateFile() throws IOException {
    create("private/f.m");

    Project project = Project.at(projectRoot);
    MatlabProgram f = Iterables.getOnlyElement(project.getMatlabPrograms());
    assertEquals("private/f.m", f.getPath());
    assertTrue(f.isPrivate());
    assertEquals("", f.getPackage());
  }

  public void testFileInPackage() throws IOException {
    create("+pkg/f.m");
    create("+pkg/+nested/f.m");

    Project project = Project.at(projectRoot);
    FluentIterable<MatlabProgram> programs = FluentIterable.from(project.getMatlabPrograms());
    assertTrue(programs.anyMatch(new Predicate<MatlabProgram>() {
      @Override public boolean apply(MatlabProgram program) {
        return program.getPath().equals("+pkg/f.m")
            && !program.isPrivate()
            && program.getPackage().equals("pkg");
      }
    }));
    assertTrue(programs.anyMatch(new Predicate<MatlabProgram>() {
      @Override public boolean apply(MatlabProgram program) {
        return program.getPath().equals("+pkg/+nested/f.m")
            && !program.isPrivate()
            && program.getPackage().equals("pkg.nested");
      }
    }));
  }
}
