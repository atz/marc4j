/**
 * Copyright (C) 2004 Bas Peters
 *
 * This file is part of MARC4J licensed unter GNU LGPL 2.1+
 *
 */
package org.marc4j.marc
import org.marc4j.marc.Leader
import org.marc4j.marc.Field
import org.marc4j.marc.Subfield

/**
 * Represents a MARC record.
 *
 */
class Record {
    Long   id
    Leader leader
    def fields = []   // some control fields are repeatable (006/007)
    String type

    def addChild(child) {   // for BuilderSupport
      if (child in org.marc4j.marc.Leader) {
        leader = child
      } else {
        addField(child)
      }
    }
    def addField(field) {
      def tag = field.tag
      if (field.isControlField()) {
        if (tag == "001" || tag == "003" || tag == "005") {  // not repeatable!
          fields = fields.grep{ it != field }
        }
      }
      fields << field
    }

    def removeField(field) {
      fields.remove(field)
    }

    /**
     * Returns the control number field or <code>null</code> if no control
     * number field is available.
     *
     * @return ControlField - the control number field
     */
    def getControlField(String tag = "001") {
      return fields.grep{ it.tag == tag }?.first()
    }
    def getControlNumber() {
      return getControlField()?.data
    }

    def getField(String tag) {
      return fields[tag] ?: dataFields.grep{ it.tag == tag }?.first() ?: null
    }

    def getFields(String tag) { getFields([tag]) }
    def getFields(List tags=[]) {
      if (tags.size == 0) {
        return fields.plus(dataFields).sort{a,b -> a.tag <=> b.tag}
      }
      return fields.findAll{it.tag in tags}.plus(
        dataFields.grep{it.tag in tags}
      ).sort{a,b -> a.tag <=> b.tag}
    }

    /**
     * Returns a string representation of this record.
     * <p/>
     * <p/>
     * Example:
     * <p/>
     * <pre>
     *
     *      LEADER 00714cam a2200205 a 4500
     *      001 12883376
     *      005 20030616111422.0
     *      008 020805s2002 nyu j 000 1 eng
     *      020   $a0786808772
     *      020   $a0786816155 (pbk.)
     *      040   $aDLC$cDLC$dDLC
     *      100 1 $aChabon, Michael.
     *      245 10$aSummerland /$cMichael Chabon.
     *      250   $a1st ed.
     *      260   $aNew York :$bMiramax Books/Hyperion Books for Children,$cc2002.
     *      300   $a500 p. ;$c22 cm.
     *      650  1$aFantasy.
     *      650  1$aBaseball$vFiction.
     *      650  1$aMagic$vFiction.
     *
     * </pre>
     *
     * @return String - a string representation of this record
     */
    String toString() {
      return "LEADER " + this.leader.toString() + "\n" + this.getFields().collect(it.toString() + '\n').join()
    }
    String toXML() {

    }
    def toBinary() {
      
    }
}

/* 

Example migration:
OLD method:  List<VariableField> getVariableFields(String[] tags)
Replace with:
  record.getFields().grep{ it.tag in tags }

Replace List<VariableField> find(String pattern)
with:
record.getFields.grep{ it.value =~ /pattern/ }

Replace List<VariableField> find(String tag, String pattern)
with:
record.getFields.grep{ it.tag == tag && it.value =~ /pattern/ }

Replace List<VariableField> find(String[] tags, String pattern)
with:
record.getFields.grep{ it.tag in tags && it.value =~ /pattern/ }

*/
