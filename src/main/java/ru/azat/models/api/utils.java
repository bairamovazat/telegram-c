package ru.azat.models.api;


import org.telegram.bot.kernel.engine.MemoryApiState;
import org.telegram.tl.TLBytes;
import ru.azat.models.api.engine.MyApiState;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class utils {
    private static String urlToBasePath = "src/main/resources/";

    public static void main(String[] args) throws IOException {
        MyApiState apiState = new MyApiState("127.0.0.1");
        serializeOnFile("apiState" + System.currentTimeMillis() / 60000, apiState);
    }

    public static String EncodingMD5(String string) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(string.getBytes());
        byte[] digest = md.digest();
        String myHash = DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        return myHash;
    }

    public static byte[] stringToByteArray(String str) {
        return DatatypeConverter.parseBase64Binary(str);
    }

    public static String byteArrayToString(byte[] byteArray) {
        return DatatypeConverter.printBase64Binary(byteArray);
    }

    public static void serializeOnFile(String fileName, MyApiState apiState) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/main/resources/" + fileName));
        oos.writeObject(apiState);
    }

    public static MyApiState deserializeInFile(String fileName) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("src/main/resources/" + fileName));
        return (MyApiState) ois.readObject();
    }

    public static void write(String fileName, byte[] bytes) throws IOException {
        FileOutputStream fos = new FileOutputStream(new File("src/main/resources/" + fileName));
        fos.write(bytes);
    }

    public static InputStream getInputStream(String fileName) throws FileNotFoundException {
        File initialFile = new File(urlToBasePath + fileName);
        return new FileInputStream(initialFile);
    }

    public static byte[] read(String fileName) throws IOException {
        Path path = Paths.get("src/main/resources/" + fileName);
        byte[] data = Files.readAllBytes(path);
        return data;
    }

    public static byte[] serialize(TLBytes bytes) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(bytes);
            out.flush();
            byte[] yourBytes = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return new byte[]{};
    }

    public static Object deserialize(byte[] bytes) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            Object o = in.readObject();
            return o;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return new Object();
    }
}

