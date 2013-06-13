/**
 * Copyright (C) 2004 Bas Peters
 *
 * This file is part of MARC4J licensed unter GNU LGPL 2.1+
 */
package org.marc4g.marc

import org.marc4g.marc.ControlField

/**
 * Handles MARC checks on tags, data elements and <code>Record</code> objects.
 */
class Verifier {

  /**
   * Returns true if the given <code>String</code> value identifies a tag for
   * a control field (001 through 009).
   */
  boolean isControlField(String tag) {
    return tag =~ /^00\d$/
  }

  /**
   * Returns true if the given <code>String</code> value identifies a tag for
   * a control number field (001).
   */
  boolean isControlNumberField(String tag){
    return tag == "001"
  }
  /**
   * Returns true if the given <code>Collection</code> contains an instance of
   * a <code>ControlField</code> with a control number field tag (001).
   * 
   * @param col
   *          the collection of <code>ControlField</code> objects.
   */
  boolean hasControlNumberField(col) {
    return (col.grep{it.tag =~ /^00\d$/}.size ? true : false)
  }

}
