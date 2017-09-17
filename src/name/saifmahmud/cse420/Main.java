package name.saifmahmud.cse420;

import name.saifmahmud.cse420.Lexer.Tokenizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        File file = new File("input.txt");
        FileInputStream stream = new FileInputStream(file);

        Tokenizer tokenizer = new Tokenizer(stream);
        SymbolTable table = tokenizer.process();

        System.out.println("Fetching symbols:");
        System.out.println("\t" + table.toString().replaceAll("\n", "\n\t"));
    }
}
