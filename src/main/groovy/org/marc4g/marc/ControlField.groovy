/**
 * Copyright (C) 2004 Bas Peters
 *
 * This file is part of MARC4J licensed unter GNU LGPL 2.1+
 */
package org.marc4g.marc

import org.marc4g.marc.Field

class ControlField extends Field {
  String data
  String toString() {
    return tag + " " + data
  }
}
