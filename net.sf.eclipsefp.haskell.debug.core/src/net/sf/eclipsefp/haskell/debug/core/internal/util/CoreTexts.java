// Copyright (c) 2006-2008 by Leif Frenzel. All rights reserved.
// This code is made available under the terms of the Eclipse Public License,
// version 1.0 (EPL). See http://www.eclipse.org/legal/epl-v10.html
package net.sf.eclipsefp.haskell.debug.core.internal.util;

import org.eclipse.osgi.util.NLS;

/** <p>provides internationalized String messages for the core.</p>
  *
  * @author Leif Frenzel
  */
public final class CoreTexts extends NLS {

  // message fields
  public static String haskellLaunchDelegate_noExe;
  public static String haskellLaunchDelegate_noProcess;

  public static String commandonchange_failed;
  public static String console_command_failed;

  public static String breakpoint_message;
  public static String thread_default_name;

  public static String launchconfiguration_delete_failed;

  public static String running;
  public static String testSuite_waiting;
  public static String profiling_waiting;

  public static String jdt_notFound_title;
  public static String jdt_notFound_message;

  private static final String BUNDLE_NAME
    = CoreTexts.class.getPackage().getName() + ".coretexts"; //$NON-NLS-1$

  static {
    NLS.initializeMessages( BUNDLE_NAME, CoreTexts.class );
  }
}