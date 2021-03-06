/**
 * (c) 2011, Alejandro Serrano
 * Released under the terms of the EPL.
 */
package net.sf.eclipsefp.haskell.ui.internal.editors.partitioned;

import net.sf.eclipsefp.haskell.core.codeassist.ITokenTypes;
import net.sf.eclipsefp.haskell.ui.internal.editors.haskell.text.ScionTokenScanner;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.PatternRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.WordPatternRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.jface.text.source.ISourceViewer;

/**
 * Configures highlighting for Alex lexers.
 * @author Alejandro Serrano
 *
 */
public class AlexSourceViewerConfiguration extends
    PartitionSourceViewerConfiguration {

  /**
   * The constructor
   *
   * @param editor
   *          The associated Haskell editor
   */
  public AlexSourceViewerConfiguration( final PartitionEditor editor ) {
    super( editor );
  }

  @Override
  public IPresentationReconciler getPresentationReconciler(
      final ISourceViewer viewer ) {
    PresentationReconciler reconciler = new PresentationReconciler();
    reconciler.setDocumentPartitioning( PartitionDocumentSetup.PARTITIONING );

    IFile file = ( editor != null ? editor.findFile() : null );
    ScionTokenScanner codeScanner = new ScionTokenScanner(
        getScannerManager(), file);
    DefaultDamagerRepairer haskellDr = new DefaultDamagerRepairer( codeScanner );
    reconciler.setDamager( haskellDr, PartitionDocumentSetup.HASKELL );
    reconciler.setRepairer( haskellDr, PartitionDocumentSetup.HASKELL );

    DefaultDamagerRepairer alexDr = new DefaultDamagerRepairer(
        createAlexScanner() );
    reconciler.setDamager( alexDr, IDocument.DEFAULT_CONTENT_TYPE );
    reconciler.setRepairer( alexDr, IDocument.DEFAULT_CONTENT_TYPE );

    if (editor!=null){
      editor.setTokenScanner( codeScanner );
    }

    return reconciler;
  }

  private ITokenScanner createAlexScanner() {
    RuleBasedScanner scanner = new RuleBasedScanner();
    // Patterns
    WordPatternRule dollarVars = new WordPatternRule(
        KeywordDetector.NO_DIGIT_AT_START_DETECTOR, "$", "",
        tokenByTypes.get( ITokenTypes.PREPROCESSOR_TEXT ) );
    WordPatternRule atVars = new WordPatternRule(
        KeywordDetector.NO_DIGIT_AT_START_DETECTOR, "@", "",
        tokenByTypes.get( ITokenTypes.PREPROCESSOR_TEXT ) );
    PatternRule startCodes = new PatternRule( "<", ">",
        tokenByTypes.get( ITokenTypes.IDENTIFIER_CONSTRUCTOR ), '\\', true );
    PatternRule regexSet = new PatternRule( "[", "]",
        tokenByTypes.get( ITokenTypes.LITERAL_CHAR ), '\\', true );
    PatternRule string = new PatternRule( "\"", "\"",
        tokenByTypes.get( ITokenTypes.LITERAL_STRING ), '\\', true );
    EndOfLineRule comment = new EndOfLineRule( "-- ",
        tokenByTypes.get( ITokenTypes.LITERATE_COMMENT ) );
    // Single words
    WordRule colon = createRuleForToken( ";", ITokenTypes.SYMBOL_RESERVED );
    WordRule pre = createRuleForToken( "^", ITokenTypes.SYMBOL_RESERVED );
    WordRule post = createRuleForToken( "/", ITokenTypes.SYMBOL_RESERVED );
    WordRule empty = createRuleForToken( "$", ITokenTypes.SYMBOL_RESERVED );
    WordRule startRules = createRuleForToken( ":-",
        ITokenTypes.SYMBOL_RESERVED );
    WordRule equals = createRuleForToken( "=", ITokenTypes.SYMBOL_RESERVED );
    WordRule pipe = createRuleForToken( "|", ITokenTypes.SYMBOL_RESERVED );
    WordRule wrapper = createRuleForToken( "%wrapper", ITokenTypes.KEYWORD );

    scanner.setRules( new IRule[] { dollarVars, atVars, startCodes, regexSet,
        string, comment, colon, pre, post, empty, startRules, equals, pipe,
        wrapper } );
    return scanner;
  }
}
