/**
 * Copyright (C) 2004 Bas Peters (mail@bpeters.com)
 *
 * This file is part of MARC4J licensed unter GNU LGPL 2.1+
 */
package org.marc4j.marc

class IllegalAddException extends IllegalArgumentException {
  def IllegalAddException(String className) {
    super("The addition of the object of type ${className} is not allowed.")
  }
  def IllegalAddException(String className, String reason) {
    super("The addition of the object of type ${className} is not allowed: $reason.")
  }
}
