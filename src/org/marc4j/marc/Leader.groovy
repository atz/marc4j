/**
 * Copyright (C) 2004 Bas Peters
 *
 * This file is part of MARC4J
 *
 * MARC4J is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public 
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 *
 * MARC4J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with MARC4J; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.marc4j.marc

public class Leader {
  Long id;
  private int    recordLength       // The logical record length (Position  0-4)
  private char   recordStatus       // The record status         (Position  5)
  private char   typeOfRecord       // Type of record            (Position  6)
  private char[] implDefined1       // Implementation defined    (Position  7-8)
  private char   charCodingScheme   // Character coding scheme   (Position  9)
  private int    indicatorCount     // The indicator count       (Position 10)
  private int    subfieldCodeLength // The subfield code length  (Position 11)
  private int    baseAddressOfData  // The base address of data  (Position 12-16)
  private char[] implDefined2       // Implementation defined    (Position 17-18)
  private char[] entryMap           // Entry map                 (Position 19-23)

  /**
   * Creates a new leader from a String object.
   * 
   * @param ldr
   *            the leader string value
   */
  def Leader (String ldr) {
    unmarshal(ldr);
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
      String s;
      s = ldr.substring(0, 5);
      if (s.isInteger())
          setRecordLength(Integer.parseInt(s));
      else
          setRecordLength(0);
      setRecordStatus(ldr.charAt(5));
      setTypeOfRecord(ldr.charAt(6));
      setImplDefined1(ldr.substring(7, 9).toCharArray());
      setCharCodingScheme(ldr.charAt(9));
      s = String.valueOf(ldr.charAt(10));
      if (s.isInteger())
          setIndicatorCount(Integer.parseInt(s));
      else
          setIndicatorCount(2);
      s = String.valueOf(ldr.charAt(10));
      if (s.isInteger())
          setSubfieldCodeLength(Integer.parseInt(s));
      else
          setSubfieldCodeLength(2);
      s = ldr.substring(12, 17);
      if (s.isInteger())
          setBaseAddressOfData(Integer.parseInt(s));
      else
          setBaseAddressOfData(0);
      setImplDefined2(ldr.substring(17, 20).toCharArray())
      setEntryMap(ldr.substring(20, 24).toCharArray())
    } catch (NumberFormatException e) {
      throw new RuntimeException("Unable to parse Leader", e);
    }
  }

  /**
   * Creates a string object from this leader object.
   * 
   * @return String - the string object from this leader object
   */
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

  DecimalFormat format5 = new org.marc4j.util.CustomDecimalFormat(5)
}
