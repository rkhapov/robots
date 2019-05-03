package robots;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        throw new ClassNotFoundException(e.getMessage());
      }
    }
  }

  private Class<?> loadMyClass(String name) throws IOException {
    if (cache.containsKey(name))
      return cache.get(name);

    var file = new File(name);

    try (var fin = new FileInputStream(file)) {
      var bytes = readFile(fin);
      var className = parseClassName(bytes);
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
        out.write(buffer, 0, iCount);
      else
        break;
    }
    return out.toByteArray();
  }

  private String parseClassName(byte[] classBytes) throws IOException {
    var stream = new DataInputStream(new ByteArrayInputStream(classBytes));

    if (stream.readInt() != 0xCAFEBABE) //check magic
      throw new IOException("Invalid magic number");

    stream.readUnsignedShort(); //read minor, dont need it
    stream.readUnsignedShort(); //read major, dont need it

    var constantPoolCount = stream.readUnsignedShort();
    var constantPool = readConstantPool(stream, constantPoolCount);

    stream.readUnsignedShort(); //read access flags, dont need it

    var thisClass = stream.readUnsignedShort();

    return extractStringConstant(constantPool, thisClass).replace("/", ".");
  }

  private String extractStringConstant(List<byte[]> constantPool, int thisClass) {
    var classInfo = constantPool.get(thisClass - 1);
    var nameIdx = (classInfo[1] >> 8) | classInfo[2];
    var nameConstant = constantPool.get(nameIdx - 1);

    return new String(nameConstant, 3, nameConstant.length - 3);
  }

  //see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4 for more information
  private static final Map<Integer, Integer> tagToConstantSize = Map.ofEntries(
      Map.entry(7, 2),
      Map.entry(9, 4),
      Map.entry(10, 4),
      Map.entry(11, 4),
      Map.entry(8, 2),
      Map.entry(3, 4),
      Map.entry(4, 4),
      Map.entry(5, 8),
      Map.entry(6, 8),
      Map.entry(12, 4),
      Map.entry(15, 3),
      Map.entry(16, 2),
      Map.entry(18, 4)
  );


  private List<byte[]> readConstantPool(DataInputStream stream, int count) throws IOException {
    var constants = new ArrayList<byte[]>();
    constants.ensureCapacity(count);

    for (var i = 1; i <= count - 1; i++) {
      var tag = stream.readUnsignedByte();

      if (tag != 1)
        constants.add(readConstant(stream, tag));
      else //Utf-8 constant
        constants.add(readUtf8Constant(stream));

      //Double or Long constant takes two entries in constant pool
      //see specification for more info
      if (tag == 5 || tag == 6) {
        constants.add(null);
        i++;
      }
    }

    return constants;
  }

  private byte[] readUtf8Constant(DataInputStream stream) throws IOException {
    var length = stream.readUnsignedShort();
    var bytes = new byte[1 + 2 + length]; // tag + length + string bytes
    bytes[0] = 1; //utf-8 constant tag

    bytes[1] = (byte)((length & 0x0000FF00) >> 8);
    bytes[2] = (byte)((length & 0x000000FF));

    var read = stream.read(bytes, 3, length);

    if (read < length)
      throw new IOException(String.format("Invalid bytes amount for tag: %d", 1));

    return bytes;
  }

  private byte[] readConstant(DataInputStream stream, int tag) throws IOException {
    var size = tagToConstantSize.get(tag);

    if (size == null)
      throw new UnsupportedOperationException(String.format("Unknown tag: %d", tag));

    var bytes = new byte[size + 1];
    bytes[0] = (byte)tag;

    var read = stream.read(bytes, 1, size);

    if (read < size)
      throw new IOException(String.format("Invalid bytes amount for tag: %d", tag));

    return bytes;
  }
}
