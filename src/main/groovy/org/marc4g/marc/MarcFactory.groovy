/**
 * Copyright (C) 2004 Bas Peters
 *
 * This file is part of MARC4J licensed unter GNU LGPL 2.1+
 */
package org.marc4g.marc

import java.util.Properties

/**
 * Factory for creating MARC record objects.
 * <p/>
 * You can use <code>MarcFactory</code> to create records from scratch:
 * <p/>
 * <pre>
 *
 *  MarcFactory factory = MarcFactory.newInstance();
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
  def MarcFactory newInstance() {
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
      return new org.marc4j.marc.MarcFactory()
  }

  private static String getFactoryClassName(ClassLoader loader, int attempt) {
      final String propertyName = "org.marc4j.marc.MarcFactory"
      try {
        switch (attempt) {
          case 0:
              return System.getProperty(propertyName)
          case 1:
              if (props == null) {
                file = new File(System.getProperty("java.home"), "lib")
                ins = new FileInputStream(new File(file, "marc4j.properties"))
                props = new Properties()
                props.load(ins)
                ins.close()
              }
              return props.getProperty(propertyName)
          case 2:
              String serviceKey = "/META-INF/services/" + propertyName
              InputStream in = (loader != null) ? loader.getResourceAsStream(serviceKey) : MarcFactory.class.getResourceAsStream(serviceKey)
              if (in != null) {
                BufferedReader r = new BufferedReader(new InputStreamReader(in))
                String ret = r.readLine()
                r.close()
                return ret
              }
        }
      } catch (IOException e) {}
      return null
  }

  abstract ControlField newControlField()
  abstract ControlField newControlField(String tag)
  abstract ControlField newControlField(String tag, String data)
  abstract DataField newDataField()
  abstract DataField newDataField(String tag, char ind1, char ind2)
  abstract DataField newDataField(String tag, char ind1, char ind2, String... subfieldCodesAndData)
  abstract Leader newLeader()
  abstract Leader newLeader(String ldr)
  abstract Record newRecord()
  abstract Record newRecord(Leader leader)
  abstract Record newRecord(String leader)
  abstract Subfield newSubfield()
  abstract Subfield newSubfield(char code)
  abstract Subfield newSubfield(char code, String data)

  boolean validateRecord(x) {
    return x.validate()
  }
  boolean validateControlField(x) {
    return x.validate()
  }
  boolean validateDataField(x) {
    return x.validate()
  }
  boolean validateSubField(x) {
    return x.validate()
  }

}
