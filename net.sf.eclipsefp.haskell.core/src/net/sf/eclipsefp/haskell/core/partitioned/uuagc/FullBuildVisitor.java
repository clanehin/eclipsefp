package net.sf.eclipsefp.haskell.core.partitioned.uuagc;

import net.sf.eclipsefp.haskell.core.HaskellCorePlugin;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;

/**
 * Builds all resources in the project.
 *
 * @author Alejandro Serrano
 */
public class FullBuildVisitor implements IResourceVisitor {

  @Override
  public boolean visit( final IResource resource ) throws CoreException {
    if( UuagcBuilder.mustBeVisited( resource ) ) {
      // We have to clean the previous markers
      resource.deleteMarkers( HaskellCorePlugin.ID_UUAGC_MARKER, true,
          IResource.DEPTH_ZERO );
     UuagcBuilder.build( resource );
    }
    return true;
  }

}
