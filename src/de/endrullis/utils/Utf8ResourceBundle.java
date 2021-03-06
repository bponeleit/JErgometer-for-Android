package de.endrullis.utils;

/**
 * This is a ResourceBundle implementation using UTF-8, because Sun's is not able to implement it.
 * I found this code on this website, posted by a user named "thoughts":
 * http://www.thoughtsabout.net/blog/archives/000044.html
 */
import de.poneleit.jergometer.R;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public abstract class Utf8ResourceBundle {

  public static ResourceBundle getBundle(String baseName) {
    ResourceBundle bundle = ResourceBundle.getBundle(baseName);
    return createUtf8PropertyResourceBundle(bundle);
  }

  public static ResourceBundle getBundle(String baseName, Locale locale) {
    ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale);
    return createUtf8PropertyResourceBundle(bundle);
  }

  public static ResourceBundle getBundle(String baseName, Locale locale, ClassLoader loader) {
    ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale, loader);
    return createUtf8PropertyResourceBundle(bundle);
  }

  private static ResourceBundle createUtf8PropertyResourceBundle(ResourceBundle bundle) {
    if (!(bundle instanceof PropertyResourceBundle)) return bundle;

    return new Utf8PropertyResourceBundle((PropertyResourceBundle)bundle);
  }



// inner classes

  private static class Utf8PropertyResourceBundle extends ResourceBundle {
    PropertyResourceBundle bundle;

    private Utf8PropertyResourceBundle(PropertyResourceBundle bundle) {
      this.bundle = bundle;
    }
    /* (non-Javadoc)
     * @see java.util.ResourceBundle#getKeys()
     */
    public Enumeration<String> getKeys() {
      return bundle.getKeys();
    }
    /* (non-Javadoc)
     * @see java.util.ResourceBundle#handleGetObject(java.lang.String)
     */
    protected Object handleGetObject(String key) {
      String value = bundle.getString(key);
      if (value==null) return null;
      try {
         return new String (value.getBytes("ISO-8859-1"),"UTF-8") ;
      } catch (UnsupportedEncodingException e) {
        // Shouldn't fail - but should we still add logging message?
        return null;
      }
    }
  }
}
