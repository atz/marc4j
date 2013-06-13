package org.marc4j.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


class Normalizer {
    Class<?> normalizerInAvailableJar = null;
    Class<?> normalizerFormClass = null;
    boolean hasJavaTextNormalizer = false;
//  Class normalizerModeInAvailableJar = null;
//  Object normalizerModeNone = null;    
//  Object normalizerModeNFD = null;
//  Object normalizerModeNFC = null;
//  Object normalizerModeNFKD = null;
//  Object normalizerModeNFKC = null;
    Method composeMethod   = null;
    Method decomposeMethod = null;
    Method normalizeMethod = null;
   
    int NONE = 1;
    int NFD  = 2;
    int NFKD = 3;
    int NFC  = 4;
    int NFKC = 5;
    Object NFD_obj  = null;
    Object NFKD_obj = null;
    Object NFC_obj  = null;
    Object NFKC_obj = null;
   
    String normalize(String str, int mode) {
      String result = str
      if (normalizerInAvailableJar == null) initializeNormalizer()

      try {
        if (hasJavaTextNormalizer) {
          switch (mode) {
            case (NFD):
              result = normalizeMethod.invoke(null, str, NFD_obj).toString(); break
            case (NFC):
              result = normalizeMethod.invoke(null, str, NFC_obj).toString(); break
            case (NFKD):
              result = normalizeMethod.invoke(null, str, NFKD_obj).toString(); break
            case (NFKC):
              result = normalizeMethod.invoke(null, str, NFKC_obj).toString(); break
          }
        } else {
          switch (mode) {
            case (NFD):
                result = decomposeMethod.invoke(null, str, false).toString(); break
            case (NFC):
                result = composeMethod.invoke(null, str, false).toString(); break
            case (NFKD):
                result = decomposeMethod.invoke(null, str, true).toString(); break
            case (NFKC):
                result = composeMethod.invoke(null, str, true).toString(); break
          } // default case for each switch is keep result the same (result = str)
        }
      }
      catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e){
        e.printStackTrace();
      }

      return(result);
    }
    
  private static void initializeNormalizer() 
  {
    try
    {
        normalizerInAvailableJar = Class.forName("java.text.Normalizer");
        hasJavaTextNormalizer = true;
        normalizerFormClass = Class.forName("java.text.Normalizer$Form");
//          Method[] normalizeMethods = normalizerInAvailableJar.getMethods();
        normalizeMethod = normalizerInAvailableJar.getMethod("normalize", java.lang.CharSequence.class, normalizerFormClass);
        Method valueOf = normalizerFormClass.getMethod("valueOf", String.class);
        NFD_obj  = valueOf.invoke(null, "NFD");
        NFKD_obj = valueOf.invoke(null, "NFKD");
        NFC_obj  = valueOf.invoke(null, "NFC");
        NFKC_obj = valueOf.invoke(null, "NFKC");
    }
    catch (ClassNotFoundException cnfe)
    {
      try {
        normalizerInAvailableJar = Class.forName("com.ibm.icu.text.Normalizer");
      }
      catch (ClassNotFoundException e) {
        try {
          normalizerInAvailableJar = Class.forName("com.solrmarc.icu.text.Normalizer");
        }
        catch (ClassNotFoundException e1) {
          throw new RuntimeException(e);
        }
      }
      composeMethod   = normalizerInAvailableJar.getMethod("compose",   String.class, boolean.class);
      decomposeMethod = normalizerInAvailableJar.getMethod("decompose", String.class, boolean.class);
    }
    catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
      e.printStackTrace()
    }
  }
}
