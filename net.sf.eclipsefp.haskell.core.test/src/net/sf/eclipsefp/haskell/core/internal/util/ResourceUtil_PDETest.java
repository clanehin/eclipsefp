package net.sf.eclipsefp.haskell.core.internal.util;

import net.sf.eclipsefp.haskell.core.internal.project.HaskellProject;
import net.sf.eclipsefp.haskell.core.test.TestCaseWithProject;
import net.sf.eclipsefp.haskell.core.util.ResourceUtil;
import net.sf.eclipsefp.haskell.util.FileUtil;
import org.eclipse.core.resources.IFile;

public class ResourceUtil_PDETest extends TestCaseWithProject {

	public void testExtractModuleNameFromUnliterateFileName() {
    assertEquals( "MyModule", ResourceUtil.getModuleName( "MyModule.hs" ) );
  }

  public void testExtractModuleNameFromLiterateFileName() {
    assertEquals( "MyModule", ResourceUtil.getModuleName( "MyModule.lhs" ) );
  }

  public void testGetSourceDirRelativeName() throws Exception {
    HaskellProject hp = new HaskellProject( project );
    hp.addSourcePath( FileUtil.DEFAULT_FOLDER_SRC );

    IFile file = null;
    try {
      file = project.getFile( "A.hs" );
      ResourceUtil.getSourceFolderRelativeName( file );
      fail();
    } catch( final IllegalArgumentException illarex ) {
      // expected

    }
    file = project.getFile( "src/A.hs" );
    assertEquals( "A.hs", ResourceUtil.getSourceFolderRelativeName( file).toPortableString() );

    file = project.getFile( "src/Bla/A.hs" );
    assertEquals( "Bla/A.hs",
                  ResourceUtil.getSourceFolderRelativeName( file ).toPortableString() );

    file = project.getFile( "src/Some/Long/Path/A.hs" );
    assertEquals( "Some/Long/Path/A.hs",
                  ResourceUtil.getSourceFolderRelativeName( file ).toPortableString() );

    try {
      file = project.getFile( "test/Some/Path/A.hs" );
      ResourceUtil.getSourceFolderRelativeName( file );
      fail();
    } catch( final IllegalArgumentException illarex ) {
      // expected
    }

    hp.addSourcePath( "test" );
    file = project.getFile( "test/Path/A.hs" );
    assertEquals( "Path/A.hs",
                  ResourceUtil.getSourceFolderRelativeName( file ).toPortableString() );
  }
}
