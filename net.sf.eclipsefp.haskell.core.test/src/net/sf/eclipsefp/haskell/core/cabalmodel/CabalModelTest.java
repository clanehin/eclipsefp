package net.sf.eclipsefp.haskell.core.cabalmodel;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import junit.framework.TestCase;


public class CabalModelTest extends TestCase {

  public CabalModelTest( final String name ) {
    super( name );
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();

  }

  private String getContent(final String fileName){
    try {
      InputStream is=getClass().getResourceAsStream( fileName);
      ByteArrayOutputStream baos=new ByteArrayOutputStream();
      int c=-1;
      while ((c=is.read())!=-1){
        baos.write(c);
      }
      is.close();
      return new String(baos.toByteArray(),"UTF8");
    } catch (Exception e){
      e.printStackTrace();
      fail(e.getLocalizedMessage());
    }
    return null;
  }

    public void testParseExample1(){
      String content3=getContent( "Example1.cabal" );
      PackageDescription pd=PackageDescriptionLoader.load( content3 );
      PackageDescriptionStanza[] pdss=pd.getStanzas();
      assertNotNull(pdss);
      assertEquals(2,pdss.length);
      assertTrue(pdss[0] instanceof GeneralStanza);
      assertEquals(0,pdss[0].getIndent());
      assertEquals("HUnit",pdss[0].getName());
      assertEquals(0,pdss[0].getStartLine());
      assertEquals(9,pdss[0].getEndLine());
      assertNotNull(pdss[0].getProperties());
      assertEquals(9,pdss[0].getProperties().size());
      assertEquals("HUnit",pdss[0].getProperties().get( "name"));
      assertEquals("HUnit",pdss[0].getProperties().get( "Name"));
      assertEquals("HUnit",pdss[0].getProperties().get( CabalSyntax.FIELD_NAME));
      ValuePosition vp=pdss[0].getPositions().get(CabalSyntax.FIELD_NAME);
      assertEquals(0,vp.getStartLine());
      assertEquals(1,vp.getEndLine());
      assertEquals(7,vp.getInitialIndent());
      assertEquals(13,vp.getSubsequentIndent());
      assertEquals("1.1.1",pdss[0].getProperties().get( "Version"));
      assertEquals(">= 1.2",pdss[0].getProperties().get( "Cabal-Version"));
      assertEquals("BSD3",pdss[0].getProperties().get( "License"));
      assertEquals("LICENSE",pdss[0].getProperties().get( CabalSyntax.FIELD_LICENSE_FILE));
      assertEquals("Dean Herington",pdss[0].getProperties().get( "Author"));
      assertEquals("A unit testing framework for Haskell",pdss[0].getProperties().get( "Synopsis"));
      assertEquals("http://hunit.sourceforge.net/",pdss[0].getProperties().get( "Homepage"));
      assertEquals("Testing",pdss[0].getProperties().get( CabalSyntax.FIELD_CATEGORY));

      assertTrue(pdss[1] instanceof LibraryStanza);
      assertNull(pdss[1].getName());
      assertEquals(2,pdss[1].getIndent());
      assertEquals(10,pdss[1].getStartLine());
      assertEquals(16,pdss[1].getEndLine());
      assertNotNull(pdss[1].getProperties());
      assertEquals(3,pdss[1].getProperties().size());
      assertEquals("base",pdss[1].getProperties().get( "Build-Depends"));
      vp=pdss[1].getPositions().get("Build-Depends");
      assertEquals(11,vp.getStartLine());
      assertEquals(12,vp.getEndLine());
      assertEquals(17,vp.getInitialIndent());
      assertEquals(20,vp.getSubsequentIndent());
      assertEquals("CPP",pdss[1].getProperties().get( "Extensions"));
      assertEquals("Test.HUnit.Base, Test.HUnit.Lang, Test.HUnit.Terminal,\nTest.HUnit.Text, Test.HUnit",pdss[1].getProperties().get( "Exposed-modules"));
  }

