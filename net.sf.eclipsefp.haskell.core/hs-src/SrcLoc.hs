module SrcLoc where

import Marshal

data SrcLoc = SrcLoc 
   { line :: Int
   , column :: Int      	        
   }

   
instance Marshal SrcLoc where
    marshal s = marshal ( line s ) ++ marshal ( column s )
    
instance UnMarshal SrcLoc where
    unmarshal_partial xs0 = 
        let ( l, xs1 ) = unmarshal_partial xs0
            ( c, xs2 ) = unmarshal_partial xs1
        in  ( SrcLoc { line = l, column = c } , xs2 )
        
