/**
 * Copyright (C) 2004 Bas Peters
 *
 * This file is part of MARC4J licensed unter GNU LGPL 2.1+
 */
package org.marc4g.marc

import org.marc4g.MarcException
import org.marc4g.marc.*

class MarcFactory {
  ControlField newControlField(String tag=null, String data=null) {
    return new ControlField('tag':tag, 'data':data)
  }
  DataField newDataField() {
    return new DataField()
  }
  DataField newDataField(String tag, char ind1, char ind2) {
    return new DataField(tag, ind1, ind2)
  }
  DataField newDataField(String tag, char ind1, char ind2, String ... pairs) {
    df = new DataField(tag, ind1, ind2);
    if (pairs.length % 2 == 1)
    {
      throw new MarcException("Error: must provide even number of parameters for subfields: code, data, code, data, ...")
    }
    for (int i = 0; i < pairs.length; i += 2) {
      if (pairs[i].length() != 1) {
        throw new MarcException("Error: subfieldCode must be a single character")
      }
      df.addSubfield(newSubfield(pairs[i].charAt(0), pairs[i+1]))
    }
    return(df)
  }

  Leader newLeader(String ldr=null) {
    return (ldr == null ? new Leader() : new Leader(ldr))
  }
  Subfield newSubfield(char code=null, String data=null) {
    return new Subfield('code':code, 'data':data)
  }
  Record newRecord() {
    return newRecord(default_leader)
  }
  Record newRecord(Leader leader) {
    return new Record(leader)
  }
  Record newRecord(String leadertext) {
    return newRecord(new Leader(leadertext))
  }

  def default_leader = new Leader("00000nam a2200000 a 4500")
}