  public void testModifyExample1(){
    String content3=getContent( "Small.cabal" );
    PackageDescription pd=PackageDescriptionLoader.load( content3 );
    PackageDescriptionStanza[] pdss=pd.getStanzas();
    PackageDescriptionStanza pds=pdss[0];
    assertEquals("HUnit",pds.getProperties().get( CabalSyntax.FIELD_NAME ));
    RealValuePosition rvp=pds.update( CabalSyntax.FIELD_NAME, "JP" );
    assertEquals("JP"+System.getProperty( "line.separator" ),rvp.getRealValue());
    assertEquals(0,rvp.getStartLine());
    assertEquals(1,rvp.getEndLine());
    assertEquals(13,rvp.getInitialIndent());
    rvp=pds.update( CabalSyntax.FIELD_NAME, "JP2" );
    assertEquals("JP2"+System.getProperty( "line.separator" ),rvp.getRealValue());
    assertEquals(0,rvp.getStartLine());
    assertEquals(1,rvp.getEndLine());
    assertEquals(13,rvp.getInitialIndent());

    rvp=pds.update( CabalSyntax.FIELD_VERSION, "1.0" );
    assertEquals(CabalSyntax.FIELD_VERSION+":     1.0"+System.getProperty( "line.separator" ),rvp.getRealValue());
    assertEquals(1,rvp.getStartLine());
    assertEquals(1,rvp.getEndLine());
    assertEquals(0,rvp.getInitialIndent());

    rvp=pds.update( CabalSyntax.FIELD_VERSION, "1.1.1" );
    assertEquals("1.1.1"+System.getProperty( "line.separator" ),rvp.getRealValue());
    assertEquals(1,rvp.getStartLine());
    assertEquals(2,rvp.getEndLine());
    assertEquals(13,rvp.getInitialIndent());

  }

  public void testParseExample3(){
    String content3=getContent( "Example3.cabal" );
    PackageDescription pd=PackageDescriptionLoader.load( content3 );
    PackageDescriptionStanza[] pdss=pd.getStanzas();
    assertNotNull(pdss);
    assertEquals(4,pdss.length);
    assertTrue(pdss[0] instanceof GeneralStanza);
    assertEquals("TestPackage",pdss[0].getName());
    assertEquals(0,pdss[0].getStartLine());
    assertEquals(7,pdss[0].getEndLine());
    assertNotNull(pdss[0].getProperties());
    assertEquals(7,pdss[0].getProperties().size());
    assertEquals("TestPackage",pdss[0].getProperties().get( "name"));
    assertEquals("TestPackage",pdss[0].getProperties().get( "Name"));
    assertEquals("TestPackage",pdss[0].getProperties().get( "NAME"));
    assertEquals("TestPackage",pdss[0].getProperties().get( CabalSyntax.FIELD_NAME));
    assertEquals("0.0",pdss[0].getProperties().get( "Version"));
    assertEquals(">= 1.2",pdss[0].getProperties().get( "Cabal-Version"));
    assertEquals("BSD3",pdss[0].getProperties().get( "License"));
    assertEquals("Angela Author",pdss[0].getProperties().get( "Author"));
    assertEquals("Package with library and two programs",pdss[0].getProperties().get( "Synopsis"));
    assertEquals("Simple",pdss[0].getProperties().get( "Build-Type"));

    assertTrue(pdss[1] instanceof LibraryStanza);
    assertNull(pdss[1].getName());
    assertEquals(8,pdss[1].getStartLine());
    assertEquals(11,pdss[1].getEndLine());
    assertNotNull(pdss[1].getProperties());
    assertEquals(2,pdss[1].getProperties().size());
    assertEquals("HUnit",pdss[1].getProperties().get( "Build-Depends"));
    assertEquals("A, B, C",pdss[1].getProperties().get( CabalSyntax.FIELD_EXPOSED_MODULES));

    assertTrue(pdss[2] instanceof ExecutableStanza);
    assertEquals("program1",pdss[2].getName());
    assertEquals(12,pdss[2].getStartLine());
    assertEquals(16,pdss[2].getEndLine());
    assertNotNull(pdss[2].getProperties());
    assertEquals(3,pdss[2].getProperties().size());
    assertEquals("Main.hs",pdss[2].getProperties().get( "Main-Is"));
    assertEquals("prog1",pdss[2].getProperties().get( "Hs-Source-Dirs"));
    assertEquals("A, B",pdss[2].getProperties().get( "Other-Modules"));



    assertTrue(pdss[3] instanceof ExecutableStanza);
    assertEquals("program2",pdss[3].getName());
    assertEquals(17,pdss[3].getStartLine());
    assertEquals(21,pdss[3].getEndLine());
    assertNotNull(pdss[3].getProperties());
    assertEquals(3,pdss[3].getProperties().size());
    assertEquals("Main.hs",pdss[3].getProperties().get( "Main-Is"));
    assertEquals("prog2",pdss[3].getProperties().get( "Hs-Source-Dirs"));
    assertEquals("A, C, Utils",pdss[3].getProperties().get( "Other-Modules"));

  }


}