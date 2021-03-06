// Copyright (c) 2003-2005 by Leif Frenzel - see http://leiffrenzel.de
package net.sf.eclipsefp.haskell.haddock.core.preferences;

import net.sf.eclipsefp.haskell.haddock.HaddockPlugin;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;


/** <p>initializer for the GHC compiler preferences (declared in the
  * <code>plugin.xml</code>).</p>
  *
  * @author Leif Frenzel
  */
public class PreferenceInitializer extends AbstractPreferenceInitializer
                                   implements IHaddockPreferenceNames {

  // interface methods of AbstractPreferenceInitializer
  /////////////////////////////////////////////////////

  @Override
  public void initializeDefaultPreferences() {
    // at least a chance it's on the PATH
    new DefaultScope().getNode( HaddockPlugin.getPluginId() ).put( EXECUTABLE_NAME, "haddock" );
  }
}
