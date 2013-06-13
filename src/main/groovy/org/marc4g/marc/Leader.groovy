/**
 * Copyright (C) 2004 Bas Peters
 *
 * This file is part of MARC4J licensed unter GNU LGPL 2.1+
 */
package org.marc4g.marc

class Leader {
  int    recordLength       // The logical record length (Position  0-4)
  char   recordStatus       // The record status         (Position  5)
  char   typeOfRecord       // Type of record            (Position  6)
  char[] implDefined1       // Implementation defined    (Position  7-8)
  char   charCodingScheme   // Character coding scheme   (Position  9)
  int    indicatorCount     // The indicator count       (Position 10)
  int    subfieldCodeLength // The subfield code length  (Position 11)
  int    baseAddressOfData  // The base address of data  (Position 12-16)
  char[] implDefined2       // Implementation defined    (Position 17-18)
  char[] entryMap           // Entry map                 (Position 19-23)

  def format5 = new org.marc4j.util.CustomDecimalFormat(5)
  /**
   * Creates a new leader from a String object.
   * 
   * @param ldr
   *            the leader string value
   */
  def Leader (String ldr) {
    unmarshal(ldr)
  }

  /**
   * <p>
   * Creates a leader object from a string object.
   * </p>
   * 
   * <p>
   * Indicator count and subfield code length are defaulted to 2 if they are
   * not integer values.
   * </p>
   * 
   * @param ldr
   *            the leader
   */
  def unmarshal(String ldr) {
    try {
      s = ldr.substring(0, 5)
      recordLength = s.isInteger() ? Integer.parseInt(s) : 0
      recordStatus = ldr.charAt(5)
      typeOfRecord = ldr.charAt(6)
      implDefined1 = ldr.substring(7, 9).toCharArray()
      charCodingScheme = ldr.charAt(9)
      s = String.valueOf(ldr.charAt(10))
      indicatorCount     = s.isInteger() ? Integer.parseInt(s) : 2
      s = String.valueOf(ldr.charAt(11))
      subfieldCodeLength = s.isInteger() ? Integer.parseInt(s) : 2
      s = ldr.substring(12, 17)
      baseAddressOfData  = s.isInteger() ? Integer.parseInt(s) : 0
      implDefined2       = ldr.substring(17, 20).toCharArray()
      entryMap           = ldr.substring(20, 24).toCharArray()
    } catch (NumberFormatException e) {
      throw new RuntimeException("Unable to parse Leader", e);
    }
  }

  String marshal() {
    return this.toString()
  }

  /**
   * Returns a string representation of this leader.
   * 
   * <p>
   * Example:
   * 
   * <pre>
   *  00714cam a2200205 a 4500
   * </pre>
   */
  String toString() {
    return format5.format(getRecordLength()) + \
             getRecordStatus()        + \
             getTypeOfRecord()        + \
             getImplDefined1()        + \
             getCharCodingScheme()    + \
             getIndicatorCount()      + \
             getSubfieldCodeLength()  + \
             format5.format(getBaseAddressOfData()) + \
             getImplDefined2()        + \
             getEntryMap()
  }
}

