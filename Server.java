import java.io.*;
import java.util.Scanner;
import java.net.*;

class Server {
  public static void main(String[] args) throws Exception {
    String filename = "";
    String msg2 = "";
    String msg = "";
    String out = "";
    String str2 = "";
    Scanner sc;
    ServerSocket ss = new ServerSocket(6666);
    Socket s = ss.accept();
    System.out.println("connection Established...");
    DataInputStream din = new DataInputStream(s.getInputStream());
    DataOutputStream dout = new DataOutputStream(s.getOutputStream());
    String str = "WELCOME CLIENT! TYPE 'help' FOR MORE INFO";
    dout.writeUTF(str);
    do {
      msg = din.readUTF();
      if (msg.equals("help")) {
        msg2 = "FILE OPERATION COMMANDS \n\t1.create <file_name.txt> \n\t2.edit <file_name.txt> \n\t3.cat <file_name.txt> \n\t4.delete <file_name.txt> \n\t5.exit";
        dout.writeUTF(msg2);
      } else if (msg.startsWith("create")) {
        filename = msg.trim();
        filename = filename.substring(7);
        out = filename;
        File fin = new File(filename);
        if (fin.createNewFile()) {
          out = "FILE " + fin.getName() + " CREATED SUCCESSFULLY";
        } else {
          out = "FILE ALREADY EXISTS";
        }
        dout.writeUTF(out);
        str2 = "hello world";
        FileWriter f = new FileWriter(filename, true);
        f.write(str2);
        f.close();
      } else if (msg.startsWith("cat")) {
        try {
          filename = msg.trim();
          filename = filename.substring(4);
          File f = new File(filename);
          sc = new Scanner(f);
          out = "";
          while (sc.hasNextLine()) {
            String data = sc.nextLine();
            out = out + "\n" + data;
          }
          dout.writeUTF(out);
          sc.close();
        } catch (Exception e) {
          out = "FILE DOESN'T EXISTS!";
          dout.writeUTF(out);
        }
      } else if (msg.startsWith("edit")) {
        try {
          filename = msg.trim();
          filename = filename.substring(5);
          FileWriter f = new FileWriter(filename, true);
          dout.writeUTF("ENTER THE STRING TO APPEND:");
          out = din.readUTF();
          f.write(" " + out + "\n");
          f.close();
          out = "SUCCESSFULLY WROTE TO THE FILE";
          dout.writeUTF(out);
        } catch (Exception e) {
          out = "FILE DOESN'T EXISTS!";
          dout.writeUTF(out);
        }
      } else if (msg.startsWith("delete")) {
        try {
          filename = msg.trim();
          filename = filename.substring(7);
          File f = new File(filename);
          if (f.delete()) {
            out = "FILE " + f.getName() + " DELETED SUCCESSFULLY!";
          } else {
            out = "FAILED TO DELETE THE FILE!";
          }
          dout.writeUTF(out);
        } catch (Exception e) {
          out = "FILE DOESN'T EXISTS!";
          dout.writeUTF(out);
        }
      } else {
        out = "INVALID COMMAND RECEIVED!";
        dout.writeUTF(out);
      }
    } while (!msg.equals("exit"));
    ss.close();
    s.close();
  }
}