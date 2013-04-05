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
import org.marc4j.marc.Subfield

class DataField extends Field {
  char ind1
  char ind2
  subfields = []

  def DataField(String tag, String ind1, String ind2) {
    DataField(tag, ind1 as char, ind2 as char)
  }
  def DataField(String tag, char ind1, String ind2) {
    DataField(tag, ind1, ind2 as char)
  }
  def DataField(String tag, String ind1, char ind2) {
    DataField(tag, ind1 as char, ind2)
  }

	def getSubfields(String code) { getSubfields(code as char) }
	def getSubfields(char   code) {
    subfields.grep{it.code == code}
  }
	def findSubfield(String code) { findSubfield(code as char) }
	def findSubfield(char   code) {
    subfields.find{it.code == code}
  }

	def addSubfield(Subfield subfield) {
    subfields.add(subfield)
  }
	def addSubfield(int index, Subfield subfield) {
    subfields[index..<index] = subfield
  }
	def addSubfields(Collection more) {
    subfields << more
  }
	def addSubfields(int index, Collection more) {
    subfields[index..<index] = more
  }
  def removeSubfield(Subfield subfield) {
    subfields.remove(subfield);
  }
  String toString() {
    return this.tag + ' ' + \
    (this.ind1 == null ? '#' : this.ind1) + \
    (this.ind2 == null ? '#' : this.ind2) + \
    subfields.collect{it.toString()}.join()
  }
  boolean validate() {
    for (sub in subfields) {
      if (!s.validate()) return false
    }
    return validateTag() && (!isControlField())
  }
}

/**

replace: public List<Subfield> getSubfields(char code)
with:
field.subfields.grep{it.code == code}

replace: public Subfield getSubfield(char code)
with: 
field.subfields.find{it.code == code}

replace: public boolean find(String pattern)
with:
field.subfields.find{it.code == code}

*/

