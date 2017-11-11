package name.saifmahmud.cse420.Regex;

import name.saifmahmud.cse420.Regex.Expression.Matcher;
import name.saifmahmud.cse420.Regex.Expression.Pattern;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Processor
{
    private FileInputStream stream;

    public Processor(FileInputStream stream)
    {
        this.stream = stream;
    }

    public void process()
    {
        try
        {
            int id = 1;
            Scanner sc = new Scanner(stream);

            while (sc.hasNext())
            {
                System.out.println("Input #" + id++ + ":");

                LinkedList<Pattern> patterns = new LinkedList<>();

                int patternCount = sc.nextInt();
                sc.nextLine();

                for (int i = 0; i < patternCount; i++)
                {
                    patterns.add(new Pattern(sc.nextLine().trim()));
                }

                int textCount = sc.nextInt();
                sc.nextLine();

                for (int i = 0; i < textCount; i++)
                {
                    int n = validateString(sc.nextLine().trim(), patterns);
                    if (n > -1)
                    {
                        System.out.println("YES," + ++n);
                    }
                    else
                    {
                        System.out.println("NO,0");
                    }
                }

                System.out.println();
            }
        }
        catch (Exception ignore) { }
    }

    private int validateString(String str, List<Pattern> patterns)
    {
        for (int i = 0; i < patterns.size(); i++)
        {
            Matcher matcher = new Matcher(patterns.get(i));

            if (matcher.match(str))
            {
                return i;
            }
        }

        return -1;
    }
}
