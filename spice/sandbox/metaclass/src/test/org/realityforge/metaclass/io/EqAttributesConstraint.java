package org.realityforge.metaclass.io;

import com.mockobjects.constraint.Constraint;
import org.xml.sax.helpers.AttributesImpl;

class EqAttributesConstraint
   implements Constraint
{
   private final AttributesImpl _attributes;

   EqAttributesConstraint( final AttributesImpl attributes )
   {
      _attributes = attributes;
   }

   public boolean eval( Object object )
   {
      if ( !( object instanceof AttributesImpl ) )
      {
         return false;
      }
      final AttributesImpl other = (AttributesImpl) object;
      final int length = _attributes.getLength();
      if ( other.getLength() != length )
      {
         return false;
      }
      for ( int i = 0; i < length; i++ )
      {
         final String qName = _attributes.getQName( i );
         final String value = _attributes.getValue( qName );
         final String otherValue = other.getValue( qName );
         if ( !value.equals( otherValue ) )
         {
            return false;
         }
      }
      return true;
   }
}
