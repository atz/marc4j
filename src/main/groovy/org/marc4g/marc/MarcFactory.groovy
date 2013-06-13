/**
 * Copyright (C) 2004 Bas Peters
 *
 * This file is part of MARC4J licensed unter GNU LGPL 2.1+
 */
package org.marc4g.marc

import java.util.Properties
import org.marc4g.MarcException
import org.marc4g.marc.*

/**
 * Factory for creating MARC record objects.
 * <p/>
 * You can use <code>MarcFactory</code> to create records from scratch:
 * <p/>
 * <pre>
 *  import org.marc4g.MarcFactory
 *
 *  factory = MarcFactory.newInstance()
 *  Record record = factory.newRecord();
 *  ControlField cf = factory.newControlField(&quot;001&quot;);
 *  record.addField(cf);
 *  etc...
 *
 * </pre>
 *
 * @author Bas Peters
 */
class MarcFactory {
  String className = null
  Properties props = null      // cache props to avoid touching filesystem every time
  def default_leader = new Leader("00000nam a2200000 a 4500")

  /**
   * Creates a new factory instance. The implementation class to load is the
   * first found in the following locations:
   * <ol>
   * <li>the <code>org.marc4j.marc.MarcFactory</code> system property</li>
   * <li>the above named property value in the
   * <code><i>$JAVA_HOME</i>/lib/marc4j.properties</code> file</li>
   * <li>the class name specified in the
   * <code>META-INF/services/org.marc4j.marc.MarcFactory</code> system
   * resource</li>
   * <li>the default factory class</li>
   * </ol>
   */
  MarcFactory newInstance() {
    ClassLoader loader = Thread.currentThread().getContextClassLoader() ?: MarcFactory.class.getClassLoader()
    int count = 0
    do {
      className = getFactoryClassName(loader, count++)
      if (className != null) {
        try {
          Class<?> t = (loader != null) ? loader.loadClass(className) : Class.forName(className)
          return (MarcFactory) t.newInstance()
        } catch (ClassNotFoundException e) {
          className = null
        } catch (Exception e) {
        }
      }
    } while (className == null && count < 3)
    return new org.marc4g.marc.MarcFactory()
  }

  private static String getFactoryClassName(ClassLoader loader, int attempt) {
      final String propertyName = "org.marc4j.marc.MarcFactory"
      try {
        switch (attempt) {
          case 0:
              return System.getProperty(propertyName)
          case 1:
              if (props == null) {
                file  = new File(System.getProperty("java.home"), "lib")
                ins   = new FileInputStream(new File(file, "marc4j.properties"))
                props = new Properties()
                props.load(ins)
                ins.close()
              }
              return props.getProperty(propertyName)
          case 2:
              String serviceKey = "/META-INF/services/" + propertyName
              in = (loader != null) ? loader.getResourceAsStream(serviceKey) : MarcFactory.class.getResourceAsStream(serviceKey)
              if (in != null) {
                r = new BufferedReader(new InputStreamReader(in))
                ret = r.readLine()
                r.close()
                return ret
              }
        }
      } catch (IOException e) {}
      return null
  }

  ControlField newControlField(String tag, String data=null) {
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
      if (pairs[i].length() != 1) throw new MarcException("Error: subfieldCode must be a single character")
      df.addSubfield(newSubfield(pairs[i].charAt(0), pairs[i+1]))
    }
    return(df)
  }

  Leader newLeader(String ldr=null) {
    return (ldr == null ? new Leader() : new Leader(ldr))
  }
  Subfield newSubfield(char code, String data=null) {
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

}

