// Copyright (c) 2003-2005 by Leif Frenzel - see http://leiffrenzel.de
package net.sf.eclipsefp.haskell.ui.preferences;

import java.util.ArrayList;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import net.sf.eclipsefp.haskell.core.HaskellCorePlugin;
import net.sf.eclipsefp.haskell.core.preferences.ICorePreferenceNames;


/** <p>the preference page for new Haskell projects. The user can pre-define
  * settings here that apply when the 'New Haskell Project' wizard creates
  * a new project.</p>  
  * 
  * @author Leif Frenzel
  */
public class NewHaskellProjectPP extends PreferencePage
                                 implements IWorkbenchPreferencePage {

  private ArrayList<Button> alRadioButtons;
  private ArrayList<Text> alTextControls;

  private SelectionListener selectionListener;
  private ModifyListener modifyListener;

  private Text txtBinFolderName;
  private Text txtSrcFolderName;
  private Text txtOutFolderName;
  private Text txtTargetBinaryName;

  private Button btnProjectAsSourceFolder;
  private Button btnFoldersAsSourceFolder;


  public NewHaskellProjectPP() {
    super( "New Haskell Project Preferences" );
    setDescription(   "Specify the names of the default directories when "
    alRadioButtons = new ArrayList<Button>();
    alTextControls = new ArrayList<Text>();

    selectionListener = new SelectionListener() {
      public void widgetDefaultSelected( final SelectionEvent e ) {
        // unused
      }

      public void widgetSelected( final SelectionEvent e ) {
        controlChanged( e.widget );
      }
    };

    modifyListener = new ModifyListener() {
      public void modifyText( final ModifyEvent evt ) {
        controlModified( evt.widget );
      }
    };
  }

  public static void initDefaults( final IPreferenceStore store ) {
    store.setDefault( ICorePreferenceNames.FOLDERS_IN_NEW_PROJECT, true );
    store.setDefault( ICorePreferenceNames.FOLDERS_SRC, "src" );
    store.setDefault( ICorePreferenceNames.FOLDERS_BIN, "bin" );
    store.setDefault( ICorePreferenceNames.FOLDERS_OUT, "out" );
    store.setDefault( ICorePreferenceNames.TARGET_BINARY, "theResult" );
  }

  public void init( final IWorkbench workbench ) {
    // empty implementation
  }

  
  // helping methods
  //////////////////
  
  private Button addRadioButton( final Composite parent,
                                 final String label,
                                 final String key,
                                 final String value,
                                 final int indent ) {
    GridData gd = new GridData( GridData.HORIZONTAL_ALIGN_FILL );
    gd.horizontalSpan = 2;
    gd.horizontalIndent = indent;

    Button result = new Button( parent, SWT.RADIO );
    result.setText( label );
    result.setData( new String[] { key, value } );
    result.setLayoutData( gd );

    boolean selected = value.equals( getPreferenceStore().getString( key ) );
    result.setSelection( selected );

    alRadioButtons.add( result );
    return result;
  }

  private Text addTextControl( final Composite parent,
                               final String labelText,
                               final String key,
                               final int indent ) {
    Label label = new Label( parent, SWT.NONE );
    label.setText( labelText );
                                 
    GridData gd = new GridData();
    gd.horizontalIndent = indent;

    label.setLayoutData( gd );

    gd = new GridData( GridData.FILL_HORIZONTAL );
    gd.widthHint = convertWidthInCharsToPixels( 40 );

    Text result = new Text( parent, SWT.SINGLE | SWT.BORDER );
    result.setText( getPreferenceStore().getString( key ) );
    result.setData( key );
    result.setLayoutData( gd );

    alTextControls.add( result );
    result.addModifyListener( modifyListener );
    return result;
  }

  protected Control createContents( final Composite parent ) {
    initializeDialogUnits( parent );

    Composite result = new Composite( parent, SWT.NONE );
    GridLayout layout = new GridLayout();
    layout.marginHeight =
      convertVerticalDLUsToPixels( IDialogConstants.VERTICAL_MARGIN );
    layout.marginWidth = 0;
    layout.verticalSpacing = convertVerticalDLUsToPixels( 10 );
    layout.horizontalSpacing =
      convertHorizontalDLUsToPixels( IDialogConstants.HORIZONTAL_SPACING );
    layout.numColumns = 2;
    result.setLayout( layout );

    createFoldersGroup( result );
    validate();

    Dialog.applyDialogFont( result );
    return result;
  }

  private void createFoldersGroup( final Composite result ) {
    Group folderGroup = new Group( result, SWT.NONE );
    GridLayout layout = new GridLayout();
    layout.numColumns = 2;
    folderGroup.setLayout( layout );
    
    GridData gd = new GridData( GridData.FILL_HORIZONTAL );
    gd.horizontalSpan = 2;
    folderGroup.setLayoutData( gd );
    folderGroup.setText( "Source and output folders" );
    
    createRadios( folderGroup );
    createTexts( folderGroup );
  }

  private void createRadios( final Group folderGroup ) {
    int indent = 0;
    String prefFolders = ICorePreferenceNames.FOLDERS_IN_NEW_PROJECT;
    btnProjectAsSourceFolder = addRadioButton( folderGroup, 
                                               "Project",
                                               prefFolders, 
                                               IPreferenceStore.FALSE, 
                                               indent );
    btnProjectAsSourceFolder.addSelectionListener( selectionListener );
    btnFoldersAsSourceFolder = addRadioButton( folderGroup, 
                                               "Folders",
                                               prefFolders, 
                                               IPreferenceStore.TRUE, 
                                               indent );
    btnFoldersAsSourceFolder.addSelectionListener( selectionListener );
  }

  private void createTexts( final Group folderGroup ) {
    int indent = convertWidthInCharsToPixels( 4 );
    txtSrcFolderName = addTextControl( folderGroup, 
                                         "Source folder name:", 
                                         ICorePreferenceNames.FOLDERS_SRC, 
                                         indent );
    txtOutFolderName = addTextControl( folderGroup, 
                                         "Output folder name:", 
                                         ICorePreferenceNames.FOLDERS_OUT, 
                                         indent );
    txtBinFolderName = addTextControl( folderGroup, 
                                         "Binaries folder name:", 
                                         ICorePreferenceNames.FOLDERS_BIN, 
                                         indent );
    txtTargetBinaryName = addTextControl( folderGroup, 
                                          "Target binary name:", 
                                          ICorePreferenceNames.TARGET_BINARY, 
                                         indent );
  }

  private void validate() {
    boolean useFolders = btnFoldersAsSourceFolder.getSelection();
    txtSrcFolderName.setEnabled( useFolders );
    txtOutFolderName.setEnabled( useFolders );
    txtBinFolderName.setEnabled( useFolders );
    txtTargetBinaryName.setEnabled( useFolders );

    boolean valid = true;
    if( useFolders ) {
      valid = validateFolders();
    }
    if( valid ) {
      setValid( true );
      setMessage( null );
      setErrorMessage( null );
    }
  }

  private boolean validateFolders() {
    boolean result;
    if(    txtBinFolderName.getText().length() == 0 
        && txtSrcFolderName.getText().length() == 0 
        && txtOutFolderName.getText().length() == 0 ) {
      updateNonOkStatus( "Folder names are empty." );
      result = false;
    } else {
      IProject dmy = getWorkspace().getRoot().getProject( "project" );
      result =    isValidPath( txtSrcFolderName.getText(), "source", dmy )
               && isValidPath( txtOutFolderName.getText(), "output", dmy )
               && isValidPath( txtBinFolderName.getText(), "binaries", dmy );
    }
    return result;
  }

  private boolean isValidPath( final String folderName,
                               final String name, 
                               final IProject dmy ) {
    boolean result = true;
    IPath path = dmy.getFullPath().append( folderName );
    if( folderName.length() != 0 ) {
      IStatus status = getWorkspace().validatePath( path.toString(), 
                                                    IResource.FOLDER );
      if( !status.isOK() ) {
        String message =   "Invalid " + name + " path name:" 
                         + status.getMessage();
        updateNonOkStatus( message );
        result = false;
      }
    }
    return result;
  }

  private IWorkspace getWorkspace() {
    return ResourcesPlugin.getWorkspace();
  }
  
  private void updateNonOkStatus( final String message ) {
    setValid( false );
    setMessage( message, NONE );
    setErrorMessage( message );
  }

  private void controlChanged( final Widget widget ) {
    if(    widget == btnFoldersAsSourceFolder 
        || widget == btnProjectAsSourceFolder ) {
      validate();
    }
  }

  private void controlModified( final Widget widget ) {
    if(    widget == txtSrcFolderName 
        || widget == txtBinFolderName
        || widget == txtOutFolderName ) {
      validate();
    }
  }

  /* @see PreferencePage#performDefaults() */
  protected void performDefaults() {
    IPreferenceStore store = getPreferenceStore();
    for( int i = 0; i < alRadioButtons.size(); i++ ) {
      Button button = alRadioButtons.get( i );
      String[] info = ( String[] )button.getData();
      boolean sel = info[ 1 ].equals( store.getDefaultString( info[ 0 ] ) );
      button.setSelection( sel );
    }
    for( int i = 0; i < alTextControls.size(); i++ ) {
      Text text = alTextControls.get( i );
      String key = ( String )text.getData();
      text.setText( store.getDefaultString( key ) );
    }
    validate();
    super.performDefaults();
  }

  /* @see IPreferencePage#performOk() */
  public boolean performOk() {
    IPreferenceStore store = getPreferenceStore();
    for( int i = 0; i < alRadioButtons.size(); i++ ) {
      Button button = alRadioButtons.get( i );
      if( button.getSelection() ) {
        String[] info = ( String[] )button.getData();
        store.setValue( info[ 0 ], info[ 1 ] );
      }
    }
    for( int i = 0; i < alTextControls.size(); i++ ) {
      Text text = alTextControls.get( i );
      String key = ( String )text.getData();
      store.setValue( key, text.getText() );
    }
    HaskellCorePlugin.getDefault().savePluginPreferences();
    return super.performOk();
  }

  @Override
  protected IPreferenceStore doGetPreferenceStore() {
	  return new ScopedPreferenceStore(new InstanceScope(),
			         HaskellCorePlugin.getDefault()
			             .getBundle().getSymbolicName());
  }
  
  
}