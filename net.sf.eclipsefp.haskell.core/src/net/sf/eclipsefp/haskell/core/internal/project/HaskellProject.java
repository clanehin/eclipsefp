// Copyright (c) 2003-2008 by Leif Frenzel - see http://leiffrenzel.de
package net.sf.eclipsefp.haskell.core.internal.project;

import java.util.Set;
import net.sf.eclipsefp.haskell.core.compiler.CompilerManager;
import net.sf.eclipsefp.haskell.core.compiler.DefaultHaskellCompiler;
import net.sf.eclipsefp.haskell.core.compiler.IHaskellCompiler;
import net.sf.eclipsefp.haskell.core.project.HaskellProjectManager;
import net.sf.eclipsefp.haskell.core.project.IBuildTarget;
import net.sf.eclipsefp.haskell.core.project.IHaskellProject;
import net.sf.eclipsefp.haskell.core.project.IHaskellProjectInfo;
import net.sf.eclipsefp.haskell.core.project.IImportLibrary;
import net.sf.eclipsefp.haskell.core.project.ImportLibrariesList;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;

/**
 * <p>
 * Implementation for a Haskell project info. Contains information about source
 * paths etc. This is essentially what is stored in the .hsproject file in the
 * project directory.
 * </p>
 *
 * @author Leif Frenzel
 */
public final class HaskellProject implements IHaskellProject, IHaskellProjectInfo {

	private final IProject project;
  private final IHaskellProjectInfo projectInfo;
	private IHaskellCompiler compiler;

	public HaskellProject( final IProject project ) {
    compiler = CompilerManager.getInstance().getCompiler();
    this.project = project;
    this.projectInfo = new SimpleHaskellProjectInfo(); // for now...
  }


	// interface methods of IHaskellProject
	// /////////////////////////////////////

	public IProject getResource() {
		return project;
	}

	public Set<IPath> getSourcePaths() {
    return projectInfo.getSourcePaths();
  }

	public Set<IBuildTarget> getTargets() {
	  return projectInfo.getTargets();
	}

	public IPath getOutputPath() {
		return projectInfo.getOutputPath();
	}

	public IPath getBinPath() {
		return projectInfo.getBinPath();
	}

	public IImportLibrary[] getImportLibraries() {
    ImportLibrariesList list = new ImportLibrariesList( getResource() );
    return list.getAll();
  }

	// interface methods of IAdaptable
	// ////////////////////////////////

	public Object getAdapter( final Class required ) {
    Object result = null;
    if( required == IResource.class ) {
      result = getResource();
    }
    return result;
  }

	// internal setters
	// /////////////////

	public void addSourcePath( final IPath sourcePath ) {
    String name = IHaskellProject.PROPERTY_SOURCE_PATH;
    ProjectPropertiesEvent event = new ProjectPropertiesEvent( this, name );
    event.setOldValue( getSourcePaths() );

    projectInfo.addSourcePath( sourcePath );

    event.setNewValue( getSourcePaths() );
    HaskellProjectManager.broadcast( event );
  }

  public void setOutputPath( final IPath outputPath ) {
    String name = IHaskellProject.PROPERTY_OUTPUT_PATH;
    ProjectPropertiesEvent event = new ProjectPropertiesEvent( this, name );
    event.setOldValue( getOutputPath() );

    projectInfo.setOutputPath( outputPath );

    event.setNewValue( getOutputPath() );
    HaskellProjectManager.broadcast( event );
  }

  public void setBinPath( final IPath binPath ) {
    String name = IHaskellProject.PROPERTY_BIN_PATH;
    ProjectPropertiesEvent event = new ProjectPropertiesEvent( this, name );
    event.setOldValue( getBinPath() );

    projectInfo.setBinPath( binPath );

    event.setNewValue( getBinPath() );
    HaskellProjectManager.broadcast( event );
  }

  public void addTarget( final IBuildTarget target ) {
    String name = IHaskellProject.PROPERTY_TARGET;
    ProjectPropertiesEvent event = new ProjectPropertiesEvent( this, name );
    event.setOldValue( getTargets() );

    projectInfo.addTarget( target );

    event.setNewValue( getTargets() );
    HaskellProjectManager.broadcast( event );
  }

  public IContainer getSourceFolder() {
    IContainer result = project;
    if( !getSourcePaths().isEmpty() ) {
      IPath sourcePath = getSourcePaths().iterator().next();
      if( !sourcePath.equals( project.getProjectRelativePath() ) ) {
        result = project.getFolder( sourcePath );
      }
    }
    return result;
  }

  public IHaskellCompiler getCompiler() {
    return compiler;
  }

	public void setCompiler( final IHaskellCompiler comp ) {
    compiler = ( comp == null ) ? new DefaultHaskellCompiler() : comp;
  }

  public void compile( final IFile file ) {
    getCompiler().compile( file );
  }

	// helping methods
  ///////////////////

  /**
   * Returns an {@link IPath} that represents the given path,
   * relative to the project root.
   */
  public IPath getProjectRelativePath( final String whichPath ) {
    IPath result;
    if( "".equals( whichPath ) ) { //$NON-NLS-1$
      result = project.getProjectRelativePath();
    } else {
      result = project.getFolder( whichPath ).getProjectRelativePath();
    }
    return result;
  }

}