
package org.marc4j
import org.marc4j.marc

public class MarcBuilder extends BuidlerSupport {
  factory = new MarcFactory()

  def createNode(Object name, Map attributes=null, Object value=null) {
    println "createNode(${name}, ${attributes}, ${value})"
  }

  void setParent(parent, child){
    parent.addChild(child)
  }
}
