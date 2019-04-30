package robots;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RobotClassLoader extends ClassLoader {

  private final Map<String, Class<?>> cache;

  public RobotClassLoader() {
    cache = new HashMap<>();
  }

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    try {
      return super.findClass(name);
    } catch (ClassNotFoundException ex) {
      try {
        return loadMyClass(name);
      } catch (IOException e) {
        throw new ClassNotFoundException(name);
      }
    }
  }

  private Class<?> loadMyClass(String name) throws IOException {
    if (cache.containsKey(name))
      return cache.get(name);

    var file = new File(name);

    try (var fin = new FileInputStream(file)) {
      var bytes = readFile(fin);
      var className = file.getName().replaceFirst("[.][^.]+$", "");
      var class_ = super.defineClass(className, bytes,0, bytes.length);

      cache.put(name, class_);

      return class_;
    }
  }

  private byte[] readFile(FileInputStream fin) throws IOException {
    var bin = new BufferedInputStream(fin);
    var out = new ByteArrayOutputStream();
    var buffer = new byte[8192];
    while (true)
    {
      var iCount = bin.read(buffer);
      if (iCount >= 0)
      {
        out.write(buffer, 0, iCount);
      }
      else
      {
        break;
      }
    }
    return out.toByteArray();
  }
}
