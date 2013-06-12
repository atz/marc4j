/**
 * Copyright (C) 2004 Bas Peters
 *
 * This file is part of MARC4J licensed unter GNU LGPL 2.1+
 */
package org.marc4j.marc

class Field {
  String tag

  int compareTo(Field obj) {
    return tag <=> obj.tag
  }

  String toString() {
    return tag
  }
  boolean isControlField() {tag =~ /^00[1-9]$/}
  boolean validateTag()    {tag != null && tag =~ /^\d\d\d$/}
  boolean validate() {
    return validateTag()
  }
}
