/**
 * Copyright (C) 2004 Bas Peters
 *
 * This file is part of MARC4J licensed unter GNU LGPL 2.1+
 */
package org.marc4g.marc

class Subfield {
  char   code
  String data

  String toString() {
    return '$' + code + (data == null ? '' : data)
  }

  boolean validate() {
    return code != null && data != null
  }
}
