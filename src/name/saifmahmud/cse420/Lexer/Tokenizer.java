package name.saifmahmud.cse420.Lexer;

import name.saifmahmud.cse420.SymbolTable;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer
{
    private FileInputStream stream;

    public Tokenizer(FileInputStream stream)
    {
        this.stream = stream;
    }

    public SymbolTable process() throws IOException
    {
        SymbolTable table = new SymbolTable();
        StringBuilder sb = new StringBuilder();

        int ch;
        boolean rewind = true;
        Token last = null;

        while ((ch = stream.read()) > 0)
        {
            sb.append((char) ch);
            Token cur = getMatchedToken(sb.toString());

            if (cur == null)
            {
                if (last != null)
                {
                    table.add(last);
                }

                sb = new StringBuilder();

                if (rewind)
                {
                    stream.getChannel().position(stream.getChannel().position() - 1);
                    rewind = false;
                }
            }
            else
            {
                last = cur;
                rewind = true;
            }
        }

        return table;
    }

    private Token getMatchedToken(String str)
    {
        for (Token.Type type : Token.Type.values())
        {
            Pattern p = Pattern.compile(type.getPattern(), Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(str);

            if (m.matches())
            {
                return new Token(type, str);
            }
        }

        return null;
    }
}
