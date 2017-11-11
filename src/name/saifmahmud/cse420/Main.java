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

        Parser parser = new Parser(stream);
        parser.process();
    }
}
