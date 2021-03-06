// Copyright (c) 2003-2008 by Leif Frenzel - see http://leiffrenzel.de
// This code is made available under the terms of the Eclipse Public License,
// version 1.0 (EPL). See http://www.eclipse.org/legal/epl-v10.html
package net.sf.eclipsefp.haskell.ui.internal.editors.haskell.text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.eclipsefp.haskell.ui.HaskellUIPlugin;
import net.sf.eclipsefp.haskell.ui.internal.editors.haskell.HaskellInformationControl;
import net.sf.eclipsefp.haskell.ui.internal.editors.haskell.HaskellTextHover;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.IAnnotationHoverExtension;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ILineRange;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.LineRange;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.texteditor.DefaultMarkerAnnotationAccess;

/** <p>Determines all markers for the given line and collects, concatenates,
  * and formats their messages.</p>
  *
  * @author Leif Frenzel
  */
public class AnnotationHover implements IAnnotationHover,IAnnotationHoverExtension {
  private final DefaultMarkerAnnotationAccess fMarkerAnnotationAccess;

  /**
   *
   */
  public AnnotationHover() {
    fMarkerAnnotationAccess = new DefaultMarkerAnnotationAccess();
  }

  // interface methods of IAnnotationHover
  ////////////////////////////////////////

  @Override
  public String getHoverInfo( final ISourceViewer sv, final int line ) {
    String result = null;

    List<Annotation> annotations = getAnnotations( sv, line );
    if( annotations != null ) {
      if( annotations.size() == 1 ) {
        Annotation annotation = annotations.get( 0 );
        result = formatAnnotation( annotation );
      } else {
        List<String> messages = collectMessages( annotations );
        if( messages.size() == 1 ) {
          result = messages.get( 0 );
        } else {
          result = formatMultipleMessages( messages );
        }
      }
    }
    return result;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jface.text.source.IAnnotationHoverExtension#canHandleMouseCursor()
   */
  @Override
  public boolean canHandleMouseCursor() {
    return true;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jface.text.source.IAnnotationHoverExtension#getHoverControlCreator()
   */
  @Override
  public IInformationControlCreator getHoverControlCreator() {
    return new IInformationControlCreator() {

      @Override
      public IInformationControl createInformationControl( final Shell parent ) {
        return new HaskellInformationControl(parent);
      }
    } ;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jface.text.source.IAnnotationHoverExtension#getHoverInfo(org.eclipse.jface.text.source.ISourceViewer, org.eclipse.jface.text.source.ILineRange, int)
   */
  @Override
  public Object getHoverInfo( final ISourceViewer paramISourceViewer,
      final ILineRange paramILineRange, final int paramInt ) {
    try {
     int offset=paramISourceViewer.getDocument().getLineOffset( paramILineRange.getStartLine() );
     int length=paramISourceViewer.getDocument().getLineLength( paramILineRange.getStartLine());
     return HaskellTextHover.computeProblemInfo( paramISourceViewer, new Region( offset, length ), fMarkerAnnotationAccess );
    } catch (BadLocationException ble){
      HaskellUIPlugin.log( ble );
      return null;
    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.jface.text.source.IAnnotationHoverExtension#getHoverLineRange(org.eclipse.jface.text.source.ISourceViewer, int)
   */
  @Override
  public ILineRange getHoverLineRange( final ISourceViewer paramISourceViewer,
      final int paramInt ) {
    return new LineRange( paramInt, 1 );
  }

  // helping methods
  //////////////////

  private String formatAnnotation( final Annotation annotation ) {
    String result = null;
    String message = annotation.getText();
    if( message != null && message.trim().length() > 0 ) {
      result = message.trim();
    }
    return result;
  }

  private List<String> collectMessages( final List<Annotation> annotations ) {
    List<String> result = new ArrayList<String>();
    Iterator<Annotation> it = annotations.iterator();
    while( it.hasNext() ) {
      result.add( formatAnnotation( it.next() ) );
    }
    return result;
  }

  private String formatMultipleMessages( final List<String> messages ) {
    StringBuffer sb = new StringBuffer();
    sb.append( "Multiple markers at this line:\n" );
    Iterator<String> iter = messages.iterator();
    while( iter.hasNext() ) {
      sb.append( "\n  - " );
      sb.append( iter.next() );
    }
    return sb.toString();
  }

  private int compareRulerLine( final Position position,
                                final IDocument document,
                                final int line ) {
    int result = 0;
    if( position.getOffset() > -1 && position.getLength() > -1 ) {
      try {
        int annotationLine = document.getLineOfOffset( position.getOffset() );
        int posEnd = position.getOffset() + position.getLength();
        if( line == annotationLine ) {
          result = 1;
        } else if(    annotationLine < line
                   && line <= document.getLineOfOffset( posEnd ) ) {
          result = 2;
        }
      } catch( BadLocationException badlox ) {
        // ignored
      }
    }
    return result;
  }

  private List<Annotation> getAnnotations( final ISourceViewer viewer,
		                                       final int line ) {
    List<Annotation> result = null;
    IAnnotationModel model = viewer.getAnnotationModel();
    if( model != null ) {
      result = new ArrayList<Annotation>();
      IDocument document = viewer.getDocument();

      Iterator it = model.getAnnotationIterator();
      Map<Position, Set<String>> msgs = new HashMap<Position, Set<String>>();
      while( it.hasNext() ) {
        Object obj = it.next();
        if( obj instanceof Annotation ) {
          Annotation ann = (Annotation)obj;
          // Don't add ocurrences annotations
          if (ann.getType().equals("net.sf.eclipsefp.haskell.ui.occurrences")) {
            continue;
          }

          Position position = model.getPosition( ( Annotation )obj );
          if( position == null ) {
            continue;
          }

          String text = ( ( Annotation )obj ).getText();
          if( isDuplicate( msgs, position, text ) ) {
            continue;
          }

          switch( compareRulerLine( position, document, line ) ) {
          case 1:
            result.add( ( Annotation ) obj );
            break;
          }
        }
      }
    }
    return result;
  }

  // side-effect! this doesn't just check, it also adds new msgs by default
  private boolean isDuplicate(
      final Map<Position, Set<String>> messagesAtPosition,
      final Position position,
      final String message ) {
    boolean result = false;
    if( messagesAtPosition.containsKey( position ) ) {
      Set<String> msgs = messagesAtPosition.get( position );
      result = msgs.contains( message );
      msgs.add( message );
    } else {
      Set<String> msgs = new HashSet<String>();
      msgs.add( message );
      messagesAtPosition.put( position, msgs );
    }
    return result;
  }
}